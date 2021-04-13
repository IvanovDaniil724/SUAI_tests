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
    private LayoutInflater inflater;
    private ItemsPOJO[] tests;
    private Context context;

    public TestsRecyclerViewAdapter(Context context, ItemsPOJO[] tests)
    {
        this.tests = tests;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        //this.mainActivity = context;
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
        //Countries_API country = countries.get(position);

        //Activity activity = (Activity) mainActivity;

        /*holder.CountryName_TextView.setText(country.getName());
        holder.CountryCapital_TextView.setText("Capital: " + country.getCapital());
        holder.CountryPopulation_TextView.setText("Population: " + country.getPopulation());
        holder.CountryArea_TextView.setText("Area: " + country.getArea() + " km²");
        holder.CountryRegion_TextView.setText("Region: " + country.getRegion());
        holder.CountrySubregion_TextView.setText("Subregion: " + country.getSubregion());*/

        ItemsPOJO test = tests[position];

        holder.TestsTitleTextView.setText(test.getItems()[1]);
        holder.TestsDescriptionTextView.setText(test.getItems()[2]);
    }

    @Override
    public int getItemCount()
    {
        return tests.length; //countries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        /*final LinearLayout RecyclerView_Layout;
        final ImageView CountryBackground_ImageView;
        final TextView CountryName_TextView;
        final TextView CountryCapital_TextView;
        final TextView CountryPopulation_TextView;
        final TextView CountryArea_TextView;
        final TextView CountryRegion_TextView;
        final TextView CountrySubregion_TextView;*/

        final TextView TestsTitleTextView;
        final TextView TestsDescriptionTextView;

        ViewHolder(View view)
        {
            super(view);
            /*RecyclerView_Layout = (LinearLayout) view.findViewById(R.id.RecyclerView_Layout);
            CountryBackground_ImageView = (ImageView)view.findViewById(R.id.ItemBackground_ImageView);
            CountryName_TextView = (TextView)view.findViewById(R.id.CountryName_TextView);
            CountryCapital_TextView = (TextView)view.findViewById(R.id.CountryCapital_TextView);
            CountryPopulation_TextView = (TextView)view.findViewById(R.id.CountryPopulation_TextView);
            CountryArea_TextView = (TextView)view.findViewById(R.id.CountryArea_TextView);
            CountryRegion_TextView = (TextView)view.findViewById(R.id.CountryRegion_TextView);
            CountrySubregion_TextView = (TextView)view.findViewById(R.id.CountrySubregion_TextView);*/

            TestsTitleTextView = view.findViewById(R.id.TestsTitleTextView);
            TestsDescriptionTextView = view.findViewById(R.id.TestsDescriptionTextView);
        }
    }
}
