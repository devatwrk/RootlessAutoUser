package aara.tech.rootless_auto_user.ui.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aara.tech.rootless_auto_user.Adapters.MyVehicleAdapter;
import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.model.CarModelListData;
import aara.tech.rootless_auto_user.repository.model.CarModelResponse;
import aara.tech.rootless_auto_user.repository.model.LoginResponse;
import aara.tech.rootless_auto_user.repository.model.MyVehicleResponse;
import aara.tech.rootless_auto_user.repository.model.VehicleData;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyVehicleFragment extends Fragment {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyVehicleAdapter adapter;
    private List<VehicleData> vehicleDataList;
    private ImageView edit_iv, delete_iv;
    ApiService apiService;
    private Commonhelper commonhelper;
    private TextView oops_tv;
    private String uid;

    private List<CarModelListData> carModelListData;



    private void initViews(View view) {

        edit_iv = view.findViewById(R.id.edit_iv);
        delete_iv = view.findViewById(R.id.delete_iv);
        oops_tv = view.findViewById(R.id.oops_tv);

        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_vehicle, container, false);
        initViews(view);
        commonhelper.ShowLoader();

        carModelListNetworkCall();

        vehicleNetworkCall();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vehicleDataList.clear();
                commonhelper.ShowLoader();
                vehicleNetworkCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        uid = commonhelper.getSharedPreferences("current_user_id", null);

        /*selectYear(view, getContext());
        selectModel(view, getContext());*/

        return view;
    }

    private void vehicleNetworkCall() {
       /* if (vehicleDataList != null) {
            vehicleDataList.clear();
        }*/

        Call<MyVehicleResponse> call = apiService.getMyVehicleList(uid);

        call.enqueue(new Callback<MyVehicleResponse>() {
            @Override
            public void onResponse(Call<MyVehicleResponse> call, Response<MyVehicleResponse> response) {
                if (response.body().getData() != null) {
                    vehicleDataList = response.body().getData();
                    adapter = new MyVehicleAdapter(getActivity(), vehicleDataList, carModelListData);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    commonhelper.HideLoader();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    oops_tv.setVisibility(View.VISIBLE);
                    commonhelper.HideLoader();
                }

            }

            @Override
            public void onFailure(Call<MyVehicleResponse> call, Throwable t) {

            }
        });

    }

    private void carModelListNetworkCall() {
        Call<CarModelResponse> call = apiService.getCarModelList();
        call.enqueue(new Callback<CarModelResponse>() {
            @Override
            public void onResponse(Call<CarModelResponse> call, Response<CarModelResponse> response) {
                if (response.body().getData() != null) {
                    carModelListData = response.body().getData();
//                    for (CarModelListData carModel : carModelListData) {
//                        models.add(carModel.getModel());
//                    }
                }

            }

            @Override
            public void onFailure(Call<CarModelResponse> call, Throwable t) {

            }
        });

    }




}
