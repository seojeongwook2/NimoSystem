package kr.ac.knu.nimosystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.ac.knu.nimosystem.Model.PhoneItem;

public class PopupActivity extends Activity {

    private LinearLayout add_layout;
    private ArrayList<TextView> textViewArrayList = new ArrayList<>();
    private ArrayList<EditText> editTextArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);


        add_layout = findViewById(R.id.add_layout);
        //데이터 가져오기
        Intent intent = getIntent();
        ArrayList<PhoneItem> data = (ArrayList<PhoneItem>)intent.getSerializableExtra("data");

        for(int i=0;i<data.size();i++){
            TextView tmpTextView = new TextView(this);
            textViewArrayList.add(i,tmpTextView);
            tmpTextView.setText(data.get(i).getNumber());
            EditText tmpEditText = new EditText(this);
            editTextArrayList.add(i,tmpEditText);
            add_layout.addView(tmpTextView);
            add_layout.addView(tmpEditText);
        }


    }

    void sendFunction(){

        for(int i=0;i<editTextArrayList.size();i++){
            String phoneNum = textViewArrayList.get(i).getText().toString();
            String content = editTextArrayList.get(i).getText().toString();
            if(content.equals(""))continue;
            sendMessage(phoneNum,content);
        }
    }

    private void sendMessage(final String phoneNumber,final String body) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, body, null, null);
        }catch (Exception e){
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //확인 버튼 클릭
    public void mOnSend(View v){
        sendFunction();
        finish();
    }

    public void mOnClose(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }


}
