package com.gbm.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Creted by Vinoth on 9/6/2017.
 */

public class SettingsVO implements Serializable {

    private long id;
    private String invoiceNo;
    private String bookingNo;
    private String products;
    private String places;
    private String vehicles;

    public SettingsVO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getVehicles() { return vehicles; }

    public void setVehicles(String vehicles) { this.vehicles = vehicles; }
}
