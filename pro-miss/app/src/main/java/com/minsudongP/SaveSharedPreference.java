package com.minsudongP;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static final String PREF_USER_ID = "id";
    static final String PREF_USER_PW = "password";
    static final String ALERT_ON="alert";
    static final String VOICE_ON="voice";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserInfo(Context ctx, String userId, String userPw) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userId);
        editor.putString(PREF_USER_PW, userPw);
        editor.commit();
    }

    public static void setSetting_Alert(Context ctx,int alert)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(ALERT_ON, alert);
        editor.commit();
    }
    public static void setSetting_Voice(Context ctx,int voice)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(ALERT_ON, voice);
        editor.commit();
    }

    public static int getSetting_Alert(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(ALERT_ON,0);
    }

    public static int getSetting_Voice(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(VOICE_ON,0);
    }
    // 저장된 정보 가져오기
    public static String getUserId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }
    public static String getUserPw(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PW, "");
    }

    // 정보 삭제
    public static void clearUserInfo(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}