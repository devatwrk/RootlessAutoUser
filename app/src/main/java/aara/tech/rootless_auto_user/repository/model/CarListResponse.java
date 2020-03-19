package aara.tech.rootless_auto_user.repository.model;

import java.util.List;

public class CarListResponse {
    private String Status;
    private List<CarListData> data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<CarListData> getData() {
        return data;
    }

    public void setData(List<CarListData> data) {
        this.data = data;
    }
}
