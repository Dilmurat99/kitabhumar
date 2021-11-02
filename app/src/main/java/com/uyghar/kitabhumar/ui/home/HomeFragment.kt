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
import com.google.android.material.shape.CornerSize
import com.google.android.material.tabs.TabLayoutMediator
import com.uyghar.kitabhumar.R
import com.uyghar.kitabhumar.databinding.FragmentHomeBinding
import com.google.android.material.shape.CornerFamily
import com.google.gson.GsonBuilder
import com.uyghar.kitabhumar.models.Slider
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var slider_array: Array<Slider>? = null
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


        val images = arrayOf("book1","book2","book3","book4","book1","book2","book3","book4")
        var i = 0
        images.forEach {
            val imageView = ImageView(requireContext())
            val id = resources.getIdentifier(it,"drawable",activity?.packageName)
            imageView.setImageResource(id)
            imageView.tag = i
            imageView.setOnClickListener {
                val tag = it.tag as Int
                Log.i("besildi", tag.toString())
            }
            i += 1
            //val layoutParams = ViewGroup.LayoutParams(240,240)
            //imageView.layoutParams = layoutParams
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            val density = resources.displayMetrics.density
            val dpi_width = (120 * density).toInt()
            val imageView_aptor = com.google.android.material.imageview.ShapeableImageView(requireContext())

            val id_aptor = resources.getIdentifier("aptor","drawable",activity?.packageName)
            imageView_aptor.setImageResource(id_aptor)
            //val layoutParams = ViewGroup.LayoutParams(240,240)
            //imageView.layoutParams = layoutParams
            imageView_aptor.scaleType = ImageView.ScaleType.FIT_XY
            val dpi_width_aptor = (80 * density).toInt()
            val shapeAppearanceModel = imageView_aptor.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes((dpi_width_aptor/2).toFloat())
                .build()
            imageView_aptor.shapeAppearanceModel = shapeAppearanceModel
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
            imageView_aptor.strokeColor = colorList
            imageView_aptor.strokeWidth = 5f
            binding.kitablarList.addView(imageView,dpi_width,dpi_width)
            binding.aptorlarList.addView(imageView_aptor,dpi_width_aptor,dpi_width_aptor)
            val space = View(requireContext())
            val space_width = (5 * density).toInt()
            binding.aptorlarList.addView(space,space_width,space_width)

        }
        getRequest()
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

    fun getRequest() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("http://172.104.143.75:8004/api/sliders/"))
            .build()
        client.newCall(request).enqueue(
            object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val json_str = response.body?.string()
                    val gson = GsonBuilder().create()
                    slider_array = gson.fromJson(json_str, Array<Slider>::class.java)
                    activity?.runOnUiThread {
                        binding.viewPager.adapter = SlideAdapter(slider_array!!, childFragmentManager, lifecycle)
                        TabLayoutMediator(binding.tabLayout,binding.viewPager) {
                                tab, position ->
                        }.attach()

                        startTimer()
                    }
                }

            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}