package com.gbm.db;

import android.provider.BaseColumns;

/**
 * Created by Sri on 9/6/2017.
 */

public final class GBMContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private GBMContract() {}

    /* Inner class that defines the table contents */
    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "gbm_users";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_FULL_NAME = "full_name";
        public static final String COLUMN_NAME_ROLE = "role";
        public static final String COLUMN_NAME_CREATED_DT = "created_dt";
    }

    /* Inner class that defines the table contents */
    public static class Invoice implements BaseColumns {
        public static final String TABLE_NAME = "gbm_invoices";
        public static final String COLUMN_NAME_INVOICE_NO = "invoice_no";
        public static final String COLUMN_NAME_INVOICE_DT = "invoice_dt";
        public static final String COLUMN_NAME_SUPPLY_DT = "supply_dt";
        public static final String COLUMN_NAME_VEHICLE_NO = "vehicle_no";
        public static final String COLUMN_NAME_RECEIVER_NAME = "receiver_name";
        public static final String COLUMN_NAME_RECEIVER_ADDRESS = "receiver_address";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_GSTIN = "gstin";
        public static final String COLUMN_NAME_AADHAR = "aadhar";
        public static final String COLUMN_NAME_PAN = "pan";
        public static final String COLUMN_NAME_TOTAL_BEFORE_TAX = "total_before_tax";
        public static final String COLUMN_NAME_SGST = "sgst";
        public static final String COLUMN_NAME_CGST = "cgst";
        public static final String COLUMN_NAME_TOTAL_AFTER_TAX = "total_after_tax";
        public static final String COLUMN_NAME_CREATED_DT = "created_dt";
    }

    /* Inner class that defines the table contents */
    public static class Product implements BaseColumns {
        public static final String TABLE_NAME = "gbm_products";
        public static final String COLUMN_NAME_INVOICE_NO = "invoice_no";
        public static final String COLUMN_NAME_PRODUCT_NAME = "product_name";
        public static final String COLUMN_NAME_HSN_CODE = "hsn_code";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_TOTAL = "total";
    }

    public static class Booking implements BaseColumns {
        public static final String TABLE_NAME = "gbm_bookings";
        public static final String COLUMN_NAME_BOOKING_NO = "booking_no";
        public static final String COLUMN_NAME_BOOKING_DT = "booking_dt";
        public static final String COLUMN_NAME_RECEIVER_NAME = "receiver_name";
        public static final String COLUMN_NAME_RECEIVER_ADDRESS = "receiver_address";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_ITEM = "item";
        public static final String COLUMN_NAME_RENT = "rent";
        public static final String COLUMN_NAME_TOTAL = "total";
        public static final String COLUMN_NAME_ADVANCE = "advance";
        public static final String COLUMN_NAME_BALANCE = "balance";
        public static final String COLUMN_NAME_CREATED_DT = "created_dt";
    }

    public static class Credit implements BaseColumns {
        public static final String TABLE_NAME = "gbm_credits";
        public static final String COLUMN_NAME_BOOKING_DT = "booking_dt";
        public static final String COLUMN_NAME_RECEIVER_NAME = "receiver_name";
        public static final String COLUMN_NAME_PRODUCT_NAME = "product_name";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_SALE_TYPE = "sale_type";
        public static final String COLUMN_NAME_VEHICLE_NO = "vehicle_no";
        public static final String COLUMN_NAME_BOOKING_NO = "booking_no";
    }
	
	public static class Customer implements BaseColumns {
        public static final String TABLE_NAME = "gbm_customers";
        public static final String COLUMN_NAME_RECEIVER_NAME = "receiver_name";
        public static final String COLUMN_NAME_RECEIVER_ADDRESS = "receiver_address";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_GSTIN = "gstin";
        public static final String COLUMN_NAME_AADHAR = "aadhar";
        public static final String COLUMN_NAME_PAN = "pan";
        public static final String COLUMN_NAME_CREATED_DT = "created_dt";
    }

    /* Inner class that defines the table contents */
    public static class Settings implements BaseColumns {
        public static final String TABLE_NAME = "gbm_settings";
        public static final String COLUMN_NAME_INVOICE_NO = "invoice_no";
        public static final String COLUMN_NAME_BOOKING_NO = "booking_no";
        public static final String COLUMN_NAME_PRODUCTS = "products";
        public static final String COLUMN_NAME_PLACES = "places";
        public static final String COLUMN_NAME_VEHICLES = "vehicles";
    }
}
