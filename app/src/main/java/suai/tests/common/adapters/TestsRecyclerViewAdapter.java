package suai.tests.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import suai.tests.R;
import suai.tests.common.api.pojo.common.ItemsPOJO;

public class TestsRecyclerViewAdapter extends RecyclerView.Adapter<TestsRecyclerViewAdapter.ViewHolder>
{
    private LayoutInflater inflater; private ItemsPOJO[] tests; private Context context;

    public TestsRecyclerViewAdapter(Context context, ItemsPOJO[] tests)
    {
        this.tests = tests;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        ItemsPOJO test = tests[position];

        holder.TestsTitleTextView.setText(test.getItems()[1]);
        holder.TestsDescriptionTextView.setText(test.getItems()[2]);
    }

    @Override
    public int getItemCount()
    {
        return tests.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView TestsTitleTextView;
        final TextView TestsDescriptionTextView;

        ViewHolder(View view)
        {
            super(view);
            TestsTitleTextView = view.findViewById(R.id.TestsTitleTextView);
            TestsDescriptionTextView = view.findViewById(R.id.TestsDescriptionTextView);
        }
    }
}
