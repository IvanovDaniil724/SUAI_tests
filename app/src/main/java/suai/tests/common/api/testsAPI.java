package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public interface testsAPI
{
    @GET("tests/tests/GetTests.php")
    Call<ItemsPOJO[]> getTests(@Query("idUser") int idUser, @Query("role") int role);

    @GET("tests/tests/GetTestDetails.php")
    Call<ItemsPOJO[]> getTestDetails(@Query("testID") int testID, @Query("role") int role);

    @FormUrlEncoded
    @POST("tests/tests/SetStudentMark.php")
    Call<String[]> setStudentMark(@Field("idTest") String idTest, @Field("mark") String mark, @Field("comment") String comment);
}
