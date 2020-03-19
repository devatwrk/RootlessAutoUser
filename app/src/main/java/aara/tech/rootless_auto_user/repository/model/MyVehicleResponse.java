package aara.tech.rootless_auto_user.repository.model;

import java.util.List;

public class MyVehicleResponse {

    private String Status;
    private List<VehicleData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<VehicleData> getData() {
        return data;
    }

    public void setData(List<VehicleData> data) {
        this.data = data;
    }
}
