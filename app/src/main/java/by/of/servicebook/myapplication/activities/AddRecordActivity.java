package by.of.servicebook.myapplication.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import by.of.servicebook.myapplication.R;
import by.of.servicebook.myapplication.fragments.dialogs.NoticeDialogFragment;
import by.of.servicebook.myapplication.parse.models.ParseService;
import by.of.servicebook.myapplication.utils.AppConst;
import by.of.servicebook.myapplication.utils.AppUtils;


public class AddRecordActivity extends ActionBarActivity {
    private final int PICK_PHOTO_REQCODE = 100;
    private final int CAPTURE_IMAGE_REQCODE = 101;

    private ImageView ivDoc;

    private ParseFile mDocFile;
    private String mClientEmail, mServiceId, mServiceName;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        ivDoc = (ImageView) findViewById(R.id.ivDoc);

        //pick image
        Button btnFile = (Button) findViewById(R.id.btnFile);
        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        //capture image
        Button btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendClientInfo();
            }
        });

        setToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }



    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_REQCODE);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_REQCODE);
    }

    private void sendClientInfo(){
        //internet connection validation
        if (!AppUtils.isNetworkConnected(this)){
            NoticeDialogFragment.newInstance(getString(R.string.warning), getString(R.string.no_internet), null)
                    .show(getFragmentManager(), null);
            return;
        }

        //file validation
        if (mDocFile == null){
            NoticeDialogFragment.newInstance(getString(R.string.warning), getString(R.string.no_file), null)
                    .show(getFragmentManager(), null);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.file_sending));
        progressDialog.show();

        ParseUser user = ParseUser.getCurrentUser();

        mDocFile.saveInBackground();
        ParseService parseService = new ParseService("service_id", "service_name", user.getUsername(), mDocFile);
        parseService.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if (e == null){
                    Toast.makeText(AddRecordActivity.this, getString(R.string.file_successfully_sent), Toast.LENGTH_SHORT).show();
                    AddRecordActivity.this.finish();
                } else {
                    Toast.makeText(AddRecordActivity.this, getString(R.string.file_not_sent), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDocFile = null;
        if (requestCode == PICK_PHOTO_REQCODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }

            //show image
            Uri selectedImage = data.getData();

            String filePath = getPath(selectedImage);

            if(filePath!=null) {
                Bitmap photo = BitmapFactory.decodeFile(filePath);
                ivDoc.setImageBitmap(photo);
            } else {
                try {
                    InputStream is = getContentResolver().openInputStream(selectedImage);
                    ivDoc.setImageBitmap(BitmapFactory.decodeStream(is));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }



            Log.d("TAG", "got result from galery");

            //create ParseFile object
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] fileData = new byte[16384];
                while ((nRead = inputStream.read(fileData, 0, fileData.length)) != -1) {
                    buffer.write(fileData, 0, nRead);
                }
                buffer.flush();
                byte[] allFileData = buffer.toByteArray();

                mDocFile = new ParseFile(allFileData);

            } catch (IOException e){
                e.printStackTrace();
            }
        } else if (requestCode == CAPTURE_IMAGE_REQCODE && resultCode == Activity.RESULT_OK){
            if (data == null) {
                //Display an error
                return;
            }

            //show image
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivDoc.setImageBitmap(photo);
            Log.d("TAG", "got camera result");

            //create ParseFile object
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] fileData = new byte[16384];
                while ((nRead = inputStream.read(fileData, 0, fileData.length)) != -1) {
                    buffer.write(fileData, 0, nRead);
                }
                buffer.flush();
                byte[] allFileData = buffer.toByteArray();

                mDocFile = new ParseFile(allFileData);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor;
        if(Build.VERSION.SDK_INT >19){
            // Will return "image:x*"
            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, sel, new String[]{ id }, null);
        } else {
            cursor = getContentResolver().query(uri, projection, null, null, null);
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

}
