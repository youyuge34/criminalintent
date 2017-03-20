package com.example.yousheng.criminalintent_170318;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private TextView mTextViewIfEmpty;
    private boolean mSubtitleVisible;

    private static final String SAVED_SUBTITLE="subtitle";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让fragmentManger知道此fragment需要接受选项菜单方法回调
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, null);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mTextViewIfEmpty= (TextView) view.findViewById(R.id.text_if_empty);
        //layoutmanger负责在屏幕上定位列表项，还定义了屏幕滚动行为
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mCrimeRecyclerView.setLayoutManager(manager);

        //解决设备旋转后，显示的子标题消失的问题
        if(savedInstanceState!=null){
            mSubtitleVisible=savedInstanceState.getBoolean(SAVED_SUBTITLE);
        }

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

        //为了按back键以后立刻更新subtitle的值，而按向前按钮后activity会重oncreate，实例清空，所以这样没用
        updateSubtitle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list_menu, menu);

        MenuItem subtitleItem=menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getmId());
                startActivity(intent);
                return true;

            case R.id.menu_item_show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSubtitle() {
        int count= CrimeLab.get(getActivity()).getmCrimes().size();
        String subtitle=""+count+(count<=1?" crime":" crimes");
        if(!mSubtitleVisible){
            subtitle=null;
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        //详情页返回列表页后不会自己刷新，需要通知adapter进行刷新
//        mCrimeAdapter.notifyDataSetChanged();
        //优化：通知adpter刷新单个
//        Log.d("adapter", "onResume: "+mCrimeAdapter.po[0]);

        //若列表数据为空，则让一行提示文字显示在空白界面上
        if (CrimeLab.get(getActivity()).getmCrimes().isEmpty()) {
          mCrimeRecyclerView.setVisibility(View.INVISIBLE);
            mTextViewIfEmpty.setVisibility(View.VISIBLE);
        }else {
            mTextViewIfEmpty.setVisibility(View.INVISIBLE);
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
        }
        if (mCrimeAdapter != null) {
            mCrimeAdapter.notifyItemChanged(mCrimeAdapter.po[0]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存值，旋转设备后可以恢复
        outState.putBoolean(SAVED_SUBTITLE,mSubtitleVisible);
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
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getmId());
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
