package com.gbm.utils;

import android.content.Context;

import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;

public interface PDFCreator {
    <T> File createPDF(Context context, String filename, T t) throws Exception;
    <T> PdfPTable generatePDFContent(T t) throws Exception;
    void viewPDF(Context context, File file) throws Exception;
}
