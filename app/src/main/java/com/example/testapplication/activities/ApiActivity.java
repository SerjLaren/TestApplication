package com.example.testapplication.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.testapplication.R;
import com.example.testapplication.adapters.RecyclerApiAdapter;
import com.example.testapplication.fragments.ApiImageFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiActivity extends BaseActivity implements RecyclerApiAdapter.onRVItemClickListener, ApiImageFragment.ApiImageFragmentListener {

    private RecyclerView myRecyclerView;
    private RecyclerApiAdapter myAdapter;
    private ArrayList data;
    private RecyclerView.LayoutManager lm;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ApiImageFragment imageFrag;
    private String url, ans;
    private int boobsCount = 0, i, nLast = 0, nNext = 50;
    private boolean loadingImages = true;
    private JSONObject objJson;
    private RequestQueue queue;
    private String boobsImage;
    private String errorMsg, noConnectMsg;
    private Cache cache;
    private Network network;
    private Toast toast;
    private ProgressBar progressLoading, progressTop;
    private static final String POSITION = "position", BOOBS_COUNT = "boobsCount", COUNT = "count", PREVIEW = "preview", baseUrl = "http://api.oboobs.ru/boobs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        initValues();
        initViews();
        progressTop.setVisibility(View.VISIBLE);
        if (hasConnection(this)) {
            workWithRV();
        } else {
            toast = Toast.makeText(getApplicationContext(),
                    noConnectMsg, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void initViews() {
        myRecyclerView = (RecyclerView) findViewById(R.id.rv_oboobs);
        progressLoading = (ProgressBar) findViewById(R.id.rvProgressBar);
        progressLoading.setVisibility(View.INVISIBLE);
        progressTop = (ProgressBar) findViewById(R.id.topProgressBar);
        progressTop.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initValues() {
        errorMsg = getString(R.string.errorStr);
        noConnectMsg = getString(R.string.noConnectStr);
        url = "http://api.oboobs.ru/boobs/count/";  // не константа, т.к. url меняется с каждым запросом картинок
        data = new ArrayList();
        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
    }

    private void workWithRV() {
        queue = new RequestQueue(cache, network);
        queue.start();
        JsonArrayRequest jsObjReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray count) {
                ans = count.toString();
                try {
                    objJson = count.getJSONObject(0);
                    ans = objJson.getString(COUNT);
                    boobsCount = Integer.valueOf(ans);
                } catch (JSONException e) {
                    toast = Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast = Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            lm = new GridLayoutManager(this, 2);
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            lm = new GridLayoutManager(this, 4);
        myRecyclerView.setLayoutManager(lm);
        myAdapter = new RecyclerApiAdapter(this, data, myRecyclerView);
        myRecyclerView.setAdapter(myAdapter);
        myRecyclerView.setHasFixedSize(true);

        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {

                if (!rv.canScrollVertically(1)) {
                    if ((nNext + 50) > boobsCount) {  //почти конец списка, 50 картинок уже не загрузить, грузим оставшиеся
                        nNext += (boobsCount - nNext);
                        nLast = nNext;
                        loadingImages = false;
                    } else if (nNext == boobsCount) { // конец списка, ничего не грузим
                        loadingImages = false;
                    } else {                          // конец списка не близко, грузим 50 картинок
                        nLast += 50;
                        nNext += 50;
                    }
                    if (loadingImages) {
                        url = baseUrl + nLast + "/" + nNext + "/";  //грузим элементы с nLast по nNext с сайта
                        JsonArrayRequest jsNewImagesReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray imagesJson) {
                                try {
                                    for (i = 0; i < (nNext - nLast); i++) {
                                        progressLoading.setVisibility(View.VISIBLE);
                                        objJson = imagesJson.getJSONObject(i);
                                        boobsImage = objJson.getString(PREVIEW);
                                        data.add(boobsImage);
                                        myAdapter.notifyDataSetChanged();
                                    }
                                    progressLoading.setVisibility(View.INVISIBLE);
                                } catch (JSONException e) {
                                    toast = Toast.makeText(getApplicationContext(),
                                            errorMsg, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                toast = Toast.makeText(getApplicationContext(),
                                        error.toString(), Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                        queue.add(jsNewImagesReq);
                    }
                }
            }
        });

        url = baseUrl + nLast + "/" + nNext + "/";
        JsonArrayRequest jsImagesReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray imagesJson) {
                try {
                    for (i = 0; i < (nNext - nLast); i++) {
                        progressTop.setVisibility(View.VISIBLE);
                        objJson = imagesJson.getJSONObject(i);
                        boobsImage = objJson.getString(PREVIEW);
                        data.add(boobsImage);
                        myAdapter.notifyDataSetChanged();
                    }
                    progressTop.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    toast = Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast = Toast.makeText(getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
        queue.add(jsObjReq);
        queue.add(jsImagesReq);
    }

    @Override
    public void onItemClick(int position) {
        if (imageFrag == null) {
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_image, R.anim.slide_out);
            imageFrag = new ApiImageFragment();
            Bundle posBundle = new Bundle();
            posBundle.putString(POSITION, String.valueOf(data.get(position)));
            imageFrag.setArguments(posBundle);
            ft.add(R.id.fl_api_rv, imageFrag);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void fragmentImageDestroyed() {
        imageFrag = null;
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(BOOBS_COUNT, boobsCount);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boobsCount = savedInstanceState.getInt(BOOBS_COUNT);
    }
}
