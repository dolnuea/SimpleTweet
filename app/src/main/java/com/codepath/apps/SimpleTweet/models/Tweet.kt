package com.codepath.apps.SimpleTweet.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
class Tweet(var body: String = "",
                 var createdAt: String = "",
                 var user: User? = null) : Parcelable {

    companion object {
        fun fromJson(jsonObject: JSONObject): Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            return tweet
        }
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet>{
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()){ //0..length goes all the way including length: which gives index out of bounds exception
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }
        fun getFormattedTimestamp(time_created: String): String {
            return TimeFormatter.getTimeDifference(time_created)
        }
    }
}