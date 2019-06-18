package com.kevinas.mitramecash

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.kevinas.mitramecash.json.JSONArray
import com.kevinas.mitramecash.json.JSONObject

class DaftarMutasiRekeningActivity : AppCompatActivity() {
    var loading: AlertDialog? = null;
    var andLib: AndroidLib? = null;
    fun onListMutasiSucceed(data: JSONObject) {
        
        loading?.dismiss();
        setContentView(R.layout.activity_daftar_mutasi);
        (findViewById(R.id.btn_ok) as Button).setOnClickListener{
            var intent = Intent(this@DaftarMutasiRekeningActivity, ProfileActivity::class.java);
            startActivity(intent);
        }
        var listMutasi = (data.get("data") as JSONObject).get("listTrn") as JSONArray;
        var listLayout = findViewById<LinearLayout>(R.id.list);

        for (row in listMutasi) {
            var rowLayout = layoutInflater.inflate(R.layout.list_transaksi_row, listLayout, false);
            var column = row as JSONObject;
            rowLayout.findViewById<TextView>(R.id.release_saldo).text = column.get("Ref") as String;
            rowLayout.findViewById<TextView>(R.id.waktu).text =
                (column.get("Tanggal") as String) + (row.get("Jam") as String);
            rowLayout.findViewById<TextView>(R.id.debet).text = (column.get("Debet") as String);
            rowLayout.findViewById<TextView>(R.id.ket).text = (column.get("Ket") as String);
            listLayout.addView(rowLayout);
        }

    }

    fun onListMutasiError(msg: String) {
        
        loading?.dismiss();
    }

    fun onListMutasiFail(data: JSONObject) {
        
        loading?.dismiss();
    }

    open fun onListMutasiApiSucceed(data: JSONObject) {

    }

    open fun onListMutasiApiError(msg: String) {

    }

    open fun onListMutasiApiFail(data: JSONObject) {

    }

    fun listMutasiApi(username: String, noAcc: String, apiOnly:Boolean) {
        try {
            var net = object : MeCashLib() {
                override fun <ByteArray> onDone(data: ByteArray) {
                    
                    var json = JSONObject(String(data as kotlin.ByteArray));
                    if (json.get("rc") as String != "00") {
                        
                        onListMutasiApiFail(json);
                        if (!apiOnly) {
                            
                            onListMutasiFail(json);
                        }
                    }else {
                        
                        onListMutasiApiSucceed(json);
                        if (!apiOnly) {
                            
                            onListMutasiSucceed(json);
                        }
                    }

                }

                override fun onError(msg: String) {
                    
                    onListMutasiApiError(msg);
                    if (!apiOnly) {
                        
                        onListMutasiError(msg);
                    }
                }
            }
            var toSend = JSONObject();
            var sendData = JSONObject();
            toSend = toSend.put("userID", username);
            toSend = toSend.put("kd", "DMR");
            toSend = toSend.put("traceID", System.nanoTime());
            sendData = sendData.put("idDevice", andLib!!.getImei());
            sendData = sendData.put("noAcc", noAcc);

            toSend.put("data", sendData);

            net.hitMainApi(toSend, "", 0);

        } catch (e: Exception) {
            
            onListMutasiApiError(e.toString());
            if (!apiOnly) {
                
                onListMutasiError(e.toString());
            }
        }

    }
    override fun onBackPressed(){
        var intent = Intent(this@DaftarMutasiRekeningActivity, HomeActivity::class.java);
        startActivity(intent);
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        andLib = AndroidLib(this@DaftarMutasiRekeningActivity, this);
        loading = andLib!!.getLoading("");
        loading?.show();
        listMutasiApi(andLib!!.getData("userID"), andLib!!.getData("noAcc"), false);
    }
}