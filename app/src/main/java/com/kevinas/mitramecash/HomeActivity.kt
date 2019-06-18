package com.kevinas.mitramecash

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import com.kevinas.mitramecash.json.JSONObject

class HomeActivity: AppCompatActivity(){

    var andLib : AndroidLib = AndroidLib(this@HomeActivity, this);
    var loading: AlertDialog? = null;
    override fun onBackPressed() {
        var builder : AlertDialog.Builder = andLib.dialog("Anda ingin keluar?", "Konfirmasi");
        var listener0: DialogInterface.OnClickListener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                andLib.exitAppFull();
            }
        }
        var listener1: DialogInterface.OnClickListener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
            }
        }
        builder.setPositiveButton("Ok", listener0);
        builder.setNegativeButton("Cancel", listener1);
        builder.show();
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        loading = andLib.getLoading("", "");
        loading!!.show();
        var moneyGet: MeCashLib = object: MeCashLib(this, this@HomeActivity){
            override fun <ByteArray>onDone(data: ByteArray){
                var dataJson = JSONObject(String(data as kotlin.ByteArray));
                andLib!!.setData(dataJson);
                loading!!.hide();
                setContentView(R.layout.fragment_home_new);
                findViewById<LinearLayout>(R.id.btn_pay_release).setOnClickListener{
                    var intent = Intent(this@HomeActivity, ReleaseActivity::class.java);
                    startActivity(intent);
                }
                findViewById<LinearLayout>(R.id.btn_daftar_mutasi).setOnClickListener{
                    var intent = Intent(this@HomeActivity, DaftarMutasiRekeningActivity::class.java);
                    startActivity(intent);
                }
                findViewById<LinearLayout>(R.id.btn_info_saldo).setOnClickListener{
                    var intent = Intent(this@HomeActivity, InfoSaldoActivity::class.java);
                    startActivity(intent);
                }
                (findViewById(R.id.lbl_saldo_emoney) as TextView).setText((dataJson.get("data") as JSONObject).get("saldoEmoney") as String);
                (findViewById(R.id.lbl_info_rekening) as LinearLayout).setOnClickListener {
                    var intent = Intent(this@HomeActivity, ProfileActivity::class.java);
                    startActivity(intent);
                }
            }
        }
        moneyGet.getEMoney(andLib.getData("userID"));

    }
}