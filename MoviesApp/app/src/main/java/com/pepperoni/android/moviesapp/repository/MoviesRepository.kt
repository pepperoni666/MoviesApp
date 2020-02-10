package com.pepperoni.android.moviesapp.repository

import android.util.Log
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesRepository : CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private val httpClient = OkHttpClient()

    init{
        launch {

            val url = HttpUrl.Builder()
                .scheme("https")
                .host("api.themoviedb.org")
                .addPathSegment("3")
                .addPathSegment("movie")
                .addPathSegment("now_playing")
                .addQueryParameter("api_key", "529549b0138460ff02c40a3d40099222")
                .build()
            val request = Request.Builder()
                //.header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Mjk1NDliMDEzODQ2MGZmMDJjNDBhM2Q0MDA5OTIyMiIsInN1YiI6IjVlMzlkZDViOThmMWYxMDAxMjA4MTJhYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xb26TTMQsqgMXYVq9KvTvikekXhJaQWbFfwQ21CPKgA")
                //.url("https://api.themoviedb.org/3/movie/now_playing")
                .url(url)
                .build()

            val response = httpClient.newCall(request).execute()
            if(response.isSuccessful){
                Log.d("TAGGGG", response.body().string())
            }
        }
    }

}