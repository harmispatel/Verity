package com.certified.verityscanningOne.Chat.ui.activity;//package lexlink.app.com.lexlink.Chat.ui.activity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import com.quickblox.auth.session.QBSessionManager;
//import com.quickblox.core.QBEntityCallback;
//import com.quickblox.core.exception.QBResponseException;
//
//import com.quickblox.sample.core.ui.activity.CoreSplashActivity;
//import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
//import com.quickblox.sample.core.utils.SharedPrefsHelper;
//import com.quickblox.users.model.QBUser;
//
//import lexlink.app.com.lexlink.App;
//import lexlink.app.com.lexlink.Chat.utils.chat.ChatHelper;
//import lexlink.app.com.lexlink.R;
//
//public class SplashActivity extends CoreSplashActivity {
//
//    private static final String TAG = SplashActivity.class.getSimpleName();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (checkConfigsWithSnackebarError()){
//            proceedToTheNextActivityWithDelay();
//        }
//    }
//
//    @Override
//    protected String getAppName() {
//        return getString(R.string.splash_app_title);
//    }
//
//    @Override
//    protected void proceedToTheNextActivity() {
//        if (checkSignIn()) {
//            restoreChatSession();
//        } else {
//            LoginActivity.start(this);
//            finish();
//        }
//    }
//
//    @Override
//    protected boolean sampleConfigIsCorrect() {
//        boolean result = super.sampleConfigIsCorrect();
//        result = result && App.getSampleConfigs() != null;
//        return result;
//    }
//
//    private void restoreChatSession(){
//        if (ChatHelper.getInstance().isLogged()) {
//            DialogsActivity.start(this);
//            finish();
//        } else {
//            QBUser currentUser = getUserFromSession();
//            loginToChat(currentUser);
//        }
//    }
//
//    private QBUser getUserFromSession(){
//        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
//        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
//        return user;
//    }
//
//    @Override
//    protected boolean checkSignIn() {
//        return SharedPrefsHelper.getInstance().hasQbUser();
//    }
//
//    private void loginToChat(final QBUser user) {
//        ProgressDialogFragment.show(getSupportFragmentManager(), R.string.dlg_restoring_chat_session);
//
//        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
//            @Override
//            public void onSuccess(Void result, Bundle bundle) {
//                Log.v(TAG, "Chat login onSuccess()");
//
//                ProgressDialogFragment.hide(getSupportFragmentManager());
//                DialogsActivity.start(SplashActivity.this);
//                finish();
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//                ProgressDialogFragment.hide(getSupportFragmentManager());
//                Log.w(TAG, "Chat login onError(): " + e);
//                showSnackbarError( findViewById(R.id.layout_root), R.string.error_recreate_session, e,
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                loginToChat(user);
//                            }
//                        });
//            }
//        });
//    }
//}