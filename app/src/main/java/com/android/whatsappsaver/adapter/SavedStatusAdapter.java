package com.android.whatsappsaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.whatsappsaver.ImageViewActivity;
import com.android.whatsappsaver.R;
import com.android.whatsappsaver.StatusModel;
import com.android.whatsappsaver.VideoViewActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class SavedStatusAdapter extends RecyclerView.Adapter<SavedStatusAdapter.ViewHolder> {
    Context context;
    ArrayList<StatusModel> arrayList;

    public SavedStatusAdapter(Context context, ArrayList<StatusModel> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_whatsapp_saved, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedStatusAdapter.ViewHolder holder, int position) {
        StatusModel statusModel = arrayList.get(position);
        Glide.with(context).load(statusModel.getAbsolutePath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void deleteFile(String path, int position) {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                remove(position);
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Could not delete file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void share(String type, String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        context.startActivity(Intent.createChooser(intent, "Share Using"));
    }

    public void remove(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton share, delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            share = itemView.findViewById(R.id.list_share_content);
            delete = itemView.findViewById(R.id.list_delete_content);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = arrayList.get(getAdapterPosition());
                    if (statusModel.getAbsolutePath().endsWith(".jpg")) {
                        share("image/jpg", statusModel.getAbsolutePath());
                    } else {
                        share("video/mp4", statusModel.getAbsolutePath());
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        StatusModel statusModel = arrayList.get(getAdapterPosition());
                        deleteFile(statusModel.getAbsolutePath(), getAdapterPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = arrayList.get(getAdapterPosition());
                    Intent intent;
                    if (statusModel.getAbsolutePath().endsWith(".jpg")) {
                        intent = new Intent(context, ImageViewActivity.class);
                        intent.putExtra("image", statusModel.getAbsolutePath());
                    } else {
                        intent = new Intent(context, VideoViewActivity.class);
                        intent.putExtra("video", statusModel.getAbsolutePath());
                    }
                    intent.putExtra("type", "" + statusModel.getType());
                    intent.putExtra("aType", "0");
                    context.startActivity(intent);
                }
            });
        }
    }
}