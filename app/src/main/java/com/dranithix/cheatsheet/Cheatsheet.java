package com.dranithix.cheatsheet;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by user on 5/21/2016.
 */

public class Cheatsheet extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("rXyaQwfskbK4MHaqTwlOol91CJ2poeUNhy4yV9qN")
                .clientKey("0gixFsRkvLb4YpSb9zhSa4cP5EFLNRLnEbIT2JbU")
                .build()
        );
    }
}
