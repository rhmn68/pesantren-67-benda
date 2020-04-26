package com.madrasahdigital.walisantri.ppi67benda.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper{
    fun getCurrentDate(): String{
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun compareDateForShowArticles(createdDateString: String): Boolean{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentTime = Calendar.getInstance().time

        val createdDate = dateFormat.parse(createdDateString)

        if (currentTime == createdDate){
            return true
        }

        return false
    }
}