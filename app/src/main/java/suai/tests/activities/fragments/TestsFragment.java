package suai.tests.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.common.adapters.TestsRecyclerViewAdapter;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.pojo.tests.TestPOJO;
import suai.tests.common.api.testsAPI;

public class TestsFragment extends Fragment
{
    //private RetrofitConnection retrofitConnection;
    //private API api;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_tests, container, false);

        RecyclerView testsRecyclerView = root.findViewById(R.id.TestsRecyclerView);
        testsAPI service = RetrofitConnection.testsApi;

        Call<TestPOJO[]> call = service.getTests();

        call.enqueue(new Callback<TestPOJO[]>()
        {
            @Override
            public void onResponse(@NonNull Call<TestPOJO[]> call, @NonNull Response<TestPOJO[]> response)
            {
                TestPOJO[] tests = response.body();

                //TestsRecyclerViewAdapter adapter = new TestsRecyclerViewAdapter(root.getContext(), tests);
                testsRecyclerView.setAdapter(new TestsRecyclerViewAdapter(root.getContext(), tests));
                testsRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

                //Log.e("test", tests[0][0]);
                //Log.e("test", String.valueOf(tests[1].getMaxMark()));
                //Log.e("test", String.valueOf(tests[0].getTestPOJO()[5]));

                //Log.e("success", connectionResponse);
            }

            @Override
            public void onFailure(@NonNull Call<TestPOJO[]> call, @NonNull Throwable t)
            {
                Log.e("retrofitError", t.getMessage());
            }
        });

        return root;
    }
}