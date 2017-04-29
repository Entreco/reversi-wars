package nl.entreco.reversi.game;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import nl.entreco.reversi.ReversiActivity;
import nl.entreco.reversi.databinding.ItemStoneBinding;
import nl.entreco.reversi.model.Stone;

class StoneHolder extends RecyclerView.ViewHolder{

    private final ItemStoneBinding itemView;
    private ReversiActivity.StoneClickListener stoneClickListener;

    StoneHolder(final ItemStoneBinding itemView) {
        super(itemView.getRoot());
        this.itemView = itemView;
        itemView.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stoneClickListener != null){
                    stoneClickListener.onStoneClicked(itemView.getStone());
                }
            }
        });
    }

    ItemStoneBinding getItemView() {
        return itemView;
    }

    void setStone(Stone stone) {
        itemView.setStone(stone);
    }

    void setOnClickListener(ReversiActivity.StoneClickListener stoneClickListener){
        this.stoneClickListener = stoneClickListener;
    }
}
