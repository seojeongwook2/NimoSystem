package kr.ac.knu.nimosystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.knu.nimosystem.Adapter.LoginAdapter;
import kr.ac.knu.nimosystem.Model.LoginItem;

public class LoginHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DbOpenHelper mDbOpenHelper;
    private ArrayList<LoginItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_history);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        showDatabase();
    }


    private void showDatabase() {
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        Cursor isCursor = mDbOpenHelper.sortColumn_login();
        list.clear();
        while (isCursor.moveToNext()) {
            String tempId = isCursor.getString(isCursor.getColumnIndex("_id"));
            String tempTime = isCursor.getString(isCursor.getColumnIndex("time"));
            tempTime = "로그인 시간 : " + tempTime;
            String tempType = isCursor.getString(isCursor.getColumnIndex("type"));

            if (tempType.equals("0")) {
                tempType = "로그인 타입 : 관리자";
            } else {
                tempType = "로그인 타입 : 사용자";
            }

            list.add(new LoginItem(tempTime, tempType));
        }
        recyclerView.setAdapter(new LoginAdapter(getApplicationContext(), list));
    }
}