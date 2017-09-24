package edu.wpi.alcoholcontextualizer.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tupac on 3/19/2017.
 */

public class PrefManager {
    private static final String MY_PREFERENCES = "IS_FIRST_TIME";
    private static final String MY_LOGIN_PREFERENCES = "IS_FIRST_LOGIN";

    public boolean isFirst(Context context) {
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        return first;
    }

    public void setToFirstTime(Context context) {
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = reader.edit();
        editor.putBoolean("is_first", false);
        editor.commit();

    }

    public boolean isFirstLogin(Context context) {
        final SharedPreferences reader = context.getSharedPreferences(MY_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first_login", true);
        return first;
    }

    public void setToFirstLogin(Context context) {
        final SharedPreferences reader = context.getSharedPreferences(MY_LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = reader.edit();
        editor.putBoolean("is_first_login", false);
        editor.commit();

    }
}
