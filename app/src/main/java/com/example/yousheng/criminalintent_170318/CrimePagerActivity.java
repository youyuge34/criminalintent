package com.example.yousheng.criminalintent_170318;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private static final String EXTRA_CRIME_ID="crime_from_crimelistfragment_to_crimeactivity";
    //用此方法启动此活动,intent中已接受到crime实例的id信息，从而可以知道是哪个crime，然而需要id信息的是crimefragment
    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeId= (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager= (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes=CrimeLab.get(this).getmCrimes();
        FragmentManager fragmentManager=getSupportFragmentManager();

        //设置viewpager的适配器，由于需要建立fragment所以需要传入manger
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            //将返回的fragment实例添加给托管的activity
            public Fragment getItem(int position) {
                Crime crime=mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getmId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //viewpager默认显示adapter中第一个列表项，所以要设置选中的列表项
        for(int i=0;i<mCrimes.size();i++){
            //循环检查crime的id，直到找到和点中列表项一样的id，设置它为viewpager的显示位置
            if(mCrimes.get(i).getmId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
