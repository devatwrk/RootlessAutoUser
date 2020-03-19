package aara.tech.rootless_auto_user.ui.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.model.MoreInfoData;
import aara.tech.rootless_auto_user.repository.model.MoreInfoResponse;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreInfoFragment extends Fragment {

    private EditText etName, etNumber, etSubject, etEmail, etMessage;
    private TextView tvName, tvNumber, tvSubject, tvEmail , tvDate, tvMessage, tvDatePicker;
    private Commonhelper commonhelper;
    private Button submit;
    private ApiService apiService;


    private void initViews(View view) {
        //EditText
        etName = view.findViewById(R.id.et_your_name);
        etNumber = view.findViewById(R.id.et_number);
        etSubject = view.findViewById(R.id.et_subject);
        etEmail = view.findViewById(R.id.et_email);
        etMessage = view.findViewById(R.id.et_message);
        //TextView
        tvName = view.findViewById(R.id.tv_name);
        tvNumber = view.findViewById(R.id.tv_number);
        tvSubject = view.findViewById(R.id.tv_subject);
        tvEmail = view.findViewById(R.id.tv_email);
        tvDate = view.findViewById(R.id.tv_pick_date);
        tvMessage = view.findViewById(R.id.tv_message);
        tvDatePicker = view.findViewById(R.id.tv_date_picker);
        commonhelper = new Commonhelper(getContext());
        //Button
        submit = view.findViewById(R.id.btn_submit);
        //Retrofit Instance
        apiService = ApiUtils.getApiService();


    }
    private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvName.getPaint().setShader(textShader);
        tvNumber.getPaint().setShader(textShader);
        tvSubject.getPaint().setShader(textShader);
        tvEmail.getPaint().setShader(textShader);
        tvDate.getPaint().setShader(textShader);
        tvMessage.getPaint().setShader(textShader);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);

        //Initializing Views
        initViews(view);
        //Date Picker Popup
        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        // Setting TextView Color Dynamically
        textViewColor();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show Loader
                commonhelper.ShowLoader();
                //Views to String
                String name = etName.getText().toString();
                String number = etNumber.getText().toString();
                String email = etEmail.getText().toString();
                String date = tvDatePicker.getText().toString();
                String message = etMessage.getText().toString();
                //Retrofit Network Call
                postNetworkCall(name, number, email, date, message);

            }
        });

        return view;
    }

    private void postNetworkCall(String name, String number, String email, String date, String message) {

        Call<MoreInfoResponse> call = apiService.postEnquiry(name, number, email, date, message);
        call.enqueue(new Callback<MoreInfoResponse>() {
            @Override
            public void onResponse(Call<MoreInfoResponse> call, Response<MoreInfoResponse> response) {
                if(response.isSuccessful()) {
                    MoreInfoData moreInfoData = response.body().getData();
                    commonhelper.ShowMesseage(moreInfoData.getMessage());
                    //Setting Values to default
                    etName.setText("");
                    etNumber.setText("");
                    etEmail.setText("");
                    etMessage.setText("");
                    tvDatePicker.setText("");
                    //Hide Loader
                    commonhelper.HideLoader();
                }
            }

            @Override
            public void onFailure(Call<MoreInfoResponse> call, Throwable t) {
                //Hide Loader
                commonhelper.HideLoader();
            }
        });

    }

    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1)
                        + "/" + String.valueOf(year);
                tvDatePicker.setText(date);
            }
        }, yy, mm, dd);
        datePicker.show();
    }
}
