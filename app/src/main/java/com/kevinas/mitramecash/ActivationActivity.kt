package com.kevinas.mitramecash

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.kevinas.mitramecash.json.JSONObject

class ActivationActivity: AppCompatActivity(){
    var aes: AES = AES();
    var andLib : AndroidLib? = null;
    var frame: RelativeLayout? = null;
    var loading: AlertDialog? = null;
    var savedInstanceState: Bundle? = null;
    var wrong = 0;
    var maxWrong = 3;
    fun activationFail(){
        println("activationFail() called");
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.activation_error_msg) as TextView;
        var runnable = object: Runnable{
            override fun run(){
                wrong++;
                if (wrong>maxWrong){
                    wrong=0;
                    var intent = Intent(this@ActivationActivity, SignUpActivity::class.java);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                loading?.dismiss();
                error!!.setText("Wrong code. please try again");
                error!!.visibility = View.VISIBLE;
            }
        }
        mainHandler.post(runnable);
    }
    fun activationError(msg: String){

        println("activationError() called");
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.activation_error_msg) as TextView;
        var runnable = object: Runnable{
            override fun run(){
                loading?.dismiss();
                error!!.setText("Connection failed: "+msg );
                error!!.visibility = View.VISIBLE;
            }
        }
        mainHandler.post(runnable);
    }
    fun activationSucceed(data: JSONObject){
        println("activationSucceed() called");
        andLib!!.writeFile(Configuration.listFile.get("login")!!, data.toString());
        var activationContext: Context = this;
        var homeIntent = Intent(activationContext, HomeActivity::class.java);
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.activation_error_msg) as TextView;
        var runnable = object: Runnable{
            override fun run(){
                error!!.visibility = View.INVISIBLE;
                loading?.dismiss();

                //instead redirect user to login, just let him in because he already type username and password in register
                var loginActivity: LoginActivity = object : LoginActivity() {

                    override fun loginApiSucceed(data: JSONObject) {
                        var intent = Intent(this@ActivationActivity, HomeActivity::class.java);
                        startActivity(intent);
                    }

                    override fun loginApiError(msg: String) {
                        andLib!!.snack(frame as View, "Data broken. connection to slow?");

                    }

                    override fun loginApiFail() {
                        andLib!!.snack(frame as View, "Register Failed");
                    }
                };
                loginActivity.andLib = andLib;
                loginActivity.loginApi(
                    getIntent().getStringExtra("userID"), getIntent().getStringExtra("pass")
                );
            }
        }
        mainHandler.post(runnable);

        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fadein)
        anim.duration = 500
        anim.start();
        startActivity(homeIntent);
    }
    open fun activationApiFail(data: JSONObject){

    }
    open fun activationApiError(msg: String){

    }
    open fun activationApiSucceed(data: JSONObject){

    }

    fun activation(){
        var pin = (findViewById(R.id.txt_pin) as EditText);
        var username = getIntent().getStringExtra("userID");
        var pass = getIntent().getStringExtra("pass");
        var kdReg = getIntent().getStringExtra("kdReg")!!;
        var api = activationApi(username, kdReg, pin.text.toString(), false);
        andLib!!.setData("userID", username);
        pin.text = SpannableStringBuilder("");
        loading?.show();
        loading!!.setOnCancelListener {

        }
    }


    fun activationApi(username: String, kdReg: String, pin: String, apionly:Boolean=true): JSONObject{
        var net = object: MeCashLib(){
            override fun<ByteArray>onDone(result: ByteArray) {
                try{
                    var activationApiReturn = JSONObject(String(aes.decrypt((result as kotlin.ByteArray))));
                    if (activationApiReturn.get("rc")=="00"){
                        activationApiSucceed(activationApiReturn);
                        if (!apionly){
                            activationSucceed(activationApiReturn);
                        }
                    }
                    else{
                        activationApiFail(activationApiReturn);
                        if (!apionly){
                            activationFail();
                        }
                    }
                }
                catch (e: Exception){
                    activationApiError(e.toString());
                    if (!apionly){
                        activationError(e.toString());
                    }
                }
            }
            override fun onError(msg: String){
                activationApiError(msg);
                if (!apionly){
                    activationError(msg);
                }
            }
        }
        var activationJson = JSONObject();
        var activationData = JSONObject();

        activationJson = activationJson.put("kd", "AKV");
        activationJson = activationJson.put("userID", username);
        activationJson = activationJson.put("traceID", System.nanoTime());

        activationData = activationData.put("idDevice", andLib!!.getImei());
        activationData = activationData.put("kdReg", kdReg);
        activationData = activationData.put("pin", pin);

        activationJson = activationJson.put("data", activationData);

        net.hitMainApi(activationJson, "", 0);

        return activationJson;
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivasi);
        andLib = AndroidLib(this@ActivationActivity, this);
        frame = findViewById(R.id.aktivasi_frame_layout);
        this.savedInstanceState = savedInstanceState;
        loading = andLib!!.getLoading("Loading...");
        var pin = findViewById(R.id.txt_pin) as EditText;
        pin.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable){
                println("berubah jadi $s");
                if (s.toString().length==6){
                    activation();
                }

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })


    }
}
