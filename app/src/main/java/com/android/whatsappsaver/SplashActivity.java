package com.android.whatsappsaver;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && isFileAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            activityResultLauncher.launch(intent);
        }
    }

    private void startMainActivity(){
        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(() -> startActivity(new Intent(SplashActivity.this, MainActivity.class)), 3000);
    }

    private boolean isFileAccessGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return checkFileAccess();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean checkFileAccess(){
        AppOpsManager appOpsManager = getApplicationContext().getSystemService(AppOpsManager.class);
        int mode = appOpsManager.unsafeCheckOpNoThrow(
                "android:manage_external_storage",
                getApplicationContext().getApplicationInfo().uid,
                getPackageName()
        );
        return mode != AppOpsManager.MODE_ALLOWED;
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