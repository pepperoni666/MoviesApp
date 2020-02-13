package com.pepperoni.android.moviesapp

import android.app.Application
import android.widget.Toast
import androidx.room.Room
import com.pepperoni.android.moviesapp.database.MoviesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoviesApp : Application() {

    init {
        instance = this
    }

    lateinit var db: MoviesDatabase private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java, "movie-database"
        ).build()
    }

    companion object {
        private var instance: MoviesApp? = null

        /**
         * Allows to show Toast from anywhere in the project.
         * @param msg = message to be shown in toast
         * @param makeLong = flag, if true: toast will be long, elsewise short
         */
        fun showToast(msg: String, makeLong: Boolean) {
            instance?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        it,
                        msg,
                        if (makeLong)
                            Toast.LENGTH_LONG
                        else
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}