package com.ipek.gunaltay.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class EditNoteActivity extends AppCompatActivity {

    Intent receivedIntent;
    DatabaseHelper dbHelper;
    Note n;
    boolean isEdited = false;
    String noteTitle = "";
    String noteContent = "";
    String folderName;

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

        receivedIntent = getIntent();
        dbHelper = new DatabaseHelper(this);

        editNoteTitle = findViewById(R.id.editNoteTitleEN);
        editNoteContent = findViewById(R.id.editNoteContentEN);
        buttonSave = findViewById(R.id.buttonSaveEN);
        buttonBack = findViewById(R.id.buttonBackEN);
        spinnerFolder = findViewById(R.id.spinnerFolderEN);

        ArrayList<String> types = new ArrayList<>();
        for(Folder f : Commons.getFolders(this)) {
            Collections.addAll(types, f.getFolderName().toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types);
        spinnerFolder.setAdapter(adapter);

        if(receivedIntent != null) {
            int noteID = receivedIntent.getIntExtra("note_id", -1);

            if(noteID != -1) {
                n = NoteTable.findNoteByID(dbHelper, noteID);
                Log.d("NOTE", n.getTitle().toString());

                editNoteTitle.setText(n.getTitle().toString());
                editNoteContent.setText(n.getContent().toString());

                noteTitle = editNoteTitle.getText().toString();
                noteContent = editNoteContent.getText().toString();
                folderName = n.getFolderName().toString();
                Log.d("FOLDERS", folderName);
            } else {
                //TO-DO: Set spinner selected to the folder we are in
                folderName = receivedIntent.getStringExtra("folder_name");
            }

            int pos = types.indexOf(folderName);
            spinnerFolder.setSelection(pos);

            editNoteTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    isEdited = true;
                    noteTitle = editNoteTitle.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) { }
            });

            editNoteContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    isEdited = true;
                    noteContent = editNoteContent.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) { }
            });

            Log.d("NOTE", noteTitle);

            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(noteID != -1) {
                        boolean updated = NoteTable.updateTitleContentFolder(dbHelper, n.getId(), noteContent, noteTitle, spinnerFolder.getSelectedItem().toString());

                        if(updated) {
                            Toast.makeText(EditNoteActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                            isEdited = false;
                            finish();
                        }
                        else
                            Toast.makeText(EditNoteActivity.this, "Couldn't save...", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean ofya = NoteTable.insert(dbHelper, noteContent, noteTitle, spinnerFolder.getSelectedItem().toString(), 0);

                        if(ofya) {
                            Toast.makeText(EditNoteActivity.this, "New note created!", Toast.LENGTH_SHORT).show();
                            isEdited = false;
                            finish();
                        }
                        else
                        Toast.makeText(EditNoteActivity.this, "Couldn't create new note...", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isEdited) {
                        finish();
                    } else {
                        createDialog();
                    }
                }
            });

            spinnerFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    isEdited = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    isEdited = false;
                }
            });
        } else {
            Log.d("NOTE", "Received Intent is null.");
        }

    }

    public void createDialog(){
        customDialog = new Dialog(this);
        customDialog.setContentView(R.layout.discard_changes_dialog);

        discardButton = customDialog.findViewById(R.id.buttonDeleteD);
        cancelButton = customDialog.findViewById(R.id.buttonCancelD);
        imageWarning = customDialog.findViewById(R.id.imageWarningD);

        imageWarning.setImageResource(R.drawable.ic_baseline_warning_24);

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

        customDialog.show();
    }
}