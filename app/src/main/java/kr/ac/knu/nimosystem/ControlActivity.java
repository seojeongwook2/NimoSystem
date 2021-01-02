package kr.ac.knu.nimosystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.knu.nimosystem.Adapter.PhoneAdapter;
import kr.ac.knu.nimosystem.Model.PhoneItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ControlActivity extends AppCompatActivity {
    private String login_type = "";
    private Button all_control_button;
    private Button next_page_button;
    private Button add_button,login_his_button;
    private RecyclerView recyclerView;
    private ArrayList<PhoneItem> list;

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

        list.add(new PhoneItem("01062817950", "서정욱"));
        list.add(new PhoneItem("01037806514", "장진현"));
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
    }

    private void processIntent(Intent intent){
        if(intent != null){
            String string = intent.getStringExtra("string");
            // Toast.makeText(ControlActivity.this, string, Toast.LENGTH_LONG).show();

            list.clear();
            list.add(new PhoneItem("01062817950", "서정욱"));
            list.add(new PhoneItem("01037806514", "장진현"));

            final PhoneAdapter adapter = new PhoneAdapter(ControlActivity.this, list);

            adapter.setSendInterface(new PhoneAdapter.SendInterface() {
                @Override
                public void onSignal(int position) {
                    adapter.notifyItemChanged(position, "sms_receive");
                }
            });

            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);
    }
}