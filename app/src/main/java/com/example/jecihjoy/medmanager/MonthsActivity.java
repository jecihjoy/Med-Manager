package com.example.jecihjoy.medmanager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jecihjoy.medmanager.adapters.MonthsRecyclerAdapter;

public class MonthsActivity extends AppCompatActivity {

    private MonthsRecyclerAdapter monthsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_meds);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] months = new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_months);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        monthsRecyclerAdapter = new MonthsRecyclerAdapter(months,this);
        rv.setAdapter(monthsRecyclerAdapter);
    }

}
