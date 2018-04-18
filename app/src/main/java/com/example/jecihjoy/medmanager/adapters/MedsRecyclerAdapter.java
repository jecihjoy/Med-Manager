package com.example.jecihjoy.medmanager.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jecihjoy.medmanager.R;
import com.example.jecihjoy.medmanager.model.Medicine;

import java.util.ArrayList;

/**
 * Created by Jecihjoy on 4/10/2018.
 */

public class MedsRecyclerAdapter  extends  RecyclerView.Adapter<MedsRecyclerAdapter.MedAdapterViewHolder> {

        ArrayList<Medicine> myMeds;

        public MedsRecyclerAdapter(ArrayList<Medicine> medData){
            myMeds = medData;
        }

        public class MedAdapterViewHolder extends RecyclerView.ViewHolder{

            public CardView mCardView;
            public TextView mTxtName;
            public TextView mTxtFrequency;
            public TextView mTxtDate;
            public TextView mTxtDays;
            public TextView mTxtDesc;
            public MedAdapterViewHolder( View viewItem){
                super(viewItem);

                mCardView = (CardView) viewItem.findViewById(R.id.card_view);
                mTxtName = (TextView) viewItem.findViewById(R.id.txt_name);
                mTxtFrequency = (TextView) viewItem.findViewById(R.id.txt_frequency);
                mTxtDate = (TextView) viewItem.findViewById(R.id.txt_date);
                mTxtDays = (TextView) viewItem.findViewById(R.id.txt_days);
                mTxtDesc = (TextView) viewItem.findViewById(R.id.txt_desc);
            }
        }

        @Override
        public MedAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            int layout = R.layout.med_card;
            LayoutInflater infater = LayoutInflater.from(context);
            boolean attachImmeaditely = false;

            View view = infater.inflate(layout, parent,attachImmeaditely);
            MedAdapterViewHolder myViewHolder = new MedAdapterViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MedAdapterViewHolder holder, final int position) {
            Medicine md  = myMeds.get(position);
            holder.mTxtName.setText(md.getName().toUpperCase());
            holder.mTxtDays.setText("End Date:  "+ md.getEndDate());
            holder.mTxtDate.setText("Start Date:  "+md.getStartDate());
            holder.mTxtDesc.setText( "Description: "+md.getDuration());
            holder.mTxtFrequency.setText("Frequency: " +md.getFrequency().charAt(0) + " Tablets  " +md.getFrequency().charAt(2)+ "  times per day");
            holder.itemView.setTag(md.getMedId());
        }

        @Override
        public int getItemCount() {

            return myMeds.size();
        }

        public void removeItem(int position) {
            myMeds.remove(position);
            // notify the item removed by position
            // to perform recycler view delete animations
            // NOTE: don't call notifyDataSetChanged()
            notifyItemRemoved(position);
        }

//        public void restoreItem(Item item, int position) {
//            myMeds.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
        public void setFilter(ArrayList<Medicine> newList){
            myMeds = new ArrayList<>();
            myMeds.addAll(newList);
            notifyDataSetChanged();
        }
    }

