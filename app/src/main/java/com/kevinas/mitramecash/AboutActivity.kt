package com.kevinas.mitramecash

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class AboutActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_about);
        findViewById<TextView>(R.id.txt_pesan).text = "if you found this application problem, please contact: "//getResources().getString(R.string.text_about_app);
        findViewById<TextView>(R.id.txt_email).setTextColor(Color.parseColor("#00d0ff"));
        findViewById<TextView>(R.id.txt_email).text = Configuration.companyEmail;//getResources().getString(R.string.text_about_app);
        findViewById<TextView>(R.id.txt_email).setOnClickListener{
            val email = Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf(Configuration.companyEmail))
            email.putExtra(Intent.EXTRA_SUBJECT, "IBA M-Banking Problem")
            //email.putExtra(Intent.EXTRA_TEXT, "message")
            email.type = "message/rfc822"
            startActivity(Intent.createChooser(email, Configuration.companyEmail))
        }
        findViewById<Button>(R.id.btn_ok).setOnClickListener {
            onBackPressed();
        }
    }
}