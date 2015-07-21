package by.of.servicebook.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import by.of.servicebook.myapplication.R;
import by.of.servicebook.myapplication.commands.SaveToParse;
import by.of.servicebook.myapplication.db.models.Car;
import by.of.servicebook.myapplication.db.DataProvider;

public class CarDetailsActivity extends ActionBarActivity{

    public static final String CAR_ID = "car_id";
    private final String DEFAULT_ID = "";
    private Car car;

    public static void launch(Context context, String carId){
        Intent intent = new Intent(context, CarDetailsActivity.class);
        intent.putExtra(CarDetailsActivity.CAR_ID, carId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        setToolbar();
        initData();

        initViews();
    }

    private void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initViews(){
        TextView tvMake = (TextView) findViewById(R.id.tvMake);
        TextView tvModel = (TextView) findViewById(R.id.tvModel);
        TextView tvYear = (TextView) findViewById(R.id.tvYear);
        TextView tvRegNumber = (TextView) findViewById(R.id.tvRegNumber);
        TextView tvVinCode = (TextView) findViewById(R.id.tvVinCode);
        TextView tvColor = (TextView) findViewById(R.id.tvColor);
        TextView tvRegDate = (TextView) findViewById(R.id.tvRegDate);
        TextView tvRegMileage = (TextView) findViewById(R.id.tvRegMileage);


        tvMake.setText(car.make);
        tvModel.setText(car.model);
        tvYear.setText("" + car.year);
        tvRegNumber.setText(car.regNumber);
        tvVinCode.setText(car.vinCode);
        tvColor.setText(car.color);
        tvRegDate.setText(car.regDate);
        tvRegMileage.setText(String.format("%,d", car.regMileage) + " " + getString(R.string.km));
    }

    private void initData(){
        String carId = DEFAULT_ID;
        if (getIntent() != null){
            carId = getIntent().getExtras().getString(CAR_ID, DEFAULT_ID);
        }
        car = DataProvider.getInstance().getCarById(carId);
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
}
