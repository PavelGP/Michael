package by.of.servicebook.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import by.of.servicebook.myapplication.R;
import by.of.servicebook.myapplication.utils.AppUtils;

public class LoginActivity extends ActionBarActivity {

    public static void launch (Activity activity){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.initViews(this);
    }

    class ViewHolder{
        EditText etLogin, etPassword;
        Button btnEnter, btnRegister;
        ProgressBar progressBar;

        void initViews(Activity activity){
            activity.setContentView(R.layout.activity_login);
            etLogin = (EditText)activity.findViewById(R.id.etLogin);
            etPassword = (EditText)activity.findViewById(R.id.etPassword);
            btnEnter = (Button) activity.findViewById(R.id.btnEnter);
            btnRegister = (Button) activity.findViewById(R.id.btnRegister);
            progressBar = (ProgressBar) activity.findViewById(R.id.progress);

            btnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppUtils.isNetworkConnected(LoginActivity.this)){
                        doLogin();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.no_internet),
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegisterActivity.launch(LoginActivity.this);
                }
            });
        }

        void doLogin(){
            progressBar.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(etLogin.getText().toString(),
                    etPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null && parseUser != null){
                                progressBar.setVisibility(View.INVISIBLE);
                                SplashActivity.launch(LoginActivity.this);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this, getString(R.string.bad_login_or_password),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
