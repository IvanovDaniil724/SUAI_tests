package suai.tests.activities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.common.ConfirmationDialogBuilder;
import suai.tests.common.adapters.ChatsAdapter;
import suai.tests.common.api.ChatClass;
import suai.tests.common.api.RetrofitConnection;

import suai.tests.R;
import suai.tests.common.api.messengerAPI;

public class MessengerFragment extends Fragment
{
    public static String FIO;
    public static messengerAPI service = RetrofitConnection.messengerApi;
    public static RecyclerView recyclerViewChats;
    public static View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_messenger, container, false);

        ConstraintLayout header = root.findViewById(R.id.constraintLayoutHeaderMessenger);
        ConstraintLayout findHeader = root.findViewById(R.id.constraintLayoutSearchMessaenger);
        RecyclerView recyclerViewChats = root.findViewById(R.id.recyclerViewChats);

        ImageButton buttonSearch = root.findViewById(R.id.imageButtonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header.setVisibility(View.INVISIBLE);
                findHeader.setVisibility(View.VISIBLE);
            }
        });

        EditText find = root.findViewById(R.id.editTextFind);
        find.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                UpdateChats(recyclerViewChats, root, find);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ImageButton back = root.findViewById(R.id.imageButtonBackChat);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findHeader.setVisibility(View.INVISIBLE);
                header.setVisibility(View.VISIBLE);
                find.setText("");
            }
        });

        UpdateChats(recyclerViewChats,root, find);

        ImageButton buttonNewChat = root.findViewById(R.id.imageButtonNewChat);
        buttonNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.action_navigation_messenger_to_newChatFragment);
            }
        });

        Handler h = new Handler();
        Runnable run = new Runnable() {

            @Override
            public void run() {
                 if (ConfirmationDialogBuilder.deletedChat==1) {
                     //Log.v("g", "f");]
                     UpdateChats(recyclerViewChats, root, find);
                     ConfirmationDialogBuilder.deletedChat=0;
                 }
                h.postDelayed(this, 3000);
            }
        };
        h.postDelayed(run, 3000);
        return root;
    }

    public static void UpdateChats(RecyclerView recyclerViewChats, View root, EditText find)
    {
        ChatsAdapter.OnChatClickListener chatClickListener = new ChatsAdapter.OnChatClickListener() {
            @Override
            public void onStateClick(ChatClass chat, int position) {
                NewChatFragment.FIO = "";
                Bundle bundleId = new Bundle();
                bundleId.putInt("idChat", Integer.parseInt(chat.getChats()[0]));
                FIO = chat.getChats()[2];
                Navigation.findNavController(root).navigate(R.id.action_navigation_messenger_to_chatFragment, bundleId);
            }
        };
        Call<ChatClass[]> call = service.getChats(AccountFragment.idUser,AccountFragment.role, find.getText().toString());
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

    }

}