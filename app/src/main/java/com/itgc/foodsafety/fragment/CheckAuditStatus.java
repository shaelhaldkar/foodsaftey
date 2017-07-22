package com.itgc.foodsafety.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.dao.Answers;
import com.itgc.foodsafety.dao.StartAudit;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by root on 10/12/15.
 */
public class CheckAuditStatus extends Fragment implements View.OnClickListener {

    private Context ctx;
    private TextView audit_date, question;
    private RadioButton rdt_yes, rdt_no;
    private EditText edt_remark, edt_comment;
    private Button btn_submit;
    private Bundle b;
    private ArrayList<StartAudit> audit, getAudit;
    private ArrayList<Answers> answerses;
    private int Cat_id, Store_id, type;
    private String category, Store_name, formattedDate, store_loc;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = getArguments();
        audit = (ArrayList<StartAudit>) b.getSerializable("DAO");
        getAudit = new ArrayList<>();
        answerses = new ArrayList<>();
        Cat_id = b.getInt("Cat_id");
        category = b.getString("Cat_name");
        Store_id = b.getInt("Store_id");
        type = b.getInt("Type");
        Store_name = b.getString("Store_name");
        store_loc = b.getString("Store_region");
        formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm a").format(Calendar.getInstance().getTime());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        restoreToolbar();

        View view = inflater.inflate(R.layout.checkauditstatus, container, false);
        setUpView(view);

        return view;
    }

    private void setUpView(View view) {
        audit_date = (TextView) view.findViewById(R.id.audit_date);
        audit_date.setText(getResources().getText(R.string.audit_title) + " " + formattedDate);

        question = (TextView) view.findViewById(R.id.question);
        if (Cat_id == 3)
            question.setText("Do you have a " + category + " Section ?");
        else if (Cat_id == 6)
            question.setText("Are you dealing with " + category + " ?");
        else if (Cat_id == 5)
            question.setText("Do you have a dedicated " + category + " ?");
        else if (Cat_id == 1)
            question.setText("Do you have a " + category + " ?");
        else
            question.setText("Do you have a " + category + " Shop ?");

        rdt_yes = (RadioButton) view.findViewById(R.id.rdt_yes);
        rdt_no = (RadioButton) view.findViewById(R.id.rdt_no);

        edt_remark = (EditText) view.findViewById(R.id.edt_remark);
        edt_comment = (EditText) view.findViewById(R.id.edt_comment);

        btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                hideKeyboard(btn_submit);
                if (rdt_no.isChecked())
                {
                    insertSkip();
                } else if (rdt_yes.isChecked())
                {
                    auditing();
                }
                break;
        }
    }

    private void hideKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void auditing() {
        Fragment fragment = new AuditStartFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DAO", audit);
        bundle.putInt("Cat_id", Cat_id);
        bundle.putString("Cat_name", category);
        bundle.putInt("Store_id", Store_id);
        bundle.putString("Store_name", Store_name);
        bundle.putString("Store_region", store_loc);
        bundle.putInt("Type", type);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
                .addToBackStack("Audit").commit();
    }

    private void insertSkip() {
        Answers answers = new Answers();
        try {
            answers.setRemark(edt_remark.getText().toString());
            answers.setComment(edt_comment.getText().toString());
            answers.setCat_id(Cat_id);
            answers.setCat_skip("yes");
            answers.setQues_skip("no");
            answerses.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        answerses.add(0, answers);

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.ANSWER_id, Cat_id);
            cv.put(DBHelper.ANSWER_Cat_id, Cat_id);
            cv.put(DBHelper.ANSWER_Cat, category);
            cv.put(DBHelper.ANSWER_Store_id, Store_id);
            cv.put(DBHelper.ANSWER_Store, Store_name);
            cv.put(DBHelper.ANSWER_DateTime, formattedDate);
            cv.put(DBHelper.ANSWER_Status, "Skipped");
            cv.put(DBHelper.ANSWER_value, serializeObject(answerses));
            cv.put(DBHelper.ANSWER_Draft_value, serializeObject(getAudit));

            DbManager.getInstance().insertDetails(cv, DBHelper.ANSWER_Tbl_NAME);

            getFragmentManager().popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] serializeObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();

            byte[] buf = bos.toByteArray();

            return buf;
        } catch (IOException ioe) {
            Log.e("serializeObject", "error", ioe);

            return null;
        }
    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.VISIBLE);
        Toolbar toolbar = MainActivity.mToolbar;
        toolbar.setTitle(category);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitleTextAppearance(ctx, R.style.MyTitleTextApperance);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                getFragmentManager().popBackStack();
            }
        });
    }

}
