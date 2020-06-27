package com.madrasahdigital.walisantri.ppi67benda.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateHelper{
    @JvmStatic
    fun getCurrentDate(): String{
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    @JvmStatic
    fun compareDateForShowArticles(publishedAt: String, lastAccessDate: String): Boolean{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//        val fakeDate = "2020-06-22 11:56:00"
        val currentTime = Calendar.getInstance().time

        val dateCurrentString = dateFormat.format(currentTime)

        val dateCurrentCreated = dateFormat.parse(dateCurrentString)
        val datePublishedCreated = dateFormat.parse(publishedAt)
        val dateLastAccessCreated = dateFormat.parse(lastAccessDate)

        datePublishedCreated?.let {
            if (datePublishedCreated in dateLastAccessCreated..dateCurrentCreated){
                return true
            }
        }

        return false
    }

    fun compareDateAfter(lastAccessDate: String): Boolean{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTime = Calendar.getInstance().time

        val currentString = dateFormat.format(currentTime)

        val dateCurrentCreated = dateFormat.parse(currentString)
        val dateLastAccessCreated = dateFormat.parse(lastAccessDate)

        dateCurrentCreated?.let {
            if (dateCurrentCreated.after(dateLastAccessCreated)){
                return true
            }
        }

        return false
    }
}