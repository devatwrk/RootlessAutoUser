package aara.tech.rootless_auto_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.utils.Commonhelper;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Commonhelper commonhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        commonhelper = new Commonhelper(this);

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfUserLoggedIn();


            }
        }, 3000);

    }

    private void checkIfUserLoggedIn() {
        // getting boolean
        try {

            if (commonhelper.getSharedPreferences("current_user_name", null) != null) {

                String username = commonhelper.getSharedPreferences("current_user_name", null);
                commonhelper.ShowMesseage("Welcome Back\n" + username);
                commonhelper.callintent(SplashActivity.this, MainActivity.class);
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (pref.getString("error", null).equals("false")){
            String username = pref.getString("uname", null);
            commonhelper.ShowMesseage("Welcome Back\n" + username);
            commonhelper.callintent(LoginActivity.this, MainActivity.class);
        }*/


    }
}
