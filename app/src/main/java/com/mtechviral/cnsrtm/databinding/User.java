package com.mtechviral.cnsrtm.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.mtechviral.cnsrtm.BR;

/**
 * Created by pawankumar on 26/03/17.
 */

public class User extends BaseObservable {
    private String username;
    private String password;

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
