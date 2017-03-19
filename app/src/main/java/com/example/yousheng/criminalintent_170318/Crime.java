package com.example.yousheng.criminalintent_170318;

import java.util.Date;
import java.util.UUID;

/**
 * Created by yousheng on 17/3/18.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    //crime时间,默认为当前日期
    private Date mDate;
    //crime是否已得到处理
    private boolean mSolved;

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {

        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Crime() {
        //生成唯一的ID号码
        mId=UUID.randomUUID();
        mDate=new Date();
    }
}
