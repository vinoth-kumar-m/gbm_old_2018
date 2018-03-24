package com.gbm.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Pattern;

import com.gbm.vo.InvoiceVO;
import com.gbm.vo.ProductVO;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFCreatorInvoice extends AbstractPDFCreator {

    @Override
    public <T> File createPDF(Context context, String filename, T t) throws Exception {

        File folder = new File(context.getCacheDir(), "documents");
        if (!folder.exists()) {
            folder.mkdir();
        } else {
            for (File file : folder.listFiles()) {
                file.delete();
            }
        }

        File file = new File(folder, filename);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        //document.setPageSize(new Rectangle(595,396));
        document.setPageSize(PageSize.LETTER);
        document.setMargins(5, 5, 5, 5);
        document.open();
        document.add(getDocumentTitle("Original"));
        document.add(generatePDFContent(t));
        document.newPage();
        document.add(getDocumentTitle("Transporter Copy"));
        document.add(generatePDFContent(t));
        document.newPage();
        document.add(getDocumentTitle("Supplier Copy"));
        document.add(generatePDFContent(t));
        document.close();
        return file;

    }


    private PdfPTable getDocumentTitle(String title) {

        PdfPTable detail = new PdfPTable(1);
        detail.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase(title, new Font(FontFamily.HELVETICA, 7, Font.BOLD)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5f);
        detail.addCell(cell);

        return detail;
    }

    @Override
    public <T> PdfPTable generatePDFContent(T t) {

        InvoiceVO invoice = (InvoiceVO) t;

        PdfPTable detail;
        PdfPCell cell;

        PdfPTable main = new PdfPTable(1);
        main.setWidthPercentage(100);

        detail = new PdfPTable(4);
        detail.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("GSTIN: 33AAAFG1953A1Z5", new Font(FontFamily.HELVETICA, 8, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Cell: 9597703209 / 9677790437", new Font(FontFamily.HELVETICA, 8, Font.NORMAL)));
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("GEMINI BLUE METAL WORKS", new Font(FontFamily.HELVETICA, 15, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(4);
        cell.setPadding(2f);
        cell.setBorder(Rectangle.NO_BORDER);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("73/3, Veppamara Salai, Alangayam Road, Vaniyambadi - 635752. Vellore Dt", new Font(FontFamily.HELVETICA, 8)));
        cell.setColspan(4);
        cell.setPadding(2f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        detail.addCell(cell);

        main.addCell(new PdfPCell(detail));

        detail = new PdfPTable(3);
        detail.setWidthPercentage(100);

        cell = new PdfPCell(
                new Phrase("Invoice No.: " + invoice.getInvoiceNo(), new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setPaddingLeft(5f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("TAX INVOICE", new Font(FontFamily.HELVETICA, 10, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        detail.addCell(cell);

        cell = new PdfPCell(
                new Phrase("Invoice Date.: " + invoice.getInvoiceDt(), new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setPaddingLeft(5f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        detail.addCell(cell);

        main.addCell(new PdfPCell(detail));

        detail = new PdfPTable(new float[]{2f, 3f, 2f, 3f});
        detail.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("Reverse Charge: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("No.  ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Date of Supply: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getSupplyDt(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Vehicle Number: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getVehicleNo(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("State and Code: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Tamilnadu - 33", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Place of Supply: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Tamilnadu - 33", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(""));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        main.addCell(new PdfPCell(detail));

        cell = new PdfPCell(new Phrase("Details of Receiver / Buyer", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        main.addCell(cell);

        detail = new PdfPTable(new float[]{2f, 3f, 2f, 3f});
        detail.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("Name: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getReceiverName(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Aadhar: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getAadhar(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Address: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getAddress(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("PAN: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getPan(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Phone: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getPhone(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("GSTIN: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getGstin(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("State: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Tamilnadu", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("State Code: ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("33", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(2f);
        detail.addCell(cell);

        main.addCell(new PdfPCell(detail));

        detail = new PdfPTable(new float[]{0.75f, 2.75f, 1.25f, 1.25f, 2f, 2f});
        detail.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("Sr. No.", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Name of Product / Commodity", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("HSN / Code", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Qty", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Rate", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Taxable Value", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        int counter = 0;
        for (ProductVO product : invoice.getProducts()) {
            counter = counter + 1;

            cell = new PdfPCell(new Phrase(String.valueOf(counter), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(2f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getName(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(2f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getHsnCode(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(2f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getQuantity().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(2f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getPrice().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(2f);
            detail.addCell(cell);

            cell = new PdfPCell(new Phrase(product.getTotal().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(2f);
            detail.addCell(cell);
        }

        String[] totalAfterTax = invoice.getTotalAfterTax().toString().split(Pattern.quote("."));

        Phrase inWords = new Phrase();
        inWords.add(new Chunk("Total Invoice Amount in Words:  \n\n", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        inWords.add(new Chunk(NumberToWord.convert(totalAfterTax[0]) + " Only ", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));

        cell = new PdfPCell(inWords);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPadding(2f);
        cell.setColspan(3);
        cell.setRowspan(4);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Total Amount Before Tax", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        cell.setColspan(2);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getTotalBeforeTax().setScale(2).toString(),
                new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Tax (CGST - 2.5%)", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        cell.setColspan(2);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getcGST().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Tax (SGST - 2.5%)", new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        cell.setColspan(2);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getsGST().setScale(2).toString(), new Font(FontFamily.HELVETICA, 9, Font.NORMAL)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase("Total Amount After Tax", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        cell.setColspan(2);
        detail.addCell(cell);

        cell = new PdfPCell(new Phrase(invoice.getTotalAfterTax().setScale(2).toString(),
                new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2f);
        detail.addCell(cell);

        main.addCell(new PdfPCell(detail));

        cell = new PdfPCell(new Phrase("Terms & Conditions: ", new Font(FontFamily.HELVETICA, 9, Font.BOLD)));
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setPadding(2f);
        cell.setMinimumHeight(50f);
        main.addCell(cell);

        return main;
    }


}
