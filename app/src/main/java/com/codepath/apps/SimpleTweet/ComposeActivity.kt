package com.codepath.apps.SimpleTweet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.codepath.apps.SimpleTweet.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var characterCount: TextView
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    var charactersLeft: Int = MAX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        characterCount = findViewById(R.id.tvCharCount)
        btnTweet = findViewById(R.id.btnTweet)
        client = TwitterApplication.getRestClient(this)

        characterCount.text = "$MAX characters left"

        //handle character count
        etCompose.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //todo
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //todo
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    charactersLeft = charactersLeft(s.length)
                }
                characterCount.text = "$charactersLeft characters left"

                if (charactersLeft < 0){
                    characterCount.setTextColor(resources.getColor(R.color.red))
                    btnTweet.isClickable = false
                }
                else {
                    characterCount.setTextColor(resources.getColor(R.color.black))
                    btnTweet.isClickable = true
                    }
            }

        })

        // handle user click on tweet button
        btnTweet.setOnClickListener {

            // grab content of edittext/etCompose
            val tweetContent = etCompose.text.toString()
            //make sure tweet isnt empty
            if (tweetContent.isEmpty()){
                Toast.makeText(this, "Please write for tweet", LENGTH_SHORT).show()
            }
            //make sure tweet is under char count
            else if (tweetContent.length > 280) {
                Toast.makeText(this, "Allowed character count is 280 characters", LENGTH_SHORT).show()
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

    fun charactersLeft(count: Int): Int {
        return MAX - count
    }

    companion object {
        val TAG = "ComposeTweet"
        val MAX = 280
    }
}
