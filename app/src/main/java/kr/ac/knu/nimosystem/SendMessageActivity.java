package kr.ac.knu.nimosystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessageActivity extends AppCompatActivity {
    private EditText phone_number;
    private EditText message;
    private Button send_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        init();
    }

    private void init() {
        phone_number = findViewById(R.id.phone_number);
        message = findViewById(R.id.message);
        send_button = findViewById(R.id.send_button);
        button_setting();
    }

    private void button_setting() {
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        final String phoneNumber = phone_number.getText().toString();
        final String body = message.getText().toString();

        try {
            /* 정상 동작하는 파트
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, body, null, null);
            return true;
             */

            AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this);
            builder.setTitle("메시지 전송").setMessage("정말 보내시겠습니까?");
            builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, body, null, null);
                    Toast.makeText(SendMessageActivity.this, "전송 완료", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch(Exception e) {
            Log.e("send message error", e.getMessage());
            Toast.makeText(SendMessageActivity.this, "전송 실패, 다시 시도 하세요.", Toast.LENGTH_LONG).show();
        }
    }
}