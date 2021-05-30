package suai.tests.activities.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import org.checkerframework.checker.units.qual.C;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.activities.AuthorizationActivity;
import suai.tests.activities.MainActivity;
import suai.tests.common.AlertDialogBuilder;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.commonAPI;

public class AccountFragment extends Fragment
{
    public static int role, idUser;
    public static String email = "", lastName = "", firstName = "", patronymic = "";
    public static Calendar birthday = new GregorianCalendar();
    public static String[] data;

    private TextView EmailTextView, BirthdayTextView, NameTextView, RoleTextView, AvatarNameTextView;
    private LinearLayout AccountLayout; private View informationItem, informationDivider;
    private MaterialButton ChangePasswordButton; private ImageButton AccountEmailEditImageButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        MessengerFragment.chats = null;

        int birthMonthInt = birthday.get(Calendar.MONTH) + 1; String birthMonth = "";
        int birthDayInt = birthday.get(Calendar.DAY_OF_MONTH); String birthDay = "";
        if (birthMonthInt < 10) { birthMonth = "0" + birthMonthInt; } else { birthMonth = String.valueOf(birthMonthInt); }
        if (birthDayInt < 10) { birthDay = "0" + birthDayInt; } else { birthDay = String.valueOf(birthDayInt); }

        EmailTextView = root.findViewById(R.id.AccountEmailTextView);
        BirthdayTextView = root.findViewById(R.id.AccountBirthdayTextView);
        NameTextView = root.findViewById(R.id.AccountNameTextView);
        RoleTextView = root.findViewById(R.id.AccountRoleTextView);
        AvatarNameTextView = root.findViewById(R.id.AccountAvatarNameTextView);
        AccountEmailEditImageButton = root.findViewById(R.id.AccountEmailEditImageButton);
        ChangePasswordButton = root.findViewById(R.id.ChangePasswordButton);

        NameTextView.setText(lastName + "\n" + firstName + "\n" + patronymic);
        BirthdayTextView.setText(birthDay + "." + birthMonth + "." + birthday.get(Calendar.YEAR));
        EmailTextView.setText(email); AvatarNameTextView.setText(lastName.substring(0, 1));

        AccountLayout = root.findViewById(R.id.AccountLinearLayout);

        ChangePasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext()); builder.setTitle("Смена пароля");
                View fields = inflater.inflate(R.layout.change_password_dialog, null);
                builder.setView(fields)
                        .setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) { }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                        });
                AlertDialog changePasswordDialog = builder.create(); changePasswordDialog.show();

                changePasswordDialog.getButton(-1).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        EditText currentPasswordEditText = changePasswordDialog.findViewById(R.id.CurrentPasswordEditText);
                        EditText newPasswordEditText = changePasswordDialog.findViewById(R.id.NewPasswordEditText);
                        String currentPasswordText = currentPasswordEditText.getText().toString();
                        String newPasswordText = newPasswordEditText.getText().toString();

                        if (currentPasswordText.equals("") || newPasswordText.equals(""))
                        {
                            new AlertDialogBuilder(changePasswordDialog.getContext())
                                    .alert("Ошибка смены пароля", "Все поля должны быть заполнены.");
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(changePasswordDialog.getContext());
                            builder.setTitle("Подтверждение смены пароля");
                            builder.setMessage("Вы уверены, что хотите поменять существующий пароль на новый?");
                            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) { }
                            });
                            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) { dialog.cancel(); changePasswordDialog.cancel(); }
                            });
                            AlertDialog confirmPassword = builder.create(); confirmPassword.show();

                            confirmPassword.getButton(-1).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    final HashCode currentPassword = Hashing.sha1().hashString(currentPasswordText, Charset.defaultCharset());

                                    commonAPI service = RetrofitConnection.commonAPI;
                                    Call<String[]> call = service.changePassword(email, currentPassword.toString(), newPasswordText);
                                    call.enqueue(new Callback<String[]>() {
                                        @Override
                                        public void onResponse(Call<String[]> call, Response<String[]> response)
                                        {
                                            String[] resp = response.body();

                                            if (resp[0].equals("wrong password"))
                                            {
                                                new AlertDialogBuilder(confirmPassword.getContext())
                                                        .alert("Ошибка смены пароля", "Настоящий пароль введён неверно.\n" +
                                                                "Пожалуйста, введите корректные данные и повторите попытку.");
                                                confirmPassword.cancel(); changePasswordDialog.cancel();
                                            }

                                            if (resp[0].equals("success"))
                                            {
                                                new AlertDialogBuilder(confirmPassword.getContext())
                                                        .alert("Успешно", "Пароль успешно изменён.");
                                                confirmPassword.cancel(); changePasswordDialog.cancel();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String[]> call, Throwable t) { Log.e("retrofitError", t.getMessage()); }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });

        AccountEmailEditImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext()); builder.setTitle("Смена электронной почты");
                View fields = inflater.inflate(R.layout.change_email_dialog, null);
                builder.setView(fields)
                        .setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) { }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                        });
                AlertDialog changeEmailDialog = builder.create(); changeEmailDialog.show();

                changeEmailDialog.getButton(-1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        EditText currentEmailEditText = changeEmailDialog.findViewById(R.id.CurrentEmailEditText);
                        EditText newEmailEditText = changeEmailDialog.findViewById(R.id.NewEmailEditText);
                        EditText passwordEditText = changeEmailDialog.findViewById(R.id.PasswordEditText);
                        String currentEmail = currentEmailEditText.getText().toString();
                        String newEmail = newEmailEditText.getText().toString();
                        String password = passwordEditText.getText().toString();

                        if (currentEmail.equals("") || newEmail.equals("") || password.equals(""))
                        {
                            new AlertDialogBuilder(changeEmailDialog.getContext())
                                    .alert("Ошибка смены электронной почты", "Все поля должны быть заполнены.");
                        }
                        else
                        {
                            if (!Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches() || !Patterns.EMAIL_ADDRESS.matcher(newEmail).matches())
                            {
                                new AlertDialogBuilder(changeEmailDialog.getContext())
                                        .alert("Ошибка смены электронной почты", "Неверный формат ввода электронной почты.");
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(changeEmailDialog.getContext());
                                builder.setTitle("Подтверждение смены электронной почты");
                                builder.setMessage("Вы уверены, что хотите поменять существующую электронную почту на новую?");
                                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) { }
                                });
                                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); changeEmailDialog.cancel(); }
                                });
                                AlertDialog confirmEmail = builder.create(); confirmEmail.show();

                                confirmEmail.getButton(-1).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        if (!currentEmail.equals(email))
                                        {
                                            new AlertDialogBuilder(changeEmailDialog.getContext())
                                                    .alert("Ошибка смены электронной почты",
                                                            "Настоящая электронная почта введена неверно.\n" +
                                                                     "Пожалуйста, введите корректные данные и повторите попытку.");
                                            confirmEmail.cancel();
                                        }
                                        else
                                        {
                                            commonAPI service = RetrofitConnection.commonAPI;
                                            Call<String[]> call = service.changeEmail(idUser, newEmail, password);
                                            call.enqueue(new Callback<String[]>()
                                            {
                                                @Override
                                                public void onResponse(Call<String[]> call, Response<String[]> response)
                                                {
                                                    String[] resp = response.body();

                                                    if (resp[0].equals("wrong password"))
                                                    {
                                                        new AlertDialogBuilder(confirmEmail.getContext())
                                                                .alert("Ошибка смены электронной почты",
                                                                        "Настоящий пароль введён неверно.\n" +
                                                                                "Пожалуйста, введите корректные данные и повторите попытку.");
                                                        confirmEmail.cancel(); changeEmailDialog.cancel();
                                                    }

                                                    if (resp[0].equals("success"))
                                                    {
                                                        new AlertDialogBuilder(confirmEmail.getContext())
                                                                .alert("Успешно", "Электронная почта успешно изменена.");
                                                        email = newEmail; confirmEmail.cancel(); changeEmailDialog.cancel();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<String[]> call, Throwable t) {
                                                    Log.e("retrofitError", t.getMessage());
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        if (role == 0)
        {
            RoleTextView.setText("Преподаватель");
            setInformation(inflater, true,"Образование", data[0]);
            setInformation(inflater, true,"Должность", data[1]);
            setInformation(inflater, false,"Предметы", data[2]);
        }
        else
        {
            RoleTextView.setText("Студент");
            setInformation(inflater, false,"Группа", data[0]);
        }

        root.findViewById(R.id.StatisticsButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("idUser", idUser); bundle.putInt("role", role);
                Navigation.findNavController(root).navigate(R.id.action_navigation_account_to_statisticsFragment, bundle);
            }
        });

        root.findViewById(R.id.ExitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = AuthorizationActivity.spSavedData.edit(); editor.clear(); editor.commit();
                startActivity(new Intent(root.getContext(), AuthorizationActivity.class)); getActivity().finish();
            }
        });

        return root;
    }

    public void setInformation(LayoutInflater inflater, boolean isBordered, String title, String data)
    {
        informationItem = inflater.inflate(R.layout.account_information_item, AccountLayout, false);
        informationDivider = inflater.inflate(R.layout.layouts_divider, AccountLayout, false);

        TextView TitleTextView = informationItem.findViewById(R.id.AccountInformationTitleTextView);
        TextView DataTextView = informationItem.findViewById(R.id.AccountInformationDataTextView);

        TitleTextView.setText(title + ":"); DataTextView.setText(data);
        AccountLayout.addView(informationItem);

        if (isBordered) { AccountLayout.addView(informationDivider); }
    }
}