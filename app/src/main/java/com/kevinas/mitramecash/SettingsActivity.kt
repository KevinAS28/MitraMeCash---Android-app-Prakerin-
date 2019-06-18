
package com.kevinas.mitramecash

import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    var andLib : AndroidLib?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        andLib = AndroidLib(this, this@SettingsActivity);
        setContentView(R.layout.activity_menu_pengaturan);
        var imgProfile: ImageButton = (findViewById(R.id.settings_profile_picture) as ImageButton);
        var nama: TextView = findViewById(R.id.settings_nama) as TextView;
        var namaMerchant: TextView = findViewById(R.id.settings_namamerchant) as TextView;
        var kodeMerchant: TextView = findViewById(R.id.settings_kdmerchant) as TextView;

        nama.text = andLib!!.getData("userID");
        namaMerchant.text = andLib!!.getDataAddr("data/nmMerchant");
        kodeMerchant.text = andLib!!.getDataAddr("data/kdMerchant");
        //make those press-able textview underlined...
        nama.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
        namaMerchant.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
        kodeMerchant.paintFlags = Paint.UNDERLINE_TEXT_FLAG;

        imgProfile.setOnClickListener {
            var img: ImageView = findViewById(R.id.settings_profile_picture) as ImageView;
            andLib!!.imgFull(img);
        }

        nama.setOnClickListener {
            andLib!!.copySnack(nama.text.toString(), "Name Copied", nama);
        }
        namaMerchant.setOnClickListener {
            andLib!!.copySnack(namaMerchant.text.toString(), "Name Merchant Copied", namaMerchant);
        }
        kodeMerchant.setOnClickListener {
            andLib!!.copySnack(kodeMerchant.text.toString(), "Kode Merhant Copied", kodeMerchant);
        }
    }


}


