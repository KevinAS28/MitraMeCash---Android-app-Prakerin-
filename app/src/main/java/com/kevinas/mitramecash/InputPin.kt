package com.kevinas.mitramecash

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

open class InputPin{
    var activity: Activity? = null
    constructor (activity: Activity){
        this.activity = activity;
    }
    open fun onDone(pin: String){

    }
    fun getPin(maxLength: Int){
        activity!!.setContentView(R.layout.activity_pin);
        var pin = activity!!.findViewById(R.id.txtPIN) as TextInputEditText;
        pin.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable){
                println("berubah jadi $s");
                if (s.toString().length==maxLength){
                    onDone(s.toString());
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })
    }
}