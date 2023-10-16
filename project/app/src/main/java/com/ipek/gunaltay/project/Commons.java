package com.ipek.gunaltay.project;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Commons {

    Context context;

    public static String URL_TO="https://liminalspacesimulator.neocities.org/";
    public static String URL_FOR_JSON = "https://liminalspacesimulator.neocities.org/json.json";

    static ArrayList<Folder> folders;
    static ArrayList<Note> notes;

    public static ArrayList<String> mc_songs = new ArrayList<>();
    public static ArrayList<String> sh_songs = new ArrayList<>();
    public static ArrayList<String> ts2_songs = new ArrayList<>();

    public static void makeDirectory(Context context) {
        try {
            String fileToDatabase = "/data/data/" + MainActivity.PACKAGE_NAME + "/databases/"+DatabaseHelper.DATABASE_NAME;
            File file = new File(fileToDatabase);
            File pathToDatabasesFolder = new File("/data/data/" + MainActivity.PACKAGE_NAME + "/databases/");
            if (!file.exists()) {
                pathToDatabasesFolder.mkdirs();
                Log.d("DATABASE", "Made directory");
                CopyDB( context.getResources().getAssets().open(DatabaseHelper.DATABASE_NAME),
                        new FileOutputStream(fileToDatabase));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        // Copy 1K bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        Log.d("DATABASE", "Copying DB");

        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
            Log.d("DATABASE", "Still copying");

        }
        inputStream.close();
        outputStream.close();
    }

    public static ArrayList<Folder> getFolders(Context c) {
        folders = FolderTable.getAllFolders(new DatabaseHelper(c));
        return folders;
    }

    public static ArrayList<Note> getNotes(Context c) {
        notes = NoteTable.getAllNotes(new DatabaseHelper(c));
        return notes;
    }
}
