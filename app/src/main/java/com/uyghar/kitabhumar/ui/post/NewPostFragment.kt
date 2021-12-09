package com.uyghar.kitabhumar.ui.post

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import com.uyghar.kitabhumar.MainActivity
import com.uyghar.kitabhumar.R
import com.uyghar.kitabhumar.databinding.FragmentHomeBinding
import com.uyghar.kitabhumar.databinding.FragmentNewPostBinding
import com.uyghar.kitabhumar.models.*
import com.uyghar.kitabhumar.ui.user.UserHelper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.net.URL

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
    private var cat_array: Array<PostCategory>? = null
    private var param2: String? = null

    private var book_id: Int? = null
    private var cat_id: Int? = null
    private var user_id: Int? = null

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

        binding.spinnerBook.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                if (i > 0) {
                    book_id = book_array?.get(i - 1)?.id
                    Log.i("book_id", book_id.toString())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.spinnerCategory.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                if (i > 0) {
                    cat_id = cat_array?.get(i-1)?.id
                    Log.i("cat_id", cat_id.toString())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.spinnerBook.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,books)
        // Inflate the layout for this fragment
        (activity as MainActivity).wait(true)
        getCategories()
        binding.buttonSubmit.setOnClickListener {
            saveToRemote()
        }
        return binding.root
    }

    fun saveToRemote() {
        //val bitmap = (buttonImage.drawable as BitmapDrawable).bitmap
        val params = HashMap<String,String>()
        val content = binding.editContent.text.toString()
        val userHelper = UserHelper(requireContext())
        if (userHelper.members().size > 0) {
            val user = userHelper.members().first()
            user_id = user.id
        }
        params["content"] = content ?: ""
        params["user"] = user_id.toString()
        params["book"] = book_id.toString()
        params["post_category"] = cat_id.toString()


        var images = ArrayList<Uri>()
        /*uri?.let {
            images.add(it)
        }*/
        //val params = ["email":email,"name":name,"surname":surname,"username":userName]
        //waitDialog.show()
        Thread() {
            postMultipart("http://172.104.143.75:8004/api/posts/",params,images)
        }.start()
    }

    fun postMultipart(url: String, params: HashMap<String, String>, images: List<Uri>) {
        var formBody = MultipartBody.Builder()
        formBody.setType(MultipartBody.FORM)
        for ((k, v) in params) {
            formBody.addFormDataPart(k, v)
        }

        /*images.forEach {
            val imgPath = getRealPathFromURI(requireContext(), it) ?: return
            val file = File(imgPath)
            var typeStr = "images/jpeg"
            var fileName = "file.jpg"
            if (file.name.substringAfterLast('.',"").capitalize().equals("PNG")) {
                typeStr = "images/png"
                fileName = "file.png"
            }
            val fileRequestBody = file.asRequestBody(typeStr.toMediaTypeOrNull())
            formBody.addFormDataPart("image", fileName, fileRequestBody)
        }*/

        val requestBody = formBody.build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val gson = GsonBuilder().create()
                val json_str = response.body?.string()
                val post = gson.fromJson(json_str, Post::class.java)

                activity?.runOnUiThread {
                    findNavController().popBackStack(R.id.nav_home,true)
                }
            } else {

                Log.i("Submit", response.toString())

            }
        }

    }

    fun getCategories() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("http://172.104.143.75:8004/api/categories/"))
            .build()
        client.newCall(request).enqueue(
            object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    activity?.runOnUiThread {
                        (activity as MainActivity).wait(false)
                    }

                }

                override fun onResponse(call: Call, response: Response) {
                    activity?.runOnUiThread {
                        (activity as MainActivity).wait(false)
                        val json_str = response.body?.string()
                        val gson = GsonBuilder().create()
                        var cats = ArrayList<String>()
                        cats.add("يازما تۈرىنى تاللاڭ")
                        cat_array = gson.fromJson(json_str, Array<PostCategory>::class.java)
                        cat_array?.forEach {
                            cats.add(it.title ?: "")
                        }
                        binding.spinnerCategory.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item,cats)
                    }


                }

            }
        )
    }


}