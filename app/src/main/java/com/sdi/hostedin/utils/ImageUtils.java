package com.sdi.hostedin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    public static byte[] uriToBytes(Context context, Uri uri)  {
        byte[] imageBytes = null;
        if (uri != null) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }

                imageBytes = byteArrayOutputStream.toByteArray();

            } catch (IOException e) {
                Log.e("ProfilePhoto", "Error reading image bytes: " + e.getMessage(), e);
            }
        }

        return imageBytes;
    }

    public static Bitmap bytesToBitmap(byte[] imageBytes) {
        Bitmap bitmap = null;

        if (imageBytes != null && imageBytes.length > 0) {
            try {
                bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            } catch (Exception e) {
                Log.e("ProfilePhoto", "Error converting bytes to Bitmap: " + e.getMessage(), e);
            }
        }

        return bitmap;
    }

    public static byte[] loadProfilePhoto(User user, ImageView imvProfilePhoto) {
        byte[] imageData = null;

        if (user.getProfilePhoto() != null) {
            imageData = user.getProfilePhoto().getData();
            Bitmap profilePhoto = ImageUtils.bytesToBitmap(imageData);

            if (profilePhoto != null) {
                imvProfilePhoto.setImageBitmap(profilePhoto);
            } else {
                imvProfilePhoto.setImageResource(R.drawable.profile_icon);
            }
        } else {
            imvProfilePhoto.setImageResource(R.drawable.profile_icon);
        }

        return imageData;
    }

    public static void loadAccommodationImage(byte[] bytesImage, ImageView imvAccommodation) {
        if (bytesImage != null) {
            Bitmap bitmapImage = ImageUtils.bytesToBitmap(bytesImage);

            if (bitmapImage != null) {
                imvAccommodation.setImageBitmap(bitmapImage);
            }
        }
    }

    public static File createTempVideoFile(Context context, byte[] videoBytes) throws IOException {
        File tempVideoFile = new File(context.getCacheDir(), "temp_video.mp4");
        try (FileOutputStream fos = new FileOutputStream(tempVideoFile)) {
            fos.write(videoBytes);
            fos.flush();
        }
        return tempVideoFile;
    }

    public static File createTempFileFromBytes(Context context, byte[] bytes, String fileName) throws IOException {
        File file = new File(context.getCacheDir(), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }

        return file;
    }

    public static Uri getUriFromFile(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
    }
}