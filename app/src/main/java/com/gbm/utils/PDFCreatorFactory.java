package com.gbm.utils;

import com.gbm.constants.GBMConstants;

public class PDFCreatorFactory {
	public static PDFCreator getInstance(String type) {
		if(GBMConstants.INVOICE.equalsIgnoreCase(type)) {
			return new PDFCreatorInvoice();
		} else if(GBMConstants.BOOKING.equalsIgnoreCase(type)) {
			return new PDFCreatorBooking();
		} else if(GBMConstants.REPORT_INVOICE.equalsIgnoreCase(type)) {
			return new PDFCreatorInvoiceReport();
		} else if(GBMConstants.REPORT_CREDIT.equalsIgnoreCase(type)) {
			return new PDFCreatorCreditReport();
		}
		return null;
	}
}
