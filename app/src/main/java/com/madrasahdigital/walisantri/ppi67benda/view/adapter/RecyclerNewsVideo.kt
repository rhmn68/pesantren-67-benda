package com.madrasahdigital.walisantri.ppi67benda.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madrasahdigital.walisantri.ppi67benda.R
import com.madrasahdigital.walisantri.ppi67benda.model.newsmodel.Post
import com.madrasahdigital.walisantri.ppi67benda.utils.UtilsManager
import kotlinx.android.synthetic.main.item_news_v2.view.*

class RecyclerNewsVideo(private val posts: List<Post>) : RecyclerView.Adapter<RecyclerNewsVideo.ViewHolder>(){

    private lateinit var listener: (Post) -> Unit

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(post: Post, listener: (Post) -> Unit) {
            Glide.with(itemView)
                .load(post.featuredImage)
                .centerCrop()
                .placeholder(R.drawable.bg_silver)
                .error(R.drawable.bg_silver)
                .into(itemView.ivThumbnailNews)

            itemView.tvTitleNews.text = post.title
            if (post.publishedAt != null){
                itemView.tvTanggal.text = UtilsManager.getDateAnotherFormatFromString2(post.publishedAt.toString())
            }else{
                itemView.tvTanggal.text = ""
            }

            itemView.setOnClickListener {
                listener(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news_v2, parent, false))

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position], listener)
    }

    fun onClick(listener: (Post) -> Unit){
        this.listener = listener
    }
}