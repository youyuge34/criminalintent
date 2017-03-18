package com.example.yousheng.criminalintent_170318;

import java.util.UUID;

/**
 * Created by yousheng on 17/3/18.
 */

public class Crime {
    private UUID mId;
    private String mTitle;

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {

        this.mTitle = mTitle;
    }

    public Crime() {
        //生成唯一的ID号码
        mId=UUID.randomUUID();

    }
}
