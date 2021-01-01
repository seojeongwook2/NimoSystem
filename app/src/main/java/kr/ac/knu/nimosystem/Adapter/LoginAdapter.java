package kr.ac.knu.nimosystem.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.ac.knu.nimosystem.Model.LoginItem;
import kr.ac.knu.nimosystem.R;

public class LoginAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<LoginItem> list;

    public LoginAdapter(Context context, ArrayList<LoginItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_login_history,parent,false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryViewHolder historyViewHolder =(HistoryViewHolder)holder;
        historyViewHolder.time.setText(list.get(position).getTime());
        historyViewHolder.type.setText(list.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        TextView type;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            type = itemView.findViewById(R.id.type);
        }
    }
}
