package com.example.jecihjoy.medmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.example.jecihjoy.medmanager.adapters.DatabaseAdapter;
import com.example.jecihjoy.medmanager.adapters.MedsRecyclerAdapter;
import com.example.jecihjoy.medmanager.model.Medicine;

import java.util.ArrayList;

public class MonthlyMedsActivity extends AppCompatActivity {
    public static long id;
    public DatabaseAdapter databaseAdapter;
    public ArrayList<Medicine> medicines;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med);
        bundle = getIntent().getExtras();
        setTitle(bundle.getString("month"));

    //dbAdapter class
    databaseAdapter = new DatabaseAdapter(this);
    databaseAdapter.open();
    medicines = databaseAdapter.getMonthlyMeds(bundle.getString("month").toLowerCase());
    databaseAdapter.close();

    RecyclerView rv = (RecyclerView) findViewById(R.id.rv_recycler_view);
        if((medicines.size() != 0)){
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rv.setLayoutManager(llm);
            rv.setHasFixedSize(true);
            MedsRecyclerAdapter adapter = new MedsRecyclerAdapter(medicines);
            rv.setAdapter(adapter);
    }else {
        Toast.makeText(this, "No any medication information for "+bundle.getString("month"), Toast.LENGTH_SHORT).show();
    }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                            ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            long id = (long) viewHolder.itemView.getTag();
            databaseAdapter.open();
            databaseAdapter.deleteMedicine(id);
            Intent intent = new Intent(getApplicationContext(), AllMedsActivity.class);
            startActivity(intent);
            databaseAdapter.close();
        }
    }).attachToRecyclerView(rv);

    }
}
