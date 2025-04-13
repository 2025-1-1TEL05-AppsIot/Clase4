package com.example.clase4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.clase4.databinding.ActivityMainBinding;

import com.example.clase4.viewmodel.ContadorViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Usando ExecutorService
        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;
        // **********************

        ContadorViewModel contadorViewModel =
                new ViewModelProvider(MainActivity.this).get(ContadorViewModel.class);

        contadorViewModel.getContador().observe(this, contador -> {
            //aquí o2
            binding.contadorTextView.setText(String.valueOf(contador));
       });

        binding.button.setOnClickListener(view -> {

            //es un hilo en background
            executorService.execute(() -> {
                for (int i = 1; i <= 10; i++) {
                    contadorViewModel.getContador().postValue(i); // o1
                    Log.d("msg-test", "i: " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        });

    }
}