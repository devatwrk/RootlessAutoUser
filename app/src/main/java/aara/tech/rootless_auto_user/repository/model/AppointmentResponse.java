package aara.tech.rootless_auto_user.repository.model;

import java.util.List;

public class AppointmentResponse {
    private String Status;
    private List<AppointmentData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<AppointmentData> getData() {
        return data;
    }

    public void setData(List<AppointmentData> data) {
        this.data = data;
    }
}
