package com.example.testapplication.adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testapplication.R;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList dataSet;
    public Context cont;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sTextView;

        public ViewHolder(View v) {
            super(v);
            sTextView = (TextView) v.findViewById(R.id.fs_lv_second);
        }
    }

    public void updateRV(ArrayList data) {
        dataSet = data;
        notifyDataSetChanged();
    }

    // Конструктор
    public RecyclerAdapter(Context context, ArrayList dataSet) {
        cont = context;
        this.dataSet = dataSet;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_seconds_lv_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.sTextView.setText(dataSet.get(position).toString());

    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
