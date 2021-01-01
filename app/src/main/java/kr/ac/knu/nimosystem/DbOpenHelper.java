package kr.ac.knu.nimosystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
public class DbOpenHelper {

    private static final String DATABASE_NAME="InnerDatabase(SQLite).db";
    private static final int DATABAES_VERSION = 1;

    //내 sqlite db
    public static SQLiteDatabase mDB;
    //
    private DatabaseHelper dbHelper;

    private Context context;


    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        //데이터베이스의 테이블을 생성  테이블 구성 시 다른 테이블 명칭을 추가하여 작성하면 하나의 데이터베이스에서 여러 테이블도 생성
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(MyDatabase.CreateDB._CREATE0);
        }


        //버전 업그레이드 시 사용, 이전 버전을 지우고 새 버전을 생성
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyDatabase.CreateDB._TABLENAME0 );
            onCreate(sqLiteDatabase);
        }

    }


    public long insertColumns(String time,String type){
        ContentValues values = new ContentValues();
        values.put(MyDatabase.CreateDB.TIME,time);
        values.put(MyDatabase.CreateDB.TYPE, type);
        return mDB.insert(MyDatabase.CreateDB._TABLENAME0, null, values);

    }



    public Cursor selectColumns(){
        return mDB.query(MyDatabase.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }


    public DbOpenHelper(Context context) {
        this.context = context;
    }

    public DbOpenHelper open() throws SQLException {
        dbHelper = new DatabaseHelper(context,DATABASE_NAME,null,DATABAES_VERSION);
        mDB = dbHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        dbHelper.onCreate(mDB);
        /*
        String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +NAME+" text not null , "
                +NUMBER+" text not null);";
         */
    }

    public void close(){
        mDB.close();
    }


    public boolean updateColumn(long id,String time,String type){
        ContentValues values =new ContentValues();
        values.put(MyDatabase.CreateDB.TYPE,type);
        values.put(MyDatabase.CreateDB.TIME,time);
        return mDB.update(MyDatabase.CreateDB._TABLENAME0,values,"_id="+id,null)>0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(MyDatabase.CreateDB._TABLENAME0, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(MyDatabase.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }

    public Cursor sortColumn(){
        Cursor c = mDB.rawQuery( "SELECT * FROM usertable", null);
        return c;
    }

}
