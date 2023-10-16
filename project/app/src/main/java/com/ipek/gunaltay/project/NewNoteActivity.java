package com.ipek.gunaltay.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class NewNoteActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Note n;
    boolean isEdited = false;
    String noteTitle, noteContent,folderName;

    EditText editNoteTitle, editNoteContent;
    Button buttonSave, buttonBack;
    Spinner spinnerFolder;

    Dialog customDialog;
    Button discardButton, cancelButton;
    ImageView imageWarning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Hide the action and status bars
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dbHelper = new DatabaseHelper(this);

        editNoteTitle = findViewById(R.id.editNoteTitleEN);
        editNoteContent = findViewById(R.id.editNoteContentEN);
        buttonSave = findViewById(R.id.buttonSaveEN);
        buttonBack = findViewById(R.id.buttonBackEN);
        spinnerFolder = findViewById(R.id.spinnerFolderEN);
    }
}