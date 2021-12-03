package com.uyghar.kitabhumar.ui.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.uyghar.kitabhumar.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_user, container, false)
        val profile = root.findViewById<ConstraintLayout>(R.id.profile)
        val userHelper = UserHelper(requireContext())
        if (userHelper.members().size > 0) {
            val user = userHelper.members().first()
            profile.isVisible = user != null
            if (profile.isVisible) {
                val text_user_name: TextView = root.findViewById(R.id.text_username)
                val profile_image: ImageView = root.findViewById(R.id.profile_image)
                text_user_name.setText(user.nickname ?: "")
                if (user.image?.isBlank() == false)
                    Picasso.get().load("http://172.104.143.75:8004" + user.image)
                        .into(profile_image)
            }
        }

        val buttonExit = root.findViewById<Button>(R.id.button_exit)
        buttonExit.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("")
                .setMessage("راستىنلا چېكىنەمسىز؟")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("ھەئە", object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        userHelper.clearDB()
                        profile.isVisible = false
                    }

                })
                .setNegativeButton("ياق", null)
                .show()

        }

        val buttonReg = root.findViewById<Button>(R.id.button_register)
        buttonReg.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("is_login",false)
            findNavController().navigate(R.id.registerFragment,bundle)
        }
        val buttonLogin = root.findViewById<Button>(R.id.button_login)
        buttonLogin.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("is_login",true)
            findNavController().navigate(R.id.registerFragment,bundle)
        }
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}