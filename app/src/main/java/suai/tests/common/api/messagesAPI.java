package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface messagesAPI {
    @GET("messenger/chats/GetChat.php")
    Call<MessagesClass[]> getMessages(@Query("chat_id") int chatId);
}
