package kr.ac.knu.nimosystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.ac.knu.nimosystem.Adapter.LoginAdapter;
import kr.ac.knu.nimosystem.Adapter.ManageAdapter;
import kr.ac.knu.nimosystem.Model.LoginItem;
import kr.ac.knu.nimosystem.Model.PhoneItem;

public class ManageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DbOpenHelper mDbOpenHelper;
    private EditText number,name;
    private Button insert;
    private ArrayList<PhoneItem> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        init_binding();

        showDatabase();

    }
    private void init_binding(){


        mDbOpenHelper =  new DbOpenHelper(this);
        mDbOpenHelper.open();


        recyclerView= findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        insert =findViewById(R.id.insert);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_name= name.getText().toString();
                String str_number = number.getText().toString();
                setManagementDB(str_name,str_number);
            }
        });

    }

    private void showDatabase(){


        Cursor isCursor = mDbOpenHelper.sortColumn_info();
        list.clear();
        while(isCursor.moveToNext()){
            String tempId = isCursor.getString(isCursor.getColumnIndex("_id"));
            String tempName = isCursor.getString(isCursor.getColumnIndex("name"));
            tempName = "이름:" + tempName;
            String tempNumber =isCursor.getString(isCursor.getColumnIndex("number"));
            tempNumber = "번호" + tempNumber;
            list.add(new PhoneItem(tempName,tempNumber,tempId));
        }
         recyclerView.setAdapter(new ManageAdapter(getApplicationContext(),list));
    }

    private void setManagementDB(String name,String number){
        mDbOpenHelper.insertColumn_info(name,number);
        showDatabase();
    }



}