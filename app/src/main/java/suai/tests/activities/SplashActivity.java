package suai.tests.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import suai.tests.R;
import suai.tests.common.api.RetrofitConnection;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new RetrofitConnection().CreateConnection();

        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, AuthorizationActivity.class)); finish();
            }
        }, 1000);
    }
}