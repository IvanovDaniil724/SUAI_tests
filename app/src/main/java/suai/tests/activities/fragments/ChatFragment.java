package suai.tests.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.common.ConfirmationDialogBuilder;
import suai.tests.common.adapters.ChatsAdapter;
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
    public static messagesAPI service = RetrofitConnection.messagesApi;
    public static messengerAPI newChat = RetrofitConnection.messengerApi;
    public static RecyclerView recyclerViewMessages;
    public static View root;
    public static Integer idChat;
    public static EditText message;
    Integer user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        idChat = getArguments().getInt("idChat");
        user = getArguments().getInt("idUser");
        TextView fio = root.findViewById(R.id.fio);
        if (MessengerFragment.FIO=="")
            fio.setText(NewChatFragment.FIO);
        else fio.setText(MessengerFragment.FIO);
        EditText message = root.findViewById(R.id.editTextMessage);

        ConstraintLayout header = root.findViewById(R.id.constraintLayoutHeaderChat);
        ConstraintLayout findHeader = root.findViewById(R.id.constraintLayoutFindHeader);
         ImageButton buttonMoreAction = root.findViewById(R.id.imageButtonMoreAction);
      // registerForContextMenu(header);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), fio);
                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Поиск");
                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Удалить историю");
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case 0:
                                findHeader.setVisibility(View.VISIBLE);
                                header.setVisibility(View.INVISIBLE);
                                break;
                            case 1:
                                new ConfirmationDialogBuilder(ChatsAdapter.context, idChat.toString()).alert("Удаление", "Вы точно хотите удалить чат?",0);
                                break;
                        }
                        return true;
                    }
                });
            }
        });



        RecyclerView recyclerViewMessages = root.findViewById(R.id.recyclerViewMessages);
        UpdateMessages(recyclerViewMessages, root, idChat, message);

        Handler h = new Handler();
        Runnable run = new Runnable() {

            @Override
            public void run() {
                if (ConfirmationDialogBuilder.deletedMessage==1) {
                    //Log.v("g", "f");]
                    UpdateMessages(recyclerViewMessages,root,idChat,message);
                    ConfirmationDialogBuilder.deletedMessage=0;
                }
                h.postDelayed(this, 3000);
            }
        };
        h.postDelayed(run, 3000);

        ImageButton buttonSendMessage = root.findViewById(R.id.imageButtonSend);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().length()!=0)
                {
                    if (idChat==0)
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

                        Call<String> createNewChat = newChat.createNewChat(String.valueOf(teacher), String.valueOf(student));
                        createNewChat.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Call<ItemsPOJO[]> ch = newChat.getChatsWithUser(String.valueOf(teacher), String.valueOf(student));
                                ch.enqueue(new Callback<ItemsPOJO[]>() {
                                    @Override
                                    public void onResponse(Call<ItemsPOJO[]> call, Response<ItemsPOJO[]> response) {
                                        ItemsPOJO[] idChats = response.body();
                                        Log.v("df",idChats[0].getItems()[0]);
                                        idChat = Integer.parseInt(idChats[0].getItems()[0]);
                                        SendMessages(root, idChat, message);
                                        UpdateMessages(recyclerViewMessages, root, idChat, message);
                                    }

                                    @Override
                                    public void onFailure(Call<ItemsPOJO[]> call, Throwable t) {
                                        Log.e("g", t.getMessage());
                                        UpdateMessages(recyclerViewMessages, root, idChat, message);
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("h",t.getMessage());
                                UpdateMessages(recyclerViewMessages, root, idChat, message);
                            }
                        });
                    }
                    else
                    {
                        SendMessages(root, idChat, message);
                        UpdateMessages(recyclerViewMessages, root, idChat, message);
                    }

                }
            }
        });

        return root;

    }

    public static void UpdateMessages(RecyclerView recyclerViewMessages, View root, Integer idChat, EditText message)
    {
        MessagesAdapter.OnMessagesClickListener messageClickListener = new MessagesAdapter.OnMessagesClickListener() {
            @Override
            public void onStateClick(MessagesClass chat, int position) {

            }
        };
        Call<MessagesClass[]> call = service.getMessages(idChat, String.valueOf(AccountFragment.role));
        call.enqueue(new Callback<MessagesClass[]>() {
            @Override
            public void onResponse(Call<MessagesClass[]> call, Response<MessagesClass[]> response) {
                MessagesClass[] messages = response.body();
                for (int i=0;i<messages.length;i++) {
                    if (Integer.parseInt(messages[i].getMessages()[4]) == 0 && Integer.parseInt(messages[i].getMessages()[1]) != AccountFragment.idUser) {
                        Call<String> read = service.readMessages(messages[i].getMessages()[0]);
                        read.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.v("result", response.body());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("result", t.getMessage());
                            }
                        });
                    }
                }
                recyclerViewMessages.setAdapter(new MessagesAdapter(root.getContext(),messages, messageClickListener));
                recyclerViewMessages.setLayoutManager(new LinearLayoutManager(root.getContext()));
                message.setText("");
            }

            @Override
            public void onFailure(Call<MessagesClass[]> call, Throwable t) {
                Log.e("result",t.getMessage());
            }
        });
    }

    public void SendMessages(View root, Integer idChat, EditText message)
    {
        Call<String> c = service.createMessage(idChat.toString(),String.valueOf(AccountFragment.idUser), message.getText().toString());
        c.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v("result",response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("result",t.getMessage());
            }
        });
    }

    public void EditMessages(View root, Integer idMessage, EditText message)
    {
        Call<String> call = service.editMessage(idMessage.toString(),message.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v("result",response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("result",t.getMessage());
            }
        });
    }

}