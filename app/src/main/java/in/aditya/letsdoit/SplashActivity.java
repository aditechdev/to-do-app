package in.aditya.letsdoit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        },1000);

    }
}