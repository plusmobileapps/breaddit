package com.plusmobileapps.breaddit

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.util.Base64
import android.util.Log
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


private const val AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
        "&response_type=code&state=%s&redirect_uri=%s&" +
        "duration=permanent&scope=identity"

private const val CLIENT_ID = "kV9k939bhxpvyg"

private const val REDIRECT_URI = "https://plusmobileapps.com/breaddit"

private const val STATE = "MY_RANDOM_STRING_1"

private const val ACCESS_TOKEN_URL = "https://www.reddit.com/api/v1/access_token"

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    companion object {
        val TAG = MainActivity::class.java.canonicalName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        login_button.setOnClickListener {
            startSignIn()
        }
    }

    override fun onResume() {
        super.onResume()

        if (intent != null && intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri!!.getQueryParameter("error") != null) {
                val error = uri.getQueryParameter("error")
                Log.e(TAG, "An error has occurred : $error")
            } else {
                val state = uri.getQueryParameter("state")
                if (state == STATE) {
                    val code = uri.getQueryParameter("code")
                    getAccessToken(code)
                }
            }
        }
    }

    private fun getAccessToken(code: String?) {
        val authString = "$CLIENT_ID:"
        val encodedAuthString = Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)

        val request = Request.Builder()
            .addHeader("User-Agent", "Breaddit")
            .addHeader("Authorization", "Basic $encodedAuthString")
            .url(ACCESS_TOKEN_URL)
            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),"grant_type=authorization_code&code=" + code +
                    "&redirect_uri=" + REDIRECT_URI
                ))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@MainActivity, "There was an error getting a token", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body()?.string() ?: return
                val data = JSONObject(json)
                val accessToken = data.optString("access_token")
                val refreshToken = data.optString("refresh_token")
                Log.d(TAG, "Access Token: $accessToken")
                Log.d(TAG, "Refresh Token: $refreshToken")

            }
        })
    }

    private fun startSignIn() {
        val url = String.format(AUTH_URL, CLIENT_ID, STATE, REDIRECT_URI)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
