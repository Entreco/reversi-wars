package nl.entreco.reversi.game;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nl.entreco.reversi.R;
import nl.entreco.reversi.databinding.ItemStoneBinding;
import nl.entreco.reversi.model.Arbiter;
import nl.entreco.reversi.model.Board;
import nl.entreco.reversi.model.GameCallback;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class BoardAdapter extends RecyclerView.Adapter<StoneHolder>
        implements StoneClickListener {

    @NonNull private final Board board;

    @Nullable private GameCallback gameCallback;
    @Nullable private Player currentPlayer;
    private boolean animate;

    public BoardAdapter(@NonNull final Arbiter arbiter) {
        this.animate = true;
        this.board = arbiter.getBoard();
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
        holder.setStone(board.get(position));
        holder.setOnClickListener(this);
        holder.getItemView().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return board.size();
    }

    @Override
    public void onStoneClicked(@NonNull final Stone stone) {
        if (gameCallback != null && currentPlayer != null && stone.color() == Stone.EMPTY) {
            gameCallback.submitMove(currentPlayer, new Move(stone.row(), stone.col()));
        }
    }

    void update(Move move, @Stone.Color int stoneColor) {
        List<Stone> flipped = board.apply(move, stoneColor);
        notifyChanged(board.getItemPosition(move));
        for (final Stone turned : flipped) {
            notifyChanged(board.getItemPosition(turned.row(), turned.col()));
        }
    }

    void setCurrentPlayer(@NonNull final Player player,
                                 @NonNull final GameCallback callback) {
        this.currentPlayer = player;
        if (player.isHuman()) {
            this.gameCallback = callback;
        } else {
            this.gameCallback = null;
        }
    }

    private void notifyChanged(int position) {
        if (shouldAnimate()) {
            notifyItemChanged(position);
        }
    }

    @VisibleForTesting
    boolean shouldAnimate() {
        return animate;
    }

    @VisibleForTesting
    @Nullable
    GameCallback getGameCallback() {
        return gameCallback;
    }

    void start() {
        if(shouldAnimate()){
            notifyDataSetChanged();
        }
    }
}
