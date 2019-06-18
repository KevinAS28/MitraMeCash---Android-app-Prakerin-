package com.kevinas.mitramecash

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import com.kevinas.mitramecash.json.JSONObject

open class LoginActivity : AppCompatActivity() {
    var aes: AES = AES();
    var andLib: AndroidLib? = null;
    var loading: AlertDialog? = null;
    fun loginFail(data: JSONObject) {
        println("loginFail() called");
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.login_error_msg) as TextView;
        var runnable = object : Runnable {
            override fun run() {
                error!!.setText("Username or Password incorrect. please check again");
                error.visibility = View.VISIBLE;
                loading!!.dismiss();
            }
        }
        mainHandler.post(runnable);
    }

    fun loginError(msg: String) {
        println("loginError() called");
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.login_error_msg) as TextView;
        var runnable = object : Runnable {
            override fun run() {
                error!!.setText("Connection failed: " + msg);
                error.visibility = View.VISIBLE;
                loading!!.dismiss();
            }
        }
        mainHandler.post(runnable);
    }

    fun loginSucceed(data: JSONObject) {
        println("loginSucceed() called");
        andLib!!.setData("userID", data.get("userID") as String);
        var loginContext: Context = this;
        var homeIntent = Intent(loginContext, HomeActivity::class.java);
        var mainHandler = Handler(baseContext.mainLooper);
        var error = findViewById(R.id.login_error_msg) as TextView;
        var runnable = object : Runnable {
            override fun run() {
                loading!!.dismiss();
                error.visibility = View.INVISIBLE;
            }
        }
        mainHandler.post(runnable);
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fadein)
        anim.duration = 500
        anim.start();
        startActivity(homeIntent);
    }

    open fun loginApiFail() {

    }

    open fun loginApiError(msg: String) {

    }

    open fun loginApiSucceed(data: JSONObject) {

    }

    fun login() {
        var uname: TextInputEditText = findViewById(R.id.txt_username) as TextInputEditText;
        var pass: TextInputEditText = findViewById(R.id.txt_password) as TextInputEditText;
        loading!!.show();
        loginApi(uname.text.toString(), pass.text.toString(), false);
    }

    fun loginApi(username: String, password: String, apionly: Boolean = true): JSONObject {
        var net = object : MeCashLib() {
            override fun <ByteArray> onDone(result: ByteArray) {
                try {
                    var loginApiReturn = JSONObject(String(aes.decrypt((result as kotlin.ByteArray))));
                    if (loginApiReturn.get("rc") == "00") {
                        //write the pass to file
                        andLib.setData("pass", password);
                        loginApiSucceed(loginApiReturn);
                        if (!apionly) {
                            loginSucceed(loginApiReturn);
                        }
                    } else {
                        loginApiFail();
                        if (!apionly) {
                            loginFail(loginApiReturn);
                        }
                    }
                } catch (e: Exception) {
                    loginApiError(e.toString());
                    if (!apionly) {
                        loginError(e.toString());
                    }
                }
            }

            override fun onError(msg: String) {
                loginApiError(msg);
                if (!apionly) {
                    loginError(msg);
                }
            }
        }

        var authJson = JSONObject();
        var authDataJson = JSONObject();
        authJson = authJson.put("KD", "SLD");
        authJson = authJson.put("userid", username);
        authJson = authJson.put("pass", password);
        authJson = authJson.put("traceid", System.nanoTime());
        authDataJson = authDataJson.put("idDevice", andLib!!.getImei());
        authJson = authJson.put("data", authDataJson);
        net.andLib = andLib!!;
        net.hitMainApi(authJson, "", 0);

        return authJson;

    }

    override fun onBackPressed() {
        andLib!!.exitAppFull();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        andLib = AndroidLib(this@LoginActivity, this);
        loading = andLib!!.getLoading("");
        var login: Button = findViewById(R.id.btn_login) as Button;
        var register: TextView = andLib!!.underScoreTextView(findViewById(R.id.link_signup) as TextView);
        login.setOnClickListener {
            login();
        }
        register.setOnClickListener {
            var intent = Intent(this@LoginActivity, SignUpActivity::class.java);
            startActivity(intent);

        }

    }
}