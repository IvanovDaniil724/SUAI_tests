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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.common.adapters.ChatsAdapter;
import suai.tests.common.adapters.MessagesAdapter;
import suai.tests.common.api.ChatClass;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.messagesAPI;
import suai.tests.common.api.MessagesClass;

import suai.tests.R;
import suai.tests.common.api.messengerAPI;

public class MessengerFragment extends Fragment
{
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_messenger, container, false);

        messengerAPI service = RetrofitConnection.messengerApi;

        RecyclerView recyclerViewChats = root.findViewById(R.id.recyclerViewChats);
        ChatsAdapter.OnChatClickListener chatClickListener = new ChatsAdapter.OnChatClickListener() {
            @Override
            public void onStateClick(ChatClass chat, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("idChat", Integer.parseInt(chat.getChats()[0]));
                Navigation.findNavController(root).navigate(R.id.action_navigation_messenger_to_chatFragment, bundle);
            }
        };

        Call<ChatClass[]> call = service.getChats(AccountFragment.idUser,AccountFragment.role);
        call.enqueue(new Callback<ChatClass[]>() {
            @Override
            public void onResponse(Call<ChatClass[]> call, Response<ChatClass[]> response) {
                ChatClass[] chats = response.body();
                recyclerViewChats.setAdapter(new ChatsAdapter(root.getContext(),chats, chatClickListener));
                recyclerViewChats.setLayoutManager(new LinearLayoutManager(root.getContext()));
            }
            @Override
            public void onFailure(Call<ChatClass[]> call, Throwable t) {

            }
        });

        ImageButton buttonNewChat = root.findViewById(R.id.imageButtonNewChat);
        buttonNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.action_navigation_messenger_to_newChatFragment);
            }
        });
        return root;
    }
}