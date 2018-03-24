package com.gbm.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Sri on 9/6/2017.
 */

public class BookingVO implements Serializable {

    private long id;
    private String bookingNo;
    private String bookingDt;
    private String receiverName;
    private String address;
    private String phone;
    private String item;
    private BigDecimal rent;
    private BigDecimal total;
    private BigDecimal advance;
    private BigDecimal balance;
    private List<ProductVO> products;


    public BookingVO() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getBookingDt() {
        return bookingDt;
    }

    public void setBookingDt(String bookingDt) {
        this.bookingDt = bookingDt;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<ProductVO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductVO> products) {
        this.products = products;
    }

}


