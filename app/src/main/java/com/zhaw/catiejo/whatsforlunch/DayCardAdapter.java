package com.zhaw.catiejo.whatsforlunch;

import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DayCardAdapter extends RecyclerView.Adapter<DayCardAdapter.DayCardViewHolder> {
    private List<DayCard> mData;
    public static class DayCardViewHolder extends RecyclerView.ViewHolder {
        protected TextView dayOfWeek;
        protected TextView dayOfYear;
        protected ImageView checkMark;

        public DayCardViewHolder(CardView dc) {

            super(dc);
            dayOfWeek = (TextView) dc.findViewById(R.id.dayOfWeek);
            dayOfYear = (TextView) dc.findViewById(R.id.dayOfYear);
            checkMark = (ImageView) dc.findViewById(R.id.checkMark);
        }
    }

    public DayCardAdapter(List<DayCard> cards) {
        mData = cards;
    }

    @Override
    public DayCardAdapter.DayCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_day, parent, false);
        DayCardViewHolder vh = new DayCardViewHolder(c);
        return vh;
    }

    @Override
    public void onBindViewHolder(DayCardAdapter.DayCardViewHolder holder, int position) {
        DayCard dc = mData.get(position);
        holder.dayOfWeek.setText(dc.getDayOfWeek());
        holder.dayOfYear.setText(dc.getDayOfYear());
        if (dc.getIsInPast()) {
            holder.dayOfWeek.setPaintFlags(holder.dayOfWeek.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.dayOfWeek.setTextColor(Color.parseColor("#4A4A4A"));
        }
        if (dc.getIsSelected()) {
            holder.checkMark.setImageResource(R.drawable.ic_check_blue_24dp);
            holder.dayOfWeek.setTextColor(Color.parseColor("#4A90E2"));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}