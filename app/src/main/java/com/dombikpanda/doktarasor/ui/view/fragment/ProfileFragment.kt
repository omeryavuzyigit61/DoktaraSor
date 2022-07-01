package com.dombikpanda.doktarasor.ui.view.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.dombikpanda.doktarasor.databinding.FragmentProfileBinding
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dombikpanda.doktarasor.R
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import android.text.InputFilter.LengthFilter
import androidx.lifecycle.ViewModelProvider
import com.dombikpanda.doktarasor.showShortToast
import com.dombikpanda.doktarasor.ui.view.activity.LoginActivity
import com.dombikpanda.doktarasor.ui.viewmodel.ProfileViewModel
import timber.log.Timber
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var uid: String
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var globalProfileImage: CircleImageView
    private lateinit var profileName: TextView
    private lateinit var profilePhone: TextView
    private lateinit var aboutDescription: TextView
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()
        uid= mAuth.uid.toString()
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )
        profileViewModel.userLiveData.observe(viewLifecycleOwner,
            { firebaseUser ->
                if (firebaseUser != null) {
                    Timber.i("Success")
                }
            })

        binding.apply {
            globalProfileImage = profileImage
            profileName = tvProfileName
            profilePhone = tvProfilePhone
            aboutDescription = tvAboutDescription

            profileViewModel.retrieveUser(
                profileName,
                profilePhone,
                aboutDescription,
                globalProfileImage,
                requireContext(),
                "users"
            )

            if (currentUser?.photoUrl == null) {
                Timber.i("Gmail if there is no photo")
            } else {
                Glide.with(this@ProfileFragment).load(currentUser.photoUrl).into(profileImage)
            }
            userDetailLinear.setOnClickListener {
                showalertdialog(
                    getString(R.string.enter_yor_name),
                    getString(R.string.name),
                    tvProfileName
                )
            }

            userAboutLinear.setOnClickListener {
                showalertdialog(
                    getString(R.string.add_about_me),
                    getString(R.string.about),
                    tvAboutDescription
                )
            }

            fabAddProfile.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                     ImagePicker.with(this@ProfileFragment)
                        .crop()
                        .cropOval()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start()

                } else {
                    //Request permission
                    ActivityCompat.requestPermissions(
                        requireContext() as Activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        121
                    )
                }
            }

            btnLogout.setOnClickListener {
                mAuth.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }

    private var userMap: HashMap<String, Any> = HashMap()
    //Update
    private fun updateUser(updateText: EditText, updateField: String) {

        userMap[updateField] = updateText.text.toString()
        profileViewModel.updateUser(userMap, requireActivity(),uid)

    }

    private fun uploadImage() {
        if (mSelectedImageFileUri != null) {
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "users/ $uid/profile.jpg"
            )
            sRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            showShortToast(getString(R.string.image_uploaded_successfully) + "$url")
                        }
                }.addOnFailureListener {
                    showShortToast(getString(R.string.image_uploaded_error))
                }
        } else {
            showShortToast(getString(R.string.select_image))
        }
    }

    private fun showalertdialog(titleText: String, hintText: String, valueEditText: TextView) {
        //Inflate the dialog with custom view
        val mAboutDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.about_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mAboutDialogView)
        val mAlertDialog = mBuilder.show()
        //All get/set veriable and value set text
        val tvTitle = mAboutDialogView.findViewById<TextView>(R.id.tv_dialog_title)
        tvTitle.text = titleText

        val tilText = mAboutDialogView.findViewById<TextInputLayout>(R.id.til_dialog_text)
        tilText.hint = hintText

        val etText = mAboutDialogView.findViewById<EditText>(R.id.et_dialog_text)
        etText.setText(valueEditText.text.toString())

        val textField: String

        if (tilText.hint.toString() == getString(R.string.name)) {
            tilText.counterMaxLength =
                30  // Max counter value but this maximum text lenght does not limit
            etText.filters =
                arrayOf<InputFilter>(LengthFilter(30))  // This EditText maximum text lenght 30 character
            textField = "fullname"
        } else {
            tilText.counterMaxLength = 120
            textField = "about"
        }

        mAboutDialogView.findViewById<Button>(R.id.btn_dialog_about_save)
            .setOnClickListener {
                mAlertDialog.dismiss()
                updateUser(etText, textField)
                profileViewModel.retrieveUser(
                    profileName,
                    profilePhone,
                    aboutDescription,
                    globalProfileImage,
                    requireContext(),
                    "users"
                )
            }
        mAboutDialogView.findViewById<Button>(R.id.btn_dialog_about_cancel)
            .setOnClickListener {
                mAlertDialog.dismiss()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 121) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,222)

        } else {
            showShortToast(getString(R.string.storage_permission_error))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    mSelectedImageFileUri = data.data!!
                    globalProfileImage.setImageURI(mSelectedImageFileUri)
                    uploadImage()
                } catch (e: IOException) {
                 //   fileLogger.logError(e.message.toString())
                    e.printStackTrace()
                    showShortToast(getString(R.string.image_selection_failed))
                }
            }
        }
    }
}