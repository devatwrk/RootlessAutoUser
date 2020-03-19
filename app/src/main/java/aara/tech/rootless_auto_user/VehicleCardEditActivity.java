package aara.tech.rootless_auto_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_user.repository.model.AddData;
import aara.tech.rootless_auto_user.repository.model.AddResponse;
import aara.tech.rootless_auto_user.repository.model.CarModelListData;
import aara.tech.rootless_auto_user.repository.model.CarModelResponse;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleCardEditActivity extends AppCompatActivity {

    private EditText etPlateNumber, etEngine, etMileage, etColour;
    private TextView tvPlateNumber, tvYear, tvSelectYourModel, tvEngine, tvMileage, tvColour;
    private String plate, engine, colour, mileage, id;
    private Spinner year_spinner, model_spinner;
    private Button submit;
    private Commonhelper commonhelper;
    private ApiService apiService;
    private List<CarModelListData> carModelListData;
    private ArrayList<String> models;
    private Context context;

    Intent intent;

    private void initViews() {
        context = VehicleCardEditActivity.this;
        //EditText
        etPlateNumber = findViewById(R.id.et_vehicle_plate);
        etEngine = findViewById(R.id.et_engine);
        etMileage = findViewById(R.id.et_mileage);
        etColour = findViewById(R.id.et_colour);
        //TextView
        tvPlateNumber = findViewById(R.id.tv_vehicle_plate);
        tvYear = findViewById(R.id.tv_Year);
        tvSelectYourModel = findViewById(R.id.tv_select_model);
        tvEngine = findViewById(R.id.tv_engine);
        tvMileage = findViewById(R.id.tv_mileage);
        tvColour = findViewById(R.id.tv_colour);
        //Spinner
        year_spinner = findViewById(R.id.year_spinner);
        model_spinner = findViewById(R.id.select_model_spinner);
        //Button
        submit = findViewById(R.id.btn_update);

        commonhelper = new Commonhelper(context);
        apiService = ApiUtils.getApiService();
        models = new ArrayList<>();

    }


    /*private void initViews() {
        context = getApplicationContext();
        //EditText
        etName = findViewById(R.id.et_name_car);
        etEngine = findViewById(R.id.et_engine);
        //TextView
        tvName = findViewById(R.id.tv_name);
        tvYear = findViewById(R.id.tv_Year);
        tvSelectYourModel = findViewById(R.id.tv_select_model);
        tvEngine = findViewById(R.id.tv_engine);
        //Button
        update_btn = findViewById(R.id.btn_update);
        //Spinner
        year_spinner = findViewById(R.id.year_spinner);
        model_spinner = findViewById(R.id.select_model_spinner);
        //Common Helper
        commonhelper = new Commonhelper(this);
        apiService = ApiUtils.getApiService();
        intent = getIntent();



    }*/
    /*private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvName.getPaint().setShader(textShader);
        tvYear.getPaint().setShader(textShader);
        tvSelectYourModel.getPaint().setShader(textShader);
        tvEngine.getPaint().setShader(textShader);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_card_edit);

        //Initializing Views
        initViews();

        // Setting TextView Color Dynamically
//        textViewColor();
        //Getting Intent Data
        setReceivedData();

        //Dummy Data For Spinners
        selectYear(context);
        carModelSpinnerListNetworkCall();

  /*      //Submit button clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ProgressBar Show
                commonhelper.ShowLoader();
                //getting selected items from spinner
                String year = year_spinner.getSelectedItem().toString();
                String model = model_spinner.getSelectedItem().toString();
                String name = etName.getText().toString();
                String engine = etEngine.getText().toString();
                String id = intent.getStringExtra("id");
                //Network Call to Post Data
                updateVehicleNetworkCall(id, name, model, year, engine);
            }
        });*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressBar Show
                commonhelper.ShowLoader();
                //getting selected items from spinner
                String name = etPlateNumber.getText().toString();
                String year = year_spinner.getSelectedItem().toString();
                String model = model_spinner.getSelectedItem().toString();
                String engine = etEngine.getText().toString();
                String colour = etColour.getText().toString();
                String mileage = etMileage.getText().toString();

                //Network Call to Post Data
                updateVehicleNetworkCall(id, name, model, year, engine, colour, mileage);
            }
        });

    }

    private void setReceivedData() {


        id = commonhelper.getSharedPreferences("vehicle_id", null);
        plate = commonhelper.getSharedPreferences("vehicle_plate", null);
        engine = commonhelper.getSharedPreferences("vehicle_engine", null);
        colour = commonhelper.getSharedPreferences("vehicle_colour", null);
        mileage = commonhelper.getSharedPreferences("vehicle_mileage", null);

        etPlateNumber.setText(plate);
        etEngine.setText(engine);
        etColour.setText(colour);
        etMileage.setText(mileage);

    }

   /* private void updateVehicleNetworkCall(String id, String name, String model, String year, String engine) {

        Call<AddResponse> call = apiService.updateVehicle(id, name, model, year, engine);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if(response.isSuccessful()) {
                    try {
                        AddData addData = response.body().getData();
                        //setting spinners to default value
                        selectYear(context);
                        selectModel(context);
                        //Setting EditTexts to default
                        etName.setText("");
                        etEngine.setText("");
                        //ProgressBar Hide
                        commonhelper.HideLoader();
                        //Toast
                        commonhelper.ShowMesseage(addData.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {
                //ProgressBar Hide
                commonhelper.HideLoader();
                //Toast
                commonhelper.ShowMesseage(t.getMessage());
            }
        });
    }*/


    private void updateVehicleNetworkCall(String id, String name, String model, String year, String engine, String colour, String mileage) {

        Call<AddResponse> call = apiService.updateVehicle(id, name, model, year, engine, colour, mileage);

        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        AddData addData = response.body().getData();
                        //setting spinners to default value
                       /* selectYear(context);
                        selectModel(context);*/
                        etPlateNumber.setText("");
                        etEngine.setText("");
                        etMileage.setText("");
                        etColour.setText("");
                        //ProgressBar Hide
                        commonhelper.HideLoader();
                        //Toast
                        commonhelper.ShowMesseage(addData.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {
                //ProgressBar Hide
                commonhelper.HideLoader();
                //Toast
                commonhelper.ShowMesseage(t.getMessage());
            }
        });

    }

    private void selectYear(Context context) {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Your Year:-");
        years.add(Integer.toString(1900));
        for (int i = 1991; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, years);
        year_spinner.setAdapter(adapter);
    }

    private void selectModel(Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, models);
        model_spinner.setAdapter(adapter);
    }

    private void carModelSpinnerListNetworkCall() {
        Call<CarModelResponse> call = apiService.getCarModelList();
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
}
