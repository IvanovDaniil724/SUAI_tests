package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersAPI {
    @GET("common/GetUsers.php")
    Call<UserClass[]> getUsers(@Query("id") int userId, @Query("role") int role);
}
