package com.bi_instatag.sdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import com.bi_instatag.itandroidsdk.IDType
import com.bi_instatag.itandroidsdk.Instatag
import com.bi_instatag.itandroidsdk.VisitorType

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val versionView: TextView = findViewById(R.id.version)
        val resultView: TextView = findViewById(R.id.result)
        resultView.movementMethod = ScrollingMovementMethod()

        val mobileKeyView: TextView = findViewById(R.id.mobileKeyInput)
        val screenNameView: TextView = findViewById(R.id.screenNameInput)

        findViewById<View>(R.id.buttonApply).setOnClickListener {
            Instatag.configure(this.application, mobileKeyView.text.toString())
        }

        findViewById<View>(R.id.buttonTrackScreenView).setOnClickListener { Instatag.trackScreen(screenNameView.text.toString()) }
        findViewById<View>(R.id.buttonTrackButton).setOnClickListener { Instatag.trackButton("First demo btn") }
        findViewById<View>(R.id.buttonTrackFormStart).setOnClickListener { Instatag.trackFormStart("demo form", "speciality", "medical info") }
        findViewById<View>(R.id.buttonTrackFormEnd).setOnClickListener { Instatag.trackFormEnd("demo form", "speciality", "medical info") }
        findViewById<View>(R.id.buttonTrackSearch).setOnClickListener { Instatag.trackSearch("whatsup", "42", "life") }
        findViewById<View>(R.id.buttonTrackLogin).setOnClickListener { Instatag.trackLogin("user-id", "password", VisitorType.HCP, IDType.VeevaId) }
        findViewById<View>(R.id.buttonTrackRegistrationFormStart).setOnClickListener { Instatag.trackRegistrationFormStart("reg form", "speciality", "medical info") }
        findViewById<View>(R.id.buttonTrackRegistrationFormEnd).setOnClickListener { Instatag.trackRegistrationFormEnd("reg form", "speciality", "medical info") }
        findViewById<View>(R.id.buttonTrackInputFieldChange).setOnClickListener { Instatag.trackInputFieldChange("reg form", "firstname") }
        findViewById<View>(R.id.buttonTrackSoftAuth).setOnClickListener { Instatag.trackSoftAuth(VisitorType.HCP) }


        var ver: String = "IT Version: " + Instatag.version()
        versionView.text = ver;
        Log.i(TAG, ver);
    }
}