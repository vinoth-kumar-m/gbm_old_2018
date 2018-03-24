package com.gbm.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Creted by Vinoth on 9/6/2017.
 */

public class InvoiceVO implements Serializable {

    private long id;
    private String invoiceNo;
    private String invoiceDt;
    private String supplyDt;
    private String vehicleNo;
    private String receiverName;
    private String address;
    private String phone;
    private String gstin;
    private String aadhar;
    private String pan;
    private BigDecimal sGST;
    private BigDecimal cGST;
    private BigDecimal totalBeforeTax;
    private BigDecimal totalAfterTax;
    private List<ProductVO> products;
    private String fromDate;
    private String toDate;


    public InvoiceVO() {

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

    public String getInvoiceDt() {
        return invoiceDt;
    }

    public void setInvoiceDt(String invoiceDt) {
        this.invoiceDt = invoiceDt;
    }

    public String getSupplyDt() {
        return supplyDt;
    }

    public void setSupplyDt(String supplyDt) {
        this.supplyDt = supplyDt;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public BigDecimal getsGST() {
        return sGST;
    }

    public void setsGST(BigDecimal sGST) {
        this.sGST = sGST;
    }

    public BigDecimal getcGST() {
        return cGST;
    }

    public void setcGST(BigDecimal cGST) {
        this.cGST = cGST;
    }

    public BigDecimal getTotalBeforeTax() {
        return totalBeforeTax;
    }

    public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }

    public BigDecimal getTotalAfterTax() {
        return totalAfterTax;
    }

    public void setTotalAfterTax(BigDecimal totalAfterTax) {
        this.totalAfterTax = totalAfterTax;
    }

    public List<ProductVO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductVO> products) {
        this.products = products;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
