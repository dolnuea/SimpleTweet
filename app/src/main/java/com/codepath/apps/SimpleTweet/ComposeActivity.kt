package com.codepath.apps.SimpleTweet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)

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
                Toast.makeText(this, tweetContent, LENGTH_SHORT).show()
            }




        }
    }
}