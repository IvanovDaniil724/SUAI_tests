package suai.tests.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.checkerframework.checker.units.qual.C;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.common.adapters.ChatsAdapter;
import suai.tests.common.adapters.UsersAdapter;
import suai.tests.common.api.ChatClass;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.UserClass;
import suai.tests.common.api.UsersAPI;
import suai.tests.common.api.messengerAPI;

public class NewChatFragment extends Fragment
{
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_new_chat, container, false);

        UsersAPI service = RetrofitConnection.usersAPI;
        messengerAPI newChat = RetrofitConnection.messengerApi;

        RecyclerView recyclerViewUsers = root.findViewById(R.id.recyclerViewUsers);
        UsersAdapter.OnUserClickListener userClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void onStateClick(UserClass user, int position) {
                int student, teacher;
                if (AccountFragment.role==0)
                {
                    teacher = AccountFragment.idUser;
                    student = Integer.parseInt(user.getUsers()[0]);
                }
                else
                {
                    teacher = Integer.parseInt(user.getUsers()[0]);
                    student = AccountFragment.idUser;
                }

                Call<String> chat = newChat.getChatsWithUser(String.valueOf(teacher), String.valueOf(student));
                chat.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().length()>=17)
                        {
                            String st = response.body();
                            Log.v("g",st);
                            Integer d = Integer.parseInt(st);
                        }

                     //   }
                        /*    Call<String[]> g = newChat.createNewChat(String.valueOf(teacher), String.valueOf(student));
                            g.enqueue(new Callback<String[]>() {
                                @Override
                                public void onResponse(Call<String[]> call, Response<String[]> response) {
                                    Log.v("result",response.body()[0]);
                                }
                                @Override
                                public void onFailure(Call<String[]> call, Throwable t) {
                                    Log.v("result",t.getMessage());
                                }
                            });
                            Navigation.findNavController(root).navigate(R.id.action_newChatFragment_to_chatFragment);
                        }
                        else
                        {
                            Bundle bundle = new Bundle();
                            bundle.putInt("idChat", Integer.parseInt(response.body()[0]));
                            Navigation.findNavController(root).navigate(R.id.action_newChatFragment_to_chatFragment, bundle);
                        }*/
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("g", t.getMessage());
                    }
                });
            }
        };

        Call<UserClass[]> call = service.getStudentsOrTeachers(AccountFragment.idUser,AccountFragment.role);
        call.enqueue(new Callback<UserClass[]>() {
            @Override
            public void onResponse(Call<UserClass[]> call, Response<UserClass[]> response) {
                UserClass[] users = response.body();
                recyclerViewUsers.setAdapter(new UsersAdapter(root.getContext(),users, userClickListener));
                recyclerViewUsers.setLayoutManager(new LinearLayoutManager(root.getContext()));
            }
            @Override
            public void onFailure(Call<UserClass[]> call, Throwable t) {

            }
        });
        return root;
    }
}