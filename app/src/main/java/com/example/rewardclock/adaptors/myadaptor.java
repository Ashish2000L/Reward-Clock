package com.example.rewardclock.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.rewardclock.R;
import com.example.rewardclock.database.AppDatabase;
import com.example.rewardclock.database.UserDao;
import com.example.rewardclock.database.entities.User;

import java.util.List;

public class myadaptor extends RecyclerView.Adapter<myadaptor.myviewholder> {

    List<User> users;

    public myadaptor(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_row_design,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.recid.setText(String.valueOf(users.get(position).getUid()));
        holder.recfname.setText(users.get(position).getFirstName());
        holder.reclname.setText(users.get(position).getLastName());

        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.AppDatabases db = Room.databaseBuilder(holder.recid.getContext(),
                        AppDatabase.AppDatabases.class, "room_db").allowMainThreadQueries().build();
                UserDao userDao = db.userDao();
                userDao.deleteById(users.get(position).getUid());

                users.remove(position);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class myviewholder extends RecyclerView.ViewHolder{

        TextView recid,recfname,reclname;
        ImageButton delbtn;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            recid = itemView.findViewById(R.id.uid);
            recfname = itemView.findViewById(R.id.fname);
            reclname = itemView.findViewById(R.id.lname);
            delbtn = itemView.findViewById(R.id.delbtn);
        }
    }

}
