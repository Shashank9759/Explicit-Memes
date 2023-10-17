package com.example.explicitmemes



import android.content.Intent

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.android.volley.Request

import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    lateinit var memeImage: ImageView
    lateinit var nextButton: Button
    lateinit var shareButton: Button
    lateinit var currentUrl: String
    lateinit var title: TextView
    lateinit var report_url: String
    lateinit var report: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        memeImage = findViewById<ImageView>(R.id.memeImage)
        shareButton = findViewById(R.id.shareButton)
        nextButton = findViewById(R.id.nextButton)
        title = findViewById(R.id.title)
        report = findViewById(R.id.report)
        onLoad()
    }

    fun onLoad() {
        val progressbar = findViewById<ProgressBar>(R.id.progressBar)
        progressbar.visibility = View.VISIBLE
        nextButton.isEnabled = false
        shareButton.isEnabled = false
        report.isEnabled = false
        val url = "https://meme-api.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null, { response ->
                val titleString = response.getString("title")
                title.text = titleString
                report_url = response.getString("postLink")
                report.isEnabled = true
                currentUrl = response.getString("url")
                Glide.with(this).load(currentUrl).listener(object : RequestListener<Drawable> {

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar.visibility = View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled = true
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar.visibility = View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled = true
                        return false
                    }


                }).into(memeImage)


            }, { error ->
                Toast.makeText(this, "Something Went wrong", Toast.LENGTH_SHORT).show()

                Log.e("@@@@", this.toString())

            }
        )

        VollySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun onShare(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey Checkout Explicit memes : $currentUrl")
        val chooser = Intent.createChooser(intent, "Share This meme")
        startActivity(chooser)


    }

    fun onNext(view: View) {
        onLoad()


    }

    fun onReport(view: View) {


        customChromeTab().launchUrl(this, Uri.parse(report_url))

    }

    private fun customChromeTab() :CustomTabsIntent{
        val builder = CustomTabsIntent.Builder()

        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.purple))
        builder.setDefaultColorSchemeParams(params.build())

        builder.setShowTitle(true)

        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
        builder.setUrlBarHidingEnabled(true)

        builder.setInstantAppsEnabled(true)

        val customBuilder = builder.build()
        return customBuilder
    }

    fun onGithub(view: View) {

    customChromeTab().launchUrl(this, Uri.parse("https://github.com/Shashank9759"))
    }


}