package com.madrasahdigital.walisantri.ppi67benda.repository

import android.content.Context
import com.google.gson.Gson
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.NewsModel
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post
import java.io.IOException

object RepoNewsVideo {

    fun getDataNewsVideo(context: Context): List<Post>?{
        val json: String?
        try {
            val inputStream = context.assets?.open("json/posts.json")

            json = inputStream?.bufferedReader().use{ it?.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        val newsModel = Gson().fromJson(json, NewsModel::class.java)
        return newsModel.posts
    }
}