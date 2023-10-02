package com.vance.example.jank;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vance.example.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.MyViewHolder> {
    private SharedPreferences sp;
    private LayoutInflater layoutInflater;

    public LayoutAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        sp = context.getSharedPreferences("test", Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_adapter_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText("标题:" + position);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("position", position);
        edit.commit();
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
