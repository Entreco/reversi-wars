package nl.entreco.reversi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.entreco.reversi.databinding.ActivityReversiBinding;
import nl.entreco.reversi.databinding.ItemStoneBinding;
import nl.entreco.reversi.model.Board;
import nl.entreco.reversi.model.Move;
import nl.entreco.reversi.model.Referee;
import nl.entreco.reversi.model.Stone;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ReversiActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityReversiBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_reversi);


        final Board board = new Board();
        board.start();
        final Referee ref = new Referee(board);
        final ReversiViewModel viewModel = new ReversiViewModel(ref);
        binding.setViewModel(viewModel);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 8));
        binding.recyclerView.setAdapter(new BoardAdapter(board, Stone.BLACK));
    }

    private class BoardAdapter extends RecyclerView.Adapter<StoneHolder>
            implements StoneClickListener {

        @NonNull private final Board board;
        private int currentColor;

        public BoardAdapter(@NonNull final Board board, @Stone.Value int startColor){
            this.board = board;
            this.currentColor = startColor;
        }

        @Override
        public StoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ItemStoneBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_stone, parent, false);
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
            return 8*8;
        }

        @Override
        public void onStoneClicked(@NonNull Stone stone) {
            if(stone.value() == Stone.EMPTY) {
                int index = stone.getItemPosition();
                stone.set(currentColor == Stone.BLACK ? Stone.BLACK : Stone.WHITE);
                notifyItemChanged(index);

                board.apply(new Move(stone.row(), stone.col()), stone.value());
                notifyDataSetChanged();
            }

            currentColor = -1 * currentColor;
        }
    }

    private static class StoneHolder extends RecyclerView.ViewHolder{

        private final ItemStoneBinding itemView;
        private StoneClickListener stoneClickListener;

        public StoneHolder(final ItemStoneBinding itemView) {
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

        public ItemStoneBinding getItemView() {
            return itemView;
        }

        public void setStone(Stone stone) {
            itemView.setStone(stone);
        }

        public void setOnClickListener(StoneClickListener stoneClickListener){
            this.stoneClickListener = stoneClickListener;
        }
    }

    public interface StoneClickListener{
        void onStoneClicked(@NonNull final Stone stone);
    }
}
