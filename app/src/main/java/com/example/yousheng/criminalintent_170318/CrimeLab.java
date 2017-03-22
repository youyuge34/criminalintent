package com.example.yousheng.criminalintent_170318;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yousheng.criminalintent_170318.database.CrimeDbSchema.CrimeBaseHelper;
import com.example.yousheng.criminalintent_170318.database.CrimeDbSchema.CrimeCursorWrapper;
import com.example.yousheng.criminalintent_170318.database.CrimeDbSchema.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yousheng on 17/3/19.
 */

//单例是特殊的java类，仅允许创建一个实例，应用活多久，单例活多久，是数据集中存储池
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    //要和arraylist再见了，我们开始使用sqlite
//    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
//        mCrimes = new ArrayList<>();
        mContext = context.getApplicationContext();
        //创建crime数据库，helper要做如下工作：
        //尝试打开，若不存在则新建。首次新建时调用oncreate方法，然后保存版本号。
        //若已创建过，则检查版本号，若更高，则调用onUpdate方法升级。
        mDatabase = new CrimeBaseHelper(mContext, null, null, CrimeBaseHelper.VERSION).getWritableDatabase();
    }

    public void addCrime(Crime crime) {
//        mCrimes.add(crime);
        //使用getcontentvalue方法将模型crime的数据转换成可以写入数据库的数据
        ContentValues contentValues = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    public boolean deleteCrime(Crime crime) {
//            return mCrimes.remove(crime);
        int count=mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID+" = ? ",new String[]{crime.getmId().toString()});
        return (count!=0);
    }

    //需要查询数据库，将每一行转换成crime实例后存入list
    public List<Crime> getmCrimes() {
        List<Crime> list = new ArrayList<>();

        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, //代表查询所有列
                null, null, null, null, null
        );

        //此类中做了封装，能把游标指向的每一行转换成crime实例返回
        CrimeCursorWrapper cursorWrapper = new CrimeCursorWrapper(cursor);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                list.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    //列表碎片传递uuid给详情页，详情页通过uuid找到crime实例
    public Crime getCrime(UUID id) {
        Cursor cursor = mDatabase.query(CrimeTable.NAME, null, CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()},
                null, null, null);

        CrimeCursorWrapper cursorWrapper = new CrimeCursorWrapper(cursor);
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            //游标找到了crime实例所在行
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    //原本更新完数据后会自动写入模型层crime，而现在必须增加一个步骤：从模型层获取新数据，更新数据库
    public void updateCrime(Crime crime) {
        String uuidString = crime.getmId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }


    //写入和更新数据库需要辅助类contentValue，所以我们要把crime实例数据转换一下才能写入数据库
    //contentValue类键值对存放，且只能用于处理sqlite数据
    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getmId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getmTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getmDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.ismSolved() ? 1 : 0);
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getmSuspect());
        return contentValues;
    }

    //此方法不会创建任何文件，只是返回指向某个具体位置的File对象
    public File getPhotoFile(Crime crime){
        File fileDir=new File(mContext.getExternalCacheDir(),crime.getPhotoFilename());
        if(fileDir==null){
            return null;
        }
        try{
            if (fileDir.exists()){
                fileDir.delete();
            }
            fileDir.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileDir;
    }
}
