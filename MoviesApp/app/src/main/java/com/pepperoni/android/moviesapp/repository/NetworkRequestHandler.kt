package com.pepperoni.android.moviesapp.repository

import com.pepperoni.android.moviesapp.MoviesApp
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.io.IOException

object NetworkRequestHandler {
    suspend fun handleNetworkRequest(function: suspend () -> Unit){
        try{
            //set timeout for API response to 10s
            withTimeout(10000){
                function()
            }
        }catch (e: TimeoutCancellationException) {
            MoviesApp.showToast("Connection timeout", true)
        } catch (e: IOException) {
            e.message?.let {
                MoviesApp.showToast(it, false)
            }
        }
    }
}