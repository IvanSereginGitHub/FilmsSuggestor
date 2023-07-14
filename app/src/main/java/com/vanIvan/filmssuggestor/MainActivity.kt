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
}