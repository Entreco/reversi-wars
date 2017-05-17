package nl.entreco.reversi;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;
import nl.entreco.reversi.ui.PulseDrawable;

public class AnimationUtils {
    public static void reject(@NonNull View view, @Stone.Color int stone,
                              @NonNull Player rejected) {
        if (stone == rejected.getStoneColor()) {

            float rot = 10f;
            float startRotation = view.getRotation();
            Animator anim1 = ObjectAnimator.ofFloat(view, "rotation", startRotation, rot);
            Animator anim2 = ObjectAnimator.ofFloat(view, "rotation", rot, -rot);
            Animator anim3 = ObjectAnimator.ofFloat(view, "rotation", -rot, rot);
            Animator anim4 = ObjectAnimator.ofFloat(view, "rotation", rot, -rot);
            Animator anim5 = ObjectAnimator.ofFloat(view, "rotation", -rot, 0);

            AnimatorSet set1 = new AnimatorSet();
            set1.playSequentially(anim1, anim2, anim3, anim4, anim5);
            set1.setDuration(50);
            set1.start();

            float trans = 16f;
            float start = view.getTranslationX();
            Animator anim6 = ObjectAnimator.ofFloat(view, "translationX", start, trans);
            Animator anim7 = ObjectAnimator.ofFloat(view, "translationX", trans, -trans);
            Animator anim8 = ObjectAnimator.ofFloat(view, "translationX", -trans, 0);

            AnimatorSet set2 = new AnimatorSet();
            set2.playSequentially(anim6, anim7, anim8);
            set2.setDuration(50);
            set2.start();

        } else {
            view.animate().rotation(0F).cancel();
        }
    }

    public static void current(@NonNull View view, @Stone.Color int stone,
                               @NonNull Player player) {
        Log.i("THREAD CURRENT", "AnimationUtils::current: " + Thread.currentThread() + " main:" + (Looper.myLooper() == Looper.getMainLooper()));
        if (stone == player.getStoneColor()) {
            view.animate().scaleX(1F).scaleY(1F).alpha(1F).start();
        } else {
            view.animate().scaleX(.6F).scaleY(.6F).alpha(.5F).start();
        }
    }

    public static void crossFade(@NonNull final TextView view, @StringRes int text) {
        TransitionManager.beginDelayedTransition((ViewGroup) view.getParent());
        if (text > 0) {
            view.setText(text);
        } else {
            view.setText("");
        }
    }

    private static int dOrNull(@DrawableRes int drawable) {
        return drawable > 0 ? drawable : 0;
    }

    public static void pulse(@NonNull final View container, boolean start) {

        final View hide = container.findViewById(start ? R.id.stone_end : R.id.stone_start);
        final View pulse = container.findViewById(start ? R.id.stone_start : R.id.stone_end);

        hide.animate().scaleX(0F).scaleY(0F).alpha(0F).start();
        pulse.animate().scaleX(1F).scaleY(1F).alpha(1F).start();
        int accent = ContextCompat.getColor(container.getContext(), R.color.colorAccent);
        pulse.setBackground(new PulseDrawable(accent));

    }
}
