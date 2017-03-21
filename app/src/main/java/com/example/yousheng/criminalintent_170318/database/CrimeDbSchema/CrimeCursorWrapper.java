package com.example.yousheng.criminalintent_170318.database.CrimeDbSchema;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.yousheng.criminalintent_170318.Crime;

import java.util.Date;
import java.util.UUID;

/**
 * Created by yousheng on 17/3/21.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    //cursor封装进此类，调用此get方法可以将一行的数据转换成一个crime实例取出
    public Crime getCrime(){
        String uuidString=getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title=getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date=getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved=getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));

        Crime crime=new Crime(UUID.fromString(uuidString));
        crime.setmTitle(title);
        crime.setmDate(new Date(date));
        crime.setmSolved(isSolved==1);
        return crime;
    }
}
