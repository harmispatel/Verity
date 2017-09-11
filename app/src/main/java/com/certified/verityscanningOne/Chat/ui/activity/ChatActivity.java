package com.certified.verityscanningOne.Chat.ui.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.certified.verityscanningOne.App;
import com.certified.verityscanningOne.BaseClass;
import com.certified.verityscanningOne.Chat.ui.adapter.AttachmentPreviewAdapter;
import com.certified.verityscanningOne.Chat.ui.adapter.ChatAdapter;
import com.certified.verityscanningOne.Chat.ui.widget.AttachmentPreviewAdapterView;
import com.certified.verityscanningOne.Chat.utils.chat.ChatHelper;
import com.certified.verityscanningOne.Chat.utils.qb.PaginationHistoryListener;
import com.certified.verityscanningOne.Chat.utils.qb.QbChatDialogMessageListenerImp;
import com.certified.verityscanningOne.Chat.utils.qb.QbDialogHolder;
import com.certified.verityscanningOne.Chat.utils.qb.QbDialogUtils;
import com.certified.verityscanningOne.Chat.utils.qb.QbUsersHolder;
import com.certified.verityscanningOne.Chat.utils.qb.VerboseQbChatConnectionListener;
import com.certified.verityscanningOne.R;
import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.certified.verityscanningOne.Utils.CommonUtils;
import com.certified.verityscanningOne.video.activities.CallActivity;
import com.certified.verityscanningOne.video.activities.OpponentsActivity;
import com.certified.verityscanningOne.video.activities.PermissionsActivity;
import com.certified.verityscanningOne.video.db.QbUsersDbManager;
import com.certified.verityscanningOne.video.services.CallService;
import com.certified.verityscanningOne.video.util.QBResRequestExecutor;
import com.certified.verityscanningOne.video.utils.CollectionsUtils;
import com.certified.verityscanningOne.video.utils.Consts;
import com.certified.verityscanningOne.video.utils.PermissionsChecker;
import com.certified.verityscanningOne.video.utils.PushNotificationSender;
import com.certified.verityscanningOne.video.utils.QBEntityCallbackImpl;
import com.certified.verityscanningOne.video.utils.WebRtcSessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.sample.core.utils.imagepick.ImagePickHelper;
import com.quickblox.sample.core.utils.imagepick.OnImagePickedListener;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChatActivity extends BaseActivity implements OnImagePickedListener {
    private static final String TAG = ChatActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ATTACHMENT = 721;

    private static final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";
    public static final String EXTRA_DIALOG_ID = "dialogId";
    public static final String RECIEVER_ID = "reciever_id";
    String recieverId = "";

    private ProgressBar progressBar;
    private StickyListHeadersListView messagesListView;
    private EditText messageEditText;

    private LinearLayout attachmentPreviewContainerLayout;
    private Snackbar snackbar;

    private ChatAdapter chatAdapter;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private ConnectionListener chatConnectionListener;

    private QBChatDialog qbChatDialog;
    private ArrayList<QBChatMessage> unShownMessages;
    private int skipPagination = 0;
    private ChatMessageListener chatMessageListener;

    String opponentEmail = null;


    public static void startForResult(Activity activity, int code, QBChatDialog dialogId, String recieverId) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra(ChatActivity.RECIEVER_ID, recieverId);
        activity.startActivityForResult(intent, code);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        try {
            sharedPrefsHelper = SharedPrefsHelper.getInstance();
            qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID);
            recieverId = getIntent().getStringExtra(RECIEVER_ID);

            commonSession = new CommonSession(this);
            //doUserOfflineApi(CommonKeyword.USER_ONLINE);
            try {
                qbChatDialog.initForChat(QBChatService.getInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }

            chatMessageListener = new ChatMessageListener();
            qbChatDialog.addMessageListener(chatMessageListener);
            initChatConnectionListener();
            initViews();
            initChat();


            initAudioVideo();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (qbChatDialog != null) {
            outState.putString(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (qbChatDialog == null) {
            qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(savedInstanceState.getString(EXTRA_DIALOG_ID));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
            BaseClass.unregisterQbChatListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {

            ChatHelper.getInstance().removeConnectionListener(chatConnectionListener);
            BaseClass.registerQbChatListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
         //   doUserOfflineApi(CommonKeyword.USER_OFFLINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
           // doUserOfflineApi(CommonKeyword.USER_OFFLINE);
            BaseClass.registerQbChatListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        try {
            BaseClass.registerQbChatListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public void onImagePicked(int requestCode, File file) {
        switch (requestCode) {
            case REQUEST_CODE_ATTACHMENT:
                attachmentPreviewAdapter.add(file);
                break;
        }
    }

    @Override
    public void onImagePickError(int requestCode, Exception e) {
        showErrorSnackbar(0, e, null);
    }

    @Override
    public void onImagePickClosed(int requestCode) {
        // ignore
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.list_chat_messages);
    }

    //ad
    public void onSendChatClick(View view) {
        int totalAttachmentsCount = attachmentPreviewAdapter.getCount();
        Collection<QBAttachment> uploadedAttachments = attachmentPreviewAdapter.getUploadedAttachments();
        if (!uploadedAttachments.isEmpty()) {
            if (uploadedAttachments.size() == totalAttachmentsCount) {
                for (QBAttachment attachment : uploadedAttachments) {
                    sendChatMessage(null, attachment);
                }
            } else {
                Toaster.shortToast(R.string.chat_wait_for_attachments_to_upload);
            }
        }

        String text = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            sendChatMessage(text, null);

        }
        if (CommonUtils.isTextAvailable(text)) {
            commonSession = new CommonSession(this);
            //sendMessageApi(text);
        }

    }


   /* //
    private void sendMessageApi(String message) {
        try {
            if (CommonUtils.isConnectingToInternet(ChatActivity.this)) {

                Call<LawyerEducationBase> call = ApiHandler.getApiService().chatMessage(chatMessageparameter(message));

                call.enqueue(new Callback<LawyerEducationBase>() {
                    @Override
                    public void onResponse(Call<LawyerEducationBase> call, final Response<LawyerEducationBase> response) {
                        try {
                            if (response.isSuccessful()) {

                                JSONObject jsonObj = new JSONObject(new Gson().toJson(response).toString());
                                Log.e(" responce => ", jsonObj.getJSONObject("body").toString());

                                if (response.body().getSuccess() == 1) {
                                    Log.e(" responce => ", "message=" + response.body().getMessage());

                                } else if (response.body().getSuccess() == 0) {
                                    Log.e(" responce => ", "message=" + response.body().getMessage());
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();


                        }
                    }

                    @Override
                    public void onFailure(Call<LawyerEducationBase> call, Throwable t) {


                    }
                });

            } else {
                CommonUtils.commonToast(ChatActivity.this, getResources().getString(R.string.no_internet_exist));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private JsonObject chatMessageparameter(String message) {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_bid = new JSONObject();

            if (commonSession.getUserType().equalsIgnoreCase("lawyer")) {
                jsonObj_bid.put("ClientEmail", opponentEmail);
                jsonObj_bid.put("LawyerEmail", commonSession.getLoggedEmail());
            } else if (commonSession.getUserType().equalsIgnoreCase("client")) {
                jsonObj_bid.put("ClientEmail", commonSession.getLoggedEmail());
                jsonObj_bid.put("LawyerEmail", opponentEmail);
            }
            jsonObj_bid.put("SenderName", commonSession.getLoggedUserFName());
            jsonObj_bid.put("LoginType", commonSession.getUserType());
            jsonObj_bid.put("LastMessage", message);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_bid.toString());
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }
    //
    private void  doUserOfflineApi(String status) {
        try {
            if (CommonUtils.isConnectingToInternet(ChatActivity.this)) {

                Call<LawyerEducationBase> call = ApiHandler.getApiService().doUserOffline(doUserOfflineParameter(status));

                call.enqueue(new Callback<LawyerEducationBase>() {
                    @Override
                    public void onResponse(Call<LawyerEducationBase> call, final Response<LawyerEducationBase> response) {
                        try {
                            if (response.isSuccessful()) {

                                JSONObject jsonObj = new JSONObject(new Gson().toJson(response).toString());
                                Log.e(" responce => ", jsonObj.getJSONObject("body").toString());

                                if (response.body().getSuccess() == 1) {
                                    Log.e(" responce => ", "message=" + response.body().getMessage());

                                } else if (response.body().getSuccess() == 0) {
                                    Log.e(" responce => ", "message=" + response.body().getMessage());
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();


                        }
                    }

                    @Override
                    public void onFailure(Call<LawyerEducationBase> call, Throwable t) {


                    }
                });

            } else {
                CommonUtils.commonToast(ChatActivity.this, getResources().getString(R.string.no_internet_exist));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private JsonObject doUserOfflineParameter(String status) {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_bid = new JSONObject();

            jsonObj_bid.put("userEmail", commonSession.getLoggedEmail());
            jsonObj_bid.put("LoginType", commonSession.getUserType());
            jsonObj_bid.put("Status", status);

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_bid.toString());
            Log.e("MY gson.JSON:  ", "doUserOffline > AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }
    //
*/
    public void onAttachmentsClick(View view) {
        new ImagePickHelper().pickAnImage(this, REQUEST_CODE_ATTACHMENT);
    }

    public void showMessage(QBChatMessage message) {
        if (chatAdapter != null) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            if (unShownMessages == null) {
                unShownMessages = new ArrayList<>();
            }
            unShownMessages.add(message);
        }
    }

    private void initChatConnectionListener() {
        chatConnectionListener = new VerboseQbChatConnectionListener(getSnackbarAnchorView()) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case GROUP:
                        chatAdapter = null;
                        // Join active room if we're in Group Chat
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupChat();
                            }
                        });
                        break;
                }
            }
        };
    }

    private void initViews() {
        try {
            actionBar.setDisplayHomeAsUpEnabled(true);

            messagesListView = _findViewById(R.id.list_chat_messages);
            messageEditText = _findViewById(R.id.edit_chat_message);
            progressBar = _findViewById(R.id.progress_chat);
            attachmentPreviewContainerLayout = _findViewById(R.id.layout_attachment_preview_container);

            attachmentPreviewAdapter = new AttachmentPreviewAdapter(this,
                    new AttachmentPreviewAdapter.OnAttachmentCountChangedListener() {
                        @Override
                        public void onAttachmentCountChanged(int count) {
                            attachmentPreviewContainerLayout.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
                        }
                    },
                    new AttachmentPreviewAdapter.OnAttachmentUploadErrorListener() {
                        @Override
                        public void onAttachmentUploadError(QBResponseException e) {
                            showErrorSnackbar(0, e, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onAttachmentsClick(v);
                                }
                            });
                        }
                    });
            AttachmentPreviewAdapterView previewAdapterView = _findViewById(R.id.adapter_view_attachment_preview);
            previewAdapterView.setAdapter(attachmentPreviewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendChatMessage(String text, QBAttachment attachment) {
        try {
            QBChatMessage chatMessage = new QBChatMessage();
            if (attachment != null) {
                chatMessage.addAttachment(attachment);
            } else {
                chatMessage.setBody(text);
            }
            chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
            chatMessage.setDateSent(System.currentTimeMillis() / 1000);
            chatMessage.setMarkable(true);

            if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType()) && !qbChatDialog.isJoined()) {
                Toaster.shortToast("You're still joining a group chat, please wait a bit");
                return;
            }

            try {
                qbChatDialog.sendMessage(chatMessage);

                if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                    showMessage(chatMessage);
                }

                if (attachment != null) {
                    attachmentPreviewAdapter.remove(attachment);
                } else {
                    messageEditText.setText("");
                }
            } catch (SmackException.NotConnectedException e) {
                Log.w(TAG, e);
                Toaster.shortToast("Can't send a message, You are not connected to chat");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initChat() {
        switch (qbChatDialog.getType()) {
            case GROUP:
            case PUBLIC_GROUP:
                joinGroupChat();
                break;

            case PRIVATE:
                loadDialogUsers();
                break;

            default:
                Toaster.shortToast(String.format("%s %s", getString(R.string.chat_unsupported_type), qbChatDialog.getType().name()));
                finish();
                break;
        }
    }

    private void joinGroupChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle b) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                loadDialogUsers();
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                snackbar = showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
    }

    private void leaveGroupDialog() {
        try {
            ChatHelper.getInstance().leaveChatDialog(qbChatDialog);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private void releaseChat() {
        qbChatDialog.removeMessageListrener(chatMessageListener);
        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
            leaveGroupDialog();
        }
    }

    private void updateDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().updateDialogUsers(qbChatDialog, selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        qbChatDialog = dialog;
                        loadDialogUsers();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        showErrorSnackbar(R.string.chat_info_add_people_error, e,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateDialog(selectedUsers);
                                    }
                                });
                    }
                }
        );
    }

    private void loadDialogUsers() {
        ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                setChatNameToActionBar();
                loadChatHistory();

            }

            @Override
            public void onError(QBResponseException e) {
                showErrorSnackbar(R.string.chat_load_users_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadDialogUsers();
                            }
                        });
            }
        });


    }

    private void setChatNameToActionBar() {
        String chatName = QbDialogUtils.getDialogName(qbChatDialog);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(chatName);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
    }

    private void loadChatHistory() {
        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order
                Collections.reverse(messages);
                if (chatAdapter == null) {
                    chatAdapter = new ChatAdapter(ChatActivity.this, qbChatDialog, messages);
                    chatAdapter.setPaginationHistoryListener(new PaginationHistoryListener() {
                        @Override
                        public void downloadMore() {
                            loadChatHistory();
                        }
                    });
                    chatAdapter.setOnItemInfoExpandedListener(new ChatAdapter.OnItemInfoExpandedListener() {
                        @Override
                        public void onItemInfoExpanded(final int position) {
                            if (isLastItem(position)) {
                                // HACK need to allow info textview visibility change so posting it via handler
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messagesListView.setSelection(position);
                                    }
                                });
                            } else {
                                messagesListView.smoothScrollToPosition(position);
                            }
                        }

                        private boolean isLastItem(int position) {
                            return position == chatAdapter.getCount() - 1;
                        }
                    });
                    if (unShownMessages != null && !unShownMessages.isEmpty()) {
                        List<QBChatMessage> chatList = chatAdapter.getList();
                        for (QBChatMessage message : unShownMessages) {
                            if (!chatList.contains(message)) {
                                chatAdapter.add(message);
                            }
                        }
                    }
                    messagesListView.setAdapter(chatAdapter);
                    messagesListView.setAreHeadersSticky(false);
                    messagesListView.setDivider(null);
                } else {
                    chatAdapter.addList(messages);
                    messagesListView.setSelection(messages.size());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                skipPagination -= ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
                snackbar = showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
        skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
    }

    private void scrollMessageListDown() {
        messagesListView.setSelection(messagesListView.getCount() - 1);
    }

    private void deleteChat() {
        ChatHelper.getInstance().deleteDialog(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                showErrorSnackbar(R.string.dialogs_deletion_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteChat();
                            }
                        });
            }
        });
    }


    public class ChatMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            Log.e("ChatMessageLi  ===>>> ", s);
            showMessage(qbChatMessage);
        }


    }

    // ===============  audio & video ====================

    SharedPrefsHelper sharedPrefsHelper;

    private QBUser currentUser;
    private ArrayList<QBUser> currentOpponentsList;
    private boolean isRunForCall;
    private WebRtcSessionManager webRtcSessionManager;
    private QbUsersDbManager dbManager;

    private PermissionsChecker checker;

    protected QBResRequestExecutor requestExecutor;
    GooglePlayServicesHelper googlePlayServicesHelper;
    public CommonSession commonSession = null;
    QBUser userForSave;

    private void initAudioVideo() {



        initFields();
        initVideo();
        if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
            CallActivity.start(ChatActivity.this, true);
            finish();
        }
        checker = new PermissionsChecker(getApplicationContext());


        for (Integer id : qbChatDialog.getOccupants()) {
            QBUser user = QbUsersHolder.getInstance().getUserById(id);
            if (user == null) {
                //throw new RuntimeException("User from dialog is not in memory. This should never happen, or we are screwed");
            }
            if (user.getLogin().equalsIgnoreCase(commonSession.getLoggedEmail())) {

            } else {
                opponentEmail = user.getLogin();
                Log.e("Email ==>> ", opponentEmail);

            }


        }

    }

    private void initFields() {
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
            }

            dbManager = QbUsersDbManager.getInstance(getApplicationContext());
            webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initVideo() {
        try {

            googlePlayServicesHelper = new GooglePlayServicesHelper();
            requestExecutor = App.getInstance().getQbResRequestExecutor();

            dbManager = QbUsersDbManager.getInstance(getApplicationContext());
            webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
            checker = new PermissionsChecker(getApplicationContext());

            login(commonSession.getLoggedEmail());

            QBRTCClient.getInstance(this).prepareToProcessCalls();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(String name) {

        try {
            QBUsers.getUserByLogin(name).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    loginToChat(qbUser);
                }

                @Override
                public void onError(QBResponseException errors) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loginToChat(final QBUser qbUser) {
        try {
            qbUser.setPassword(Consts.DEFAULT_USER_PASSWORD);

            userForSave = qbUser;
            startLoginService(qbUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLoginService(QBUser qbUser) {
        try {
            Intent tempIntent = new Intent(this, CallService.class);
            PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
            CallService.start(this, qbUser, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {

            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                try {
                    saveUserData(userForSave);
                    signInCreatedUser(userForSave);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toaster.longToast(getString(R.string.login_chat_login_error) + errorMessage);

            }
        }

    }

    private void saveUserData(QBUser qbUser) {
        try {
            SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
            sharedPrefsHelper.saveQbUser(qbUser);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signInCreatedUser(final QBUser user) {
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {

            }

            @Override
            public void onError(QBResponseException responseException) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.private_dialog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case android.R.id.home:
//                try {
//                    doUserOfflineApi(CommonKeyword.USER_OFFLINE);
//                    BaseClass.registerQbChatListeners();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
            case R.id.action_audio_call:
                if (commonSession.getLoggedEmail().equalsIgnoreCase("adam@certified.bz")) {

                    OpponentsActivity.start(ChatActivity.this,false);
                }
                else
                {
                    startOpponentsActivity(false);
                }



                break;
            case R.id.switch_camera_toggle:
                if (commonSession.getLoggedEmail().equalsIgnoreCase("adam@certified.bz")) {

                    OpponentsActivity.start(ChatActivity.this,true);
                }
                else
                {
                    startOpponentsActivity(true);
                }                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }


    private void startOpponentsActivity(final boolean isVideo) {

        try {


            ProgressDialogFragment.show(getSupportFragmentManager(), R.string.please_wait);
           // currentUser = sharedPrefsHelper.getQbUser();

            QBUsers.getUserByLogin("adam@certified.bz").performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {

                    currentOpponentsList = new ArrayList<>();
                    currentOpponentsList.add(qbUser);

                    dbManager.saveAllUsers(currentOpponentsList, true);

                    if (isLoggedInChat()) {
                        startCall(isVideo);
                    }
                    if (checker.lacksPermissions(Consts.PERMISSIONS)) {
                        startPermissionsActivity(false);
                    }
                }

                @Override
                public void onError(QBResponseException errors) {

                    ProgressDialogFragment.hide(getSupportFragmentManager());
                    CommonUtils.commonToast(ChatActivity.this, "User is not available");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //  Opponents
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if (intent.getExtras() != null) {
                isRunForCall = intent.getExtras().getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
                if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
                    CallActivity.start(ChatActivity.this, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Consts.PERMISSIONS);
    }


    private boolean isLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            Toaster.shortToast(R.string.dlg_signal_error);
            tryReLoginToChat();
            return false;
        }
        return true;
    }

    private void tryReLoginToChat() {
        try {
            if (sharedPrefsHelper.hasQbUser()) {
                QBUser qbUser = sharedPrefsHelper.getQbUser();
                CallService.start(this, qbUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCall(boolean isVideoCall) {

        try {

            ProgressDialogFragment.hide(getSupportFragmentManager());
            Log.d("TAG", "startCall()");
            ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(currentOpponentsList);
            QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                    ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                    : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

            QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

            QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

            WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);

            PushNotificationSender.sendPushMessage(opponentsList,"Piter");

            CallActivity.start(this, false);
            Log.d("TAG", "conferenceType = " + conferenceType);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

}
