package com.uyghar.kitabhumar.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.uyghar.kitabhumar.R
import com.uyghar.kitabhumar.databinding.FragmentHomeBinding
import com.uyghar.kitabhumar.databinding.FragmentSlideBinding
import com.uyghar.kitabhumar.models.Slider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SlideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SlideFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentSlideBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var slider: Slider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSlideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val id = resources.getIdentifier("book$no", "drawable", activity?.packageName)
        //binding.image.setImageResource(id)
        Log.i("address",  slider?.image ?: "")
        Picasso.get().load(slider?.image ?: "").into(binding.image)
    }

}