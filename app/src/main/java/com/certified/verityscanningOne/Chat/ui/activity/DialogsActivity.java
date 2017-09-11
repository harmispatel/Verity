package com.certified.verityscanningOne.Chat.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.certified.verityscanningOne.BaseClass;
import com.certified.verityscanningOne.Chat.managers.DialogsManager;
import com.certified.verityscanningOne.Chat.ui.adapter.DialogsAdapter;
import com.certified.verityscanningOne.Chat.utils.chat.ChatHelper;
import com.certified.verityscanningOne.Chat.utils.qb.QbChatDialogMessageListenerImp;
import com.certified.verityscanningOne.Chat.utils.qb.QbDialogHolder;
import com.certified.verityscanningOne.Chat.utils.qb.callback.QbEntityCallbackImpl;
import com.certified.verityscanningOne.R;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;



public class DialogsActivity extends BaseActivity implements DialogsManager.ManagingDialogsCallbacks {
    private static final String TAG = DialogsActivity.class.getSimpleName();
    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;
    private static final int REQUEST_SELECT_PEOPLE = 174;
    private ProgressBar progressBar;
    private ActionMode currentActionMode;
    private SwipyRefreshLayout setOnRefreshListener;
    private QBRequestGetBuilder requestBuilder;
    private int skipRecords = 0;
    private boolean isProcessingResultInProgress;
    private Menu menu;
    private BroadcastReceiver pushBroadcastReceiver;
    private GooglePlayServicesHelper googlePlayServicesHelper;
    private DialogsAdapter dialogsAdapter;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;
    private QBUser currentUser;

    public static void start(Context context) {
        Intent intent = new Intent(context, DialogsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);

        googlePlayServicesHelper = new GooglePlayServicesHelper();

        pushBroadcastReceiver = new PushBroadcastReceiver();

        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();
        dialogsManager = new DialogsManager();

        currentUser = SharedPrefsHelper.getInstance().getQbUser();

        loginToChat(currentUser);

        initUi();
        setActionBarTitle(getString(R.string.dialogs_logged_in_as, currentUser.getFullName()));
        actionBar.setDisplayHomeAsUpEnabled(true);


        if (QbDialogHolder.getInstance().getDialogs().size() > 0) {
            loadDialogsFromQb(true, true);
        } else {
            loadDialogsFromQb(false, true);
        }
    }

    private void loginToChat(final QBUser user) {

        try {
            ChatHelper.getInstance().login(user, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void result, Bundle bundle) {

                    try {
                        SharedPrefsHelper.getInstance().saveQbUser(user);
                        registerQbChatListeners();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            googlePlayServicesHelper.checkPlayServicesAvailable(this);

            LocalBroadcastManager.getInstance(this).registerReceiver(pushBroadcastReceiver,
                    new IntentFilter(GcmConsts.ACTION_NEW_GCM_EVENT));
            loadDialogsFromQb(true, true);

            BaseClass.unregisterQbChatListeners();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(pushBroadcastReceiver);
            BaseClass.registerQbChatListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            BaseClass.registerQbChatListeners();


        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterQbChatListeners();
        BaseClass.registerQbChatListeners();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dialogs, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isProcessingResultInProgress) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.menu_dialogs_action_logout:
                SelectUsersActivity.startForResult(this, REQUEST_SELECT_PEOPLE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            isProcessingResultInProgress = true;
            if (requestCode == REQUEST_SELECT_PEOPLE) {
                ArrayList<QBUser> selectedUsers = (ArrayList<QBUser>) data
                        .getSerializableExtra(SelectUsersActivity.EXTRA_QB_USERS);

                if (isPrivateDialogExist(selectedUsers)) {
                    selectedUsers.remove(ChatHelper.getCurrentUser());
                    QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(selectedUsers.get(0));
                    isProcessingResultInProgress = false;
                    ChatActivity.startForResult(DialogsActivity.this, REQUEST_DIALOG_ID_FOR_UPDATE, existingPrivateDialog,"0");

                } else {
                    ProgressDialogFragment.show(getSupportFragmentManager(), R.string.create_chat);
                    createDialog(selectedUsers);
                }
            } else if (requestCode == REQUEST_DIALOG_ID_FOR_UPDATE) {
                if (data != null) {
                    String dialogId = data.getStringExtra(ChatActivity.EXTRA_DIALOG_ID);
                    loadUpdatedDialog(dialogId);
                } else {
                    isProcessingResultInProgress = false;
                    updateDialogsList();
                }
            }
        } else {
            updateDialogsAdapter();
        }
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(ChatHelper.getCurrentUser());
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }

    private void loadUpdatedDialog(String dialogId) {
        ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle bundle) {
                isProcessingResultInProgress = false;
                QbDialogHolder.getInstance().addDialog(result);
                updateDialogsAdapter();
            }

            @Override
            public void onError(QBResponseException e) {
                isProcessingResultInProgress = false;
            }
        });
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.layout_root);
    }

    @Override
    public ActionMode startSupportActionMode(ActionMode.Callback callback) {
        currentActionMode = super.startSupportActionMode(callback);
        return currentActionMode;
    }


    private void updateDialogsList() {
        requestBuilder.setSkip(skipRecords = 0);
        loadDialogsFromQb(true, true);
    }


    private void initUi() {
        try {
            ListView dialogsListView = _findViewById(R.id.list_dialogs_chats);
            progressBar = _findViewById(R.id.progress_dialogs);
            setOnRefreshListener = _findViewById(R.id.swipy_refresh_layout);

            dialogsAdapter = new DialogsAdapter(this, new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values()));


            dialogsListView.setAdapter(dialogsAdapter);

            dialogsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    QBChatDialog selectedDialog = (QBChatDialog) parent.getItemAtPosition(position);

                    if (currentActionMode == null) {
                        ChatActivity.startForResult(DialogsActivity.this, REQUEST_DIALOG_ID_FOR_UPDATE, selectedDialog,"0");
                    } else {
                        dialogsAdapter.toggleSelection(selectedDialog);
                    }
                }
            });

            requestBuilder = new QBRequestGetBuilder();

            setOnRefreshListener.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {
                    requestBuilder.setSkip(skipRecords += ChatHelper.DIALOG_ITEMS_PER_PAGE);
                    loadDialogsFromQb(true, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerQbChatListeners() {
        try {
            incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
            systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

            if (incomingMessagesManager != null) {
                incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                        ? allDialogsMessagesListener : new AllDialogsMessageListener());
            }

            if (systemMessagesManager != null) {
                systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                        ? systemMessagesListener : new SystemMessagesListener());
            }

            dialogsManager.addManagingDialogsCallbackListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterQbChatListeners() {
        try {
            if (incomingMessagesManager != null) {
                incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
            }

            if (systemMessagesManager != null) {
                systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
            }

            dialogsManager.removeManagingDialogsCallbackListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        isProcessingResultInProgress = false;
                        dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                        ChatActivity.startForResult(DialogsActivity.this, REQUEST_DIALOG_ID_FOR_UPDATE, dialog,"0");
                        ProgressDialogFragment.hide(getSupportFragmentManager());
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        isProcessingResultInProgress = false;
                        ProgressDialogFragment.hide(getSupportFragmentManager());
                        showErrorSnackbar(R.string.dialogs_creation_error, null, null);
                    }
                }
        );
    }

    private void loadDialogsFromQb(final boolean silentUpdate, final boolean clearDialogHolder) {
        isProcessingResultInProgress = true;
        if (!silentUpdate) {
            progressBar.setVisibility(View.VISIBLE);
        }

        ChatHelper.getInstance().getDialogs(requestBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
                isProcessingResultInProgress = false;
                progressBar.setVisibility(View.GONE);
                setOnRefreshListener.setRefreshing(false);

                if (clearDialogHolder) {
                    QbDialogHolder.getInstance().clear();
                }
                QbDialogHolder.getInstance().addDialogs(dialogs);
                updateDialogsAdapter();
            }

            @Override
            public void onError(QBResponseException e) {
                isProcessingResultInProgress = false;
                progressBar.setVisibility(View.GONE);
                setOnRefreshListener.setRefreshing(false);
                Toast.makeText(DialogsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDialogsAdapter() {
        dialogsAdapter.updateList(new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values()));
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onDialogUpdated(String chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }


    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
            Log.v(TAG, "Received broadcast " + intent.getAction() + " with data: " + message);
            requestBuilder.setSkip(skipRecords = 0);
            loadDialogsFromQb(true, true);
        }
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {

        }
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            Log.v("AllD ", qbChatMessage.getBody());
            if (!senderId.equals(ChatHelper.getCurrentUser().getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
            }
        }
    }
}
