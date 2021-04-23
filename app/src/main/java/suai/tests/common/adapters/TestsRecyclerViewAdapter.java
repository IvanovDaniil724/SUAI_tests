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

import suai.tests.R;
import suai.tests.activities.fragments.AccountFragment;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public class TestsRecyclerViewAdapter extends RecyclerView.Adapter<TestsRecyclerViewAdapter.ViewHolder>
{
    private LayoutInflater inflater; private ItemsPOJO[] tests; private Context context;

    public interface OnTestClickListener{
        void onStateClick(ItemsPOJO test, int position);
    }
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



        holder.itemView.setOnClickListener(new View.OnClickListener(){
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
        final TextView TestsTitleTextView, TestsStudentOrSubjectTextView, TestsStatusMarkTextView;
        final ImageView TestsStatusImageView;

        ViewHolder(View view)
        {
            super(view);
            TestsTitleTextView = view.findViewById(R.id.TestsTitleTextView);
            TestsStudentOrSubjectTextView = view.findViewById(R.id.TestsStudentOrSubjectTextView);
            TestsStatusMarkTextView = view.findViewById(R.id.TestsStatusMarkTextView);;
            TestsStatusImageView = view.findViewById(R.id.TestsStatusImageView);

        }
    }

    public void setStudentTests(TestsRecyclerViewAdapter.ViewHolder holder, String[] test)
    {
        holder.TestsTitleTextView.setText(test[3]);
        holder.TestsStudentOrSubjectTextView.setText(test[5]);

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
    }

    public void setTeacherTests(TestsRecyclerViewAdapter.ViewHolder holder, String[] test)
    {
        holder.TestsTitleTextView.setText(test[1]);
        holder.TestsStudentOrSubjectTextView.setText(test[4]);

        holder.TestsStatusMarkTextView.setVisibility(View.GONE);
        if (test[2].equals("1")) { holder.TestsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_24); }
        else { holder.TestsStatusImageView.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24); }
        holder.TestsStatusImageView.setColorFilter(ContextCompat.getColor(context, R.color.suai_secondary), PorterDuff.Mode.SRC_IN);
    }
}
