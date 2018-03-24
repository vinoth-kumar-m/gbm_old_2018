package com.gbm.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.gbm.constants.GBMConstants;
import com.gbm.db.GBMContract;
import com.gbm.db.GBMDBHelper;
import com.gbm.vo.CustomerVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sri on 10/10/2017.
 */

public class BackupAndRestore {
    public static void restore(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if(!sd.exists()) {
                Toast.makeText(context, "No memory card found", Toast.LENGTH_LONG).show();
                return;
            }
            if (sd.canWrite()) {
                File backupDB = context.getDatabasePath(GBMDBHelper.getDBName());
                String backupDBPath = String.format("%s.bak", GBMDBHelper.getDBName());
                File folder = new File(sd, GBMConstants.BACKUP_FOLDER);
                if(!folder.exists()) {
                    Toast.makeText(context, "Backup folder not exists in memory card", Toast.LENGTH_LONG).show();
                    return;
                }
                File currentDB = new File(folder, backupDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                if(GBMDBHelper.alterDatabse) {
                    SQLiteDatabase db = new GBMDBHelper(context).getWritableDatabase();
                    db.beginTransaction();
                    try {
                        db.execSQL("ALTER TABLE gbm_settings ADD COLUMN vehicles TEXT");
                        db.setTransactionSuccessful();
                    }
                    finally {
                        db.endTransaction();
                    }
                    GBMDBHelper.alterDatabse = false;
                }

                Toast.makeText(context, "Data restore successful", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void backup(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if(!sd.exists()) {
                Toast.makeText(context, "No memory card found", Toast.LENGTH_LONG).show();
                return;
            }

            if (sd.canWrite()) {
                String backupDBPath = String.format("%s.bak", GBMDBHelper.getDBName());

                File folder = new File(sd, GBMConstants.BACKUP_FOLDER);
                if(!folder.exists()) {
                    folder.mkdir();
                } else {
                    File temp = new File(folder, backupDBPath);
                    temp.renameTo(new File(folder, new SimpleDateFormat("ddMMyyyyHHmm").format(new Date()) + "_" + backupDBPath));
                }

                File currentDB = context.getDatabasePath(GBMDBHelper.getDBName());
                File backupDB = new File(folder, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Toast.makeText(context, "Data backup successful", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}