package suai.tests.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import suai.tests.R;
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

        if (test[0].equals("2"))
        {
            holder.TestsStatusMarkTextView.setVisibility(View.GONE);
            holder.TestsStatusImageView.setImageResource(android.R.drawable.stat_sys_warning);
        }

        if (test[0].equals("0"))
        {
            holder.TestsStatusMarkTextView.setVisibility(View.GONE);
            holder.TestsStatusImageView.setImageResource(android.R.drawable.ic_menu_recent_history);
        }

        if (test[0].equals("1"))
        {
            holder.TestsStatusImageView.setVisibility(View.GONE);
            holder.TestsStatusMarkTextView.setText(test[1]);
        }

        holder.TestsTitleTextView.setText(test[2]);
        holder.TestsSubjectTextView.setText(test[4]);

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
        final TextView TestsTitleTextView, TestsSubjectTextView, TestsStatusMarkTextView;
        final ImageView TestsStatusImageView;

        ViewHolder(View view)
        {
            super(view);
            TestsTitleTextView = view.findViewById(R.id.TestsTitleTextView);
            TestsSubjectTextView = view.findViewById(R.id.TestsSubjectTextView);
            TestsStatusMarkTextView = view.findViewById(R.id.TestsStatusMarkTextView);;
            TestsStatusImageView = view.findViewById(R.id.TestsStatusImageView);

        }
    }
}
