package suai.tests.common.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import suai.tests.R;
import suai.tests.activities.fragments.AccountFragment;
import suai.tests.common.api.ChatClass;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    public interface OnChatClickListener{
        void onStateClick(ChatClass chats, int position);
    }
    private final OnChatClickListener onClickListener;

    private final LayoutInflater inflater;
    private final ChatClass[] chats;
    private Context context;

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
        // holder.imageView.setImageResource(chat.getPhoto());
        holder.nameView.setText(chat.getChats()[2]);
        if (Integer.parseInt(chat.getChats()[1])==AccountFragment.idUser)
            holder.messageView.setText("Вы: "+chat.getChats()[3]);
        else holder.messageView.setText(chat.getChats()[3]);
        holder.dateView.setText(chat.getChats()[4]);
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
    }

    @Override
    public int getItemCount() {
        return chats.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // final ImageView imageView;
        final TextView nameView, messageView, dateView;
        final ImageView statusView;

        ViewHolder(View view){
            super(view);
            //  imageView = (ImageView)view.findViewById(R.id.image);
            nameView = (TextView)view.findViewById(R.id.name);
            dateView = (TextView)view.findViewById(R.id.date);
            messageView = (TextView)view.findViewById(R.id.message);
            statusView = (ImageView)view.findViewById(R.id.status);
        }

    }
}