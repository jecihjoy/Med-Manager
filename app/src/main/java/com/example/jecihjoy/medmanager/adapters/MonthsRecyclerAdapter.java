package com.example.jecihjoy.medmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jecihjoy.medmanager.MonthlyMedsActivity;
import com.example.jecihjoy.medmanager.R;

/**
 * Created by Jecihjoy on 4/9/2018.
 */

public class MonthsRecyclerAdapter extends RecyclerView.Adapter<MonthsRecyclerAdapter.MyViewHolder> {

    private String[] myMonths;
    private LayoutInflater layoutInflater;

    public MonthsRecyclerAdapter(String[] myMonths, Context mContext) {
        this.myMonths = myMonths;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.monthly_cards,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mMonthtxt.setText(myMonths[position]);

    }

    @Override
    public int getItemCount() {
        return myMonths.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView mMonthtxt;

        public MyViewHolder( View viewItem) {
            super(viewItem);

            mMonthtxt = (TextView) viewItem.findViewById(R.id.txt_months);

            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   int pos = getAdapterPosition();
                    Intent intent = new Intent(view.getContext().getApplicationContext(), MonthlyMedsActivity.class);
                    intent.putExtra("month",myMonths[pos]);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().getApplicationContext().startActivity(intent);
                }
            });
        }
    }
}
