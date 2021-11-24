package com.uyghar.kitabhumar.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.uyghar.kitabhumar.databinding.FragmentDetailBinding
import com.uyghar.kitabhumar.models.Post
import com.uyghar.kitabhumar.models.Slider
import com.uyghar.kitabhumar.ui.home.SlideAdapter


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           post = it.getParcelable("post")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(post?.user?.image).into(binding.userImage)
        Picasso.get().load(post?.book?.image).into(binding.bookImage)
        binding.bookText.setText(post?.book?.title + "\n" + post?.book?.author?.name + " " + post?.book?.author?.surname)
        binding.postDate.setText(post?.updated_at)
        binding.userName.setText(post?.user?.nickname + " " + post?.post_category?.title)
        binding.postText.setText(post?.content)
        //val image_count = post?.post_images?.size ?: 0
        //if (image_count > 0) {
        //Array<Slider>(image_count) { Slider(null, null, null, null, null, null) }
            var sliders = ArrayList<Slider>()
            //var n = 0
            post?.post_images?.forEach { postImage ->
                val slider = Slider(null, null, postImage.images, null, null, null)
                sliders.add(slider)
                //sliders.set(n, slider)
                //n += 1
            }

            binding.postImage.adapter = SlideAdapter(sliders.toTypedArray(), childFragmentManager, lifecycle)
        //}

    }

}