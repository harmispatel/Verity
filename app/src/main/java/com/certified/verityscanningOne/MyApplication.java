package com.certified.verityscanningOne;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class MyApplication extends App {

    public static Context context;
    public static Location mCurrentLocation;
    public static String locationProvider;
    public static Location oldLocation;
    public static String locationTime;
    private static final String TAG = MyApplication.class.getSimpleName();
    public static Context mContext;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        try {
            // Create global configuration and initialize ImageLoader with this
            // configuration
            context = getApplicationContext();

            // UNIVERSAL IMAGE LOADER SETUP
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(new FadeInBitmapDisplayer(300)).build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    getApplicationContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100 * 1024 * 1024).build();

            ImageLoader.getInstance().init(config);
            // END - UNIVERSAL IMAGE LOADER SETUP
            // Setup handler for uncaught exceptions.
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable e) {
                    handleUncaughtException(thread, e);


                }
            });

            mInstance = this;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }


    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public void handleUncaughtException(Thread thread, Throwable e) {

    }



}
