package com.ipek.gunaltay.project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.FolderRecyclerItemViewHolder> {
    Context context;
    ArrayList<Folder> itemValues;

    interface FolderRecyclerAdapterInterface {
        //void displayBlock(Folder f);
    }

    FolderRecyclerAdapterInterface folderRecyclerAdapterInterface;

    public FolderRecyclerAdapter(Context context, ArrayList<Folder> itemValues) {
        this.context = context;
        this.itemValues = itemValues;
    }

    public void setListener(FolderRecyclerAdapterInterface fInterface) {
        folderRecyclerAdapterInterface = fInterface;
    }

    @NonNull
    @Override
    public FolderRecyclerItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.folder_recycler_item, viewGroup, false);
        FolderRecyclerItemViewHolder viewHolder = new FolderRecyclerItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderRecyclerItemViewHolder viewHolder, int position) {
        Folder f = itemValues.get(position);

        viewHolder.folderName.setText(f.getFolderName().toString());
        viewHolder.star.setVisibility(View.INVISIBLE);

        Log.d("FOLDER", f.getFolderName().toString());

        if(f.isChecked() == false) {
            viewHolder.check.setVisibility(View.INVISIBLE);
        }

        if(f.getFolderName().equals("Starred Notes")) {
            viewHolder.star.setVisibility(View.VISIBLE);
        }

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteListActivity.class);
                intent.putExtra("folder_name", f.getFolderName().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemValues.size();
    }

    class FolderRecyclerItemViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parentLayout;
        TextView folderName;
        ImageView check, star;

        public FolderRecyclerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.folderItemLayout);
            folderName = itemView.findViewById(R.id.textFolderItemName);
            check = itemView.findViewById(R.id.imageFolderItemCheckmark);
            star = itemView.findViewById(R.id.imageFolderItemStar);
        }
    }
}
