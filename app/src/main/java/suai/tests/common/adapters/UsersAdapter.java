package suai.tests.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import suai.tests.R;
import suai.tests.common.api.UserClass;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    public interface OnUserClickListener{
        void onStateClick(UserClass chats, int position);
    }
    private final UsersAdapter.OnUserClickListener onClickListener;

    private final LayoutInflater inflater;
    private final UserClass[] users;
    private Context context;

    public UsersAdapter(Context context, UserClass[] users, UsersAdapter.OnUserClickListener onClickListener) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.users_item, parent, false);
        return new UsersAdapter.ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {
        UserClass user = users[position];
        // holder.imageView.setImageResource(user.getPhoto());
        holder.fioView.setText(user.getUsers()[1]);
        holder.infoView.setText(user.getUsers()[2]);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // final ImageView imageView;
        final TextView fioView, infoView;

        ViewHolder(View view){
            super(view);
            //  imageView = (ImageView)view.findViewById(R.id.image);
            fioView = (TextView)view.findViewById(R.id.fio);
            infoView = (TextView)view.findViewById(R.id.info);
        }

    }
}
