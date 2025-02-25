package com.example.clase4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.widget.Button;

import com.example.clase4.databinding.ActivityMain2Binding;
import com.example.clase4.workers.ContadorWorker;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        binding.buttonWorkMang.setOnClickListener(view -> {

            WorkRequest workRequest = new OneTimeWorkRequest.Builder(ContadorWorker.class).build();

            WorkManager
                    .getInstance(binding.getRoot().getContext())
                    .enqueue(workRequest);
        });
    }
}