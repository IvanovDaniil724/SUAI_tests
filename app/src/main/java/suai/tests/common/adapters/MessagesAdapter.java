package suai.tests.common.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import suai.tests.R;
import suai.tests.activities.fragments.AccountFragment;
import suai.tests.common.api.MessagesClass;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    public interface OnMessagesClickListener{
        void onStateClick(MessagesClass messages, int position);
    }
    private final OnMessagesClickListener onClickListener;

    private final LayoutInflater inflater;
    private MessagesClass[] messages;
    private Context context;

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
        holder.messageView.setText(message.getMessages()[2]);
        if (Integer.parseInt(message.getMessages()[0]) == AccountFragment.idUser)
        {
            holder.messageView.setGravity(Gravity.RIGHT);
         /*   FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(0, 10);
            param.gravity = Gravity.RIGHT;
            holder.messageView.setLayoutParams(param);*/
            holder.messageView.setBackgroundResource(R.drawable.message_box_post);
        }
        if (Integer.parseInt(message.getMessages()[3])==1 && Integer.parseInt(message.getMessages()[0])==AccountFragment.idUser)
            holder.readView.setImageResource(R.drawable.ic_message_read);
        else if (Integer.parseInt(message.getMessages()[3])==0 && Integer.parseInt(message.getMessages()[0])==AccountFragment.idUser)
            holder.readView.setImageResource(R.drawable.ic_message_post);
        else if (Integer.parseInt(message.getMessages()[0])!=AccountFragment.idUser)
            holder.readView.setVisibility(View.INVISIBLE);
        holder.dateView.setText(message.getMessages()[1]);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(message, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // final ImageView imageView;
        final TextView messageView, dateView;
        final ImageView readView;

        ViewHolder(View view){
            super(view);
            //  imageView = (ImageView)view.findViewById(R.id.image);
            dateView = (TextView)view.findViewById(R.id.date);
            messageView = (TextView)view.findViewById(R.id.message);
            readView = (ImageView)view.findViewById(R.id.read);
        }

    }
}
