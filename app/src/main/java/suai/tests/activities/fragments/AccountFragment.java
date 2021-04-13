package suai.tests.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    private TextView EmailTextView, BirthdayTextView, NameTextView, RoleTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        EmailTextView = root.findViewById(R.id.AccountEmailTextView);
        BirthdayTextView = root.findViewById(R.id.AccountBirthdayTextView);
        NameTextView = root.findViewById(R.id.AccountNameTextView);
        RoleTextView = root.findViewById(R.id.AccountRoleTextView);

        NameTextView.setText(lastName + "\n" + firstName + "\n" + patronymic);
        BirthdayTextView.setText(birthDay.get(Calendar.DAY_OF_MONTH) + "." + birthMonth + "." + birthDay.get(Calendar.YEAR));
        EmailTextView.setText(email);

        return root;
    }
}