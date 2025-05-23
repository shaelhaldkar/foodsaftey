package com.itgc.foodsafety.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.adapter.AuditStartAdapter;
import com.itgc.foodsafety.adapter.SampleAuditAdapter;
import com.itgc.foodsafety.dao.Answers;
import com.itgc.foodsafety.dao.CategoryQuestions;
import com.itgc.foodsafety.dao.SampleDetails;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.ui.MYLinearLayoutManager;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.ImageUtils;
import com.itgc.foodsafety.utils.Vars;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.canhub.cropper.*;
/**
 * Created by root on 10/10/15.
 */
public class AuditStartFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = AuditStartFragment.class.getCanonicalName();
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<ArrayList<Answers>> lists;
    private double latitude, longitude;
    private Location mLastLocation;
    private Context ctx;
    private View view;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Bundle b;
    private String category, formattedDate, Store_name, skip = "no", store_loc;
    private TextView audit_date, question, max_num, txt_title, txt_subcat, discription_txt, read_more,tvNext;
    private RadioButton rg_yes, rg_no, rg_partial, rdt_isfail;
    private AutoCompleteTextView edt_remark, edt_comment;
    private LinearLayout btn_previous, btn_next;
    private ImageView btn_camera, img_back, img_info, img_info1;
    private int count = 1;
    private AuditStartAdapter startAdapter;
    int TAKE_PHOTO_CODE = 0;
    public static int GALLERY_IMG = 3;
    private Spinner list_sampleno;
    private DBHelper dbHelper;
    private int Cat_id, Store_id, sample_selected = 1, type;
    private String encodedImage = "";
    private boolean isClicked = true;
    private RadioGroup rg1;
    private LinearLayout lin_audit;
    private CheckBox txt_skip;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private MYLinearLayoutManager mLayoutManager;
    private Boolean isImg = false;
    private int audit_id = 0;
    private String Previous_data = "", is_failed = "0";
    private int Image_count = 0;
    private EditText actions;
    private AlertDialog d;
    private static final int PICK_IMAGE_REQUEST_PERMISSION = 1;
    //private Uri fileUri;
    private String picturePath;
    private File photoFile;
    public static int LOAD_CAMERA_RESULTS = 11;
    private ArrayList<String> imagesarray;


    private ArrayList<CategoryQuestions> categoryQuestionsArrayList = new ArrayList<>();
    private int questionPos = 0;
    private int questionSubCatId = 0;
    private int questionID = 0;
    private int maxSample = 0;
    private SampleAuditAdapter sampleAuditAdapter;
    private ArrayList<SampleDetails> samples = new ArrayList<>();
    boolean sampleBind = false;
    private ActivityResultLauncher<CropImageContractOptions> cropImageLauncher;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cropImageLauncher = registerForActivityResult(
                new CropImageContract(),
                result -> {
                    if (result.isSuccessful()) {
                        Uri croppedImageUri = result.getUriContent();
                        String croppedImageFilePath = result.getUriFilePath(getContext(),true);
                       // imageView.setImageURI(croppedImageUri);
                        saveImageS(croppedImageFilePath);
                        Log.i(TAG, "onIageget1 "+croppedImageUri);
                    } else {
                        Exception error = result.getError();
                        if (error != null) {
                            Toast.makeText(getContext(), "Crop failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        b = getArguments();
        Cat_id = b.getInt("Cat_id");
        category = b.getString("Cat_name");
        Store_id = b.getInt("Store_id");
        store_loc = b.getString("Store_region");
        Store_name = b.getString("Store_name");
        formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm a").format(Calendar.getInstance().getTime());
        dbHelper = new DBHelper(ctx, "FoodSafety.db");
        dbHelper.openDataBase();
        DbManager.initializeInstance(dbHelper, ctx);
        DbManager.getInstance().openDatabase();
    }

    private void saveImageS(String croppedImageUri) {
        Log.i(TAG, "saveImageS: "+croppedImageUri);
        try {
            File targetFile = ImageUtils.createImageFile(
                    ctx,
                    AppPrefrences.getMerchatId(ctx),
                    AppPrefrences.getAuditCODE(ctx),
                    String.valueOf(Store_id),
                    String.valueOf(questionID),
                    String.valueOf(Cat_id),
                    String.valueOf(questionSubCatId)

            );

            saveImageLocal(Uri.parse(croppedImageUri), targetFile);
            Log.i(TAG, "onIageget2 saveImageLocal");

        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "onIageget3 "+e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DbManager.getInstance().closeDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "onCreateView: $++AuditStartFragment");

        view = inflater.inflate(R.layout.startauditfragment, container, false);

        setUpView(view);
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }
        getCategoryQuestion();
        restoreToolbar();
        setStartTime();

        return view;
    }

    private void setStartTime() {
        String checkStartDateTime = "SELECT " + DBHelper.DATE_TIME + " FROM " + DBHelper.STORE_START_TIME_TABLE + " WHERE " + DBHelper.STORE_ID + "=" + Store_id;
        Cursor checkStartDate = DbManager.getInstance().getDetails(checkStartDateTime);
        Log.e("StoreDateTime", checkStartDate.getCount() + "");
        if (checkStartDate.getCount() <= 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.DATE_TIME, getDateTime());
            contentValues.put(DBHelper.STORE_ID, Store_id);
            DbManager.getInstance().insertDetails(contentValues, DBHelper.STORE_START_TIME_TABLE);
        }
    }


    private void setUpView(View view) {
        rdt_isfail = (RadioButton) view.findViewById(R.id.rdt_isfail);
        txt_skip = (CheckBox) view.findViewById(R.id.txt_skip);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        question = (TextView) view.findViewById(R.id.question);
        max_num = (TextView) view.findViewById(R.id.max_num);
        audit_date = (TextView) view.findViewById(R.id.audit_date);
        txt_subcat = (TextView) view.findViewById(R.id.txt_subcat);
        tvNext = (TextView) view.findViewById(R.id.tvNext);

        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm a").format(Calendar.getInstance().getTime());
        audit_date.setText(getResources().getText(R.string.audit_title) + " " + formattedDate);

        btn_previous = (LinearLayout) view.findViewById(R.id.btn_previous);
        btn_next = (LinearLayout) view.findViewById(R.id.btn_next);

        btn_camera = (ImageView) view.findViewById(R.id.btn_camera);
        img_back = (ImageView) view.findViewById(R.id.img_back);
        img_info = (ImageView) view.findViewById(R.id.img_info);
        img_info1 = (ImageView) view.findViewById(R.id.img_info1);

        rg_yes = (RadioButton) view.findViewById(R.id.rdt_yes);
        rg_no = (RadioButton) view.findViewById(R.id.rdt_no);
        rg_partial = (RadioButton) view.findViewById(R.id.rdt_partial);
        actions = (EditText) view.findViewById(R.id.actions);

        lin_audit = (LinearLayout) view.findViewById(R.id.lin_audit);

        discription_txt = (TextView) view.findViewById(R.id.discription_txt);
        read_more = (TextView) view.findViewById(R.id.read_more);

        read_more.setPaintFlags(read_more.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        rg1 = (RadioGroup) view.findViewById(R.id.rg1);
//        if (type == 0) {
//            rg1.setVisibility(View.GONE);
//            lin_audit.setVisibility(View.GONE);
//        } else if (type == 1) {
//            rg1.setVisibility(View.VISIBLE);
//            lin_audit.setVisibility(View.VISIBLE);
//        }

        edt_comment = (AutoCompleteTextView) view.findViewById(R.id.edt_comment);
        edt_remark = (AutoCompleteTextView) view.findViewById(R.id.edt_remark);
        ArrayAdapter<String> remarks_data = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, Vars.remarks);
        edt_remark.setAdapter(remarks_data);

        ArrayAdapter<String> comment_data = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, Vars.comments);
        edt_comment.setAdapter(comment_data);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_sample);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new MYLinearLayoutManager(ctx);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        list_sampleno = (Spinner) view.findViewById(R.id.list_sampleno);

        list_sampleno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int i = Integer.valueOf(list_sampleno.getItemAtPosition(position).toString());
                sample_selected = i;
                max_num.setText(String.valueOf(i * 10));
                setAdapter(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        btn_previous.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        img_back.setOnClickListener(this);
        img_info.setOnClickListener(this);
        img_info1.setOnClickListener(this);

        rg_yes.setOnCheckedChangeListener(this);
        rg_no.setOnCheckedChangeListener(this);
        rg_partial.setOnCheckedChangeListener(this);
        txt_skip.setOnClickListener(this);
        read_more.setOnClickListener(this);
        rdt_isfail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_failed.equalsIgnoreCase("0")) {
                    is_failed = "1";
                    rdt_isfail.setChecked(true);
                } else {
                    is_failed = "0";
                    rdt_isfail.setChecked(false);
                }
            }
        });

        //TO DO for Remove
        AppPrefrences.setStartTime(ctx, getDateTime());
        btn_previous.setVisibility(View.INVISIBLE);

        sampleAuditAdapter = new SampleAuditAdapter(samples, ctx, Store_id, Cat_id, questionID, AuditStartFragment.this);
        mRecyclerView.setAdapter(sampleAuditAdapter);
    }

    private void setSampleSpinnerData(int count) {
        AppPrefrences.setStartTime(ctx, getDateTime());
        list_sampleno.setAdapter(new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, getSampleCount(count)));
    }

    private ArrayList<Integer> getSampleCount(int sample_no) {
        ArrayList<Integer> integers = new ArrayList<>();
        if (sample_no > 0) {
            for (int i = 1; i < sample_no + 1; i++) {
                integers.add(i);
            }
        } else {
            integers.add(1);
        }
        return integers;
    }

    private void setAdapter(int size) {
        if (size > samples.size()) {
            int newItem = size - samples.size();
            for (int i = 0; i < newItem; i++) {
                SampleDetails d = new SampleDetails();
                d.setClicked(true);
                d.setRateX(0);
                d.setSampleCount((samples.size() + i + 1));
                d.setSampleCurrentRate(0);
                d.setSamplePos((samples.size() + i + 1));
                d.setSampleRate(0);
                d.setProduct_name("");
                d.setMfdpkd("");
                d.setMfd_date("");
                d.setShellife_value(0);
                d.setClicked(false);
                d.setBrand_name("");
                d.setNo_sample_product("");
                d.setBb_exp("");
                d.setBb_exp_date("");


                samples.add(d);
            }
            sampleAuditAdapter.notifyDataSetChanged();
        } else if (size < samples.size()) {
            samples.clear();
            for (int i = 0; i < size; i++) {
                SampleDetails d = new SampleDetails();
                d.setClicked(true);
                d.setRateX(0);
                d.setSampleCount((samples.size() + i + 1));
                d.setSampleCurrentRate(0);
                d.setSamplePos((i));
                d.setSampleRate(0);
                d.setProduct_name("");
                d.setMfdpkd("");
                d.setMfd_date("");
                d.setShellife_value(0);
                d.setClicked(false);
                d.setBrand_name("");
                d.setNo_sample_product("");
                d.setBb_exp("");
                d.setBb_exp_date("");
                samples.add(d);
            }
            sampleAuditAdapter.notifyDataSetChanged();
        }
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                if( questionPos == (categoryQuestionsArrayList.size() - 2)) {
                    tvNext.setText(R.string.submit_report_audit_submit);
                }else{
                    tvNext.setText(getString(R.string.audit_next));
                }
                Log.i(TAG, "onClick: "+questionPos);

                if (questionPos <= 0) {
                    String checkStartDateTime = "SELECT " + DBHelper.CATEGORY_START_DATE + " FROM " + DBHelper.CATEGORY_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + Store_id + " AND " + DBHelper.CATEGORY_ID + "=" + Cat_id + " AND " + DBHelper.CATEGORY_STATUS + "='Incomplete'";
                    Cursor checkStartDate = DbManager.getInstance().getDetails(checkStartDateTime);
                    Log.e("StartDateTime", checkStartDate.getCount() + "");
                    if (checkStartDate.getCount() <= 0) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.CATEGORY_STATUS, "Incomplete");
                        contentValues.put(DBHelper.CATEGORY_START_DATE, getDateTime());
                        DbManager.getInstance().updateDetails(contentValues, DBHelper.CATEGORY_TBL_NAME, DBHelper.STORE_ID + "=" + Store_id + " AND " + DBHelper.CATEGORY_ID + "=" + Cat_id);
                    }
                }

                if (questionPos < (categoryQuestionsArrayList.size() - 1)) {
                    saveAnswer(Store_id, Cat_id, categoryQuestionsArrayList.get(questionPos).getQuestionId(), questionSubCatId, edt_comment.getText().toString(), edt_remark.getText().toString(), actions.getText().toString());
                    questionPos = questionPos + 1;
                    txt_title.setText("Question No." + (questionPos + 1) + "/" + String.valueOf(categoryQuestionsArrayList.size()));
                    btn_previous.setVisibility(View.VISIBLE);
                    getQuestionDetails(categoryQuestionsArrayList.get(questionPos).getQuestionId());
                    //txt_skip.setChecked(false);
                    //skip="no";
                } else {
                    saveAnswer(Store_id, Cat_id, categoryQuestionsArrayList.get(questionPos).getQuestionId(), questionSubCatId, edt_comment.getText().toString(), edt_remark.getText().toString(), actions.getText().toString());
                    Toast.makeText(ctx, "Audit complete.", Toast.LENGTH_LONG).show();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.CATEGORY_STATUS, "Complete");
                    contentValues.put(DBHelper.CATEGORY_END_DATE, getDateTime());
                    DbManager.getInstance().updateDetails(contentValues, DBHelper.CATEGORY_TBL_NAME, DBHelper.STORE_ID + "=" + Store_id + " AND " + DBHelper.CATEGORY_ID + "=" + Cat_id);

                    try {
                        Intent intent = new Intent("DraftsCount");
                        ctx.sendBroadcast(intent);

                        Fragment fragment = new StartAuditFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("Store_id", Store_id);
                        bundle.putString("Store_name", Store_name);
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.container_body, fragment).addToBackStack("Store").commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

            case R.id.btn_camera:

                if (getAnswerImageCount(questionID) < 4) {
                   // checkPermission();
                    startImageCropper();
                  /*  CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setOutputCompressQuality(40)
                            .start(getContext(), AuditStartFragment.this);
*/

                } else {
                    Toast.makeText(getActivity(), "Only four images are allowed", Toast.LENGTH_SHORT).show();

                }

                break;

            case R.id.btn_previous:
                tvNext.setText(getString(R.string.audit_next));
                isClicked = false;
                hideKeyboard(btn_previous);
                if (questionPos == 1) {
                    btn_previous.setVisibility(View.INVISIBLE);
                }
                if (questionPos >= 1) {
                    saveAnswer(Store_id, Cat_id, categoryQuestionsArrayList.get(questionPos).getQuestionId(), questionSubCatId, edt_comment.getText().toString(), edt_remark.getText().toString(), actions.getText().toString());
                    questionPos = questionPos - 1;
                    txt_title.setText("Question No." + (questionPos + 1) + "/" + String.valueOf(categoryQuestionsArrayList.size()));
                    getQuestionDetails(categoryQuestionsArrayList.get(questionPos).getQuestionId());
                    //txt_skip.setChecked(false);
                    //skip="no";
                } else {
                    Toast.makeText(ctx, "You are currently on the first question", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.img_back:
                skipaudit();
                break;

            case R.id.txt_skip:
                if (txt_skip.isChecked()) {
                    txt_skip.setChecked(true);
                    skip = "yes";
                } else {
                    skip = "no";
                    txt_skip.setChecked(false);
                }
                break;

            case R.id.img_info:
                getFragmentManager().beginTransaction().replace(R.id.container_body, new WebViewScreen()).addToBackStack("Guidelines").commit();
                break;

            case R.id.img_info1:
                getFragmentManager().beginTransaction().replace(R.id.container_body, new WebViewScreen1()).addToBackStack("Guidelines").commit();
                break;

            case R.id.read_more:
                if (read_more.getText().toString().equalsIgnoreCase("more")) {
                    discription_txt.setVisibility(View.VISIBLE);
                    read_more.setText("Hide");
                } else {
                    discription_txt.setVisibility(View.GONE);
                    read_more.setText("More");
                }

                break;
        }
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        } else {
            latitude = 0;
            longitude = 0;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    private void deleteData() {
        DbManager.getInstance().deleteDetails(DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Store_id + "='" + Store_id + "' and " +
                DBHelper.ANSWER_Status + "= 'Complete' ");
        DbManager.getInstance().deleteDetails(DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Store_id + "='" + Store_id + "' and " +
                DBHelper.ANSWER_Status + "= 'Skipped' ");
        Fragment fragment = new Store_Fragement();
        getFragmentManager().beginTransaction().replace(R.id.container_body, fragment)
                .addToBackStack("Audit").commit();
    }

    private String getDateTime() {
        String formattedDate = new SimpleDateFormat("dd MMM yyyy kk:mm").format(Calendar.getInstance().getTime());
        //Toast.makeText(ctx, formattedDate, Toast.LENGTH_SHORT).show();
        return formattedDate;
    }

    private String getDateTime1() {
        String formattedDate = new SimpleDateFormat("ddMMyyyykkmmss").format(Calendar.getInstance().getTime());
        //Toast.makeText(ctx, formattedDate, Toast.LENGTH_SHORT).show();
        return formattedDate;
    }

    private void skipaudit() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setTitle("Skip Audit!");
        dialog.setCancelable(true);
        dialog.setMessage("Are you sure you want to skip the Audit.");
        dialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideKeyboard(btn_next);
                        if (AppUtils.isDraft) {
                            insertDraft();
                            getFragmentManager().beginTransaction().replace(R.id.container_body, new DraftsFragment()).addToBackStack("Drafts").commit();
                        } else {
                            insertDraft();
                            Intent intent = new Intent("DraftsCount");
                            ctx.sendBroadcast(intent);
                            Fragment fragment = new StartAuditFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Store_id", Store_id);
                            bundle.putString("Store_name", Store_name);
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.container_body, fragment).addToBackStack("Store").commit();
                        }
                        dialog.dismiss();
                    }
                });
        dialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void saveDraftData(int i) {

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

    private int checkRadio() {
        int checked = 0;
        if (rg_yes.isChecked()) {
            checked = 0;
        } else if (rg_no.isChecked()) {
            checked = 1;
        } else {
            checked = 2;
        }
        return checked;
    }


    private void doTakePhotoAction() {
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        fileUri = FileProvider.getUriForFile(ctx,
//                "com.itgc.foodsafety.provider", //(use your app signature + ".provider" )
//                photoFile);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        // Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //  getActivity().startActivityForResult(cameraIntent, LOAD_CAMERA_RESULTS);

        if (photoFile != null) {
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                getActivity().startActivityForResult(takePictureIntent, LOAD_CAMERA_RESULTS);
            }
        }
    }

    private File createImageFile() throws IOException {


        File dir = new File(Environment.getExternalStorageDirectory(), "FoodSafety");
        if (!dir.exists())
            dir.mkdir();
        String name = getDateTime1() + "_" + AppPrefrences.getMerchatId(ctx) + "_" + AppPrefrences.getAuditCODE(ctx) +
                "_" + Store_id + "_" + Cat_id + "_" + questionSubCatId + ".jpg";
        File pictureFile = new File(dir, "foodsafety_" + System.currentTimeMillis() + ".jpg");


//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        if (!storageDir.exists())
//            storageDir.mkdirs();
//        File image = File.createTempFile(imageFileName, /* prefix */
//                ".jpg", /* suffix */
//                storageDir /* directory */
//        );
        return pictureFile;
    }

    private void storeImage(Bitmap image) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "FoodSafety");
            if (!dir.exists())
                dir.mkdir();
            File pictureFile = new File(dir, "foodsafety_" + System.currentTimeMillis() + ".jpg");
            if (pictureFile == null) {
                Log.d("",
                        "Error creating media file, check storage permissions: ");// e.getMessage());
                return;
            }
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("", "Error accessing file: " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);


     /*   if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    File targetFile = ImageUtils.createImageFile(
                            ctx,
                            AppPrefrences.getMerchatId(ctx),
                            AppPrefrences.getAuditCODE(ctx),
                            String.valueOf(Store_id),
                            String.valueOf(questionID),
                            String.valueOf(Cat_id),
                            String.valueOf(questionSubCatId)

                    );

                    saveImageLocal(resultUri, targetFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, error != null ? error.getMessage() : "Error in getting image in onActivityResult");
            }
        }*/

    }

    private void saveImageLocal(Uri resultUri, File targetFile) {
        ImageUtils.saveImage(resultUri, targetFile.getAbsolutePath());
        saveanswerImages(targetFile.getName() + ">>" + targetFile.getAbsolutePath());
        encodedImage = "";
        int imsize = getAnswerImageCount(questionID);
        Toast.makeText(getContext(), imsize + "/4 Images Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
        FragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.GONE);
        txt_title.setText("Question No." + (questionPos + 1) + "/" + String.valueOf(categoryQuestionsArrayList.size()));
    }

    private void insertDraft() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rdt_yes:
                if (isChecked)
                    mRecyclerView.setVisibility(View.GONE);
                break;
            case R.id.rdt_no:
                if (isChecked)
                    mRecyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.rdt_partial:
                if (isChecked)
                    mRecyclerView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(ctx);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(ctx,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /////////////////// NEW ////////////////////////

    private void getCategoryQuestion() {
        String query = "SELECT " + DBHelper.QUESTION_ID + " FROM " + DBHelper.QUESTION_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + Store_id + " AND " + DBHelper.CATEGORY_ID + "=" + Cat_id;
        //Log.e("Query",query);
        Cursor c = DbManager.getInstance().getDetails(query);
        //Log.e("Total Questions",c.getCount()+"");
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                CategoryQuestions categoryQuestions = new CategoryQuestions();
                categoryQuestions.setQuestionId(c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID)));
                categoryQuestionsArrayList.add(categoryQuestions);
            } while (c.moveToNext());
            getQuestionDetails(categoryQuestionsArrayList.get(questionPos).getQuestionId());
        } else {
            Toast.makeText(ctx, "No Question Found in this section", Toast.LENGTH_LONG).show();
        }
    }

    private void getQuestionDetails(int questionId) {

        int strId = 0, catId = 0, qId = 0, sampleCount = 1;
        String query = "SELECT * FROM " + DBHelper.QUESTION_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + Store_id + " AND " + DBHelper.CATEGORY_ID + "=" + Cat_id + " AND " + DBHelper.QUESTION_ID + "=" + questionId;
        Cursor c = DbManager.getInstance().getDetails(query);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                question.setText(c.getString(c.getColumnIndex(DBHelper.QUESTION_TEXT)));
                txt_subcat.setText(category + "/" + c.getString(c.getColumnIndex(DBHelper.QUESTION_SUB_CAT_NAME)));
                discription_txt.setText(c.getString(c.getColumnIndex(DBHelper.QUESTION_DESC)));
                questionSubCatId = c.getInt(c.getColumnIndex(DBHelper.QUESTION_SUB_CAT_ID));
                catId = c.getInt(c.getColumnIndex(DBHelper.CATEGORY_ID));
                strId = c.getInt(c.getColumnIndex(DBHelper.STORE_ID));
                qId = c.getInt(c.getColumnIndex(DBHelper.QUESTION_ID));
                questionID = qId;
                sampleCount = c.getInt(c.getColumnIndex(DBHelper.QUESTION_SAMPLES));
                type = Integer.parseInt(c.getString(c.getColumnIndex(DBHelper.QUESTION_TYPE)));
                rdt_isfail.setChecked(false);
                maxSample = sampleCount;
            } while (c.moveToNext());

            if (type == 1) {
                rg1.setVisibility(View.GONE);
                lin_audit.setVisibility(View.GONE);
            } else if (type == 2) {
                rg1.setVisibility(View.VISIBLE);
                lin_audit.setVisibility(View.VISIBLE);
                //       samples.get(0).setIs_temp_visible(false);

            } else {
                rg1.setVisibility(View.VISIBLE);
                lin_audit.setVisibility(View.VISIBLE);
                //        samples.get(0).setIs_temp_visible(true);
            }

            setSampleSpinnerData(sampleCount);
            getQuestionAnswer(strId, catId, qId);
        }
    }

    private void getQuestionAnswer(int storeId, int categoryId, int questionId) {
        String answerQuery = "SELECT * FROM " + DBHelper.ANSWER_TBL_NAME + " WHERE " +
                DBHelper.STORE_ID + "=" + storeId + " AND " +
                DBHelper.CATEGORY_ID + "=" + categoryId + " AND " +
                DBHelper.QUESTION_ID + "=" + questionId;

        Log.e("Answer Query", answerQuery);
        Cursor answer = DbManager.getInstance().getDetails(answerQuery);
        if (answer.getCount() > 0) {
            Log.e("Answer", "Available");
            answer.moveToFirst();
            //do {
            Log.e("Answer", answer.getString(answer.getColumnIndex(DBHelper.STORE_ID)) + " " + answer.getString(answer.getColumnIndex(DBHelper.CATEGORY_ID)) + " " + answer.getString(answer.getColumnIndex(DBHelper.QUESTION_ID)) + " " + answer.getString(answer.getColumnIndex(DBHelper.ANSWER_COMMENT)) + " " + answer.getString(answer.getColumnIndex(DBHelper.ANSWER_REMARK)) + " " + answer.getString(answer.getColumnIndex(DBHelper.ANSWER_ACTION)) + " " + answer.getString(answer.getColumnIndex(DBHelper.ANSWER_DATETIME)));
            //}while (answer.moveToNext());
            edt_comment.setText(answer.getString(answer.getColumnIndex(DBHelper.ANSWER_COMMENT)));
            edt_remark.setText(answer.getString(answer.getColumnIndex(DBHelper.ANSWER_REMARK)));
            actions.setText(answer.getString(answer.getColumnIndex(DBHelper.ANSWER_ACTION)));
            String skipInfo = answer.getString(answer.getColumnIndex(DBHelper.ANSWER_QUES_SKIP));


            int checked = Integer.parseInt(answer.getString(answer.getColumnIndex(DBHelper.ANSWER_TYPE)));
            if (checked == 0) {
                rg_yes.setChecked(true);
            } else if (checked == 1) {
                rg_no.setChecked(true);
            } else {
                rg_partial.setChecked(true);
            }

            if (skipInfo.equalsIgnoreCase("no")) {
                skip = "no";
                txt_skip.setChecked(false);
            } else {
                skip = "yes";
                txt_skip.setChecked(true);
            }

            int sample = answer.getInt(answer.getColumnIndex(DBHelper.ANSWER_NO_SAMPLE));
            if (type == 1) {
                rg1.setVisibility(View.GONE);
                lin_audit.setVisibility(View.GONE);
            } else if (type == 2 || type == 3) {
                rg1.setVisibility(View.VISIBLE);
                lin_audit.setVisibility(View.VISIBLE);
            }


            list_sampleno.setSelection(sample - 1);

            String sampleQuery = "SELECT * FROM " + DBHelper.AUDIT_SAMPLE_TBL_NAME + " WHERE " +
                    DBHelper.STORE_ID + "=" + storeId + " AND " +
                    DBHelper.CATEGORY_ID + "=" + categoryId + " AND " +
                    DBHelper.QUESTION_ID + "=" + questionId;
            Cursor samplesCursor = DbManager.getInstance().getDetails(sampleQuery);
            Log.e("Total Samples", samplesCursor.getCount() + "");
            if (samplesCursor.getCount() > 0) {
                samples.clear();
                samplesCursor.moveToFirst();
                do {
                    SampleDetails d = new SampleDetails();
                    if (type == 3) {
                        d.setIs_temp_visible(true);
                    } else {
                        d.setIs_temp_visible(false);
                    }

                    d.setClicked(Boolean.parseBoolean(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.SAMPLE_IS_CLICKED))));
                    d.setIs_sample_failed(samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.IS_SAMPLE_CLICKED)));
                    d.setSamplePos(samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_POS)));
                    d.setSampleCount(samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_COUNT)));
                    d.setNo_sample_product(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.NO_SAMPLE_PRODUCT)));
                    d.setProduct_name(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.PRODUCCT_NAME)));
                    d.setBrand_name(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.BRAND_NAME)));
                    d.setMfdpkd(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.MFDPKD)));
                    d.setMfd_date(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.MFDDATA)));
                    d.setBb_exp(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.BB_EXP)));
                    d.setBb_exp_date(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.BBEXPDATA)));
                    d.setShellife_value(samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SELFLIFE)));
                    d.setTemperature(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.TEMPERATURE)));
                    d.setSampleCurrentRate(samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_CURRENT_RATE)));
//                    d.setIs_sample_failed(Integer.parseInt(samplesCursor.getString(samplesCursor.getColumnIndex(DBHelper.is_sample_failed))));
                    samples.add(d);
                    Log.e("Samples Rates", samplesCursor.getInt(samplesCursor.getColumnIndex(DBHelper.SAMPLE_CURRENT_RATE)) + "");
                } while (samplesCursor.moveToNext());

                // sampleAuditAdapter.setData(samples);
                // sampleAuditAdapter = new SampleAuditAdapter(samples,ctx,Store_id,Cat_id,questionID,AuditStartFragment.this);
                sampleAuditAdapter.notifyDataSetChanged();
            }

        } else {
            Log.e("Answer", "Not Available");
            edt_comment.setText("");
            edt_remark.setText("");
            actions.setText("");
            rg_yes.setChecked(true);
            skip = "no";
            txt_skip.setChecked(false);
            samples.clear();

            SampleDetails d = new SampleDetails();
            if (type == 3) {
                d.setIs_temp_visible(true);
            } else {
                d.setIs_temp_visible(false);
            }
            samples.add(d);
            sampleAuditAdapter.notifyDataSetChanged();


        }
        answer.close();
    }

    private void saveAnswer(int storeId, int categoryId, int questionId, int subCatId, String comment, String remark, String action) {
        String currentDateTime = new SimpleDateFormat("dd MMM yyyy HH:mm a").format(Calendar.getInstance().getTime());
        Cursor checkIfExists = DbManager.getInstance().getDetails("SELECT * FROM " + DBHelper.ANSWER_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + storeId + " AND " +
                DBHelper.CATEGORY_ID + "=" + categoryId + " AND " +
                DBHelper.QUESTION_ID + "=" + questionId);
        if (checkIfExists.getCount() > 0) {
            Log.e("Answer Available", " So Update The Existing Answer");
            ContentValues c = new ContentValues();
            c.put(DBHelper.ANSWER_COMMENT, comment);
            c.put(DBHelper.ANSWER_REMARK, remark);
            c.put(DBHelper.ANSWER_ACTION, action);
            c.put(DBHelper.ANSWER_DATETIME, currentDateTime);
            c.put(DBHelper.ANSWER_TYPE, checkRadio());
            c.put(DBHelper.ANSWER_CAT_SKIP, "no");
            c.put(DBHelper.ANSWER_IS_SEEN, "false");
            c.put(DBHelper.ANSWER_MAX_NO, sample_selected);
            c.put(DBHelper.ANSWER_MAX_SAMPLE, maxSample);
            c.put(DBHelper.ANSWER_NO_SAMPLE, sample_selected);
            c.put(DBHelper.ANSWER_QUES_SKIP, skip);
            c.put(DBHelper.ANSWER_SUBCAT_ID, subCatId);
            c.put(DBHelper.SEC_EXISTS, "1");
            c.put(DBHelper.QUESTION_FAIL, is_failed);
            c.put(DBHelper.ANSWER_CAT_TYPE, type);

            DbManager.getInstance().updateDetails(c, DBHelper.ANSWER_TBL_NAME, DBHelper.STORE_ID + "=" + storeId + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId + " AND " + DBHelper.QUESTION_ID + "=" + questionId);
        } else {
            Log.e("Answer Not Available", " So Inserting Answer");
            ContentValues c = new ContentValues();
            c.put(DBHelper.STORE_ID, storeId);
            c.put(DBHelper.CATEGORY_ID, categoryId);
            c.put(DBHelper.ANSWER_SUBCAT_ID, subCatId);
            c.put(DBHelper.QUESTION_ID, questionId);
            c.put(DBHelper.ANSWER_COMMENT, comment);
            c.put(DBHelper.ANSWER_REMARK, remark);
            c.put(DBHelper.ANSWER_ACTION, action);
            c.put(DBHelper.ANSWER_DATETIME, currentDateTime);
            c.put(DBHelper.ANSWER_TYPE, checkRadio());
            c.put(DBHelper.ANSWER_CAT_SKIP, "no");
            c.put(DBHelper.ANSWER_IS_SEEN, "false");
            c.put(DBHelper.ANSWER_MAX_NO, max_num.getText().toString());
            c.put(DBHelper.ANSWER_MAX_SAMPLE, maxSample);
            c.put(DBHelper.ANSWER_NO_SAMPLE, sample_selected);
            c.put(DBHelper.ANSWER_QUES_SKIP, skip);
            c.put(DBHelper.SEC_EXISTS, "1");
            c.put(DBHelper.QUESTION_FAIL, is_failed);
            c.put(DBHelper.ANSWER_CAT_TYPE, type);
            DbManager.getInstance().insertDetails(c, DBHelper.ANSWER_TBL_NAME);
        }
        saveSampleAudits(storeId, categoryId, questionId);
    }

    private void saveSampleAudits(int storeId, int categoryId, int questionId) {
        DbManager.getInstance().deleteDetails(DBHelper.AUDIT_SAMPLE_TBL_NAME, DBHelper.STORE_ID + "=" + storeId + " AND " + DBHelper.CATEGORY_ID + "=" + categoryId + " AND " + DBHelper.QUESTION_ID + "=" + questionId);
        if (checkRadio() > 0) {
            Log.e("Partial", "Checked");

            for (int i = 0; i < samples.size(); i++) {
                ContentValues c = new ContentValues();
                c.put(DBHelper.STORE_ID, storeId);
                c.put(DBHelper.CATEGORY_ID, categoryId);
                c.put(DBHelper.QUESTION_ID, questionId);
                c.put(DBHelper.SAMPLE_IS_CLICKED, "true");
                c.put(DBHelper.SAMPLE_POS, samples.get(i).getSamplePos());
                c.put(DBHelper.NO_SAMPLE_PRODUCT, samples.get(i).getNo_sample_product());
                c.put(DBHelper.PRODUCCT_NAME, samples.get(i).getProduct_name());
                c.put(DBHelper.BRAND_NAME, samples.get(i).getBrand_name());
                c.put(DBHelper.MFDPKD, samples.get(i).getMfdpkd());
                c.put(DBHelper.MFDDATA, samples.get(i).getMfd_date());
                c.put(DBHelper.BB_EXP, samples.get(i).getBb_exp());
                c.put(DBHelper.BBEXPDATA, samples.get(i).getBb_exp_date());
                c.put(DBHelper.SELFLIFE, samples.get(i).getShellife_value());
                c.put(DBHelper.TEMPERATURE, samples.get(i).getTemperature());
                c.put(DBHelper.IS_SAMPLE_CLICKED, samples.get(i).getIs_sample_failed());
                c.put(DBHelper.SAMPLE_COUNT, samples.get(i).getSampleCount());
                c.put(DBHelper.SAMPLE_CURRENT_RATE, samples.get(i).getSampleCurrentRate());
                DbManager.getInstance().insertDetails(c, DBHelper.AUDIT_SAMPLE_TBL_NAME);
            }
        }
    }

    private int getAnswerImageCount(int questionId) {
        String query = "SELECT * FROM " + DBHelper.ANSWER_IMAGE_TBL_NAME + " WHERE " + DBHelper.STORE_ID + "=" + Store_id + " AND " + DBHelper.CATEGORY_ID + "=" + Cat_id + " AND " + DBHelper.QUESTION_ID + "=" + questionId;
        Cursor c = DbManager.getInstance().getDetails(query);
        //Toast.makeText(ctx,"Image Count "+ c.getCount()+"", Toast.LENGTH_SHORT).show();
        return c.getCount();
    }

    private void saveanswerImages(String imageString) {
        Log.i(TAG, "saveanswerImages: "+imageString);
        ContentValues c = new ContentValues();
        c.put(DBHelper.STORE_ID, Store_id);
        c.put(DBHelper.CATEGORY_ID, Cat_id);
        c.put(DBHelper.QUESTION_ID, questionID);
        c.put(DBHelper.ANSWER_IMAGE, imageString);
        DbManager.getInstance().insertDetails(c, DBHelper.ANSWER_IMAGE_TBL_NAME);
    }


    public void updateSamples(int pos, double value, String no_sample_product, String product_name, String brand_name,
                              String mfdpkd, String mfd_date, String bb_exp, String bb_expdate, int Selflife_value, String temperature, int sample_value_fail) {
        samples.get(pos).setSampleCurrentRate(value);
        samples.get(pos).setNo_sample_product(no_sample_product);
        samples.get(pos).setProduct_name(product_name);
        samples.get(pos).setBrand_name(brand_name);
        samples.get(pos).setMfdpkd(mfdpkd);
        samples.get(pos).setMfd_date(mfd_date);
        samples.get(pos).setBb_exp(bb_exp);
        samples.get(pos).setBb_exp_date(bb_expdate);
        samples.get(pos).setShellife_value(Selflife_value);
        samples.get(pos).setTemperature(temperature);
        samples.get(pos).setIs_sample_failed(sample_value_fail);
        for (int i = 0; i < samples.size(); i++) {
            Log.e("Sample Rate", samples.get(i).getSampleCurrentRate() + "");
        }
    }
    private void startImageCropper() {
        CropImageOptions options = new CropImageOptions();
        options.guidelines =CropImageView.Guidelines.ON;
        options.aspectRatioX =1;
        options.aspectRatioY =1;

        cropImageLauncher.launch(new CropImageContractOptions(null, options));

       // cropImageLauncher.launch(options);
    }
}