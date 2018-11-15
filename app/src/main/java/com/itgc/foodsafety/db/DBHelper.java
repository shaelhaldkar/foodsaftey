package com.itgc.foodsafety.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author root
 */
public class DBHelper extends SQLiteOpenHelper {

    //public final static String DATABASE_NAME = "Android/data/com.itgc.iorderfresh/" + "IOF.sqlite";
    //private final static String DATABASE_NAME = "IOF.sqlite";
    //private final static String DATABASE_NAME = "iof_database";

    private static final int DATABASE_VERSION = 1;


    public static final String CAT_Tbl_NAME = "category";
    public static final String CAT_id = "id";
    public static final String CAT_NAME = "categoryName";
    public static final String CAT_STATUS = "status";
    public static final String CAT_CREATEDBY = "createdBy";
    public static final String CAT_CREATEDON = "createdOn";
    public static final String CAT_UPDATEDBY = "updatedBy";
    public static final String CAT_UPDATEDON = "updatedOn";

    public static final String SUBCAT_Tbl_NAME = "subCategory";
    public static final String SUBCAT_id = "id";
    public static final String SUBCAT_CAT_id = "categoryId";
    public static final String SUBCAT_NAME = "subCategoryName";
    public static final String SUBCAT_STATUS = "status";
    public static final String SUBCAT_CREATEDBY = "createdBy";
    public static final String SUBCAT_CREATEDON = "createdOn";
    public static final String SUBCAT_UPDATEDBY = "updatedBy";
    public static final String SUBCAT_UPDATEDON = "updatedOn";

    public static final String QUES_Tbl_NAME = "questions";
    public static final String QUES_id = "id";
    public static final String QUES_SUBCAT_id = "subCategoryId";
    public static final String QUES_TEXT = "questionText";
    public static final String QUES_ANSWERYES = "answerValuesForYes";
    public static final String QUES_ANSWERNO = "answerValuesForNo";
    public static final String QUES_STATUS = "status";
    public static final String QUES_CREATEDBY = "createdBy";
    public static final String QUES_CREATEDON = "createdOn";
    public static final String QUES_UPDATEDBY = "updatedBy";
    public static final String QUES_UPDATEDON = "updatedOn";
    public static final String QUES_NO_SAMPLE = "numberOfSamples";
    public static final String QUES_DISCRIPTION = "discription";

    public static final String USER_Tbl_NAME = "user";
    public static final String USER_id = "id";
    public static final String USER_NAME = "uName";
    public static final String USER_OWNER_NAME = "ownerName";
    public static final String USER_PASS = "password";
    public static final String USER_BUSS_CAT = "businessCategory";
    public static final String USER__LICENSE = "licenceNo";
    public static final String USER_SHOP_NAME = "shopName";
    public static final String USER_EMAIL = "email";
    public static final String USER_MOBILE_NO = "mobileNumber";
    public static final String USER__PHONE_NO = "phoneNumber";
    public static final String USER_CORP_ADD = "corporateAddress";
    public static final String USER_PINCODE = "areaPinCode";
    public static final String USER_STATUS = "status";
    public static final String USER_CREATEDBY = "createdBy";
    public static final String USER_CREATEDON = "createdOn";
    public static final String USER_UPDATEDBY = "updatedBy";
    public static final String USER_UPDATEDON = "updatedOn";
    public static final String USER_REMEMBERME = "rememberMe";
    public static final String USER_DEVICEID = "deviceId";
    public static final String USER_DEVICETYPE = "deviceType";
    public static final String USER_LATITUDE = "latitude";
    public static final String USER_LONGITUDE = "longitude";
    public static final String USER_LAST_LAT = "lastLatitude";
    public static final String USER_LAST_LON = "lastLongitude";
    public static final String USER_ROLEID = "roleId";

    public static final String BRANCH_Tbl_NAME = "branch";
    public static final String BRANCH_id = "id";
    public static final String BRANCH_USER_ID = "userId";
    public static final String BRANCH_MANG_NAME = "branchManagerName";
    public static final String BRANCH_EMAIL = "email";
    public static final String BRANCH_BRANCH_ID = "branchId";
    public static final String BRANCH_PHONE = "phone";
    public static final String BRANCH_ADDRESS = "address";
    public static final String BRANCH_AREA_PINCODE = "areaPinCode";
    public static final String BRANCH_STATUS = "status";
    public static final String BRANCH_CREATEDBY = "createdBy";
    public static final String BRANCH_CREATEDON = "createdOn";
    public static final String BRANCH_UPDATEBY = "updatedBy";
    public static final String BRANCH_UPDATEON = "updatedOn";

    public static final String AUDIT_Tbl_NAME = "audits";
    public static final String AUDIT_id = "id";
    public static final String AUDIT_CAT_ID = "categoryId";
    public static final String AUDIT_SUBCAT_ID = "subCategoryId";
    public static final String AUDIT_QUES_ID = "questionsId";
    public static final String AUDIT_ANS_TYPE = "answerType";
    public static final String AUDIT_NO_SAMPLE = "numberOfSample";
    public static final String AUDIT_TOTAL_SAMPLE = "totalSampleValues";
    public static final String AUDIT_COMMENT = "comments";
    public static final String AUDIT_REMARK = "remark";
    public static final String AUDIT_IMAGE = "image";
    public static final String AUDIT_CREATEDBY = "createdBy";
    public static final String AUDIT_CREATEDON = "createdOn";
    public static final String AUDIT_ACTIONS = "actions";

    public static final String AUDIT_SAMPLE_Tbl_NAME = "auditsSample";
    public static final String AUDIT_SAMPLE_id = "id";
    public static final String AUDIT_SAMPLE_AUDIT_id = "auditsId";
    public static final String AUDIT_SAMPLE_QUES_SAMPLE_id = "questionSampleId";
    public static final String AUDIT_SAMPLE_QUES_SAMPLE_VALUE = "questionSampleValue";

    public static final String BUSS_CAT_Tbl_NAME = "businessCategory";
    public static final String BUSS_id = "id";
    public static final String BUSS_CAT_NAME = "businessCategoryName";
    public static final String BUSS_STATUS = "status";
    public static final String BUSS_CREATEDBY = "createdBy";
    public static final String BUSS_CREATEDON = "createdOn";
    public static final String BUSS_UPDATEDBY = "updatedBy";
    public static final String BUSS_UPDATEDON = "updatedOn";

    public static final String ANSWER_Tbl_NAME = "answer";
    public static final String ANSWER_id = "id";
    public static final String ANSWER_Cat_id = "ans_catid";
    public static final String ANSWER_Store_id = "ans_storeid";
    public static final String ANSWER_Store = "ans_store";
    public static final String ANSWER_Store_reg = "ans_store_reg";
    public static final String ANSWER_value = "answer_val";
    public static final String ANSWER_Draft_value = "answer_draft_val";
    public static final String ANSWER_DateTime = "ans_datetime";
    public static final String ANSWER_Status = "ans_status";
    public static final String ANSWER_Cat = "ans_category";
    public static final String ANSWER_Type = "ans_type";


    private final String TABLE_ANSWER_CREATE = "CREATE TABLE " + ANSWER_Tbl_NAME + " (" + ANSWER_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ANSWER_Cat_id + " INTEGER, " + ANSWER_value + " BLOB, " + ANSWER_Draft_value + " BLOB, " + ANSWER_DateTime + " TEXT, "
            + ANSWER_Status + " TEXT, " + ANSWER_Store_id + " INTEGER, " + ANSWER_Store + " TEXT, " + ANSWER_Store_reg + " TEXT, " + ANSWER_Type + " INTEGER, " +
            ANSWER_Cat + " TEXT); ";


    private final String TABLE_Category_CREATE = "CREATE TABLE " + CAT_Tbl_NAME
            + " (" + CAT_id + " INTEGER PRIMARY KEY , " + CAT_NAME
            + " TEXT, " + CAT_STATUS
            + " TEXT, " + CAT_CREATEDBY + " INTEGER, " + CAT_CREATEDON + " DATE, " + CAT_UPDATEDBY + " INTEGER, "
            + CAT_UPDATEDON + " DATE); ";


    private final String TABLE_SubCategory_CREATE = "CREATE TABLE " + SUBCAT_Tbl_NAME
            + " (" + SUBCAT_id + " INTEGER PRIMARY KEY , " + SUBCAT_CAT_id + " INTEGER, " + SUBCAT_NAME
            + " TEXT, " + SUBCAT_STATUS + " TEXT, " + SUBCAT_CREATEDBY + " INTEGER, " + SUBCAT_CREATEDON + " DATE, " +
            SUBCAT_UPDATEDBY + " INTERGER, " + SUBCAT_UPDATEDON + " DATE); ";


    private final String TABLE_Questions_CREATE = "CREATE TABLE " + QUES_Tbl_NAME + " (" + QUES_id + " INTEGER PRIMARY KEY , " +
            QUES_SUBCAT_id + " INTEGER, " +
            QUES_TEXT + " TEXT, " + QUES_ANSWERYES + " INTEGER, " + QUES_ANSWERNO + " INTEGER, " + QUES_STATUS + " TEXT, " +
            QUES_CREATEDBY + " INTEGER, " + QUES_CREATEDON + " DATE, " + QUES_UPDATEDBY + " INTEGER, " + QUES_UPDATEDON + " DATE, "
            + QUES_DISCRIPTION + " TEXT, " +
            QUES_NO_SAMPLE + " INTEGER); ";


    private final String TABLE_Audit_CREATE = "CREATE TABLE " + AUDIT_Tbl_NAME + " (" + AUDIT_id + " INTEGER PRIMARY KEY , " +
            AUDIT_CAT_ID + " INTEGER, " +
            AUDIT_SUBCAT_ID + " INTEGER, " +
            AUDIT_QUES_ID + " INTEGER, " +
            AUDIT_ANS_TYPE + " INTEGER, " + AUDIT_NO_SAMPLE + " INTEGER, " + AUDIT_TOTAL_SAMPLE + " INTEGER, " +
            AUDIT_COMMENT + " TEXT, " + AUDIT_REMARK + " TEXT, " + AUDIT_IMAGE + " BLOB, " + AUDIT_CREATEDBY + " INTEGER," +
            AUDIT_ACTIONS + " TEXT," +
            AUDIT_CREATEDON + " DATE); ";


    private final String TABLE_AuditSample_CREATE = "CREATE TABLE " + AUDIT_SAMPLE_Tbl_NAME + " (" + AUDIT_SAMPLE_id + " INTEGER PRIMARY KEY , " +
            AUDIT_SAMPLE_AUDIT_id + " INTEGER, " +
            AUDIT_SAMPLE_QUES_SAMPLE_id + " INTEGER, " +
            AUDIT_SAMPLE_QUES_SAMPLE_VALUE + " TEXT); ";


    private final String TABLE_BusinessCategory_CREATE = "CREATE TABLE " + BUSS_CAT_Tbl_NAME + " (" + BUSS_id + " INTEGER PRIMARY KEY, " +
            BUSS_CAT_NAME + " TEXT, " + BUSS_STATUS + " TEXT, " + BUSS_CREATEDBY + " INTEGER, " + BUSS_CREATEDON + " DATE, " +
            BUSS_UPDATEDBY + " INTEGER, " + BUSS_UPDATEDON + " DATE); ";


    private final String TABLE_Branch_CREATE = "CREATE TABLE " + BRANCH_Tbl_NAME + " (" + BRANCH_id + " INTEGER PRIMARY KEY, " +
            BRANCH_USER_ID + " INTEGER, " +
            BRANCH_MANG_NAME + " TEXT, " + BRANCH_EMAIL + " TEXT," + BRANCH_BRANCH_ID + " INTEGER, " +
            BRANCH_PHONE + " INTEGER, " + BRANCH_ADDRESS + " TEXT, " + BRANCH_AREA_PINCODE + " INTEGER, " +
            BRANCH_STATUS + " TEXT, " + BRANCH_CREATEDBY + " INTEGER, " + BRANCH_CREATEDON + " DATE, " +
            BRANCH_UPDATEBY + " INTEGER, " + BRANCH_UPDATEON + " DATE); ";


    private final String TABLE_User_CREATE = "CREATE TABLE " + USER_Tbl_NAME + " (" + USER_id + " INTEGER PRIMARY KEY, " +
            USER_NAME + " TEXT, " + USER_OWNER_NAME + " TEXT, " + USER_PASS + " TEXT, " + USER_BUSS_CAT + " TEXT, " +
            USER__LICENSE + " INTEGER, " + USER_SHOP_NAME + " TEXT, " + USER_EMAIL + " TEXT, " + USER_MOBILE_NO + " INTEGER, " +
            USER__PHONE_NO + " INTEGER, " + USER_CORP_ADD + " TEXT, " + USER_PINCODE + " INTEGER, " + USER_STATUS + " TEXT, " +
            USER_CREATEDBY + " INTEGER, " + USER_CREATEDON + " DATE, " + USER_UPDATEDBY + " INTEGER, " + USER_UPDATEDON + " DATE, " +
            USER_REMEMBERME + " INTEGER, " + USER_DEVICEID + " INTEGER, " + USER_DEVICETYPE + " TEXT, " + USER_LATITUDE + " INTEGER, " +
            USER_LONGITUDE + " INTEGER, " + USER_LAST_LAT + " INTEGER, " + USER_LAST_LON + " INTEGER, " + USER_ROLEID + " INTEGER);  ";


    private final String TABLE_Category_DROP = "DROP TABLE IF EXISTS " +  CAT_Tbl_NAME;

    private final String TABLE_SubCategory_DROP = "DROP TABLE IF EXISTS " + SUBCAT_Tbl_NAME;

    private final String TABLE_Questions_DROP = "DROP TABLE IF EXISTS " + QUES_Tbl_NAME;

    private final String TABLE_Audit_DROP = "DROP TABLE IF EXISTS " + AUDIT_Tbl_NAME;

    private final String TABLE_AuditSample_DROP = "DROP TABLE IF EXISTS " + AUDIT_SAMPLE_Tbl_NAME;

    private final String TABLE_BusinessCategory_DROP = "DROP TABLE IF EXISTS " + BUSS_CAT_Tbl_NAME;

    private final String TABLE_Branch_DROP = "DROP TABLE IF EXISTS " + BRANCH_Tbl_NAME;

    private final String TABLE_User_DROP = "DROP TABLE IF EXISTS " + USER_Tbl_NAME;

    private final String TABLE_Answer_DROP = "DROP TABLE IF EXISTS " + ANSWER_Tbl_NAME;

    private SQLiteDatabase mDb;
    private final Context mContext;

    public DBHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        getWritableDatabase();
        mContext = context;
    }

    public boolean openDataBase() throws SQLException {
        mDb = this.getWritableDatabase();
        return mDb.isOpen();
    }

    @Override
    public synchronized void close() {
        if (mDb != null)
            mDb.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_Category_CREATE);
        db.execSQL(TABLE_SubCategory_CREATE);
        db.execSQL(TABLE_Questions_CREATE);
        db.execSQL(TABLE_Audit_CREATE);
        db.execSQL(TABLE_AuditSample_CREATE);
        db.execSQL(TABLE_BusinessCategory_CREATE);
        db.execSQL(TABLE_Branch_CREATE);
        db.execSQL(TABLE_User_CREATE);
        db.execSQL(TABLE_ANSWER_CREATE);

        db.execSQL(TABLE_STORE_INFO_CREATE);
        db.execSQL(TABLE_STORE_SIGNATUTE_CREATE);
        db.execSQL(TABLE_CATEGORY_CREATE);
        db.execSQL(TABLE_QUESTION_CREATE);
        db.execSQL(TABLE_QUESTION_ANSWER_CREATE);
        db.execSQL(TABLE_QUESTION_ANSWER_IMAGE_CREATE);
        db.execSQL(TABLE_QUESTION_ANSWER_PATH_CREATE);
        db.execSQL(TABLE_SAMPLE_CREATE);
        db.execSQL(TABLE_STORE_DETAILSE_CREATE);
        db.execSQL(TABLE_STORE_START_DATE_TIME_CREATE);
        Log.d("Table created", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_Category_DROP);
        db.execSQL(TABLE_SubCategory_DROP);
        db.execSQL(TABLE_Questions_DROP);
        db.execSQL(TABLE_Audit_DROP);
        db.execSQL(TABLE_AuditSample_DROP);
        db.execSQL(TABLE_BusinessCategory_DROP);
        db.execSQL(TABLE_Branch_DROP);
        db.execSQL(TABLE_User_DROP);
        db.execSQL(TABLE_Answer_DROP);

        db.execSQL(TABLE_STORE_INFO_DROP);
        db.execSQL(TABLE_STORE_SIGNATURE_DROP);
        db.execSQL(TABLE_CATEGORY_DROP);
        db.execSQL(TABLE_QUESTION_DROP);
        db.execSQL(TABLE_QUESTION_ANSWER_DROP);
        db.execSQL(TABLE_QUESTION_ANSWER_IMAGE_DROP);
        db.execSQL(TABLE_QUESTION_ANSWER_PATh_DROP);
        db.execSQL(TABLE_SAMPLE_DROP);
        db.execSQL(TABLE_STORE_DETAILS_DROP);
        db.execSQL(TABLE_STORE_START_DATE_TIME_DROP);

        onCreate(db);
    }

    // For Store Information
    public static final String STORE_INFO_TBL_NAME = "storeInfo";
    public static final String STORE_ID = "storeId";
    public static final String STORE_NAME = "storeName";
    public static final String STORE_MARCHANT_ID = "storeMarchantId";
    public static final String STORE_REGION = "storeRegion";
    public static final String AUDIT_CODE="auditcode";
    public static final String AUDIT_DATE="auditdate";
    public static final String LATITUDE="lat";
    public static final String LONGITUDE="long";
    public static final String MERCHANT_NAME="merchantname";

    private final String TABLE_STORE_INFO_CREATE = "CREATE TABLE " + STORE_INFO_TBL_NAME + " (" + STORE_ID + " INTEGER,"+ STORE_NAME + " TEXT," + STORE_MARCHANT_ID +" INTEGER," + AUDIT_CODE + " TEXT," + AUDIT_DATE +" TEXT," + LATITUDE + " TEXT," + LONGITUDE + " TEXT," + MERCHANT_NAME + " TEXT," + STORE_REGION + " TEXT)";
    private final String TABLE_STORE_INFO_DROP = "DROP TABLE IF EXISTS " + STORE_INFO_TBL_NAME;


    // For Store Signature Information
    public static final String STORE_SIGNATURE_TBL_NAME = "storeSignature";
    public static final String STORE_SIGNATURE_IMAGE = "storeSignatureImage";
    public static final String AUDIOTR_SIGNATURE_IMAGE = "auditorSignatureImage";
    public static final String EXPIRY_QUESTION = "expiry_question";

    private final String TABLE_STORE_SIGNATUTE_CREATE = "CREATE TABLE " + STORE_SIGNATURE_TBL_NAME + " (" + STORE_ID + " INTEGER,"+ CATEGORY_ID + " INTEGER,"+ STORE_SIGNATURE_IMAGE + " TEXT,"+ AUDIOTR_SIGNATURE_IMAGE + " TEXT,"+ EXPIRY_QUESTION + " TEXT" +")";
    private final String TABLE_STORE_SIGNATURE_DROP = "DROP TABLE IF EXISTS " + STORE_SIGNATURE_TBL_NAME;


    // For Category Information
    public static final String CATEGORY_TBL_NAME = "categoryInfo";
    public static final String CATEGORY_ID = "categoryId";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String CATEGORY_TYPE = "categoryType";
    public static final String CATEGORY_STATUS = "categoryStatus";
    public static final String CATEGORY_START_DATE = "categoryStartDate";
    public static final String CATEGORY_END_DATE = "categoryEndDate";


    private final String TABLE_CATEGORY_CREATE = "CREATE TABLE " + CATEGORY_TBL_NAME + " (" + STORE_ID + " INTEGER,"+ CATEGORY_ID + " INTEGER,"+ CATEGORY_NAME + " TEXT,"+
                                                                                              CATEGORY_TYPE + " INTEGER,"+ CATEGORY_STATUS + " TEXT,"+ CATEGORY_START_DATE + " TEXT,"+ CATEGORY_END_DATE + " TEXT" +")";
    private final String TABLE_CATEGORY_DROP = "DROP TABLE IF EXISTS " + CATEGORY_TBL_NAME;


    // For Category Question
    public static final String QUESTION_TBL_NAME = "questionInfo";
    public static final String QUESTION_ID="quesId";
    public static final String QUESTION_SUB_CAT_ID="subCategoryId";
    public static final String QUESTION_SUB_CAT_NAME="subCategoryName";
    public static final String QUESTION_TEXT="questionText";
    public static final String QUESTION_DESC="questionDesc";
    public static final String QUESTION_SAMPLES="numberOfSamples";
    public static final String QUESTION_TYPE="questiontype";
    public static final String VERSION="version";

    private final String TABLE_QUESTION_CREATE = "CREATE TABLE " + QUESTION_TBL_NAME + " (" + STORE_ID + " INTEGER," + CATEGORY_ID + " INTEGER,"+ QUESTION_ID + " INTEGER,"+ QUESTION_SUB_CAT_ID + " INTEGER,"+
                                                                    QUESTION_SUB_CAT_NAME + " TEXT,"+ QUESTION_TEXT + " TEXT,"+ QUESTION_DESC + " TEXT,"+ QUESTION_TYPE + " TEXT,"+ VERSION +" TEXT,"+ QUESTION_SAMPLES + " TEXT" +")";
    private final String TABLE_QUESTION_DROP = "DROP TABLE IF EXISTS " + QUESTION_TBL_NAME;


    // For Category Question Answers
    public static final String ANSWER_TBL_NAME="answerInfo";
    public static final String ANSWER_COMMENT="comment";
    public static final String ANSWER_REMARK="remark";
    public static final String ANSWER_ACTION="actions";
    public static final String ANSWER_DATETIME="answerDateTime";
    public static final String ANSWER_TYPE="answer_type";
    public static final String ANSWER_CAT_SKIP="cat_skip";
    public static final String ANSWER_IS_SEEN="isSeen";
    public static final String ANSWER_MAX_NO="max_no";
    public static final String ANSWER_MAX_SAMPLE="max_sample";
    public static final String ANSWER_NO_SAMPLE="no_sample";
    public static final String ANSWER_QUES_SKIP="ques_skip";
    public static final String ANSWER_SUBCAT_ID="subcat_id";
    public static final String ANSWER_CAT_TYPE="type";
    public static final String SEC_EXISTS="secexists";
    public static final String QUESTION_FAIL="questionfail";

    private final String TABLE_QUESTION_ANSWER_CREATE = "CREATE TABLE " + ANSWER_TBL_NAME + " (" +  STORE_ID + " INTEGER," + CATEGORY_ID + " INTEGER,"+
                                                                                                    QUESTION_ID + " INTEGER,"+ ANSWER_SUBCAT_ID + " INTEGER,"+
                                                                                                    ANSWER_COMMENT + " TEXT,"+ ANSWER_REMARK + " TEXT,"+ ANSWER_ACTION + " TEXT,"+
                                                                                                    ANSWER_DATETIME + " TEXT,"+ ANSWER_TYPE + " INTEGER,"+ ANSWER_CAT_SKIP + " TEXT,"+
                                                                                                    ANSWER_IS_SEEN + " TEXT,"+ ANSWER_MAX_NO + " INTEGER,"+ ANSWER_MAX_SAMPLE + " INTEGER,"+
                                                                                                    ANSWER_NO_SAMPLE + " INTEGER,"+ ANSWER_QUES_SKIP + " TEXT,"+ SEC_EXISTS + " TEXT,"+ QUESTION_FAIL + " TEXT,"+ ANSWER_CAT_TYPE + " INTEGER" +")";
    private final String TABLE_QUESTION_ANSWER_DROP = "DROP TABLE IF EXISTS " + ANSWER_TBL_NAME;


    // For Category Question Images
    public static final String ANSWER_IMAGE_TBL_NAME="answerImage";
    public static final String ANSWER_IMAGE="answerImage";

    private final String TABLE_QUESTION_ANSWER_IMAGE_CREATE = "CREATE TABLE " + ANSWER_IMAGE_TBL_NAME + " ("+ STORE_ID + " INTEGER," + CATEGORY_ID + " INTEGER,"+ QUESTION_ID + " INTEGER,"+ ANSWER_IMAGE + " TEXT" +")";
    private final String TABLE_QUESTION_ANSWER_IMAGE_DROP = "DROP TABLE IF EXISTS " + ANSWER_IMAGE_TBL_NAME;

    public static final String ANSWER_IMAGE_TBL_PATH="answerpath";
    public static final String ANSWER_PATH="answerpath";

    private final String TABLE_QUESTION_ANSWER_PATH_CREATE = "CREATE TABLE " + ANSWER_IMAGE_TBL_PATH + " ("+ STORE_ID + " INTEGER," + CATEGORY_ID + " INTEGER,"+ QUESTION_ID + " INTEGER,"+ ANSWER_PATH + " TEXT" +")";
    private final String TABLE_QUESTION_ANSWER_PATh_DROP = "DROP TABLE IF EXISTS " + ANSWER_IMAGE_TBL_PATH;


    // For Sample Audits
    public static final String AUDIT_SAMPLE_TBL_NAME="sampleInfo";
    public static final String SAMPLE_POS="samplePos";
    public static final String SAMPLE_IS_CLICKED="isClicked";
    public static final String IS_SAMPLE_CLICKED="ratex";
    public static final String SAMPLE_COUNT="sampleCount";
    public static final String SAMPLE_CURRENT_RATE="sampleCurrentRate";
    public static final String NO_SAMPLE_PRODUCT="nosampleproduct";
    public static final String PRODUCCT_NAME="productname";
    public static final String BRAND_NAME="brandname";
    public static final String MFDPKD="mfdpkd";
    public static final String MFDDATA="mfddate";
    public static final String BB_EXP="bbexp";
    public static final String BBEXPDATA="bbexpdata";
    public static final String SELFLIFE="selflife";
    public static final String TEMPERATURE="temperature";

    private final String TABLE_SAMPLE_CREATE = "CREATE TABLE " + AUDIT_SAMPLE_TBL_NAME + " ("+ STORE_ID + " INTEGER,"+ CATEGORY_ID + " INTEGER,"+ QUESTION_ID + " INTEGER,"+ SAMPLE_POS + " INTEGER,"+ NO_SAMPLE_PRODUCT + " TEXT,"+ PRODUCCT_NAME + " TEXT,"+ BRAND_NAME + " TEXT,"+ MFDPKD + " TEXT,"+ MFDDATA + " TEXT,"+ BB_EXP + " TEXT,"+ BBEXPDATA + " TEXT,"+ SELFLIFE + " TEXT,"+ TEMPERATURE +" TEXT,"+ SAMPLE_IS_CLICKED + " TEXT,"+ IS_SAMPLE_CLICKED + " TEXT,"+ SAMPLE_COUNT + " TEXT,"+ SAMPLE_CURRENT_RATE + " TEXT" +")";
    private final String TABLE_SAMPLE_DROP = "DROP TABLE IF EXISTS " + AUDIT_SAMPLE_TBL_NAME;

    // For Store Details
    public static final String STORE_DETAILS_TBL_NAME="storeDetails";
    public static final String AUDITOR_ID= "auditor_id";
    public static final String CHILLERS="chillers";
    public static final String FREZERS="freezers";
    public static final String VENDOR_CHILLERS="chillers_from_vendors";
    public static final String VENDOR_FREZEERS="freezers_from_vendors";
    public static final String BOXEX="rodent_boxes";
    public static final String FLY_CATCHERS="flyCatchers";
    public static final String AIR_CUTTERS="airCutters";
    public static final String THERMOMETERS="thermometers";
    public static final String MANAGER_NAME="manager_name";
    public static final String MANAGER_EMAIL="manager_email";

    private final String TABLE_STORE_DETAILSE_CREATE = "CREATE TABLE " + STORE_DETAILS_TBL_NAME + " ("+
            STORE_ID + " INTEGER,"+ AUDITOR_ID + " INTEGER,"+
            CHILLERS + " TEXT,"+ FREZERS + " TEXT,"+
            VENDOR_CHILLERS + " TEXT,"+VENDOR_FREZEERS + " TEXT,"+
            BOXEX + " TEXT,"+FLY_CATCHERS + " TEXT,"+
            AIR_CUTTERS + " TEXT,"+THERMOMETERS + " TEXT,"+
            MANAGER_NAME + " TEXT,"+MANAGER_EMAIL + " TEXT"+")";
    private final String TABLE_STORE_DETAILS_DROP = "DROP TABLE IF EXISTS " + STORE_DETAILS_TBL_NAME;



    // For Store Start & End Date Time
    public static final String STORE_START_TIME_TABLE="startTimeTable";
    public static final String DATE_TIME="startDateTime";

    private final String TABLE_STORE_START_DATE_TIME_CREATE = "CREATE TABLE " + STORE_START_TIME_TABLE + " ("+ STORE_ID + " INTEGER," + DATE_TIME + " TEXT" +")";
    private final String TABLE_STORE_START_DATE_TIME_DROP = "DROP TABLE IF EXISTS " + STORE_START_TIME_TABLE;

}