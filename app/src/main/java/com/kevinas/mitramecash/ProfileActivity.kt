package com.kevinas.mitramecash

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.kevinas.mitramecash.json.JSONObject

class ProfileActivity: AppCompatActivity(){
    var andLib = AndroidLib(this, this@ProfileActivity);
    var loading: AlertDialog? = null;
    var aes = AES();
    var meCashLib: MeCashLib? = null;
    override fun onBackPressed(){
        var intent = Intent(this@ProfileActivity, HomeActivity::class.java);
        startActivity(intent);
    }
    fun profileSucceed(data:JSONObject){
        loading!!.hide();
        var jsonData = data.get("data") as JSONObject;
        setContentView(R.layout.fragment_profile);
        (findViewById(R.id.lbl_saldo_emoney) as TextView).setText(jsonData.get("saldoEmoney") as String);
        (findViewById(R.id.btn_release) as LinearLayout).setOnClickListener{
            var intent = Intent(this@ProfileActivity, ReleaseActivity::class.java);
            startActivity(intent);
        }
        (findViewById(R.id.text_history_transaksi) as TextView).setOnClickListener{
            var intent = Intent(this@ProfileActivity, DaftarMutasiRekeningActivity::class.java);
            startActivity(intent);
        }
        (findViewById(R.id.btn_logout) as RelativeLayout).setOnClickListener{
            meCashLib!!.logout();
        }
        (findViewById(R.id.btn_setting) as RelativeLayout).setOnClickListener{
            var intent = Intent(this, SettingsActivity::class.java);
            startActivity(intent);
        }
        (findViewById(R.id.profile_account) as LinearLayout).setOnClickListener{
            var intent = Intent(this, SettingsActivity::class.java);
            startActivity(intent);
        }   
        (findViewById(R.id.btn_about) as RelativeLayout).setOnClickListener{
            var intent = Intent(this, AboutActivity::class.java);
            startActivity(intent);
        }
        (findViewById(R.id.lbl_info_account) as TextView).text = andLib.getData("userID");
        (findViewById(R.id.lbl_info_rekening) as TextView).text = andLib.getDataAddr("data/nmAcc");
    }
    fun profileError(msg: String){

    }
    fun profileFail(data: JSONObject){

    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        meCashLib = MeCashLib(this, this@ProfileActivity);
        loading = andLib.getLoading("", "");
        loading!!.show();

        var net = object: MeCashLib(this, this@ProfileActivity){
            override fun<ByteArray>onDone(result: ByteArray) {
                try{
                    var money = JSONObject(String(aes.decrypt((result as kotlin.ByteArray))));
                    if (money.get("rc")=="00"){
                        profileSucceed(money);
                    }
                    else{
                        profileFail(money);
                    }
                }
                catch (e: Exception){
                    profileError(e.toString());
                }
            }
            override fun onError(msg: String){
                profileError(msg);
            }
        }
        net.getEMoney(andLib.getData("userID"));
    }
}