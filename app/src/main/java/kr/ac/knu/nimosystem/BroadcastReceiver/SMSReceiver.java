package kr.ac.knu.nimosystem.BroadcastReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.ac.knu.nimosystem.ControlActivity;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if (messages.length > 0) {
            // 문자메세지에서 송신자와 관련된 내용을 뽑아낸다.
            String sender = messages[0].getOriginatingAddress();
            Log.d(TAG, "sender: " + sender);

            // 문자메세지 내용 추출
            String contents = messages[0].getMessageBody().toString();
            Log.d(TAG, "contents: " + contents);

            // 수신 날짜/시간 데이터 추출
            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.d(TAG, "received date: " + receivedDate);

            // 해당 내용을 모두 합쳐서 액티비티로 보낸다.
            sendToActivity(context, sender + '\n' + contents + '\n' + format.format(receivedDate));
        }
    }


    private void sendToActivity(Context context, String str) {
        Intent intent = new Intent(context, ControlActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("string", str);
        context.startActivity(intent);
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for (int i = 0; i < objs.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }

        return messages;
    }
}
