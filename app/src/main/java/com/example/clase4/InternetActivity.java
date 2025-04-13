package com.example.clase4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.clase4.databinding.ActivityInternetBinding;
import com.example.clase4.databinding.ActivityMainBinding;
import com.example.clase4.dto.Comment;
import com.example.clase4.dto.Post;
import com.example.clase4.dto.Profile;
import com.example.clase4.services.TypicodeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InternetActivity extends AppCompatActivity {

    private ActivityInternetBinding binding;
    TypicodeService typicodeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toast.makeText(this, "Tiene internet: " + tengoInternet(), Toast.LENGTH_LONG).show();

        // Utilizando Retrofit
        typicodeService = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TypicodeService.class);

        binding.buttonInternetWS.setOnClickListener(view -> fetchProfileFromWs());
    }

    public boolean tengoInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean tieneInternet = false;
        if (connectivityManager != null) {
            NetworkCapabilities capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("msg-Internet", "NetworkCapabilities.TRANSPORT_CELLULAR");
                    tieneInternet = true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("msg-Internet", "NetworkCapabilities.TRANSPORT_WIFI");
                    tieneInternet = true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("msg-Internet", "NetworkCapabilities.TRANSPORT_ETHERNET");
                    tieneInternet = true;
                }
            }
        }
        return tieneInternet;
    }

    public void fetchProfileFromWs(){
        if(tengoInternet()){
            typicodeService.getProfile().enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    //aca estoy en el UI Thread
                    if(response.isSuccessful()){
                        Profile profile = response.body();
                        binding.textViewInternetWS.setText(profile.getName());
                        Log.d("msg-test-ws-profile",  "Profile");
                        Log.d("msg-test-ws-profile","name: " + profile.getName());
                        fetchPostFromWs(1);
                        fetchCommentsFromWs();
                    } else{
                        Log.d("msg-test-ws-profile", "error en la respuesta del webservice");
                    }

                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void fetchCommentsFromWs(){
        if(tengoInternet()){
            typicodeService.getComments().enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    if(response.isSuccessful()){
                        List<Comment> comments = response.body();
                        Log.d("msg-test-ws-comments",  "Comment");
                        for(Comment c : comments){
                            Log.d("msg-test-ws-comments",
                                    "id: " + c.getId() +
                                            " | body: " + c.getBody() +
                                            " | postId: " + c.getPostId());
                        }
                    } else {
                        Log.d("msg-test", "error en la respuesta del webservice");

                    }

                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            //typicodeService.getProfileWithData(nombre, apellido)
        }
    }

    public void fetchPostFromWs(int id){
        if(tengoInternet()){
            typicodeService.existePost(id).enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    if(response.isSuccessful()){
                        List<Post> post = response.body();
                        Log.d("msg-test-ws-post","Post");
                        for(Post p : post){
                            Log.d("msg-test-ws-post","id: " + p.getId()
                                    + " | title: " + p.getTitle());
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            //typicodeService.getProfileWithData(nombre, apellido)
        }
    }

}