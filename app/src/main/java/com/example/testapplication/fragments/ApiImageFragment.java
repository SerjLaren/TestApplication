package com.example.testapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.testapplication.R;
import com.example.testapplication.activities.StopwatchActivity;
import com.example.testapplication.helpers.MySingleton;
import com.example.testapplication.helpers.SharedPreferencesHelper;

public class ApiImageFragment extends Fragment implements View.OnClickListener {

    private Button btnSetBack;
    private NetworkImageView tvApiImage;
    private String position;
    private final static String url = "http://media.oboobs.ru/", POSITION = "position";
    private ImageLoader mImageLoader;
    private SharedPreferencesHelper prefs;
    private String backDone;
    private Toast toast;
    private String noConnectMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle posBundle = getArguments();
        position = posBundle.getString(POSITION);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_api_image, container, false);
        initValues();
        initViews(v);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApiImageFragmentListener listener = (ApiImageFragmentListener) getActivity();
        listener.fragmentImageDestroyed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSetBackImage:
                prefs.putSavedOboobs(url + position);
                toast = Toast.makeText(getActivity(),
                        backDone, Toast.LENGTH_SHORT);
                toast.show();
                Intent stopwatchActIntent = new Intent(getActivity(), StopwatchActivity.class);
                stopwatchActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(stopwatchActIntent);
                break;
        }
    }

    private void initViews(View v) {
        tvApiImage = (NetworkImageView) v.findViewById(R.id.tvApiImage);
        btnSetBack = (Button) v.findViewById(R.id.btnSetBackImage);
        btnSetBack.setOnClickListener(this);
        if (prefs.getSetBoobsBack()) {
            btnSetBack.setVisibility(View.VISIBLE);
        } else {
            btnSetBack.setVisibility(View.INVISIBLE);
        }
        if (hasConnection(getActivity())) {
            tvApiImage.setImageUrl(url + position, mImageLoader);
        } else {
            toast = Toast.makeText(getActivity(),
                    noConnectMsg, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void initValues() {
        mImageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        backDone = getString(R.string.oboobsBackDone);
        prefs = new SharedPreferencesHelper(getActivity());
        noConnectMsg = getString(R.string.noConnectStr);
    }

    public interface ApiImageFragmentListener {
        public void fragmentImageDestroyed();
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
}
