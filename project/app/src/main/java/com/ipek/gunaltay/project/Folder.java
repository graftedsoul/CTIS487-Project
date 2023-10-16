package com.ipek.gunaltay.project;

import java.util.ArrayList;

public class Folder {
    private int id;
    private String folderName;
    private boolean isChecked;
    private boolean isDeletable;
    private ArrayList<Integer> noteIDs;

    public Folder(int id, String folderName, boolean isChecked, boolean isDeletable, ArrayList<Integer> noteIDs) {
        this.id = id;
        this.folderName = folderName;
        this.isChecked = isChecked;
        this.isDeletable = isDeletable;
        this.noteIDs = noteIDs;
    }

    public Folder(String folderName, boolean isChecked, boolean isDeletable, ArrayList<Integer> noteIDs) {
        this.folderName = folderName;
        this.isChecked = isChecked;
        this.isDeletable = isDeletable;
        this.noteIDs = noteIDs;
    }

    public String getFolderName() {
        return folderName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public ArrayList<Integer> getNoteIDs() {
        return noteIDs;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setNoteIDs(ArrayList<Integer> noteIDs) {
        this.noteIDs = noteIDs;
    }
}
