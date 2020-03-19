package aara.tech.rootless_auto_user.repository.model;

public class AddResponse {

    String Status;
    AddData data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public AddData getData() {
        return data;
    }

    public void setData(AddData data) {
        this.data = data;
    }
}
