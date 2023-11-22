package com.example.ringmap.ui.friends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ringmap.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendAdapterHold> {

    private List<String> friendList;

    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FriendAdapter() {
        this.friendList = new ArrayList<>();
    }

    public void setFriendAdapters(List<String> friendList) {
        this.friendList = friendList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendAdapterHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friends, parent, false);
        return new FriendAdapterHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapterHold holder, int position) {
        String user_id = friendList.get(position);
        holder.bind(user_id);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    static class FriendAdapterHold extends RecyclerView.ViewHolder {

        private TextView userIdTextView;

        FriendAdapterHold(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.friendID);
        }

        void bind(String userID) {
            DocumentReference documentReference = db.collection("usuarios").document(userID);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if(documentSnapshot != null) {
                        userIdTextView.setText(documentSnapshot.getString("nome"));
                    }
                }
            });
        }
    }
}
