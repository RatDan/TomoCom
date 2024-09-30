package com.danrat.tomocom.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserListViewModel extends ViewModel {
    private final MutableLiveData<List<User>> userDataList = new MutableLiveData<>();

    public LiveData<List<User>> getUserList() {
        return userDataList;
    }

    public void fetchUserData () {
        if (userDataList.getValue() != null && !userDataList.getValue().isEmpty())
            return;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.w("Firestore", "Listen failed", e);
                        return;
                    }

                    if (snapshot != null && !snapshot.isEmpty()) {
                        List<User> userList = new ArrayList<>();
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                        userDataList.setValue(userList);
                    } else {
                        List<User> userList = new ArrayList<>();
                        userDataList.setValue(userList);
                    }
                });
    }


}
