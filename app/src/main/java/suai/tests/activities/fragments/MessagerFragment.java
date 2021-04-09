package suai.tests.activities.fragments;

import android.os.Bundle;
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
import suai.tests.common.adapters.ChatAdapter;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.ChatClass;
import suai.tests.common.api.messengerAPI;

import suai.tests.R;

public class MessagerFragment extends Fragment
{
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_messenger, container, false);

        RecyclerView recyclerViewChats = root.findViewById(R.id.recyclerViewChats);
        messengerAPI service = RetrofitConnection.messengerApi;

        Call<ChatClass[]> call = service.getChats(2,0);
        call.enqueue(new Callback<ChatClass[]>() {
            @Override
            public void onResponse(Call<ChatClass[]> call, Response<ChatClass[]> response) {
                ChatClass[] chats = response.body();
                recyclerViewChats.setAdapter(new ChatAdapter(root.getContext(),chats));
                recyclerViewChats.setLayoutManager(new LinearLayoutManager(root.getContext()));
            }

            @Override
            public void onFailure(Call<ChatClass[]> call, Throwable t) {

            }
        });

        return root;
    }
}