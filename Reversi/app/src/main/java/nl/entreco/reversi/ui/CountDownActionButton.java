package nl.entreco.reversi.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;

public class CountDownActionButton extends FloatingActionButton {

    private static final float START_ANGLE = -90F;
    private CountDownTimer timer;
    private RectF oval;
    private Paint paint;
    private float elapsed; // [0,360] based on Timer
    private float STROKE_WIDTH = 10F;

    public CountDownActionButton(Context context) {
        this(context, null, 0);
    }
    public CountDownActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CountDownActionButton(Context context, AttributeSet attrs,
                                 int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initTimer();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(186);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    private void initTimer(){
        final long countDown = 4000;
        timer = new CountDownTimer(countDown, 1000 / 60) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsed = ((countDown - millisUntilFinished) * 1.0F/ countDown) * 360.0F;
                postInvalidate();
            }

            @Override
            public void onFinish() {
                cancelCountDown();
            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float radius = Math.min(w, h) / 2;
        oval = new RectF(STROKE_WIDTH, STROKE_WIDTH, 2*radius - STROKE_WIDTH, 2*radius - STROKE_WIDTH);
    }

    public void startCountDown(final long countDown){
        Log.i("TIMER", "startCountDown");
        elapsed = 0;
        timer.start();
        postInvalidate();
    }

    public void cancelCountDown(){
        Log.i("TIMER", "cancelCountDown");
        elapsed = 360F;
        if(timer != null){
            timer.cancel();
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final float sweepAngle = 360F - elapsed;
        Log.i("TIMER", "onDraw start:" + START_ANGLE + " to:" + sweepAngle + " timer:" + timer);
        if(timer != null && elapsed < 360F) {
            canvas.drawArc(oval, START_ANGLE, sweepAngle, false, paint);
        }
    }
}
