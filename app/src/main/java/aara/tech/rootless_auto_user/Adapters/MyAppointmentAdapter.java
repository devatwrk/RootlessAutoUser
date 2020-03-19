package aara.tech.rootless_auto_user.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import aara.tech.rootless_auto_user.R;
import aara.tech.rootless_auto_user.repository.model.AppointmentData;
import aara.tech.rootless_auto_user.repository.remote.ApiService;
import aara.tech.rootless_auto_user.repository.remote.ApiUtils;
import aara.tech.rootless_auto_user.utils.Commonhelper;

public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.ViewHolder>{
    private Context context;
    private List<AppointmentData> appointmentDataList;
    private ApiService apiService;
    private Commonhelper commonhelper;

    public MyAppointmentAdapter(Context context, List<AppointmentData> appointmentDataList) {
        this.context = context;
        this.appointmentDataList = appointmentDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_appointment, parent, false);
        apiService = ApiUtils.getApiService();
        commonhelper = new Commonhelper(context);

        return new MyAppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.date_result_tv.setText(appointmentDataList.get(position).getName());
        holder.car_result_tv.setText(appointmentDataList.get(position).getChoose_car());
        holder.model_result_tv.setText(appointmentDataList.get(position).getChoose_model());
        holder.engine_result_tv.setText(appointmentDataList.get(position).getEngine());
        holder.services_result_tv.setText(appointmentDataList.get(position).getService());
        holder.price_result_tv.setText(appointmentDataList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return appointmentDataList.size();
    }

//    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date_result_tv;
        public TextView car_result_tv;
        public TextView model_result_tv;
        public TextView engine_result_tv;
        public TextView services_result_tv;
        public TextView price_result_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_result_tv = itemView.findViewById(R.id.date_result_tv);
            car_result_tv = itemView.findViewById(R.id.car_result_tv);
            model_result_tv = itemView.findViewById(R.id.model_result_tv);
            engine_result_tv = itemView.findViewById(R.id.engine_result_tv);
            services_result_tv = itemView.findViewById(R.id.services_result_tv);
            price_result_tv = itemView.findViewById(R.id.price_result_tv);

        }
    }

}



