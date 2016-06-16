package com.example.testapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.fragments.SavedTimersFragment;

import java.util.ArrayList;

public class RecyclerApiAdapter extends RecyclerView.Adapter<RecyclerApiAdapter.ViewHolder> {

    RecyclerView myRV;
    private ArrayList dataSet;
    public Context cont;
    private int position;
    SavedTimersFragment stf;

    private final View.OnClickListener mOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            position = myRV.getChildAdapterPosition(v);
            String item = String.valueOf(dataSet.get(position));
            onRVItemClickListener listener = (onRVItemClickListener) cont;
            listener.onItemClick(position);
        }
    };

    public interface onRVItemClickListener {
        public void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sTextView;

        public ViewHolder(View v) {
            super(v);
            sTextView = (TextView) v.findViewById(R.id.tv_api_item);
        }
    }

    public void updateRV(ArrayList data) {
        dataSet = data;
        notifyDataSetChanged();
    }

    // Конструктор
    public RecyclerApiAdapter(Context context, ArrayList dataSet, RecyclerView rv) {
        cont = context;
        this.dataSet = dataSet;
        myRV = rv;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerApiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_api_rv_item, parent, false);
        v.setOnClickListener(mOnclick);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.position = position;
        holder.sTextView.setText(dataSet.get(position).toString());

    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}