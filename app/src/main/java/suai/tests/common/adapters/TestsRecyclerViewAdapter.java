package suai.tests.common.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import suai.tests.R;
import suai.tests.activities.fragments.AccountFragment;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public class TestsRecyclerViewAdapter extends RecyclerView.Adapter<TestsRecyclerViewAdapter.ViewHolder>
{
    private LayoutInflater inflater; private ItemsPOJO[] tests; private Context context;

    public interface OnTestClickListener { void onStateClick(ItemsPOJO test, int position); }
    private final TestsRecyclerViewAdapter.OnTestClickListener onClickListener;

    public TestsRecyclerViewAdapter(Context context, OnTestClickListener onClickListener, ItemsPOJO[] tests)
    {
        this.tests = tests;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TestsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.recycler_tests_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestsRecyclerViewAdapter.ViewHolder holder, int position)
    {
        ItemsPOJO test_arr = tests[position]; String[] test = test_arr.getItems();

        if (AccountFragment.role == 1) { setStudentTests(holder, test); } else { setTeacherTests(holder, test); }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(test_arr, position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return tests.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView TestsTitleTextView, TestsSubjectTextView, TestsStatusMarkTextView,
                       TestsLanguageTextView, TestsGroupAndStudentTextView, TestsTimestampTextView;
        final ImageView TestsStatusImageView;

        ViewHolder(View view)
        {
            super(view);
            TestsTitleTextView = view.findViewById(R.id.TestsTitleTextView);
            TestsSubjectTextView = view.findViewById(R.id.TestsSubjectTextView);
            TestsStatusMarkTextView = view.findViewById(R.id.TestsStatusMarkTextView);
            TestsLanguageTextView = view.findViewById(R.id.TestsLanguageTextView);
            TestsGroupAndStudentTextView = view.findViewById(R.id.TestsGroupAndStudentTextView);
            TestsStatusImageView = view.findViewById(R.id.TestsStatusImageView);
            TestsTimestampTextView = view.findViewById(R.id.TestsTimestampTextView);
        }
    }

    public void setStudentTests(TestsRecyclerViewAdapter.ViewHolder holder, String[] test)
    {
        holder.TestsTitleTextView.setText(test[3]); holder.TestsSubjectTextView.setText(test[5]);
        holder.TestsGroupAndStudentTextView.setVisibility(View.GONE);

        if (test[6].equals("1")) { holder.TestsLanguageTextView.setText("C++"); }
        else { holder.TestsLanguageTextView.setText("Java"); }

        if (test[1].equals("2"))
        {
            holder.TestsStatusMarkTextView.setVisibility(View.GONE);
            holder.TestsStatusImageView.setImageResource(R.drawable.ic_baseline_error_24);
            holder.TestsStatusImageView.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_light),
                    PorterDuff.Mode.SRC_IN);
        }

        if (test[1].equals("0"))
        {
            holder.TestsStatusMarkTextView.setVisibility(View.GONE);
            holder.TestsStatusImageView.setImageResource(R.drawable.ic_baseline_access_time_24);
            holder.TestsStatusImageView.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_orange_light),
                    PorterDuff.Mode.SRC_IN);
        }

        if (test[1].equals("1"))
        {
            holder.TestsStatusImageView.setVisibility(View.GONE);
            holder.TestsStatusMarkTextView.setText(test[2]);
        }

        Calendar timestamp = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        try { timestamp.setTime(sdf.parse(test[7])); } catch (ParseException e) { e.printStackTrace(); }

        int monthInt = timestamp.get(Calendar.MONTH) + 1; String month = "";
        int dayInt = timestamp.get(Calendar.DAY_OF_MONTH); String day = "";
        int hourInt = timestamp.get(Calendar.HOUR_OF_DAY); String hour = "";
        int minuteInt = timestamp.get(Calendar.MINUTE); String minute = "";
        if (monthInt < 10) { month = "0" + monthInt; } else { month = String.valueOf(monthInt); }
        if (dayInt < 10) { day = "0" + dayInt; } else { day = String.valueOf(dayInt); }
        if (hourInt < 10) { hour = "0" + hourInt; } else { hour = String.valueOf(hourInt); }
        if (minuteInt < 10) { minute = "0" + minuteInt; } else { minute = String.valueOf(minuteInt); }

        holder.TestsTimestampTextView.setText(day+ "." + month + "." + timestamp.get(Calendar.YEAR) + " " + hour + ":" + minute);
    }

    public void setTeacherTests(TestsRecyclerViewAdapter.ViewHolder holder, String[] test)
    {
        holder.TestsTitleTextView.setText(test[1]); holder.TestsSubjectTextView.setText(test[4]);
        holder.TestsGroupAndStudentTextView.setVisibility(View.VISIBLE);
        holder.TestsGroupAndStudentTextView.setText(test[6] + " | " + test[7]);

        if (test[5].equals("1")) { holder.TestsLanguageTextView.setText("C++"); }
        else { holder.TestsLanguageTextView.setText("Java"); }

        holder.TestsStatusMarkTextView.setVisibility(View.GONE);
        if (test[2].equals("1")) { holder.TestsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_24); }
        else { holder.TestsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24); }
        holder.TestsStatusImageView.setColorFilter(ContextCompat.getColor(context, R.color.suai_secondary), PorterDuff.Mode.SRC_IN);

        Calendar timestamp = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        try { timestamp.setTime(sdf.parse(test[8])); } catch (ParseException e) { e.printStackTrace(); }

        int monthInt = timestamp.get(Calendar.MONTH) + 1; String month = "";
        int dayInt = timestamp.get(Calendar.DAY_OF_MONTH); String day = "";
        int hourInt = timestamp.get(Calendar.HOUR_OF_DAY); String hour = "";
        int minuteInt = timestamp.get(Calendar.MINUTE); String minute = "";
        if (monthInt < 10) { month = "0" + monthInt; } else { month = String.valueOf(monthInt); }
        if (dayInt < 10) { day = "0" + dayInt; } else { day = String.valueOf(dayInt); }
        if (hourInt < 10) { hour = "0" + hourInt; } else { hour = String.valueOf(hourInt); }
        if (minuteInt < 10) { minute = "0" + minuteInt; } else { minute = String.valueOf(minuteInt); }

        holder.TestsTimestampTextView.setText(day+ "." + month + "." + timestamp.get(Calendar.YEAR) + " " + hour + ":" + minute);
    }
}
