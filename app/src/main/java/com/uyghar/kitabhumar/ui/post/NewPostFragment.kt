package com.uyghar.kitabhumar.ui.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.uyghar.kitabhumar.R
import com.uyghar.kitabhumar.databinding.FragmentHomeBinding
import com.uyghar.kitabhumar.databinding.FragmentNewPostBinding
import com.uyghar.kitabhumar.models.Author
import com.uyghar.kitabhumar.models.Book
import com.uyghar.kitabhumar.models.Post
import com.uyghar.kitabhumar.models.Slider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewPostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var book_array: Array<Book>? = null
    private var param2: String? = null

    private var _binding: FragmentNewPostBinding? = null
    private lateinit var binding: FragmentNewPostBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book_array = it.getParcelableArray("book_array") as Array<Book>
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        binding  = _binding!!
        var book_title_array = book_array?.map { it.title }
        var books = ArrayList<String>()
        books.add("كىتاب تاللاڭ")
        book_title_array?.forEach {
            books.add(it ?: "")
        }
        binding.spinnerBook.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,books)
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewPostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewPostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}