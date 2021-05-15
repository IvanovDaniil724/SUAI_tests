package suai.tests.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.json.JSONArray;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
    public static SharedPreferences spSavedData;
    private final String ID_USER = "idUser";
    private final String ROLE = "role";
    private final String EMAIL = "email";
    private final String LAST_NAME = "lastName";
    private final String FIRST_NAME = "firstName";
    private final String PATRONYMIC = "patronymic";
    private final String BIRTHDAY = "birthday";
    private final String DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        spSavedData = getPreferences(MODE_PRIVATE);
        if (spSavedData.contains(ID_USER))
        {
            AccountFragment.idUser = spSavedData.getInt(ID_USER, 0);
            AccountFragment.role = spSavedData.getInt(ROLE, 0);
            AccountFragment.email = spSavedData.getString(EMAIL, "");
            AccountFragment.lastName = spSavedData.getString(LAST_NAME, "");
            AccountFragment.firstName = spSavedData.getString(FIRST_NAME, "");
            AccountFragment.patronymic = spSavedData.getString(PATRONYMIC, "");

            Calendar calendar = Calendar.getInstance(); SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try { calendar.setTime(sdf.parse(spSavedData.getString(BIRTHDAY, ""))); } catch (ParseException e) { e.printStackTrace(); }
            AccountFragment.birthday = calendar;
            AccountFragment.data = spSavedData.getString(DATA, "").split("#");

            startActivity(new Intent(AuthorizationActivity.this, MainActivity.class)); finish();
        }

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
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        try { calendar.setTime(sdf.parse(user[5])); }
                                        catch (ParseException e) { e.printStackTrace(); }
                                        AccountFragment.birthday = calendar;

                                        AccountFragment.role = Integer.parseInt(user[6]);

                                        AccountFragment.data = new String[user.length - 7];
                                        for (int i = 0; i < AccountFragment.data.length; i++) { AccountFragment.data[i] = user[i + 7]; }

                                        CheckBox RememberMeCheckBox = findViewById(R.id.RememberMeCheckBox);
                                        if (RememberMeCheckBox.isChecked())
                                        {
                                            spSavedData = getPreferences(MODE_PRIVATE);
                                            SharedPreferences.Editor editor = spSavedData.edit();
                                            editor.putInt(ID_USER, AccountFragment.idUser);
                                            editor.putInt(ROLE, AccountFragment.role);
                                            editor.putString(EMAIL, AccountFragment.email);
                                            editor.putString(LAST_NAME, AccountFragment.lastName);
                                            editor.putString(FIRST_NAME, AccountFragment.firstName);
                                            editor.putString(PATRONYMIC, AccountFragment.patronymic);
                                            editor.putString(BIRTHDAY, user[5]);
                                            StringBuilder sb = new StringBuilder();
                                            for (int i = 0; i < AccountFragment.data.length; i++) {
                                                sb.append(AccountFragment.data[i]).append("#");
                                            }
                                            editor.putString(DATA, sb.toString());
                                            editor.commit();
                                        }

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