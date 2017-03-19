package com.example.yousheng.criminalintent_170318;

import android.support.v4.app.Fragment;

/**
 * Created by yousheng on 17/3/19.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
