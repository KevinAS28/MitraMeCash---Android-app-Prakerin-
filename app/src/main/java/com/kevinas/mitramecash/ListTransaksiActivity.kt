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

class ListTransaksiActivity : AppCompatActivity() {
    var loading: AlertDialog? = null;
    var andLib: AndroidLib? = null;
    fun onListTransaksiSucceed(data: JSONObject) {
        
        loading?.dismiss();
        setContentView(R.layout.activity_daftar_mutasi);
        findViewById<Button>(R.id.btn_ok).setOnClickListener{
            var intent = Intent(this@ListTransaksiActivity, ProfileActivity::class.java);
            startActivity(intent);
        }
        var listTransaksi = (data.get("data") as JSONObject).get("listTrn") as JSONArray;
        var listLayout = findViewById<LinearLayout>(R.id.list);

        for (row in listTransaksi) {
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

    fun onListTransaksiError(msg: String) {
        
        loading?.dismiss();
    }

    fun onListTransaksiFail(data: JSONObject) {
        
        loading?.dismiss();
    }

    open fun onListTransaksiApiSucceed(data: JSONObject) {

    }

    open fun onListTransaksiApiError(msg: String) {

    }

    open fun onListTransaksiApiFail(data: JSONObject) {

    }

    fun listTransaksiApi(
        username: String,
        pin: String,
        tgl: String,
        jamAwal: String,
        jamAkhir: String,
        apiOnly: Boolean = true
    ) {
        try {
            var net = object : MeCashLib() {
                override fun <ByteArray> onDone(data: ByteArray) {
                    
                    var json = JSONObject(String(data as kotlin.ByteArray));
                    if (json.get("rc") as String != "00") {
                        
                        onListTransaksiApiFail(json);
                        if (!apiOnly) {
                            
                            onListTransaksiFail(json);
                        }
                    }else {
                        
                        onListTransaksiApiSucceed(json);
                        if (!apiOnly) {
                            
                            onListTransaksiSucceed(json);
                        }
                    }

                }

                override fun onError(msg: String) {
                    
                    onListTransaksiApiError(msg);
                    if (!apiOnly) {
                        
                        onListTransaksiError(msg);
                    }
                }
            }
            var toSend = JSONObject();
            var sendData = JSONObject();
            toSend = toSend.put("userID", username);
            toSend = toSend.put("kd", "LTR");
            toSend = toSend.put("traceID", System.nanoTime());

            sendData = sendData.put("idDevice", andLib!!.getImei());
            sendData = sendData.put("pin", pin);
            sendData = sendData.put("tgl", tgl);
            sendData = sendData.put("jamAwal", jamAwal);
            sendData = sendData.put("jamAkhir", jamAkhir);

            toSend.put("data", sendData);

            net.hitMainApi(toSend, "", 0);

        } catch (e: Exception) {
            
            onListTransaksiApiError(e.toString());
            if (!apiOnly) {
                
                onListTransaksiError(e.toString());
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        andLib = AndroidLib(this@ListTransaksiActivity, this);
        loading = andLib!!.getLoading("");
        setContentView(R.layout.activity_list_trx);
        (findViewById(R.id.btn_ok) as Button).setOnClickListener {

            var tgl = findViewById<TextView>(R.id.txt_tgl).text.toString();
            var jam0 = findViewById<TextView>(R.id.txt_jam1).text.toString();
            var jam1 = findViewById<TextView>(R.id.txt_jam2).text.toString();
            var getPin = object : InputPin(this@ListTransaksiActivity) {
                override fun onDone(pin: String) {
                    
                    loading?.show();
                    listTransaksiApi(andLib!!.getData("userID"), tgl, jam0, jam1, pin, false);
                }
            };
            getPin.getPin(6);

        }

    }

}