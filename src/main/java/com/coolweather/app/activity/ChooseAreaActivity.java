package com.coolweather.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christopher on 2016/5/30.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private RecyclerView recyclerView;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        titleText = (TextView) findViewById(R.id.title_text);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
    }
}
