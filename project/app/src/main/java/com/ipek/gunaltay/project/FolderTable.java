package com.ipek.gunaltay.project;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class FolderTable {
    public static String TABLE_NAME="folder";
    public static String FIELD_FID = "folder_id";
    public static String FIELD_FNAME = "folder_name";
    public static String FIELD_CHECKED = "isChecked";
    public static String FIELD_DELETABLE = "deletable";
    public static String FIELD_NID = "note_ids";

    public static String CREATE_TABLE_SQL="CREATE TABLE "+TABLE_NAME+" ( "+FIELD_FID+" integer, "+FIELD_FNAME+" text, "+FIELD_CHECKED+" integer ,"+FIELD_DELETABLE+" integer,"+FIELD_NID+" integer)";
    public static String DROP_TABLE_SQL = "DROP TABLE if exists "+TABLE_NAME;

    public static ArrayList<String> INSERT_RECORD_SQL_LIST = new ArrayList<String>(){
        {

        }
    };

    public static ArrayList<Folder> getAllFolders(DatabaseHelper dbHelper){
        Folder anItem;

        ArrayList<Folder> data = new ArrayList<>();

        Cursor cursor = dbHelper.getAllRecords(TABLE_NAME, null);
        Log.d("FOLDERS", cursor.getCount()+",  "+cursor.getColumnCount());

        while(cursor.moveToNext()){
            int folder_id = cursor.getInt(0);
            String folder_name = cursor.getString(1);
            boolean isChecked = Boolean.parseBoolean(String.valueOf(cursor.getInt(2)));
            boolean deletable = Boolean.parseBoolean(String.valueOf(cursor.getInt(3)));
            ArrayList<Integer> noteIds = new ArrayList<>();

            anItem = new Folder(folder_id, folder_name, isChecked, deletable, noteIds);
            data.add(anItem);
        }
        Log.d("FOLDERS", data.toString());
        return data;
    }

    public static ArrayList<Folder> findFolders(DatabaseHelper dbHelper, String key) {
        Folder anItem;
        ArrayList<Folder> data = new ArrayList<>();
        String where = FIELD_FNAME + " like '%"+key+"%'";

        Cursor cursor = dbHelper.getSomeRecords(TABLE_NAME, null, where);
        Log.d("FOLDERS",  where+", "+cursor.getCount()+",  "+cursor.getColumnCount());
        while(cursor.moveToNext()){
            int folder_id = cursor.getInt(0);
            String folder_name = cursor.getString(1);
            boolean isChecked = Boolean.parseBoolean(String.valueOf(cursor.getInt(2)));
            boolean deletable = Boolean.parseBoolean(String.valueOf(cursor.getInt(3)));
            ArrayList<Integer> noteIds = new ArrayList<>();
            anItem = new Folder(folder_id, folder_name, isChecked, deletable, noteIds);
            data.add(anItem);
        }
        Log.d("FOLDERS",data.toString());
        return data;
    }

    public static boolean searchFolder(DatabaseHelper dbHelper, String key) {

        String where = FIELD_FNAME + " = '" + key + "'";

        Cursor cursor = dbHelper.getSomeRecords(TABLE_NAME, null, where);
        Log.d("FOLDERS",  where+", "+cursor.getCount()+",  "+cursor.getColumnCount());
        if(cursor.getCount() == 0) {
            return false;
        }
        else
            return true;
    }

    public static boolean insert(DatabaseHelper dbHelper, String name, int isChecked, int deletable, ArrayList<Integer> noteIds) {
        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_FNAME, name);
        contentValues.put(FIELD_CHECKED, isChecked);
        contentValues.put(FIELD_DELETABLE, deletable);
        contentValues.put(FIELD_NID, String.valueOf(noteIds));

        boolean res = dbHelper.insert(TABLE_NAME,contentValues);
        return res;
    }

    public static boolean update(DatabaseHelper dbHelper, String id, String name, int isChecked, int deletable, ArrayList<Integer> noteIds) {

        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_FNAME, name);
        contentValues.put(FIELD_CHECKED, isChecked);
        contentValues.put(FIELD_DELETABLE, deletable);
        contentValues.put(FIELD_NID, String.valueOf(noteIds));

        String where = FIELD_FID +" = "+id;
        boolean res = dbHelper.update(TABLE_NAME,contentValues,where );
        return res;
    }

    public static boolean delete(  DatabaseHelper dbHelper, String name){
        Log.d("FOLDERS", "DELETE DONE");
        String where = FIELD_FNAME + " = '"+name + "'";
        boolean res =  dbHelper.delete(TABLE_NAME, where);
        return  res;
    }

}
