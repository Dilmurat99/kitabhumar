package com.uyghar.kitabhumar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import com.uyghar.kitabhumar.databinding.ActivityMainBinding
import com.uyghar.kitabhumar.models.Book
import com.uyghar.kitabhumar.models.DataModel
import com.uyghar.kitabhumar.models.FBMessage
import com.uyghar.kitabhumar.models.NotificationModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http2.Header
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var book_array: Array<Book>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        wait(true)
        getBooks()
        binding.appBarMain.fab.setOnClickListener {
            sendNotification("Alo","Firebase test", 2828)
            /*val bundle = Bundle()
            bundle.putParcelableArray("book_array",book_array)
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.newPostFragment,bundle)*/
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        wait(true)
    }

    fun sendNotification(title: String, body: String, id: Int) {
        val notificationModel = NotificationModel(title,body,"high",true)
        val dataModel = DataModel(id)
        val fbMessage = FBMessage("/topics/kitabhumar",notificationModel, dataModel)
        val gson = GsonBuilder().create()
        val json_param = gson.toJson(fbMessage)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json_param.toRequestBody(mediaType)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("https://fcm.googleapis.com/fcm/send"))
            .addHeader("Authorization","key=AAAAxn7XiGY:APA91bHwUBlefzJVst21mjFze33c85eCXKLuq_V44jFWsxRNtVZqDcRMM54XkISjbhV5HRS7TgzKDa2A4PhpXLnJ2vYw1nZGASHheyi4FwN9h6TO9uSpUZrDghn9Vhsa73WrDm_mfCh5")
            .post(body)
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string()
                Log.i("response", res ?: "")
            }

        })
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.changeLocale("ug"))
    }

    fun Context.changeLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        return createConfigurationContext(config)
    }

    fun getBooks() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("http://172.104.143.75:8004/api/books/"))
            .build()
        client.newCall(request).enqueue(
            object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    runOnUiThread {
                        wait(false)

                    }

                }

                override fun onResponse(call: Call, response: Response) {
                    val json_str = response.body?.string()
                    val gson = GsonBuilder().create()
                    book_array = gson.fromJson(json_str, Array<Book>::class.java)
                    runOnUiThread {
                        wait(false)

                    }

                }

            }
        )

    }


    fun wait(isOn: Boolean) {
        binding.appBarMain.progressBar.isVisible = isOn
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}