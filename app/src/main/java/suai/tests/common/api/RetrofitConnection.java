package suai.tests.common.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitConnection //extends AppCompatActivity
{
    public static final String BASE_URL = "http://f0530060.xsph.ru/api/"; //"http://10.0.2.2:80/suailabs/api/";
    public Retrofit retrofit;
    public static testsAPI testsApi; public static messengerAPI messengerApi; public static commonAPI commonAPI;
    public static messagesAPI messagesApi;

    private OkHttpClient CreateOkHttpClient()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    public void CreateConnection()
    {
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(CreateOkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        testsApi = retrofit.create(testsAPI.class);
        messengerApi = retrofit.create(messengerAPI.class);
        messagesApi = retrofit.create(messagesAPI.class);
        commonAPI = retrofit.create(commonAPI.class);
    }
}
