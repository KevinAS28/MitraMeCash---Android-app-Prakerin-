package com.kevinas.mitramecash

import android.app.Activity
import android.content.Context
import android.provider.Settings
import com.kevinas.mitramecash.json.*;

open class MeCashLib:Network<ByteArray>{
    var andLib: AndroidLib = AndroidLib();
    var context: Context? = null;
    constructor(){}
    constructor(context: Context){
        this.context = context;
        this.andLib = AndroidLib(context);
    }
    constructor(context: Context, activity: Activity){
        this.andLib = AndroidLib()
        this.andLib = AndroidLib(context, activity);
    }
    constructor(activity: Activity){
        this.andLib = AndroidLib(activity);
    }
    fun getEMoney(username: String){
        var json = JSONObject();
        var data = JSONObject();
        json = json.put("userID", username);
        json = json.put("kd", "SLD");
        json = json.put("traceId", System.nanoTime());
        data = data.put("idDevice", andLib.getImei());
        json.put("data", data);
        this.hitMainApi(json, "");
    }

    fun hitMainApi(json: JSONObject, contextUrl: String){
        this.hitMainApi(json, contextUrl, 0);
    }
    fun hitMainApi(json: JSONObject, contextUrl: String, delay: Long){
        json.put("appid", "");
        json.put("timestamp", "");
        json.put("signature", "");
        this.send(Configuration.serverIP, Configuration.serverPort, json.toString().toByteArray(), delay);
    }
    fun logout(){
            andLib.delete(Configuration.listFile.get("dat")!!);
            Temp.data = JSONObject();
            andLib.restartApp();
    }
}