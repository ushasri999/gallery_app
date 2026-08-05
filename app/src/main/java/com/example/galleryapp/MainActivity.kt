package com.example.galleryapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.galleryapp.data.database.AppDataBase
import com.example.galleryapp.data.repositories.api.GalleryRepositoryApi
import com.example.galleryapp.presentation.GalleryViewEvent
import com.example.galleryapp.presentation.view.GalleryScreen
import com.example.galleryapp.presentation.GalleryViewModel
import com.example.galleryapp.presentation.IGalleryViewModel
import com.example.galleryapp.presentation.viewstate.GalleryEvent
import com.example.galleryapp.ui.theme.GalleryAppTheme
import kotlinx.coroutines.launch

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
            viewModel.dispatchGalleryViewEvent(GalleryViewEvent.MediaPermissionDenied)
            Log.d(TAG, "Permission Denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder<AppDataBase>(applicationContext, "favourites")
            .setDriver(AndroidSQLiteDriver())
            .build()

        val factory = GalleryViewModel.Factory(application, GalleryRepositoryApi(db.favouritesDao()))
        viewModel = ViewModelProvider(this, factory)[GalleryViewModel::class.java]

        requestMediaPermissionIfNeeded()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateFlow.collect { viewState ->
                    observeEventData(viewState.galleryEvent)
                }
            }
        }

        setContent {
            GalleryAppTheme {
                GalleryScreen(viewModel)
            }
        }
    }

    private fun observeEventData(event: GalleryEvent) {
        when {
            event.shouldAskMediaPermission -> {
                viewModel.dispatchGalleryViewEvent(GalleryViewEvent.MediaPermissionsAsked)
                openAppSettings()
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.example.galleryapp", null)
        intent.data = uri
        startActivity(intent)
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
