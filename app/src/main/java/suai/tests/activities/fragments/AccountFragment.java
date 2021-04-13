package suai.tests.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import suai.tests.R;

public class AccountFragment extends Fragment
{
    public static int role, idUser;
    public static String email = "", lastName = "", firstName = "", patronymic = "";
    public static Calendar birthDay = new GregorianCalendar();
    private TextView EmailTextView, BirthdayTextView, NameTextView, RoleTextView, AvatarNameTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        int birthMonthInt = birthDay.get(Calendar.MONTH) + 1; String birthMonth = "";
        if (birthMonthInt < 10) { birthMonth = "0" + birthMonthInt; }

        EmailTextView = root.findViewById(R.id.AccountEmailTextView);
        BirthdayTextView = root.findViewById(R.id.AccountBirthdayTextView);
        NameTextView = root.findViewById(R.id.AccountNameTextView);
        RoleTextView = root.findViewById(R.id.AccountRoleTextView);
        AvatarNameTextView = root.findViewById(R.id.AccountAvatarNameTextView);

        NameTextView.setText(lastName + "\n" + firstName + "\n" + patronymic);
        BirthdayTextView.setText(birthDay.get(Calendar.DAY_OF_MONTH) + "." + birthMonth + "." + birthDay.get(Calendar.YEAR));
        if (role == 0) { RoleTextView.setText("Преподаватель"); } else { RoleTextView.setText("Студент"); }
        EmailTextView.setText(email); AvatarNameTextView.setText(lastName.substring(0, 1));

        return root;
    }
}