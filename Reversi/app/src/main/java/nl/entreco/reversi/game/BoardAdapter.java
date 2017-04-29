package nl.entreco.reversi.game;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nl.entreco.reversi.R;
import nl.entreco.reversi.ReversiActivity;
import nl.entreco.reversi.databinding.ItemStoneBinding;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Referee;
import nl.entreco.reversi.model.Stone;

public class BoardAdapter extends RecyclerView.Adapter<StoneHolder>
        implements ReversiActivity.StoneClickListener {

    @NonNull private final Referee referee;

    public BoardAdapter(@NonNull final Referee referee) {
        this.referee = referee;
    }

    @Override
    public StoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemStoneBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_stone, parent,
                        false);
        return new StoneHolder(binding);
    }

    @Override
    public void onBindViewHolder(StoneHolder holder, int position) {
        holder.setStone(referee.getBoard().get(position));
        holder.setOnClickListener(this);
        holder.getItemView().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return referee.getBoard().size();
    }

    @Override
    public void onStoneClicked(@NonNull Stone stone) {
        if (stone.color() == Stone.EMPTY && referee.getPlayers().size() == 2) {

            final Player player = referee.getCurrentPlayer();
            final List<Stone>
                    updated = referee.onMoveReceived(player,
                    String.format("[%s,%s]", stone.row(), stone.col()));


            notifyItemChanged(referee.getBoard().getItemPosition(stone.row(), stone.col()));
            for (final Stone turned : updated) {
                notifyItemChanged(referee.getBoard().getItemPosition(turned.row(), turned.col()));
            }
        }
    }
}
