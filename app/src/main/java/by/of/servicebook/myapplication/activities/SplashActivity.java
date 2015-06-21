package by.of.servicebook.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseUser;

import by.of.servicebook.myapplication.R;
import by.of.servicebook.myapplication.commands.SyncData;
import by.of.servicebook.myapplication.utils.AppUtils;

public class SplashActivity extends ActionBarActivity {
    ViewHolder viewHolder;

    public static void launch (Activity activity){
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewHolder = new ViewHolder();
        viewHolder.initViews(this);

        ParseUser user = ParseUser.getCurrentUser();
        if (user == null){
            LoginActivity.launch(this);
        } else if (AppUtils.isNetworkConnected(this)) {
            new SyncData(this, syncCallback).execute();
        } else {
            MainActivity.launch(SplashActivity.this);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            animateLogo();
            animateDescription();
        }
    }

    private SyncData.Callback syncCallback = new SyncData.Callback() {
        @Override
        public void doCallback() {
            MainActivity.launch(SplashActivity.this);
        }
    };

    public Rect locateView(View view) {
        Rect loc = new Rect();
        int[] location = new int[2];
        if (view == null) {
            return loc;
        }
        view.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        loc.right = loc.left + view.getWidth();
        loc.bottom = loc.top + view.getHeight();
        return loc;
    }

    public void animateLogo() {
        float leftLogoEdge = locateView(viewHolder.tvLogo).left;
        int logoWidth = viewHolder.tvLogo.getWidth();
        float fromXLogo = -(leftLogoEdge + logoWidth);
        Animation translateLogo = new TranslateAnimation(fromXLogo, 0, 0, 0);
        translateLogo.setDuration(900);
        viewHolder.tvLogo.startAnimation(translateLogo);
    }

    public void animateDescription() {
        float rightLogoEdge = locateView(viewHolder.tvDescription).right;
        int logoWidth = viewHolder.tvDescription.getWidth();
        float fromXLogo = (rightLogoEdge + logoWidth);
        Animation translateLogo = new TranslateAnimation(fromXLogo, 0, 0, 0);
        translateLogo.setDuration(900);
        viewHolder.tvDescription.startAnimation(translateLogo);
    }

    private class ViewHolder{
        TextView tvLogo, tvDescription;

        void initViews(Activity activity){
            tvLogo = (TextView) activity.findViewById(R.id.tvLogo);
            tvDescription = (TextView) activity.findViewById(R.id.tvDescription);
        }
    }

}
