package com.ipek.gunaltay.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {
    ArrayList<Note> notes;

    RecyclerView recyclerNote;
    TextView folderTitle;
    String folderName;
    Button buttonNewNote, buttonBack;
    ImageView imageDelete;

    DatabaseHelper dbHelper;
    EditText etKey;

    Intent receivedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // Hide the action and status bars
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        receivedIntent = getIntent();
        folderName = receivedIntent.getStringExtra("folder_name");

        etKey = findViewById(R.id.editSearchNoteNL);

        Commons.makeDirectory(this);

        recyclerNote = findViewById(R.id.recyclerNotesNL);
        folderTitle = findViewById(R.id.textNoteFolderTitleNL);
        buttonNewNote = findViewById(R.id.buttonNewNoteNL);
        buttonBack = findViewById(R.id.buttonBackNL);
        imageDelete = findViewById(R.id.imageDeleteNL);

        dbHelper = new DatabaseHelper(this);

        checkFolder();

        folderTitle.setText(folderName);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerNote.setLayoutManager(gridLayoutManager);
        recyclerNote.setAdapter(new NotesRecyclerAdapter(this, notes));

        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("KEY", "Key: " + etKey.getText().toString());

                if(etKey.getText().toString().equals("")) {
                    Log.d("KEY", "Key empty");
                    checkFolder();
                    recyclerNote.setAdapter(new NotesRecyclerAdapter(NoteListActivity.this, notes));
                } else {
                    Log.d("KEY", "Key not mepty: " + charSequence.toString());
                    notes = NoteTable.findNotesByKey(dbHelper, charSequence.toString(), folderName);
                    recyclerNote.setAdapter(new NotesRecyclerAdapter(NoteListActivity.this, notes));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        buttonNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, EditNoteActivity.class);
                intent.putExtra("folder_name", folderName);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sureDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFolder();
        recyclerNote.setAdapter(new NotesRecyclerAdapter(this, notes));
    }

    public void checkFolder() {
        if(folderName.equals("All Notes")) {
            notes = NoteTable.getAllNotes(dbHelper);
        } else if(folderName.equals("Starred Notes")) {
            notes = NoteTable.findStarredNotes(dbHelper);
            Log.d("STARRED", "Title of first starred: " + notes.get(0).getTitle().toString());
        }
        else {
            notes = NoteTable.findNotesInFolder(dbHelper, folderName);
        }
    }

    public void sureDialog(){
        Dialog customDialog = new Dialog(this);
        customDialog.setContentView(R.layout.sure_dialog_layout);

        Button cancel = customDialog.findViewById(R.id.buttonCancelS);
        Button delete = customDialog.findViewById(R.id.buttonDeleteS);
        ImageView warning = customDialog.findViewById(R.id.imageWarningS);

        warning.setImageResource(R.drawable.ic_baseline_warning_24);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
                keepDialog();
            }
        });

        customDialog.show();
    }

    public void keepDialog() {
        Dialog customDialog = new Dialog(this);
        customDialog.setContentView(R.layout.keep_notes_dialog);

        Button keep = customDialog.findViewById(R.id.buttonKeepKN);
        Button delete = customDialog.findViewById(R.id.buttonDeleteKN);

        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Note> notes = NoteTable.findNotesInFolder(dbHelper, folderName);

                for(Note n : notes) {
                    boolean r = NoteTable.updateTitleContentFolder(dbHelper, n.getId(), n.getTitle().toString(), n.getContent().toString(), "All Notes");
                    if(r)
                    {
                        continue;
                    }
                }

                boolean r = FolderTable.delete(dbHelper, folderName);
                if(r)
                {
                    customDialog.dismiss();
                    finish();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Note> notes = NoteTable.findNotesInFolder(dbHelper, folderName);

                for(Note n : notes) {
                    int id = n.getId();
                    boolean r = NoteTable.delete(dbHelper, id);
                    if(r)
                    {
                        continue;
                    }
                }

                boolean r = FolderTable.delete(dbHelper, folderName);
                if(r)
                {
                    customDialog.dismiss();
                    finish();
                }
            }
        });

        customDialog.show();
    }
}