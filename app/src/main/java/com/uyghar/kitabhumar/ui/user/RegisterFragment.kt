package com.uyghar.kitabhumar.ui.user

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uyghar.kitabhumar.R
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.Window
import android.widget.ImageView
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.security.Permission
import android.graphics.drawable.BitmapDrawable





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
    private val CAMERA_REQUEST = 1888
    private val ALBUM_REQUEST = 2888
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val MY_ALBUM_PERMISSION_CODE = 200
    private var image: Image? = null

    private lateinit var buttonImage: ImageButton
    private lateinit var dialog: Dialog
    var from = 0 //This must be declared as global !

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
        val editName = root.findViewById<EditText>(R.id.editName)
        val editSurName = root.findViewById<EditText>(R.id.editSurname)
        val editUserName = root.findViewById<EditText>(R.id.editUsername)

        val editPassword = root.findViewById<EditText>(R.id.editPassword)
        buttonImage = root.findViewById<ImageButton>(R.id.buttonImage)
        buttonImage.setOnClickListener {
            dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.album_camera)
            val buttonCamera = dialog.findViewById<Button>(R.id.buttonCamera)
            val buttonAlbum = dialog.findViewById<Button>(R.id.buttonAlbum)
            val buttonClose = root.findViewById<Button>(R.id.buttonClose)

            val listener = View.OnClickListener { button ->
                when(button.id) {
                    R.id.buttonCamera -> openCamera()
                    R.id.buttonAlbum -> openAlbum()
                    R.id.buttonClose -> buttonImage.setImageResource(R.drawable.ic_baseline_image_24)
                }

            }
            buttonCamera.setOnClickListener(listener)
            buttonAlbum.setOnClickListener(listener)
            buttonClose.setOnClickListener(listener)
            dialog.show()

            /*val choice = arrayOf("Choose from Gallery", "Capture a photo")

            val alert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
            alert.setTitle("Upload Photo")
            alert.setSingleChoiceItems(choice, -1,
                DialogInterface.OnClickListener { dialog, which ->
                    if (choice[which] === "Choose from Gallery") {
                        from = 1
                    } else if (choice[which] === "Capture a photo") {
                        from = 2
                    }
                })
            alert.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                if (from == 0) {
                    Toast.makeText(
                        activity, "Select One Choice",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (from == 1) {
                    // Your Code
                } else if (from == 2) {
                    // Your Code
                }
            })
            alert.show()*/


        }
        buttonReg.setOnClickListener {
            val email = editEmail.text.toString()
            val name = editName.text.toString()
            val surName = editSurName.text.toString()
            val userName = editUserName.text.toString()
            //val bitmap = (buttonImage.drawable as BitmapDrawable).bitmap
            val params = HashMap<String,String>()
            params["email"] = email
            params["name"] = name
            params["nickname"] = userName
            params["surname"] = surName
            //val params = ["email":email,"name":name,"surname":surname,"username":userName]
            Thread() {
                postMultipart("http://172.104.143.75:8004/api/members/",params,ArrayList())
            }.start()

            /*if (is_login) {
                auth.signInWithEmailAndPassword(editEmail.text.toString(),
                    editPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i("regUser", "Login")
                        }
                        else {
                            Log.i("regUser", it.exception?.localizedMessage.toString())
                        }
                    }
                    /*.addOnFailureListener {
                        //Log.i("regUser", it.toString())

                        Log.i("regUser", it.getLocalizedMessage())
                    }*/
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
                            Log.i("regUser", task.exception?.localizedMessage ?: "")

                        }
                    }
            }*/
        }
        return root
    }

    fun openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(requireContext(),Manifest.permission.CAMERA) != PermissionChecker.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    MY_CAMERA_PERMISSION_CODE
                )
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }
    }

    fun postMultipart(url: String, params: HashMap<String, String>, images: List<Uri>) {
        var formBody = MultipartBody.Builder()
        formBody.setType(MultipartBody.FORM)
        for ((k, v) in params) {
            formBody.addFormDataPart(k, v.toString())
        }

        images.forEach {
            val imgPath = getRealPathFromURI(requireContext(), it) ?: return
            val file = File(imgPath)
            var typeStr = "images/jpeg"
            var fileName = "file.jpg"
            if (file.name.substringAfterLast('.',"").capitalize().equals("PNG")) {
                typeStr = "images/png"
                fileName = "file.png"
            }
            val fileRequestBody = file.asRequestBody(typeStr.toMediaTypeOrNull())
            formBody.addFormDataPart("images[]", fileName, fileRequestBody)
        }

        val requestBody = formBody.build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.i("Submit", "OK")

                } else {
                    Log.i("Submit", "Failed")

                }
            }

        }

    fun getRealPathFromURI(context: Context, contentURI: Uri): String? {
        val result: String?
        val cursor =
            context.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun openAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_ALBUM_PERMISSION_CODE
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, ALBUM_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dialog.dismiss()
        if (resultCode == Activity.RESULT_OK) {

            when(requestCode) {
                CAMERA_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    buttonImage.setImageBitmap(bitmap)

                }
                ALBUM_REQUEST -> {
                    buttonImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    buttonImage.setImageURI(data?.data)
                }

            }
        }

    }
}