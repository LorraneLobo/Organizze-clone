package com.example.organizze.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.organizze.databinding.ActivityPrincipalBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.organizze.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

public class PrincipalActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPrincipalBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        MaterialCalendarView calendarView = binding.content.calendarView;

        calendarView.setOnMonthChangedListener((widget, date) -> {
            Log.i("wsd", "Mês: " + date);
        });

    }

    public void adicionarDespesa(View v){
        startActivity(new Intent(this, DespesaActivity.class));
    }

    public void adicionarReceita(View v){
        startActivity(new Intent(this, ReceitaActivity.class));
    }

}