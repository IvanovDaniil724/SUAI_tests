package suai.tests.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import suai.tests.R;
import suai.tests.activities.fragments.AccountFragment;
import suai.tests.activities.fragments.ChatFragment;
import suai.tests.common.api.RetrofitConnection;
import suai.tests.common.api.messagesAPI;
import suai.tests.common.api.messengerAPI;

public class ConfirmationDialogBuilder {
    private final Context context;
    private final String idChat;
    public static int deletedChat;
    public static int deletedMessage;

    public ConfirmationDialogBuilder(Context context, String idChat) {
        this.context = context;
        this.idChat = idChat;
    }

    public void alert(String title, String message, int delete)
    {
        messengerAPI service = RetrofitConnection.messengerApi;
        messagesAPI serviceMessages = RetrofitConnection.messagesApi;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (delete == 0)
                {
                    Call<String> call = service.deleteChat(idChat, String.valueOf(AccountFragment.role));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
                    Navigation.findNavController(ChatFragment.root).navigate(R.id.action_chatFragment_to_navigation_messenger);
                    deletedChat = 1;
                }
                else
                {
                    Call<String> call = serviceMessages.deleteMessage(idChat);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
                    deletedMessage = 1;
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id) { }
        });
        builder.setMessage(message).setTitle(title);
        AlertDialog dialog = builder.create(); dialog.show();
    }
}

