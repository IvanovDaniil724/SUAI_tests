package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface commonAPI
{
    @GET("common/GetUsers.php")
    Call<ItemsPOJO[]> getUsers();
}
