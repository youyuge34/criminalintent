package com.example.yousheng.criminalintent_170318;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        mCrimeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        //详情页返回列表页后不会自己刷新，需要通知adapter进行刷新
//        mCrimeAdapter.notifyDataSetChanged();
        //优化：通知adpter刷新单个
        mCrimeAdapter.notifyItemChanged(mCrimeAdapter.po[0]);
    }

    //adapter是控制器对象，从模型层获取数据后，提供recycleview显示，桥梁作用
    //adapter负责：创建必要的viewholder，把每一个itemview的模型层数据绑定给viewholder
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        //po记录点击的cardview的位置，返回列表时只需刷新单个cardview无需刷新全部
        final Integer[] po = {new Integer(0)};
        private List<Crime> mCrimes;
        private int position;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public int getPosition() {
            return position;
        }

        @Override
        //执行不频繁，一旦创建了足够的视图，就会停止create，通过利用旧的viewholder节约时间和内存
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, null);
            final CrimeHolder holder = new CrimeHolder(view);
            holder.mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = holder.getAdapterPosition();
                    Crime crime = mCrimes.get(position);
                    crime.setmSolved(isChecked);
                }
            });

            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Crime crime = mCrimes.get(position);
                    //po记录点击的cardview的位置，返回列表时只需刷新单个cardview无需刷新全部
                    po[0] = position;
                    //将crime实例的id号传递到详情页crimeactivity中，调用活动的方法写在crimeactivity中，方便合作
                    Intent intent = CrimeActivity.newIntent(getActivity(), crime.getmId());
                    startActivity(intent);
                }
            });
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
        private CardView mCardView;

        public CrimeHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView;
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_textview);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_textview);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_checkbox);
        }


    }
}
