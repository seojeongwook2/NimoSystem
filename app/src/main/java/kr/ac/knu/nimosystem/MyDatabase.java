package kr.ac.knu.nimosystem;

import android.provider.BaseColumns;

public class MyDatabase {

    //db 구조 create 해주는 class
    public static final class CreateDB implements BaseColumns{
        public static final String TYPE = "type";
        public static final String TIME = "time";
        public static final String NAME = "name";
        public static final String NUMBER  ="number";
        public static final String _TABLENAME0 = "usertable";
        public static final String _TABLENAME1 = "infotable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +TIME+" text not null , "
                +TYPE+" text not null);";

        public static final String _CREATE1="create table if not exists "+ _TABLENAME1+"("
                +_ID+" integer primary key autoincrement, "
                +NAME+" text not null , "
                +NUMBER+" text not null);";
    }


}
