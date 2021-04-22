package suai.tests.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.common.adapters.MessagesAdapter;
import suai.tests.common.api.MessagesClass;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.ChatClass;
import suai.tests.common.api.messagesAPI;
import suai.tests.common.api.messengerAPI;

import suai.tests.R;

public class ChatFragment extends Fragment
{

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        messagesAPI service = RetrofitConnection.messagesApi;
        Integer idChat = getArguments().getInt("idChat");
        Log.e("", String.valueOf(idChat));
        RecyclerView recyclerViewMessages = root.findViewById(R.id.recyclerViewMessages);
        MessagesAdapter.OnMessagesClickListener messageClickListener = new MessagesAdapter.OnMessagesClickListener() {
            @Override
            public void onStateClick(MessagesClass chat, int position) {

            }
        };

        Call<MessagesClass[]> call = service.getMessages(idChat);
        call.enqueue(new Callback<MessagesClass[]>() {
            @Override
            public void onResponse(Call<MessagesClass[]> call, Response<MessagesClass[]> response) {
                MessagesClass[] messages = response.body();
                recyclerViewMessages.setAdapter(new MessagesAdapter(root.getContext(),messages, messageClickListener));
                recyclerViewMessages.setLayoutManager(new LinearLayoutManager(root.getContext()));
            }

            @Override
            public void onFailure(Call<MessagesClass[]> call, Throwable t) {

            }
        });

        ImageButton buttonSendMessage = root.findViewById(R.id.imageButtonSend);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;

    }
}