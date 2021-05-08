package suai.tests.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import suai.tests.R;

public class AccountFragment extends Fragment
{
    public static int role, idUser;
    public static String email = "", lastName = "", firstName = "", patronymic = "";
    public static Calendar birthday = new GregorianCalendar();
    public static String[] data;

    private TextView EmailTextView, BirthdayTextView, NameTextView, RoleTextView, AvatarNameTextView;
    private LinearLayout AccountLayout; private View informationItem, informationDivider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        int birthMonthInt = birthday.get(Calendar.MONTH) + 1; String birthMonth = "";
        int birthDayInt = birthday.get(Calendar.DAY_OF_MONTH); String birthDay = "";
        if (birthMonthInt < 10) { birthMonth = "0" + birthMonthInt; } else { birthMonth = String.valueOf(birthMonthInt); }
        if (birthDayInt < 10) { birthDay = "0" + birthDayInt; } else { birthDay = String.valueOf(birthDayInt); }

        EmailTextView = root.findViewById(R.id.AccountEmailTextView);
        BirthdayTextView = root.findViewById(R.id.AccountBirthdayTextView);
        NameTextView = root.findViewById(R.id.AccountNameTextView);
        RoleTextView = root.findViewById(R.id.AccountRoleTextView);
        AvatarNameTextView = root.findViewById(R.id.AccountAvatarNameTextView);

        NameTextView.setText(lastName + "\n" + firstName + "\n" + patronymic);
        BirthdayTextView.setText(birthDay + "." + birthMonth + "." + birthday.get(Calendar.YEAR));
        EmailTextView.setText(email); AvatarNameTextView.setText(lastName.substring(0, 1));

        AccountLayout = root.findViewById(R.id.AccountLinearLayout);

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