package com.codepath.apps.SimpleTweet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.SimpleTweet.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    val tweets = ArrayList<Tweet>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setLogo(R.drawable.twitterlogowhite2)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeContainer)

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refresh listener")
            // Your code to refresh the list here.
            populateHomeTimeline()
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        client = TwitterApplication.getRestClient(this)
        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)
        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        populateHomeTimeline()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // handles clicks on menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Compose) {
            Toast.makeText(this, "Ready to compose tweet!", LENGTH_SHORT).show()
            val intent = Intent(this, ComposeActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //get data from intent
            val tweet = data?.getParcelableExtra("tweet") as Tweet

            //update timeline
            //modify data source of tweets
            tweets.add(0, tweet)
            //update adapter
            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun populateHomeTimeline(){
        client.getHomeTimeline(object: JsonHttpResponseHandler(){

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess $json")

                val JSONArray = json.jsonArray
                try {

                    // Remember to CLEAR OUT old items before appending in the new ones
                    adapter.clear()

                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(JSONArray)
                    tweets.addAll(listOfNewTweetsRetrieved)
                    adapter.notifyDataSetChanged()

                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.isRefreshing = false
                } catch (e: JSONException){
                    Log.e(TAG, "JSON Exception $e")
                }
            }
        })
    }
    companion object {
        const val TAG = "TimelineActivity"
        val REQUEST_CODE = 100
    }
}