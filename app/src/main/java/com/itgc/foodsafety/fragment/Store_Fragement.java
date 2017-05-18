package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.adapter.StoreAdapter;
import com.itgc.foodsafety.dao.Stores;
import com.itgc.foodsafety.ui.ExpandableHeightListView;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by root on 20/11/15.
 */
public class Store_Fragement extends Fragment
{
    private Context context;
    private ExpandableHeightListView store_list;
    private Fragment fragment;
    private String store_name;
    private int store_id;
    private ArrayList<Stores> stores_list = new ArrayList<Stores>();
    private StoreAdapter storeAdapter;
    private int page = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.store_fragement, container, false);
        restoreToolbar();
        getData();
        setUpView(view);

        return view;
    }

    private void getData() {
        stores_list.clear();
        try {
            JSONArray array = new JSONArray(AppPrefrences.getStoreJson(context));
            for (int i = 0; i < array.length(); i++) {
                JSONObject storeobject = array.getJSONObject(i);
                Stores stores = new Stores();
                stores.setStore_id(storeobject.getInt("store_id"));
                stores.setStore_name(storeobject.getString("storeName"));
                stores.setStore_loc(storeobject.getString("region"));
                stores.setMerchant_id(storeobject.getString("merchantId"));
                stores_list.add(stores);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpView(View view) {
        store_list = (ExpandableHeightListView) view.findViewById(R.id.store_list);

        setAdapter();
    }

    private void setAdapter() {
        storeAdapter = new StoreAdapter(context, stores_list);
        store_list.setAdapter(storeAdapter);
    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.VISIBLE);
        Toolbar toolbar = MainActivity.mToolbar;
        toolbar.setTitle("Store");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitleTextAppearance(context, R.style.MyTitleTextApperance);
        toolbar.setNavigationIcon(R.mipmap.revealicon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AppUtils.hideKeyBoard(getActivity(), v);
                } catch (Exception e) {

                }
                if (FragmentDrawer.mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    FragmentDrawer.mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    FragmentDrawer.mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

}
