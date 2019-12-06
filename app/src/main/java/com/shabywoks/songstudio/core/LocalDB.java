package com.shabywoks.songstudio.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Shubham Bhiwaniwala on 2019-11-12.
 */
public class LocalDB {

    public static final String PREMIUM = "PREMIUM";

    SharedPreferences pref;

    public LocalDB(Context context) {
        pref = context.getSharedPreferences("songstudio", 0); // 0 - for private mode
    }

    public void saveRecent(ArrayList<PlayListItem> listItems) {
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(listItems);
        editor.putString("recent", json);
        editor.commit();

    }

    public ArrayList<PlayListItem> getRecent() {
        Gson gson = new Gson();
        String data = pref.getString("recent", null);
        if (data == null) {
            return new ArrayList<>();
        }
        Type collectionType = new TypeToken<ArrayList<PlayListItem>>(){}.getType();
        return (new Gson()).fromJson(data, collectionType);
    }

    public void playedItem(PlayListItem item) {
        ArrayList<PlayListItem> recents = getRecent();

        if (recents.size() == 4) {
            recents.remove(3);
        }

        for(int i=0; i<recents.size(); i++) {
            if (recents.get(i).url.equals(item.url)) {
                recents.remove(i);
                break;
            }
        }

        recents.add(0, item);
        saveRecent(recents);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

}
