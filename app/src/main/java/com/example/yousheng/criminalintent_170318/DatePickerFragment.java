package com.example.yousheng.criminalintent_170318;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by yousheng on 17/3/20.
 */

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE="intent_with_date_from_pickerfragment_to_crimefragment";

    private DatePicker mDatePicker;

    //创建实现此类本身实例的方法，规定了需要传入一个date数据，之后将date放入argument中，实现数据到此实例的传输
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //初始化日历选择器的布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        //date数据经过calendar的转换才能得到年月日信息
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //获取选择器实例后，按照传入的date数据进行初始化
        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                //为dialog实现在标题栏和按钮之间传入view对象
                .setView(view)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year=mDatePicker.getYear();
                                int month=mDatePicker.getMonth();
                                int day=mDatePicker.getDayOfMonth();
                                //获取到要返回给crimefragment的日期数据
                                Date date=new GregorianCalendar(year,month,day).getTime();

                                //若有目标fragment，则用方法onActivityResult带着intent传输数据
                                if(getTargetFragment()!=null){
                                    Intent intent=new Intent();
                                    intent.putExtra(EXTRA_DATE,date);
                                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                                }
                            }
                        })
                .create();

    }
}
