package suai.tests.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import suai.tests.R;
import suai.tests.common.api.ChatClass;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<ChatClass> chats;

    ChatAdapter(Context context, List<ChatClass> chats) {
        this.chats = chats;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        ChatClass chat = chats.get(position);
        holder.imageView.setImageResource(chat.getPhoto());
        holder.nameView.setText(chat.getName());
        holder.dateView.setText(chat.getLastMessage().toString());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, dateView;

        ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.image);
            nameView = (TextView)view.findViewById(R.id.name);
            dateView = (TextView)view.findViewById(R.id.date);
        }

    }
}
