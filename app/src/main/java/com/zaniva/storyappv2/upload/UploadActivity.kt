package com.zaniva.storyappv2.upload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zaniva.storyappv2.databinding.ActivityUploadBinding
import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.zaniva.storyappv2.story.HomeActivity
import com.zaniva.storyappv2.story.ui.home.HomeFragment
import com.zaniva.storyappv2.utlis.createCustomTempFile
import com.zaniva.storyappv2.utlis.rotateBitmap
import com.zaniva.storyappv2.utlis.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var vm: UploadVM

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        vm = ViewModelProvider(this).get(
            UploadVM::class.java
        )

        setMyButtonEnable()

        binding.apply {
            etDesc.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setMyButtonEnable()
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
            btCamerax.setOnClickListener { startCameraX() }
            btCamera.setOnClickListener { startTakePhoto() }
            btGallery.setOnClickListener { startGallery() }
            btUpload.setOnClickListener { uploadImage() }
        }

    }

    private fun uploadImage() {
        val token = intent.getStringExtra(TOKEN).toString()
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description = binding.etDesc.text.toString()
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            showLoading(true)
            vm.upload(token, description, imageMultipart)
            vm.load.observe(this) {
                showLoading(it)
                Intent(this, HomeActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
                Toast.makeText(this, "Upload Success!", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this@UploadActivity, "Please Input a Picture!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.zaniva.storyappv2",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun reduceFileImage(file: File): File {
        return file
    }

    private fun showLoading(load: Boolean) {
        if (load) {
            binding.pbHome.visibility = View.VISIBLE
        } else {
            binding.pbHome.visibility = View.GONE
        }
    }

    private fun setMyButtonEnable() {
        binding.btUpload.isEnabled = binding.etDesc.text!!.isNotEmpty()
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.ivPhoto.setImageBitmap(result)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(myFile.path)

            binding.ivPhoto.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)

            getFile = myFile
            binding.ivPhoto.setImageURI(selectedImg)
        }
    }


    companion object {
        const val TOKEN = "token"
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}