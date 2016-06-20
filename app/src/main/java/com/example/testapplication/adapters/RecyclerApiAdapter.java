package com.example.testapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.testapplication.R;
import com.example.testapplication.fragments.SavedTimersFragment;
import com.example.testapplication.helpers.MySingleton;

import java.util.ArrayList;
import java.util.Objects;

public class RecyclerApiAdapter extends RecyclerView.Adapter<RecyclerApiAdapter.ViewHolder> {

    private RecyclerView myRV;
    private ArrayList dataSet;
    public Context cont;
    private int position;
    private static final String url = "http://media.oboobs.ru/";
    private ImageLoader mImageLoader;

    private final View.OnClickListener mOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            position = myRV.getChildAdapterPosition(v);
            onRVItemClickListener listener = (onRVItemClickListener) cont;
            listener.onItemClick(position);
        }
    };

    public interface onRVItemClickListener {
        public void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView rvImageView;

        public ViewHolder(View v) {
            super(v);
            rvImageView = (NetworkImageView) v.findViewById(R.id.tv_api_item);
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
        mImageLoader = MySingleton.getInstance(cont).getImageLoader();
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
        holder.rvImageView.setImageUrl(url + dataSet.get(position), mImageLoader);
    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}