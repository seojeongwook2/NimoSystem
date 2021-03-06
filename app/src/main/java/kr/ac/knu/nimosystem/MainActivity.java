package kr.ac.knu.nimosystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    private EditText login_code;
    private Button login_button;
    private DbOpenHelper mDbOpenHelper;
    public static final int CHANGE_REQUEST = 892;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check_permission();
        init();
    }

    private void init() {
        login_code = findViewById(R.id.login_code);
        login_button = findViewById(R.id.login_button);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        // showDatabase();
        button_setting();
    }

    private void button_setting() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_code = login_code.getText().toString();

                if (get_code.equals(getResources().getString(R.string.management_code))) {
                    // login code 에서 management code 와 일치 시
                    Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                    intent.putExtra("LOGIN_TYPE", "0");
                    startActivity(intent);
                    setLogin_history(makeTimeStamp(System.currentTimeMillis()), "0");
                } else if (get_code.equals(getResources().getString(R.string.user_1_code))) {
                    // 1번 user
                    Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                    intent.putExtra("LOGIN_TYPE", "1");
                    startActivity(intent);
                    setLogin_history(makeTimeStamp(System.currentTimeMillis()), "1");
                } else if (get_code.equals(getResources().getString(R.string.user_2_code))) {
                    // 2번 user
                    Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                    intent.putExtra("LOGIN_TYPE", "2");
                    startActivity(intent);
                    setLogin_history(makeTimeStamp(System.currentTimeMillis()), "2");
                } else if (get_code.equals(getResources().getString(R.string.user_3_code))) {
                    // 3번 user
                    Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                    intent.putExtra("LOGIN_TYPE", "3");
                    startActivity(intent);
                    setLogin_history(makeTimeStamp(System.currentTimeMillis()), "3");
                } else {
                    final View external_layout = findViewById(R.id.external_layout);
                    final Snackbar snackbar = Snackbar.make(external_layout, "로그인 코드와 일치하지 않습니다.",
                            Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                    return;
                }
            }
        });
    }

    private void check_permission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 1001);
        }
    }


    private void showDatabase() {
        Cursor isCursor = mDbOpenHelper.sortColumn_login();

        while (isCursor.moveToNext()) {
            String tempId = isCursor.getString(isCursor.getColumnIndex("_id"));
            String tempTime = isCursor.getString(isCursor.getColumnIndex("time"));
            String tempType = isCursor.getString(isCursor.getColumnIndex("type"));
            Log.e("MainActivity", tempTime);
        }

    }


    private void setLogin_history(String time, String type) {
        mDbOpenHelper.open();
        mDbOpenHelper.insertColumn_login(time, type);
        // showDatabase();
    }


    private String makeTimeStamp(long in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(in);
    }
}