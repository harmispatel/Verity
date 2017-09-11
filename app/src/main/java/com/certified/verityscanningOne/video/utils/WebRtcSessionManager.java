package com.certified.verityscanningOne.video.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.certified.verityscanningOne.HomeActivity;
import com.certified.verityscanningOne.Utils.CommonKeyword;
import com.certified.verityscanningOne.Utils.CommonSession;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacksImpl;

import static com.certified.verityscanningOne.BaseClass.commonSession;


/**
 * Created by tereha on 16.05.16.
 */
public class WebRtcSessionManager extends QBRTCClientSessionCallbacksImpl {
    private static final String TAG = WebRtcSessionManager.class.getSimpleName();

    private static WebRtcSessionManager instance;
    private Context context;

    private static QBRTCSession currentSession;

    private WebRtcSessionManager(Context context) {
        this.context = context;
    }

    public static WebRtcSessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new WebRtcSessionManager(context);
        }

        return instance;
    }

    public QBRTCSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(QBRTCSession qbCurrentSession) {
        currentSession = qbCurrentSession;
    }

    @Override
    public void onReceiveNewSession(QBRTCSession session) {
        Log.d(TAG, "onReceiveNewSession to WebRtcSessionManager");

        if (currentSession == null) {
            setCurrentSession(session);

            CommonSession commonSession = new CommonSession(context);



                    Intent in = new Intent(context, HomeActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    in.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, true);
                    context.startActivity(in);





        }
    }

    @Override
    public void onSessionClosed(QBRTCSession session) {
        Log.d(TAG, "onSessionClosed WebRtcSessionManager");

        if (session.equals(getCurrentSession())) {
            setCurrentSession(null);
        }
    }
}
