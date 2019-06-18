package com.kevinas.mitramecash

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.kevinas.mitramecash.json.*

class SignUpActivity: AppCompatActivity(){
    var andLib: AndroidLib? = null;
    var aes = AES();
    var frame: RelativeLayout? = null;
    var loading: AlertDialog? = null;
    var savedInstanceState: Bundle? = null;
    var username = ""
    var pass = ""
    fun regisFail(data: JSONObject){
        println("regisFail() called");
        loading?.dismiss();
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.regis_error_msg) as TextView;
        var runnable = object: Runnable{
            override fun run(){
                error!!.setText("Account name is already used by someone else. please try again");
                error.visibility = View.VISIBLE;
                loading!!.dismiss();
            }
        }
        mainHandler.post(runnable);
    }

    fun regisError(msg: String){
        println("regisError() called");
        loading?.dismiss();
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.regis_error_msg) as TextView;
        var runnable = object: Runnable{
            override fun run(){
                error!!.setText("Connection failed: "+msg );
                error.visibility = View.VISIBLE;
                loading!!.dismiss();
            }
        }
        mainHandler.post(runnable);
    }
    fun regisSucceed(data: JSONObject){
        println("regisSucceed() called");
        loading?.dismiss();
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.regis_error_msg) as TextView;
        var intent = Intent(this@SignUpActivity, ActivationActivity::class.java);
        intent.putExtra("userID", username);
        intent.putExtra("pass", pass );
        intent.putExtra("kdReg", data.get("kdReg") as String);

        var runnable = object: Runnable{
            override fun run(){
                loading!!.dismiss();
                error.visibility = View.INVISIBLE;

            }
        }
        mainHandler.post(runnable);
        startActivity(intent);

    }

    open fun regisApiFail(data: JSONObject){

    }
    open fun regisApiError(msg: String){

    }
    open fun regisApiSucceed(data: JSONObject){

    }

    fun regis(){
        loading?.show();
        var password: TextInputEditText = findViewById(R.id.txt_password) as TextInputEditText;
        var retype: TextInputEditText = findViewById(R.id.txt_retype) as TextInputEditText;
        if (password.text.toString()!=retype.text.toString()){
            var builder = andLib!!.dialog("Password not match", "the both password you was entered not match.");
            builder.setPositiveButton("Coba lagi", object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.cancel();

                }
            });
            return;
        }


        username = (findViewById(R.id.txt_username) as TextInputEditText).text.toString();
        pass = password.text.toString();
        var noRek = (findViewById(R.id.txt_norek) as TextInputEditText).text.toString();
        var nama = (findViewById(R.id.txt_nama) as TextInputEditText).text.toString();
        var noHp = (findViewById(R.id.txt_nohp) as TextInputEditText).text.toString();
        var idMerchant = (findViewById(R.id.txt_kdmerchant) as TextInputEditText).text.toString();
        var nmMerchant = (findViewById(R.id.txt_namamerchant) as TextInputEditText).text.toString();
        var json = regisApi(username, pass, noRek, nama, noHp, idMerchant, nmMerchant, false);

    }
    fun regisApi(username: String, pass: String, noAcc:String, nmAcc:String, noHp: String, idMerchant:String, nmMerchant:String, apiOnly: Boolean=true): JSONObject{
        var net = object: MeCashLib(){
            override fun<ByteArray> onDone(result: ByteArray){
                try {
                    var response = JSONObject(String(aes.decrypt(result as kotlin.ByteArray)));

                    if (response.get("rc")=="00"){
                        regisApiSucceed(response);
                        if (!apiOnly){
                            regisSucceed(response);
                        }
                    }
                    else{
                        regisApiFail(response);
                        if (!apiOnly){
                            regisFail(response);
                        }
                    }
                }catch (e: Exception){
                    regisApiError(e.toString());
                    if (!apiOnly){
                        regisError(e.toString());
                    }
                    e.printStackTrace()

                }
            }
            override fun onError(msg: String){

            }

            override fun onLoading() {

            }
        }
        var regisJson = JSONObject();
        var regisData = JSONObject();
        regisJson = regisJson.put("kd", "REG");
        regisJson = regisJson.put("userID", username);
        regisJson = regisJson.put("traceID", "traceID");

        regisData = regisData.put("pass", pass);
        regisData = regisData.put("idDevice", andLib!!.getImei());
        regisData = regisData.put("noAcc", noAcc);
        regisData = regisData.put("nmAcc", nmAcc);
        regisData = regisData.put("idMerchant", idMerchant);
        regisData = regisData.put("nmMerchant", nmMerchant);
        regisData = regisData.put("noHp", noHp);
        regisJson = regisJson.put("data", regisData);
        net.hitMainApi(regisJson, "", 0);
        return regisJson;
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        andLib = AndroidLib(this, this@SignUpActivity);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_sign_up_new);
        frame = findViewById(R.id.signup_frame_layout) as RelativeLayout;
        (findViewById(R.id.link_login) as TextView).setOnClickListener{
            var intent = Intent(this@SignUpActivity, LoginActivity::class.java);
            startActivity(intent);
        }

        (findViewById(R.id.btn_regis) as Button).setOnClickListener{
            regis();
        }
        loading = andLib!!.getLoading("");
    }
}
