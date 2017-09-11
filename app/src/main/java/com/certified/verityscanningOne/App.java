package com.certified.verityscanningOne;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;


import com.certified.verityscanningOne.Chat.models.SampleConfigs;
import com.certified.verityscanningOne.Chat.utils.Consts;
import com.certified.verityscanningOne.Chat.utils.configs.ConfigUtils;
import com.certified.verityscanningOne.video.util.QBResRequestExecutor;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;
import com.quickblox.sample.core.utils.SharedPrefsHelper;

import java.io.IOException;



public class App extends CoreApp implements
        Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {


    //Applock

    public static String stateOfLifeCycle = "";
    public static boolean wasInBackground = false;

    ScreenOffReceiver screenOffReceiver = new ScreenOffReceiver();


    //chat
    private static final String TAG = App.class.getSimpleName();
    private static SampleConfigs sampleConfigs;

    //video
    private static App instance;
    private QBResRequestExecutor qbResRequestExecutor;

    public static App getInstance() {

        if (instance == null) {
            instance = new App();
        }

        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();


        try {

            //AppLock
            registerActivityLifecycleCallbacks(this);

            registerReceiver(screenOffReceiver, new IntentFilter(
                    "android.intent.action.SCREEN_OFF"));

            //chat
            ActivityLifecycle.init(this);
            initSampleConfigs();
            initQBSessionManager();

            //video
            initApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    /**
     * chat
     */
    private void initSampleConfigs() {
        try {
            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
    }

    private void initQBSessionManager() {
        QBSessionManager.getInstance().addListener(new QBSessionManager.QBSessionListener() {
            @Override
            public void onSessionCreated(QBSession qbSession) {
                Log.d(TAG, "Session Created");
            }

            @Override
            public void onSessionUpdated(QBSessionParameters qbSessionParameters) {
                Log.d(TAG, "Session Updated");
            }

            @Override
            public void onSessionDeleted() {
                Log.d(TAG, "Session Deleted");
            }

            @Override
            public void onSessionRestored(QBSession qbSession) {
                Log.d(TAG, "Session Restored");
            }

            @Override
            public void onSessionExpired() {
                Log.d(TAG, "Session Expired");
            }
        });
    }

    /**
     * video
     */

    private void initApplication() {
        instance = this;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }


    //AppLock

    @Override
    public void onActivityCreated(Activity activity, Bundle arg1) {
        wasInBackground = false;
        stateOfLifeCycle = "Create";
    }

    @Override
    public void onActivityStarted(Activity activity) {
        stateOfLifeCycle = "Start";
    }

    @Override
    public void onActivityResumed(Activity activity) {
        stateOfLifeCycle = "Resume";
    }

    @Override
    public void onActivityPaused(Activity activity) {
        stateOfLifeCycle = "Pause";
    }

    @Override
    public void onActivityStopped(Activity activity) {
        stateOfLifeCycle = "Stop";
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle arg1) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        wasInBackground = false;
        stateOfLifeCycle = "Destroy";
    }

    @Override
    public void onTrimMemory(int level) {
        if (stateOfLifeCycle.equals("Stop")) {
            wasInBackground = true;
        }
        super.onTrimMemory(level);
    }

    class ScreenOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            wasInBackground = true;
        }
    }


}
