package whatsappclone.proyecto_javier_juan_uceda.tinderclone.Matches;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import whatsappclone.proyecto_javier_juan_uceda.tinderclone.R;

class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMatchId;
    public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = itemView.findViewById(R.id.Matchid);
    }

    @Override
    public void onClick(View view) {

    }

}
