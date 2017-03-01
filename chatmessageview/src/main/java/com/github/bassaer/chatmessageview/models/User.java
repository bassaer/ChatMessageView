package com.github.bassaer.chatmessageview.models;

import android.graphics.Bitmap;

/**
 * User object
 * Created by nakayama on 2017/01/12.
 */
public class User {
    private int mId;
    private String mName;
    private Bitmap mIcon;

    public User(int id, String name, Bitmap icon) {
        mId = id;
        mName = name;
        mIcon = icon;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }
}
