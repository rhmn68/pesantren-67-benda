package com.madrasahdigital.walisantri.ppi67benda.view.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
        const val EXTRA_URL_NEWS_VIDEO = "extra_url_news_video"
        val TAG: String = DetailNewsVideoActivity::class.java.simpleName
    }

    private var post: Post? = null
    private var urlNewsVideo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_news_video)

        sharedPrefManager = SharedPrefManager(this)
        setupActionBar()
        getDataPost()
        onClick()
    }

    private fun onClick() {
        tbDetailNewsVideo.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupView(detailNewsModel: DetailNewsModel?) {
        if (detailNewsModel != null){
            tvTitleNewsVideo.text = detailNewsModel.title
            tvPublishedAtNewsVideo.text = UtilsManager.getDateAnotherFormatFromString2(detailNewsModel.publishedAt.toString())
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(tbDetailNewsVideo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getDataPost() {
        if (intent != null){
            urlNewsVideo = intent.getStringExtra(EXTRA_URL_NEWS_VIDEO)

            if (urlNewsVideo != null){
                GetDetailArticle(urlNewsVideo.toString(), sharedPrefManager!!.token).execute()
            }
        }
    }

    @SuppressLint("NewApi")
    private fun initYoutubeVideo(embedVideo: String?) {
        Log.d("DetailNewsVideoActivity", "initYoutubeVideo: embedVideo: $embedVideo")
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
                    if (embedVideo != null){
                        youTubePlayer.cueVideo(embedVideo)
                    }
                }else{
                    FirebaseCrashlytics.getInstance().setCustomKey(TAG, "1-" + "Video empty")
                }
            }

            override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
            ) {
                Log.d("coba", "error : $youTubeInitializationResult")
            }
        }

        val youtube = supportFragmentManager.findFragmentById(R.id.youtubeView) as YouTubePlayerSupportFragmentX
        youtube.initialize(getString(R.string.youtube_api_key), onInitializedListener)
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
                return true
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().setCustomKey(TAG, "1-" + e.message)
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(isSuccess: Boolean?) {
            super.onPostExecute(isSuccess)
            if (isSuccess!!){
                setDetailArticle(detailNewsModel!!.content)
                initYoutubeVideo(detailNewsModel?.embedVideo)
                setupView(detailNewsModel)
            }
        }

    }
}