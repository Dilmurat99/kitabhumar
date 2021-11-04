package com.uyghar.kitabhumar.ui.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.CornerSize
import com.google.android.material.tabs.TabLayoutMediator
import com.uyghar.kitabhumar.R
import com.uyghar.kitabhumar.databinding.FragmentHomeBinding
import com.google.android.material.shape.CornerFamily
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.uyghar.kitabhumar.models.Author
import com.uyghar.kitabhumar.models.Book
import com.uyghar.kitabhumar.models.Slider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var slider_array: Array<Slider>? = null
    private var book_array: Array<Book>? = null
    private var author_array: Array<Author>? = null
    private var no = 0;
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        GlobalScope.launch {
            val isBooksReady = async { getBooks() }
            val isAuthorsReady = async { getAuthors() }
            val isSliderReady = async { getSlider() }
            if (isBooksReady.await() && isAuthorsReady.await() && isSliderReady.await()) {
                activity?.runOnUiThread {
                    binding.progressView.visibility = View.INVISIBLE
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerView.adapter = PostAdapter(requireContext())
                }
            }
            /*getBooks()
        getAuthors()
        getSlider()*/
        }


        return root
    }

    private fun startTimer() {
        var timer: Timer = Timer()
        timer!!.schedule(object: TimerTask()
        {
            override fun run() {
                activity?.runOnUiThread {
                    binding.viewPager.setCurrentItem(no, false)
                }
                no += 1
                if (no >= slider_array?.size ?: 0)
                    no = 0
            }

        },0,3000)
    }

    suspend fun getSlider(): Boolean = suspendCoroutine { res ->
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("http://172.104.143.75:8004/api/sliders/"))
            .build()
        client.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    res.resume(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json_str = response.body?.string()
                    val gson = GsonBuilder().create()
                    slider_array = gson.fromJson(json_str, Array<Slider>::class.java)
                    activity?.runOnUiThread {
                        binding.viewPager.adapter =
                            SlideAdapter(slider_array!!, childFragmentManager, lifecycle)
                        TabLayoutMediator(
                            binding.tabLayout,
                            binding.viewPager
                        ) { tab, position ->
                        }.attach()
                        startTimer()
                    }
                    res.resume(true)
                }

            }
        )
    }

    suspend fun getBooks(): Boolean = suspendCoroutine { res ->
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("http://172.104.143.75:8004/api/books/"))
            .build()
        client.newCall(request).enqueue(
            object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    res.resume(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json_str = response.body?.string()
                    val gson = GsonBuilder().create()
                    book_array = gson.fromJson(json_str, Array<Book>::class.java)
                    activity?.runOnUiThread {
                        showBooks()
                    }
                    res.resume(true)
                }

            }
        )
    }

    suspend fun getAuthors(): Boolean = suspendCoroutine { res ->
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("http://172.104.143.75:8004/api/authors/"))
            .build()
        client.newCall(request).enqueue(
            object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    res.resume(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json_str = response.body?.string()
                    val gson = GsonBuilder().create()
                    author_array = gson.fromJson(json_str, Array<Author>::class.java)
                    activity?.runOnUiThread {
                        showAuthor()
                    }
                    res.resume(true)
                }

            }
        )
    }

    fun showBooks() {
        book_array?.forEach {
            val imageView = ImageView(requireContext())
            Picasso.get().load(it.image).into(imageView)
            /*imageView.tag = i
            imageView.setOnClickListener {
                val tag = it.tag as Int
                Log.i("besildi", tag.toString())
            }
            i += 1*/
            //val layoutParams = ViewGroup.LayoutParams(240,240)
            //imageView.layoutParams = layoutParams
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            val density = resources.displayMetrics.density
            val dpi_height = (120 * density).toInt()
            val dpi_width = (0.62 * dpi_height).toInt()
            binding.kitablarList.addView(imageView,dpi_width,dpi_height)
            val space = View(requireContext())
            val space_width = (5 * density).toInt()
            binding.kitablarList.addView(space,space_width,space_width)

        }
    }

    fun showAuthor() {
        author_array?.forEach {
            val imageView = com.google.android.material.imageview.ShapeableImageView(requireContext())
            Picasso.get().load(it.image).into(imageView)

            imageView.scaleType = ImageView.ScaleType.FIT_XY
            val density = resources.displayMetrics.density
            val dpi_width = (80 * density).toInt()

            val shapeAppearanceModel = imageView.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes((dpi_width/2).toFloat())
                .build()
            imageView.shapeAppearanceModel = shapeAppearanceModel
            val colorList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),  // Disabled
                    intArrayOf(android.R.attr.state_enabled)    // Enabled
                ),
                intArrayOf(
                    Color.BLACK,     // The color for the Disabled state
                    Color.parseColor("#a8a8a8")        // The color for the Enabled state
                )
            )
            imageView.strokeColor = colorList
            imageView.strokeWidth = 5f
            binding.aptorlarList.addView(imageView,dpi_width,dpi_width)
            val space = View(requireContext())
            val space_width = (5 * density).toInt()
            binding.aptorlarList.addView(space,space_width,space_width)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}