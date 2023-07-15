package com.vanIvan.filmssuggestor.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.vanIvan.filmssuggestor.DBHelper
import com.vanIvan.filmssuggestor.R
import com.vanIvan.filmssuggestor.databinding.FragmentHomeBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors

const val api_key = "9cc1ed67"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        println(inflater)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rnds = (1..7000000).random()

        val url = "http://www.omdbapi.com/?i=tt${rnds}&apikey=${api_key}"
        runMovie(url)
        println("Recreating view...")
        runOnUiThread {
            view?.findViewById<Button>(R.id.refresh_button)?.visibility = View.VISIBLE
        }
        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun refreshFragment() {
        parentFragmentManager.beginTransaction().detach(this).commit();
        parentFragmentManager.beginTransaction().attach(this).commit();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    public fun updateText(
        title: String,
        genre: String,
        plot: String,
        imdb_id: String
    ) {
        var db = DBHelper(requireContext(), null)
        db.addEntry(title, imdb_id)
        runOnUiThread {
            view?.findViewById<ProgressBar>(R.id.progressBar1)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.titleView)?.text = title
            view?.findViewById<TextView>(R.id.genreView)?.text = "Genres: $genre"
            view?.findViewById<TextView>(R.id.plotView)?.text = plot
            view?.findViewById<Button>(R.id.button)?.visibility = View.VISIBLE
            view?.findViewById<Button>(R.id.button)?.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/$imdb_id"))
                startActivity(i)
            }
            view?.findViewById<com.google.android.material.button.MaterialButton>(R.id.refresh_button)?.visibility =
                View.VISIBLE
            view?.findViewById<com.google.android.material.button.MaterialButton>(R.id.refresh_button)
                ?.setOnClickListener {
                    refreshFragment()
                }
        }

//        imageView?.setImageBitmap(binaryImage)
//        titleView?.text = title
//        genreView?.text = "Genres: $genre"
//        plotView?.text = plot

    }

    private val client = OkHttpClient()


    fun runMovie(url: String) {
        var thread = Thread {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()
                var res: String? = ""
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(
                                activity,
                                "Catched error, restarting...",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        generateIMDBid()
                    }

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    res = response.body?.string()
                    var json = JSONObject(res)
                    println(json)
                    if (json.has("Error")) {
                        println("Unexpected error, trying again...")
                        Thread.sleep(1_000)
                        generateIMDBid()
                        return@Thread
                    }

                    val executor = Executors.newSingleThreadExecutor()
                    val handler = Handler(Looper.getMainLooper())
                    var image: Bitmap? = null
                    executor.execute {

                        val imageUrl = json.getString("Poster")
                        var title = json.getString("Title")
                        val genre = json.getString("Genre")
                        val plot = json.getString("Plot")
                        val imdbID = json.getString("imdbID")
                        if (json.has("seriesID")) {
                            val seriesID = json.getString("seriesID")
                            var seriesTitle: String? = null

                            val url = "http://www.omdbapi.com/?i=${seriesID}&apikey=${api_key}"
                            val request = Request.Builder()
                                .url(url)
                                .build()
                            println("getting series name")
                            client.newCall(request).execute().use { response1 ->
                                if (!response1.isSuccessful) {
                                    seriesTitle = null
                                }

                                for ((name, value) in response1.headers) {
                                    println("$name: $value")
                                }

                                var json = JSONObject(response1.body?.string())
                                println(json)
                                if (json.has("Error")) {
                                    println("Unexpected error")
                                }
                                if (json.has("Title")) {
                                    seriesTitle = json.getString("Title")
                                }
                            }
                            if (seriesTitle != null) {
                                println("found series name: $seriesTitle")
                                title = "$seriesTitle ($title)"
                            }
                        }
                        if (genre.contains("Adult", true)) {
                            runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    "Whoops, this is 18+, cannot show that :)",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            generateIMDBid()
                            return@execute
                        }
                        try {
                            val `in` = java.net.URL(imageUrl).openStream()
                            image = BitmapFactory.decodeStream(`in`)
                            handler.post {
                                runOnUiThread {
                                    view?.findViewById<ImageView>(R.id.imageView2)
                                        ?.setImageBitmap(image)
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            println("getting alternative image")
                            runOnUiThread {
                                Glide.with(requireView())
                                    .clear(view?.findViewById<ImageView>(R.id.imageView2)!!)
                                Glide.with(requireView())
                                    .load("https://cataas.com/cat/gif")
                                    .into(view?.findViewById<ImageView>(R.id.imageView2)!!)
                            }
                        }
                        println("updating")
                        updateText(title, genre, plot, imdbID)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(
                        activity,
                        "Catched error, restarting...",
                        Toast.LENGTH_LONG
                    ).show()
                }
                generateIMDBid()
                return@Thread
            }
        }
        thread.start()
    }

    fun generateIMDBid() {
        val rnds = (1..7000000).random()

        val url = "http://www.omdbapi.com/?i=tt${rnds}&apikey=${api_key}"
        runMovie(url)
    }

    fun getSerialInfo(id: String): String? {
        val url = "http://www.omdbapi.com/?i=${id}&apikey=${api_key}"
        val request = Request.Builder()
            .url(url)
            .build()
        var res: String? = ""
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                return
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    res = response.body?.string()
                    var json = JSONObject(res)
                    println(json)
                    if (json.has("Error")) {
                        println("Unexpected error")
                        res = null
                        return
                    }
                    res = json.getString("Title")
                    println(res)
                }
            }
        })
        return res
        println(res)
    }

    fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return // Fragment not attached to an Activity
        activity?.runOnUiThread(action)
    }
}