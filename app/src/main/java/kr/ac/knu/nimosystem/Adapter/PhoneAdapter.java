package kr.ac.knu.nimosystem.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.knu.nimosystem.Model.MessageItem;
import kr.ac.knu.nimosystem.Model.PhoneItem;
import kr.ac.knu.nimosystem.R;
import kr.ac.knu.nimosystem.ReadMessageActivity;
import kr.ac.knu.nimosystem.SendMessageActivity;

public class PhoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private PhoneVH external_vh[];
    private Context context;
    private ArrayList<PhoneItem> data;
    ArrayList<MessageItem> message_list = new ArrayList<>();
    private SendInterface sendInterface;


    public PhoneAdapter(Context context, ArrayList<PhoneItem> data) {
        this.context = context;
        this.data = data;
        external_vh = new PhoneVH[data.size() + 1];
    }

    public void setSendInterface(SendInterface sendInterface) {
        this.sendInterface = sendInterface;
    }

    public interface SendInterface {
        void onSignal(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_phone, parent, false);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.width = (parent.getWidth() / 2);
        view.setLayoutParams(layoutParams);

        PhoneVH vh = new PhoneVH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final PhoneVH vh = (PhoneVH) holder;
        final String PHONE_NUMBER = data.get(position).getNumber();

        vh.name.setText(data.get(position).getName());
        vh.number.setText(data.get(position).getNumber());

        readMessage(PHONE_NUMBER);

        for (int k = 0; k < 10; k++) {
            vh.message_log_layout[k].setVisibility(View.VISIBLE);
        }

        if (message_list.size() < 10) {
            for (int k = message_list.size(); k < 10; k++) {
                vh.message_log_layout[k].setVisibility(View.GONE);
            }
            for (int k = 0; k < message_list.size(); k++) {
                vh.message_log_content[k].setText(message_list.get(k).getBody());
                vh.message_log_time[k].setText(message_list.get(k).getTimestamp());
            }
        } else {
            for (int k = 0; k < 10; k++) {
                vh.message_log_content[k].setText(message_list.get(k).getBody());
                vh.message_log_time[k].setText(message_list.get(k).getTimestamp());
            }
        }
    }


    private class PhoneVH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView number;
        private EditText message_body;
        private Button send_button;
        private LinearLayout[] message_log_layout = new LinearLayout[10];
        private TextView[] message_log_content = new TextView[10];
        private TextView[] message_log_time = new TextView[10];

        public PhoneVH(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            message_body = itemView.findViewById(R.id.message_body);
            send_button = itemView.findViewById(R.id.send_button);

            message_log_layout[0] = itemView.findViewById(R.id.message_log_layout_0);
            message_log_content[0] = itemView.findViewById(R.id.message_log_content_0);
            message_log_time[0] = itemView.findViewById(R.id.message_log_time_0);

            message_log_layout[1] = itemView.findViewById(R.id.message_log_layout_1);
            message_log_content[1] = itemView.findViewById(R.id.message_log_content_1);
            message_log_time[1] = itemView.findViewById(R.id.message_log_time_1);

            message_log_layout[2] = itemView.findViewById(R.id.message_log_layout_2);
            message_log_content[2] = itemView.findViewById(R.id.message_log_content_2);
            message_log_time[2] = itemView.findViewById(R.id.message_log_time_2);

            message_log_layout[3] = itemView.findViewById(R.id.message_log_layout_3);
            message_log_content[3] = itemView.findViewById(R.id.message_log_content_3);
            message_log_time[3] = itemView.findViewById(R.id.message_log_time_3);

            message_log_layout[4] = itemView.findViewById(R.id.message_log_layout_4);
            message_log_content[4] = itemView.findViewById(R.id.message_log_content_4);
            message_log_time[4] = itemView.findViewById(R.id.message_log_time_4);

            message_log_layout[5] = itemView.findViewById(R.id.message_log_layout_5);
            message_log_content[5] = itemView.findViewById(R.id.message_log_content_5);
            message_log_time[5] = itemView.findViewById(R.id.message_log_time_5);

            message_log_layout[6] = itemView.findViewById(R.id.message_log_layout_6);
            message_log_content[6] = itemView.findViewById(R.id.message_log_content_6);
            message_log_time[6] = itemView.findViewById(R.id.message_log_time_6);

            message_log_layout[7] = itemView.findViewById(R.id.message_log_layout_7);
            message_log_content[7] = itemView.findViewById(R.id.message_log_content_7);
            message_log_time[7] = itemView.findViewById(R.id.message_log_time_7);

            message_log_layout[8] = itemView.findViewById(R.id.message_log_layout_8);
            message_log_content[8] = itemView.findViewById(R.id.message_log_content_8);
            message_log_time[8] = itemView.findViewById(R.id.message_log_time_8);

            message_log_layout[9] = itemView.findViewById(R.id.message_log_layout_9);
            message_log_content[9] = itemView.findViewById(R.id.message_log_content_9);
            message_log_time[9] = itemView.findViewById(R.id.message_log_time_9);

            send_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String content = message_body.getText().toString();
                    sendMessage(data.get(getAdapterPosition()).getNumber(), content, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    String type = (String) payload;

                    if (TextUtils.equals(type, "sms_receive") && holder instanceof PhoneVH) {
                        readMessage(data.get(position).getNumber());
                        final PhoneVH vh = (PhoneVH) holder;

                        for (int k = 0; k < 10; k++) {
                            vh.message_log_layout[k].setVisibility(View.VISIBLE);
                        }

                        if (message_list.size() < 10) {
                            for (int k = message_list.size(); k < 10; k++) {
                                vh.message_log_layout[k].setVisibility(View.GONE);
                            }
                            for (int k = 0; k < message_list.size(); k++) {
                                vh.message_log_content[k].setText(message_list.get(k).getBody());
                                vh.message_log_time[k].setText(message_list.get(k).getTimestamp());
                            }
                        } else {
                            for (int k = 0; k < 10; k++) {
                                vh.message_log_content[k].setText(message_list.get(k).getBody());
                                vh.message_log_time[k].setText(message_list.get(k).getTimestamp());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void sendMessage(final String phoneNumber, final String body, final int pos) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("메시지 전송").setMessage("정말 보내시겠습니까?");
            builder.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, body, null, null);
                    Toast.makeText(context, "전송 완료", Toast.LENGTH_LONG).show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendInterface.onSignal(pos);
                        }
                    }, 1000);
                }
            });

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            Log.e("send message error", e.getMessage());
            Toast.makeText(context, "전송 실패, 다시 시도 하세요.", Toast.LENGTH_LONG).show();
        }
    }

    private void readMessage(final String phone_n) {
        message_list.clear();

        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body"}, null, null, "date DESC");

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

            if (item.getAddress().equals(phone_n)) {
                message_list.add(item);
            }
        }
    }

    private String makeTimeStamp(long in) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(in);
    }
}
