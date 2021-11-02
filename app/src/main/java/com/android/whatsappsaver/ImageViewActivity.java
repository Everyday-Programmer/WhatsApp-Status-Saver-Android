package com.android.whatsappsaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ImageViewActivity extends AppCompatActivity {
    ImageButton share, download, repost;
    String imagePath, path, aType, packageName;
    String type = "";
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageView = findViewById(R.id.imageViewer);
        share = findViewById(R.id.imageViewer_share);
        download = findViewById(R.id.imageViewer_save);
        repost = findViewById(R.id.imageViewer_repost);

        Intent intent = getIntent();
        if (intent != null){
            imagePath = intent.getStringExtra("image");
            type = intent.getStringExtra("type");
            aType = intent.getStringExtra("aType");

            if (imagePath != null) {
                Glide.with(this).load(imagePath).into(imageView);
            }
        }

        path = Utils.statusSaverPath;
        packageName = "com.whatsapp";

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(imagePath);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyFileOrDirectory(imagePath, path);
            }
        });

        repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repost(imagePath, packageName);
            }
        });

        if (aType.equals("0")){
            download.setVisibility(View.GONE);
        } else {
            download.setVisibility(View.VISIBLE);
        }
    }

    private void copyFileOrDirectory(String source, String destination){
        try {
            File src = new File(source);
            File dest = new File(destination);

            if (src.isDirectory()) {
                String files[] = src.list();
                int fileLength = files.length;
                for (int i = 0; i < fileLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dest1 = dest.getPath();
                    copyFileOrDirectory(src1, dest1);
                }
            } else {
                copyFile(src, dest);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException{
        if (!destFile.getParentFile().exists()){
            destFile.getParentFile().mkdirs();
        }

        if (!destFile.exists()){
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            Toast.makeText(this, "Picture Saved", Toast.LENGTH_SHORT).show();
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    private void share(String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(Intent.createChooser(intent, "Share Using"));
    }

    private void repost(String path, String packageName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        intent.setPackage(packageName);
        startActivity(Intent.createChooser(intent, "Share to"));
    }
}