package com.example.memeappusingvolley

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.memeappusingvolley.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val url: String = "https://meme-api.com/gimme" // Api Url

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        getMemeData()

        binding.getMemeButton.setOnClickListener {
            getMemeData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getMemeData() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait while we load new Meme")
        progressDialog.show()

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url, { response -> // Response

            Log.d("RESPONSE", "RESPONSE: $response")

            val responseObject = JSONObject(response)

            // Meme Title
            binding.titleText.text = responseObject.getString("title")

            // Author Name
            binding.autherText.text = "Author: ${responseObject.getString("author")}"

            // Meme Image
            Glide.with(this@MainActivity)
                .load(responseObject.getString("url"))
                .placeholder(R.drawable.baseline_image_search_24)
                .error(R.drawable.baseline_image_search_24).into(binding.memeImage)

            progressDialog.dismiss()

        }, { error ->// Error
            progressDialog.dismiss()
            Toast.makeText(this@MainActivity, error.localizedMessage, Toast.LENGTH_SHORT).show()
        })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}