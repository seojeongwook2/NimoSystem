package kr.ac.knu.nimosystem;

import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import kr.ac.knu.nimosystem.Adapter.ManageAdapter;
import kr.ac.knu.nimosystem.Model.PhoneItem;

public class ManageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DbOpenHelper mDbOpenHelper;
    private EditText number, name;
    private Button insert_button;
    private Button confirm_button;
    private ArrayList<PhoneItem> list = new ArrayList<>();
    private ManageAdapter manageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        init_binding();
        showDatabase();
    }

    private void init_binding() {
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        insert_button = findViewById(R.id.insert_button);

        insert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_name = name.getText().toString();
                String str_number = number.getText().toString();
                list.add(new PhoneItem(str_number, str_name));
                manageAdapter.notifyItemInserted(list.size() - 1);
            }
        });

        confirm_button = findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ManageActivity.this);
                builder.setTitle("정보 수정").setMessage("정말 수정하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();

                        intent.putExtra("list", list);
                        setResult(RESULT_OK, intent);
                        modifyDb();

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

        final View external_layout = findViewById(R.id.external_layout);
        Snackbar.make(external_layout, "수정 후 확인 버튼을 누르셔야 완료됩니다", Snackbar.LENGTH_SHORT).show();
    }

    private void showDatabase() {

        Cursor isCursor = mDbOpenHelper.sortColumn_info();

        list.clear();
        while (isCursor.moveToNext()) {
            String tempName = isCursor.getString(isCursor.getColumnIndex("name"));
            String tempNumber = isCursor.getString(isCursor.getColumnIndex("number"));
            list.add(new PhoneItem(tempNumber, tempName));
        }

        manageAdapter = new ManageAdapter(getApplicationContext(), list);
        manageAdapter.setOnItemClickListener(new ManageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(PhoneItem deleteItem) {
                int pos = list.indexOf(deleteItem);
                list.remove(pos);
                manageAdapter.notifyItemRemoved(pos);
            }
        });
        recyclerView.setAdapter(manageAdapter);
    }


    private void modifyDb() {
        mDbOpenHelper.deleteAllColumns_info();
        for (PhoneItem item : list) {
            mDbOpenHelper.insertColumn_info(item.getName(), item.getNumber());
        }
    }
}