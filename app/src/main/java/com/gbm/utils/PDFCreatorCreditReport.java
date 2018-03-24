package com.gbm.utils;

import java.util.List;

import com.gbm.vo.CreditVO;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PDFCreatorCreditReport extends AbstractPDFCreator {

    @Override
    @SuppressWarnings("unchecked")
    public <T> PdfPTable generatePDFContent(T t) {

        List<CreditVO> credits = (List<CreditVO>) t;

        PdfPTable detail;
        PdfPCell cell;

        PdfPTable main = new PdfPTable(1);
        main.setWidthPercentage(100);

        detail = new PdfPTable(new float[]{2.5f, 2.5f, 2.5f, 2.5f});
        detail.setWidthPercentage(100);


        cell = new PdfPCell(new Phrase("CASH / CREDIT REPORT", new Font(FontFamily.HELVETICA, 10, Font.BOLD)));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        detail.addCell(cell);

        cell = new PdfPCell(detail);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);

        main.addCell(cell);

        detail = new PdfPTable(new float[]{3.25f, 3.25f, 1.25f, 2.25f});
        detail.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("Date", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(1f);
        cell.setBorder(Rectangle.NO_BORDER);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Name", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(1f);
        cell.setBorder(Rectangle.NO_BORDER);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Type", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(1f);
        cell.setBorder(Rectangle.NO_BORDER);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Amount", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(1f);
        cell.setBorder(Rectangle.NO_BORDER);
        detail.addCell(cell);

        for (CreditVO credit : credits) {

            cell = new PdfPCell(new Phrase(credit.getBookingDt(),
                    new Font(FontFamily.HELVETICA, 9)));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(1f);
            cell.setBorder(Rectangle.NO_BORDER);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(credit.getReceiverName(),
                    new Font(FontFamily.HELVETICA, 9)));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(1f);
            cell.setBorder(Rectangle.NO_BORDER);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(credit.getSaleType(),
                    new Font(FontFamily.HELVETICA, 9)));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(1f);
            cell.setBorder(Rectangle.NO_BORDER);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(credit.getAmount().setScale(2)),
                    new Font(FontFamily.HELVETICA, 9)));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPadding(1f);
            cell.setBorder(Rectangle.NO_BORDER);
            detail.addCell(cell);

        }

        cell = new PdfPCell(detail);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(3f);

        main.addCell(cell);

        return main;
    }

}



