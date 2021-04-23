package suai.tests.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.activities.fragments.AccountFragment;
import suai.tests.common.AlertDialogBuilder;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.commonAPI;
import suai.tests.common.api.pojo.common.UserPOJO;

public class AuthorizationActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        getSupportActionBar().hide();

        findViewById(R.id.AuthSignInButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText emailEditText = findViewById(R.id.AuthEmailEditText),
                         passwordEditText = findViewById(R.id.AuthPasswordEditText);
                String emailText = emailEditText.getText().toString(),
                       passwordText = passwordEditText.getText().toString();

                if (emailText.equals("") || passwordText.equals(""))
                {
                    if (emailText.equals(""))
                    {
                        new AlertDialogBuilder(AuthorizationActivity.this)
                                .alert("Ошибка авторизации", "Поле электронной почты должно быть заполнено");
                    }
                    else
                    {
                        new AlertDialogBuilder(AuthorizationActivity.this)
                                .alert("Ошибка авторизации", "Поле пароля должно быть заполнено");
                    }
                }
                else
                {
                    if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
                        {
                            startLoading();

                            final HashCode password = Hashing.sha1().hashString(passwordText, Charset.defaultCharset());

                            commonAPI service = RetrofitConnection.commonAPI;
                            Call<String[]> call = service.getUser(emailText, password.toString());
                            call.enqueue(new Callback<String[]>()
                            {
                                @Override
                                public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response)
                                {
                                    String[] user = response.body();

                                    if (user[0].equals("unknown"))
                                    {
                                        endLoading();
                                        new AlertDialogBuilder(AuthorizationActivity.this)
                                                .alert("Ошибка авторизации", "Данного пользователя не существует.\n" +
                                                        "Пожалуйста, введите корректные данные и повторите попытку.");
                                    }
                                    else if (user[0].equals("access denied"))
                                    {
                                        endLoading();
                                        new AlertDialogBuilder(AuthorizationActivity.this)
                                                .alert("Ошибка авторизации", "Пароль введён неверно.\n" +
                                                        "Пожалуйста, введите корректные данные и повторите попытку.");
                                    }
                                    else
                                    {
                                        AccountFragment.idUser = Integer.parseInt(user[0]);
                                        AccountFragment.email = user[1];
                                        AccountFragment.lastName = user[2];
                                        AccountFragment.firstName = user[3];
                                        AccountFragment.patronymic = user[4];

                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
                                        try { calendar.setTime(sdf.parse(user[5])); }
                                        catch (ParseException e) { e.printStackTrace(); }
                                        AccountFragment.birthDay = calendar;

                                        AccountFragment.role = Integer.parseInt(user[6]);

                                        AccountFragment.data = new String[user.length - 7];
                                        for (int i = 0; i < AccountFragment.data.length; i++) { AccountFragment.data[i] = user[i + 7]; }

                                        endLoading();
                                        startActivity(new Intent(AuthorizationActivity.this, MainActivity.class)); finish();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<String[]> call, @NonNull Throwable t)
                                {
                                    endLoading();
                                    new AlertDialogBuilder(AuthorizationActivity.this)
                                            .alert("Ошибка подключения", "Невозможно подключиться к серверу.\n" +
                                                    "Проверьте соединение с Интернетом или попробуйте позже.");
                                    Log.e("retrofitError", t.getMessage());
                                }
                            });
                        }
                        else
                        {
                            endLoading();
                            new AlertDialogBuilder(AuthorizationActivity.this)
                                    .alert("Ошибка авторизации", "Неверный формат ввода адреса элестронной почты.\n" +
                                            "Пожалуйста, введите верный адрес.");
                        }
                }
            }
        });
    }

    private void startLoading()
    {
        FrameLayout LoadingFrameLayout = findViewById(R.id.LoadingFrameLayout);
        LoadingFrameLayout.setVisibility(View.VISIBLE);
    }

    private void endLoading()
    {
        FrameLayout LoadingFrameLayout = findViewById(R.id.LoadingFrameLayout);
        LoadingFrameLayout.setVisibility(View.GONE);
    }
}