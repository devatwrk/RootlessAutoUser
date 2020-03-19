package aara.tech.rootless_auto_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {

    private EditText email;
    private Button reset;
    private String email_txt;
    private Commonhelper commonhelper;
    private ApiService apiService;

    private void initview() {
        setContentView(R.layout.activity_forget_paasword);
        email = findViewById(R.id.etx_email);
        reset = findViewById(R.id.btn_reset);
        commonhelper = new Commonhelper(this);
        apiService = ApiUtils.getApiService();

    }

    private void OnClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        OnClick();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_txt = email.getText().toString();
                resetPassword(email_txt);

            }
        });
    }

    private void resetPassword( String email) {

        Call<ResponseBody> call = apiService.forgetPassword(email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    String msg = response.body().string();
                    commonhelper.ShowMesseage(msg);
                    commonhelper.HideLoader();
                    startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
                     finish();
                } catch (IOException e) {
                    commonhelper.HideLoader();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                commonhelper.HideLoader();
                commonhelper.ShowMesseage(t.getMessage());
            }
        });


    }
}
