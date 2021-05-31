package suai.tests.common.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.C;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import suai.tests.R;
import suai.tests.activities.fragments.AccountFragment;
import suai.tests.activities.fragments.ChatFragment;
import suai.tests.common.AndroidElementsBuilder;
import suai.tests.common.ConfirmationDialogBuilder;
import suai.tests.common.api.MessagesClass;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    public interface OnMessagesClickListener{
        void onStateClick(MessagesClass messages, int position);
    }
    private final OnMessagesClickListener onClickListener;
    private final LayoutInflater inflater;
    private MessagesClass[] messages;
    private static Context context;


    public MessagesAdapter(Context context, MessagesClass[] messages, OnMessagesClickListener onClickListener) {
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.onClickListener = onClickListener;
    }


    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(MessagesAdapter.ViewHolder holder, int position) {
        MessagesClass message = messages[position];

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date messageDate = simpledateformat.parse(message.getMessages()[2],pos);
        Calendar instance = Calendar.getInstance();
        instance.setTime(messageDate);
        instance.add(Calendar.DAY_OF_MONTH, 1);
        Date newDate = instance.getTime();

        SimpleDateFormat oldMessageFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        if (newDate.getTime()>=System.currentTimeMillis())
            holder.dateView.setText(timeFormat.format(messageDate));
        else holder.dateView.setText(oldMessageFormat.format(messageDate));
        
        if ((Integer.parseInt(message.getMessages()[1]) == AccountFragment.idUser) && newDate.getTime()>=System.currentTimeMillis())
        {
            holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    MenuItem Delete = contextMenu.add(Menu.NONE, 1, 1, "Удалить");
                    Delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            new ConfirmationDialogBuilder(MessagesAdapter.context, message.getMessages()[0]).alert("Удаление", "Вы точно хотите удалить сообщение?", 1);
                            return true;
                        }
                    });
                    MenuItem Edit = contextMenu.add(Menu.NONE, 2, 2, "Редактировать");
                    Edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            ChatFragment.OpenEditField(ChatFragment.root);
                            ChatFragment.message.setText(message.getMessages()[3]);
                            ChatFragment.buttonSendMessage.setBackgroundResource(R.drawable.ic_check_circle_outline);
                            ChatFragment.isEdit = 1;
                            ChatFragment.idMessage = message.getMessages()[0];
                          //  message.getMessages()[3];
                            //   new ConfirmationDialogBuilder(MessagesAdapter.context, message.getMessages()[0]).alert("Редактирование", "Вы точно хотите удалить сообщение?", 1);
                            return true;
                        }
                    });
                }
            });
        }
        else
        {
            holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                }
            });
        }
        holder.messageView.setText(message.getMessages()[3]);

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        holder.messageView.setMaxWidth((int)(width/1.25));
        if (Integer.parseInt(message.getMessages()[1]) == AccountFragment.idUser)
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.constrainHeight(holder.messageView.getId(), ConstraintSet.WRAP_CONTENT);

            constraintSet.clear(holder.messageView.getId(),ConstraintSet.LEFT);
            constraintSet.connect(holder.messageView.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
            constraintSet.connect(holder.messageView.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT);

            constraintSet.clear(holder.dateView.getId(),ConstraintSet.LEFT);
            constraintSet.connect(holder.dateView.getId(),ConstraintSet.RIGHT, holder.messageView.getId(), ConstraintSet.RIGHT,16);
            constraintSet.connect(holder.dateView.getId(),ConstraintSet.TOP, holder.messageView.getId(),ConstraintSet.BOTTOM);
            constraintSet.constrainWidth(holder.dateView.getId(), ConstraintSet.WRAP_CONTENT);

            constraintSet.clear(holder.editView.getId(), ConstraintSet.RIGHT);
            constraintSet.clear(holder.editView.getId(), ConstraintSet.LEFT);
            constraintSet.connect(holder.editView.getId(), ConstraintSet.RIGHT, holder.readView.getId(), ConstraintSet.LEFT, 16);
            constraintSet.connect(holder.editView.getId(),ConstraintSet.TOP, holder.messageView.getId(),ConstraintSet.BOTTOM);
            constraintSet.constrainWidth(holder.editView.getId(), ConstraintSet.WRAP_CONTENT);

            constraintSet.applyTo(holder.layoutView);
            holder.messageView.setBackgroundResource(R.drawable.message_box_post);
        }
        else
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.constrainHeight(holder.messageView.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.clear(holder.messageView.getId(),ConstraintSet.RIGHT);
            constraintSet.connect(holder.messageView.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
            constraintSet.connect(holder.messageView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT);

            constraintSet.clear(holder.dateView.getId(),ConstraintSet.RIGHT);
            constraintSet.connect(holder.dateView.getId(),ConstraintSet.LEFT, holder.messageView.getId(), ConstraintSet.LEFT,16);
            constraintSet.connect(holder.dateView.getId(),ConstraintSet.TOP, holder.messageView.getId(),ConstraintSet.BOTTOM);
            constraintSet.constrainWidth(holder.dateView.getId(), ConstraintSet.WRAP_CONTENT);

            constraintSet.clear(holder.editView.getId(), ConstraintSet.RIGHT);
            constraintSet.clear(holder.editView.getId(), ConstraintSet.LEFT);
            constraintSet.connect(holder.editView.getId(), ConstraintSet.LEFT, holder.dateView.getId(), ConstraintSet.RIGHT, 16);
            constraintSet.connect(holder.editView.getId(),ConstraintSet.TOP, holder.messageView.getId(),ConstraintSet.BOTTOM);
            constraintSet.constrainWidth(holder.editView.getId(), ConstraintSet.WRAP_CONTENT);

            constraintSet.applyTo(holder.layoutView);
            holder.messageView.setBackgroundResource(R.drawable.message_box_get);
        }
        if (Integer.parseInt(message.getMessages()[4])==1 && Integer.parseInt(message.getMessages()[1])==AccountFragment.idUser)
            holder.readView.setImageResource(R.drawable.ic_message_read);
        else if (Integer.parseInt(message.getMessages()[4])==0 && Integer.parseInt(message.getMessages()[1])==AccountFragment.idUser)
            holder.readView.setImageResource(R.drawable.ic_message_post);
        if (Integer.parseInt(message.getMessages()[1])!=AccountFragment.idUser)
            holder.readView.setVisibility(View.INVISIBLE);
        else
            holder.readView.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(message, position);
            }
        });
        if (Integer.parseInt(message.getMessages()[5])==1)
            holder.editView.setVisibility(View.VISIBLE);
        else holder.editView.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }

    public void update(MessagesClass[] newMessages) { this.messages=newMessages;}

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // final ImageView imageView;
        final TextView messageView, dateView, editView;
        final ImageView readView;
        final ConstraintLayout layoutView;

        ViewHolder(View view){
            super(view);
            dateView = (TextView)view.findViewById(R.id.date);
            messageView = (TextView)view.findViewById(R.id.message);
            readView = (ImageView)view.findViewById(R.id.read);
            editView = (TextView)view.findViewById(R.id.isEdit);
            layoutView = (ConstraintLayout)view.findViewById(R.id.layoutMessage);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }
    }

}
