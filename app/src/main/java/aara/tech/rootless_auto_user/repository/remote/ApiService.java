package aara.tech.rootless_auto_user.repository.remote;

import aara.tech.rootless_auto_user.repository.model.AddResponse;
import aara.tech.rootless_auto_user.repository.model.AppointmentResponse;
import aara.tech.rootless_auto_user.repository.model.CarListResponse;
import aara.tech.rootless_auto_user.repository.model.CarModelResponse;
import aara.tech.rootless_auto_user.repository.model.DeleteVehicleResponse;
import aara.tech.rootless_auto_user.repository.model.LoginResponse;
import aara.tech.rootless_auto_user.repository.model.MechanicListResponse;
import aara.tech.rootless_auto_user.repository.model.MoreInfoResponse;
import aara.tech.rootless_auto_user.repository.model.MyVehicleResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("Api.php?apicall=login")
    Call<LoginResponse> login (
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("forgateapi.php")
    Call<ResponseBody> forgetPassword (
            @Field("femail") String femail
    );

    @FormUrlEncoded
    @POST("Api.php?apicall=register")
    Call<ResponseBody> createUser(
            @Field("email") String email,
            @Field("uname") String uname,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("password") String password
    );
//..................................................................................................

    @GET("objects/modelapi.php")
    Call<CarModelResponse> getCarModelList();

    @GET("objects/modelapi.php")
    Call<CarModelResponse> getCarModelDependentList(
            @Query("car_id") String car_id
    );

    /*@GET("user/object/vehicleapi.php")
    Call<MyVehicleResponse> getMyVehicleList();*/

    @GET("vehicle/api.php")
    Call<MyVehicleResponse> getMyVehicleList(
            @Query("id") String uid
    );

    @GET("mechanic/api.php")
    Call<MechanicListResponse> getMechanicList();

    @GET("objects/carapi.php")
    Call<CarListResponse> getCarList();

    @GET("user/object/bookapi.php")
    Call<AppointmentResponse> getAppointmentList(
            @Query("id") String uid
    );




    /*@FormUrlEncoded
    @POST("user/object/vehicleapi.php")
    Call<AddResponse> addVehicle(
            @Field("name") String name,
            @Field("model") String model,
            @Field("year") String year,
            @Field("engine") String engine
            @Field("engine") String engine
            @Field("engine") String engine
    );*/

    @FormUrlEncoded
    @POST("user/object/bookapi.php")
    Call<AddResponse> addBooking(
            @Field("uid") String uid,
            @Field("year") String year,
            @Field("choose_car") String choose_car,
            @Field("choose_model") String choose_model,
            @Field("engine") String engine,
            @Field("zip_code") String zip_code,
            @Field("gpscoordinate") String gpscoordinate,
            @Field("service") String service,
            @Field("price") String price,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("vehicle/api.php")
    Call<AddResponse> addVehicle(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("model") String model,
            @Field("year") String year,
            @Field("engine") String engine,
            @Field("colour") String colour,
            @Field("mileage") String mileage
    );

    @FormUrlEncoded
    @PUT("vehicle/api.php")
    Call<AddResponse> updateVehicle(
            @Field("id") String id,
            @Field("name") String name,
            @Field("model") String model,
            @Field("year") String year,
            @Field("engine") String engine,
            @Field("colour") String colour,
            @Field("mileage") String mileage
    );

    @DELETE("user/object/vehicleapi.php")
    Call<DeleteVehicleResponse> deleteVehicle(
            @Query("id") String id
    );

    @FormUrlEncoded
    @POST("user/object/infoapi.php")
    Call<MoreInfoResponse> postEnquiry(
            @Field("customer_name") String customer_name,
            @Field("phone_number") String phone_number,
            @Field("email") String email,
            @Field("appointment_date") String appointment_date,
            @Field("message") String message
    );

}
