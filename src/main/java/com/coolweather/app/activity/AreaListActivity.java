package com.coolweather.app.activity;

import android.support.v4.app.Fragment;

import com.coolweather.app.fragment.AreaListFragment;

/**
 * Created by christopher on 2016/5/31.
 */
public class AreaListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new AreaListFragment();
    }
}
