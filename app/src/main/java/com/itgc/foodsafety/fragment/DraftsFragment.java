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

import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.adapter.DraftAdapter;
import com.itgc.foodsafety.dao.Answers;
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

    private void getData() {
        Cursor curs = null;
        String query = "";

        try {
            query = "SELECT * FROM answer where ans_status = 'Complete' OR ans_status = 'Incomplete' ";

            DbManager.getInstance().openDatabase();
            curs = DbManager.getInstance().getDetails(query);

            draftses = new ArrayList<>();
            while (curs != null && curs.moveToNext()) {
                Drafts drafts = new Drafts();
                drafts.setDraft_surveyid(curs.getInt(curs.getColumnIndex(DBHelper.ANSWER_id)));
                drafts.setDraft_storename(curs.getString(curs.getColumnIndex(DBHelper.ANSWER_Store)));
                drafts.setDraft_date(curs.getString(curs.getColumnIndex(DBHelper.ANSWER_DateTime)));
                drafts.setDraft_storeid(curs.getInt(curs.getColumnIndex(DBHelper.ANSWER_Store_id)));
                drafts.setDraft_storeregion(curs.getString(curs.getColumnIndex(DBHelper.ANSWER_Store_reg)));
                drafts.setDraft_cat(curs.getString(curs.getColumnIndex(DBHelper.ANSWER_Cat)));
                drafts.setDraft_catid(curs.getInt(curs.getColumnIndex(DBHelper.ANSWER_Cat_id)));
                drafts.setAnswerses((ArrayList<Answers>) deserializeObject(curs.getBlob(curs.getColumnIndex(
                        DBHelper.ANSWER_value))));
                if (curs.getBlob(curs.getColumnIndex(
                        DBHelper.ANSWER_Draft_value)) != null){
                    drafts.setStartAudits((ArrayList<StartAudit>) deserializeObject(curs.getBlob(curs.getColumnIndex(
                            DBHelper.ANSWER_Draft_value))));
                }
                else {
                    drafts.setStartAudits((ArrayList<StartAudit>) deserializeObject(curs.getBlob(curs.getColumnIndex(
                            DBHelper.ANSWER_value))));
                }

                drafts.setDraft_status(curs.getString(curs.getColumnIndex(DBHelper.ANSWER_Status)));
                drafts.setType(curs.getInt(curs.getColumnIndex(DBHelper.ANSWER_Type)));
                draftses.add(drafts);
            }
            setAdapter(draftses);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void setAdapter(final ArrayList<Drafts> draftses) {
        adapter = new DraftAdapter(draftses, context);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new DraftAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (!draftses.get(position).getDraft_status().equals("Complete")){
                    Fragment fragment = new AuditStartFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DAO", draftses.get(position).getStartAudits());
                    bundle.putSerializable("DAO_ANS", draftses.get(position).getAnswerses());
                    bundle.putInt("Cat_id", draftses.get(position).getDraft_catid());
                    bundle.putString("Cat_name", draftses.get(position).getDraft_cat());
                    bundle.putInt("Store_id", draftses.get(position).getDraft_storeid());
                    bundle.putString("Store_name", draftses.get(position).getDraft_storename());
                    bundle.putString("Store_region", draftses.get(position).getDraft_storeregion());
                    bundle.putInt("Type", draftses.get(position).getType());
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.container_body,fragment)
                            .addToBackStack("Audit").commit();
                }
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
