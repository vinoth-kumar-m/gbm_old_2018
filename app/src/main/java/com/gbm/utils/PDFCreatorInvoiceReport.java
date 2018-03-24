package com.gbm.utils;

import java.util.List;

import com.gbm.vo.InvoiceVO;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PDFCreatorInvoiceReport extends AbstractPDFCreator {

	@Override
	@SuppressWarnings("unchecked")
	public <T> PdfPTable generatePDFContent(T t) {

		List<InvoiceVO> invoices = (List<InvoiceVO>) t;

		PdfPTable detail;
		PdfPCell cell;

		PdfPTable main = new PdfPTable(1);
		main.setWidthPercentage(100);

		
		detail = new PdfPTable(new float[]{ 2f, 2f, 4f, 2f });
		detail.setWidthPercentage(100);

		cell = new PdfPCell(new Phrase("GSTIN: 33AAAFG1953A1Z5"));
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		detail.addCell(cell);

		cell = new PdfPCell(new Phrase("Cell: 9597703209 / 9677790437"));
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		detail.addCell(cell);

		cell = new PdfPCell(new Phrase("GEMINI BLUE METAL WORKS", new Font(FontFamily.HELVETICA, 25, Font.BOLD)));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(4);
		cell.setPadding(5f);
		cell.setBorder(Rectangle.NO_BORDER);
		detail.addCell(cell);

		cell = new PdfPCell(new Phrase("73/3, Veppamara Salai, Alangayam Road, Vaniyambadi - 635752. Vellore Dt",
				new Font(FontFamily.HELVETICA, 9)));
		cell.setColspan(4);
		cell.setPadding(5f);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		detail.addCell(cell);

		cell = new PdfPCell(new Phrase("INVOICE REPORT", new Font(FontFamily.HELVETICA, 15, Font.BOLD)));
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.NO_BORDER);
		detail.addCell(cell);

		cell = new PdfPCell(new Phrase("Invoice Number", new Font(FontFamily.HELVETICA, 12, Font.BOLD)));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(5f);
		detail.addCell(cell);

		cell = new PdfPCell(new Phrase("Invoice Date", new Font(FontFamily.HELVETICA, 12, Font.BOLD)));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(5f);
		detail.addCell(cell);

		cell = new PdfPCell(new Phrase("Name", new Font(FontFamily.HELVETICA, 12, Font.BOLD)));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(5f);
		detail.addCell(cell);

		cell = new PdfPCell(new Phrase("Total (Incl. Tax)", new Font(FontFamily.HELVETICA, 12, Font.BOLD)));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(5f);
		detail.addCell(cell);

		for(InvoiceVO invoice : invoices) {

			cell = new PdfPCell(new Phrase(invoice.getInvoiceNo()));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(5f);
			detail.addCell(cell);

			cell = new PdfPCell(new Phrase(invoice.getInvoiceDt()));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(5f);
			detail.addCell(cell);

			cell = new PdfPCell(new Phrase(invoice.getReceiverName()));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(5f);
			detail.addCell(cell);

			cell = new PdfPCell(new Phrase(String.valueOf(invoice.getTotalAfterTax())));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPadding(5f);
			detail.addCell(cell);

		}

		main.addCell(new PdfPCell(detail));

		return main;
	}

}



