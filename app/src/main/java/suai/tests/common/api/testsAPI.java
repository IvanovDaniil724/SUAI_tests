package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public interface testsAPI
{
    @GET("tests/tests/GetTests.php")
    Call<ItemsPOJO[]> getTests(@Query("idUser") int idUser, @Query("role") int role);

    @GET("tests/tests/GetTestDetails.php")
    Call<ItemsPOJO[]> getTestDetails(@Query("testID") int testID, @Query("role") int role);

    //@GET("tests/tests/GetTeacherTests.php")
    //Call<ItemsPOJO[]> getTeacherTests(@Query("idUser") int idUser);
}
