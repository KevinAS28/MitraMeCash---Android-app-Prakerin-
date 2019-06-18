package com.kevinas.mitramecash

import android.app.Activity
import android.os.Bundle
import android.app.AlertDialog;
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.kevinas.mitramecash.json.JSONObject

class InfoSaldoActivity: AppCompatActivity(){
    var andLib = AndroidLib();
    var aes = AES();
    var loading : AlertDialog? = null;
    var activity: Activity? = null;
    fun infoSaldoSucceed(data: JSONObject){
        loading!!.dismiss();
        setContentView(R.layout.activity_response_text);
        var saldo = (data.get("data") as JSONObject).get("saldo") as String;
        var norek = andLib.getData("noAcc");
        var str = "Saldo anda pada rekening $norek, sebesar $saldo";
        findViewById<TextView>(R.id.text_response).text= str;
        println(findViewById<TextView>(R.id.text_response).text);
        findViewById<Button>(R.id.btn_ok).setOnClickListener{
//            var intent = Intent(activity, HomeActivity::class.java);
//            startActivity(intent);
            onBackPressed();
        }
    }

    override fun onBackPressed() {
        super.onBackPressed();
    }
    fun infoSaldoError(msg: String) {
        println("infoSaldoError");
    }
    fun infoSaldoFail(data: JSONObject){
        println("infoSaldoFail");
    }
    fun infoSaldoApiSucceed(data: JSONObject){

    }
    fun infoSaldoApiError(msg: String) {

    }
    fun infoSaldoApiFail(data: JSONObject){

    }
    fun infoSaldoApi(userID: String, noAcc: String, apiOnly:Boolean = true): JSONObject{
        var net = object : MeCashLib() {
            override fun <ByteArray> onDone(result: ByteArray) {
                try {
                    var infoSaldoApiReturn = JSONObject(String(aes.decrypt((result as kotlin.ByteArray))));
                    if (infoSaldoApiReturn.get("rc") == "00") {
                        infoSaldoApiSucceed(infoSaldoApiReturn);
                        if (!apiOnly) {
                            infoSaldoSucceed(infoSaldoApiReturn);
                        }
                    } else {
                        infoSaldoApiFail(infoSaldoApiReturn);
                        if (!apiOnly) {
                            infoSaldoFail(infoSaldoApiReturn);
                        }
                    }
                } catch (e: Exception) {
                    infoSaldoApiError(e.toString());
                    if (!apiOnly) {
                        infoSaldoError(e.toString());
                    }
                }
            }

            override fun onError(msg: String) {
                infoSaldoApiError(msg);
                if (!apiOnly) {
                    infoSaldoError(msg);
                }
            }
        }

        var authJson = JSONObject();
        var authDataJson = JSONObject();
        authJson = authJson.put("KD", "SLD");
        authJson = authJson.put("userid", userID);
        authJson = authJson.put("traceid", System.nanoTime());
        authDataJson = authDataJson.put("idDevice", andLib!!.getImei());
        authDataJson = authDataJson.put("noAcc",noAcc);
        authJson = authJson.put("data", authDataJson);
        net.andLib = andLib!!;
        net.hitMainApi(authJson, "", 0);

        return authJson;


    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        this.andLib = AndroidLib(this, this@InfoSaldoActivity);
        activity = this@InfoSaldoActivity;
        loading = andLib.getLoading("", "");
        loading!!.show();
        infoSaldoApi(andLib.getData("userID"), andLib.getData("noAcc"), false);
    }
}