package com.gbm.utils;

import com.gbm.vo.BookingVO;
import com.gbm.vo.ProductVO;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PDFCreatorBooking extends AbstractPDFCreator {

    @Override
    public <T> PdfPTable generatePDFContent(T t) {

            BookingVO booking = (BookingVO) t;

        PdfPTable main = new PdfPTable(1);
        main.setPaddingTop(10f);
        main.setWidthPercentage(100);

        PdfPTable detail;
        PdfPCell cell;

        cell = new PdfPCell(new Phrase("BOOKING DETAILS", new Font(FontFamily.HELVETICA, 10, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        main.addCell(cell);

        detail = new PdfPTable(new float[]{20f, 30f, 20f, 30f});
        detail.setTotalWidth(100f);

        cell = new PdfPCell(new Phrase("Booking No.: ", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getBookingNo(), new Font(FontFamily.HELVETICA, 9)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Booking Date: ", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getBookingDt(), new Font(FontFamily.HELVETICA, 9)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Name: ", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getReceiverName(), new Font(FontFamily.HELVETICA, 9)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Address: ", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getAddress(), new Font(FontFamily.HELVETICA, 9)));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Phone: ", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getPhone(), new Font(FontFamily.HELVETICA, 9)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        detail.addCell(cell);

        main.addCell(new PdfPCell(detail));

        detail = new PdfPTable(new float[]{ 0.75f, 2.75f, 1.25f, 1.25f, 2f, 2f });
        detail.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("Sr. No.", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Name of Product / Commodity", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("HSN / Code", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Qty", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Rate", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Taxable Value", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        int counter = 0;
        for(ProductVO product : booking.getProducts()) {
            counter = counter + 1;

            cell = new PdfPCell(new Phrase(String.valueOf(counter), new Font(FontFamily.HELVETICA, 9)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getName(), new Font(FontFamily.HELVETICA, 9)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getHsnCode(), new Font(FontFamily.HELVETICA, 9)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getQuantity().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getPrice().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getTotal().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            detail.addCell(cell);
        }

        cell = new PdfPCell(new Phrase("Rent", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getRent().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Total", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getTotal().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Advance", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getAdvance().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Balance", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(5);
        cell.setPadding(5f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(booking.getBalance().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        detail.addCell(cell);

        main.addCell(new PdfPCell(detail));

        return main;
    }

}