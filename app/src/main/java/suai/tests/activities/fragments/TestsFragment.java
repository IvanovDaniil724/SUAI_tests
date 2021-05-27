package suai.tests.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.common.OnSwipeListener;
import suai.tests.common.adapters.TestsRecyclerViewAdapter;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.pojo.common.ItemsPOJO;
import suai.tests.common.api.testsAPI;

public class TestsFragment extends Fragment
{
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_tests, container, false);

        MessengerFragment.chats = null;

        root.setOnTouchListener(new OnSwipeListener(root.getContext())
        {
            public void onSwipeTop() {  }
            public void onSwipeRight() { }
            public void onSwipeLeft() { Navigation.findNavController(root).navigate(R.id.action_navigation_tests_to_navigation_messenger); }
            public void onSwipeBottom() {  }
        });

        getTests(root);

        root.findViewById(R.id.TestsRefreshImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTests(root, new ItemsPOJO[]{}); getTests(root);
            }
        });

        return root;
    }

    public void getTests(View root)
    {
        testsAPI service = RetrofitConnection.testsApi; Call<ItemsPOJO[]> call;
        call = service.getTests(AccountFragment.idUser, AccountFragment.role);
        call.enqueue(new Callback<ItemsPOJO[]>()
        {
            @Override
            public void onResponse(@NonNull Call<ItemsPOJO[]> call, @NonNull Response<ItemsPOJO[]> response)
            {
                ItemsPOJO[] tests = response.body(); setTests(root, tests);
            }

            @Override
            public void onFailure(@NonNull Call<ItemsPOJO[]> call, @NonNull Throwable t)
            {
                Log.e("retrofitError", t.getMessage());
            }
        });
    }

    public void setTests(View root, ItemsPOJO[] tests)
    {
        RecyclerView testsRecyclerView = root.findViewById(R.id.TestsRecyclerView);
        TestsRecyclerViewAdapter.OnTestClickListener testClickListener = new TestsRecyclerViewAdapter.OnTestClickListener() {
            @Override
            public void onStateClick(ItemsPOJO test, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("idTest", Integer.parseInt(tests[position].getItems()[0]));
                Navigation.findNavController(root).navigate(R.id.action_navigation_tests_to_testDetailsFragment, bundle);
            }
        };
        TestsRecyclerViewAdapter adapter = new TestsRecyclerViewAdapter(root.getContext(), testClickListener, tests);
        testsRecyclerView.setAdapter(adapter);
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
    }
}