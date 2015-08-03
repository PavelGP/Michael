package by.of.servicebook.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import by.of.servicebook.myapplication.parse.models.ParseCar;
import by.of.servicebook.myapplication.parse.models.ParseDetail;
import by.of.servicebook.myapplication.parse.models.ParseJob;
import by.of.servicebook.myapplication.parse.models.ParseRecord;
import by.of.servicebook.myapplication.parse.models.ParseService;
import by.of.servicebook.myapplication.parse.models.ParseStatistic;
import by.of.servicebook.myapplication.utils.AppConst;

/**
 * Created by Pavel on 10.12.2014.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        //register parse.com subclasses
        ParseObject.registerSubclass(ParseCar.class);
        ParseObject.registerSubclass(ParseRecord.class);
        ParseObject.registerSubclass(ParseJob.class);
        ParseObject.registerSubclass(ParseDetail.class);
        ParseObject.registerSubclass(ParseStatistic.class);
        ParseObject.registerSubclass(ParseService.class);
        Parse.initialize(this, "ZjTI65G77ck4IneaorWJTVdZnUz6YBUTkC8GOpxh", "h9gPunO5xfdcEF4yVQXoRGhjkRaUzEc550GCZvi1");
    }
}
