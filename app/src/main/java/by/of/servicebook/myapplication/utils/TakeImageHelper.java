package by.of.servicebook.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by p.gulevich on 03.08.2015.
 */
public class TakeImageHelper{
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String CAMERA_DIR = "/dcim/";
    private static final String ALBUM_NAME = "Michael";
    public static final String MIME_TYPE_IMAGE = "image/*";

    private Uri mPhotoUri;
    private Intent mResultData;

    private FragmentActivity activity;

    public TakeImageHelper(FragmentActivity activity) {
        this.activity = activity;
    }

    public Intent createImageChooserIntent(){
        //pick image
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
        pickImageIntent.setType(MIME_TYPE_IMAGE);
        //take photo
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = setupImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (photoFile != null) {
            mPhotoUri = Uri.fromFile(photoFile);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
        }

        //create chooser
        Intent chooserIntent = Intent.createChooser(pickImageIntent, "Choose resource");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});

        return chooserIntent;
    }

    public void setResultData(Intent data){
        mResultData = data;
    }

    public Uri getImageUri(){
        Uri imageUriFromData = mResultData.getData();
        if (imageUriFromData != null) {
            return imageUriFromData;
        } else if (mPhotoUri != null) {
            return mPhotoUri;
        }
        return null;
    }

    @SuppressLint("NewApi")
    public String getImagePath() {
        Uri uri = getImageUri();
        if( uri == null ) {
            return null;
        }
        //if we capture image from camera
        if (uri.getScheme().equals("file")){
            return uri.getPath();
        }
        //if we take image in gallery
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(uri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, sel, new String[]{id}, null);
            } catch (Exception e){
                e.printStackTrace();
                cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            }
        } else {
            cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        }

        String path = null;
        try {
            int column_index = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index).toString();
            cursor.close();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        return path;
    }

    //create temp image file in PICTURE folder
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    //create temp image file in DCIM folder
    public static File setupImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private static File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = getAlbumStorageDir(ALBUM_NAME);

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v("TAG", "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    public static File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}
