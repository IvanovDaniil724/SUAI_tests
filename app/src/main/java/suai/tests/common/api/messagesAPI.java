package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface messagesAPI {
    @GET("messenger/chats/GetChat.php")
    Call<MessagesClass[]> getMessages(@Query("chat_id") int chatId, @Query("role") String role, @Query("find") String find);


    @FormUrlEncoded
    @POST("messenger/chats/CreateMessage.php")
    Call<String> createMessage(@Field("chat") String chat, @Field("sender") String sender, @Field("message") String message);

    @FormUrlEncoded
    @POST("messenger/chats/ReadMessages.php")
    Call<String> readMessages(@Field("id") String id);

    @FormUrlEncoded
    @POST("messenger/chats/DeleteMessage.php")
    Call<String> deleteMessage(@Field("id") String id);

    @FormUrlEncoded
    @POST("messenger/chats/EditMessage.php")
    Call<String> editMessage(@Field("id") String id, @Field("new_message") String new_message);
}
