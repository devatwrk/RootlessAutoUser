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

import java.io.IOException;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.model.User;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText  email_et, name_et , contact_et , address_et, password_et;
    private TextView login_tv ;
    private Button signup_btn;
    private Commonhelper commonhelper;
    private ApiService apiService;

    private void initview(){
        setContentView(R.layout.activity_sign_up);

        email_et = findViewById(R.id.etx_email);
        name_et = findViewById(R.id.etxname);
        contact_et = findViewById(R.id.etx_mobile);
        address_et = findViewById(R.id.etx_address);
        password_et = findViewById(R.id.etx_password);

        login_tv = findViewById(R.id.txt_login);
        signup_btn = findViewById(R.id.btn_signup);
        commonhelper = new Commonhelper(this);
        apiService = ApiUtils.getApiService();
    }

    private void OnClick(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        OnClick();

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonhelper.ShowLoader();
                signUpClicked();

            }
        });

        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
    }


    private void signUpClicked() {
        String email = email_et.getText().toString().trim();
        String uname = name_et.getText().toString().trim();
        String mobile = contact_et.getText().toString().trim();
        String address = address_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();

        //Validation
        if (validationCheck(email, uname, mobile, address, password)) {
            doSignUp( email, uname, mobile, address, password);
        } else {
            commonhelper.HideLoader();
        }


    }


    private boolean validationCheck(String email,String uname,String mobile,String address, String password) {
        if (TextUtils.isEmpty(email) ) {
            email_et.setError("Email is required");
            email_et.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_et.setError("Email a valid email");
            email_et.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(uname) ) {
            name_et.setError("Name is required");
            name_et.requestFocus();
            return false;


        }
        if (TextUtils.isEmpty(mobile) ) {
            contact_et.setError("Contact is required");
            contact_et.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(address) ) {
            address_et.setError("Address is required");
            address_et.requestFocus();
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
       /* else {
            doSignUp( email, uname, mobile, address, password);
        }*/
    }

    private void doSignUp( String email, String uname, String mobile, String address, String password) {

        Call<ResponseBody> call = apiService.createUser(email, uname, mobile, address, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                String msg = response.message();
                commonhelper.ShowMesseage("Registered Successfully");
                commonhelper.HideLoader();
                commonhelper.callintent(SignUpActivity.this, LoginActivity.class);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                commonhelper.HideLoader();
                commonhelper.ShowMesseage(t.getMessage());
            }
        });


    }

/*    private void gotoMainScreen() {

        // getting boolean
        if (commonhelper.getSharedPreferences("error", null).equals("false")){
            String username = commonhelper.getSharedPreferences("uname", null);
            commonhelper.ShowMesseage("Login Successful");
            commonhelper.callintent(SignUpActivity.this, MainActivity.class);
        }
    }

    public void saveUserDataSharedPref(User user) {
        String error = "false";
        try {

            commonhelper.setSharedPreferences("error", error);
            commonhelper.setSharedPreferences("email", user.getEmail());
            commonhelper.setSharedPreferences("password", user.getPassword());
            commonhelper.setSharedPreferences("uname", user.getUname());
            commonhelper.callintent(SignUpActivity.this, MainActivity.class);
            //  editor.putBoolean("error", false); // Storing boolean - true/false
//            editor.putString("error", error); // Storing boolean - true/false
//            editor.putString("email", user.getEmail()); // Storing string
//            editor.putString("password", user.getPassword());
//            editor.putString("uname", user.getUname());// Storing string
//            editor.commit();
//            commonhelper.callintent(LoginActivity.this, MainActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* private void checkIfUserLoggedIn() {
        // getting boolean
        try {
            String error = commonhelper.getSharedPreferences("error", null);

            if (error.equals("false")){
                String username = commonhelper.getSharedPreferences("uname", null);
                commonhelper.ShowMesseage("Welcome Back\n" + username);
                commonhelper.callintent(SignUpActivity.this, MainActivity.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        *//*if (pref.getString("error", null).equals("false")){
            String username = pref.getString("uname", null);
            commonhelper.ShowMesseage("Welcome Back\n" + username);
            commonhelper.callintent(LoginActivity.this, MainActivity.class);
        }*//*


    }*/

}
