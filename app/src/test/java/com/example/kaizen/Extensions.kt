package com.example.kaizen

import java.io.ByteArrayOutputStream
import java.io.InputStream

fun InputStream.readStreamGetString() : String {
    val outPut = ByteArrayOutputStream()
    val buff = ByteArray(1024)
    var len = read(buff)
    try {
        while (len != -1){
            outPut.write(buff,0,len)
            len = read(buff)
        }
        outPut.close()
        close()
    }catch (e : Exception){

    }
    return outPut.toString()
}