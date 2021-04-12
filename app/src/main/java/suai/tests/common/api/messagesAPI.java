package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface messagesAPI {
    @GET("messenger/chats/GetChat.php")
    Call<ChatClass[]> getChats(@Query("id") int userId, @Query("role") int role);
}
