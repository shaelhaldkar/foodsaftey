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


    private final String TABLE_Category_DROP = "DROP TABLE IF EXISTS "
            + CAT_Tbl_NAME;

    private final String TABLE_SubCategory_DROP = "DROP TABLE IF EXISTS "
            + SUBCAT_Tbl_NAME;

    private final String TABLE_Questions_DROP = "DROP TABLE IF EXISTS "
            + QUES_Tbl_NAME;

    private final String TABLE_Audit_DROP = "DROP TABLE IF EXISTS "
            + AUDIT_Tbl_NAME;

    private final String TABLE_AuditSample_DROP = "DROP TABLE IF EXISTS "
            + AUDIT_SAMPLE_Tbl_NAME;

    private final String TABLE_BusinessCategory_DROP = "DROP TABLE IF EXISTS "
            + BUSS_CAT_Tbl_NAME;

    private final String TABLE_Branch_DROP = "DROP TABLE IF EXISTS "
            + BRANCH_Tbl_NAME;

    private final String TABLE_User_DROP = "DROP TABLE IF EXISTS "
            + USER_Tbl_NAME;

    private final String TABLE_Answer_DROP = "DROP TABLE IF EXISTS "
            + ANSWER_Tbl_NAME;

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
        onCreate(db);
    }

}