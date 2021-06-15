package suai.tests.activities.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.activities.MainActivity;
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
    public static ConstraintLayout header;
    public static ConstraintLayout findHeader;
    public static ImageButton buttonSearch;
    private Parcelable recyclerViewState;
    public static ChatsAdapter adapter;
    public static ChatClass[] chats;
    public LinearLayoutManager mLayoutManager;
    Timer t;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_messenger, container, false);

        chats = null;
        header = root.findViewById(R.id.constraintLayoutHeaderMessenger);
        findHeader = root.findViewById(R.id.constraintLayoutSearchMessenger);
        recyclerViewChats = root.findViewById(R.id.recyclerViewChats);

        buttonSearch = root.findViewById(R.id.imageButtonSearch);
        buttonSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: buttonSearch.setImageResource(R.drawable.ic_search_white); break;
                    case MotionEvent.ACTION_UP:
                    {
                        buttonSearch.setImageResource(R.drawable.ic_search);
                        header.setVisibility(View.INVISIBLE);
                        findHeader.setVisibility(View.VISIBLE);
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: buttonSearch.setImageResource(R.drawable.ic_search); break;
                }
                return true;
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

        ImageButton back = root.findViewById(R.id.imageButtonBack);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: back.setImageResource(R.drawable.ic_back_arrow_white); break;
                    case MotionEvent.ACTION_UP:
                    {
                        back.setImageResource(R.drawable.ic_back_arrow);
                        findHeader.setVisibility(View.INVISIBLE);
                        header.setVisibility(View.VISIBLE);
                        find.setText("");
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: back.setImageResource(R.drawable.ic_back_arrow); break;
                }
                return true;
            }
        });

        UpdateChats(recyclerViewChats,root, find);

        ImageButton buttonNewChat = root.findViewById(R.id.imageButtonNewChat);
        buttonNewChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN: buttonNewChat.setBackgroundResource(R.drawable.round_button_tint); break;
                            case MotionEvent.ACTION_UP:
                            {
                                buttonNewChat.setBackgroundResource(R.drawable.round_button);
                                Navigation.findNavController(root).navigate(R.id.action_navigation_messenger_to_newChatFragment);
                                break;
                            }
                            case MotionEvent.ACTION_CANCEL: buttonNewChat.setBackgroundResource(R.drawable.round_button); break;
                        }
                return true;
            }
        });

        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                recyclerViewState = recyclerViewChats.getLayoutManager().onSaveInstanceState();
                UpdateChats(recyclerViewChats, root, find);
            }
        },1000,4000);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        t.cancel();
    }

    public void UpdateChats(RecyclerView recyclerViewChats, View root, EditText find)
    {
         ChatsAdapter.OnChatClickListener chatClickListener = new ChatsAdapter.OnChatClickListener() {
            @Override
            public void onStateClick(ChatClass chat, int position) {
                NewChatFragment.FIO = "";
                Bundle bundleId = new Bundle();
                bundleId.putInt("idChat", Integer.parseInt(chat.getChats()[0]));
                FIO = chat.getChats()[2];
                Navigation.findNavController(MainActivity.activity,R.id.nav_host_fragment).navigate(R.id.action_navigation_messenger_to_chatFragment, bundleId);
            }
        };

        Call<ChatClass[]> call = service.getChats(AccountFragment.idUser,AccountFragment.role, find.getText().toString());
        call.enqueue(new Callback<ChatClass[]>() {
            @Override
            public void onResponse(Call<ChatClass[]> call, Response<ChatClass[]> response) {
                if (chats==null)
                {
                    chats = response.body();
                    adapter = new ChatsAdapter(root.getContext(),chats,chatClickListener);
                    recyclerViewChats.setAdapter(adapter);
                    mLayoutManager = new LinearLayoutManager(recyclerViewChats.getContext());
                    recyclerViewChats.setLayoutManager(mLayoutManager);
                    recyclerViewChats.setDrawingCacheEnabled(true);
                    recyclerViewChats.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                }
                else
                {
                    ChatClass[] newChats = response.body();
                    adapter.update(newChats);
                    adapter.notifyDataSetChanged();
                    if (newChats!=chats)
                        recyclerViewChats.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                }
            }
            @Override
            public void onFailure(Call<ChatClass[]> call, Throwable t) {

            }
        });

    }

}