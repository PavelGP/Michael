package by.of.servicebook.myapplication.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import by.of.servicebook.myapplication.R;
import by.of.servicebook.myapplication.fragments.dialogs.BaseDialogFragment;
import by.of.servicebook.myapplication.fragments.dialogs.NoticeDialogFragment;
import by.of.servicebook.myapplication.utils.AppUtils;

/**
 * Created by p.gulevich on 07.07.2015.
 */
public class RegisterActivity extends ActionBarActivity {

    public static void launch (Activity activity){
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.initViews(this);
    }

    class ViewHolder{
        EditText etEmail, etPassword;
        Button btnEnter;
        ProgressBar progressBar;

        void initViews(Activity activity){
            activity.setContentView(R.layout.activity_register);
            etEmail = (EditText)activity.findViewById(R.id.etLogin);
            etPassword = (EditText)activity.findViewById(R.id.etPassword);
            btnEnter = (Button) activity.findViewById(R.id.btnEnter);
            progressBar = (ProgressBar) activity.findViewById(R.id.progress);

            btnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppUtils.isNetworkConnected(RegisterActivity.this)){
                        doSignUp();
                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.no_internet),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        void doSignUp(){
            if (!AppUtils.isValidEmail(etEmail.getText().toString())){
                Toast.makeText(RegisterActivity.this, getString(R.string.not_valid_email),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            ParseUser user = new ParseUser();
            user.setUsername(etEmail.getText().toString());
            user.setPassword(etPassword.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        //TODO move dialog into other activity
                        NoticeDialogFragment.newInstance(getString(R.string.successful_registration_title),
                                String.format(getString(R.string.successful_registration_message), etEmail.getText().toString(), etPassword.getText().toString()),
                                new BaseDialogFragment.BaseNoticeDialogListener() {
                                    @Override
                                    public void onDialogPositiveClick(DialogFragment dialog) {
                                        dialog.dismiss();
                                        doLogin();
                                    }
                                }).show(getFragmentManager(), null);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, getString(R.string.existing_email),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        void doLogin(){
            progressBar.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(etEmail.getText().toString(),
                    etPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null && parseUser != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                                SplashActivity.launch(RegisterActivity.this);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, getString(R.string.bad_login_or_password),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
