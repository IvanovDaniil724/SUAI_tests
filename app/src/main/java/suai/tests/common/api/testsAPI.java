package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface testsAPI
{
    @GET("tests/tests/GetTests.php")
    Call<ItemsPOJO[]> getTests();
}
