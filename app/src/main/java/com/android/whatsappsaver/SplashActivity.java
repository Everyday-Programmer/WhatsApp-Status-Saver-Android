package com.android.whatsappsaver;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    private final ActivityResultLauncher<String> permissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result){
                    startMainActivity();
                } else {
                    Toast.makeText(this, "Please Grant Storage Permission", Toast.LENGTH_SHORT).show();
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.teal_700));
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_700));

        if (isPermissionGranted()) {
            startMainActivity();
        } else {
            permissionResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void startMainActivity(){
        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 3000);
    }

    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                permissionResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return false;
            }
        } else if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            return true;
        }
        return false;
    }
}