package com.example.galleryapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.galleryapp.data.repositories.api.GalleryRepositoryApi
import com.example.galleryapp.presentation.GalleryViewEvent
import com.example.galleryapp.presentation.view.GalleryScreen
import com.example.galleryapp.presentation.GalleryViewModel
import com.example.galleryapp.presentation.IGalleryViewModel
import com.example.galleryapp.ui.theme.GalleryAppTheme

class MainActivity : ComponentActivity() {
    private val TAG = "MAIN_ACTIVITY";
    private lateinit var viewModel: IGalleryViewModel
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if(result) {
            viewModel.dispatchGalleryViewEvent(GalleryViewEvent.FetchImages)
            Log.d(TAG, "Permission Granted")
        } else {
            Log.d(TAG, "Permission Denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val factory = GalleryViewModel.Factory(application, GalleryRepositoryApi())
        viewModel = ViewModelProvider(this, factory)[GalleryViewModel::class.java]

        requestMediaPermissionIfNeeded()

        setContent {
            GalleryAppTheme {
                GalleryScreen(viewModel)
            }
        }
    }

    private fun mediaPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermissionForReadingImages(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun requestMediaPermissionIfNeeded() {
        val permission: String = getPermissionForReadingImages()

        if (mediaPermissionGranted(permission)) {
            viewModel.dispatchGalleryViewEvent(GalleryViewEvent.FetchImages)
        } else {
            permissionLauncher.launch(permission)
        }
    }
}
