package com.gbm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gbm.db.GBMContract.Booking;
import com.gbm.db.GBMContract.Invoice;
import com.gbm.db.GBMContract.Product;
import com.gbm.db.GBMContract.Credit;
import com.gbm.db.GBMContract.User;
import com.gbm.db.GBMContract.Customer;
import com.gbm.db.GBMContract.Settings;
import com.gbm.vo.UserVO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vinoth on 9/6/2017.
 */

public class GBMDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gbm.db";
    private static final String SQL_CREATE_USER =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User._ID + " INTEGER PRIMARY KEY," +
                    User.COLUMN_NAME_USERNAME + " TEXT," +
                    User.COLUMN_NAME_PASSWORD + " TEXT," +
                    User.COLUMN_NAME_FULL_NAME + " TEXT," +
                    User.COLUMN_NAME_ROLE + " TEXT," +
                    User.COLUMN_NAME_CREATED_DT + " TEXT)";

    private static final String SQL_CREATE_SETTINGS =
            "CREATE TABLE " + Settings.TABLE_NAME + " (" +
                    Settings._ID + " INTEGER PRIMARY KEY," +
                    Settings.COLUMN_NAME_INVOICE_NO + " TEXT," +
                    Settings.COLUMN_NAME_BOOKING_NO + " TEXT," +
                    Settings.COLUMN_NAME_PRODUCTS + " TEXT," +
                    Settings.COLUMN_NAME_PLACES + " TEXT," +
                    Settings.COLUMN_NAME_VEHICLES + " TEXT)";

    private static final String SQL_CREATE_INVOICE =
            "CREATE TABLE " + Invoice.TABLE_NAME + " (" +
                    Invoice._ID + " INTEGER PRIMARY KEY," +
                    Invoice.COLUMN_NAME_INVOICE_NO + " TEXT," +
                    Invoice.COLUMN_NAME_INVOICE_DT + " TEXT," +
                    Invoice.COLUMN_NAME_SUPPLY_DT + " TEXT," +
                    Invoice.COLUMN_NAME_VEHICLE_NO + " TEXT," +
                    Invoice.COLUMN_NAME_RECEIVER_NAME + " TEXT," +
                    Invoice.COLUMN_NAME_RECEIVER_ADDRESS + " TEXT," +
                    Invoice.COLUMN_NAME_PHONE + " TEXT," +
                    Invoice.COLUMN_NAME_GSTIN + " TEXT," +
                    Invoice.COLUMN_NAME_AADHAR + " TEXT," +
                    Invoice.COLUMN_NAME_PAN + " TEXT," +
                    Invoice.COLUMN_NAME_TOTAL_BEFORE_TAX + " NUMBER," +
                    Invoice.COLUMN_NAME_SGST + " NUMBER," +
                    Invoice.COLUMN_NAME_CGST + " NUMBER," +
                    Invoice.COLUMN_NAME_TOTAL_AFTER_TAX + " NUMBER," +
                    Invoice.COLUMN_NAME_CREATED_DT + " TEXT)";

    private static final String SQL_CREATE_PRODUCT =
            "CREATE TABLE " + Product.TABLE_NAME + " (" +
                    Product._ID + " INTEGER PRIMARY KEY," +
                    Product.COLUMN_NAME_INVOICE_NO + " TEXT," +
                    Product.COLUMN_NAME_PRODUCT_NAME + " TEXT," +
                    Product.COLUMN_NAME_HSN_CODE + " TEXT," +
                    Product.COLUMN_NAME_QUANTITY + " NUMBER," +
                    Product.COLUMN_NAME_PRICE + " NUMBER," +
                    Product.COLUMN_NAME_TOTAL + " NUMBER)";

    private static final String SQL_CREATE_BOOKING =
            "CREATE TABLE " + Booking.TABLE_NAME + " (" +
                    Booking._ID + " INTEGER PRIMARY KEY," +
                    Booking.COLUMN_NAME_BOOKING_NO + " TEXT," +
                    Booking.COLUMN_NAME_BOOKING_DT + " TEXT," +
                    Booking.COLUMN_NAME_RECEIVER_NAME + " TEXT," +
                    Booking.COLUMN_NAME_RECEIVER_ADDRESS + " TEXT," +
                    Booking.COLUMN_NAME_PHONE + " TEXT," +
                    Booking.COLUMN_NAME_ITEM + " TEXT," +
                    Booking.COLUMN_NAME_RENT + " NUMBER," +
                    Booking.COLUMN_NAME_TOTAL + " NUMBER," +
                    Booking.COLUMN_NAME_ADVANCE + " NUMBER," +
                    Booking.COLUMN_NAME_BALANCE + " NUMBER," +
                    Booking.COLUMN_NAME_CREATED_DT + " TEXT)";

    private static final String SQL_CREATE_CREDIT =
            "CREATE TABLE " + Credit.TABLE_NAME + " (" +
                    Credit._ID + " INTEGER PRIMARY KEY," +
                    Credit.COLUMN_NAME_BOOKING_DT + " TEXT," +
                    Credit.COLUMN_NAME_RECEIVER_NAME + " TEXT," +
                    Credit.COLUMN_NAME_PRODUCT_NAME + " TEXT," +
                    Credit.COLUMN_NAME_QUANTITY + " NUMBER," +
                    Credit.COLUMN_NAME_AMOUNT + " NUMBER," +
                    Credit.COLUMN_NAME_SALE_TYPE + " TEXT," +
                    Credit.COLUMN_NAME_VEHICLE_NO + " TEXT," +
                    Credit.COLUMN_NAME_BOOKING_NO + " TEXT)";
	
	private static final String SQL_CREATE_CUSTOMER =
            "CREATE TABLE " + Customer.TABLE_NAME + " (" +
                    Customer._ID + " INTEGER PRIMARY KEY," +
                    Customer.COLUMN_NAME_RECEIVER_NAME + " TEXT," +
                    Customer.COLUMN_NAME_RECEIVER_ADDRESS + " TEXT," +
                    Customer.COLUMN_NAME_PHONE + " TEXT," +
                    Customer.COLUMN_NAME_GSTIN + " TEXT," +
                    Customer.COLUMN_NAME_AADHAR + " TEXT," +
                    Customer.COLUMN_NAME_PAN + " TEXT," +
                    Customer.COLUMN_NAME_CREATED_DT + " TEXT)";

    private static final String SQL_DROP_USER =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    private static final String SQL_DROP_SETTINGS =
            "DROP TABLE IF EXISTS " + Settings.TABLE_NAME;

    private static final String SQL_DROP_INVOICE =
            "DROP TABLE IF EXISTS " + Invoice.TABLE_NAME;

    private static final String SQL_DROP_PRODUCT =
            "DROP TABLE IF EXISTS " + Product.TABLE_NAME;

    private static final String SQL_DROP_BOOKING =
            "DROP TABLE IF EXISTS " + Booking.TABLE_NAME;

    private static final String SQL_DROP_CREDIT =
            "DROP TABLE IF EXISTS " + Credit.TABLE_NAME;
			
	private static final String SQL_DROP_CUSTOMER =
            "DROP TABLE IF EXISTS " + Customer.TABLE_NAME;

    private Context context;

    public GBMDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static String getDBName() {
        return DATABASE_NAME;
    }

    public static Boolean alterDatabse = false;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER);
        db.execSQL(SQL_CREATE_SETTINGS);
        db.execSQL(SQL_CREATE_INVOICE);
        db.execSQL(SQL_CREATE_PRODUCT);
        db.execSQL(SQL_CREATE_BOOKING);
        db.execSQL(SQL_CREATE_CREDIT);
		db.execSQL(SQL_CREATE_CUSTOMER);

        // Inserting default admin user
        UserVO user = null;
        ContentValues values = null;

        db.beginTransaction();
        try {

            user = new UserVO();
            user.setUsername("gbmadmin");
            user.setPassword("Admin123$$");
            user.setFullName("GBM Administrator");
            user.setRole("Administrator");

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(User.COLUMN_NAME_USERNAME, user.getUsername());
            values.put(User.COLUMN_NAME_PASSWORD, user.getPassword());
            values.put(User.COLUMN_NAME_FULL_NAME, user.getFullName());
            values.put(User.COLUMN_NAME_ROLE, user.getRole());
            values.put(User.COLUMN_NAME_CREATED_DT, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

            db.insert(User.TABLE_NAME, null, values);

            user = new UserVO();
            user.setUsername("gbmbk");
            user.setPassword("Bk123");
            user.setFullName("GBM Booking");
            user.setRole("Booking");

            values = new ContentValues();
            values.put(User.COLUMN_NAME_USERNAME, user.getUsername());
            values.put(User.COLUMN_NAME_PASSWORD, user.getPassword());
            values.put(User.COLUMN_NAME_FULL_NAME, user.getFullName());
            values.put(User.COLUMN_NAME_ROLE, user.getRole());
            values.put(User.COLUMN_NAME_CREATED_DT, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));

            db.insert(User.TABLE_NAME, null, values);

            values = new ContentValues();
            values.put(Settings.COLUMN_NAME_INVOICE_NO, new Integer(1));
            values.put(Settings.COLUMN_NAME_BOOKING_NO, new Integer(1));
            values.put(Settings.COLUMN_NAME_PRODUCTS, "Dust,6mm,12mm,20mm,40mm,M-Sand,4SB,6SB");
            values.put(Settings.COLUMN_NAME_PLACES, "Vaniyambadi,Alangayam,Jolarpet,Kodiyur,Tirupattur,Ambur,Thimmampet,Natrampalli,Ramanaikanpet,Jamuna Marathur,Gudiyatham,Aasanamput");

            db.insert(Settings.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        alterDatabse = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("ALTER TABLE gbm_settings ADD COLUMN vehicles TEXT");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
