package aara.tech.rootless_auto_user.repository.model;

public class MoreInfoResponse {

    String Status;
    MoreInfoData data;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public MoreInfoData getData() {
        return data;
    }

    public void setData(MoreInfoData data) {
        this.data = data;
    }



}
