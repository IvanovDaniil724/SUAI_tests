package suai.tests.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import suai.tests.common.api.pojo.common.ItemsPOJO;

public class ChatFragment extends Fragment
{
    messagesAPI service = RetrofitConnection.messagesApi;
    messengerAPI newChat = RetrofitConnection.messengerApi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        Integer idChat = getArguments().getInt("idChat");
        Integer user = getArguments().getInt("idUser");

        RecyclerView recyclerViewMessages = root.findViewById(R.id.recyclerViewMessages);
        UpdateMessages(recyclerViewMessages, root, idChat);
        EditText message = root.findViewById(R.id.editTextMessage);

        ImageButton buttonSendMessage = root.findViewById(R.id.imageButtonSend);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().length()!=0)
                {
                    Call<ItemsPOJO[]> chat = newChat.getChatsWithUser("2", "20");
                    chat.enqueue(new Callback<ItemsPOJO[]>() {
                        @Override
                        public void onResponse(Call<ItemsPOJO[]> call, Response<ItemsPOJO[]> response) {
                            ItemsPOJO[] idChat = response.body();
                            Integer ch = Integer.getInteger(idChat[0].toString());
                            Log.v("s",response.body()[0].toString());
                        }

                        @Override
                        public void onFailure(Call<ItemsPOJO[]> call, Throwable t) {
                            Log.e("g", t.getMessage());
                        }
                    });
                   /* if (idChat==0)
                    {
                        int teacher, student;
                        if (AccountFragment.role==0)
                        {
                            teacher = AccountFragment.idUser;
                            student = user;
                        }
                        else
                        {
                            teacher = user;
                            student = AccountFragment.idUser;
                        }

                        Call<String[]> createNewChat = newChat.createNewChat(String.valueOf(teacher), String.valueOf(student));
                        createNewChat.enqueue(new Callback<String[]>() {
                            @Override
                            public void onResponse(Call<String[]> call, Response<String[]> response) {
                                Call<ItemsPOJO[]> chat = newChat.getChatsWithUser(String.valueOf(teacher), String.valueOf(student));
                                chat.enqueue(new Callback<ItemsPOJO[]>() {
                                    @Override
                                    public void onResponse(Call<ItemsPOJO[]> call, Response<ItemsPOJO[]> response) {
                                        ItemsPOJO[] idChat = response.body();

                                    }

                                    @Override
                                    public void onFailure(Call<ItemsPOJO[]> call, Throwable t) {
                                        Log.e("g", t.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<String[]> call, Throwable t) {
                            }
                        });
                    }

                 /*   Call<String[]> call = service.createMessage(idChat.toString(),String.valueOf(AccountFragment.idUser), message.getText().toString());
                    call.enqueue(new Callback<String[]>() {
                        @Override
                        public void onResponse(Call<String[]> call, Response<String[]> response) {
                            Log.v("result",response.body()[0]);
                        }

                        @Override
                        public void onFailure(Call<String[]> call, Throwable t) {
                            Log.v("result",t.getMessage());
                        }
                    });*/
                    UpdateMessages(recyclerViewMessages, root, idChat);
                }
            }
        });

        return root;

    }

    public void UpdateMessages(RecyclerView recyclerViewMessages, View root, Integer idChat)
    {
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

    }
}