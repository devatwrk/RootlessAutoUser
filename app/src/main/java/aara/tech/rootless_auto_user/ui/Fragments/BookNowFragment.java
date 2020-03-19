package aara.tech.rootless_auto_user.ui.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.model.AddData;
import aara.tech.rootless_auto_user.repository.model.AddResponse;
import aara.tech.rootless_auto_user.repository.model.CarListData;
import aara.tech.rootless_auto_user.repository.model.CarListResponse;
import aara.tech.rootless_auto_user.repository.model.CarModelListData;
import aara.tech.rootless_auto_user.repository.model.CarModelResponse;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class BookNowFragment extends Fragment {

    private EditText et_engine, et_zip_code, et_location;
    private TextView tv_ac_price, tv_airbag_price, tv_battery_price, tv_break_price, tv_speed_meter_price, tv_booking_date_picker, tv_model_default;
    private CheckBox ac_checkbox, airbag_checkbox, battery_checkbox, break_checkbox, speed_meter_checkbox;
    private Spinner year_spinner, choose_car_spinner, select_model_spinner;

    String all_checkbox, year, choose_car, choose_model, engine, zip_code, gpscoordinate, service, price, status;

    private boolean mCarSpinnerInitialized;

    int total_price;


    //Location
    private ImageView iv_picker;
    int PLACE_PICKER_REQUEST = 1;

    //Time Utils
    private int mYear, mMonth, mDay, mHour, mMinute;

    private List<CarListData> carListData;
    private List<CarModelListData> carModelListData;

    private ArrayList<String> carList;
    private ArrayList<String> models;

    private Commonhelper commonhelper;
    private ApiService apiService;
    private ArrayList<String> mechanic;
    private Context context;
    private String uid;

    private Button submit;

    private void initViews(View view) {

        context = getContext();
        //TextView
        tv_ac_price = view.findViewById(R.id.tv_ac_price);
        tv_airbag_price = view.findViewById(R.id.tv_airbag_price);
        tv_battery_price = view.findViewById(R.id.tv_battery_price);
        tv_break_price = view.findViewById(R.id.tv_break_price);
        tv_speed_meter_price = view.findViewById(R.id.tv_speed_meter_price);
        tv_booking_date_picker = view.findViewById(R.id.tv_booking_date_picker);
        tv_model_default = view.findViewById(R.id.tv_model_default);

        //EditText
        et_engine = view.findViewById(R.id.et_engine);
        et_zip_code = view.findViewById(R.id.et_zip_code);


        //CheckBoxes
        ac_checkbox = view.findViewById(R.id.ac_checkbox);
        airbag_checkbox = view.findViewById(R.id.airbag_checkbox);
        battery_checkbox = view.findViewById(R.id.battery_checkbox);
        break_checkbox = view.findViewById(R.id.break_checkbox);
        speed_meter_checkbox = view.findViewById(R.id.speed_meter_checkbox);

        //Spinner
        year_spinner = view.findViewById(R.id.year_spinner);
        choose_car_spinner = view.findViewById(R.id.choose_car_spinner);
        select_model_spinner = view.findViewById(R.id.select_model_spinner);

        //Location
        iv_picker = view.findViewById(R.id.iv_picker);
        et_location = view.findViewById(R.id.et_location);

        //Button
        submit = view.findViewById(R.id.btn_update);

        //Boolean for spinner
        mCarSpinnerInitialized = false;

        //Common Helper
        commonhelper = new Commonhelper(getContext());
        apiService = ApiUtils.getApiService();
        carList = new ArrayList<>();
        models = new ArrayList<>();
    }

/*    private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvYear.getPaint().setShader(textShader);
        tvChooseCar.getPaint().setShader(textShader);
        tvSelectYourModel.getPaint().setShader(textShader);
        tvChooseEngine.getPaint().setShader(textShader);
        tvZipCode.getPaint().setShader(textShader);
        tv_services.getPaint().setShader(textShader);
        tv_select_mechanic.getPaint().setShader(textShader);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_now, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
//        textViewColor();

//        mechanicNetworkCall();
        //Dummy Data For Spinners
        selectYear(context);
        carListNetworkCall();


        tv_booking_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        uid = commonhelper.getSharedPreferences("current_user_id", null);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                commonhelper.ShowLoader();
                all_checkbox = "";
                total_price = 0;

                if (ac_checkbox.isChecked()) {
                    all_checkbox = all_checkbox + ac_checkbox.getText() + ",";
                    total_price = total_price +  530;
                }
                if (airbag_checkbox.isChecked()) {
                    all_checkbox = all_checkbox + airbag_checkbox.getText() + ",";
                    total_price = total_price +  540;
                }
                if (battery_checkbox.isChecked()) {
                    all_checkbox = all_checkbox + battery_checkbox.getText() + ",";
                    total_price = total_price +  550;
                }
                if (break_checkbox.isChecked()) {
                    all_checkbox = all_checkbox + break_checkbox.getText() + ",";
                    total_price = total_price +  560;
                }
                if (speed_meter_checkbox.isChecked()) {
                    all_checkbox = all_checkbox + speed_meter_checkbox.getText() + ",";
                    total_price = total_price +  570;
                }

                year = year_spinner.getSelectedItem().toString();
                choose_car = choose_car_spinner.getSelectedItem().toString();
                choose_model = select_model_spinner.getSelectedItem().toString();
                engine = et_engine.getText().toString();
                zip_code = et_zip_code.getText().toString();
                gpscoordinate = et_location.getText().toString();
                service = all_checkbox;
                price = "" + total_price;
                status = "";

//                Toast.makeText(context,  all_checkbox , Toast.LENGTH_LONG).show();
              /*  //getting selected items from spinner
                String c_name = customer_name_spinner.getSelectedItem().toString();
                String b_time = tv_booking_time_picker.getText().toString();
                String p_required = product_required_spinner.getSelectedItem().toString();
                String s_type = et_service_type.getText().toString();
                String b_date = tv_booking_date_picker.getText().toString();
                String a_service = assigned_service_center_spinner.getSelectedItem().toString();*/

                //Network Call to Post Data
                addBookingNetworkCall(year, choose_car, choose_model, engine, zip_code, gpscoordinate, service, price, status);
            }
        });

        // Location Place Picker
        iv_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity())
                            , PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        //      Spinner Item Selected Listeners.............................................................
        choose_car_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mCarSpinnerInitialized) {
                    mCarSpinnerInitialized = true;
                    return;
                }
                // do stuff
                String item = choose_car_spinner.getSelectedItem().toString();
                models.clear();
                for (CarListData data : carListData) {
                    if (item.equals(data.getCars())) {
                        String car_id = data.getCar_id();
                        carModelSpinnerListNetworkCall(car_id);
                        tv_model_default.setVisibility(View.GONE);
                        select_model_spinner.setVisibility(View.VISIBLE);
                        return;
                    }
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        return view;
    }

    //Network Calls
    private void carListNetworkCall() {
        Call<CarListResponse> call = apiService.getCarList();
        call.enqueue(new Callback<CarListResponse>() {
            @Override
            public void onResponse(Call<CarListResponse> call, Response<CarListResponse> response) {
                if (response.body().getData() != null) {
                    carListData = response.body().getData();
                    for (CarListData data : carListData) {
                        carList.add(data.getCars());
                    }
                    selectCar(context);
                }
            }

            @Override
            public void onFailure(Call<CarListResponse> call, Throwable t) {

            }
        });

    }

    private void carModelSpinnerListNetworkCall(String car_id) {
        Call<CarModelResponse> call = apiService.getCarModelDependentList(car_id);
        call.enqueue(new Callback<CarModelResponse>() {
            @Override
            public void onResponse(Call<CarModelResponse> call, Response<CarModelResponse> response) {
                if (response.body().getData() != null) {
                    carModelListData = response.body().getData();
                    for (CarModelListData carModel : carModelListData) {
                        models.add(carModel.getModel());
                    }
                    selectModel(context);
                }
            }

            @Override
            public void onFailure(Call<CarModelResponse> call, Throwable t) {

            }
        });

    }

    private void addBookingNetworkCall(String year, String choose_car, String choose_model, String engine, String zip_code, String gpscoordinate, String service, String price, String status) {
        Call<AddResponse> call = apiService.addBooking(uid, year, choose_car, choose_model, engine, zip_code, gpscoordinate, service, price, status);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                try {
                    AddData data = response.body().getData();

                    //Setting EditTexts to default
                    et_engine.setText("");
                    et_zip_code.setText("");
                    et_location.setText("");
                    tv_booking_date_picker.setText("");

                    //Checkbox
                    if (ac_checkbox.isChecked()) {
                        ac_checkbox.setChecked(false);
                    }
                    if (airbag_checkbox.isChecked()) {
                        airbag_checkbox.setChecked(false);
                    }
                    if (battery_checkbox.isChecked()) {
                        battery_checkbox.setChecked(false);
                    }
                    if (break_checkbox.isChecked()) {
                        break_checkbox.setChecked(false);
                    }
                    if (speed_meter_checkbox.isChecked()) {
                        speed_meter_checkbox.setChecked(false);
                    }

                    selectYear(context);
                    selectCar(context);
                    selectModel(context);
                    //ProgressBar Hide
                    commonhelper.HideLoader();
                    //Toast
                    commonhelper.ShowMesseage(data.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {
                //ProgressBar Hide
                commonhelper.HideLoader();
            }
        });
    }


    private void selectYear(Context context) {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Your Year :-");
        years.add(Integer.toString(1900));
        for (int i = 1991; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        year_spinner.setAdapter(adapter);
    }

    private void selectCar(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, carList);
        choose_car_spinner.setAdapter(adapter);
    }

    private void selectModel(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, models);
        select_model_spinner.setAdapter(adapter);
    }

    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                        + "/" + String.valueOf(year);
                tv_booking_date_picker.setText(date);
            }
        }, yy, mm, dd);
        datePicker.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(context, data);
                StringBuilder stringBuilder = new StringBuilder();
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String latlng = latitude + ", " + longitude;
                et_location.setText(latlng);
            }
        }
    }

}
