package suai.tests.activities.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.pojo.common.ItemsPOJO;
import suai.tests.common.api.testsAPI;

public class TestDetailsFragment extends Fragment {

    private static final String ID_TEST = "idTest";

    private int idTest;

    public static Calendar timestamp = new GregorianCalendar();

    private TextView TestsDetailsTitleTextView, TestsDetailsDescriptionTextView, TestsDetailsResultsOrErrorsTextView,
                     TestsDetailsTimestampTextView, TestsDetailsLanguageTextView, TestsDetailsSubjectTextView,
                     TestsDetailsStudentTextView, TestsDetailsStudentInfoTextView, TestsDetailsStatusMarkTextView;
    private ImageView TestsDetailsStatusImageView;

    public TestDetailsFragment() {
        // Required empty public constructor
    }

    public static TestDetailsFragment newInstance(int idTest) {
        TestDetailsFragment fragment = new TestDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ID_TEST, idTest);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            idTest = getArguments().getInt(ID_TEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_details, container, false);

        TestsDetailsTitleTextView = root.findViewById(R.id.TestsDetailsTitleTextView);
        TestsDetailsDescriptionTextView = root.findViewById(R.id.TestsDetailsDescriptionTextView);
        TestsDetailsResultsOrErrorsTextView = root.findViewById(R.id.TestsDetailsResultsOrErrorsTextView);
        TestsDetailsTimestampTextView = root.findViewById(R.id.TestsDetailsTimestampTextView);
        TestsDetailsLanguageTextView = root.findViewById(R.id.TestsDetailsLanguageTextView);
        TestsDetailsSubjectTextView = root.findViewById(R.id.TestsDetailsSubjectTextView);
        TestsDetailsStudentTextView = root.findViewById(R.id.TestsDetailsStudentTextView);
        TestsDetailsStudentInfoTextView = root.findViewById(R.id.TestsDetailsStudentInfoTextView);
        TestsDetailsStatusMarkTextView = root.findViewById(R.id.TestsDetailsStatusMarkTextView);
        TestsDetailsStatusImageView = root.findViewById(R.id.TestsDetailsStatusImageView);

        testsAPI service = RetrofitConnection.testsApi; Call<ItemsPOJO[]> call;
        call = service.getTestDetails(idTest, AccountFragment.role);
        call.enqueue(new Callback<ItemsPOJO[]>()
        {
            @Override
            public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
            {
                ItemsPOJO[] resp = response.body();
                String[] test = resp[0].getItems();

                TestsDetailsTitleTextView.setText(test[0]); TestsDetailsDescriptionTextView.setText(test[1]);
                TestsDetailsSubjectTextView.setText("Дисциплина:\n" + test[3]);
                if (test[4].equals("1")) { TestsDetailsLanguageTextView.setText("C++"); }
                else { TestsDetailsLanguageTextView.setText("Java"); }

                timestamp = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try { timestamp.setTime(sdf.parse(test[5])); } catch (ParseException e) { e.printStackTrace(); }

                int monthInt = timestamp.get(Calendar.MONTH) + 1; String month = "";
                int dayInt = timestamp.get(Calendar.DAY_OF_MONTH); String day = "";
                int hourInt = timestamp.get(Calendar.HOUR); String hour = "";
                int minuteInt = timestamp.get(Calendar.MINUTE); String minute = "";
                if (monthInt < 10) { month = "0" + monthInt; } else { month = String.valueOf(monthInt); }
                if (dayInt < 10) { day = "0" + dayInt; } else { day = String.valueOf(dayInt); }
                if (hourInt < 10) { hour = "0" + hourInt; } else { hour = String.valueOf(hourInt); }
                if (minuteInt < 10) { minute = "0" + minuteInt; } else { minute = String.valueOf(minuteInt); }

                TestsDetailsTimestampTextView.setText("Сдано: " + day+ "." + month + "." + timestamp.get(Calendar.YEAR) +
                        " " + hour + ":" + minute);

                if (AccountFragment.role == 1) { setStudentTestDetails(root, test); } else { setTeacherTestDetails(root, test); }
            }

            @Override
            public void onFailure(@NonNull Call<ItemsPOJO[]> call, @NonNull Throwable t)
            {
                Log.e("retrofitError", t.getMessage());
            }
        });

        return root;
    }

    private void setStudentTestDetails(View root, String[] test)
    {
        TestsDetailsStudentTextView.setVisibility(View.GONE); TestsDetailsStudentInfoTextView.setVisibility(View.GONE);

        if (test[2].equals("2"))
        {
            TestsDetailsStatusMarkTextView.setVisibility(View.GONE);
            TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_error_24);
            TestsDetailsStatusImageView.setColorFilter(ContextCompat.getColor(root.getContext(),
                    android.R.color.holo_red_light), PorterDuff.Mode.SRC_IN);
        }

        if (test[2].equals("0"))
        {
            TestsDetailsStatusMarkTextView.setVisibility(View.GONE);
            TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_access_time_24);
            TestsDetailsStatusImageView.setColorFilter(ContextCompat.getColor(root.getContext(),
                    android.R.color.holo_orange_light), PorterDuff.Mode.SRC_IN);
        }

        if (test[2].equals("1"))
        {
            TestsDetailsStatusImageView.setVisibility(View.GONE); TestsDetailsStatusMarkTextView.setText(test[8]);
        }

        if (test[6] == null) { TestsDetailsResultsOrErrorsTextView.setText("Ошибки:\n<Отсутствуют>" + "\n\nРезультат:\n" + test[7]); }
        else { TestsDetailsResultsOrErrorsTextView.setText("Ошибки:\n" + test[6] + "\n\nРезультат:\n" + test[7]); }
    }

    private void setTeacherTestDetails(View root, String[] test)
    {
        TestsDetailsStatusMarkTextView.setVisibility(View.GONE);
        if (test[2].equals("1")) { TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_24); }
        else { TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24); }
        TestsDetailsStatusImageView.setColorFilter(ContextCompat.getColor(root.getContext(),
                R.color.suai_secondary), PorterDuff.Mode.SRC_IN);

        TestsDetailsStudentTextView.setText(test[9] + " " + test[10] + " " + test[11]);

        if (test[6] == null) { TestsDetailsResultsOrErrorsTextView.setText("Ошибки:\n<Отсутствуют>" + "\n\nРезультат:\n" + test[7]); }
        else { TestsDetailsResultsOrErrorsTextView.setText("Ошибки:\n" + test[6] + "\n\nРезультат:\n" + test[7]); }
    }
}