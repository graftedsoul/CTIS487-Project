package com.ipek.gunaltay.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String PACKAGE_NAME;

    RecyclerView recyclerFolder;
    Button optionsButton, createButton;

    BottomFragment bottomFragment;

    ArrayList<Folder> folders;
    DatabaseHelper dbHelper;

    EditText etKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the action and status bars
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        PACKAGE_NAME = getApplicationContext().getPackageName();
        Commons.makeDirectory(this);

        etKey = findViewById(R.id.editSearchFolderNameM);
        dbHelper = new DatabaseHelper(this);
        folders = FolderTable.getAllFolders(dbHelper);

        Log.d("ISPLAYING", "AT MAIN");
        bottomFragment = (BottomFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBottomM);

        recyclerFolder = findViewById(R.id.recyclerFolderM);
        optionsButton = findViewById(R.id.buttonOptionsM);
        createButton = findViewById(R.id.buttonCreateFolderM);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFolder.setLayoutManager(layoutManager);
        recyclerFolder.setAdapter(new FolderRecyclerAdapter(this, folders));

        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("KEY", "Key: " + etKey.getText().toString());

                if(etKey.getText().toString().equals("")) {
                    Log.d("KEY", "Key empty");
                    folders = FolderTable.getAllFolders(dbHelper);
                    recyclerFolder.setAdapter(new FolderRecyclerAdapter(MainActivity.this, folders));
                } else {
                    Log.d("KEY", "Key not mepty: " + charSequence.toString());
                    folders = FolderTable.findFolders(dbHelper, charSequence.toString());
                    recyclerFolder.setAdapter(new FolderRecyclerAdapter(MainActivity.this, folders));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
                intent.putExtra("isPlaying", bottomFragment.isPlaying);
                startActivity(intent);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });
    }

    public void createDialog(){
        Dialog customDialog = new Dialog(this);
        customDialog.setContentView(R.layout.new_folder_dialog);

        EditText folderName = customDialog.findViewById(R.id.editFolderNameNF);
        Button cancel = customDialog.findViewById(R.id.buttonCancelNF);
        Button create = customDialog.findViewById(R.id.buttonCreateNF);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean r = false;
                if(folderName.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please give your folder a name.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean fnc = FolderTable.searchFolder(dbHelper, folderName.getText().toString());
                    if(fnc){
                        Toast.makeText(MainActivity.this, "Folder already exists!", Toast.LENGTH_SHORT).show();
                    }else{
                        r = FolderTable.insert(dbHelper, folderName.getText().toString(), 0, 1, new ArrayList<Integer>());
                    }

                    if (r)
                    {
                        folders = FolderTable.getAllFolders(dbHelper);
                        recyclerFolder.setAdapter(new FolderRecyclerAdapter(MainActivity.this, folders));
                        Toast.makeText(MainActivity.this, "New folder created!", Toast.LENGTH_SHORT).show();
                        customDialog.dismiss();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Creation unsuccessful.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

        customDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        folders = FolderTable.getAllFolders(dbHelper);
        recyclerFolder.setAdapter(new FolderRecyclerAdapter(MainActivity.this, folders));
    }
}