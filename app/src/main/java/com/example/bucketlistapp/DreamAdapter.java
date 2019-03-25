package com.example.bucketlistapp;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class DreamAdapter extends RecyclerView.Adapter<DreamAdapter.ViewHolder> {

    private List<Dream> dreamList;
    private CheckButtonListener checkButtonListener;

    public DreamAdapter(List<Dream> dreamList, CheckButtonListener cbl){
        this.checkButtonListener = cbl;
        this.dreamList = dreamList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view  = inflater.inflate(R.layout.content_add, viewGroup, false);
        DreamAdapter.ViewHolder viewHolder = new DreamAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Dream dream = dreamList.get(i);
        viewHolder.textView_descr.setText(dream.getDescription());
        viewHolder.textView_name.setText(dream.getTitle());
        viewHolder.checkBox.setChecked(dream.isChecked());
        setTextDeco(dream.isChecked(), viewHolder);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dreamList.get(viewHolder.getAdapterPosition()).setChecked(isChecked);
                setTextDeco(isChecked, viewHolder);
                checkButtonListener.onCheckClick(dreamList.get(viewHolder.getAdapterPosition()));
            }
        });
    }

    public interface CheckButtonListener {
        void onCheckClick(Dream dream);
    }

    @Override
    public int getItemCount() {
        return dreamList.size();
    }

    private void setTextDeco(boolean isChecked, ViewHolder holder) {
        if(isChecked) {
            holder.textView_descr.setPaintFlags(holder.textView_descr.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView_name.setPaintFlags(holder.textView_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textView_descr.setPaintFlags(holder.textView_descr.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView_name.setPaintFlags(holder.textView_name.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_descr;
        TextView textView_name;
        CheckBox checkBox;

        public ViewHolder(View view){
            super(view);
            textView_descr = view.findViewById(R.id.textView_descr);
            textView_name = view.findViewById(R.id.textView_title);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }
}