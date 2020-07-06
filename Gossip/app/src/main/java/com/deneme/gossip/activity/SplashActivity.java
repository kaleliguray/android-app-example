package com.deneme.gossip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.deneme.gossip.R;
import com.deneme.gossip.dialog.CustomDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.logo_image_view).startAnimation(AnimationUtils.loadAnimation(this,R.anim.bounce_animation));
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
                switch (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SplashActivity.this)) {
                    case ConnectionResult.SUCCESS:
                        Intent intent = new Intent(SplashActivity.this, FirebaseAuth.getInstance().getCurrentUser() == null ?  LoginActivity.class : MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case ConnectionResult.SERVICE_MISSING:
                            new CustomDialog(SplashActivity.this, "Error", "GooglePlay Service has missed. Please install GooglePlay Services", true, false, result -> {
                                if (result == CustomDialog.ActionListener.ActionResult.OK) finish();
                            }).show();
                        break;
                    case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                        new CustomDialog(SplashActivity.this, "Error", "GooglePlay Service require update", true, false, result -> {
                            if (result == CustomDialog.ActionListener.ActionResult.OK) finish();
                        }).show();
                        break;
                    case ConnectionResult.SERVICE_DISABLED:
                        new CustomDialog(SplashActivity.this, "Error", "GooglePlay Service disable. Please enable GooglePlay Service ", true, false, result -> {
                            if (result == CustomDialog.ActionListener.ActionResult.OK) finish();
                        }).show();
                        break;
                    case ConnectionResult.SERVICE_INVALID:
                        new CustomDialog(SplashActivity.this, "Error", "GooglePlay Service invalid. Please make valid GooglePlay Service ", true, false, result -> {
                            if (result == CustomDialog.ActionListener.ActionResult.OK) finish();
                        }).show();
                        break;

                }

        },2000);
    }
}
