package com.example.finalprojectandroid.ui.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroid.R;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private Context context;
    private ArrayList<Match> matchList;

    public MatchAdapter(Context context, ArrayList<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.txtGameName.setText(match.getGameName());
        holder.txtMatchDate.setText(match.getMatchDate());
        holder.txtScore.setText("Pontuação: " + match.getScore());
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView txtGameName, txtMatchDate, txtScore;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGameName = itemView.findViewById(R.id.txtGameName);
            txtMatchDate = itemView.findViewById(R.id.txtMatchDate);
            txtScore = itemView.findViewById(R.id.txtScore);
        }
    }
}