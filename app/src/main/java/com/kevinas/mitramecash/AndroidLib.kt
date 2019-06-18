package com.kevinas.mitramecash

import android.Manifest.permission.*
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.kevinas.mitramecash.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class AndroidLib() : AppCompatActivity() {
    companion object {
        var mainActivity: Activity? = null
            set(value) {
                field = value;
            }
            get() {
                if (field == null) {
                    throw Exception("You need to set main activity first");
                } else {
                    return field;
                }
            };

    }

    var lib = Lib();
    var context: Context? = null
        set(value) {
            field = value;
        }
        get() {
            if (field == null) {
                throw Exception("AndroidLib need context from another activity");
            } else {
                return field;
            }
        };
    var activity: Activity? = null
        set(value) {
            field = value;
        }
        get() {
            if (field == null) {
                throw Exception("AndroidLib need activity instance (this) from another activity");
            } else {
                return field;
            }
        }

    constructor(context: Context) : this() {
        this.context = context;
    }

    constructor(context: Context, activity: Activity) : this() {
        this.context = context;
        this.activity = activity;
    }

    constructor(context: Context, activity: Activity, mainActivity: Activity) : this() {
        this.context = context;
        this.activity = activity;
        AndroidLib.mainActivity = mainActivity;
    }

    constructor(activity: Activity) : this() {
        this.activity = activity;
    }

    fun getImei(): String {
        var perm = mutableListOf(READ_PHONE_STATE);
        if (checkPermissions(perm)[0]) {
            val tel = activity!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager;
            try {
                var imei: String;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = tel.getImei();
                } else {
                    imei = tel.getDeviceId();
                }
                return imei;
            } catch (e: SecurityException) {
                throw Exception("need READ_PHONE_STATE");
            }
        }
        throw Exception("need READ_PHONE_STATE");
    }

    fun setStatusBarColor(color: Int) {
        var window: Window = activity!!.window;

        //clear FLAG_TRANSLUCENT_STATUS
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(ContextCompat.getColor(context!!, color));
    }


    fun getStatusBarColor(): String {
        var window: Window = activity!!.window;
        var color: Int = window.statusBarColor;
        return String.format("", 0xFFFFFF and color);
    }

    fun dialog(msg: String, title: String): AlertDialog.Builder {
        var builder = AlertDialog.Builder(this.activity);
        builder.setMessage(msg).setTitle(title);
        builder.create();
        return builder;
    }

    fun restartApp() {
        this.activity!!.finishAndRemoveTask();
        var intent = activity!!.baseContext.packageManager.getLaunchIntentForPackage(activity!!.baseContext.packageName);
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity!!.startActivity(intent);
    }

    fun exitApp() {
        var homeIntent = Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AndroidLib.mainActivity!!.startActivity(homeIntent);
    }

    fun exitAppFull() {
        AndroidLib.mainActivity!!.finish();
        AndroidLib.mainActivity!!.finishAndRemoveTask()
        AndroidLib.mainActivity!!.finishAffinity();
        System.exit(0);
    }

    fun checkPermissions(perm: MutableList<String>): Array<Boolean> {
        var result = Array(perm.size, { false });
        var index = 0;
        for (m: String in perm) {
            if (ContextCompat.checkSelfPermission(this.context!!, m) != PackageManager.PERMISSION_GRANTED) {
                result[index] = false;
            } else {
                result[index] = true;
            }
            index += 1;
        }
        return result;
    }

    fun requestPermisssions() {
        var permissionCode = 200;
        var permission = arrayOf(INTERNET, WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE);
        ActivityCompat.requestPermissions(activity!!, permission, permissionCode);
    }


    fun getFileAddress(file: String): String {
        val path0 = context!!.filesDir
        val path1 = File(path0, file)
        return path1.path
    }

    fun checkFile(the_file: String): Boolean {
        val path = context!!.filesDir
        val test = File(path, the_file)
        return test.exists()
    }

    fun writeFile(file: String, to_input: String): Boolean {
        val path = context!!.filesDir
        if (!checkFile(file)) {
            createFile(file)
        }
        val filee = File(path, file)
        try {
            val out = FileOutputStream(filee)
            out.write(to_input.toByteArray())
            out.close()
            return true
        } catch (err: Exception) {
            return false
        }

    }

    fun appendFile(file: String, buffer: ByteArray): Boolean {
        if (!checkFile(file)) {
            createFile(file)
        }
        try {
            var buffer = lib.fusionArray(readFileByte(file).toTypedArray(), buffer.toTypedArray()).toByteArray();
            writeFile(file, buffer);
            return true;
        } catch (e: Exception) {
            return false;
        }

    }

    fun writeFile(file: String, to_input: ByteArray): Boolean {
        val path = context!!.filesDir
        if (!checkFile(file)) {
            createFile(file)
        }
        val filee = File(path, file)
        try {
            val out = FileOutputStream(filee)
            out.write(to_input)
            out.close()
            return true
        } catch (err: Exception) {
            return false
        }
    }

    fun createFile(file: String): Boolean {
        try {
            val path = context!!.filesDir
            File(path, file);
            return true;
        } catch (yay: Exception) {
            return false;
        }

    }

    fun readFileByte(file: String): ByteArray {
        val path = context!!.filesDir
        if (!checkFile(file)) {
            createFile(file)
        }
        val filee = File(path, file)
        val length = filee.length().toInt()
        val bytes = ByteArray(length)
        try {
            val inn = FileInputStream(filee)
            inn.read(bytes)

            return bytes
        } catch (err: Exception) {
            return "Error ReadFileByte".toByteArray()
        }

    }

    fun delete(file: String): Boolean {
        val path = context!!.filesDir
        if (!checkFile(file)) {
            return true;
        }
        val filee = File(path, file)
        try {
            filee.delete();
            return true
        } catch (err: Exception) {
            return false;
        }
    }

    fun readFile(file: String): String {
        val path = context!!.filesDir
        if (!checkFile(file)) {
            createFile(file)
        }
        val filee = File(path, file)
        val length = filee.length().toInt()
        val bytes = ByteArray(length)
        try {
            val inn = FileInputStream(filee)
            inn.read(bytes)
            return String(bytes)
        } catch (err: Exception) {
            return "Error ReadFile"
        }
    }


    fun underScoreTextView(tv: TextView): TextView {
        tv.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
        return tv;
    }

    fun imgFull(img: ImageView): View {
        BaseActivity.fullImage.add(img);
        var intent = Intent(activity!!, ImageFullScreenActivity::class.java);
        activity!!.startActivity(intent);
        return img;
    }

    fun snack(view: View, msg: String) {
        var snack: Snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snack.show();
    }

    fun copy(text: String) {
        (BaseActivity.clipboard)?.primaryClip = ClipData.newPlainText("text", text);
    }

    fun copySnack(text: String, msg: String = "Copied", view: View) {
        this.copy(text);
        this.snack(view, msg);
    }

    fun getLoading(
        title: String,
        message: String = "Sabar adalah kunci kesuksesan",
        cancelAble: Boolean = false
    ): AlertDialog {
        var msg = message;
        var loadingMessage = arrayOf(
            "Please Wait..."
            //"Orang sabar disayang tuhan",
            //"Kesabaran anda adalah kualitas kami"
        );
        if (msg == "") {
            msg = loadingMessage[Random().nextInt(loadingMessage.size)];
        }
        var builder = AlertDialog.Builder(this.activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setView(R.layout.activity_loading_portrait);
        var build = builder.create();
        build.setCancelable(cancelAble);
        return build;
    }

    val datFile = "dat";
    //val sourceData = arrayOf(Temp.data, readFile(Configuration.listFile.get(datFile)!!));
    //those function is for set mmodify from multiple source
    //ex: when the application can be offline, you can modify them
    fun setData(key: String, value: String): Boolean {
        Temp.data = Temp.data.put(key, value);
        this.writeFile(Configuration.listFile.get(datFile)!!, Temp.data.toString().toByteArray());
        return true;
    }

    fun setData(data: JSONObject): Boolean {
        var json: JSONObject? = null;
        try {
            json = JSONObject(readFile(Configuration.listFile.get(datFile)!!));
        } catch (msg: Exception) {
            json = JSONObject();
        }
        for (i: String in Temp.data.keySet()) {
            json = json!!.put(i, Temp.data.get(i));
        }
        for (i: String in data.keySet()) {
            json = json!!.put(i, data.get(i));
        }
        Temp.data = json!!;
        this.writeFile(Configuration.listFile.get(datFile)!!, Temp.data.toString().toByteArray());
        return true;
    }

    fun getData(key: String): String {
        var data = readFile(Configuration.listFile.get(datFile)!!);
        var jsonData = JSONObject(data);
        try {
            return jsonData.get(key) as String;
        } catch (e: Exception) {
            try {
                return Temp.data.get(key) as String;
            } catch (e: Exception) {
                return "";
            }
        }
    }

    fun getDataAddr(address: String): String {
        var data = readFile(Configuration.listFile.get(datFile)!!);
        try {
            var addr = address.split("/");
            var jsonData: JSONObject;
            jsonData = JSONObject(data);

            for (i: Int in 0.rangeTo(addr.size - 2)) {
                println(jsonData.toString());
                println(addr[i]);
                println((jsonData.get(addr[i]) as JSONObject).toString())
                println("\n\n");
                jsonData = (jsonData.get(addr[i]) as JSONObject);
            }
            return jsonData.get(addr[addr.size - 1]) as String;
        }catch (e: Exception){
            println(e.printStackTrace());
        }
        return "No data";
    }
}