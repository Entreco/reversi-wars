package nl.entreco.reversi.model.players;

import android.graphics.Point;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.entreco.reversi.model.Move;

public class SpiralPlayer extends BasePlayer {

    private final List<Point> points;
    private final List<Point> alreadyTried = new ArrayList<>();

    public SpiralPlayer() {
        points = new Spiral(8, 8).spiral();
        Collections.reverse(points);
    }

    @Override
    public void yourTurn(@NonNull String board) {
        super.yourTurn(board);
        points.addAll(0, alreadyTried);
        alreadyTried.clear();
        next();
    }

    private void next() {
        final Point point = points.remove(0);
        final Move move = new Move(point.y + 4, point.x + 4);
        alreadyTried.add(point);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                submitMove(move);
            }
        }, 10);
    }

    @Override
    public void onMoveRejected(@NonNull String board) {
        super.onMoveRejected(board);
        next();
    }

    @NonNull
    @Override
    public String getName() {
        return "Spiral";
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    private static class Spiral{
        private enum Direction {
            E(0,1) {Direction next(){return S;}},
            S(1,0) {Direction next(){return W;}},
            W(0,-1) {Direction next(){return N;}},
            N(-1,0) {Direction next(){return E;}};
            private int dx;
            private int dy;

            Point advance(Point point){
                return new Point(point.x + dx, point.y + dy);
            }
            abstract Direction next();
            Direction(int dx, int dy){
                this.dx = dx;
                this.dy = dy;
            }
        };
        private static final Point ORIGIN = new Point(0, 0);
        private final int width;
        private final int height;
        private Point point;
        private Direction direction = Direction.E;
        private List<Point> list = new ArrayList<>();
        Spiral(int width, int height){

            this.width = width;
            this.height = height;
        }

        List<Point> spiral(){
            point = ORIGIN;
            int steps = 1;
            while(list.size() < width * height){
                advance(steps);
                advance(steps);
                steps++;
            }
            return list;
        }
        private void advance(int n){
            for(int i=0 ; i < n ; ++i){
                if(inBounds(point)){
                    list.add(point);
                }
                point = direction.advance(point);
            }
            direction = direction.next();
        }
        private boolean inBounds(Point p){
            return between(-width / 2, width / 2, p.x) && between(-height / 2, height / 2, p.y);
        }

        private boolean between(int low, int high, int n) {
            return low <= n && n < high;
        }
    }
}
