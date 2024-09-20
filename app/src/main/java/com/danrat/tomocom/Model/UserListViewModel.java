package com.danrat.tomocom.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
                .limit(20)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<User> userList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = document.toObject(User.class);
                                userList.add(user);
                            }
                            userDataList.setValue(userList);
                        } else {
                            Log.d(TAG, "get error: ", task.getException());
                        }
                    }
                });
    }


}
