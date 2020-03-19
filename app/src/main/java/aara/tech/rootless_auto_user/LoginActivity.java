package aara.tech.rootless_auto_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.model.LoginResponse;
import aara.tech.rootless_auto_user.repository.model.User;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email_et, password_et;
    private TextView tv_signup, forgot_password;
    private Button login_btn, google_login;

    ApiService apiService;
    private Commonhelper commonhelper;

    private void initview() {
        setContentView(R.layout.activity_login);
        email_et = findViewById(R.id.etx_email);
        password_et = findViewById(R.id.etx_password);
        forgot_password = findViewById(R.id.txt_forgot_password);
        tv_signup = findViewById(R.id.txt_dont_hv_account);
        login_btn = findViewById(R.id.btn_login);
        google_login = findViewById(R.id.btn_ggl_cust);

        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(this);
    }

    private void onclick(){
        /*login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();

            }
        });*/

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , ForgetPassword.class));
            }
        });

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Still in development ", Toast.LENGTH_SHORT).show();
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClicked();
            }

        });
        onclick();
    }

    private void loginClicked() {
        commonhelper.ShowLoader();
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        //Validation
        if (validationCheck(email, password)) {
            doLogin( email, password);
        } else {
            commonhelper.HideLoader();
        }

    }

    private boolean validationCheck(String email, String password) {
        if (TextUtils.isEmpty(email) ) {
            email_et.setError("Email is required");
            email_et.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_et.setError("Email a valid email");
            email_et.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password) ) {
            password_et.setError("Password is required");
            password_et.requestFocus();
            commonhelper.HideLoader();
            return false;

        } else if (password.length() < 6) {
            password_et.setError("Password should be atleast 6 characters long");
            password_et.requestFocus();
            commonhelper.HideLoader();
            return false;
        }
        return true;
    }

    private void doLogin(String email, String password) {
        Call<LoginResponse> call = apiService.login(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {

//                    String error;
                    email_et.setText("");
                    password_et.setText("");

                    LoginResponse loginResponse = response.body();
                    if (!loginResponse.isError()) {
                        User user = loginResponse.getUser();

                        //Saving LoggedIn User's Data into Shared Preference
                        saveUserDataSharedPref(user);
                        //Intent to mainActivity



                    } else {
                        commonhelper.HideLoader();
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                commonhelper.HideLoader();
            }
        });
    }

    public void saveUserDataSharedPref(User user) {
        String error = "false";
        try {
            commonhelper.setSharedPreferences("error", error);
            commonhelper.setSharedPreferences("current_user_id", user.getId());
            commonhelper.setSharedPreferences("current_user_email", user.getEmail());
            commonhelper.setSharedPreferences("current_user_password", user.getPassword());
            commonhelper.setSharedPreferences("current_user_name", user.getUname());
            gotoMainScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoMainScreen() {


        // getting boolean
        if (commonhelper.getSharedPreferences("error", null).equals("false")){
            String username = commonhelper.getSharedPreferences("current_user_name", null);
            commonhelper.ShowMesseage("Login Successful \n"  + commonhelper.getSharedPreferences("current_user_name", null));
            commonhelper.callintent(LoginActivity.this, MainActivity.class);
            commonhelper.HideLoader();
        }
    }



    private void checkIfUserLoggedIn() {
        // getting boolean
        try {
            String error = commonhelper.getSharedPreferences("error", null);

            if (error.equals("false")){
                String username = commonhelper.getSharedPreferences("uname", null);
                commonhelper.ShowMesseage("Welcome Back\n" + username);
                commonhelper.callintent(LoginActivity.this, MainActivity.class);
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
