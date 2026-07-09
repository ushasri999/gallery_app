package com.example.galleryapp

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.galleryapp.data.GalleryImage
import com.example.galleryapp.presentation.GalleryScreen
import com.example.galleryapp.ui.theme.GalleryAppTheme

class MainActivity : ComponentActivity() {
    var images: List<GalleryImage> = emptyList()
    private val TAG = "MAIN_ACTIVITY";
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if(result) {
            images = fetchImages()
            Log.d(TAG, "Permission Granted")
            Log.d(TAG, "images = $images")
        } else {
            Log.d(TAG, "Permission Denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestMediaPermissionIfNeeded()

        setContent {
            GalleryAppTheme {
                GalleryScreen(images)
            }
        }
    }

    private fun fetchImages(): List<GalleryImage> {
        val images = mutableListOf<GalleryImage>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val columId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while(cursor.moveToNext()) {
                val id = cursor.getLong(columId)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                images.add(
                    GalleryImage(
                        id = id,
                        uri = uri
                    )
                )
            }
        }

        return images
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
            images = fetchImages()
        } else {
            permissionLauncher.launch(permission)
        }
    }
}
