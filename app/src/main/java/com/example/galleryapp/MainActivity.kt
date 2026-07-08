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
import com.example.galleryapp.presentation.GalleryScreen
import com.example.galleryapp.ui.theme.GalleryAppTheme

class MainActivity : ComponentActivity() {
    private val TAG = "MAIN_ACTIVITY";
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if(result) {
            Log.d(TAG, "Permission Granted")
        } else {
            Log.d(TAG, "Permission Denied")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(permission)
        }

        setContent {
            GalleryAppTheme {
                GalleryScreen()
            }
        }
    }
}
