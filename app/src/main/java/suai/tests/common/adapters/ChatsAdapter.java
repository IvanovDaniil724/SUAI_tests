package suai.tests.common.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import suai.tests.R;
import suai.tests.activities.MainActivity;
import suai.tests.activities.fragments.AccountFragment;
import suai.tests.activities.fragments.ChatFragment;
import suai.tests.activities.fragments.MessengerFragment;
import suai.tests.activities.fragments.NewChatFragment;
import suai.tests.common.AlertDialogBuilder;
import suai.tests.common.ConfirmationDialogBuilder;
import suai.tests.common.api.ChatClass;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {


    public interface OnChatClickListener{
        void onStateClick(ChatClass chats, int position);
    }

    public interface OnChatTouchListener{
        void onTouch(ChatClass chats, int position);
    }

  //  private final OnChatTouchListener onTouchListener;
    private final OnChatClickListener onClickListener;

    private final LayoutInflater inflater;
    private final ChatClass[] chats;
    public static Context context;

    public ChatsAdapter(Context context, ChatClass[] chats, OnChatClickListener onClickListener) {
        this.chats = chats;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public void onBindViewHolder(ChatsAdapter.ViewHolder holder, int position) {
        ChatClass chat = chats[position];
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                MenuItem Delete = contextMenu.add(Menu.NONE, 1, 1, "Удалить");
                Delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        new ConfirmationDialogBuilder(ChatsAdapter.context, chat.getChats()[0]).alert("Удаление", "Вы точно хотите удалить чат?",0);
                        return true;
                    }
                });
            }
        });
        // holder.imageView.setImageResource(chat.getPhoto());
        holder.nameView.setText(chat.getChats()[2]);
        if (Integer.parseInt(chat.getChats()[1])==AccountFragment.idUser)
            holder.messageView.setText("Вы: "+chat.getChats()[3]);
        else holder.messageView.setText(chat.getChats()[3]);

        if (Integer.parseInt(chat.getChats()[5])==1 && Integer.parseInt(chat.getChats()[1])==AccountFragment.idUser)
            holder.statusView.setImageResource(R.drawable.ic_message_read);
        else if (Integer.parseInt(chat.getChats()[5])==0 && Integer.parseInt(chat.getChats()[1])==AccountFragment.idUser)
            holder.statusView.setImageResource(R.drawable.ic_message_post);
        else if (Integer.parseInt(chat.getChats()[5])==0 && Integer.parseInt(chat.getChats()[1])!=AccountFragment.idUser)
            holder.statusView.setImageResource(R.drawable.avatar_placeholder);
        else if (Integer.parseInt(chat.getChats()[5])==1 && Integer.parseInt(chat.getChats()[1])!=AccountFragment.idUser)
            holder.statusView.setVisibility(View.INVISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(chat, position);
            }
        });

        holder.layoutView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: holder.layoutView.setBackgroundColor(R.color.primary_tint); break;
                    case MotionEvent.ACTION_UP:
                    {
                        NewChatFragment.FIO = "";
                        Bundle bundleId = new Bundle();
                        bundleId.putInt("idChat", Integer.parseInt(chat.getChats()[0]));
                        MessengerFragment.FIO = chat.getChats()[2];
                        Navigation.findNavController(MainActivity.activity,R.id.nav_host_fragment).navigate(R.id.action_navigation_messenger_to_chatFragment, bundleId);
                        holder.layoutView.setBackgroundColor(android.R.color.transparent); break;
                    }
                    case MotionEvent.ACTION_CANCEL: holder.layoutView.setBackgroundColor(android.R.color.transparent); break;
                }
                return true;
            }
        });

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date chatDate = simpledateformat.parse(chat.getChats()[4],pos);
        Calendar instance = Calendar.getInstance();
        instance.setTime(chatDate);
        instance.add(Calendar.DAY_OF_MONTH, 1);
        Date newDate = instance.getTime();

        SimpleDateFormat oldMessageFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        if (newDate.getTime()>=System.currentTimeMillis())
            holder.dateView.setText(timeFormat.format(chatDate));
        else holder.dateView.setText(oldMessageFormat.format(chatDate));

    }

    @Override
    public int getItemCount() {
        return chats.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // final ImageView imageView;
        final TextView nameView, messageView, dateView;
        final ImageView statusView;
        final ConstraintLayout layoutView;

        ViewHolder(View view){
            super(view);
            //  imageView = (ImageView)view.findViewById(R.id.image);
            nameView = (TextView)view.findViewById(R.id.name);
            dateView = (TextView)view.findViewById(R.id.date);
            messageView = (TextView)view.findViewById(R.id.message);
            statusView = (ImageView)view.findViewById(R.id.status);
            layoutView = (ConstraintLayout)view.findViewById(R.id.constraintLayoutChat);
            //view.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }



    }

}