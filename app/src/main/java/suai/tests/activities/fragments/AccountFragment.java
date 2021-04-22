package suai.tests.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import suai.tests.R;

public class AccountFragment extends Fragment
{
    public static int role, idUser;
    public static String email = "", lastName = "", firstName = "", patronymic = "";
    public static Calendar birthDay = new GregorianCalendar();
    //public static List<String> data; //private static List<String> data;
    public static String[] data;

    //public static void setData(List<String> information) {
    //    AccountFragment.data = information;
    //}

    private TextView EmailTextView, BirthdayTextView, NameTextView, RoleTextView, AvatarNameTextView;
    private LinearLayout AccountLayout; private View informationItem, informationDivider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        int birthMonthInt = birthDay.get(Calendar.MONTH) + 1; String birthMonth = "";
        if (birthMonthInt < 10) { birthMonth = "0" + birthMonthInt; } else { birthMonth = String.valueOf(birthMonthInt); }

        EmailTextView = root.findViewById(R.id.AccountEmailTextView);
        BirthdayTextView = root.findViewById(R.id.AccountBirthdayTextView);
        NameTextView = root.findViewById(R.id.AccountNameTextView);
        RoleTextView = root.findViewById(R.id.AccountRoleTextView);
        AvatarNameTextView = root.findViewById(R.id.AccountAvatarNameTextView);

        NameTextView.setText(lastName + "\n" + firstName + "\n" + patronymic);
        BirthdayTextView.setText(birthDay.get(Calendar.DAY_OF_MONTH) + "." + birthMonth + "." + birthDay.get(Calendar.YEAR));
        EmailTextView.setText(email); AvatarNameTextView.setText(lastName.substring(0, 1));

        AccountLayout = root.findViewById(R.id.AccountLinearLayout);

        if (role == 0)
        {
            RoleTextView.setText("Преподаватель");
            setInformation(inflater, true,"Образование", data[0]);
            setInformation(inflater, false,"Должность", data[1]);
        }
        else
        {
            RoleTextView.setText("Студент");
            setInformation(inflater, false,"Группа", data[0]);
        }

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

    public static class TeacherInformation
    {
        private String subjects, education;

        public String getSubjects() {
            return subjects;
        }

        public void setSubjects(String subjects) {
            this.subjects = subjects;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public void setInformation()
        {

        }
    }

    public static class StudentInformation
    {
        private String group, specialty;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }
    }
}