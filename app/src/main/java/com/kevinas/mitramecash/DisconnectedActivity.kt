package com.kevinas.mitramecash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class DisconnectedActivity: AppCompatActivity(){
    var andLib = AndroidLib();
    open fun tryAgain(){
        andLib.restartApp();
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        andLib = AndroidLib(this, this@DisconnectedActivity);
        setContentView(R.layout.disconnected);
        (findViewById(R.id.try_again) as Button).setOnClickListener{
            tryAgain();
        }
    }

override fun onBackPressed() {
        andLib.restartApp();
    }

}