package com.itgc.foodsafety.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.itgc.foodsafety.MainActivity;
import com.itgc.foodsafety.MySingleton;
import com.itgc.foodsafety.R;
import com.itgc.foodsafety.adapter.AuditStartAdapter;
import com.itgc.foodsafety.dao.Answers;
import com.itgc.foodsafety.dao.Sample_Audit;
import com.itgc.foodsafety.dao.StartAudit;
import com.itgc.foodsafety.db.DBHelper;
import com.itgc.foodsafety.db.DbManager;
import com.itgc.foodsafety.utils.AppPrefrences;
import com.itgc.foodsafety.utils.AppUtils;
import com.itgc.foodsafety.utils.BitmapHelper;
import com.itgc.foodsafety.utils.FilePathUtils;
import com.itgc.foodsafety.utils.Vars;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/10/15.
 */
public class AuditStartFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private double latitude, longitude;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Answers> answersArrayList;
    private ArrayList<ArrayList<Answers>> lists;
    private Location mLastLocation;
    private Context ctx;
    private View view;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Bundle b;
    private String category, formattedDate, Store_name, skip = "no", store_loc;
    private TextView audit_date, question, max_num, txt_title, txt_subcat, discription_txt, read_more;
    private RadioButton rg_yes, rg_no, rg_partial;
    private AutoCompleteTextView edt_remark, edt_comment;
    private LinearLayout btn_previous, btn_next;
    private ImageView btn_camera, img_back, img_info, img_info1;
    private int count = 1;
    private AuditStartAdapter startAdapter;
    int TAKE_PHOTO_CODE = 0;
    int GALLERY_IMG = 3;
    private Spinner list_sampleno;
    private ArrayList<StartAudit> audit;
    private ArrayList<Sample_Audit> sampleAudits;
    private ArrayList<Answers> answerses, getAnswerses;
    private DBHelper dbHelper;
    private int Cat_id, Store_id, sample_selected = 1, type;
    private String encodedImage = "";
    private boolean isClicked = true;
    private RadioGroup rg1;
    private LinearLayout lin_audit;
    private CheckBox txt_skip;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private com.itgc.foodsafety.ui.LinearLayoutManager mLayoutManager;
    private Boolean isImg = false;
    private int audit_id = 0;
    private String Previous_data = "";
    private int Image_count = 0;
    private EditText actions;
    private AlertDialog d;
    private static final int PICK_IMAGE_REQUEST_PERMISSION = 1;
    private Uri fileUri;
    private String picturePath;
    private File photoFile;
    private static int LOAD_CAMERA_RESULTS = 11;


    static final String[] remarks = new String[]{"No Exceptions Found.", "Not Applicable.",
            "FSSAI License was not displayed at the prominent place; it was displayed in the back room.",
            "No pallets being used and the food stock was touching the walls in the Grocery & Back Room sections.",
            "There was Flaking/Peeling of walls observed.", "The sliding window panel of the display chiller was dirty.",
            "Gaskets of the chiller were not properly cleaned.", "The entrance door of the meat shop was dirty.",
            "The Gaskets of the chiller and freezer were dirty.",
            "No records for cleaning of chillers & freezers were being maintained.",
            "Air cutter was not in operation. Fly catcher was installed at a height of above 6ft.",
            "Flaking/Peeling of paint found on one of the walls close to the display chiller in the Meat shop.",
            "Gaskets of the chillers in the Meat shop were dirty.",
            "Flycatcher was installed at above 7ft. height in the Meat shop.",
            "Flycatcher was not working in the Back room and was installed at above 7ft. height.",
            "The iron frames for keeping vegetable baskets were not properly cleaned.",
            "Gaskets were not properly cleaned in two chillers in the Grocery.",
            "Vegetable basket frames were dirty in Fruits & Vegetable section.",
            "No Approved Chemicals were found in the store.",
            "Gaskets and Doors were found dirty both in chiller and freezer in the Meat shop.",
            "No sanitizer available and only floor cleaning chemical was found in the store.",
            "Dirty basket and dirty Mop was placed in the meat shop.",
            "No pallets were being used and the food stock was touching the walls in the Back room.",
            "Flycatcher was not in working condition both in the Meat Shop & Grocery section.",
            "Gaskets of the Chillers & Freezers were not properly cleaned in the Grocery section.",
            "The cooling fans of the chillers were dirty in the Grocery section.",
            "Only one approved chemical was found in the store.",
            "Only one UV tube was working in the flycatcher both in the Back room & FnV sections.",
            "Flycatchers were installed at a above 6ft. height in the store.",
            "Hand Wash Sinks were dirty in the meat shop.", "The floor was found to be dirty in the meat Shop.",
            "Floors were not properly cleaned in the Grocery, some food material and cart board were lying on the floor.",
            "The freezer in the meat shop was found to be dirty both from inside and outside.",
            "No Approved Chemicals were found in the Store.", "Flies were found in the Meat Section.",
            "Air cutter of the Meat section was not in operation.",
            "Iron Frames in the Fruits & Vegetable section were not properly cleaned.",
            "One of the Chillers’ cooling fan was dirty in the Grocery Section.",
            "Food Handlers were using Dettol Soap for washing hands in the store.",
            "The chillers' fan covers & Gaskets were dirty.", "No Approved Chemicals were found in the store.",
            "The food shelves were touching the walls.",
            "No pallets were being used and the food stock was directly placed on the floor and also touching the walls.",
            "Fly catcher was installed at a height of above 7ft and only one UV tube was working.",
            "Food Handlers were using Dettol Soap for washing hands in the store.",
            "The temperature inside the freezer was recorded at -15 Degrees Celsius.",
            "The temperature inside the freezer was recorded at -15 Degrees Celsius.",
            "The Gaskets and the Fan Covers of the chillers were dirty.",
            "Hand marks/stains were observed on the glass doors of the chillers.",
            "No Approved Chemicals were found in the store.",
            "Four packets of Chicken Nuggets were found alongside the Veg Products (Frozen Peas) in the freezer.",
            "No sufficient distance maintained from the walls as food shelves were touching the walls.",
            "Back Room - No pallets were being used and the food stock was touching the walls.",
            "Fly Catcher was installed at a height of above 7ft and only one UV tube was in working condition.",
            "There was rust on the bottom body part of the chillers from inside.",
            "One of the UV tubes in the flycatcher was not working.",
            "The Gaskets and the fan covers of both the chillers were dirty.",
            "The packs of Wheat Floor were directly placed on the floor and were also touching the walls on some places.",
            "No Approved Chemicals were found in the store.", "A dead flying insect was found in one of the chillers.",
            "Floor was dirty; lot of footmarks and the deposition of dirt were observed.",
            "The fan covers of the chillers were dirty.", "The fan covers of both chillers were dirty.",
            "Sufficient distance was not maintained between the Iron racks and the walls.",
            "The merchandizing shelves were attached to the walls.", "The floor of the chiller zone was not properly cleaned.",
            "The merchandizing shelves of the chillers were found to be dirty.",
            "The Gaskets of the freezers were not properly cleaned.", "No Approved Chemicals were available in the store.",
            "Frozen Veg food products like Aloo Tikki, Frozen Peas etc. were placed along with Non-Veg frozen food products in the freezer.",
            "Packs of Basmati Rice were placed directly on the floor.", "Edible oil was placed alongside the detergent.",
            "Food articles were placed directly on the floor and were touching the walls.",
            "Bad Storage Practices; Food & Non-Food Articles were placed together.",
            "Food Stock was directly placed on the floor and was touching the walls.",
            "Both Dairy Chillers were overloaded with Food Stock.", "Freezer was overloaded with Food Stock.",
            "Approx. 100 packs of frozen Veg Food Products were kept in the chillers.",
            "On many places, the Food Stock was touching the walls.",
            "No pallets were being used and the food stock was touching the walls.",
            "Mosquitoes & Flies were found in Grocery & Back Room areas.",
            "Two Flycatchers were installed in the section but only one was in the working condition.",
            "4 packs of Toffu Paneer having the date of expiry on 27th November were placed with fresh paneer in the dairy chiller with no flag for identification of near expiry date.",
            "2 packs of Yakut LCS Sherohos with Mfg. date 13th October and 40 Days of Best Before were found at the retail counter at the time of the audit.",
            "3 packs of French Fries (McCain) were found in torn-off condition in the freezer.",
            "The date of Pkg. was missing on the Egg Trays.",
            "The complete Lot of Cake (Packaged) had date of packaging - November 2015 with Expiry date of January 2015.",
            "No records of pest control activity in the store were found for the month of November 2015.",
            "Dead Lizard in the decomposed state was found in the Rodent box."};

    static final String[] comments = {
            "FSSAI licence was not available in the store.",
            "Food handler's nails were not found trimmed, also nail enamel was put on.",
            "The sliding window channel of the freezer was dirty (dust & deposition found).", "The cutting chopper (Mutton) was found dirty; Blood stains and meat particles were seen.",
            "Dirty uniforms were found placed in the meat section.",
            "Food handler was wearing the rings while on operations.",
            "Two dirty aprons were found placed in the meat shop.",
            "One dirty apron was found placed in the meat section.",
            "The food handler in the butchery was not fully aware of the guidelines on hand wash activity. ",
            "The food handler was not aware about the hand wash activity.",
            "Water Leakage observed from the hand wash sink tap.",
            "One dirty apron & two dirty white coats were found in the meat section.",
            "Two dirty aprons and one dirty white coat were found in the meat section.",
            "Food handler was found wearing a wrist band.",
            "The food handler was not aware about the hand wash activity. ",
            "Waste filters were found missing in the hand wash sink.",
            "The food handler's nails were not found trimmed.",
            "Two packs of chewing tobacco were found in the food handler's pocket.",
            "Hand wash sink was found dirty.",
            "Food handler was not wearing the dress while on operations. ",
            "The food handler was not wearing the apron while on operations. ",
            "One dirty apron was found in the meat shop.",
            "One dirty apron & two dirty white coats were found placed in the meat section.",
            "Food handler was found wearing a dirty apron. Also, one dirty apron was found placed in the meat section.",
            "The food handler was not wearing gloves while on operations in the meat shop.",
            "Elbow lever was not found fixed on the elbow action tap.",
            "No apron was available to be used by the food handler in the meat section ",
            "Food Handler was found wearing a ring and a thread during meat operations.",
            "Food handler was found wearing a dirty apron.",
            "The food handler was not fully aware of using the elbow lever while washing hands.",
            "Food handler was wearing a dirty apron.",
            "One dirty apron was found in the meat section.",
            "Hand wash sink was found very dirty and dirty knives were found in sink.",
            "Food handler was wearing a red thread on the wrist.",
            "Hand wash sink was found dirty.",
            "The product temperature (Milk) in one of the dairy chillers was recorded as 10.4 degrees C.",
            "Product temperature (Aloo Tikki) in veg freezer was recorded as -5.6 degrees C.",
            "Product temperature (Milk) in one of the dairy chillers was recorded as 9.3 degrees C.",
            "The product temperatures (Green Peas) & (Masala Fries) in two of the freezers were recorded as -11.8 degrees C and -7.5 degrees C respectively.",
            "The product temperature (Yumeeiz Cheese Finger) in veg freezer was recorded as -8.5 degrees C.",
            "The product temperature (McCain Aloo Tikki) in veg freezer was recorded as -12.5 degrees C.",
            "The temperature log book was not found updated for the day of the audit i.e. 25.07.2016.",
            "The thermometer validation log book was not found updated since 13.06.2016.",
            "Ice formation found in the non-veg freezer.",
            "Thermometers were not found in the working condition.",
            "The temperature log book was not found updated for 7 am schedule on the day of the audit.",
            "The veg freezer was found overloaded and the product temperature (Pagro Frozen Mixed Vegetables) in the same freezer was recorded as -3.7 degrees C.",
            "No thermometer was available in the store. ",
            "The temperature log book was not found updated since June 26, 2016.",
            "The thermometer validation log book was not available in the store. ",
            "The thermometer was not found in the working condition.",
            "The validation log book for thermometer was not available in the store..",
            "Thermometer was not found in the working condition.",
            "The temperature log book was found verified by the same person from last few days.",
            "The temperature record log book was not found verified by respective TL / SM from 24th August, 2016.",
            "Incorrect Storage Practices: Sufficient space was not given on the back from inside of the dairy chiller to let the cool air go down the lower shelves.",
            "No thermometer was available in the store.",
            "The temperature log book was not found updated since morning.",
            "The thermometer validation records are not being maintained; store does not have a provision for the same.",
            "Ice formation was observed in the non-veg freezer.",
            "The non-veg freezer was found overloaded and the temperature was recorded as -15.9 degrees C.",
            "Ice formation was observed in the veg freezer.",
            "Ice formation was observed in the veg freezer. The problem of condensation water leakage was observed in the dairy chiller.",
            "The log book for thermometer calibration was not available in the store.",
            "No validation records for thermometer were available in the store.",
            "One of the freezers was found overloaded.",
            "Ice formation was observed in both the veg as well as in the non-veg freezer.",
            "Ice formation was observed in the veg freezer.",
            "The inner surface area of the raw meat chiller was found dirty.",
            "Some floor tiles were found broken.",
            "The ceiling fan was found dirty.",
            "The ceiling fan was found dirty. Also, Peeling of paint observed on the ceiling.",
            "The bottom surface of one of the dairy chillers was found dirty; deposition of milk etc. observed.",
            "Ice formation was observed in non-veg freezer.",
            "The test strip for measuring the concentrated solution was not available in the store.",
            "The chopping board was not found cleaned; Blood stains and meat particles were observed. Also, the knives were dirty.",
            "One approved chemical (Kleen Hand) was not available in the the store. ",
            "Fan covers and gaskets of the non-veg freezer were found dirty. The inside walls of one of the dairy chillers were found dirty; deposition found.",
            "The sanitized Food pans were not found shrink wrapped. Also, the food pans were not found placed on their appropriate location.",
            "Floor was found dirty; deposition observed.",
            "Fan cover of one of the dairy chillers was found dirty.",
            "The chopping board was found dirty",
            "The bottom surface of the veg freezer was found dirty; corns were found spread in the freezer.",
            "The fan covers of both the dairy chillers were found dirty.",
            "Fan covers of one dairy chiller were found dirty. The gaskets of one of the freezers were found dirty. ",
            "Fan covers of both the dairy chillers were found dirty.",
            "The sliding window channel of the veg freezer was found dirty; dirt and deposition observed.",
            "The chopping board was not found sanitized. ",
            "The testing strip (for sanitizer) was not available in the store. ",
            "All utensils were not found properly cleaned & sanitized, foul smell was also observed. ",
            "The testing strip (for sanitizer) was not available in the meat section.  ",
            "The bottom surface of the vendor's chiller was found dirty. ",
            "The gaskets, fan cover and bottom surface of one of the dairy chillers were found dirty. ",
            "The test strip for measuring the concentrated solution was not found in the store. ",
            "The chopping board was not found cleaned. ",
            "Seepage was observed on one of the walls; food stock was placed near-by the same wall. ",
            "The sliding window channel of the veg freezer was dirty. ",
            "Seepage was observed on the wall, also the paint was found peeled off. ",
            "The Gaskets and fan cover of one of the dairy chillers were dirty. The sliding window channel of the veg freezer was found dirty; deposition observed. ",
            "The food stock (Pulses etc) was directly placed on the floor, no pallets were being used. ",
            "The bottom surface of the dairy chiller was found rusted. ",
            "Veg and non-veg food articles were kept together in the same freezer. ",
            "The sliding window channel of the raw meat chiller was found rusted. ",
            "The magnetic belt (for holding knives) was found rusted. ",
            "Veg and non-veg food items were found placed together in the freezer.",
            "No pallets were being used and the food stock was directly placed on the floor. ",
            "A condensation water leakage problem was observed in one of the dairy chillers.",
            "The knife holder (Magnetic Belt) was found rusted, even the marks of rust could be seen on the knives. ",
            "The condensation water leakage was observed from the dairy chiller. ",
            "The food stock was directly placed on the floor; no pallets were being used. ",
            "A condensation water leakage problem was observed from one of the dairy chillers. ",
            "The water leakage from the geyser water pipe was observed. ",
            "The raw meat food pans were not found covered with shrink wrap. ",
            "A condensation water leakage problem was observed from the Air Conditioner. ",
            "The magnetic belt (Knife Holder) was found rusted. ",
            "The iron scooper was found rusted. ",
            "Hairs found in raw meat; proper cleaning not done. ",
            "The food stock was directly placed on the floor. ",
            "The bottom surface of one dairy chiller was found rusted. ",
            "The magnetic belt (for holding knives) was found rusted. The sliding window channel of the raw meat chiller was also found rusted. ",
            "Veg and non-veg packaged food articles were found placed together in the veg freezer. ",
            "Veg and non-veg food items were found placed together in the veg freezer. ",
            "The iron scooper (in the loose sugar container) was found rusted. ",
            "The Sliding window channel of raw meat chiller was found rusted. ",
            "The veg and non-veg food articles were stored together in veg freezer. ",
            "Veg and non-veg food products were found placed together inside the ice-cream freezer. ",
            "A condensation water leakage problem was observed from the air conditioner. ",
            "Food (Soft Drinks) and non-food (Cleaning Chemicals) were found placed together. ",
            "A condensation water leakage problem was observed from the dairy chiller.  ",
            "Food stock was directly placed on the floor; no pallets were being used. ",
            "The veg and non-veg food items were kept in the same freezer. ",
            "The condensation water leakage problem was observed from raw meat chiller and from the air conditioner. ",
            "Expired food: 1 pack of easy choice pulse (Moong Chilka) was found having PKD Dt. 04/12/2015 and BEST BEFORE mentioned as 03/05/2016. ",
            "Expired food: Two trays of Farm Fresh Eggs (60 eggs) having PKD Dt. 04.July.2016 and BEST BEFORE 21 days. 5 packs of Litchi Delight Juice (1Ltr each) having Mfg. Dt. 27.12.15 and 4 packs ",
            "(Same Product) having Mfg. Dt. 19.01.16 and BEST BEFORE mentioned as 6 months. 17 kg Tilda Basmati Rice having PKD Dt. 03/04/2014 and BEST BEFORE mentioned as 24 months. 2 jars of Dawat Brown Rice having PKD Dt. 19/06/2015 and BEST BEFORE mentioned as 12 months. 2 jars of Kissan Jam having PKD Dt. 18.03.2015 and BEST BEFORE mentioned as 12 months. ",
            "Expired food: 1 Jar of Dawat Brown Basmati Rice having PKD Dt. 18/06/2015 and BEST BEFORE - 12 months. ",
            "Expired food: 2 packs of Nylon's Garam Masala having Mfg. Dt. NOV 2015 and BEST BEFORE 8 months. ",
            "Expired food: 1 pack of Britannia Cake having PKD Dt. 22/04/16 and BEST BEFORE 3 months. ",
            "Expired food: 1 pack of 2 kg Masoor Whole having PKD Dt. 28/01/2016 and BEST BEFORE Dt. 27/06/2016. ",
            "Expired food: 6 packs of Large White Eggs having PKD Dt. 06/07/2016 and BEST BEFORE 21 days. ",
            "Expired food: 10 packs of Tilda Basmati Rice having PKD Dt: 03.04.2014 and BEST BEFORE:\n" + "24 months. 8 packs Tetley Lemon Tea having Mfg. Dt: 07.2015 and BEST BEFORE: 12 months. 1 bottle of Tasty Treat Mango Drink having Mfg. Dt: 15.12.2015 and BEST BEFORE: 6 months. ",
            "Expired food: 1 pack of Venky's Chicken Pops having Mfg. Dt. 15.01.2016, 1 pack of Chicken Meat Balls having Mfg Dt. 17.01.2016, 1 pack of Yummiez Punjabi Tikka having Pkg Dt. 09.01.2016 and the BEST BEFORE Dt. for all these food products was mentioned as 6months. ",
            "Expired food: 3 packs of Nilon's Garam Masala having PKD Dt. 11,2015 and BEST BEFORE - 8 months. 11 packs of Tasty Treat Navratan Mix having PKD. Dt. 20.JAN.2016 and BEST BEFORE - 6 months. 1 bottle Mountain Dew having Mfg. Dt. 28.04.2016 and BEST BEFORE - 3 months. 16 bottles of Appy Fizz having Mfg. Dt. 21.03.2016 and BEST BEFORE - 4 months. 6 packs of Britannia Cheese Slice having PKD Dt. NOV 2015 and BEST BEFORE - 9 months. ",
            "Expired food: 1 pack of Hen Large White Eggs (10 eggs) having PKD Dt. 11.07.2016 and BEST BEFORE mentioned as 21 days. Image not available as store staff removed the expired sample from the location. ",
            "Expired food: 1 pack of Chicken Meat Balls (Brand - Venky's) having Mfg. Dt. 17.01.2016 and BEST BEFORE mentioned as 6 months. ",
            "Expired food placed as Dump but no label declaration for identification: 1 pack of Golden Harvest Maida having PKD Dt. 20/04/2016 and BEST BEFORE 3 months. 1 pack of EM Cashew having PKD 28/12/2015 and BEST BEFORE Dt. 27/06/2016. 1 pack of  Bourbon Bliss Biscuits having PKD Dt. 06/01/16 and BEST BEFORE 6 months. 1 pack of EM Cardamon Green (Elaichi) PKD Dt. 22/01/2016 and BEST BEFORE Dt. 20/07/2016. 1 pack of Hi Fi Cashew Butter Cookies having PKD Dt. 31/10/15 and BEST BEFORE 6months. 1 pack of Mango Pickle PKD Dt. June 15 and BEST BEFORE 12 months. ",
            "Leakage observed from 2 packs of Sundrop Heart Oil. ",
            "2 bags of Whole Wheat Flour were found in the damaged condition. ",
            "4 Packs of EM Pistachio having PKD Dt. 28.07.2016 and BEST BEFORE Dt 27.07.2016. 3 packs of Tropicana Guava Delight Juice having PKD Dt. 25.01.2016 and 1 pack of same product having PKD Dt. 25.12.15 and BEST BEFORE 6 months. 1 pack of EC Cashew having PKD Dt. 28.01.2016 and BEST BEFORE Dt. 27.07.2016. 1 pack of Golden Harvest having PKD Dt. 28/03/2016 and BEST BEFORE 4 months (The Stock partially found in the grocery & in the clearance bin). ",
            "A 3 kg White pumpkin in the spoiled state was found in the FnV section. Store staff immediately took away so no image available. ",
            "Food pans in the display chiller were not found labeled with the date of receiving and/or Use- by-date. ",
            "Date of manufacture could not be recognized; 4 packs of MTS Rava Idli were found having a punching hole at the spot on the label where Mfg. date was printed. ",
            "2kg white pumpkin was found in the stale condition. ",
            "Insects were seen in the loose grain sack (15kg Arhar Dal). ",
            "Vegetables in the baskets were found dirty. ",
            "The loose grain rice stock was found infested in the container. ",
            "Fat wastage of mutton and spoiled chicken salami were found inside the display chiller. ",
            "Food pans in the display chiller were not found labeled neither with the date of receiving nor with Use-by-date. ",
            "FEFO Issue: The previously received 20 packs of Golden Harvest Rajma were found in the back room and the recently acquired were found on the retail shelves. ",
            "The food pan containing Whole Chicken was not found labeled with received on date and/or Use-by-date. ",
            "4 packs of Large Brown Eggs (10 eggs each) were found in damaged condition. ",
            "A loose grain stock (Sac of Chana) was found infested in the back room area. ",
            "The packages of Hariyali Tikka and Peri Peri Tikka were found in the open condition. ",
            "1 food pan in raw meat chiller was found without having sticker/label for received on/Use-By- Date. ",
            "The raw meat in storage was found without having the information about Received on and/or Use-by-date. ",
            "Some of the food packages (Pulses) were found air punctured; pin holes were clearly visible. ",
            "Infestation issue observed in one pack of Elina Rice (5kg). ",
            " Leakage (Almost emptied pack of Amul Milk) was found in the dairy chiller.",
            "Infestation observed in Loose Chana Dal (weight - 8kg). ",
            "4 packs of Fresh and Pure Honey were found in the spoiled state. ",
            "4 packs of Dawat Rice (5Kg each) were found infested. ",
            "Potatoes in many of the net bag packs were found in the rotten state.",
            "Papaya Fruit was found in the rotten state. ",
            "2 packs of Tropicana Orange Juice and 4 packs of Tropicana Pineapple Juice (1Ltr each) were found without having MFG/PKD and BEST BEFORE Date on the labels. ",
            "Incorrect labeling: The information about the received on and Use-By-Date was found missing on the labels. The labels on the food pans were found with only a date without having any\n" +
                    "explanation on the same. ",
            "One UV tube was not found in the working condition. ",
            "5 packs of Dawat Rice (5Kg each) were found under the infestation state in the back room area. ",
            "One of the UV tubes of the flycatcher was not found to be in the working condition.\n ",
            "Two UV tubes of the flycatcher were not found in the working condition. ",
            "One of the UV tubes of the flycatcher was not found in the the working condition. ",
            "No rodent box was available in the back area. ",
            "No rodent box was found placed in the back room area. ",
            "One ordinary tube was found plugged in instead of UV tube in the fly catcher. ",
            "No rodent box was available in the store. ",
            "One UV tube of the flycatcher was not found in the working condition. ",
            "One of the UV tubes in the fly catcher was not found in the working condition. ",
            "Four rodent boxes were found without the glue pad. ",
            "One rodent box was found without the glue pad. ",
            "One UV tube was not found in the working condition. Also, the tray was found full of dead flies. ",
            "Flycatcher tray was missing. Moreover, one of the UV tubes was not found in the working condition. ",
            "Rodent box was found without the glue pad. ",
            "One of the UV tubes was not found in the working condition. ",
            "The flycatcher tray was found missing. ",
            "One of the UV tubes in the flycatcher was not found in the working condition. ",
            "The flycatcher was not found in the working condition. ",
            "Rodent box was not found placed in the Fnv section. ",
            "One UV tube was not found in the working condition. ",
            "No rodent box was available. ",
            "Dead Lizard was found in the rodent box. ",
            "A large number of flies were seen in FnV section. ",
            "A dead rodent and insects were found in the rodent box. ",
            "Pest Evidence: A rodent was seen in the grocery section. ",
            "The flycatcher tray was found full of dead flies. ",
            "A dead rat was found in the rodent box. ",
            "The flycatcher tray was found full of dead flies. ",
            "Plenty of dead flies were found in the flycatcher tray. ",
            "A dead rat and insects were found in the rodent boxes. ",
            "A large number of flies were seen in the grocery section. ",
            "Dead flies were found in the flycatcher tray. ",
            "Two flycatcher trays were found full of dead flies. ",
            "The glue pad was found full of dead flies. ",
            "Dead rat was found in the rodent box. ",
            "A large numbers of flies were seen in the meat shop. ",
            "The 3 loose rice containers; Parimal Rice, Mini Mogra and Tibra Rice were found infested. ",
            "3 dead lizards were found in the rodent box. ",
            "8 packs of Basmati Rice (5kg each) were found infested. ",
            "Dead lizards and large number of flying insects were found in the rodent box. ",
            "Infestation Issue observed in 3 packs of Elina Rice and 2 packs of Devaaya Rice. ",
            "The flycatcher was found to be dirty; plenty of dead flies were seen on the frame. ",
            "Glue pad was found missing in the flycatcher. ",
            "The glue pad in flycatcher was found full of dead flies. ",
            "Pest program was not found updated since Sept. 06, 2016. ",
            "Dead flies were found in the flycatcher tray. ",
            "Flycatcher was found without having a glue pad and/or flycatcher tray. ",
            "Cockroaches were seen roving around in the back room area. ",
            "No exceptions found ",
            "Dead lizards and insects were found in two rodent boxes. ",
            "Food handler was wearing a rubber bracelet while on operations. ",
            "15Kg stock of Arhar Daal was found infested in the back room and was stored alongside regular food stock. No identification mark such as dump area/not for sale etc. could be observed by the auditor at the time of the inspection. Auditor was not allowed to take picture. ",
            "A large number of flies were seen in the FnV section.",
            "The knife holder (Magnetic Belt) was found rusted; the rusting marks were also observed on the knives. ",
            "No Exceptions Found. ",
            "Not Applicable. ",
            "FSSAI License was not displayed at the prominent place; it was displayed in the back room. ",
            "There was Flaking/Peeling of walls observed. ",
            "The sliding window panel of the display chiller was dirty. ",
            "Gaskets of the chiller were not properly cleaned. ",
            "The entrance door of the meat shop was dirty. ",
            "The Gaskets of the chiller and freezer were dirty. ",
            "No records for cleaning of chillers & freezers were being maintained. ",
            "Air cutter was not in operation. Fly catcher was installed at a height of above 6ft. ",
            "Flaking/Peeling of paint found on one of the walls close to the display chiller in the Meat shop.  ",
            "Gaskets of the chillers in the Meat shop were dirty. ",
            "Flycatcher was installed at above 7ft. height in the Meat shop. ",
            "Flycatcher was not working in the Back room and was installed at above 7ft. height. ",
            "The iron frames for keeping vegetable baskets were not properly cleaned. ",
            "Gaskets were not properly cleaned in two chillers in the Grocery. ",
            "Vegetable basket frames were dirty in Fruits & Vegetable section. ",
            "Gaskets and Doors were found dirty both in chiller and freezer in the Meat shop.  ",
            "No sanitizer available and only floor cleaning chemical was found in the store.  ",
            "Dirty basket and dirty Mop was placed in the meat shop. ",
            "Flycatcher was not in working condition both in the Meat Shop & Grocery section. ",
            "Gaskets of the Chillers & Freezers were not properly cleaned in the Grocery section. ",
            "The cooling fans of the chillers were dirty in the Grocery section. ",
            "Only one approved chemical was found in the store. ",
            "Only one UV tube was working in the flycatcher both in the Back room & FnV sections. ",
            "Flycatchers were installed at a above 6ft. height in the store. ",
            "Hand Wash Sinks were dirty in the meat shop. ",
            "The floor was found to be dirty in the meat Shop. ",
            "Floors were not properly cleaned in the Grocery, some food material and cart board were lying on the floor. ",
            "The freezer in the meat shop was found to be dirty both from inside and outside. ",
            "Flies were found in the Meat Section. ",
            "Air cutter of the Meat section was not in operation. ",
            "Iron Frames in the Fruits & Vegetable section were not properly cleaned. ",
            "One of the Chillers’ cooling fan was dirty in the Grocery Section. ",
            "The chillers' fan covers & Gaskets were dirty. ",
            "Fly catcher was installed at a height of above 7ft and only one UV tube was working. ",
            "Food Handlers were using Dettol Soap for washing hands in the store. ",
            "The temperature inside the freezer was recorded at -15 Degrees Celsius. ",
            "The Gaskets and the Fan Covers of the chillers were dirty.  ",
            "Hand marks/stains were observed on the glass doors of the chillers. ",
            "Four packets of Chicken Nuggets were found alongside the Veg Products (Frozen Peas) in the freezer. ",
            "Fly Catcher was installed at a height of above 7ft and only one UV tube was in working condition. ",
            "There was rust on the bottom body part of the chillers from inside. ",
            "One of the UV tubes in the flycatcher was not working. ",
            "The Gaskets and the fan covers of both the chillers were dirty. ",
            "No Approved Chemicals were found in the store. ",
            "Floor was dirty; lot of footmarks and the deposition of dirt were observed. ",
            "A dead flying insect was found in one of the chillers.  ",
            "The fan covers of the chillers were dirty. ",
            "Sufficient distance was not maintained between the Iron racks and the walls. ",
            "The merchandizing shelves were attached to the walls.   ",
            "The floor of the chiller zone was not properly cleaned.  ",
            "The fan covers of both chillers were dirty.  ",
            "The merchandizing shelves of the chillers were found to be dirty.  ",
            "The Gaskets of the freezers were not properly cleaned. ",
            "No Approved Chemicals were available in the store. ",
            "Frozen Veg food products like Aloo Tikki, Frozen Peas etc. were placed along with Non-Veg frozen food products in the freezer. ",
            "Packs of Basmati Rice were placed directly on the floor. ",
            "Edible oil was placed alongside the detergent. ",
            "Bad Storage Practices; Food & Non-Food Articles were placed together. ",
            "Both Dairy Chillers were overloaded with Food Stock. ",
            "Approx. 100 packs of frozen Veg Food Products were kept in the chillers. ",
            "Freezer was overloaded with Food Stock. ",
            "Mosquitoes & Flies were found in Grocery & Back Room areas. ",
            "Two Flycatchers were installed in the section but only one was in the working condition. ",
            "4 packs of Toffu Paneer having the date of expiry on 27th November were placed with fresh paneer in the dairy chiller with no flag for identification of near expiry date.  ",
            "2 packs of Yakut LCS Sherohos with Mfg. date 13th October and 40 Days of Best Before were found at the retail counter at the time of the audit. ",
            "3 packs of French Fries (McCain) were found in torn-off condition in the freezer. ",
            "The complete Lot of Cake (Packaged) had date of packaging - November 2015 with Expiry date of January 2015. ",
            "No records of pest control activity in the store were found for the month of November 2015. ",
            "Dead Lizard in the decomposed state was found in the Rodent box."
    };

    //  private ArrayList<String> imagesarray = new ArrayList<>();
    private ArrayList<String> imagesarray;

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
        setHasOptionsMenu(true);

        try {
            getAnswerses = (ArrayList<Answers>) b.getSerializable("DAO_ANS");
        } catch (Exception e) {

        }

        Cat_id = b.getInt("Cat_id");
        category = b.getString("Cat_name");
        Store_id = b.getInt("Store_id");
        store_loc = b.getString("Store_region");
        type = b.getInt("Type");
        Store_name = b.getString("Store_name");
        formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm a").format(Calendar.getInstance().getTime());

        sampleAudits = new ArrayList<Sample_Audit>();
        answerses = new ArrayList<Answers>();

        dbHelper = new DBHelper(ctx, "FoodSafety.db");
        dbHelper.openDataBase();
        DbManager.initializeInstance(dbHelper, ctx);
        DbManager.getInstance().openDatabase();

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
        view = inflater.inflate(R.layout.startauditfragment, container, false);
        insertData();
        setUpView(view);
        restoreToolbar();
        if (getAnswerses != null && getAnswerses.size() > 0)
            setValues(count);
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }
        return view;
    }

    private void insertData() {
        Cursor curs = null;
        String query = "";
        boolean isInserted;

        try {
            query = "SELECT * FROM answer where ans_catid = '" + Cat_id + "' AND ans_storeid = '" + Store_id + "' ";
        } catch (Exception e) {

        }

        DbManager.getInstance().openDatabase();
        curs = DbManager.getInstance().getDetails(query);
        isInserted = curs.moveToFirst();
        Log.d("Inserted", "" + isInserted + ", " + query);

        if (isInserted) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.ANSWER_Cat_id, Cat_id);
            cv.put(DBHelper.ANSWER_Cat, category);
            cv.put(DBHelper.ANSWER_Store_id, Store_id);
            cv.put(DBHelper.ANSWER_Store, Store_name);
            cv.put(DBHelper.ANSWER_Store_reg, store_loc);
            cv.put(DBHelper.ANSWER_DateTime, formattedDate);
            cv.put(DBHelper.ANSWER_Status, "Incomplete");
            cv.put(DBHelper.ANSWER_Type, type);

            DbManager.getInstance().updateDetails(cv, DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Cat_id + "= '" + Cat_id + "' AND " + DBHelper.ANSWER_Store_id +
                    "= '" + Store_id + "'");
        } else {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.ANSWER_Cat_id, Cat_id);
            cv.put(DBHelper.ANSWER_Cat, category);
            cv.put(DBHelper.ANSWER_Store_id, Store_id);
            cv.put(DBHelper.ANSWER_Store, Store_name);
            cv.put(DBHelper.ANSWER_Store_reg, store_loc);
            cv.put(DBHelper.ANSWER_DateTime, formattedDate);
            cv.put(DBHelper.ANSWER_Status, "Started");
            cv.put(DBHelper.ANSWER_Type, type);

            DbManager.getInstance().insertDetails(cv, DBHelper.ANSWER_Tbl_NAME);
        }

    }

    private void setValues(int count) {
        if (count == 1)
            btn_previous.setVisibility(View.INVISIBLE);
        else
            btn_previous.setVisibility(View.VISIBLE);

        question.setText(audit.get(count - 1).getQuestion());

        try {
            if (getAnswerses.get(count - 1).getAnswer_type() == 1)
                rg_yes.setChecked(true);
            else if (getAnswerses.get(count - 1).getAnswer_type() == 2)
                rg_no.setChecked(true);
            else if (getAnswerses.get(count - 1).getAnswer_type() == 3)
                rg_partial.setChecked(true);


        } catch (Exception e) {

        }

        int sample = 0;
        try {
            sample = getAnswerses.get(count - 1).getNo_sample();
            if (sample == 1 && getAnswerses.get(count - 1).getMax_sample() == 0)
                list_sampleno.setAdapter(new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item,
                        getSampleCount(sample)));
            else
                list_sampleno.setSelection(sample - 1);
        } catch (Exception e) {

        }

        try {
            if (sample == 1 && getAnswerses.get(count - 1).getMax_sample() == 0)
                rg_partial.setEnabled(false);
            else
                rg_partial.setEnabled(true);
        } catch (Exception e) {
        }

        try {
            edt_remark.setText(getAnswerses.get(count - 1).getRemark());
        } catch (Exception e) {

        }

        try {
            edt_comment.setText(getAnswerses.get(count - 1).getComment());
        } catch (Exception e) {

        }

        try {
            actions.setText(getAnswerses.get(count - 1).getActions());
        } catch (Exception e) {

        }

        try {
            skip = getAnswerses.get(count - 1).getQues_skip();
        } catch (Exception e) {

        }

        try {
            if (getAnswerses.get(count - 1).getQues_skip().equals("yes"))
                txt_skip.setChecked(true);
            else
                txt_skip.setChecked(false);
        } catch (Exception e) {

        }

        try {
            imagesarray = getAnswerses.get(count - 1).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setAdapter(getAnswerses.get(count - 1).getNo_sample(), getAnswerses.get(count - 1).getSampleAudits());
            isClicked = false;
        } catch (Exception e) {

        }
    }

    private void setUpView(View view) {
        txt_skip = (CheckBox) view.findViewById(R.id.txt_skip);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        question = (TextView) view.findViewById(R.id.question);
        max_num = (TextView) view.findViewById(R.id.max_num);
        audit_date = (TextView) view.findViewById(R.id.audit_date);
        txt_subcat = (TextView) view.findViewById(R.id.txt_subcat);

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
        if (type == 0) {
            rg1.setVisibility(View.GONE);
            lin_audit.setVisibility(View.GONE);
        } else if (type == 1) {
            rg1.setVisibility(View.VISIBLE);
            lin_audit.setVisibility(View.VISIBLE);
        }

        edt_comment = (AutoCompleteTextView) view.findViewById(R.id.edt_comment);
        edt_remark = (AutoCompleteTextView) view.findViewById(R.id.edt_remark);
        ArrayAdapter<String> remarks_data = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1, remarks);
        edt_remark.setAdapter(remarks_data);

        ArrayAdapter<String> comment_data = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1, comments);
        edt_comment.setAdapter(comment_data);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_sample);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new com.itgc.foodsafety.ui.LinearLayoutManager(ctx);
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

                if (isClicked) {
                    audit.get(count - 1).getSample_audits().clear();
                }

                setAdapter(i, audit.get(count - 1).getSample_audits());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setAdapter(audit.get(count - 1).getSample_no(), audit.get(count - 1).getSample_audits());

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

        setData(count);

        AppPrefrences.setStartTime(ctx, getDateTime());

    }


    private void setData(int count) {
        imagesarray = new ArrayList<>();
        if (count == 1) {
            btn_previous.setVisibility(View.INVISIBLE);
            AppPrefrences.setStartTime(ctx, getDateTime());

        } else
            btn_previous.setVisibility(View.VISIBLE);
        question.setText(audit.get(count - 1).getQuestion());
        txt_subcat.setText(category + "/" + audit.get(count - 1).getSubCat());
        list_sampleno.setAdapter(new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item,
                getSampleCount(audit.get(count - 1).getSample_no())));
        int sample_no = getSampleCount(audit.get(count - 1).getSample_no()).get(0);
        if (sample_no == 1 && audit.get(count - 1).getSample_no() == 0) {
            rg_partial.setEnabled(false);
        } else
            rg_partial.setEnabled(true);
        if (audit.get(count - 1).getDiscription() != null || audit.get(count - 1).getDiscription().trim().equalsIgnoreCase("null")) {
            discription_txt.setText(audit.get(count - 1).getDiscription());
            read_more.setVisibility(View.VISIBLE);
        } else read_more.setVisibility(View.GONE);
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

    private void setAdapter(int size, ArrayList<Sample_Audit> sample_audits) {
        setdata(size, sample_audits);

        startAdapter = new AuditStartAdapter(ctx, sample_audits, size);
        mRecyclerView.setAdapter(startAdapter);
    }

    private void setdata(int size, ArrayList<Sample_Audit> sample_audits) {
        for (int i = 0; i < size; i++) {
            Sample_Audit sample_audit = new Sample_Audit();
            sample_audit.setIsclicked(false);
            sample_audit.setSample_count(0);
            sample_audit.setRate_x(0);
            sample_audit.setSample_pos(i);
            sample_audit.setSample_current_rate(0.0f);
            try {
                sample_audits.add(sample_audit);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

                Log.e("SATUS---", CheckPrevious() + "");
                if (AppUtils.checkInternetConnection(ctx)) {
                    isClicked = true;
                    hideKeyboard(btn_next);
                    if (count < audit.size()) {
                        if (AppUtils.isDraft)
                            saveDraftData(count - 1);
                        else
                            saveData(count - 1);
                        count = count + 1;
                        if (answerses.size() >= count) {
                            setArrayData();
                        } else
                            setData(count);
                        restoreToolbar();
                        if (getAnswerses != null && getAnswerses.size() > 0)
                            setValues(count);
                        insertDraft();

                    } else {
                        if (AppUtils.isDraft)
                            saveDraftData(count - 1);
                        else
                            saveData(count - 1);
                        insertDraft();
                        try {
                            if (!AppUtils.isDraft) {
                                ContentValues cv = new ContentValues();
                                cv.put(DBHelper.ANSWER_Cat_id, Cat_id);
                                cv.put(DBHelper.ANSWER_Cat, category);
                                cv.put(DBHelper.ANSWER_Store_id, Store_id);
                                cv.put(DBHelper.ANSWER_Store, Store_name);
                                cv.put(DBHelper.ANSWER_Store_reg, store_loc);
                                cv.put(DBHelper.ANSWER_DateTime, formattedDate);
                                cv.put(DBHelper.ANSWER_Status, "Complete");
                                cv.put(DBHelper.ANSWER_value, serializeObject(answerses));

                                DbManager.getInstance().updateDetails(cv, DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Cat_id + "='" + Cat_id + "' AND " + DBHelper.ANSWER_Store_id +
                                        "='" + Store_id + "' ");

                                for (int i = 0; i < answerses.size(); i++)
                                    Log.d("", "final answerses " + answerses.get(i).getImage());
                            } else {
                                ContentValues cv = new ContentValues();
                                cv.put(DBHelper.ANSWER_Cat_id, Cat_id);
                                cv.put(DBHelper.ANSWER_Cat, category);
                                cv.put(DBHelper.ANSWER_Store_id, Store_id);
                                cv.put(DBHelper.ANSWER_Store, Store_name);
                                cv.put(DBHelper.ANSWER_Store_reg, store_loc);
                                cv.put(DBHelper.ANSWER_DateTime, formattedDate);
                                cv.put(DBHelper.ANSWER_Status, "Complete");
                                cv.put(DBHelper.ANSWER_value, serializeObject(getAnswerses));

                                DbManager.getInstance().updateDetails(cv, DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Cat_id + "='" + Cat_id + "' AND " + DBHelper.ANSWER_Store_id +
                                        "='" + Store_id + "' ");

                                for (int i = 0; i < getAnswerses.size(); i++)
                                    Log.d("", "final getAnswerses " + getAnswerses.get(i).getImage());
                            }
                            Toast.makeText(ctx, "Audit Completed Successfully", Toast.LENGTH_LONG).show();
                            // if (AppUtils.checkInternetConnection(ctx)) {
                            getIntermidietdata();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else Toast.makeText(ctx, "Internet is Not connected", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_camera:
                if (imagesarray.size() < 4)
                    //doTakePhotoAction();
                    checkPermission();
                else
                    Toast.makeText(getActivity(), "Only four images are allowed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_previous:
                isClicked = false;
                hideKeyboard(btn_previous);
                if (count > 1) {
                    count = count - 1;
                    setArrayData();
                    if (getAnswerses != null && getAnswerses.size() > 0)
                        setValues(count);

                } else
                    Toast.makeText(ctx, "You are currently on the first question", Toast.LENGTH_LONG).show();
                restoreToolbar();

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
                getFragmentManager().beginTransaction().replace(R.id.container_body, new WebViewScreen()).
                        addToBackStack("Guidelines").commit();
                break;
            case R.id.img_info1:
                getFragmentManager().beginTransaction().replace(R.id.container_body, new WebViewScreen1()).
                        addToBackStack("Guidelines").commit();
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


    public boolean CheckPrevious() {
        lists = new ArrayList<ArrayList<Answers>>();
        String data1 = "";
        try {
            Cursor curs = null;
            String query = "";

            try {

                query = "SELECT answer_val FROM answer where ans_catid = '" + Cat_id + "' AND ans_storeid ='" + Store_id + "' AND  (ans_status = 'Skipped' OR " +
                        "ans_status = 'Complete')";

            } catch (Exception e) {

            }

            DbManager.getInstance().openDatabase();
            curs = DbManager.getInstance().getDetails(query);
            curs.getCount();
            if (curs != null) {
                if (curs.moveToFirst()) {
                    if (curs.getCount() > 0) {
                        for (int i = 0; i < curs.getCount(); i++) {
                            ArrayList<Answers> answersArrayList = (ArrayList<Answers>) deserializeObject(curs.getBlob(curs.getColumnIndex
                                    (DBHelper.ANSWER_value)));

                            Log.e("ANSWERS--->>", answersArrayList.toString());
                            Log.e("LISTTT--", answersArrayList.toString());

                            Gson gson = new Gson();
                            Previous_data = gson.toJson(answersArrayList);
                            Log.e("", "dataaaaaa1 " + data1);
                            // lists.add(answersArrayList);
                            curs.moveToNext();
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lists.size() == 0) {
            return true;
        } else {
            // submitData(data1);
            return false;
        }

    }

    public void getIntermidietdata() {
        lists = new ArrayList<ArrayList<Answers>>();
        String data1 = "";
        try {
            Cursor curs = null;
            String query = "";

            try {

                query = "SELECT answer_val FROM answer where ans_catid = '" + Cat_id + "' AND ans_storeid ='" + Store_id + "' AND  (ans_status = 'Skipped' OR " +
                        "ans_status = 'Complete')";

            } catch (Exception e) {

            }

            DbManager.getInstance().openDatabase();
            curs = DbManager.getInstance().getDetails(query);
            curs.getCount();
            if (curs != null) {
                if (curs.moveToFirst()) {
                    if (curs.getCount() > 0) {
                        for (int i = 0; i < curs.getCount(); i++) {
                            ArrayList<Answers> answersArrayList = (ArrayList<Answers>) deserializeObject(curs.getBlob(curs.getColumnIndex
                                    (DBHelper.ANSWER_value)));

                            Log.e("ANSWERS--->>", answersArrayList.toString());
                            Log.e("LISTTT--", answersArrayList.toString());

                            Gson gson = new Gson();
                            data1 = gson.toJson(answersArrayList);
                            Log.e("", "dataaaaaa1 " + data1);
                            // lists.add(answersArrayList);
                            curs.moveToNext();
                        }
                    }
                }

                submitData(data1);
            } else
                Toast.makeText(ctx, "No Audit has been completed", Toast.LENGTH_LONG).show();

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

    private void submitData(final String data1) {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        StringRequest str = new StringRequest(Request.Method.POST,
                Vars.BASE_URL + Vars.SUBMIT_REPORT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("SUBMIT_RESPONCE---", response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject payload = jsonObject.getJSONObject("Payload");
                        String msg = jsonObject.getString("Message");

                        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();

                        AppUtils.encodedimage = "";
                        AppPrefrences.setAuditId(ctx, payload.getString("audit_id"));
                        String Code = jsonObject.getString("Code");
                        if (!Code.equalsIgnoreCase("ok")) {
                            deleteData();
                            Toast.makeText(ctx, "Error! Please ReSubmit", Toast.LENGTH_SHORT).show();
                        }
                        // deleteData();
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
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError v) {
                v.printStackTrace();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(ctx, "Failed", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", data1);
//              params.put("audit_by", auditerName);
                params.put("audit_id", AppPrefrences.getAuditId(ctx));
//              params.put("audit_no", auditerContactNumber);
                params.put("cat_id", Cat_id + "");
                params.put("audit_sign", "");
                params.put("final_submit", "false");
                params.put("store_sign", "");
                params.put("audit_contact", "");
                params.put("auditor_id", AppPrefrences.getUserId(ctx));
                params.put("lat", AppPrefrences.getLatitude(ctx));
                params.put("long", AppPrefrences.getLongitude(ctx));
                params.put("startdateTime", AppPrefrences.getStartTime(ctx));
                params.put("enddatetime", getDateTime());
                params.put("store_id", String.valueOf(Store_id));
                params.put("expiry_question", "0");
                return params;
            }
        };

        str.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        str.setShouldCache(false);
        MySingleton.getInstance(ctx).addToRequestQueue(str);
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
                            getFragmentManager().beginTransaction().replace(R.id.container_body, new DraftsFragment())
                                    .addToBackStack("Drafts").commit();
                        } else {
                            insertDraft();
                            Intent intent = new Intent("DraftsCount");
                            ctx.sendBroadcast(intent);
                            Fragment fragment = new StartAuditFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Store_id", Store_id);
                            bundle.putString("Store_name", Store_name);
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.container_body, fragment).addToBackStack("Store")
                                    .commit();
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

    private void setArrayData() {
        imagesarray = new ArrayList<>();
        try {
            if (count == 1)
                btn_previous.setVisibility(View.INVISIBLE);
            else
                btn_previous.setVisibility(View.VISIBLE);

            txt_subcat.setText(category + "/" + audit.get(count - 1).getSubCat());
            question.setText(audit.get(count - 1).getQuestion());

            if (answerses.get(count - 1).getAnswer_type() == 1)
                rg_yes.setChecked(true);
            else if (answerses.get(count - 1).getAnswer_type() == 2)
                rg_no.setChecked(true);
            else if (answerses.get(count - 1).getAnswer_type() == 3)
                rg_partial.setChecked(true);

            int sample = answerses.get(count - 1).getNo_sample();
            if (sample == 1 && answerses.get(count - 1).getMax_sample() == 0)
                list_sampleno.setAdapter(new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item,
                        getSampleCount(sample)));
            else
                list_sampleno.setSelection(sample - 1);

            if (sample == 1 && answerses.get(count - 1).getMax_sample() == 0)
                rg_partial.setEnabled(false);
            else
                rg_partial.setEnabled(true);

            edt_remark.setText(answerses.get(count - 1).getRemark());
            edt_comment.setText(answerses.get(count - 1).getComment());
            actions.setText(answerses.get(count - 1).getActions());

            skip = answerses.get(count - 1).getQues_skip();
            if (answerses.get(count - 1).getQues_skip().equals("yes"))
                txt_skip.setChecked(true);
            else
                txt_skip.setChecked(false);

            try {
                imagesarray = answerses.get(count - 1).getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                setAdapter(answerses.get(count - 1).getNo_sample(), answerses.get(count - 1).getSampleAudits());
                isClicked = false;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveData(int i) {
        Answers answers = new Answers();
        try {
            answers.setStore_id(Store_id);
            answers.setCat_id(Cat_id);
            answers.setQuestion_id(audit.get(i).getQuestion_id());
            answers.setAnswer_type(checkRadio());
            answers.setNo_sample(sample_selected);
            answers.setMax_no(Integer.valueOf(max_num.getText().toString()));
            answers.setRemark(edt_remark.getText().toString());
            answers.setComment(edt_comment.getText().toString());
            answers.setActions(actions.getText().toString());

            answers.setSampleAudits(audit.get(i).getSample_audits());
            answers.setMax_sample(audit.get(i).getSample_no());
            answers.setSubcat_id(audit.get(i).getSubcategory_id());
            answers.setQues_skip(skip);
            answers.setCat_skip("no");

            answers.setImage(imagesarray);

            answers.setType(type);
            if (answerses.size() > i)
                answerses.remove(i);

            // imagesarray.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

        answerses.add(i, answers);
        // imagesarray.clear();
        edt_comment.setText("");
        edt_remark.setText("");
        actions.setText("");
        skip = "no";
        encodedImage = "";
        txt_skip.setChecked(false);
        rg_yes.setChecked(true);
    }

    private void saveDraftData(int i) {
        Answers answers = new Answers();
        try {
            answers.setStore_id(Store_id);
            answers.setCat_id(Cat_id);
            answers.setQuestion_id(audit.get(i).getQuestion_id());
            answers.setAnswer_type(checkRadio());
            answers.setNo_sample(sample_selected);
            answers.setMax_no(Integer.valueOf(max_num.getText().toString()));
            answers.setRemark(edt_remark.getText().toString());
            answers.setComment(edt_comment.getText().toString());
            answers.setActions(actions.getText().toString());
            answers.setSampleAudits(audit.get(i).getSample_audits());
            answers.setMax_sample(audit.get(i).getSample_no());
            answers.setSubcat_id(audit.get(i).getSubcategory_id());
            answers.setQues_skip(skip);
            answers.setCat_skip("no");

            answers.setImage(imagesarray);
            Log.e("IMAGE_SIZE11", imagesarray.size() + "");
            answers.setType(type);
            if (getAnswerses.size() > i)
                getAnswerses.remove(i);
            // imagesarray.clear();


        } catch (Exception e) {
            e.printStackTrace();
        }
        getAnswerses.add(i, answers);
        // imagesarray.clear();
        edt_comment.setText("");
        edt_remark.setText("");
        actions.setText("");
        skip = "no";
        encodedImage = "";
        txt_skip.setChecked(false);
        rg_yes.setChecked(true);
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
            checked = 1;
        } else if (rg_no.isChecked()) {
            checked = 2;
        } else {
            checked = 3;
        }
        return checked;
    }

    private void doTakePhotoAction() {
//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        fileUri = Uri.fromFile(photoFile);
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        if (photoFile != null) {
//            AppPreferences.setPath(getActivity(), photoFile.getAbsolutePath());
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                takePictureIntent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent,
                        LOAD_CAMERA_RESULTS);
            }
        }

    }

    private File createImageFile() throws IOException {


        File dir = new File(Environment.getExternalStorageDirectory(), "FoodSafety");
        if (!dir.exists())
            dir.mkdir();
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

    public class SaveImgTask extends AsyncTask<Bitmap, Void, String> {

        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ctx);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd != null)
                pd.dismiss();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            storeImage(params[0]);
            return null;
        }


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
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                byte[] dataOfImage = bos.toByteArray();
                encodedImage = Base64.encodeToString(dataOfImage, Base64.DEFAULT);
                imagesarray.add(encodedImage);
                encodedImage = "";
                int imsize = 0;
                imsize = imagesarray.size();
                Toast.makeText(getContext(), imsize + "/4 Images Added", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == GALLERY_IMG && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();

                String imageUrl = FilePathUtils.getPath(getActivity(), uri);
                Bitmap scalledBitmap = BitmapHelper.decodeSampledBitmapFromResource(imageUrl, 400, 400); //scall the bitmap into given size
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                scalledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
                byte[] dataOfImage = bos.toByteArray();
                encodedImage = Base64.encodeToString(dataOfImage, Base64.DEFAULT);
                imagesarray.add(encodedImage);
                encodedImage = "";
                int imsize = 0;
                imsize = imagesarray.size();
                Toast.makeText(getContext(), imsize + "/4 Images Added", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == LOAD_CAMERA_RESULTS
                && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
            if (photoFile != null) {
                Log.d("", "imagefilepath notcrop " + photoFile.getAbsolutePath());
                try {
                   // Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    Bitmap photo = BitmapHelper.decodeSampledBitmapFromResource(photoFile.getAbsolutePath(), 350, 400); //scall the bitmap into given size

                    if (photo != null) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.JPEG, 60, bos);
                        byte[] dataOfImage = bos.toByteArray();
                        encodedImage = Base64.encodeToString(dataOfImage, Base64.DEFAULT);
                        imagesarray.add(encodedImage);
                        encodedImage = "";
                        int imsize = 0;
                        imsize = imagesarray.size();
                        Toast.makeText(getContext(), imsize + "/4 Images Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Unable to save", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(fileUri);
        getActivity().sendBroadcast(mediaScanIntent);

    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
        FragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void restoreToolbar() {
        MainActivity.mToolbar.setVisibility(View.GONE);
        txt_title.setText("Question No." + count);
    }

    private void insertDraft() {
        try {
            if (!AppUtils.isDraft) {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.ANSWER_Cat_id, Cat_id);
                cv.put(DBHelper.ANSWER_Cat, category);
                cv.put(DBHelper.ANSWER_Store_id, Store_id);
                cv.put(DBHelper.ANSWER_Store, Store_name);
                cv.put(DBHelper.ANSWER_Store_reg, store_loc);
                cv.put(DBHelper.ANSWER_DateTime, formattedDate);
                cv.put(DBHelper.ANSWER_Status, "Incomplete");
                cv.put(DBHelper.ANSWER_Type, type);
                cv.put(DBHelper.ANSWER_value, serializeObject(answerses));
                cv.put(DBHelper.ANSWER_Draft_value, serializeObject(audit));

                Log.e("SAVED_ANSWERS---->", answerses.toString());
                DbManager.getInstance().updateDetails(cv, DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Cat_id + "='" + Cat_id + "' AND " + DBHelper.ANSWER_Store_id + "='" + Store_id + "' ");
            } else {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.ANSWER_Cat_id, Cat_id);
                cv.put(DBHelper.ANSWER_Cat, category);
                cv.put(DBHelper.ANSWER_Store_id, Store_id);
                cv.put(DBHelper.ANSWER_Store, Store_name);
                cv.put(DBHelper.ANSWER_Store_reg, store_loc);
                cv.put(DBHelper.ANSWER_DateTime, formattedDate);
                cv.put(DBHelper.ANSWER_Status, "Incomplete");
                cv.put(DBHelper.ANSWER_Type, type);
                cv.put(DBHelper.ANSWER_value, serializeObject(getAnswerses));
                cv.put(DBHelper.ANSWER_Draft_value, serializeObject(audit));

                Log.e("SAVED_ANSWERS2---->", getAnswerses.toString());
                DbManager.getInstance().updateDetails(cv, DBHelper.ANSWER_Tbl_NAME, DBHelper.ANSWER_Cat_id + "='" + Cat_id + "' AND " + DBHelper.ANSWER_Store_id + "='" + Store_id + "' ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    mRecyclerView.setVisibility(View.GONE);
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

    public void checkPermission() {


        if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            showOption();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "These permissions requared for accessing you gallery and camera.", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PICK_IMAGE_REQUEST_PERMISSION);
        }
    }

    private void showOption() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("Add photo using");
        dialog.setPositiveButton("Camera",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                        d.dismiss();
                    }
                });

        dialog.setNeutralButton("Gallery",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGallery();
                    }
                });

        dialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        d.dismiss();
                    }
                });
        d = dialog.create();
        d.show();
    }

    void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Photo"), GALLERY_IMG);
    }


}