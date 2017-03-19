package com.example.yousheng.criminalintent_170318;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yousheng on 17/3/19.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, null);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);

        //layoutmanger负责在屏幕上定位列表项，还定义了屏幕滚动行为
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mCrimeRecyclerView.setLayoutManager(manager);

        //绑定适配器，更新UI
        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimelab = CrimeLab.get(getActivity());
        List<Crime> list = crimelab.getmCrimes();

        mCrimeAdapter = new CrimeAdapter(list);
        mCrimeRecyclerView.setAdapter(mCrimeAdapter);
    }


    //adapter是控制器对象，从模型层获取数据后，提供recycleview显示，桥梁作用
    //adapter负责：创建必要的viewholder，把每一个itemview的模型层数据绑定给viewholder
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        //执行不频繁，一旦创建了足够的视图，就会停止create，通过利用旧的viewholder节约时间和内存
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, null);
            CrimeHolder holder = new CrimeHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.mTitleTextView.setText(crime.getmTitle());
            holder.mDateTextView.setText(crime.getmDate().toString());
            holder.mSolvedCheckBox.setChecked(crime.ismSolved());
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    //viewholder只做一件事：容纳item view视图，构造方法中获取超类传入的itemview
    //viewholder引用着一个个的itemview
    private class CrimeHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_textview);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_textview);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_checkbox);
        }
    }
}
