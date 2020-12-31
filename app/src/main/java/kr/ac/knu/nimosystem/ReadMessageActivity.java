package kr.ac.knu.nimosystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.knu.nimosystem.Adapter.MessageAdapter;
import kr.ac.knu.nimosystem.Model.MessageItem;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReadMessageActivity extends AppCompatActivity {
    private EditText phone_number;
    private Button search_button;
    private RecyclerView recyclerView;
    private ArrayList<MessageItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        phone_number = findViewById(R.id.phone_number);
        search_button = findViewById(R.id.search_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReadMessageActivity.this, RecyclerView.VERTICAL, false));
        data = new ArrayList<>();
        button_setting();
    }

    private void button_setting() {
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_number = phone_number.getText().toString();
                readMessage(search_number);
            }
        });
    }

    private int readMessage(String number) {
        data.clear();
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(allMessage, new String[] { "_id", "thread_id", "address", "person", "date", "body" }, null, null, "date DESC");

        while (c.moveToNext()) {
            MessageItem item = new MessageItem(
                    Long.toString(c.getLong(0)),
                    Long.toString(c.getLong(1)),
                    c.getString(2),
                    Long.toString(c.getLong(3)),
                    String.valueOf(c.getLong(3)),
                    makeTimeStamp(c.getLong(4)),
                    c.getString(5)
            );

            if(item.getAddress().equals(number)) {
                data.add(item);
            }
        }

        MessageAdapter adapter = new MessageAdapter(ReadMessageActivity.this, data);
        recyclerView.setAdapter(adapter);
        return 0;
    }

    private String makeTimeStamp(long in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(in);
    }
}