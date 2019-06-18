//library lama

package com.kevinas.mitramecash

import com.kevinas.mitramecash.json.JSONArray
import com.kevinas.mitramecash.json.JSONObject
import java.io.*
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.Arrays
import java.util.Formatter
import java.util.HashMap

class Lib {

    companion object {
        fun <Any> debug(msg: Any) {
            println("Debug: " + msg.toString());
        }

        fun <E> toArrayList(any: Array<E>): ArrayList<E> {
            val to_return = ArrayList<E>()
            for (i in any.indices) {
                to_return.add(any[i])
            }
            return to_return
        }


        fun charToByte(convert: CharArray): ByteArray {
            val cb = CharBuffer.wrap(convert)
            val bb = Charset.forName("UTF-8").encode(cb)
            val bytes = Arrays.copyOfRange(bb.array(), bb.position(), bb.limit())
            Arrays.fill(cb.array(), '\u0000')
            Arrays.fill(bb.array(), 0.toByte())
            return bytes
        }

        fun Bytetobyte(convert: Array<Byte>): ByteArray {
            val yay = ByteArray(convert.size)
            var i = 0
            for (a in convert) {
                yay[i++] = a
            }
            return yay
        }


        fun mergeString(any: Array<String>): String {
            var to_return = ""
            for (i in any.indices) {
                to_return += any[i]
            }
            return to_return
        }


        fun wait(sec: Int) {
            var sec = sec
            sec *= 1000
            try {
                Thread.sleep(sec.toLong())
            } catch (err: Exception) {
            }

        }

        internal fun trim(bytes: ByteArray): ByteArray {
            var i = bytes.size - 1
            while (i >= 0 && bytes[i].toInt() == 0) {
                --i
            }
            return Arrays.copyOf(bytes, i + 1)
        }

        fun countInStr(a: String, b: String): Int {
            var temp = ""
            var x = 0
            var i = 0
            while (i < b.length) {
                if (b[i] == a[0]) {
                    //possibility
                    for (c in i until i + a.length) {
                        try {
                            temp += b[c]
                        } catch (err: Exception) {

                        }

                    }
                    //check. pake == kok false ya\
                    var benar = 0
                    for (e in 0 until a.length) {
                        if (a[e] == temp[e]) {
                            benar++
                        }
                    }
                    if (benar == a.length) {
                        x++
                        i += a.length - 1
                    }
                }
                i++
            }
            return x
        }

        fun countchr(s: String, c: Char): Int {
            var a = 0
            for (i in 0 until s.length) {
                if (s[i] == c) {
                    ++a
                }
            }
            return a
        }

        fun splitstr(s: String, tosplit: Char): Array<String> {
            var s = s
            s += tosplit
            val to_return = ArrayList<String>();
            var temp = ""
            var a = 0

            for (i in 0 until s.length) {
                val periksa = s[i]
                if (periksa == tosplit) {
                    to_return[a] = temp
                    temp = ""
                    a += 1
                } else {
                    temp += periksa.toString()
                }
            }

            return to_return.toArray() as Array<String>;
        }
    }

//original lib


    //Library CSBO
    var log: MutableList<String> = ArrayList()

    fun range(stop: Int): List<Int> {
        val toreturn = ArrayList<Int>()
        for (i in 0 until stop) {
            toreturn.add(i)
        }
        return toreturn
    }


    fun range(start: Int, stop: Int): List<Int> {
        var start = start
        val toreturn = ArrayList<Int>()
        while (start < stop) {
            toreturn.add(start)
            start++
        }
        return toreturn
    }

    fun range(start: Int, stop: Int, step: Int): List<Int> {
        var start = start
        val toreturn = ArrayList<Int>()
        while (start < stop) {
            toreturn.add(start)
            start += step
        }
        return toreturn
    }

    fun jsonArrayToList(jsonarray: JSONArray): List<String> {
        val toreturn = ArrayList<String>()
        for (x in jsonarray) {
            toreturn.add(x as String)
        }
        return toreturn
    }

    fun <Type0, Type1> mergeArray(arr0: Array<Type0>, arr1: Array<Type1>): HashMap<Type0, Type1> {
        val toreturn = HashMap<Type0, Type1>()
        for (i in arr0.indices) {
            toreturn[arr0[i]] = arr1[i]
        }
        return toreturn
    }

    fun cmpStr(str0: String, str1: String): Boolean {
        if (str0.length != str1.length) {
            return false
        }
        for (i in 0 until str0.length) {
            if (str0[i] != str1[i]) {
                return false
            }
        }
        return true
    }

    fun <T> listMapToJSON(x: List<Map<String, T>>): JSONObject {
        var json = JSONObject()
        val a = ArrayList<JSONObject>()
        for (y in x) {
            var c = JSONObject()

            for (b in y.keys) {
                c = c.put(b, y[b])
            }
            a.add(c)
        }
        json = json.put("data", a)
        return json
    }

    fun <T> arrayToList(x: Array<T>): List<T> {
        val toReturn = ArrayList<T>()
        for (i in x) {
            toReturn.add(i)
        }
        return toReturn
    }

    fun decodeHttp(str: String): String {
        try {
            return java.net.URLDecoder.decode(str, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            return "error decode http"
        }

    }

    fun parseHttp(data: String): Map<String, String> {
        return parseHttp(data, true)
    }

    fun parseHttp(data: String, httpDecode: Boolean): Map<String, String> {
        var temp = ""
        val x = HashMap<String, String>()
        val split_and = data.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var split_eq: Array<String>
        for (i in split_and) {
            split_eq = i.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (a in split_eq.indices) {
                if (httpDecode) {
                    try {
                        split_eq[a] = java.net.URLDecoder.decode(split_eq[a], "UTF-8")
                    } catch (e: UnsupportedEncodingException) {

                    }

                }
                try {
                    temp = split_eq[1]
                } catch (e: IndexOutOfBoundsException) {
                    continue
                }

                x[split_eq[0]] = split_eq[1]
            }
        }
        return x
    }
    fun <Type0, Type1>mapGenerate(array0: Array<Type0>, array1: Array<Type1>): HashMap<Type0, Type1>{
        var toReturn: HashMap<Type0, Type1>  = HashMap();
        for (i: Int in range(array0.size)){
            toReturn.put(array0[i], array1[i]);
        }
        return toReturn;
    }
    fun <Type>fusionArray(a0: Array<Type>, a1: Array<Type>): ArrayList<Type>{
        var toReturn = ArrayList<Type>();
        for (i : Type in a0){
            toReturn.add(i);
        }
        for (i : Type in a1){
            toReturn.add(i);
        }
        return toReturn;
    }
}



