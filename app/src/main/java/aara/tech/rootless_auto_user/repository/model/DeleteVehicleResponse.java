package aara.tech.rootless_auto_user.repository.model;

public class DeleteVehicleResponse {

    String Status;
    DeleteVehicleData data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public DeleteVehicleData getData() {
        return data;
    }

    public void setData(DeleteVehicleData data) {
        this.data = data;
    }
}
