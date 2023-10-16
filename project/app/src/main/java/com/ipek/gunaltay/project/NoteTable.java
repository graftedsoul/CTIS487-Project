package com.ipek.gunaltay.project;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class NoteTable {
    public static String TABLE_NAME="note";
    public static String FIELD_NID = "note_id";
    public static String FIELD_CONTENT = "content";
    public static String FIELD_NTITLE = "note_title";
    public static String FIELD_FOLDER = "folder";
    public static String FIELD_STARRED = "starred";

    public static String CREATE_TABLE_SQL="CREATE TABLE "+TABLE_NAME+" ( "+FIELD_NID+" integer, "+FIELD_CONTENT+" text, "+FIELD_NTITLE+" text, "+FIELD_FOLDER+" text,"+FIELD_STARRED+" integer)";
    public static String DROP_TABLE_SQL = "DROP TABLE if exists "+TABLE_NAME;

    public static ArrayList<String> INSERT_RECORD_SQL_LIST = new ArrayList<String>(){
        {

        }
    };

    public static ArrayList<Note>  getAllNotes(DatabaseHelper dbHelper){
        Note anItem;
        ArrayList<Note> data = new ArrayList<>();
        Cursor cursor = dbHelper.getAllRecords(TABLE_NAME, null);
        Log.d("DATABASE OPERATIONS", cursor.getCount()+",  "+cursor.getColumnCount());
        while(cursor.moveToNext()){
            int note_id=cursor.getInt(0);
            String content = cursor.getString(1);
            String note_title = cursor.getString(2);
            String folder = cursor.getString(3);

            int s = cursor.getInt(4);
            boolean starred = false;

            if(s == 0)
                starred = false;
            else if(s == 1)
                starred = true;

            Log.d("DATABASE", "Starred: " + starred);
            anItem = new Note(note_id, content, note_title, folder, starred);
            data.add(anItem);
        }
        Log.d("DATABASE", "Data: " + data.get(2).isStarred());
        return data;
    }

    public static ArrayList<Note> findNotesByKey(DatabaseHelper dbHelper, String key, String folderName) {
        Note anItem;
        ArrayList<Note> data = new ArrayList<>();
        String where = FIELD_FOLDER + " = '" + folderName + "' AND (" + FIELD_CONTENT + " OR " + FIELD_NTITLE + " like '%" + key + "%')";

        if(folderName.equals("All Notes")) {
            where = FIELD_CONTENT + " OR " + FIELD_NTITLE + " like '%" + key + "%'";
        } else if(folderName.equals("Starred Notes")) {
            where = FIELD_STARRED + " = 1" + " AND (" + FIELD_CONTENT + " OR " + FIELD_NTITLE + " like '%" + key + "%')";
        }

        Log.d("KEY", "Where: " + where);

        Cursor cursor = dbHelper.getSomeRecords(TABLE_NAME, null, where);
        Log.d("DATABASE OPERATIONS",  where+", "+cursor.getCount()+",  "+cursor.getColumnCount());
        while(cursor.moveToNext()){
            int note_id=cursor.getInt(0);
            String content = cursor.getString(1);
            String note_title = cursor.getString(2);
            String folder = cursor.getString(3);

            int s = cursor.getInt(4);
            boolean starred = false;

            if(s == 0)
                starred = false;
            else if(s == 1)
                starred = true;

            anItem = new Note(note_id, content, note_title, folder, starred);
            data.add(anItem);
        }
        Log.d("DATABASE OPERATIONS",data.toString());
        return data;
    }

    public static ArrayList<Note> findNotesInFolder(DatabaseHelper dbHelper, String key) {
        Note anItem;
        ArrayList<Note> data = new ArrayList<>();
        String where = FIELD_FOLDER + " = '" + key + "'";

        Log.d("KEY", "Where: " + where);

        Cursor cursor = dbHelper.getSomeRecords(TABLE_NAME, null, where);
        Log.d("DATABASE OPERATIONS",  where+", "+cursor.getCount()+",  "+cursor.getColumnCount());
        while(cursor.moveToNext()){
            int note_id=cursor.getInt(0);
            String content = cursor.getString(1);
            String note_title = cursor.getString(2);
            String folder = cursor.getString(3);

            int s = cursor.getInt(4);
            boolean starred = false;

            if(s == 0)
                starred = false;
            else if(s == 1)
                starred = true;

            anItem = new Note(note_id, content, note_title, folder, starred);
            data.add(anItem);
        }
        Log.d("DATABASE OPERATIONS",data.toString());
        return data;
    }

    public static Note findNoteByID(DatabaseHelper dbHelper, int id) {
        Note anItem = new Note(0, "", "", "", false);
        String where = FIELD_NID + " = " + id;

        Log.d("NOTE", "Where: " + where);
        Cursor cursor = dbHelper.getSomeRecords(TABLE_NAME, null, where);

        Log.d("KEY", where + ", " + cursor.getCount() + ",  " + cursor.getColumnCount());
        while(cursor.moveToNext()){
            int note_id= cursor.getInt(0);
            String content = cursor.getString(1);
            String note_title = cursor.getString(2);
            String folder = cursor.getString(3);

            int s = cursor.getInt(4);
            boolean starred = false;

            if(s == 0)
                starred = false;
            else if(s == 1)
                starred = true;

            anItem = new Note(note_id, content, note_title, folder, starred);
        }
        return anItem;
    }

    public static ArrayList<Note> findStarredNotes(DatabaseHelper dbHelper) {
        Note anItem = new Note(0, "", "", "", false);
        ArrayList<Note> data = new ArrayList<>();
        String where = FIELD_STARRED + " = 1";

        Log.d("NOTE", "Where: " + where);
        Cursor cursor = dbHelper.getSomeRecords(TABLE_NAME, null, where);

        Log.d("KEY", where + ", " + cursor.getCount() + ",  " + cursor.getColumnCount());
        while(cursor.moveToNext()){
            int note_id= cursor.getInt(0);
            String content = cursor.getString(1);
            String note_title = cursor.getString(2);
            String folder = cursor.getString(3);

            int s = cursor.getInt(4);
            boolean starred = false;

            if(s == 0)
                starred = false;
            else if(s == 1)
                starred = true;

            anItem = new Note(note_id, content, note_title, folder, starred);
            data.add(anItem);
        }
        Log.d("STARRED", data.size() + "SIZE");
        return data;
    }

    public static boolean insert(DatabaseHelper dbHelper, String content, String title, String folder, int starred) {
        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_CONTENT, content);
        contentValues.put(FIELD_NTITLE, title);
        contentValues.put(FIELD_FOLDER, folder);
        contentValues.put(FIELD_STARRED, String.valueOf(starred));

        boolean res = dbHelper.insert(TABLE_NAME,contentValues);
        return res;
    }

    public static boolean updateTitleContentFolder(DatabaseHelper dbHelper, int id, String content, String title, String folderName) {
        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_CONTENT, content);
        contentValues.put(FIELD_NTITLE, title);
        contentValues.put(FIELD_FOLDER, folderName);

        String where = FIELD_NID + " = " + id;
        boolean res = dbHelper.update(TABLE_NAME, contentValues, where);
        return res;
    }

    public static boolean updateStarred(DatabaseHelper dbHelper, int id, int isStarred) {
        ContentValues contentValues = new ContentValues( );
        contentValues.put(FIELD_STARRED, isStarred);

        String where = FIELD_NID + " = " + id;
        boolean res = dbHelper.update(TABLE_NAME, contentValues, where);
        return res;
    }

    public static boolean delete(DatabaseHelper dbHelper, int id){
        Log.d("DATABASE OPERATIONS", "DELETE DONE");
        String where = FIELD_NID + " = " + id;
        boolean res =  dbHelper.delete(TABLE_NAME, where);
        return  res;
    }
}
