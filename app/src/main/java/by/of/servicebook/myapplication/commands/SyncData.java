package by.of.servicebook.myapplication.commands;

import android.content.Context;
import android.os.AsyncTask;

import com.activeandroid.query.Delete;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import by.of.servicebook.myapplication.db.models.Car;
import by.of.servicebook.myapplication.db.models.Detail;
import by.of.servicebook.myapplication.db.models.Job;
import by.of.servicebook.myapplication.db.models.Record;
import by.of.servicebook.myapplication.db.models.Statistic;
import by.of.servicebook.myapplication.parse.models.ParseCar;
import by.of.servicebook.myapplication.parse.models.ParseDetail;
import by.of.servicebook.myapplication.parse.models.ParseJob;
import by.of.servicebook.myapplication.parse.models.ParseRecord;
import by.of.servicebook.myapplication.parse.models.ParseStatistic;

/**
 * Created by Pavel on 27.04.2015.
 */
public class SyncData extends AsyncTask<Void, Void, Void> {
    Callback callback;
    Context context;

    public SyncData(Context context, Callback callback){
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void[] params) {
        ParseUser user = ParseUser.getCurrentUser();

        startDownload(user);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.doCallback();
    }

    private void startDownload(ParseUser user){
        try {
            //clear tables
            new Delete().from(Car.class).execute();
            new Delete().from(Record.class).execute();
            new Delete().from(Job.class).execute();
            new Delete().from(Detail.class).execute();
            new Delete().from(Statistic.class).execute();

            ParseRelation<ParseCar> carRelation = user.getRelation("cars_relation");
            List<ParseCar> parseCarList = carRelation.getQuery().find();
            for (int i = 0; i < parseCarList.size(); i++) {
                new Car(parseCarList.get(i)).save();
                downloadRecords(parseCarList.get(i));
                downloadStatistics(parseCarList.get(i).getString(ParseCar.STATISTIC_ID));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassCastException e){
            ParseUser.logOutInBackground();
            e.printStackTrace();
        }
    }

    public void downloadRecords(ParseCar car){
        try {
            ParseRelation<ParseRecord> recordRelation = car.getRelation(ParseCar.RECORDS_RELATION);
            List<ParseRecord> parseRecordList = recordRelation.getQuery().find();
            for (int i = 0; i < parseRecordList.size(); i++) {
                new Record(parseRecordList.get(i)).save();
                downloadJobs(parseRecordList.get(i));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public void downloadJobs(ParseRecord parseRecord){
        try {
            ParseRelation<ParseJob> jobRelation = parseRecord.getRelation(ParseRecord.JOBS_RELATION);
            List<ParseJob> parseJobList = jobRelation.getQuery().find();
            for (int i = 0; i < parseJobList.size(); i++) {
                new Job(parseJobList.get(i)).save();
                downloadDetails(parseJobList.get(i));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void downloadDetails(ParseJob parseJob){
        try {
            ParseRelation<ParseDetail> detailRelation = parseJob.getRelation(ParseJob.DETAILS_RELATION);
            List<ParseDetail> parseDetailList = detailRelation.getQuery().find();
            for (int i = 0; i < parseDetailList.size(); i++) {
                new Detail(parseDetailList.get(i)).save();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void downloadStatistics(String statisticId){
        try {
            ParseQuery<ParseStatistic> statisticParseQuery = ParseQuery.getQuery(ParseStatistic.class);
            ParseStatistic parseStatistic = statisticParseQuery.get(statisticId);
            new Statistic(parseStatistic).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public interface Callback {
        void doCallback();
    }
}
