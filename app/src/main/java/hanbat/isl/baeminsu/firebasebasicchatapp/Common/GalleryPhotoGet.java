package hanbat.isl.baeminsu.firebasebasicchatapp.Common;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ssangwoo on 2018-01-08.
 */

public class GalleryPhotoGet extends ContextWrapper {
    public static final int IMAGE_PERMISSION_REQUEST_CODE = 100;
    public static final int PICK_FROM_ALBUM = 90;

    private boolean isActivity;
    private Fragment fragment;
    private AppCompatActivity appCompatActivity;

    public GalleryPhotoGet(Context base, Fragment fragment) {
        super(base);
        this.fragment = fragment;
        isActivity = false;
    }

    public GalleryPhotoGet(Context base, AppCompatActivity appCompatActivity) {
        super(base);
        this.appCompatActivity = appCompatActivity;
        isActivity = true;
    }


    public void checkPhotoPermission() {


        if (isActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (appCompatActivity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "갤러리 접근 허용 요청", Toast.LENGTH_SHORT).show();
                    }
                    appCompatActivity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_PERMISSION_REQUEST_CODE);

                } else {
                    doTakeAlbumAction();
                }

            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//                }
                    fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_PERMISSION_REQUEST_CODE);
                } else {
                    // 사용자 언제나 허락시
                    doTakeAlbumAction();
                }
            }
        }


    }

    public void doTakeAlbumAction() {

        if (isActivity) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            appCompatActivity.startActivityForResult(intent, PICK_FROM_ALBUM);

        } else {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            fragment.startActivityForResult(intent, PICK_FROM_ALBUM);
        }
    }

    public String getImageNameToUri(Uri data) {


        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor;
        if (isActivity) {
            cursor = appCompatActivity.managedQuery(data, proj, null, null, null);

        } else {
            cursor = fragment.getActivity().managedQuery(data, proj, null, null, null);
        }

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);

        return imgPath.substring(imgPath.lastIndexOf("/") + 1);


    }

    public String saveBitmapToJpeg(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String folder_name = "/" + folder + "/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + folder_name;
        String imgUpLoadPath = string_path + file_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
            return null;
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
            return null;
        }

        return imgUpLoadPath;
    }

    public Bitmap makeBitmap(Uri selectImageUri) {
        try {
            Cursor c = this.getContentResolver().query(Uri.parse(selectImageUri.toString()),
                    null, null, null, null);
            c.moveToNext();
            String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));

            Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(
                    this.getContentResolver(), selectImageUri);

            //이미지 리사이징
            int height = image_bitmap.getHeight();
            int width = image_bitmap.getWidth();

            Log.e("체크", absolutePath);
            Bitmap src = BitmapFactory.decodeFile(absolutePath);

            return Bitmap.createScaledBitmap(src, width / 2, height / 2, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
