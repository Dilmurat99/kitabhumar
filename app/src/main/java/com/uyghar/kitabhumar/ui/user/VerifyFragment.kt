package com.uyghar.kitabhumar.ui.user

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uyghar.kitabhumar.R
import com.uyghar.kitabhumar.ui.home.auth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var email: String? = null
    private var password: String? = null
    private lateinit var root: View
    private lateinit var waitDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString("email")
            password = it.getString("password")
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_verify, container, false)
        val button = root.findViewById<Button>(R.id.buttonVerify)
        button.setOnClickListener {
            firebaseLogin()
        }
        createWaitDialog()
        return root
    }

    fun createWaitDialog() {
        waitDialog = Dialog(requireContext())
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        waitDialog.setCancelable(true)
        waitDialog.setContentView(R.layout.wait_dialog)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VerifyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VerifyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun firebaseLogin() {
            //waitDialog.show()
            auth.signInWithEmailAndPassword(email ?: "",
                password ?: "")
                .addOnCompleteListener {
                    waitDialog.dismiss()
                    val user = auth.currentUser
                    user?.let {
                        if (it.isEmailVerified) {
                            findNavController().navigate(R.id.registerFragment)
                        } else {
                            Snackbar.make(root, "ئېمەيل ئادرېسىڭىزنى تېخى دەلىللىمىدىڭىز", Snackbar.LENGTH_LONG).show()
                        }
                    }
                    if (it.isSuccessful) {
                        Log.i("regUser", "Login")
                    }
                }
                .addOnFailureListener {
                    Log.i("regUser", it.toString())
                }
    }
}