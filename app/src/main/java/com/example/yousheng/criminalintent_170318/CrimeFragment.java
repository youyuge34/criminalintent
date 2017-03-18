package com.example.yousheng.criminalintent_170318;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by yousheng on 17/3/18.
 */

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;

    //fragment的oncreate方法是public的，活动的oncreate方法是protected的，因为托管fragment的activity必须调用他们
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }

    @Nullable
    @Override
    //和activity不同，我们在此方法中实例化fragment视图的布局，然后将实例化的view返回给托管的activity
    //第二个参数为父视图，第三个参数告知布局生成器是否将生成的视图添加给父视图，因为我们将通过活动的代码方式动态添加，所以填false
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

}
