package com.ipek.gunaltay.project;

public class TextEdit {
    int start;
    int end;
    char editType;
    String color;

    public TextEdit(int start, int end, char editType, String color) {
        this.start = start;
        this.end = end;
        this.editType = editType;
        this.color = color;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public char getEditType() {
        return editType;
    }

    public void setEditType(char editType) {
        this.editType = editType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
