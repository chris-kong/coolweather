package com.coolweather.app.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.OnItemClickListener;
import com.coolweather.app.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christopher on 2016/5/31.
 */
public class AreaListFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private RecyclerView areaRecyclerView;
    private AreaAdapter adapter;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coolWeatherDB = CoolWeatherDB.getInstance(getActivity());
        adapter = new AreaAdapter(dataList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }
                else if (currentLevel ==LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arealist,container,false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        areaRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        areaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        areaRecyclerView.setAdapter(adapter);
        areaRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        queryProvinces();
        return view;
    }

    private class AreaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tv_areaName;

        private String areaName;

        public AreaHolder(View itemView) {
            super(itemView);
            tv_areaName = (TextView) itemView.findViewById(R.id.list_item_areaName);
        }

        public void bindArea(String areaName){
            this.areaName = areaName;
            tv_areaName.setText(this.areaName);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class AreaAdapter extends RecyclerView.Adapter<AreaHolder>{

        private List<String> areaList;

        private OnItemClickListener clickListener;

        public AreaAdapter(List<String> dataList){
            this.areaList = dataList;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener)
        {
            this.clickListener = onItemClickListener;
        }

        @Override
        public AreaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_area,parent,false);
            return new AreaHolder(view);
        }

        @Override
        public void onBindViewHolder(final AreaHolder holder, int position) {
            String areaName = areaList.get(position);
            holder.bindArea(areaName);

            if (clickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        clickListener.onItemClick(holder.itemView,pos);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return areaList.size();
        }

        public void setArea(List<String> dataList){
            this.areaList = dataList;
        }
    }

    private class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable divider;

        public SimpleDividerItemDecoration(Context context){
            divider = context.getResources().getDrawable(R.drawable.line_divider,null);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    private void queryProvinces(){
        provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0){
            dataList.clear();
            for (Province p : provinceList){
                dataList.add(p.getProvinceName());
            }
            titleText.setText("中国");
            adapter.notifyDataSetChanged();
            currentLevel = LEVEL_PROVINCE;
        }
        else {
            queryFromServer(null,"province");
        }
    }

    private void queryCities(){
        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0){
            dataList.clear();
            for (City c : cityList){
                dataList.add(c.getCityName());
            }
            adapter.setArea(dataList);
            adapter.notifyDataSetChanged();
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }
        else {
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }

    private void queryCounties(){
        countyList = coolWeatherDB.loadCounties(selectedCity.getId());
        if (countyList.size() > 0){
            dataList.clear();
            for (County c : countyList){
                dataList.add(c.getCountyName());
            }
            adapter.setArea(dataList);
            adapter.notifyDataSetChanged();
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        }
        else {
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

    private void queryFromServer(final String code,final String type){
        String address;
        if (!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }
        else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }

//        showProgressDialog();

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

                boolean result = false;

                if ("province".equals(type)){
                    result = Utility.handleProvincesResponse(coolWeatherDB,response);
                }
                else if ("city".equals(type)){
                    result = Utility.handleCitiesResponse(coolWeatherDB,response,selectedProvince.getId());
                }
                else if ("county".equals(type)){
                    result = Utility.handleCountiesResponse(coolWeatherDB,response,selectedCity.getId());
                }

                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }
                            else if ("city".equals(type)){
                                queryCities();
                            }
                            else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        Toast.makeText(getActivity(),"数据加载失败",Toast.LENGTH_SHORT);
//                    }
//                });
            }
        });
    }

//    private void updateUI(List<String> dataList){
//        adapter.setArea(dataList);
//        areaRecyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("数据加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
