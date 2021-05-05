package suai.tests.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import suai.tests.common.adapters.ChatsAdapter;

public class ConfirmationDialogBuilder {
    private final Context context;

    public ConfirmationDialogBuilder(Context context) {
        this.context = context;
    }

    public void alert(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (context== ChatsAdapter.context)
                    Log.v("g","gr");
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

