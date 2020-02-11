package com.pepperoni.android.moviesapp.repository

import android.util.Log
import com.google.gson.GsonBuilder
import com.pepperoni.android.moviesapp.model.Movie
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesRepository : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    private val apiKey = "529549b0138460ff02c40a3d40099222"

    private val httpClient = OkHttpClient()

    private val preparedUrlBuilder: HttpUrl.Builder get() {
        return HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addQueryParameter("api_key", apiKey)
    }

    suspend fun getMoviesNowPlaying(): List<Movie> = withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url(preparedUrlBuilder
                    .addPathSegment("movie")
                    .addPathSegment("now_playing")
                    .build())
                .build()

            val response = httpClient.newCall(request).execute()
            if(response.isSuccessful){
                val gson = GsonBuilder().create()
                val data = gson.fromJson(response.body().string(), NowPlayingMoviesResponse::class.java)
                return@withContext data.results
            }
        return@withContext listOf<Movie>()
    }

    fun getSearch(){
        launch {
            val request = Request.Builder()
                .url(preparedUrlBuilder
                    .addPathSegment("search")
                    .addPathSegment("movie")
                    .build())
                .build()

            val response = httpClient.newCall(request).execute()
            if(response.isSuccessful){
                Log.d("TAGGGG", response.body().string())
            }
        }
    }

}