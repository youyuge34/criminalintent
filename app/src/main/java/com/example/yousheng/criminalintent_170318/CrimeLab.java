package com.example.yousheng.criminalintent_170318;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yousheng on 17/3/19.
 */

//单例是特殊的java类，仅允许创建一个实例，应用活多久，单例活多久，是数据集中存储池
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Crime crime = new Crime();
//            crime.setmTitle("Crime #" + i);
//            crime.setmSolved(i % 2 == 0);
//            mCrimes.add(crime);
//        }
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public boolean deleteCrime(Crime crime) {
            return mCrimes.remove(crime);
    }

    public List<Crime> getmCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getmId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
