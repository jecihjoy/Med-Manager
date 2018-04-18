package com.example.jecihjoy.medmanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jecihjoy.medmanager.adapters.DatabaseAdapter;
import com.example.jecihjoy.medmanager.adapters.MedsRecyclerAdapter;
import com.example.jecihjoy.medmanager.model.Medicine;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class AllMedsActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    public static long id;
    public DatabaseAdapter databaseAdapter;
    public ArrayList<Medicine> medicines;
    public MedsRecyclerAdapter adapter;
    FloatingActionButton mAddBtn;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAddBtn = (FloatingActionButton) findViewById(R.id.add_new_med);

        //dbAdapter class
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();
        medicines = databaseAdapter.getAllMeds();
        databaseAdapter.close();

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);
        adapter = new MedsRecyclerAdapter(medicines);
        if(!(medicines.size() < 1)){
            rv.setAdapter(adapter);
        }else {
            Toast.makeText(this, "No any medication information, Click the plus button to add your medicines", Toast.LENGTH_SHORT).show();
        }

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

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
                finish();
                databaseAdapter.close();
            }
        }).attachToRecyclerView(rv);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddNewMedication.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null){
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            DatabaseAdapter databaseAdapter = new DatabaseAdapter(getApplicationContext());
            databaseAdapter.open();
            databaseAdapter.insertUers(personId, personName,  personFamilyName, personGivenName,personEmail);
            databaseAdapter.close();
        }else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meds_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)  search.getActionView();

        if (search != null) {
            searchView.setOnQueryTextListener(this);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            startActivity(new Intent(getApplicationContext(), AllMedsActivity.class));
            return true;
        }

        if (id == R.id.action_update) {

            Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_clear) {

            databaseAdapter.open();
            databaseAdapter.deleteAllMeds();
            Intent intent = new Intent(getApplicationContext(), AllMedsActivity.class);
            startActivity(intent);
            finish();
            databaseAdapter.close();
        }

        if (id == R.id.action_logout){
            signout();
            return  true;
        }

        if (id == R.id.action_group_by_month){
            Intent intent = new Intent(getApplicationContext(), MonthsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Medicine> newList = new ArrayList<>();
        for(Medicine med : medicines){
            String name = med.getName().toLowerCase();
            if(name.contains(newText)){
                newList.add(med);
            }
        }
        adapter.setFilter(newList);
        return  true;
    }

    //logout
    public void signout(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
    }
}
