package by.of.servicebook.myapplication.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.of.servicebook.myapplication.activities.MainActivity;
import by.of.servicebook.myapplication.R;
import by.of.servicebook.myapplication.db.models.Car;
import by.of.servicebook.myapplication.db.models.Statistic;
import by.of.servicebook.myapplication.db.DataProvider;
import by.of.servicebook.myapplication.utils.AppConst;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {
    DataProvider provider = DataProvider.getInstance();

    public static StatisticFragment newInstance() {
        return  new StatisticFragment();
    }

    public StatisticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(AppConst.FRAGMENT_STATISTIC);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistic, container, false);

        //init car make and model
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String carId = sharedPreferences.getString(AppConst.VEHICLE, "");
        if (!carId.equals("")) {
            Car car = provider.getCarById(carId);
            TextView tvCar = (TextView) rootView.findViewById(R.id.tvCar);
            tvCar.setText(car.make + " " + car.model);

            initViews(rootView, car.key);
        }

        return rootView;
    }

    private void initViews(View v, String carId){
        Statistic stat = provider.getCarStatisticById(carId);
        if (stat == null) return;

        TextView tvCurrentMileage = (TextView) v.findViewById(R.id.tvCurrentMileage);
        TextView tvMileagePerYear = (TextView) v.findViewById(R.id.tvMileagePerYear);
        TextView tvMileagePerMonth = (TextView) v.findViewById(R.id.tvMileagePerMonth);
        TextView tvMileagePerDay = (TextView) v.findViewById(R.id.tvMileagePerDay);
        TextView tvAllExpenses = (TextView) v.findViewById(R.id.tvAllExpenses);
        TextView tvExpensesPerYear = (TextView) v.findViewById(R.id.tvExpensesPerYear);
        TextView tvExpensesPerMonth = (TextView) v.findViewById(R.id.tvExpensesPerMonth);
        TextView tvExpensesPerDay = (TextView) v.findViewById(R.id.tvExpensesPerDay);
        TextView tvExpensesPer100 = (TextView) v.findViewById(R.id.tvExpensesPer100);

        tvCurrentMileage.setText(String.format("%,d", stat.mileage) + " " + getString(R.string.km));
        tvMileagePerYear.setText(String.format("%,d", stat.yearlyMileage) + " " + getString(R.string.km));
        tvMileagePerMonth.setText(String.format("%,d", stat.monthlyMileage) + " " + getString(R.string.km));
        tvMileagePerDay.setText(String.format("%,d", stat.dailyMileage) + " " + getString(R.string.km));
        tvAllExpenses.setText(String.format("%,d", stat.allExp) + " " + getString(R.string.rubles));
        tvExpensesPerYear.setText(String.format("%,d", stat.yearlyExp) + " " + getString(R.string.rubles));
        tvExpensesPerMonth.setText(String.format("%,d", stat.monthlyExp) + " " + getString(R.string.rubles));
        tvExpensesPerDay.setText(String.format("%,d", stat.dailyExp) + " " + getString(R.string.rubles));
        tvExpensesPer100.setText(String.format("%,d", stat.expPer100) + " " + getString(R.string.rubles));
    }
}
