package kr.ac.knu.nimosystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.knu.nimosystem.DbOpenHelper;
import kr.ac.knu.nimosystem.ManageActivity;
import kr.ac.knu.nimosystem.Model.PhoneItem;
import kr.ac.knu.nimosystem.R;

public class ManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<PhoneItem> list;
    private DbOpenHelper mDbOpenHelper;

    private OnDeleteClickListener mListener = null ;

    public interface OnDeleteClickListener {
        void onDeleteClick(PhoneItem deleteItem) ;
    }

    public void setOnItemClickListener(OnDeleteClickListener listener) {
        this.mListener = listener ;
    }

    public ManageAdapter(Context context, ArrayList<PhoneItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_manage, parent, false);
        ManagementViewHolder viewHolder = new ManagementViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ManagementViewHolder managementViewHolder = (ManagementViewHolder)holder;
        managementViewHolder.name.setText(list.get(position).getName()+"\n" + list.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ManagementViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        Button delete;

        public ManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);


           delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int position = getAdapterPosition();
                   if(getAdapterPosition() !=RecyclerView.NO_POSITION){
                       mListener.onDeleteClick(list.get(position));
                   }

               }
           });
        }
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
