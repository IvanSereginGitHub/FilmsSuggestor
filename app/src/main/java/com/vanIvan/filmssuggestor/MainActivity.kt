package com.vanIvan.filmssuggestor


import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.vanIvan.filmssuggestor.databinding.ActivityMainBinding
import com.vanIvan.filmssuggestor.ui.home.HomeFragment
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_archive, R.id.nav_info), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
        run("http://worldtimeapi.org/api/ip")
//        generateIMDBid()
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

    public fun addTableRow(data: TableData) {
        val table = findViewById<TableLayout>(R.id.table)
        val row = TableRow(this)
        row.layoutParams =
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        val text = TextView(this)
        text.text = "Hello"
        text.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        row.addView(text)

        val button = Button(this)
        button.text = "Click me"
        button.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        row.addView(button)
        table.addView(row);
    }

    private val client = OkHttpClient()
    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body?.string())
                }
            }
        })
    }
//
//    fun runMovie(url: String) {
//        val request = Request.Builder()
//            .url(url)
//            .build()
//        var res: String? = ""
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use {
//                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
//
//                    for ((name, value) in response.headers) {
//                        println("$name: $value")
//                    }
//
//                    res = response.body?.string()
//                    var json = JSONObject(res)
//                    if(json.has("Error")) {
//                        println("Unexpected error, trying again...")
//                        Thread.sleep(1_000)
//                        generateIMDBid()
//                        return
//                    }
//                    println(json)
//                    println(R.id.nav_home)
//                    runOnUiThread(Runnable {
////                        setContentView(R.layout.activity_main)
//                        println(R.id.nav_home)
//                        println(supportFragmentManager.fragments[0].tag)
//                        val fragment: HomeFragment? =
//                            supportFragmentManager.findFragmentById(R.id.nav_home) as HomeFragment?
//                        val executor = Executors.newSingleThreadExecutor()
//                        val handler = Handler(Looper.getMainLooper())
//                        var image: Bitmap? = null
//                        executor.execute{
//
//                            val imageUrl = json.getString("Poster")
//                            val title = json.getString("Title")
//                            val genre = json.getString("Genre")
//                            val plot = json.getString("Plot")
//                            if(imageUrl == "N\\/A") {
//                                Thread.sleep(1_000)
//                                generateIMDBid()
//                                executor.shutdown()
//                            }
//                            try {
//                                val `in` = java.net.URL(imageUrl).openStream()
//                                image = BitmapFactory.decodeStream(`in`)
//                                handler.post{
//                                    fragment?.updateText(image, title, genre, plot)
//                                    println(fragment)
//                                    if(fragment != null) {
//                                        val ft: FragmentTransaction =
//                                            supportFragmentManager.beginTransaction()
//                                        ft.detach(fragment as Fragment)
//                                        ft.attach(fragment as Fragment)
//                                        ft.commit()
//                                    }
//                                }
//                            }
//                            catch (e:java.lang.Exception) {
//                                e.printStackTrace()
//                            }
//                            fragment?.updateText(null, title, genre, plot)
//                            println(fragment)
//                            if(fragment != null) {
//                                val ft: FragmentTransaction =
//                                    supportFragmentManager.beginTransaction()
//                                ft.detach(fragment as Fragment)
//                                ft.attach(fragment as Fragment)
//                                ft.commit()
//                            }
//                        }
//                    })
//                }
//            }
//        })
//    }
//
//    fun generateIMDBid() {
//        val api_key = "9cc1ed67"
//        val rnds = (1..7000000).random()
//
//        val url = "http://www.omdbapi.com/?i=tt${rnds}&apikey=${api_key}"
//        println(url)
//        runMovie(url)
//    }
}