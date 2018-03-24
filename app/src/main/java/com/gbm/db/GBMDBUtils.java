package com.gbm.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;

import com.gbm.constants.GBMConstants;
import com.gbm.db.GBMContract.Booking;
import com.gbm.db.GBMContract.Invoice;
import com.gbm.db.GBMContract.Product;
import com.gbm.db.GBMContract.Credit;
import com.gbm.db.GBMContract.User;
import com.gbm.db.GBMContract.Customer;
import com.gbm.db.GBMContract.Settings;
import com.gbm.vo.BookingVO;
import com.gbm.vo.CustomerVO;
import com.gbm.vo.InvoiceVO;
import com.gbm.vo.ProductVO;
import com.gbm.vo.CreditVO;
import com.gbm.vo.SettingsVO;
import com.gbm.vo.UserVO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vinoth on 9/6/2017.
 */

public class GBMDBUtils {

    private static GBMDBHelper dbHelper;

    public GBMDBUtils(Context context) {
        dbHelper = new GBMDBHelper(context);
    }

    private static String[] projection = {
            User._ID,
            User.COLUMN_NAME_USERNAME,
            User.COLUMN_NAME_PASSWORD,
            User.COLUMN_NAME_FULL_NAME,
            User.COLUMN_NAME_ROLE,
            User.COLUMN_NAME_CREATED_DT
    };

    private static String[] projectionSettings = {
            Settings._ID,
            Settings.COLUMN_NAME_INVOICE_NO,
            Settings.COLUMN_NAME_BOOKING_NO,
            Settings.COLUMN_NAME_PRODUCTS,
            Settings.COLUMN_NAME_PLACES,
            Settings.COLUMN_NAME_VEHICLES
    };

    private static String[] projectionInvoice = {
            Invoice._ID,
            Invoice.COLUMN_NAME_INVOICE_NO,
            Invoice.COLUMN_NAME_INVOICE_DT,
            Invoice.COLUMN_NAME_SUPPLY_DT,
            Invoice.COLUMN_NAME_VEHICLE_NO,
            Invoice.COLUMN_NAME_RECEIVER_NAME,
            Invoice.COLUMN_NAME_RECEIVER_ADDRESS,
            Invoice.COLUMN_NAME_PHONE,
            Invoice.COLUMN_NAME_GSTIN,
            Invoice.COLUMN_NAME_AADHAR,
            Invoice.COLUMN_NAME_PAN,
            Invoice.COLUMN_NAME_TOTAL_BEFORE_TAX,
            Invoice.COLUMN_NAME_SGST,
            Invoice.COLUMN_NAME_CGST,
            Invoice.COLUMN_NAME_TOTAL_AFTER_TAX
    };

    private static String[] projectionProduct = {
            Product._ID,
            Product.COLUMN_NAME_INVOICE_NO,
            Product.COLUMN_NAME_PRODUCT_NAME,
            Product.COLUMN_NAME_HSN_CODE,
            Product.COLUMN_NAME_QUANTITY,
            Product.COLUMN_NAME_PRICE,
            Product.COLUMN_NAME_TOTAL
    };

    private static String[] projectionBooking = {
            Booking._ID,
            Booking.COLUMN_NAME_BOOKING_NO,
            Booking.COLUMN_NAME_BOOKING_DT,
            Booking.COLUMN_NAME_RECEIVER_NAME,
            Booking.COLUMN_NAME_RECEIVER_ADDRESS,
            Booking.COLUMN_NAME_PHONE,
            Booking.COLUMN_NAME_ITEM,
            Booking.COLUMN_NAME_RENT,
            Booking.COLUMN_NAME_TOTAL,
            Booking.COLUMN_NAME_ADVANCE,
            Booking.COLUMN_NAME_BALANCE
    };

    private static String[] projectionCredit = {
            Credit._ID,
            Credit.COLUMN_NAME_BOOKING_DT,
            Credit.COLUMN_NAME_RECEIVER_NAME,
            Credit.COLUMN_NAME_PRODUCT_NAME,
            Credit.COLUMN_NAME_QUANTITY,
            Credit.COLUMN_NAME_AMOUNT,
            Credit.COLUMN_NAME_SALE_TYPE,
            Credit.COLUMN_NAME_VEHICLE_NO,
            Credit.COLUMN_NAME_BOOKING_NO
    };
	
	private static String[] projectionCustomer = {
            Customer._ID,
            Customer.COLUMN_NAME_RECEIVER_NAME,
            Customer.COLUMN_NAME_RECEIVER_ADDRESS,
            Customer.COLUMN_NAME_PHONE,
            Customer.COLUMN_NAME_GSTIN,
            Customer.COLUMN_NAME_AADHAR,
            Customer.COLUMN_NAME_PAN
    };


    private static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private static String getCurrentDate() {
        return new SimpleDateFormat(GBMConstants.DATE_FORMAT_YYYYMMDD).format(new Date());
    }

    private static String formatDate(String dateStr, String fromFormat, String toFormat) {
        try {
            Date date = new SimpleDateFormat(fromFormat).parse(dateStr);
            return new SimpleDateFormat(toFormat).format(date);
        }
        catch(Exception ex) {
            Log.e("Invalid date format", dateStr);
        }
        return null;
    }

    public SettingsVO getSettings() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                Settings.TABLE_NAME,                     // The table to query
                projectionSettings,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        SettingsVO settings = null;
        if(cursor.moveToNext()) {
            settings = new SettingsVO();
            settings.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Settings._ID)));
            settings.setInvoiceNo(cursor.getString(cursor.getColumnIndexOrThrow(Settings.COLUMN_NAME_INVOICE_NO)));
            settings.setBookingNo(cursor.getString(cursor.getColumnIndexOrThrow(Settings.COLUMN_NAME_BOOKING_NO)));
            settings.setProducts(cursor.getString(cursor.getColumnIndexOrThrow(Settings.COLUMN_NAME_PRODUCTS)));
            settings.setPlaces(cursor.getString(cursor.getColumnIndexOrThrow(Settings.COLUMN_NAME_PLACES)));
            settings.setVehicles(cursor.getString(cursor.getColumnIndexOrThrow(Settings.COLUMN_NAME_VEHICLES)));
        }
        cursor.close();
        return settings;
    }

    public long saveSettings(SettingsVO settingsVO) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Settings.COLUMN_NAME_INVOICE_NO, settingsVO.getInvoiceNo());
            values.put(Settings.COLUMN_NAME_BOOKING_NO, settingsVO.getBookingNo());
            values.put(Settings.COLUMN_NAME_PRODUCTS, settingsVO.getProducts());
            values.put(Settings.COLUMN_NAME_PLACES, settingsVO.getPlaces());
            values.put(Settings.COLUMN_NAME_VEHICLES, settingsVO.getVehicles());

            id = db.update(Settings.TABLE_NAME, values, (Settings._ID + " = ?"), new String[]{"1"});

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        // Insert the new row, returning the primary key value of the new row
        return id;
    }

    public void incrementInvoiceNumber(SQLiteDatabase db) {
        String sql = "UPDATE gbm_settings SET invoice_no = invoice_no + 1 where _id = 1";
        SQLiteStatement sqlStatement = db.compileStatement(sql);
        try {
            sqlStatement.execute();
        }
        finally {
        }
    }

    public void incrementBookingNumber(SQLiteDatabase db) {
        String sql = "UPDATE gbm_settings SET booking_no = booking_no + 1 where _id = 1";
        SQLiteStatement sqlStatement = db.compileStatement(sql);
        try {
            sqlStatement.execute();
        }
        finally {
        }
    }

    public long saveUser(UserVO user) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(User.COLUMN_NAME_USERNAME, user.getUsername());
            values.put(User.COLUMN_NAME_PASSWORD, user.getPassword());
            values.put(User.COLUMN_NAME_FULL_NAME, user.getFullName());
            values.put(User.COLUMN_NAME_ROLE, user.getRole());
            values.put(User.COLUMN_NAME_CREATED_DT, getCurrentTimeStamp());

            id = db.insert(User.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        // Insert the new row, returning the primary key value of the new row
        return id;
    }

    public List<UserVO> getUser(String username, String fullName, String role) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = User.COLUMN_NAME_USERNAME  + " NOT IN ('gbmadmin') AND " + User.COLUMN_NAME_ROLE + " NOT IN ('Booking') ";
        List<String> args = new ArrayList<String>();

        if(username != null && username.length() > 0) {
            selection = selection + " AND " + User.COLUMN_NAME_USERNAME + " LIKE ?";
            args.add(username + "%");
        }
        if(fullName != null && fullName.length() > 0) {
            selection = selection + " AND " + User.COLUMN_NAME_FULL_NAME + " LIKE ?";
            args.add(fullName + "%");
        }
        if(role != null && role.length()> 0) {
            selection = selection + " AND " + User.COLUMN_NAME_ROLE + " = ? ";
            args.add(role);
        }
        String[] selectionArgs = args.toArray(new String[0]);
        Cursor cursor = db.query(
                User.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<UserVO> users = new ArrayList<UserVO>();
        UserVO user = null;
        while(cursor.moveToNext()) {
            user = new UserVO();
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(User._ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_PASSWORD)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_FULL_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_ROLE)));
            user.setCreatedDt(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_CREATED_DT)));
            users.add(user);
        }
        cursor.close();
        return users;
    }

    public UserVO validate(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = User.COLUMN_NAME_USERNAME + " = ? and " + User.COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };


        Cursor cursor = db.query(
                User.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        UserVO user = null;
        if(cursor.moveToNext()) {
            user = new UserVO();
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(User._ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_PASSWORD)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_FULL_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_ROLE)));
            user.setCreatedDt(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_CREATED_DT)));
        }
        cursor.close();
        return user;
    }

    public List<UserVO> getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                User.COLUMN_NAME_FULL_NAME + " ASC";
        String selection = User.COLUMN_NAME_USERNAME  + " NOT IN ('gbmadmin') AND " + User.COLUMN_NAME_ROLE + " NOT IN ('Booking') ";
        Cursor cursor = db.query(
                User.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                         // The columns for the WHERE clause
                null,                                                         // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<UserVO> users = new ArrayList<UserVO>();
        UserVO user = null;
        while(cursor.moveToNext()) {
            user = new UserVO();
            user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(User._ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_PASSWORD)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_FULL_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_ROLE)));
            user.setCreatedDt(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME_CREATED_DT)));
            users.add(user);
        }
        cursor.close();
        return users;
    }

    public void deleteUser(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(User.TABLE_NAME, (User._ID + " = ?"), new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }


    public void saveProducts(SQLiteDatabase db, String invoiceNo, List<ProductVO> products) {

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values;

            for(ProductVO product : products) {
                values = new ContentValues();
                values.put(Product.COLUMN_NAME_INVOICE_NO, invoiceNo);
                values.put(Product.COLUMN_NAME_PRODUCT_NAME, product.getName());
                values.put(Product.COLUMN_NAME_HSN_CODE, product.getHsnCode());
                values.put(Product.COLUMN_NAME_QUANTITY, String.valueOf(product.getQuantity()));
                values.put(Product.COLUMN_NAME_PRICE, String.valueOf(product.getPrice()));
                values.put(Product.COLUMN_NAME_TOTAL, String.valueOf(product.getTotal()));
                db.insert(Product.TABLE_NAME, null, values);
            }
        }
        finally {

        }
    }

    public long saveInvoice(InvoiceVO invoice) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Invoice.COLUMN_NAME_INVOICE_NO, invoice.getInvoiceNo());
            values.put(Invoice.COLUMN_NAME_INVOICE_DT, formatDate(invoice.getInvoiceDt(), GBMConstants.DATE_FORMAT_DDMMYYYY, GBMConstants.DATE_FORMAT_YYYYMMDD));
            values.put(Invoice.COLUMN_NAME_SUPPLY_DT, invoice.getSupplyDt());
            values.put(Invoice.COLUMN_NAME_VEHICLE_NO, invoice.getVehicleNo());
            values.put(Invoice.COLUMN_NAME_RECEIVER_NAME, invoice.getReceiverName());
            values.put(Invoice.COLUMN_NAME_RECEIVER_ADDRESS, invoice.getAddress());
            values.put(Invoice.COLUMN_NAME_PHONE, invoice.getPhone());
            values.put(Invoice.COLUMN_NAME_GSTIN, invoice.getGstin());
            values.put(Invoice.COLUMN_NAME_AADHAR, invoice.getAadhar());
            values.put(Invoice.COLUMN_NAME_PAN, invoice.getPan());
            values.put(Invoice.COLUMN_NAME_TOTAL_BEFORE_TAX, String.valueOf(invoice.getTotalBeforeTax()));
            values.put(Invoice.COLUMN_NAME_SGST, String.valueOf(invoice.getsGST()));
            values.put(Invoice.COLUMN_NAME_CGST, String.valueOf(invoice.getcGST()));
            values.put(Invoice.COLUMN_NAME_TOTAL_AFTER_TAX, String.valueOf(invoice.getTotalAfterTax()));

            saveProducts(db, invoice.getInvoiceNo(), invoice.getProducts());

            id = db.insert(Invoice.TABLE_NAME, null, values);

            incrementInvoiceNumber(db);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        // Insert the new row, returning the primary key value of the new row
        return id;
    }

    public List<ProductVO> getProducts(String invoiceNo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = " 1 = 1 ";
        List<String> args = new ArrayList<String>();

        if(!invoiceNo.isEmpty()) {
            selection = selection + " AND " + Product.COLUMN_NAME_INVOICE_NO + " = ?";
            args.add(invoiceNo);
        }

        String[] selectionArgs = args.toArray(new String[0]);
        Cursor cursor = db.query(
                Product.TABLE_NAME,                     // The table to query
                projectionProduct,                         // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<ProductVO> products = new ArrayList<ProductVO>();
        ProductVO product = null;
        while(cursor.moveToNext()) {
            product = new ProductVO();
            product.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Product._ID)));
            product.setInvoiceNo(cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_INVOICE_NO)));
            product.setName(cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_PRODUCT_NAME)));
            product.setHsnCode(cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_HSN_CODE)));
            product.setQuantity(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_QUANTITY))));
            product.setPrice(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_PRICE))));
            product.setTotal(new BigDecimal(cursor.getString(cursor.getColumnIndexOrThrow(Product.COLUMN_NAME_TOTAL))));
            products.add(product);
        }
        cursor.close();
        return products;
    }

    public List<InvoiceVO> getInvoices(InvoiceVO invoice) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String orderBy =  Invoice._ID+ " DESC";
        String selection = " 1 = 1 ";
        List<String> args = new ArrayList<String>();

        if(invoice.getInvoiceNo() != null && !invoice.getInvoiceNo().isEmpty()) {
            selection = selection + " AND " + Invoice.COLUMN_NAME_INVOICE_NO + " LIKE ?";
            args.add(invoice.getInvoiceNo()  + "%");
        }

        if(invoice.getReceiverName() != null && !invoice.getReceiverName().isEmpty()) {
            selection = selection + " AND " + Invoice.COLUMN_NAME_RECEIVER_NAME + " LIKE ?";
            args.add(invoice.getReceiverName()  + "%");
        }

        if(invoice.getInvoiceDt() != null && !invoice.getInvoiceDt().isEmpty()) {
            selection = selection + " AND " + Invoice.COLUMN_NAME_INVOICE_DT +
                    " >= date('" + formatDate(invoice.getInvoiceDt(), GBMConstants.DATE_FORMAT_DDMMYYYY, GBMConstants.DATE_FORMAT_YYYYMMDD) + "')";
        }

        if(invoice.getSupplyDt() != null && !invoice.getSupplyDt().isEmpty()) {
            selection = selection + " AND " + Invoice.COLUMN_NAME_SUPPLY_DT + " = ?";
            args.add(invoice.getSupplyDt());
        }

        if(invoice.getFromDate() != null && !invoice.getFromDate().isEmpty()) {
            selection = selection + " AND " + Invoice.COLUMN_NAME_INVOICE_DT +
                    " >= date('" + formatDate(invoice.getFromDate(), GBMConstants.DATE_FORMAT_DDMMYYYY, GBMConstants.DATE_FORMAT_YYYYMMDD) + "')";
            orderBy =  Invoice._ID + " ASC";
        }

        if(invoice.getToDate() != null && !invoice.getToDate().isEmpty()) {
            selection = selection + " AND " + Invoice.COLUMN_NAME_INVOICE_DT +
                    " <= date('" + formatDate(invoice.getToDate(), GBMConstants.DATE_FORMAT_DDMMYYYY, GBMConstants.DATE_FORMAT_YYYYMMDD) + "')";
        }

        String[] selectionArgs = args.toArray(new String[0]);
        Cursor cursor = db.query(
                Invoice.TABLE_NAME,                     // The table to query
                projectionInvoice,                         // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy                                 // The sort order
        );

        List<InvoiceVO> invoices = new ArrayList<InvoiceVO>();
        while(cursor.moveToNext()) {
            invoice = new InvoiceVO();
            invoice.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Invoice._ID)));
            invoice.setInvoiceNo(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_INVOICE_NO)));
            invoice.setInvoiceDt(formatDate(
                                    cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_INVOICE_DT)),
                                    GBMConstants.DATE_FORMAT_YYYYMMDD,
                                    GBMConstants.DATE_FORMAT_DDMMYYYY
                            ));
            invoice.setSupplyDt(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_SUPPLY_DT)));
            invoice.setVehicleNo(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_VEHICLE_NO)));
            invoice.setReceiverName(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_RECEIVER_NAME)));
            invoice.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_RECEIVER_ADDRESS)));
            invoice.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_PHONE)));
            invoice.setGstin(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_GSTIN)));
            invoice.setAadhar(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_AADHAR)));
            invoice.setPan(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_PAN)));
            invoice.setProducts(getProducts(invoice.getInvoiceNo()));

            String totalBeforeTax = cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_TOTAL_BEFORE_TAX));
            invoice.setTotalBeforeTax(new BigDecimal(totalBeforeTax));

            String sGST = cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_SGST));
            invoice.setsGST(new BigDecimal(sGST));

            String cGST = cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_CGST));
            invoice.setcGST(new BigDecimal(cGST));

            String totalAfterTax = cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_TOTAL_AFTER_TAX));
            invoice.setTotalAfterTax(new BigDecimal(totalAfterTax));

            invoices.add(invoice);
        }
        cursor.close();
        return invoices;
    }



    public List<BookingVO> getBookings(BookingVO booking) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String orderBy =  Booking._ID + " DESC";
        String selection = " 1 = 1 ";
        List<String> args = new ArrayList<String>();

        if(booking.getBookingNo() != null && !booking.getBookingNo().isEmpty()) {
            selection = selection + " AND " + Booking.COLUMN_NAME_BOOKING_NO + " LIKE ?";
            args.add(booking.getBookingNo() + "%");
        }

        if(booking.getBookingDt() != null && !booking.getBookingDt().isEmpty()) {
            selection = selection + " AND " + Booking.COLUMN_NAME_BOOKING_DT +
                    " >= date('" + formatDate(booking.getBookingDt(), GBMConstants.DATE_FORMAT_DDMMYYYY, GBMConstants.DATE_FORMAT_YYYYMMDD) + " 00:00:00')";
        }

        if(booking.getReceiverName() != null && !booking.getReceiverName().isEmpty()) {
            selection = selection + " AND " + Booking.COLUMN_NAME_RECEIVER_NAME + " LIKE ?";
            args.add(booking.getReceiverName() + "%");
        }

        String[] selectionArgs = args.toArray(new String[0]);
        Cursor cursor = db.query(
                Booking.TABLE_NAME,                     // The table to query
                projectionBooking,                         // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy                                 // The sort order
        );

        List<BookingVO> bookings = new ArrayList<BookingVO>();
        BookingVO bookingVO = null;
        while(cursor.moveToNext()) {
            bookingVO = new BookingVO();
            bookingVO.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Booking._ID)));
            bookingVO.setBookingNo(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_BOOKING_NO)));
            bookingVO.setBookingDt(formatDate(
                                    cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_BOOKING_DT)),
                                    GBMConstants.DATE_FORMAT_YYYYMMDDHHMM,
                                    GBMConstants.DATE_FORMAT_DDMMYYYYHHMM
                    ));
            bookingVO.setReceiverName(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_RECEIVER_NAME)));
            bookingVO.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_RECEIVER_ADDRESS)));
            bookingVO.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_PHONE)));
            bookingVO.setItem(cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_ITEM)));
            bookingVO.setProducts(getProducts(bookingVO.getBookingNo()));

            String rent = cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_RENT));
            bookingVO.setRent(new BigDecimal(rent));

            String total = cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_TOTAL));
            bookingVO.setTotal(new BigDecimal(total));

            String advance = cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_ADVANCE));
            bookingVO.setAdvance(new BigDecimal(advance));

            String balance = cursor.getString(cursor.getColumnIndexOrThrow(Booking.COLUMN_NAME_BALANCE));
            bookingVO.setBalance(new BigDecimal(balance));

            bookings.add(bookingVO);
        }
        cursor.close();
        return bookings;
    }

    public long saveBooking(BookingVO booking) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Booking.COLUMN_NAME_BOOKING_NO, booking.getBookingNo());
            values.put(Booking.COLUMN_NAME_BOOKING_DT, formatDate(booking.getBookingDt(), GBMConstants.DATE_FORMAT_DDMMYYYYHHMM, GBMConstants.DATE_FORMAT_YYYYMMDDHHMM));
            values.put(Booking.COLUMN_NAME_RECEIVER_NAME, booking.getReceiverName());
            values.put(Booking.COLUMN_NAME_RECEIVER_ADDRESS, booking.getAddress());
            values.put(Booking.COLUMN_NAME_PHONE, booking.getPhone());
            values.put(Booking.COLUMN_NAME_ITEM, booking.getItem());
            values.put(Booking.COLUMN_NAME_RENT, String.valueOf(booking.getRent()));
            values.put(Booking.COLUMN_NAME_TOTAL, String.valueOf(booking.getTotal()));
            values.put(Booking.COLUMN_NAME_ADVANCE, String.valueOf(booking.getAdvance()));
            values.put(Booking.COLUMN_NAME_BALANCE, String.valueOf(booking.getBalance()));

            id = db.insert(Booking.TABLE_NAME, null, values);

            saveProducts(db, booking.getBookingNo(), booking.getProducts());

            /*CreditVO credit = new CreditVO();
            credit.setBookingNo(booking.getBookingNo());
            credit.setTotal(booking.getTotal());
            credit.setPaid(booking.getAdvance());
            credit.setBalance(booking.getBalance());

            saveCredit(db, credit);*/
            incrementBookingNumber(db);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        // Insert the new row, returning the primary key value of the new row
        return id;
    }

    public void deleteBooking(BookingVO booking) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(Booking.TABLE_NAME, (Booking.COLUMN_NAME_BOOKING_NO + " = ?"), new String[]{booking.getBookingNo()});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }

    public void deleteInvoice(InvoiceVO invoice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(Invoice.TABLE_NAME, (Invoice.COLUMN_NAME_INVOICE_NO + " = ?"), new String[]{invoice.getInvoiceNo()});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }

    public List<CreditVO> getCredits(CreditVO credit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String orderBy =  Credit._ID + " DESC";
        String selection = " 1 = 1 ";
        List<String> args = new ArrayList<String>();

        if(credit.getBookingNo() != null && !credit.getBookingNo().isEmpty()) {
            selection = selection + " AND " + Credit.COLUMN_NAME_BOOKING_NO + " = ?";
            args.add(credit.getBookingNo());
        }

        if(credit.getBookingDt() != null && !credit.getBookingDt().isEmpty()) {
            selection = selection + " AND " + Credit.COLUMN_NAME_BOOKING_DT +
                    " >= date('" + formatDate(credit.getBookingDt(), GBMConstants.DATE_FORMAT_DDMMYYYY, GBMConstants.DATE_FORMAT_YYYYMMDD) + " 00:00:00')";
        }

        if(credit.getReceiverName() != null && !credit.getReceiverName().isEmpty()) {
            selection = selection + " AND LOWER(" + Credit.COLUMN_NAME_RECEIVER_NAME + ") LIKE ?";
            args.add(credit.getReceiverName().toLowerCase() + "%");
        }

        if(credit.getSaleType() != null && !credit.getSaleType().isEmpty()) {
            selection = selection + " AND " + Credit.COLUMN_NAME_SALE_TYPE + " = ?";
            args.add(credit.getSaleType());
        }

		if(credit.getFromDate() != null && !credit.getFromDate().isEmpty()) {
            selection = selection + " AND " + Credit.COLUMN_NAME_BOOKING_DT +
                    " >= date('" + formatDate(credit.getFromDate(), GBMConstants.DATE_FORMAT_DDMMYYYY, GBMConstants.DATE_FORMAT_YYYYMMDD) + " 00:00:00')";
            orderBy =  Credit._ID + " ASC";
        }

        if(credit.getToDate() != null && !credit.getToDate().isEmpty()) {
            selection = selection + " AND " + Credit.COLUMN_NAME_BOOKING_DT +
                    " <= date('" + formatDate(credit.getToDate(), GBMConstants.DATE_FORMAT_DDMMYYYY, GBMConstants.DATE_FORMAT_YYYYMMDD) + " 23:59:59')";
        }

        String[] selectionArgs = args.toArray(new String[0]);
        Cursor cursor = db.query(
                Credit.TABLE_NAME,                     // The table to query
                projectionCredit,                         // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy                                 // The sort order
        );

        List<CreditVO> credits = new ArrayList<CreditVO>();
        CreditVO creditVO = null;
        while(cursor.moveToNext()) {
            creditVO = new CreditVO();
            creditVO.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Credit._ID)));
            creditVO.setBookingNo(cursor.getString(cursor.getColumnIndexOrThrow(Credit.COLUMN_NAME_BOOKING_NO)));
            creditVO.setBookingDt(formatDate(
                    cursor.getString(cursor.getColumnIndexOrThrow(Credit.COLUMN_NAME_BOOKING_DT)),
                    GBMConstants.DATE_FORMAT_YYYYMMDDHHMM,
                    GBMConstants.DATE_FORMAT_DDMMYYYYHHMM
            ));
            creditVO.setReceiverName(cursor.getString(cursor.getColumnIndexOrThrow(Credit.COLUMN_NAME_RECEIVER_NAME)));
            creditVO.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(Credit.COLUMN_NAME_PRODUCT_NAME)));

            String quantity = cursor.getString(cursor.getColumnIndexOrThrow(Credit.COLUMN_NAME_QUANTITY));
            creditVO.setQuantity(new BigDecimal(quantity));

            String amount = cursor.getString(cursor.getColumnIndexOrThrow(Credit.COLUMN_NAME_AMOUNT));
            creditVO.setAmount(new BigDecimal(amount));

            creditVO.setSaleType(cursor.getString(cursor.getColumnIndexOrThrow(Credit.COLUMN_NAME_SALE_TYPE)));
            creditVO.setVehicleNo(cursor.getString(cursor.getColumnIndexOrThrow(Credit.COLUMN_NAME_VEHICLE_NO)));

            credits.add(creditVO);
        }
        cursor.close();
        return credits;
    }

    public long saveCredit(CreditVO credit) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys

            ContentValues values = new ContentValues();
            values.put(Credit.COLUMN_NAME_BOOKING_NO, credit.getBookingNo());
            values.put(Credit.COLUMN_NAME_BOOKING_DT, formatDate(credit.getBookingDt(), GBMConstants.DATE_FORMAT_DDMMYYYYHHMM, GBMConstants.DATE_FORMAT_YYYYMMDDHHMM));
            values.put(Credit.COLUMN_NAME_RECEIVER_NAME, credit.getReceiverName());
            values.put(Credit.COLUMN_NAME_PRODUCT_NAME, credit.getProductName());
            values.put(Credit.COLUMN_NAME_QUANTITY, String.valueOf(credit.getQuantity()));
            values.put(Credit.COLUMN_NAME_AMOUNT, String.valueOf(credit.getAmount()));
            values.put(Credit.COLUMN_NAME_SALE_TYPE, credit.getSaleType());
            values.put(Credit.COLUMN_NAME_VEHICLE_NO, credit.getVehicleNo());

            id = db.insert(Credit.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        // Insert the new row, returning the primary key value of the new row
        return id;
    }
	
	public void deleteCredit(CreditVO credit) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(Credit.TABLE_NAME, (Credit._ID + " = ?"), new String[]{String.valueOf(credit.getId())});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }
	
	public List<CustomerVO> getCustomers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
		String selection = " 1 = 1 ";
        List<String> args = new ArrayList<String>();
        String[] selectionArgs = args.toArray(new String[0]);
		
        Cursor cursor = db.query(
                Customer.TABLE_NAME,                     // The table to query
                projectionCustomer,                         // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<CustomerVO> customers = new ArrayList<CustomerVO>();
        CustomerVO customer = null;
        while(cursor.moveToNext()) {
            customer = new CustomerVO();
            customer.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Invoice._ID)));
            customer.setReceiverName(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_RECEIVER_NAME)));
            customer.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_RECEIVER_ADDRESS)));
            customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_PHONE)));
            customer.setGstin(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_GSTIN)));
            customer.setAadhar(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_AADHAR)));
            customer.setPan(cursor.getString(cursor.getColumnIndexOrThrow(Invoice.COLUMN_NAME_PAN)));
            customers.add(customer);
        }
        cursor.close();
        return customers;
    }
	
	public long saveCustomer(CustomerVO customer) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        db.beginTransaction();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Customer.COLUMN_NAME_RECEIVER_NAME, customer.getReceiverName());
            values.put(Customer.COLUMN_NAME_RECEIVER_ADDRESS, customer.getAddress());
            values.put(Customer.COLUMN_NAME_PHONE, customer.getPhone());
            values.put(Customer.COLUMN_NAME_GSTIN, customer.getGstin());
            values.put(Customer.COLUMN_NAME_AADHAR, customer.getAadhar());
            values.put(Customer.COLUMN_NAME_PAN, customer.getPan());

            id = db.insert(Customer.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        // Insert the new row, returning the primary key value of the new row
        return id;
    }

    public void deleteCustomer(CustomerVO customer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(Customer.TABLE_NAME, (Customer._ID + " = ?"), new String[]{String.valueOf(customer.getId())});
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }

}
