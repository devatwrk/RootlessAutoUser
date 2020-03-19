package aara.tech.rootless_auto_user.repository.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://rootless.aaratechnologies.in/user/";

    public static ApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }

}
