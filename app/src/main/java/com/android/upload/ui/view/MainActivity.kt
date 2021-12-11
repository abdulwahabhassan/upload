package com.android.upload.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.upload.databinding.ActivityMainBinding
import com.android.upload.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import androidx.activity.result.contract.ActivityResultContracts
import com.android.upload.ui.contract.GalleryActivityContract


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var image: String? = null
    private val viewModel: MainViewModel by viewModels()
    private val openGallery = registerForActivityResult(GalleryActivityContract()) { imagePath ->
    if (imagePath != null) {
        Toast.makeText(this, imagePath, Toast.LENGTH_LONG).show()
        image = imagePath
        displaySelectedPhoto(imagePath)
    }
    else Toast.makeText(this, "No Result", Toast.LENGTH_LONG).show()

}
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted -> if (isGranted) {
            enableImageSelection()
    } else {
        val showRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        if (showRationale) {
            showNoAccess()
        } else {
            goToSettings()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        enableImageSelection()

        binding.grantPermissionButton.setOnClickListener { enableImageSelection() }

        viewModel.feedBack.observe(this) {
            Log.d(TAG, "${it?.message}")
        }

        binding.uploadButton.setOnClickListener {
                if (image != null) {
                    val file = File(image!!)
                    val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val body: MultipartBody.Part = createFormData("image", file.name, requestFile)
                    lifecycleScope.launch {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                            viewModel.uploadImage(body)
                        }
                    }
                } else Toast.makeText(
                    this,
                    "Click to select an image to post",
                    Toast.LENGTH_LONG).show()
        }

        if (!haveStoragePermission()) {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun displaySelectedPhoto(imageUri: String) {
        Glide.with(this).load(imageUri).into(binding.imageView)
    }

    private fun goToSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            startActivity(intent)
        }
    }

    private fun enableImageSelection() {
        if (haveStoragePermission()) {
            binding.imageView.setOnClickListener {
                openGallery.launch(Unit)
            }

            binding.permissionRationaleView.visibility = View.GONE
        } else {
            requestPermission()
        }
    }

    private fun showNoAccess() {
        binding.permissionRationaleView.visibility = View.VISIBLE
    }

    companion object {
        const val TAG = "LogResponse"
    }

}