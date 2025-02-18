package com.example.cameraapp;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploader {
    private final Context context;

    public FileUploader(Context context) {
        this.context = context;
    }

    public void uploadFile(Uri fileUri) {
        File file = getFileFromUri(fileUri);
        if (file == null) {
            Log.e("Upload", "File conversion failed");
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "File Upload");

        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.uploadFile(body, description);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("Upload", "File Uploaded Successfully");
                } else {
                    Log.e("Upload", "Upload Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload", "Upload Error: " + t.getMessage());
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        try {
            ContentResolver resolver = context.getContentResolver();
            InputStream inputStream = resolver.openInputStream(uri);

            String fileName = getFileName(uri);
            File tempFile = new File(context.getCacheDir(), fileName);
            OutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();
            inputStream.close();
            outputStream.close();
            return tempFile;
        } catch (Exception e) {
            Log.e("Upload", "File conversion error: " + e.getMessage());
            return null;
        }
    }

    private String getFileName(Uri uri) {
        String fileName = "temp_file";
        ContentResolver resolver = context.getContentResolver();
        try (android.database.Cursor cursor = resolver.query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
            }
        }
        return fileName;
    }
}
