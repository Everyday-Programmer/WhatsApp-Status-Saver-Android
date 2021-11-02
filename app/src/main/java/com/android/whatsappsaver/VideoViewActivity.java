package com.android.whatsappsaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class VideoViewActivity extends AppCompatActivity {
    ImageButton share, download, repost;
    VideoView videoView;
    String videoPath, path, type, aType, packageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        videoView = findViewById(R.id.videoViewer);
        share = findViewById(R.id.videoViewer_share);
        download = findViewById(R.id.videoViewer_save);
        repost = findViewById(R.id.videoViewer_repost);

        Intent intent = getIntent();
        if (intent != null) {
            videoPath = intent.getStringExtra("video");
            type = intent.getStringExtra("type");
            aType = intent.getStringExtra("aType");
            videoView.setVideoPath(videoPath);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        path = Utils.statusSaverPath;
        packageName = "com.whatsapp";

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(videoPath);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyFileOrDirectory(videoPath, path);
            }
        });

        repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repost(videoPath, packageName);
            }
        });

        if (aType.equals("0")) {
            download.setVisibility(View.GONE);
        } else {
            download.setVisibility(View.VISIBLE);
        }
    }

    private void copyFile(File sourceFile, File destinationFile) throws IOException {
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }
        if (!destinationFile.exists()) {
            destinationFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destinationFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            Toast.makeText(this, "Video Saved", Toast.LENGTH_SHORT).show();
        } finally {
            if (source != null){
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    private void copyFileOrDirectory(String source, String destination) {
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

    private void share(String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/mp4");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    private void repost(String path, String packageName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/mp4");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        intent.setPackage(packageName);
        startActivity(Intent.createChooser(intent, "Share to"));
    }
}