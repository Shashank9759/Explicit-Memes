package com.example.explicitmemes

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class VollySingleton private constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: VollySingleton? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VollySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }



    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }
}

