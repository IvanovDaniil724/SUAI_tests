package suai.tests.common.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersAPI {
    @GET("common/GetStudentsOrTeachers.php")
    Call<UserClass[]> getStudentsOrTeachers(@Query("idUser") int userId, @Query("role") int role);
}
