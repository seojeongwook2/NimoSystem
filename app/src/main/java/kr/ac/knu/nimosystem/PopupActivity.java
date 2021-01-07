package kr.ac.knu.nimosystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.ac.knu.nimosystem.Model.PhoneItem;

public class PopupActivity extends Activity {
    private LinearLayout add_layout;
    private ArrayList<PhoneItem> data;
    private ArrayList<TextView> textViewArrayList = new ArrayList<>();
    private ArrayList<EditText> editTextArrayList = new ArrayList<>();
    private Button send_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        init();
    }

    private void init() {
        add_layout = findViewById(R.id.add_layout);
        //데이터 가져오기
        Intent intent = getIntent();
        data = (ArrayList<PhoneItem>) intent.getSerializableExtra("data");
        cancel_button = findViewById(R.id.cancel_button);
        send_button = findViewById(R.id.send_button);

        for (int i = 0; i < data.size(); i++) {
            TextView tmpTextView = new TextView(this);
            textViewArrayList.add(i, tmpTextView);
            tmpTextView.setText("#" + (i + 1) + "/" + data.size() + "  " + data.get(i).getName() + "  :  " + data.get(i).getNumber());
            tmpTextView.setTextColor(Color.BLACK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tmpTextView.setTypeface(getResources().getFont(R.font.maplestorylight));
            }

            EditText tmpEditText = new EditText(this);
            tmpEditText.setHint("위 번호로 전송할 명령을 입력하세요.");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tmpEditText.setTypeface(getResources().getFont(R.font.maplestorylight));
            }

            editTextArrayList.add(i, tmpEditText);
            add_layout.addView(tmpTextView);
            add_layout.addView(tmpEditText);
        }
        button_setting();
    }

    private void button_setting() {
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PopupActivity.this);
                builder.setTitle("메시지 일괄 전송").setMessage("입력한 정보를 다시 한번 확인한 후 전송하세요. 정말 보내시겠습니까?");

                builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendFunction();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void sendFunction() {
        for (int i = 0; i < editTextArrayList.size(); i++) {
            String phoneNum = data.get(i).getNumber();
            String content = editTextArrayList.get(i).getText().toString();
            if (content.equals("")) continue;
            sendMessage(phoneNum, content);
        }
    }

    private void sendMessage(final String phoneNumber, final String body) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, body, null, null);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
