package aara.tech.rootless_auto_user.ui.Fragments;

import android.content.Context;
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

import java.util.List;

import aara.tech.rootless_auto_user.Adapters.MyAppointmentAdapter;
import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.model.AppointmentData;
import aara.tech.rootless_auto_user.repository.model.AppointmentResponse;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAppointmentFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyAppointmentAdapter adapter;
    private List<AppointmentData> appointmentDataList;
    private ImageView edit_iv, delete_iv;
    private ApiService apiService;
    private Commonhelper commonhelper;
    private TextView oops_tv;
    private Context context;


    String current_user_id;

    private void initViews(View view) {
        context = getActivity();
//        edit_iv = view.findViewById(R.id.edit_iv);
//        delete_iv = view.findViewById(R.id.delete_iv);
        oops_tv = view.findViewById(R.id.oops_tv);

        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(getContext());

        current_user_id = commonhelper.getSharedPreferences("current_user_id", null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_appointment, container, false);

        initViews(view);
        commonhelper.ShowLoader();

        appointmentNetworkCall(current_user_id);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                appointmentDataList.clear();
                commonhelper.ShowLoader();
                appointmentNetworkCall(current_user_id);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void appointmentNetworkCall(String uid) {
        Call<AppointmentResponse> call = apiService.getAppointmentList(uid);

        call.enqueue(new Callback<AppointmentResponse>() {
            @Override
            public void onResponse(Call<AppointmentResponse> call, Response<AppointmentResponse> response) {
                if (response.body().getData() != null) {
                    appointmentDataList = response.body().getData();
                    adapter = new MyAppointmentAdapter(context, appointmentDataList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();
                    commonhelper.HideLoader();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    oops_tv.setVisibility(View.VISIBLE);
                    commonhelper.HideLoader();
                }
            }

            @Override
            public void onFailure(Call<AppointmentResponse> call, Throwable t) {

            }
        });

    }


}
