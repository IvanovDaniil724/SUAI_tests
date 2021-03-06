package suai.tests.activities.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

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

    private static final String ID_TEST = "idTest"; private int idTest;
    public static Calendar timestamp = new GregorianCalendar();

    private TextView TestsDetailsTitleTextView, TestsDetailsDescriptionTextView, TestsDetailsResultsOrErrorsTextView,
                     TestsDetailsTimestampTextView, TestsDetailsLanguageTextView, TestsDetailsSubjectTextView,
                     TestsDetailsStudentTextView, TestsDetailsStudentInfoTextView, TestsDetailsStatusMarkTextView,
                     TestDetailsStudentInfoGroupTextView, TestDetailsStudentInfoSpecialtyTextView, TestDetailsStudentInfoEmailTextView,
                     TestsDetailsTeacherCommentTextView, TestsDetailsDescriptionInfoTextView;
    private ImageView TestsDetailsStatusImageView; private ConstraintLayout TestDetailsTeacherConstraintLayout;
    private LinearLayout TestDetailsStudentInfoLinearLayout; private ScrollView TestDetailsScrollView;
    private TextInputLayout TestDetailsTeacherCommentTextInputLayout; private EditText TestDetailsTeacherCommentEditText;
    private Spinner TestDetailsTeacherChooseMarkSpinner; private MaterialButton TestDetailsTeacherChooseMarkButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idTest = getArguments().getInt(ID_TEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_test_details, container, false);

        TestsDetailsTitleTextView = root.findViewById(R.id.TestsDetailsTitleTextView);
        TestsDetailsDescriptionTextView = root.findViewById(R.id.TestsDetailsDescriptionTextView);
        TestsDetailsDescriptionInfoTextView = root.findViewById(R.id.TestsDetailsDescriptionInfoTextView);
        TestsDetailsTeacherCommentTextView = root.findViewById(R.id.TestsDetailsTeacherCommentTextView);
        TestsDetailsResultsOrErrorsTextView = root.findViewById(R.id.TestsDetailsResultsOrErrorsTextView);
        TestsDetailsTimestampTextView = root.findViewById(R.id.TestsDetailsTimestampTextView);
        TestsDetailsLanguageTextView = root.findViewById(R.id.TestsDetailsLanguageTextView);
        TestsDetailsSubjectTextView = root.findViewById(R.id.TestsDetailsSubjectTextView);
        TestsDetailsStudentTextView = root.findViewById(R.id.TestsDetailsStudentTextView);
        TestsDetailsStudentInfoTextView = root.findViewById(R.id.TestsDetailsStudentInfoTextView);
        TestsDetailsStatusMarkTextView = root.findViewById(R.id.TestsDetailsStatusMarkTextView);
        TestDetailsStudentInfoGroupTextView = root.findViewById(R.id.TestDetailsStudentInfoGroupTextView);
        TestDetailsStudentInfoSpecialtyTextView = root.findViewById(R.id.TestDetailsStudentInfoSpecialtyTextView);
        TestDetailsStudentInfoEmailTextView = root.findViewById(R.id.TestDetailsStudentInfoEmailTextView);
        TestDetailsStudentInfoLinearLayout = root.findViewById(R.id.TestDetailsStudentInfoLinearLayout);
        TestsDetailsStatusImageView = root.findViewById(R.id.TestsDetailsStatusImageView);
        TestDetailsScrollView = root.findViewById(R.id.TestDetailsScrollView);
        TestDetailsTeacherConstraintLayout = root.findViewById(R.id.TestDetailsTeacherConstraintLayout);
        TestDetailsTeacherCommentTextInputLayout = root.findViewById(R.id.TestDetailsTeacherCommentTextInputLayout);
        TestDetailsTeacherCommentEditText = root.findViewById(R.id.TestDetailsTeacherCommentEditText);
        TestDetailsTeacherChooseMarkSpinner = root.findViewById(R.id.TestDetailsTeacherChooseMarkSpinner);
        TestDetailsTeacherChooseMarkButton = root.findViewById(R.id.TestDetailsTeacherChooseMarkButton);

        TestsDetailsDescriptionInfoTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: TestsDetailsDescriptionTextView.setVisibility(View.VISIBLE); break;
                    case MotionEvent.ACTION_UP: TestsDetailsDescriptionTextView.setVisibility(View.GONE); break;
                    case MotionEvent.ACTION_CANCEL: TestsDetailsDescriptionTextView.setVisibility(View.GONE); break;
                }
                return true;
            }
        });

        TestsDetailsStudentInfoTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: TestDetailsStudentInfoLinearLayout.setVisibility(View.VISIBLE); break;
                    case MotionEvent.ACTION_UP: TestDetailsStudentInfoLinearLayout.setVisibility(View.GONE); break;
                    case MotionEvent.ACTION_CANCEL: TestDetailsStudentInfoLinearLayout.setVisibility(View.GONE); break;
                }
                return true;
            }
        });

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
                TestsDetailsSubjectTextView.setText("????????????????????:\n" + test[3]);
                if (test[4].equals("1")) { TestsDetailsLanguageTextView.setText("C++"); }
                else { TestsDetailsLanguageTextView.setText("Java"); }

                timestamp = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                try { timestamp.setTime(sdf.parse(test[5])); } catch (ParseException e) { e.printStackTrace(); }

                int monthInt = timestamp.get(Calendar.MONTH) + 1; String month = "";
                int dayInt = timestamp.get(Calendar.DAY_OF_MONTH); String day = "";
                int hourInt = timestamp.get(Calendar.HOUR_OF_DAY); String hour = "";
                int minuteInt = timestamp.get(Calendar.MINUTE); String minute = "";
                if (monthInt < 10) { month = "0" + monthInt; } else { month = String.valueOf(monthInt); }
                if (dayInt < 10) { day = "0" + dayInt; } else { day = String.valueOf(dayInt); }
                if (hourInt < 10) { hour = "0" + hourInt; } else { hour = String.valueOf(hourInt); }
                if (minuteInt < 10) { minute = "0" + minuteInt; } else { minute = String.valueOf(minuteInt); }

                TestsDetailsTimestampTextView.setText("??????????: " + day+ "." + month + "." + timestamp.get(Calendar.YEAR) +
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
        TestDetailsTeacherConstraintLayout.setVisibility(View.GONE);

        ConstraintLayout constraintLayout = root.findViewById(R.id.TestDetailsConstraintLayout);
        ConstraintSet constraintSet = new ConstraintSet(); constraintSet.clone(constraintLayout);
        constraintSet.connect(TestDetailsScrollView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM,16);
        constraintSet.applyTo(constraintLayout);

        if (test[2].equals("2"))
        {
            TestsDetailsStatusMarkTextView.setVisibility(View.GONE);
            if (test[9] != null) { TestsDetailsTeacherCommentTextView.setText("?????????????????????? ??????????????????????????:\n" + test[9]); }
            else { TestsDetailsTeacherCommentTextView.setText("?????????????????????? ??????????????????????????:\n<??????????????????????>"); }
            TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_error_24);
            TestsDetailsStatusImageView.setColorFilter(ContextCompat.getColor(root.getContext(),
                    android.R.color.holo_red_light), PorterDuff.Mode.SRC_IN);
        }

        if (test[2].equals("0"))
        {
            TestsDetailsStatusMarkTextView.setVisibility(View.GONE);
            TestsDetailsTeacherCommentTextView.setText("?????????????????????? ??????????????????????????:\n<??????????????????????>");
            TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_access_time_24);
            TestsDetailsStatusImageView.setColorFilter(ContextCompat.getColor(root.getContext(),
                    android.R.color.holo_orange_light), PorterDuff.Mode.SRC_IN);
        }

        if (test[2].equals("1"))
        {
            TestsDetailsStatusImageView.setVisibility(View.GONE); TestsDetailsStatusMarkTextView.setText(test[8]);
            if (test[9] != null) { TestsDetailsTeacherCommentTextView.setText("?????????????????????? ??????????????????????????:\n" + test[9]); }
            else { TestsDetailsTeacherCommentTextView.setText("?????????????????????? ??????????????????????????:\n<??????????????????????>"); }
        }

        if (test[6].equals("")) { TestsDetailsResultsOrErrorsTextView.setText("????????????:\n<??????????????????????>" + "\n\n??????????????????:\n" + test[7]); }
        else { TestsDetailsResultsOrErrorsTextView.setText("????????????:\n" + test[6] + "\n\n??????????????????:\n<??????????????????????>"); }
    }

    private void setTeacherTestDetails(View root, String[] test)
    {
        TestsDetailsTeacherCommentTextView.setVisibility(View.GONE);

        String[] marks = { "5", "4", "3", "2" };
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(root.getContext(), R.layout.spinner_style, marks);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_style);
        TestDetailsTeacherChooseMarkSpinner.setAdapter(spinnerAdapter);

        if (test[2].equals("1"))
        {
            TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_24);
            TestsDetailsStatusMarkTextView.setText(test[15]); TestDetailsTeacherChooseMarkButton.setText("???????????????? ????????????");
            if (test[15].equals("2")) { TestDetailsTeacherChooseMarkSpinner.setSelection(3); }
            if (test[15].equals("3")) { TestDetailsTeacherChooseMarkSpinner.setSelection(2); }
            if (test[15].equals("4")) { TestDetailsTeacherChooseMarkSpinner.setSelection(1); }
            if (test[15].equals("5")) { TestDetailsTeacherChooseMarkSpinner.setSelection(0); }
            TestDetailsTeacherCommentEditText.setText(test[16]);
        }
        else
        {
            TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
            TestsDetailsStatusMarkTextView.setVisibility(View.GONE);
        }
        TestsDetailsStatusImageView.setColorFilter(ContextCompat.getColor(root.getContext(), R.color.suai_secondary), PorterDuff.Mode.SRC_IN);

        TestsDetailsStudentTextView.setText(test[9] + " " + test[10] + " " + test[11]);

        if (test[6].equals("")) { TestsDetailsResultsOrErrorsTextView.setText("????????????:\n<??????????????????????>" + "\n\n??????????????????:\n" + test[7]); }
        else { TestsDetailsResultsOrErrorsTextView.setText("????????????:\n" + test[6] + "\n\n??????????????????:\n<??????????????????????>"); }

        TestDetailsStudentInfoGroupTextView.setText("????????????: " + test[12]);
        TestDetailsStudentInfoSpecialtyTextView.setText("??????????????????????????: " + test[14] + "\n(" + test[13] + ")");
        TestDetailsStudentInfoEmailTextView.setText("????. ??????????: " + test[8]);

        TestDetailsTeacherChooseMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestsDetailsStatusMarkTextView.setVisibility(View.VISIBLE);
                TestsDetailsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_24);
                TestsDetailsStatusMarkTextView.setText(TestDetailsTeacherChooseMarkSpinner.getSelectedItem().toString());
                TestDetailsTeacherChooseMarkButton.setText("???????????????? ????????????");

                testsAPI service = RetrofitConnection.testsApi; Call<String[]> call;
                call = service.setStudentMark(String.valueOf(idTest), TestDetailsTeacherChooseMarkSpinner.getSelectedItem().toString(),
                                              TestDetailsTeacherCommentEditText.getText().toString());
                call.enqueue(new Callback<String[]>()
                {
                    @Override
                    public void onResponse(Call<String[]> call, Response<String[]> response) { }

                    @Override
                    public void onFailure(Call<String[]> call, Throwable t) { Log.e("retrofitError", t.getMessage()); }
                });
            }
        });
    }
}