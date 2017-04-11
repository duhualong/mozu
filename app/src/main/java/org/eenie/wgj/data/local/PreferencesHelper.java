package org.eenie.wgj.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import org.eenie.wgj.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * SharedPreferences Open Helper
 */
@Singleton
public class PreferencesHelper {
  public static final String PREF_FILE_NAME = "pref_file";
  private SharedPreferences prefs;

  @Inject
  public PreferencesHelper(@ApplicationContext Context context) {
    prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
  }
  public SharedPreferences getPrefs() {
    return prefs;
  }
  public void clear() {
    prefs.edit().clear().apply();
  }
}
