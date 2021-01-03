package kr.ac.knu.nimosystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.knu.nimosystem.Adapter.ManageAdapter;
import kr.ac.knu.nimosystem.Adapter.PhoneAdapter;
import kr.ac.knu.nimosystem.Model.PhoneItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ControlActivity extends AppCompatActivity {
    private String login_type = "";
    private Button all_control_button;
    private Button next_page_button;
    private Button add_button,login_his_button;
    private RecyclerView recyclerView;
    private ArrayList<PhoneItem> list;
    private Context context;
    private DbOpenHelper mDbOpenHelper;

    /*
    로그인 기록 저장하는 것은 해당 기기에 로그인한 기록만 저장
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        init();
    }

    private void init() {
        context = this;

        mDbOpenHelper =  new DbOpenHelper(this);
        mDbOpenHelper.open();


        login_type = getIntent().getExtras().getString("LOGIN_TYPE");
        all_control_button = findViewById(R.id.all_control_button);
        next_page_button = findViewById(R.id.next_page_button);
        add_button = findViewById(R.id.add_button);
        login_his_button = findViewById(R.id.login_history_button);

        list = new ArrayList<>();

        if (login_type.equals("0")) {
            add_button.setVisibility(View.VISIBLE);
        } else {
            add_button.setVisibility(View.GONE);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ControlActivity.this, RecyclerView.HORIZONTAL, false));

        showDatabase();

        final PhoneAdapter adapter = new PhoneAdapter(ControlActivity.this, list);

        adapter.setSendInterface(new PhoneAdapter.SendInterface() {
            @Override
            public void onSignal(int position) {
                adapter.notifyItemChanged(position, "sms_receive");
            }
        });

        recyclerView.setAdapter(adapter);
        button_setting();
        Intent passedIntent = getIntent();
        processIntent(passedIntent);
    }

    private void button_setting() {
        all_control_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View verifiedView = layoutInflater.inflate(R.layout.dialog_all_send, null);

                Button cancel_button = verifiedView.findViewById(R.id.cancel_button);
                Button send_button = verifiedView.findViewById(R.id.send_button);
                final EditText send_body = verifiedView.findViewById(R.id.dialog_all_send_body);

                builder.setView(verifiedView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                send_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String send_content = send_body.getText().toString();
                        sendMessage(send_content);
                        alertDialog.dismiss();
                    }
                });
            }
        });

        next_page_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        login_his_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginHistoryActivity.class));
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ManageActivity.class));
            }
        });
    }

    private void processIntent(Intent intent){
        if(intent != null){
            String string = intent.getStringExtra("string");
            // Toast.makeText(ControlActivity.this, string, Toast.LENGTH_LONG).show();
            refresh_list();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);
    }

    private void sendMessage(final String body) {
        try {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ControlActivity.this);
            builder.setTitle("메시지 전송").setMessage("정말 보내시겠습니까?");
            builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SmsManager smsManager = SmsManager.getDefault();
                    for(int k = 0; k < list.size(); k++) {
                        smsManager.sendTextMessage(list.get(k).getNumber(), null, body, null, null);
                        Toast.makeText(ControlActivity.this, list.get(k).getName() + " 으로 전송 완료했습니다.", Toast.LENGTH_LONG).show();
                    }
                    refresh_list();
                }
            });

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            androidx.appcompat.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch(Exception e) {
            Log.e("send message error", e.getMessage());
            Toast.makeText(ControlActivity.this, "전송 실패, 다시 시도 하세요.", Toast.LENGTH_LONG).show();
        }
    }

    private void refresh_list() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showDatabase();

                final PhoneAdapter adapter = new PhoneAdapter(ControlActivity.this, list);

                adapter.setSendInterface(new PhoneAdapter.SendInterface() {
                    @Override
                    public void onSignal(int position) {
                        adapter.notifyItemChanged(position, "sms_receive");
                    }
                });

                recyclerView.setAdapter(adapter);
            }
        }, 1000);
    }


    private void showDatabase(){

        Cursor isCursor = mDbOpenHelper.sortColumn_info();
        list.clear();
        while(isCursor.moveToNext()){
            String tempId = isCursor.getString(isCursor.getColumnIndex("_id"));
            String tempName = isCursor.getString(isCursor.getColumnIndex("name"));
            String tempNumber =isCursor.getString(isCursor.getColumnIndex("number"));
            list.add(new PhoneItem(tempNumber,tempName));
        }
    }



}
