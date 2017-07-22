package com.itgc.foodsafety.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.adapter.DraftAdapter;
import com.itgc.foodsafety.dao.Answers;
import com.itgc.foodsafety.dao.DraftDetails;
import com.itgc.foodsafety.dao.Drafts;
import com.itgc.foodsafety.dao.StartAudit;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.ui.DividerItemDecoration;
import com.itgc.foodsafety.utils.AppUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by root on 9/10/15.
 */
public class DraftsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DraftAdapter adapter;
    private Context context;
    private ArrayList<Drafts> draftses;
    private ArrayList<DraftDetails> draftDetailses=new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.draftsfragment, container, false);
        setUpView(view);
        restoreToolbar();
        getData();
        return view;
    }

    private void getData()
    {
        draftDetailses.clear();
        String query = "SELECT CI.categoryId,CI.categoryName,CI.categoryType,CI.storeId,CI.categoryStatus,CI.categoryStartDate,SI.storeName,SI.storeMarchantId,SI.storeRegion FROM categoryInfo AS CI " +
                       "INNER JOIN storeInfo AS SI " +
                        "ON CI.storeId=SI.storeId " +
                        "WHERE CI.categoryStatus != 'NULL' ";

        DbManager.getInstance().openDatabase();
        Cursor draft = DbManager.getInstance().getDetails(query);

        Log.e("Draft Size", query +" " + draftDetailses.size()+"");

        if(draft.getCount()>0)
        {
            draft.moveToFirst();
            do {
                DraftDetails details=new DraftDetails();
                details.setStoreId(draft.getString(draft.getColumnIndex(DBHelper.STORE_ID)));
                details.setCategoryId(draft.getString(draft.getColumnIndex(DBHelper.CATEGORY_ID)));
                details.setStoreName(draft.getString(draft.getColumnIndex(DBHelper.STORE_NAME)));
                details.setCategoryName(draft.getString(draft.getColumnIndex(DBHelper.CATEGORY_NAME)));
                details.setStoreLocation(draft.getString(draft.getColumnIndex(DBHelper.STORE_REGION)));
                details.setCategoryType(draft.getInt(draft.getColumnIndex(DBHelper.CATEGORY_TYPE)));
                details.setCategoryStatus(draft.getString(draft.getColumnIndex(DBHelper.CATEGORY_STATUS)));
                details.setStatrDateTime(draft.getString(draft.getColumnIndex(DBHelper.CATEGORY_START_DATE)));
                draftDetailses.add(details);
            }while (draft.moveToNext());
        }
        setAdapter(draftDetailses);
        Log.e("Draft Size",draftDetailses.size()+"");
    }

    public static Object deserializeObject(byte[] b) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();

            return object;
        } catch (ClassNotFoundException cnfe) {
            Log.e("deserializeObject", "class not found error", cnfe);

            return null;
        } catch (IOException ioe) {
            Log.e("deserializeObject", "io error", ioe);

            return null;
        }

    }

    private void setAdapter(final ArrayList<DraftDetails> draftses)
    {
        adapter = new DraftAdapter(draftDetailses, context);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new DraftAdapter.MyClickListener()
        {
            @Override
            public void onItemClick(int position, View v)
            {
                if (!draftses.get(position).getCategoryStatus().equals("Complete"))
                {
                    Fragment fragment = new AuditStartFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Cat_id", Integer.parseInt(draftDetailses.get(position).getCategoryId()));
                    bundle.putString("Cat_name", draftDetailses.get(position).getCategoryName());
                    bundle.putInt("Store_id", Integer.parseInt(draftDetailses.get(position).getStoreId()));
                    bundle.putString("Store_name", draftDetailses.get(position).getStoreName());
                    bundle.putString("Store_region", draftDetailses.get(position).getStoreLocation());
                    bundle.putInt("Type", draftDetailses.get(position).getCategoryType());
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container_body,fragment).addToBackStack("Audit").commit();
                }
                //Toast.makeText(context, draftses.get(position).getCategoryStatus(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpView(View view) {
        AppUtils.isDraft = true;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.VISIBLE);
        Toolbar toolbar = MainActivity.mToolbar;
        toolbar.setTitle("Drafts");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitleTextAppearance(context, R.style.MyTitleTextApperance);
        toolbar.setNavigationIcon(R.mipmap.revealicon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FragmentDrawer.mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    FragmentDrawer.mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    FragmentDrawer.mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }
}
