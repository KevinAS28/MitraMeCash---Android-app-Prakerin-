package com.kevinas.mitramecash

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec

class AES {

    private val AES_CBS_PADDING = "AES/CBC/NoPadding"

    var ALGORITHM = "AES"

    var cipher = "4KiZkbVkFerMZGT3".toByteArray()

    var cipher_iv = "2s7EMLxg4I4fBlGw".toByteArray()

    fun decrypt(data: ByteArray): ByteArray {
        return data;
        try {
            return depadding(decrypt(cipher, cipher_iv, data))
        } catch (err: Exception) {
            return "Error Decrypt".toByteArray()
        }

    }

    fun toprintable(data: ByteArray): String {
        val printable =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ "
        var to_return = ""
        for (a in data) {
            for (i in 0 until printable.length) {
                val charnya = printable[i]
                if (charnya == a.toChar()) {
                    to_return += charnya
                    break
                }
            }
        }
        return to_return
    }

    fun encrypt(data: ByteArray): ByteArray {
        return data;
        try {
            return encrypt(cipher, cipher_iv, data)
        } catch (err: Exception) {
            return "Error Encrypt".toByteArray()
        }

    }

    fun depadding(a: ByteArray): ByteArray {
        val to_return = ByteArray(a.size)
        for (i in a.indices) {
            val apa = a[i]
            if (apa.toInt() <= 0) {
                break
            }
            if ('�'.toInt() == apa.toInt()) {
                break
            }
            to_return[i] = apa
        }
        return to_return
    }

    fun padding(a: ByteArray): ByteArray {
        var jumlah = a.size
        while (jumlah % 16 != 0) {
            jumlah++
        }
        val to_return = ByteArray(jumlah)
        for (i in a.indices) {
            to_return[i] = a[i]
        }
        val pad: Byte = 0
        for (i in a.size until jumlah - a.size) {
            to_return[i] = pad
        }
        return to_return
    }

    @Throws(Exception::class)
    fun encrypt(key: ByteArray, IV: ByteArray, message: ByteArray): ByteArray {
        return encryptDecrypt(Cipher.ENCRYPT_MODE, key, IV, padding(message))
    }

    @Throws(Exception::class)
    fun decrypt(key: ByteArray, IV: ByteArray, message: ByteArray): ByteArray {
        return depadding(encryptDecrypt(Cipher.DECRYPT_MODE, key, IV, message))
    }

    fun toCharBuff(byt: ByteArray): CharArray {
        val to_return = CharArray(byt.size)
        for (i in byt.indices) {
            val r = (byt[i].toInt() + 256).toChar()
            to_return[i] = r
        }
        return to_return
    }

    @Throws(Exception::class)
    private fun encryptDecrypt(mode: Int, key: ByteArray, IV: ByteArray, message: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(AES_CBS_PADDING)
        val keySpec = SecretKeySpec(key, ALGORITHM)
        val ivSpec = IvParameterSpec(IV)
        cipher.init(mode, keySpec, ivSpec)
        return cipher.doFinal(message)
    }

}