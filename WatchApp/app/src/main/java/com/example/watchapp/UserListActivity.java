package com.example.watchapp;


import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.watchapp.model.User;
import com.example.watchapp.restapi.RestAPIClient;
import com.example.watchapp.restapi.RestAPIService;
import com.example.watchapp.util.FileUtils;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = "UserListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean sendScreen = getIntent().getBooleanExtra("send", false);
        Log.d(TAG, "onCreate: sendScreen = " + sendScreen);
        setContentView(R.layout.user_list);

        String userId = FileUtils.readUserDetails(getFilesDir());

        WearableRecyclerView userListView = findViewById(R.id.user_list);

        RestAPIService service = RestAPIClient.getClient().create(RestAPIService.class);
        Call<List<User>> call = service.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d(TAG, "onResponse : " + response.code() + " , " + response.body().size());
                List<User> items = response.body();
                List<User> filteredItems = null;
                filteredItems = items.stream()
                        .filter(ele -> !ele.id.equals(userId))
                        .collect(Collectors.toList());
                UserListAdapter adapter = new UserListAdapter(filteredItems, sendScreen, userId);
                userListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t.toString());
            }
        });

        userListView.setEdgeItemsCenteringEnabled(true);
        userListView.setLayoutManager(new WearableLinearLayoutManager(this));

    }
}
