package com.certified.verityscanningOne.barcodescanner.core;

import android.content.Context;
import android.content.SharedPreferences;


public class ApplicationPrefs {

    private static ApplicationPrefs prefs;

    protected Context context;
    public static final String PREF_NAME = "cer";

    private static final String AUTOSCAN = "AUTOSCAN";
    private static final String CONFIRMONLINE = "confirmonline";
    private static final String DISCOVERY = "disc";
    private static final String BEPP = "beep";


    private static final String PRO = "pro";

    private ApplicationPrefs(Context context) {
        this.context = context;
    }

    public static ApplicationPrefs getInstance(Context context) {

        if (prefs == null) {
            prefs = new ApplicationPrefs(context);
        }

        return prefs;
    }


    public void setBeep(int state)
    {
        SharedPreferences.Editor editor = getPrefsEditor();
        editor.putInt(BEPP,state);
        editor.commit();
    }
    public int getBeep()
    {
        return getPrefs().getInt(BEPP,1);
    }



    public void setAutoScan(boolean state)
    {
        SharedPreferences.Editor editor = getPrefsEditor();
        editor.putBoolean(AUTOSCAN,state);
        editor.commit();
    }
    public boolean getAutoScan()
    {
        return getPrefs().getBoolean(AUTOSCAN,false);
    }





    public void setDiscState(boolean state)
    {
        SharedPreferences.Editor editor = getPrefsEditor();
        editor.putBoolean(DISCOVERY,state);
        editor.commit();
    }
    public boolean getDiscState()
    {
        return getPrefs().getBoolean(DISCOVERY,true);
    }


    public void setPro(String state)
    {
        SharedPreferences.Editor editor = getPrefsEditor();
        editor.putString(PRO, state);
        editor.commit();
    }
    public String getPro()
    {
        return getPrefs().getString(PRO, "");
    }



    protected SharedPreferences getPrefs() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getPrefsEditor() {
        return getPrefs().edit();
    }


}
