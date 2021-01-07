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

        ArrayList<String> button_name_arrayList = new ArrayList<>();
        button_name_arrayList.add(0,"PUMP1");
        button_name_arrayList.add(1,"PUMP2");
        button_name_arrayList.add(2,"PUMP3");
        button_name_arrayList.add(3,"가동중지");
        button_name_arrayList.add(4,"EOCR 리셋");



        for (int i = 0; i < data.size(); i++) {
            TextView tmpTextView = new TextView(this);
            textViewArrayList.add(i, tmpTextView);
            tmpTextView.setText("#" + (i + 1) + "/" + data.size() + "  " + data.get(i).getName() + "  :  " + data.get(i).getNumber());
            tmpTextView.setTextColor(Color.BLACK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tmpTextView.setTypeface(getResources().getFont(R.font.maplestorylight));
            }

            final EditText tmpEditText = new EditText(this);
            tmpEditText.setHint("위 번호로 전송할 명령을 입력하세요.");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tmpEditText.setTypeface(getResources().getFont(R.font.maplestorylight));
            }

            editTextArrayList.add(i, tmpEditText);


            // 안쪽 하나의 Vertical layout 각각에 번호 ,  Horizontal layout (버튼 5개) ,입력창이 배치됨
            LinearLayout inner_vertical_layout = new LinearLayout(getApplicationContext());

            inner_vertical_layout.setOrientation(LinearLayout.VERTICAL);


            // Horizontal layout 내부에 버튼 5개 들어감
            LinearLayout button_horizontal_layout = new LinearLayout(getApplicationContext());
            button_horizontal_layout.setOrientation(LinearLayout.HORIZONTAL);

            //버튼 5개 생성
            Button button_pump1 = new Button(getApplicationContext());
            Button button_pump2 = new Button(getApplicationContext());
            Button button_pump3 = new Button(getApplicationContext());
            Button button_stop = new Button(getApplicationContext());
            Button button_reset = new Button(getApplicationContext());

            button_pump1.setText("PUMP1");
            button_pump2.setText("PUMP2");
            button_pump3.setText("PUMP3");
            button_stop.setText("가동중지");
            button_reset.setText("EOCR 리셋");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                button_pump1.setTypeface(getResources().getFont(R.font.maplestorylight));
                button_pump2.setTypeface(getResources().getFont(R.font.maplestorylight));
                button_pump3.setTypeface(getResources().getFont(R.font.maplestorylight));
                button_stop.setTypeface(getResources().getFont(R.font.maplestorylight));
                button_reset.setTypeface(getResources().getFont(R.font.maplestorylight));
            }

            // horizontal layout에 버튼 추가
            button_horizontal_layout.addView(button_pump1);
            button_horizontal_layout.addView(button_pump2);
            button_horizontal_layout.addView(button_pump3);
            button_horizontal_layout.addView(button_stop);
            button_horizontal_layout.addView(button_reset);


            //가장 아래 레이아웃 구분을 위한 라인
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                                3
                            );

            params.setMargins(0,60,0,60);

            LinearLayout bottom_line = new LinearLayout(getApplicationContext());
            bottom_line.setBackgroundColor(Color.BLACK);
            if(i!=data.size()-1)
            bottom_line.setLayoutParams(params);

            //전체 vertical layout에 추가
            inner_vertical_layout.addView(tmpTextView);
            inner_vertical_layout.addView(button_horizontal_layout);
            inner_vertical_layout.addView(tmpEditText);
            inner_vertical_layout.addView(bottom_line);
            add_layout.addView(inner_vertical_layout);


            button_pump1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tmpEditText.setText("PUMP1");
                }
            });

            button_pump2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tmpEditText.setText("PUMP2");
                }
            });
            button_pump3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tmpEditText.setText("PUMP3");
                }
            });
            button_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tmpEditText.setText("0");
                }
            });
            button_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tmpEditText.setText("7");
                }
            });

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
