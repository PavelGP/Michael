package by.of.servicebook.myapplication.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import by.of.servicebook.myapplication.activities.DetailRecordActivity;
import by.of.servicebook.myapplication.activities.MainActivity;
import by.of.servicebook.myapplication.R;
import by.of.servicebook.myapplication.adapters.RecordsAdapter;
import by.of.servicebook.myapplication.db.models.Car;
import by.of.servicebook.myapplication.db.models.Record;
import by.of.servicebook.myapplication.db.DataProvider;
import by.of.servicebook.myapplication.utils.AppConst;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("ALL")
public class RecordsFragment extends Fragment {
    Activity activity;
    DataProvider provider = DataProvider.getInstance();
    RecordsAdapter adapter;
    SharedPreferences sharedPreferences;

    public static RecordsFragment newInstance () {
        return new RecordsFragment();
    }

    public RecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        ((MainActivity) activity).onSectionAttached(AppConst.FRAGMENT_RECORDS);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_records, container, false);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String carId = sharedPreferences.getString(AppConst.VEHICLE, "");
        if (!carId.equals("")) {
            Car car = provider.getCarById(carId);
            TextView tvCar = (TextView) rootView.findViewById(R.id.tvCar);
            tvCar.setText(car.make + " " + car.model);
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        List<Record> records = provider.getRecordsByCarId(carId);
        adapter = new RecordsAdapter(getActivity(), records);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recordId = ((Record)parent.getItemAtPosition(position)).key;
                DetailRecordActivity.launch(getActivity(), recordId);
            }
        });

        return rootView;

    }



}
