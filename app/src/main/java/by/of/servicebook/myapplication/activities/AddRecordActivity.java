package by.of.servicebook.myapplication.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import by.of.servicebook.myapplication.fragments.dialogs.BaseDialogFragment;
import by.of.servicebook.myapplication.fragments.dialogs.NoticeDialogFragment;
import by.of.servicebook.myapplication.parse.models.ParseService;
import by.of.servicebook.myapplication.utils.AppUtils;
import by.of.servicebook.myapplication.utils.TakeImageHelper;


public class AddRecordActivity extends ActionBarActivity {
    private final int TAKE_PHOTO_REQCODE = 100;
    private final int CAPTURE_IMAGE_REQCODE = 101;

    private ImageView ivDoc;
    private TakeImageHelper mTakeImageHelper;

    private ParseFile mDocFile;
    private String mClientEmail, mServiceId, mServiceName;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        ivDoc = (ImageView) findViewById(R.id.ivDoc);

        //take image
        Button btnTakeImage = (Button) findViewById(R.id.btnFile);
        btnTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
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



    private void takeImage() {
        mTakeImageHelper = new TakeImageHelper(this);
        Intent intent = mTakeImageHelper.createImageChooserIntent();
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_PHOTO_REQCODE);
        }
    }

    private void sendClientInfo(){
        //internet connection validation
        if (!AppUtils.isNetworkConnected(this)){
            NoticeDialogFragment.newInstance(getString(R.string.warning), getString(R.string.no_internet), new BaseDialogFragment.BaseNoticeDialogListener() {
                @Override
                public void onDialogPositiveClick(DialogFragment dialog) {
                    dialog.dismiss();
                }
            })
                    .show(getFragmentManager(), null);
            return;
        }

        //file validation
        if (mDocFile == null){
            NoticeDialogFragment.newInstance(getString(R.string.warning), getString(R.string.no_file), new BaseDialogFragment.BaseNoticeDialogListener() {
                @Override
                public void onDialogPositiveClick(DialogFragment dialog) {
                    dialog.dismiss();
                }
            })
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
        if (requestCode == TAKE_PHOTO_REQCODE && resultCode == Activity.RESULT_OK) {
            if (data == null) return;

            mTakeImageHelper.setResultData(data);

            if (mTakeImageHelper.getImageUri() == null) return;

            //show image
            Uri imageUri = mTakeImageHelper.getImageUri();

            ivDoc.setImageURI(imageUri);

            Log.d("TAG", "take image");

            //create ParseFile object
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
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
}
