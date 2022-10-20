package whatsappclone.proyecto_javier_juan_uceda.tinderclone.Chat;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ChatViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    }
}
