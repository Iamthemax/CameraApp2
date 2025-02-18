package com.example.cameraapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SecondActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_STORAGE = 102;
    private ImageView imageView;
    private Uri selectedImageUri = null;

    // ActivityResultLauncher for selecting an image from the gallery
    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imageView.setImageURI(uri);
                    FileUploader fileUploader=new FileUploader(SecondActivity.this);
                    fileUploader.uploadFile(uri);
                    Toast.makeText(SecondActivity.this, "Image Selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SecondActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            });
    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageLauncher2 =
    registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
        if (uris != null && !uris.isEmpty()) {
            if (uris.size() < 1) {
                Toast.makeText(this, "Select at least " + 1 + " image(s)", Toast.LENGTH_SHORT).show();
            } else if (uris.size() > 5) {
                Toast.makeText(this, "You can select up to " + 5 + " images only", Toast.LENGTH_SHORT).show();
            } else {
                //selectedImageUris = uris;
                imageView.setImageURI(uris.get(0)); // Display first image
                Toast.makeText(this, uris.size() + " Image(s) Selected", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView = findViewById(R.id.imageView);
        Button selectImageButton = findViewById(R.id.selectImageButton);

        // Request necessary permissions
        requestPermissions();

        selectImageButton.setOnClickListener(v -> openGallery());
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_STORAGE);
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { // Android 9 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        pickImageLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}
