package kr.ac.knu.nimosystem;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.net.ssl.SSLEngineResult;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import kr.ac.knu.nimosystem.Model.MessageItem;
import kr.ac.knu.nimosystem.Model.PhoneItem;

import static kr.ac.knu.nimosystem.R.font.maplestorylight;

public class StatusPopupActivity extends Activity {
    private Context context;
    private LinearLayout add_layout;
    private ArrayList<PhoneItem> data;
    private Button back_button;
    private ArrayList<MessageItem> total_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_status_popup);
        init();
    }

    private void init() {
        context = this;
        add_layout = findViewById(R.id.add_layout);
        Intent intent = getIntent();
        data = (ArrayList<PhoneItem>) intent.getSerializableExtra("data");
        back_button = findViewById(R.id.back_button);
        total_message = new ArrayList<>();

        readMessage();
        first_background_setting();

        for (int i = 0; i < data.size(); i++) {
            LinearLayout temp_layout = new LinearLayout(StatusPopupActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 7, 0, 7);
            temp_layout.setOrientation(LinearLayout.HORIZONTAL);
            temp_layout.setLayoutParams(params);

            TextView temp_name = new TextView(StatusPopupActivity.this);
            temp_name.setText("#" + (i + 1) + "/" + data.size() + "  " + data.get(i).getName());
            LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
            temp_name.setLayoutParams(text_params);
            temp_name.setTextColor(Color.BLACK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                temp_name.setTypeface(getResources().getFont(maplestorylight));
            }

            TextView impeller_status = new TextView(StatusPopupActivity.this);
            impeller_status.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                impeller_status.setTypeface(getResources().getFont(maplestorylight));
            }

            if (check_impeller_rotate(data.get(i).getNumber())) {
                impeller_status.setText("작동 중");
                impeller_status.setTextColor(Color.RED);
                Animation animation = new AlphaAnimation(1, 0);
                animation.setDuration(300);
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.REVERSE);
                impeller_status.setAnimation(animation);
            } else {
                impeller_status.setText("중지");
                impeller_status.setTextColor(Color.BLACK);
                impeller_status.clearAnimation();
            }

            TextView level_status = new TextView(StatusPopupActivity.this);
            level_status.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                level_status.setTypeface(getResources().getFont(maplestorylight));
            }

            if (check_level_status(data.get(i).getNumber())) {
                level_status.setText("비정상");
                level_status.setTextColor(Color.RED);
                Animation animation = new AlphaAnimation(1, 0);
                animation.setDuration(300);
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.REVERSE);
                level_status.setAnimation(animation);
            } else {
                level_status.setText("정상");
                level_status.setTextColor(Color.BLACK);
                level_status.clearAnimation();
            }

            LinearLayout bottom_line = new LinearLayout(StatusPopupActivity.this);
            bottom_line.setBackgroundColor(Color.BLACK);
            bottom_line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));


            temp_layout.addView(temp_name);
            temp_layout.addView(impeller_status);
            temp_layout.addView(level_status);
            add_layout.addView(bottom_line);
            add_layout.addView(temp_layout);
        }

        button_setting();
    }

    private void first_background_setting() {
        LinearLayout first_layout = new LinearLayout(StatusPopupActivity.this);
        ;
        first_layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams first_layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        first_layout_params.setMargins(0, 20, 0, 10);
        first_layout.setLayoutParams(first_layout_params);

        TextView first_name_text = new TextView(this);
        TextView first_status_1 = new TextView(this);
        TextView first_status_2 = new TextView(this);
        first_name_text.setText("이름");
        first_name_text.setTextColor(Color.BLACK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            first_name_text.setTypeface(getResources().getFont(maplestorylight));
        }

        first_name_text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3));
        first_status_1.setText("가동 여부");
        first_status_1.setTextColor(Color.BLACK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            first_status_1.setTypeface(getResources().getFont(maplestorylight));
        }

        first_status_1.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        first_status_2.setText("수위 위험");
        first_status_2.setTextColor(Color.BLACK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            first_status_2.setTypeface(getResources().getFont(maplestorylight));
        }
        first_status_2.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        first_layout.addView(first_name_text);
        first_layout.addView(first_status_1);
        first_layout.addView(first_status_2);
        add_layout.addView(first_layout);
    }

    private void button_setting() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void readMessage() {
        total_message.clear();

        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"},
                null, null, "date DESC");

        while (c.moveToNext()) {
            MessageItem item = new MessageItem(
                    Long.toString(c.getLong(0)),
                    Long.toString(c.getLong(1)),
                    c.getString(2),
                    Long.toString(c.getLong(3)),
                    String.valueOf(c.getLong(3)),
                    makeTimeStamp(c.getLong(4)),
                    c.getString(5),
                    c.getString(6)
            );

            total_message.add(item);
        }
    }

    private String makeTimeStamp(long in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(in);
    }

    private boolean check_impeller_rotate(final String phone_n) {
        for (MessageItem item : total_message) {
            if (phone_n.equals(item.getAddress())) {
                if (item.getBody().contains("-ON-")) {
                    return true;
                } else if (item.getBody().contains("-OFF-")) {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean check_level_status(final String phone_n) {
        for (MessageItem item : total_message) {
            if (phone_n.equals(item.getAddress())) {
                if (item.getBody().equals("LEVEL\"ALARM\"")) {
                    return true;
                } else if (item.getBody().equals("LEVEL\"NORMAL\"")) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
