package com.ipek.gunaltay.project;

import java.util.ArrayList;

public class Note {
    private int id;
    private String content, title;
    private String folderName;
    private boolean isStarred;
    private ArrayList<TextEdit> textEditArray;

    public Note(int id, String content, String title, String folderName, boolean isStarred) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.folderName = folderName;
        this.isStarred = isStarred;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    public ArrayList<TextEdit> getTextEditArray() {
        return textEditArray;
    }

    public void setTextEditArray(ArrayList<TextEdit> textEditArray) {
        this.textEditArray = textEditArray;
    }

    /*
        switch(type){
            case 'C':
                String color = textEdit.getColor();
                spannedText.setSpan(new ForegroundColorSpan(Integer.parseInt(color)), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                break;
            case 'I':
                spannedText.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                break;
            case 'B':
                spannedText.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                break;
        }
    */

}
