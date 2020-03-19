package aara.tech.rootless_auto_user.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.VehicleCardEditActivity;
import aara.tech.rootless_auto_user.repository.model.CarModelListData;
import aara.tech.rootless_auto_user.repository.model.DeleteVehicleData;
import aara.tech.rootless_auto_user.repository.model.DeleteVehicleResponse;
import aara.tech.rootless_auto_user.repository.model.MyVehicleResponse;
import aara.tech.rootless_auto_user.repository.model.VehicleData;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.ui.Fragments.BookNowFragment;
import aara.tech.rootless_auto_user.ui.Fragments.MyVehicleFragment;
import aara.tech.rootless_auto_user.utils.Commonhelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyVehicleAdapter extends RecyclerView.Adapter<MyVehicleAdapter.ViewHolder>{

    private Context context;
    private List<VehicleData> vehicleDataList;
    private ApiService apiService;
    private Commonhelper commonhelper;
    private List<CarModelListData> carModelListData;
    private ArrayList<String> models;

    public MyVehicleAdapter(Context context, List<VehicleData> vehicleDataList, List<CarModelListData> carModelListData) {
        this.context = context;
        this.vehicleDataList = vehicleDataList;
        this.carModelListData = carModelListData;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_my_vehicle, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);
        models = new ArrayList<>();
       /* //refresh
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));*/
        return new MyVehicleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

//        VehicleData vehicleData = vehicleDataList.get(position);

        holder.plate_result_tv.setText(vehicleDataList.get(position).getName());
        holder.year_result_tv.setText(vehicleDataList.get(position).getYear());
        holder.engine_result_tv.setText(vehicleDataList.get(position).getEngine());
        holder.colour_result_tv.setText(vehicleDataList.get(position).getColour());
        holder.mileage_result_tv.setText(vehicleDataList.get(position).getMileage());

        String item_id = vehicleDataList.get(position).getModel();
        for (CarModelListData data : carModelListData) {
            if (item_id.equals(data.getModel_id())) {
                holder.model_result_tv.setText(data.getModel());
                return;
            }
        }

        final String id = vehicleDataList.get(position).getId();

        holder.edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = vehicleDataList.get(position).getName();
//                final String year = vehicleDataList.get(position).getYear();
//                final String model = vehicleDataList.get(position).getModel();
                final String engine = vehicleDataList.get(position).getEngine();
                final String colour = vehicleDataList.get(position).getColour();
                final String mileage = vehicleDataList.get(position).getMileage();
                alertEditPopUp(id, name, engine, colour, mileage);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeletePopUp(id, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vehicleDataList.size();
    }

    //    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username, last_msg;

        private TextView plate_result_tv;
        private TextView year_result_tv;
        private TextView model_result_tv;
        private TextView engine_result_tv;
        private TextView colour_result_tv;
        private TextView mileage_result_tv;
        private ImageView delete_iv;
        private ImageView edit_iv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            plate_result_tv = itemView.findViewById(R.id.plate_result_tv);
            model_result_tv = itemView.findViewById(R.id.model_result_tv);
            year_result_tv = itemView.findViewById(R.id.year_result_tv);
            engine_result_tv = itemView.findViewById(R.id.engine_result_tv);
            colour_result_tv = itemView.findViewById(R.id.colour_result_tv);
            mileage_result_tv = itemView.findViewById(R.id.mileage_result_tv);
            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);

        }
    }

    private void alertDeletePopUp(final String id, final int position){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete...");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                commonhelper.ShowLoader();
                // Write your code here to invoke YES event
                deleteNetworkCall(id, position);

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    private void deleteNetworkCall(String id, final int position) {

        Call<DeleteVehicleResponse> call = apiService.deleteVehicle(id);
        call.enqueue(new Callback<DeleteVehicleResponse>() {
            @Override
            public void onResponse(Call<DeleteVehicleResponse> call, Response<DeleteVehicleResponse> response) {
                if (response.isSuccessful()) {
                    DeleteVehicleData deleteVehicleData = response.body().getData();
                    vehicleDataList.remove(position);
                    notifyDataSetChanged();
                    commonhelper.ShowMesseage(deleteVehicleData.getMessage());
                    commonhelper.HideLoader();

                 /*   //Refreshing page
                    Call<MyVehicleResponse> callRefresh = apiService.getMyVehicleList();

                    call.enqueue(new Callback<MyVehicleResponse>() {
                        @Override
                        public void onResponse(Call<MyVehicleResponse> call, Response<MyVehicleResponse> response) {
                            vehicleDataList = response.body().getData();
                            adapter = new MyVehicleAdapter(context, vehicleDataList);
                            recyclerView.setAdapter(adapter);
                            commonhelper.HideLoader();
                        }

                        @Override
                        public void onFailure(Call<MyVehicleResponse> call, Throwable t) {

                        }
                    });*/

                }
            }

            @Override
            public void onFailure(Call<DeleteVehicleResponse> call, Throwable t) {
                commonhelper.ShowMesseage(t.getMessage());
                commonhelper.HideLoader();
            }
        });
    }

    private void alertEditPopUp(final String id, final String name, final String engine, final String colour, final String mileage){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Edit...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want Edit this?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_edit);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(context, VehicleCardEditActivity.class);
                commonhelper.setSharedPreferences("vehicle_id", id);
                commonhelper.setSharedPreferences("vehicle_plate", name);
                commonhelper.setSharedPreferences("vehicle_engine", engine);
                commonhelper.setSharedPreferences("vehicle_colour", colour);
                commonhelper.setSharedPreferences("vehicle_mileage", mileage);
                context.startActivity(intent);
                // Write your code here to invoke YES event
                Toast.makeText(context, "Welcome to Edit Screen", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }




}
