package kr.ac.knu.nimosystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.knu.nimosystem.Model.MessageItem;
import kr.ac.knu.nimosystem.R;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<MessageItem> data;

    public MessageAdapter(Context context, ArrayList<MessageItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_message, parent, false);
        MessageVH vh = new MessageVH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final MessageItem item = data.get(position);
        final MessageVH viewHolder = (MessageVH) holder;

        viewHolder.time_stamp.setText("Time : " + item.getTimestamp());
        viewHolder.body.setText("Body : " + item.getBody());
    }

    private class MessageVH extends RecyclerView.ViewHolder {
        private TextView time_stamp;
        private TextView body;

        public MessageVH(@NonNull View itemView) {
            super(itemView);
            time_stamp = itemView.findViewById(R.id.time_stamp);
            body = itemView.findViewById(R.id.body);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
