package com.codepath.apps.SimpleTweet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.codepath.apps.SimpleTweet.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        client = TwitterApplication.getRestClient(this)

        // handle user click on tweet button
        btnTweet.setOnClickListener {

            // grab content of edittext/etCompose
            val tweetContent = etCompose.text.toString()
            //make sure tweet isnt empty
            if (tweetContent.isEmpty()){
                Toast.makeText(this, "Please write for tweet", LENGTH_SHORT).show()
            }
            //make sure tweet is under char count
            else if (tweetContent.length > 140) {
                Toast.makeText(this, "Allowed character count is 140 characters", LENGTH_SHORT).show()
            }
            else {
                // make api call to publish tweet
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        //todo send tweet back to timeline activity
                        Log.i(TAG, "Successfully published tweet")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                })
            }

        }
    }
    companion object {
        val TAG = "ComposeTweet"
    }
}