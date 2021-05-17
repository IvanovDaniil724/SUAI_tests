package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public interface commonAPI
{
    @GET("common/statistics/GetSubjects.php")
    Call<ItemsPOJO[]> getUserSubjects(@Query("idUser") int idUser, @Query("role") int role);

    @GET("common/statistics/GetStatistics.php")
    Call<ItemsPOJO[]> getUserStatistics(@Query("idUser") int idUser, @Query("role") int role, @Query("subject") String subject);

    @GET("common/statistics/GetTeacherGroups.php")
    Call<ItemsPOJO[]> getTeacherGroups(@Query("idUser") int idUser, @Query("subject") String subject);

    @GET("common/statistics/GetGroupsAverageMarks.php")
    Call<ItemsPOJO[]> getTeacherGroupsAverageMarks(@Query("idUser") int idUser, @Query("subject") String subject);

    @GET("common/statistics/GetGroupMarks.php")
    Call<ItemsPOJO[]> getGroupMarks(@Query("subject") String subject, @Query("group") String group);

    @FormUrlEncoded
    @POST("common/UserAuth.php")
    Call<String[]> getUser(@Field("email") String email, @Field("password") String password);
}
