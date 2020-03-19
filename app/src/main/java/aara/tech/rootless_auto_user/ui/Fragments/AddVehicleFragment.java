package aara.tech.rootless_auto_user.ui.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.model.AddData;
import aara.tech.rootless_auto_user.repository.model.AddResponse;
import aara.tech.rootless_auto_user.repository.model.CarListData;
import aara.tech.rootless_auto_user.repository.model.CarModelListData;
import aara.tech.rootless_auto_user.repository.model.CarModelResponse;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddVehicleFragment extends Fragment {

    private EditText etPlateNumber, etEngine, etMileage, etColour;
    private TextView tvPlateNumber, tvYear, tvSelectYourModel, tvEngine, tvMileage, tvColour;
    private Spinner year_spinner, model_spinner;
    private Button submit;
    private Commonhelper commonhelper;
    private ApiService apiService;
    private List<CarModelListData> carModelListData;
    private ArrayList<String> models;
    private Context context;

    private String uid;

    private boolean mModelSpinnerInitialized;
    private String model_id;

    private void initViews(View view) {
        context = getContext();
        //EditText
        etPlateNumber = view.findViewById(R.id.et_vehicle_plate);
        etEngine = view.findViewById(R.id.et_engine);
        etMileage = view.findViewById(R.id.et_mileage);
        etColour = view.findViewById(R.id.et_colour);
        //TextView
        tvPlateNumber = view.findViewById(R.id.tv_vehicle_plate);
        tvYear = view.findViewById(R.id.tv_Year);
        tvSelectYourModel = view.findViewById(R.id.tv_select_model);
        tvEngine = view.findViewById(R.id.tv_engine);
        tvMileage = view.findViewById(R.id.tv_mileage);
        tvColour = view.findViewById(R.id.tv_colour);
        //Spinner
        year_spinner = view.findViewById(R.id.year_spinner);
        model_spinner = view.findViewById(R.id.select_model_spinner);
        //Button
        submit = view.findViewById(R.id.btn_update);

        commonhelper = new Commonhelper(context);
        apiService = ApiUtils.getApiService();
        models = new ArrayList<>();

        //Boolean for spinner
        mModelSpinnerInitialized = false;

    }
    private void textViewColor() {
        Shader textShader = new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvPlateNumber.getPaint().setShader(textShader);
        tvYear.getPaint().setShader(textShader);
        tvSelectYourModel.getPaint().setShader(textShader);
        tvEngine.getPaint().setShader(textShader);
        tvMileage.getPaint().setShader(textShader);
        tvColour.getPaint().setShader(textShader);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
//        textViewColor();

        //Dummy Data For Spinners
        selectYear(context);
        carModelSpinnerListNetworkCall();

        uid = commonhelper.getSharedPreferences("current_user_id", null);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ProgressBar Show
                commonhelper.ShowLoader();
                //getting selected items from spinner
                String name = etPlateNumber.getText().toString();
                String year = year_spinner.getSelectedItem().toString();
                String model = model_id;
                String engine = etEngine.getText().toString();
                String colour = etColour.getText().toString();
                String mileage = etMileage.getText().toString();

                //Network Call to Post Data
                addVehicleNetworkCall(name, model, year, engine, colour, mileage, view);
            }
        });

        //      Spinner Item Selected Listeners.............................................................
        model_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mModelSpinnerInitialized) {
                    mModelSpinnerInitialized = true;
                    return;
                }
                // do stuff
                String item = model_spinner.getSelectedItem().toString();
                for (CarModelListData data : carModelListData) {
                    if (item.equals(data.getModel())) {
                        model_id = data.getModel_id();
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

    private void addVehicleNetworkCall(String name, String model, String year, String engine, String colour, String mileage, final View view) {

        Call<AddResponse> call = apiService.addVehicle(uid, name, model, year, engine, colour, mileage);

        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        AddData addData = response.body().getData();
                        //setting spinners to default value
                        selectYear(context);
                        selectModel(context);
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




   /* EditText etName, etEngine;
    TextView tvName, tvYear, tvSelectYourModel, tvEngine;
    Spinner year_spinner, model_spinner;
    Button submit;
    Commonhelper commonhelper;
    ApiService apiService;


    private void initViews(View view) {
        //EditText
        etName = view.findViewById(R.id.et_name_car);
        etEngine = view.findViewById(R.id.et_engine);
        //TextView
        tvName = view.findViewById(R.id.tv_name);
        tvYear = view.findViewById(R.id.tv_Year);
        tvSelectYourModel = view.findViewById(R.id.tv_select_model);
        tvEngine = view.findViewById(R.id.tv_engine);
        //Button
        submit = view.findViewById(R.id.btn_submit);
        //Spinner
        year_spinner = view.findViewById(R.id.year_spinner);
        model_spinner = view.findViewById(R.id.select_model_spinner);
        //Common Helper
        commonhelper = new Commonhelper(getContext());
        apiService = ApiUtils.getApiService();

    }
    private void textViewColor() {
        Shader textShader=new LinearGradient(0, 0, 150, 20,
                new int[]{Color.parseColor("#9C27B0"), Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        tvName.getPaint().setShader(textShader);
        tvYear.getPaint().setShader(textShader);
        tvSelectYourModel.getPaint().setShader(textShader);
        tvEngine.getPaint().setShader(textShader);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        //Initializing Views
        initViews(view);

        // Setting TextView Color Dynamically
        textViewColor();

        //Dummy Data For Spinners
        selectYear(view, getContext());
        selectModel(view, getContext());

        //Submit button clicked
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
                //Network Call to Post Data
                addVehicleNetworkCall(name, model, year, engine, view);
            }
        });



        return view;
    }

    private void addVehicleNetworkCall(String name, String model, String year, String engine, final View view) {
        Call<AddResponse> call = apiService.addVehicle(name, model, year, engine);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if(response.isSuccessful()) {
                    try {
                        AddData addData = response.body().getData();
                        //setting spinners to default value
                        selectYear(view, getContext());
                        selectModel(view, getContext());
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
    }


    private void selectYear(View view, Context context) {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add("Your Year:-");
        years.add(Integer.toString(1900));
        for (int i = 1991; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        year_spinner.setAdapter(adapter);
    }

    private void selectModel(View view, Context context) {
        ArrayList<String> models = new ArrayList<String>();
        models.add("Your Model:-");
        models.add("R8");
        models.add("A8");
        models.add("Aventador");
        models.add("BMW X5");
        models.add("DC Avanti");
        models.add("Honda City");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, models);
        model_spinner.setAdapter(adapter);
    }*/

}
