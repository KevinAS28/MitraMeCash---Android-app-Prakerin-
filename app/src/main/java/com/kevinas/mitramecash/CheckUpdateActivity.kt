package com.kevinas.mitramecash


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import android.widget.ImageButton

import java.util.ArrayList

class CheckUpdateActivity : AppCompatActivity() {
    fun checkUpdate(version: String): Boolean {
        return false
    }

    public override fun onCreate(bund: Bundle?) {
        super.onCreate(bund)
        object : CountDownTimer(tunggu.toLong(), (tunggu / 10).toLong()) {
            override fun onFinish() {
                if (!checkUpdate("1")) {
                    setContentView(R.layout.update_availble)
                    val yes = findViewById<ImageButton>(R.id.confirm_image_yes)
                    val no = findViewById<ImageButton>(R.id.confirm_image_no)
                    yes.setOnClickListener {
                        //ngapain kek
                        System.exit(0)
                    }
                    no.setOnClickListener {
                        val intent = Intent(applicationContext, next)

                        startActivity(intent, bund)
                    }
                }
            }

            override fun onTick(x: Long) {}
        }.start()

    }

    companion object {
        internal var next: Class<*> = Menu::class.java
        internal var tunggu = 1
    }
}