package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import suai.tests.common.api.pojo.tests.TestPOJO;

public interface messengerAPI {
    @GET("messenger/chats/GetChats.php")
    Call<ChatClass[]> getChats(@Query("id") int userId,@Query("role") int role);

    @GET("common/GetChatsWithUser.php")
    Call<String> getChatsWithUser(@Query("teacher") String teacher, @Query("student") String student);

    @FormUrlEncoded
    @POST("messenger/chats/CreateNewChat.php")
    Call<String[]> createNewChat(@Field("teacher") String teacher, @Field("student") String student);
}
