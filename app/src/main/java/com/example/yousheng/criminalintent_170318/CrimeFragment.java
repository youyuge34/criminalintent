package com.example.yousheng.criminalintent_170318;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

/**
 * Created by yousheng on 17/3/18.
 */

//fragment作为控制层，要将模型层crime和视图层联系起来
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private static final String ARG_CRIME_ID="argument_from_activity_to_fragment";
    private static final String DIALOG_DATE = "dialog_date";
    private static final int REQUEST_DATE = 0;

    //此方法用于在activity中调用，建立fragment实例，同时将argument传入。
    //因为fragment的setArgument方法必须在fragment创建后，添加给activity前完成。
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args=new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //fragment的oncreate方法是public的，活动的oncreate方法是protected的，因为托管fragment的activity必须调用他们
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //使用简单的方式获取到 crimelistfragment--->crimeactivity中的intent信息，即crime的id号
//        UUID crimeId= (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);

        //使用复杂的fragment的argument参数获取到id号，比起简单方式，相当于多了在crimeactivity里把id放进碎片里再启动碎片这一步
        //但是这样的话，此fragment就不用知道activity的细节，保持了复用性
        UUID crimeId= (UUID) getArguments().getSerializable(ARG_CRIME_ID);


        //根据id号识别出是哪个crime实例，获取到了之后就能更新此碎片中的信息了
        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);

    }

    @Nullable
    @Override
    //和activity不同，我们在此方法中实例化fragment视图的布局，然后将实例化的view返回给托管的activity
    //第二个参数为父视图，第三个参数告知布局生成器是否将生成的视图添加给父视图，因为我们将通过活动的代码方式动态添加，所以填false
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
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

        //初始化日期按钮，点击则启动DatePickerFragment
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getmDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                //把此碎片设置为datepicker碎片的目标碎片，为了方便返回date数据
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                //tag参数表示可唯一识别manger中的dialogfragment
                dialog.show(manager, DIALOG_DATE);
            }
        });

        //初始化checkbox，并监听勾选
        mSolvedCheckbox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.ismSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //将视图层的勾选情况记录进模型层
            mCrime.setmSolved(isChecked);
            }
        });

        //然后将实例化的view返回给托管的activity
        return view;
    }

    //更新后的数据仅仅写入了crime模型层实例，并未写入数据库，所以要在暂停时更新数据库
    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:

                //若成功删除此crime，则返回list页面，否则toast提示
                if(CrimeLab.get(getActivity()).deleteCrime(mCrime)){
                    getActivity().finish();
                }else Toast.makeText(getActivity(),"failed",Toast.LENGTH_SHORT);


        }
        return super.onOptionsItemSelected(item);
    }

    //datepicker碎片通过调用此碎片中的此方法回传日期数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DATE:
                if (resultCode == Activity.RESULT_OK) {
                    Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                    mCrime.setmDate(date);
                    mDateButton.setText(date.toString());
                }

        }
    }
}
