package com.gbm.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Sri on 9/28/2017.
 */

public abstract class AbstractPDFCreator implements PDFCreator {

    @Override
    public <T> File createPDF(Context context, String filename, T t) throws Exception {
        File folder = new File(context.getCacheDir(), "documents");
        if (!folder.exists()) {
            folder.mkdir();
        }
        else {
            for(File file : folder.listFiles()) {
                file.delete();
            }
        }
        File file = new File(folder, filename);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.setPageSize(PageSize.A4);
        document.setMargins(5, 5, 5, 5);
        document.open();
        document.add(generatePDFContent(t));
        document.close();
        return file;

    }

    @Override
    final public void viewPDF(Context context, File file) throws Exception {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(context, "com.gbm.provider", file), "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setPackage("epson.print");
            context.startActivity(intent);
        } catch (Exception e) {
            Log.i("error", e.getLocalizedMessage());
        }
    }

}
