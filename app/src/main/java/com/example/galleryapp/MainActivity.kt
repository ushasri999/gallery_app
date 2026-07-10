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
import com.example.galleryapp.data.entities.GalleryImageEntity
import com.example.galleryapp.data.repositories.api.GalleryRepositoryApi
import com.example.galleryapp.presentation.GalleryScreen
import com.example.galleryapp.presentation.viewmodel.GalleryViewModel
import com.example.galleryapp.presentation.viewmodel.IGalleryViewModel
import com.example.galleryapp.ui.theme.GalleryAppTheme

class MainActivity : ComponentActivity() {
    var images: List<GalleryImageEntity> = emptyList()
    private val TAG = "MAIN_ACTIVITY";
    private lateinit var viewModel: IGalleryViewModel
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if(result) {
            images = viewModel.fetchImages()
            Log.d(TAG, "Permission Granted")
            Log.d(TAG, "images = $images")
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
                GalleryScreen(images)
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
            images = viewModel.fetchImages()
        } else {
            permissionLauncher.launch(permission)
        }
    }
}
