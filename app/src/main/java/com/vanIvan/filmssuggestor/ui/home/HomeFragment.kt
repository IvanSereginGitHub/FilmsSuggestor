package com.vanIvan.filmssuggestor.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vanIvan.filmssuggestor.R
import com.vanIvan.filmssuggestor.databinding.FragmentHomeBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var imageView: ImageView? = null
    private var titleView: TextView? = null
    private var genreView: TextView? = null
    private var plotView: TextView? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        println(R.id.imageView2)
        imageView = view?.findViewById<ImageView>(R.id.imageView2)
        titleView = view?.findViewById<TextView>(R.id.titleView)
        genreView = view?.findViewById<TextView>(R.id.genreView)
        plotView = view?.findViewById<TextView>(R.id.plotView)

        val api_key = "9cc1ed67"
        val rnds = (1..7000000).random()

        val url = "http://www.omdbapi.com/?i=tt${rnds}&apikey=${api_key}"
        runMovie(url)
        println("Recreating view...")
        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    public fun updateText(binaryImage: Bitmap?, title: String, genre: String, plot: String) {

        view?.findViewById<TextView>(R.id.titleView)?.text = title
        view?.findViewById<TextView>(R.id.genreView)?.text = "Genres: $genre"
        view?.findViewById<TextView>(R.id.plotView)?.text = plot
//        imageView?.setImageBitmap(binaryImage)
//        titleView?.text = title
//        genreView?.text = "Genres: $genre"
//        plotView?.text = plot

    }
    private val client = OkHttpClient()
    fun runMovie(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        var res: String? = ""
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

                    res = response.body?.string()
                    var json = JSONObject(res)
                    if(json.has("Error")) {
                        println("Unexpected error, trying again...")
                        Thread.sleep(1_000)
                        generateIMDBid()
                        return
                    }
                    println(json)

                        val executor = Executors.newSingleThreadExecutor()
                        val handler = Handler(Looper.getMainLooper())
                        var image: Bitmap? = null
                        executor.execute{

                            val imageUrl = json.getString("Poster")
                            val title = json.getString("Title")
                            val genre = json.getString("Genre")
                            val plot = json.getString("Plot")
                            if(imageUrl == "N\\/A") {
                                Thread.sleep(1_000)
                                generateIMDBid()
                                executor.shutdown()
                            }
                            try {
                                val `in` = java.net.URL(imageUrl).openStream()
                                image = BitmapFactory.decodeStream(`in`)
                                handler.post{
                                    runOnUiThread {
                                        view?.findViewById<ImageView>(R.id.imageView2)?.setImageBitmap(image)
                                    }
                                }
                            }
                            catch (e:java.lang.Exception) {
                                e.printStackTrace()
                            }
                            updateText(null, title, genre, plot)
                        }
                }
            }
        })
    }

    fun generateIMDBid() {
        val api_key = "9cc1ed67"
        val rnds = (1..7000000).random()

        val url = "http://www.omdbapi.com/?i=tt${rnds}&apikey=${api_key}"
        println(url)
        runMovie(url)
    }
fun Fragment?.runOnUiThread(action: () -> Unit) {
    this ?: return
    if (!isAdded) return // Fragment not attached to an Activity
    activity?.runOnUiThread(action)
}
}