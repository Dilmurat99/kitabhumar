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
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uyghar.kitabhumar.R
import com.uyghar.kitabhumar.ui.home.auth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.gson.GsonBuilder
import com.uyghar.kitabhumar.models.Book
import com.uyghar.kitabhumar.models.User
import com.uyghar.kitabhumar.ui.home.hideSoftKeyboard
import okhttp3.*
import okhttp3.internal.wait
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var root: View
    private lateinit var waitDialog: Dialog
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    var is_login = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            is_login = it.getBoolean("is_login")
        }

    }

    fun createWaitDialog() {
        waitDialog = Dialog(requireContext())
        waitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        waitDialog.setCancelable(true)
        waitDialog.setContentView(R.layout.wait_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_email, container, false)
        createWaitDialog()
        val buttonSend = root.findViewById<Button>(R.id.buttonSend)
        buttonSend.setOnClickListener {
            if (is_login)
                firebaseLogin()
            else
                firebaseRegister()
        }
        editEmail = root.findViewById<EditText>(R.id.editEmail)
        editPassword = root.findViewById<EditText>(R.id.editPassword)
        val buttonReset: Button = root.findViewById(R.id.buttonReset)
        buttonReset.setOnClickListener {
            val email = editEmail.text.toString()
            if (!email.isNullOrBlank()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                            Snackbar.make(root,"OK",Snackbar.LENGTH_LONG).show()
                        else
                            it.exception?.printStackTrace()
                    }
            }
        }
        return root
    }

    fun getUser(uid: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL("http://172.104.143.75:8004/api/members/?uid=$uid"))
            .build()
        client.newCall(request).enqueue(
            object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Snackbar.make(root,e.toString(),Snackbar.LENGTH_LONG).show()

                }

                override fun onResponse(call: Call, response: Response) {
                    val json_str = response.body?.string()
                    val gson = GsonBuilder().create()
                    val user_array = gson.fromJson(json_str, Array<User>::class.java)
                    if (user_array.size > 0) {
                        saveToDB(user_array.first())
                        activity?.runOnUiThread {
                            waitDialog.dismiss()
                            findNavController().popBackStack(R.id.nav_home, true)
                            findNavController().navigate(R.id.userFragment)
                        }
                    } else
                        Snackbar.make(root,"خاتالىق كۆرۈلدى",Snackbar.LENGTH_LONG).show()


                }

            }
        )
    }

    fun saveToDB(user: User) {
        val userHelper = UserHelper(requireContext())
        userHelper.newMember(user)
    }

    fun firebaseLogin() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        auth.signInWithEmailAndPassword(email ?: "",
            password ?: "")
            .addOnCompleteListener { task ->
                hideSoftKeyboard(requireActivity())
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        if (it.isEmailVerified) {
                            val id = it.uid
                            getUser(id)
                        } else {
                            Snackbar.make(root, "ئېمەيل ئادرېسىڭىزنى تېخى دەلىللىمىدىڭىز", Snackbar.LENGTH_LONG).show()
                        }
                    }
                } else {
                    val localizedMessage = task.exception!!.localizedMessage
                  Snackbar.make(root, localizedMessage,Snackbar.LENGTH_LONG).show()
                }
            }

    }

    fun firebaseRegister() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        auth.createUserWithEmailAndPassword(
            email ?: "",
            password ?: ""
        )
            .addOnCompleteListener { task ->
                waitDialog.dismiss()
                hideSoftKeyboard(requireActivity())
                if (task.isSuccessful) {
                    //auth.signOut()
                    val user = auth.currentUser

                    user?.sendEmailVerification()
                        ?.addOnCompleteListener {
                            Log.i("regUser", "Email was sent")
                            val bundle = Bundle()
                            bundle.putString("email", email)
                            bundle.putString("password", password)
                            findNavController().navigate(R.id.verifyFragment,bundle)
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    //val message = task.exception?.message
                    val localizedMessage = task.exception!!.localizedMessage
//                    val errorCode =
//                        (task.exception as FirebaseAuthUserCollisionException).errorCode
                    Log.i("regUser", "createUserWithEmail:failure", task.exception)
                    Snackbar.make(root, localizedMessage,Snackbar.LENGTH_LONG).show()
                }
            }
    }




}