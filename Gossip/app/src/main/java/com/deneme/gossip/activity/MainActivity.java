package com.deneme.gossip.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deneme.gossip.R;
import com.deneme.gossip.dialog.CustomDialog;
import com.deneme.gossip.dialog.WaitDialog;
import com.deneme.gossip.model.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    private RecyclerView roomsRecyclerView;

    private final List<Room> roomList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        roomsRecyclerView = findViewById(R.id.rooms_recycler_view);
        roomsRecyclerView.setHasFixedSize(true);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        roomsRecyclerView.setAdapter(new RecyclerViewAdapter(this,roomList));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(MainActivity.this, (documentSnapshot, e) -> {
                    if (e == null) {
                        ((TextView)findViewById(R.id.toolbar_title_text_view)).setText(String.format("%s %s",documentSnapshot.get("firstName"),documentSnapshot.get("lastName")));
                    }
                });
        findViewById(R.id.sign_out_image_view).setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                new CustomDialog(MainActivity.this, "Are you sure?", "Do you want to log out?", true, true, result -> {
                    if (result == CustomDialog.ActionListener.ActionResult.OK){
                        signOut();
                    }
                }).show();
            }
        });
        WaitDialog waitDialog = new WaitDialog(this);
        waitDialog.show();

        FirebaseFirestore.getInstance().collection("rooms")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        waitDialog.dismiss();
                        if (e == null) {
                            roomList.clear();
                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot document : documents) {
                                roomList.add(new Room(document.getString("name"),document.getDate("time"),document.getLong("userCount")));
                            }
                            Collections.sort(roomList, (first, second) -> first.getName().compareTo(second.getName()));
                            ((RecyclerViewAdapter)roomsRecyclerView.getAdapter()).notifyDataSetChanged();
                        }else {
                            Log.e(TAG, "onEvent: ", e);
                        }
                    }
                });

  /*      FirebaseFirestore.getInstance().collection("GossipRooms").get().addOnCompleteListener(this, task -> {
            waitDialog.dismiss();
            if (task.isSuccessful()) {
                List<Room> roomList = new ArrayList<>();
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    roomList.add(new Room(document.getString("name"),document.getDate("time"),document.getLong("userCount")));
                }
                Collections.sort(roomList, (first, second) -> first.getName().compareTo(second.getName()));
                roomsRecyclerView.setAdapter(new RecyclerViewAdapter(MainActivity.this, roomList));
            } else {
                Log.e(TAG,"onComplete: ",task.getException());
            }
        });
*/
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {

        private List<Room> roomList;


        private Context context;

        public RecyclerViewAdapter(Context context, List<Room> roomList){
            this.context = context;
            this.roomList = roomList;
        }

        public RecyclerViewAdapter(List<Room> roomList) {
            this.roomList = roomList;
        }

        @NonNull
        @Override
        public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_room, parent, false);
            return new RowHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RowHolder holder, int position) {
            Room room = roomList.get(position);
            holder.nameTextView.setText(room.getName());
            holder.timeTextView.setText(new SimpleDateFormat("dd-mm-yyyy").format(room.getTime()));
            Glide.with(context).load(room.getTime()).into(holder.iconImageView);
            holder.userCountTextView.setText(String.valueOf(room.getUserCount()));
        }

        @Override
        public int getItemCount() {
            return roomList.size();
        }

        public static class RowHolder extends RecyclerView.ViewHolder {

            ImageView iconImageView;
            TextView nameTextView;
            TextView timeTextView;
            TextView userCountTextView;

            public RowHolder(@NonNull View itemView) {
                super(itemView);
                iconImageView = itemView.findViewById(R.id.icon_image_view);
                nameTextView = itemView.findViewById(R.id.name_text_view);
                timeTextView = itemView.findViewById(R.id.time_text_view);
                userCountTextView = itemView.findViewById(R.id.user_count_text_view);
            }
        }
    }
    
}
















