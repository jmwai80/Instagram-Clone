package com.example.insta;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("83fircruYBoiDeYI4dHWC8zuRNu7cuQzXmhTCxZ0")
                .clientKey("9520xunznRbaQohUxLuTXjMP64deufbX0NuBrPQp")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
