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

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d("adapter", "onCreate: ");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("adapter", "onResume: ");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d("adapter", "onRestart: ");
//    }
}
