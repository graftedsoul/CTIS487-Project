package com.ipek.gunaltay.project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.NotesRecyclerItemViewHolder> {
    Context context;
    ArrayList<Note> itemValues;
    DatabaseHelper dbHelper;
    boolean isStarred;
    GestureDetectorCompat gestureDetectorCompat;
    Note n;

    interface NotesRecyclerAdapterInterface {
        //void displayBlock(Folder f);
    }

    NotesRecyclerAdapterInterface notesRecyclerAdapterInterface;

    public NotesRecyclerAdapter(Context context, ArrayList<Note> itemValues) {
        this.context = context;
        this.itemValues = itemValues;

        dbHelper = new DatabaseHelper(context);
        MyGestureDetector mDetector = new MyGestureDetector();
        gestureDetectorCompat = new GestureDetectorCompat(context, mDetector);
        gestureDetectorCompat.setOnDoubleTapListener(mDetector);
    }

    public void setListener(NotesRecyclerAdapterInterface nInterface) {
        notesRecyclerAdapterInterface = nInterface;
    }

    @NonNull
    @Override
    public NotesRecyclerItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.note_item_layout, viewGroup, false);
        NotesRecyclerItemViewHolder viewHolder = new NotesRecyclerItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRecyclerItemViewHolder viewHolder, int position) {
        n = itemValues.get(position);

        String noteContent;

        if(n.getContent().length() >= 20) {
            noteContent = n.getContent().toString().substring(0, 20);
            noteContent += "...";
        } else {
            noteContent = n.getContent();
        }

        viewHolder.noteText.setText(noteContent);
        viewHolder.noteTitle.setText(n.getTitle());

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewHolder.noteTitle.setText(Html.fromHtml("<b>Bold <i>bold italic</i></b> <span style='color: yellow'>aa</span>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            viewHolder.noteTitle.setText(Html.fromHtml("<b>Bold <i>bold italic</i></b> <span style='color: yellow'>aa</span>"));
        }*/

        isStarred = n.isStarred();
        if(isStarred) {
            viewHolder.noteStar.setColorFilter(ContextCompat.getColor(context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditNoteActivity.class);
                intent.putExtra("note_id", n.getId());
                Log.d("NOTE", n.getId() + "");
                context.startActivity(intent);
            }
        });

        viewHolder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog(n, position);
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean deleted = NoteTable.delete(dbHelper, n.getId());

                if(deleted)
                {
                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                    refreshAdapterAfterDeletion(position);
                }
                else
                    Toast.makeText(context, "Could not delete.", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.noteStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStarred = !n.isStarred();
                refreshAdapterOnStar(position, isStarred);

                int s;
                if(isStarred) { s = 1; }
                else { s = 0; }

                Log.d("STARRED", n.getId() + "");
                NoteTable.updateStarred(dbHelper, n.getId(), s);

                if(isStarred) {
                    viewHolder.noteStar.setColorFilter(ContextCompat.getColor(context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    viewHolder.noteStar.setColorFilter(ContextCompat.getColor(context, R.color.medium_grey), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        });

        viewHolder.parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetectorCompat.onTouchEvent(motionEvent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemValues.size();
    }

    class NotesRecyclerItemViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parentLayout;
        TextView noteTitle, noteText;
        ImageView noteStar;
        Button editButton, viewButton, deleteButton;

        public NotesRecyclerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.noteItemLayout);
            noteText = itemView.findViewById(R.id.textNoteItemContent);
            noteTitle = itemView.findViewById(R.id.textNoteItemTitle);
            noteStar = itemView.findViewById(R.id.imageNoteItemStar);
            editButton = itemView.findViewById(R.id.buttonNoteItemEdit);
            viewButton = itemView.findViewById(R.id.buttonNoteItemView);
            deleteButton = itemView.findViewById(R.id.buttonNoteItemDelete);
        }
    }

    public void createDialog(Note n, int position){
        Log.d("NOTE", "Sent note: " + n.getTitle().toString());

        Dialog customDialog = new Dialog(context);
        customDialog.setContentView(R.layout.dialog_view_note);

        TextView noteName = customDialog.findViewById(R.id.textNoteNameVN);
        TextView noteContent = customDialog.findViewById(R.id.textNoteContentVN);
        ImageView noteIcon = customDialog.findViewById(R.id.imageNoteVN);
        ImageView noteStar = customDialog.findViewById(R.id.imageStarVN);
        Button deleteButton = customDialog.findViewById(R.id.buttonDeleteVN);
        Button editButton = customDialog.findViewById(R.id.buttonEditVN);

        if(n.getTitle().toString().length() > 12) {
            String t = n.getTitle().toString().substring(0, 12) + "...";
            noteName.setText(t);
        } else {
            noteName.setText(n.getTitle().toString());
        }
        noteContent.setText(n.getContent().toString());
        noteIcon.setImageResource(R.drawable.note);

        if(n.isStarred())
            noteStar.setImageResource(R.drawable.ic_baseline_star_24);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
                boolean deleted = NoteTable.delete(dbHelper, n.getId());

                if(deleted)
                {
                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                    refreshAdapterAfterDeletion(position);
                }
                else
                    Toast.makeText(context, "Could not delete.", Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
                Intent intent = new Intent(context, EditNoteActivity.class);
                intent.putExtra("note_id", n.getId());
                Log.d("NOTE", n.getId() + "");
                context.startActivity(intent);
            }
        });

        customDialog.show();
    }

    public void refreshAdapterAfterDeletion(int position) {
        itemValues.remove(position);
        notifyItemRemoved(position);
    }

    public void refreshAdapterOnStar(int position, boolean isStarred) {
        Note n = itemValues.get(position);
        n.setStarred(isStarred);
        itemValues.set(position, n);
        notifyItemChanged(position);
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("APPINFO", "OnDown");
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d("APPINFO", "OnDoubleTap");
            Intent intent = new Intent(context, EditNoteActivity.class);
            intent.putExtra("note_id", n.getId());
            Log.d("NOTE", n.getId() + "");
            context.startActivity(intent);
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("APPINFO", "OnSingleTapConfirmed");
            return true;
            // open the item dialog
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            // check the pressed item
            Log.d("APPINFO", "OnLongPress");
        }
    }

}
