package suai.tests.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogBuilder
{
    private final Context context;

    public AlertDialogBuilder(Context context) {
        this.context = context;
    }

    public void alert(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });
        builder.setMessage(message).setTitle(title);
        AlertDialog dialog = builder.create(); dialog.show();
    }
}
