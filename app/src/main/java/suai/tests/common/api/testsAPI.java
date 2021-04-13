package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public interface testsAPI
{
    @GET("tests/tests/GetTests.php")
    Call<ItemsPOJO[]> getTests();
}
