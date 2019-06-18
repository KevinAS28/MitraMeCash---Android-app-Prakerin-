package com.kevinas.mitramecash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView

class ImageFullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);
        (findViewById(R.id.imageFullScreen) as ImageView).setImageDrawable((BaseActivity.fullImage.get(0).drawable));

    }
}
