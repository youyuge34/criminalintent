package com.example.yousheng.criminalintent_170318.database.CrimeDbSchema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yousheng.criminalintent_170318.database.CrimeDbSchema.CrimeDbSchema.CrimeTable;

/**
 * Created by yousheng on 17/3/21.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //尝试打开，若不存在则新建。首次新建时调用oncreate方法，然后保存版本号。
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + "," +
                CrimeTable.Cols.TITLE + ", " +
                CrimeTable.Cols.DATE + ", " +
                CrimeTable.Cols.SOLVED + ", "+
                CrimeTable.Cols.SUSPECT +")"
        );
    }

    //若已创建过，则检查版本号，若更高，则调用onUpdate方法升级。
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
