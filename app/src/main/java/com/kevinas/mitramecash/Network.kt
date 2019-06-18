package com.kevinas.mitramecash
import kotlinx.coroutines.delay
import java.io.*
import java.net.HttpURLConnection
import java.net.Socket;
import java.net.URL

open class Network<ResultType>{
    var lib = Lib();
    var dest = "127.0.0.1";
    var port = 80;

    //you implemented it by anonymous class
    open fun <resultType>onDone(result: resultType){

    }
    open fun onDone(){

    }
    open fun onError(msg: String){

    }
    open fun onLoading(){

    }

    var resultData:ResultType? = null as ResultType;
    var error: String = "";
    var errorSig: Boolean = false;

    inner class NetSend(var dest:String, var port: Int, var buffer: ByteArray, var delayMills: Long=0) : android.os.AsyncTask<String, String, ByteArray>() {
        var sock: Socket? = null;

        override fun onPreExecute() {
            super.onPreExecute()
            onLoading();
        }

        override fun doInBackground(vararg p0: String?): ByteArray {

            try {
                this.sock = Socket(dest, port);
                var outputStream:OutputStream;
                var inputStream:InputStream;
                outputStream = this.sock!!.getOutputStream();
                inputStream =  this.sock!!.getInputStream();
                outputStream.write(buffer);
                return inputStream.readBytes();
                errorSig = false;
            }catch(e: Exception){
                var msg: String = "Error while connecting to server: " + e.toString();
                errorSig = true;
                error = msg;
                onError(msg);
                return byteArrayOf();
            }
        }

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            if (errorSig){
                onError(error);
            }else{
                resultData = result as? ResultType;
                onDone();
                onDone(result);
            }
        }
    }
    inner class NetSendUrl(): android.os.AsyncTask<String, String, ByteArray>(){
        var buffer: ByteArray? = null;
        var url: String? = null;
        var delay:Long? = null
        constructor(url: String, buffer: ByteArray, delayMills: Long=0):this() {
            this.buffer = buffer;
            this.url = url;
            this.delay = delayMills;
        }
        override fun onPreExecute() {
            super.onPreExecute()
            onLoading();
        }

        override fun doInBackground(vararg p0: String?): ByteArray {
            var url:URL;
            var connection: HttpURLConnection;
            try{
                url = URL(this.url);
                connection = url.openConnection() as HttpURLConnection;
                connection.requestMethod = "POST";
                connection.outputStream.write(this.buffer);
                var result = connection.inputStream.readBytes();
                connection.disconnect();
                errorSig = false;
                return result;
            }catch (e: Exception){
                onError("Connection fail: "+e.toString());
                errorSig = true;
                return byteArrayOf();
            }
        }

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            Thread.sleep(delay!!);
            if (errorSig){
                onError(error);
            }else{
                resultData = result as? ResultType;
                onDone();
                onDone(result);
            }

        }
    }

    inner class SimulateSlow(var delayMills: Integer) : android.os.AsyncTask<String, String, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            onLoading();
        }

        override fun doInBackground(vararg p0: String?): Boolean {
            Thread.sleep(delayMills as Long);
            return true;
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            onDone();
        }
    }

    fun simulateSlow(delayMills: Integer){
        SimulateSlow(delayMills).execute();
    }


    fun send(dest:String, port: Int, buffer: ByteArray, delayMills: Long=0): android.os.AsyncTask<String, String, ByteArray>{
        this.dest = dest;
        this.port = port;
        var net: android.os.AsyncTask<String, String, ByteArray> = NetSend(dest, port, buffer, delayMills);
        net.execute();
        return net;
    }
    fun send(dest:String, buffer: ByteArray, delayMills: Long): android.os.AsyncTask<String, String, ByteArray>{
        return this.send(dest, port, buffer, delayMills);
    }
    fun sendUrl(url: String, buffer: ByteArray, delay: Long): android.os.AsyncTask<String, String, ByteArray>{
        var net: android.os.AsyncTask<String, String, ByteArray> = NetSendUrl(url, buffer, delay);
        net.execute();
        return net;
    }
}