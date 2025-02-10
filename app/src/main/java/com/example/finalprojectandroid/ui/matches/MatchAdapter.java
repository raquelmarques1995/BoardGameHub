package com.example.finalprojectandroid.ui.matches;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectandroid.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private Context context;
    private ArrayList<Match> matchList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Match match);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MatchAdapter(Context context, ArrayList<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView txtMatchName, txtMatchDate, txtMatchScore;

        public MatchViewHolder(View itemView) {
            super(itemView);
            txtMatchName = itemView.findViewById(R.id.txtGameName);
            txtMatchDate = itemView.findViewById(R.id.txtMatchDate);
            txtMatchScore = itemView.findViewById(R.id.txtScore);

            // Set the click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(matchList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Match match) {
            txtMatchName.setText(match.getGameName());

            //Check the date value
            Log.d("RecyclerView", "Match Date: " + match.getMatchDate());

            // Format the date before displaying
            if (match.getMatchDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                txtMatchDate.setText(dateFormat.format(match.getMatchDate()));
            } else {
                txtMatchDate.setText("Data desconhecida");
            }


            // Display the Score
            if (match.getScore() == -1000) {
                txtMatchScore.setText("Pontuação: Sem pontuação");
            } else {
                txtMatchScore.setText("Pontuação: " + match.getScore());
            }

        }
    }

}
