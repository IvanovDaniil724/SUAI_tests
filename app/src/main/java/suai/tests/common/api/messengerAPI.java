package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import suai.tests.common.api.pojo.tests.TestPOJO;

public interface messengerAPI {
    @GET("messenger/chats/GetChats.php")
    Call<ChatClass[]> getChats(@Query("id") int userId,@Query("role") int role);
}
