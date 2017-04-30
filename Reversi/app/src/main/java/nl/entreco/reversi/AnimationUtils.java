package nl.entreco.reversi;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import nl.entreco.reversi.model.Player;
import nl.entreco.reversi.model.Stone;

public class AnimationUtils {
    public static void reject(@NonNull View view, @Stone.Color int stone,
                               @NonNull Player rejected) {
        if (stone == rejected.getStoneColor()) {

            float rot = 5f;
            float startRotation = view.getRotation();
            Animator anim1 = ObjectAnimator.ofFloat(view, "rotation", startRotation, rot);
            Animator anim2 = ObjectAnimator.ofFloat(view, "rotation", rot, -rot);
            Animator anim3 = ObjectAnimator.ofFloat(view, "rotation", -rot, rot);
            Animator anim4 = ObjectAnimator.ofFloat(view, "rotation", rot, -rot);
            Animator anim5 = ObjectAnimator.ofFloat(view, "rotation", -rot, startRotation);

            AnimatorSet set1 = new AnimatorSet();
            set1.playSequentially(anim1, anim2, anim3, anim4, anim5);
            set1.setDuration(50);
            set1.start();

            float trans = 5f;
            float start = view.getTranslationX();
            Animator anim6 = ObjectAnimator.ofFloat(view, "translationX", start, trans);
            Animator anim7 = ObjectAnimator.ofFloat(view, "translationX", trans, -trans);
            Animator anim8 = ObjectAnimator.ofFloat(view, "translationX", -trans, start);

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
        if (stone == player.getStoneColor()) {
            view.animate().scaleX(1F).scaleY(1F).alpha(1F).start();
        } else {
            view.animate().scaleX(.6F).scaleY(.6F).alpha(.5F).start();
        }
    }
}
