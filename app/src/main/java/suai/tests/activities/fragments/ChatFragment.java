package suai.tests.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.common.ConfirmationDialogBuilder;
import suai.tests.common.adapters.ChatsAdapter;
import suai.tests.common.adapters.MessagesAdapter;
import suai.tests.common.api.MessagesClass;
import suai.tests.common.api.RetrofitConnection;
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
    public static String idMessage;
    public static Integer isEdit = 0;
    public static ConstraintLayout header;
    public static ConstraintLayout findHeader;
    public static ConstraintLayout edit;
    public static ImageButton buttonMoreAction;
    public static ImageButton buttonSendMessage;
    public static ImageButton backFromEdit;
    private Parcelable recyclerViewState, firstState;

    public  MessagesClass[] messages;
    public  MessagesAdapter adapter;
    Timer t;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        root = inflater.inflate(R.layout.fragment_chat, container, false);

        MessengerFragment.chats = null;

        idChat = getArguments().getInt("idChat");
        user = getArguments().getInt("idUser");
        edit = root.findViewById(R.id.constraintLayoutEdit);
        TextView fio = root.findViewById(R.id.fio);
        if (MessengerFragment.FIO=="")
            fio.setText(NewChatFragment.FIO);
        else fio.setText(MessengerFragment.FIO);
        message = root.findViewById(R.id.editTextMessage);
        backFromEdit = root.findViewById(R.id.imageButtonBackFromEdit);

        backFromEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:  backFromEdit.setImageResource(R.drawable.ic_back_arrow_white); break;
                    case MotionEvent.ACTION_UP:
                    {
                        findHeader.setVisibility(View.INVISIBLE);
                        header.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.INVISIBLE);
                        message.setText("");
                        buttonSendMessage.setBackgroundResource(R.drawable.ic_send);
                    }
                    case MotionEvent.ACTION_CANCEL:  backFromEdit.setImageResource(R.drawable.ic_back_arrow); break;
                }
                return true;
            }
        });

        header = root.findViewById(R.id.constraintLayoutHeaderMessenger);
        findHeader = root.findViewById(R.id.constraintLayoutSearchMessenger);
        buttonMoreAction = root.findViewById(R.id.imageButtonMoreAction);
        recyclerViewMessages = root.findViewById(R.id.recyclerViewMessages);
        EditText find = root.findViewById(R.id.editTextFind);

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

        find.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                UpdateMessages(recyclerViewMessages, root, idChat, message, find);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ImageButton back = root.findViewById(R.id.imageButtonBack);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:  back.setImageResource(R.drawable.ic_back_arrow_white); break;
                    case MotionEvent.ACTION_UP:
                    {
                        findHeader.setVisibility(View.INVISIBLE);
                        header.setVisibility(View.VISIBLE);
                        find.setText("");
                    }
                    case MotionEvent.ACTION_CANCEL:  back.setImageResource(R.drawable.ic_back_arrow); break;
                }
                return true;
            }
        });

        UpdateMessages(recyclerViewMessages, root, idChat, message, find);

        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
            recyclerViewState = recyclerViewMessages.getLayoutManager().onSaveInstanceState();
            UpdateMessages(recyclerViewMessages, root, idChat, message, find);
            }
        },1000,4000);

        buttonSendMessage = root.findViewById(R.id.imageButtonSend);
        buttonSendMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    {
                        if (isEdit==1)
                            buttonSendMessage.setBackgroundResource(R.drawable.ic_check_circle_outline_white);
                        else buttonSendMessage.setBackgroundResource(R.drawable.ic_baseline_send_24);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    {
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
                                                idChat = Integer.parseInt(idChats[0].getItems()[0]);
                                                SendMessages(root, idChat, message);
                                                UpdateMessages(recyclerViewMessages, root, idChat, message, find);
                                            }

                                            @Override
                                            public void onFailure(Call<ItemsPOJO[]> call, Throwable t) {
                                                UpdateMessages(recyclerViewMessages, root, idChat, message, find);
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        UpdateMessages(recyclerViewMessages, root, idChat, message, find);
                                    }
                                });
                            }
                            else
                            {
                                if (isEdit==1)
                                {
                                    Call<String> editMessage = service.editMessage(idMessage, message.getText().toString());
                                    editMessage.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                        }
                                    });
                                    isEdit = 0;
                                    message.setText("");
                                    edit.setVisibility(View.INVISIBLE);
                                    header.setVisibility(View.VISIBLE);
                                    buttonSendMessage.setBackgroundResource(R.drawable.ic_send);
                                }
                                else {
                                    SendMessages(root, idChat, message);
                                }
                                UpdateMessages(recyclerViewMessages, root, idChat, message, find);
                            }

                        }
                    }
                    case MotionEvent.ACTION_CANCEL:
                    {
                        if (isEdit==1)
                            buttonSendMessage.setBackgroundResource(R.drawable.ic_check_circle_outline);
                        else buttonSendMessage.setBackgroundResource(R.drawable.ic_send);
                        break;
                    }
                }
                return true;
            }
        });
        return root;
    }


    @Override
    public void onPause() {
        super.onPause();
        t.cancel();
    }

    public void UpdateMessages(RecyclerView recyclerViewMessages, View root, Integer idChat, EditText message, EditText find)
    {
        MessagesAdapter.OnMessagesClickListener messageClickListener = new MessagesAdapter.OnMessagesClickListener() {
            @Override
            public void onStateClick(MessagesClass chat, int position) {
            }
        };
        Call<MessagesClass[]> call = service.getMessages(idChat, String.valueOf(AccountFragment.role), find.getText().toString());
        call.enqueue(new Callback<MessagesClass[]>() {
            @Override
            public void onResponse(Call<MessagesClass[]> call, Response<MessagesClass[]> response) {
                if (messages==null)
                {
                    messages = response.body();
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(recyclerViewMessages.getContext());
                    mLayoutManager.setStackFromEnd(true);
                    adapter = new MessagesAdapter(root.getContext(), messages, messageClickListener);
                    recyclerViewMessages.setAdapter(adapter);
                    recyclerViewMessages.setLayoutManager(mLayoutManager);
                    firstState = recyclerViewMessages.getLayoutManager().onSaveInstanceState();
                    recyclerViewMessages.getLayoutManager().onRestoreInstanceState(firstState);
                }
                else
                {
                    MessagesClass[] newMessages = response.body();
                    if (newMessages!=messages)
                    {
                        adapter.update(newMessages);
                        adapter.notifyDataSetChanged();
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(recyclerViewMessages.getContext());
                        mLayoutManager.setStackFromEnd(true);
                        recyclerViewMessages.setLayoutManager(mLayoutManager);
                        recyclerViewMessages.setDrawingCacheEnabled(true);
                        recyclerViewMessages.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        messages = newMessages;
                    }

                }

                for (int i=0;i<messages.length;i++) {
                    if (Integer.parseInt(messages[i].getMessages()[4]) == 0 && Integer.parseInt(messages[i].getMessages()[1]) != AccountFragment.idUser) {
                        Call<String> read = service.readMessages(messages[i].getMessages()[0]);
                        read.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<MessagesClass[]> call, Throwable t) {
            }
        });
    }

    public void SendMessages(View root, Integer idChat, EditText message)
    {
        Call<String> c = service.createMessage(idChat.toString(),String.valueOf(AccountFragment.idUser), message.getText().toString());
        c.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        message.setText("");
    }

    public static void OpenEditField(View root)
    {
        ChatFragment.edit.setVisibility(View.VISIBLE);
        ChatFragment.findHeader.setVisibility(View.INVISIBLE);
        ChatFragment.header.setVisibility(View.INVISIBLE);
    }

}