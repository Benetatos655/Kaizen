package com.example.kaizen.extensions

import java.util.concurrent.TimeUnit

fun Long.convertTimeToString(): String?  {
    return java.lang.String.format(
        "%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(this)
        ),
        TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(this)
        )
    )
}

fun <T>MutableCollection<T>.addOrRemove(item : T){
    if (contains(item)){
        remove(item)
    }else{
        add(item)
    }
}