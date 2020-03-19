package aara.tech.rootless_auto_user.repository.model;

public class AppointmentData {
    private String id;
    private String date;
    private String choose_car;
    private String choose_model;
    private String engine;
    private String service;
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChoose_car() {
        return choose_car;
    }

    public void setChoose_car(String choose_car) {
        this.choose_car = choose_car;
    }

    public String getChoose_model() {
        return choose_model;
    }

    public void setChoose_model(String choose_model) {
        this.choose_model = choose_model;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
