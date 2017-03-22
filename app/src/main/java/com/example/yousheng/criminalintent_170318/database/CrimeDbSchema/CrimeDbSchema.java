package com.example.yousheng.criminalintent_170318.database.CrimeDbSchema;

/**
 * Created by yousheng on 17/3/21.
 */

public class CrimeDbSchema {

    //此内部类的唯一用途是定义描述数据表元素的string常量
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT ="suspect";

        }
    }
}