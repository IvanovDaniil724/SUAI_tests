package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public interface commonAPI
{
    @GET("common/GetUsers.php")
    Call<ItemsPOJO[]> getUsers();

    @FormUrlEncoded
    @POST("common/UserAuth.php")
    Call<String[]> getUser(@Field("email") String email, @Field("password") String password);
}
