package com.codepath.apps.SimpleTweet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TweetsAdapter(val tweets: List<Tweet>): RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        //inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)
    }

    //populating data into the item thru holder
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        //get model data based on pos
        val tweet: Tweet = tweets.get(position)

        //set item views based on views and data model
        holder.tvUserName.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body

        Glide.with(holder.itemView)
            .load(tweet.user?.publicImageURL)
            .into(holder.ivProfileImage)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
    }
}