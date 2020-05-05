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
        val dateString = "2020-05-04"

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentTime = Calendar.getInstance().time

        val currentString = dateFormat.format(currentTime)

        val dateCurrent = dateFormat.parse(currentString)

        val dateCreated = dateFormat.parse(createdDateString)

        val fakeDate = dateFormat.parse(dateString)

        if (fakeDate == dateCreated){
            return true
        }

        return false
    }
}