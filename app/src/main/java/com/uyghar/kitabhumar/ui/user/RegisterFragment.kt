package com.uyghar.kitabhumar.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.Toast

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener
import com.uyghar.kitabhumar.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    var is_login = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        arguments?.let {
            is_login = it.getBoolean("is_login")
        }
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_register, container, false)
        val buttonReg = root.findViewById<Button>(R.id.buttonReg)
        val editEmail = root.findViewById<EditText>(R.id.editEmail)
        val editPassword = root.findViewById<EditText>(R.id.editPassword)
        buttonReg.setOnClickListener {
            if (is_login) {
                auth.signInWithEmailAndPassword(editEmail.text.toString(),
                    editPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i("regUser", "Login")
                        }
                    }
                    .addOnFailureListener {
                        Log.i("regUser", it.toString())
                    }
            } else {
                auth.createUserWithEmailAndPassword(
                    editEmail.text.toString(),
                    editPassword.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener {
                                    Log.i("regUser", "Email was sent")
                                }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("regUser", "createUserWithEmail:failure", task.exception)

                        }
                    }
            }
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
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}