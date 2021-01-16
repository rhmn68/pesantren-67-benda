package com.madrasahdigital.walisantri.ppi67benda.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.crashlytics.android.Crashlytics
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.gson.Gson
import com.madrasahdigital.walisantri.ppi67benda.R
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.DetailNewsModel
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post
import com.madrasahdigital.walisantri.ppi67benda.utils.Constant
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager
import kotlinx.android.synthetic.main.activity_detail_news_video.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class DetailNewsVideoActivity : AppCompatActivity() {

    private var sharedPrefManager: SharedPrefManager? = null

    companion object{
        const val EXTRA_POST = "extra_post"
        val TAG = DetailNewsVideoActivity::class.java.simpleName
    }

    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news_video)

        sharedPrefManager = SharedPrefManager(this)
        setupActionBar()
        getDataPost()
        initYoutubeVideo()
        setupView()
        onClick()
    }

    private fun onClick() {
        tbDetailNewsVideo.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupView() {
        tvTitleNewsVideo.text = post.title
        tvPublishedAtNewsVideo.text = UtilsManager.getDateAnotherFormatFromString2(post.publishedAt.toString())
    }

    private fun setupActionBar() {
        setSupportActionBar(tbDetailNewsVideo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getDataPost() {
        post = intent.getSerializableExtra(EXTRA_POST) as Post
    }

    @SuppressLint("NewApi")
    private fun initYoutubeVideo() {
        val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {

            override fun onPlaying() {

            }

            override fun onPaused() {

            }

            override fun onStopped() {

            }

            override fun onBuffering(b: Boolean) {

            }

            override fun onSeekTo(i: Int) {

            }
        }

        val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {
            override fun onLoading() {

            }

            override fun onLoaded(s: String) {

            }

            override fun onAdStarted() {

            }

            override fun onVideoStarted() {

            }

            override fun onVideoEnded() {

            }

            override fun onError(errorReason: YouTubePlayer.ErrorReason) {

            }
        }

        val onInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer,
                    b: Boolean
            ) {
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener)
                youTubePlayer.setPlaybackEventListener(playbackEventListener)

                if (!b){
                    youTubePlayer.cueVideo(post.embedVideo)
                }else{
                    Crashlytics.setString(TAG, "1-" + "Video empty")
                    Log.d(TAG,"video empty")
                }
            }

            override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
            ) {
                Log.d("coba", "error : $youTubeInitializationResult")
            }
        }

        val youtube = supportFragmentManager.findFragmentById(R.id.youtubeView) as YouTubePlayerSupportFragment
        youtube.initialize(getString(R.string.youtube_api_key), onInitializedListener)

        GetDetailArticle(post.url, sharedPrefManager!!.token).execute()
    }

    private fun setDetailArticle(detail: String) {
        //set ChromeClient
        wvDescription.webViewClient = WebViewClient()
        wvDescription.setBackgroundResource(android.R.color.black)
        wvDescription.loadData(detail, "text/html", "utf-8")
    }

    inner class GetDetailArticle(urlNews: String, token: String): AsyncTask<Void, Int, Boolean>(){

        private val urlNewsWeakReference = WeakReference(urlNews)
        private val tokenWeakReference = WeakReference(token)

        private lateinit var urlNews: String
        private lateinit var token: String

        private var detailNewsModel: DetailNewsModel? = null

        override fun onPreExecute() {
            super.onPreExecute()
            urlNews = urlNewsWeakReference.get().toString()
            token = tokenWeakReference.get().toString()
        }

        override fun doInBackground(vararg p0: Void?): Boolean {
            val client = OkHttpClient.Builder()
                    .connectTimeout(Constant.TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(Constant.TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .readTimeout(Constant.TIMEOUT.toLong(), TimeUnit.SECONDS)
                    .build()

            val request: Request = Request.Builder()
                    .url(urlNews)
                    .get()
                    .addHeader(Constant.Authorization, token)
                    .build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body
                val bodyString = responseBody!!.string()
                val gson = Gson()
                detailNewsModel = gson.fromJson(bodyString, DetailNewsModel::class.java)
                Log.d("coba", "response: $bodyString")
                return true
            } catch (e: Exception) {
                Crashlytics.setString(TAG, "1-" + e.message)
                Crashlytics.logException(e)
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(isSuccess: Boolean?) {
            super.onPostExecute(isSuccess)
            if (isSuccess!!){
                setDetailArticle(detailNewsModel!!.content)
            }
        }

    }
}