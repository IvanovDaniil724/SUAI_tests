package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;
import suai.tests.common.api.pojo.ItemsPOJO;
import suai.tests.common.api.pojo.tests.TestPOJO;

public interface commonAPI
{
    @GET("common/GetUsers.php")
    Call<ItemsPOJO[]> getUsers();
}
