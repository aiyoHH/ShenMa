package cn.javava.shenma.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import cn.javava.shenma.app.App;


public class UIUtils {


	/**
	 * 获得handler
	 * 
	 * @return
	 */
	public static Handler getMainHandler()
	{
		return App.getInstance().getHandler();
	}

	/**
	 * 执行延时任务
	 * @param task
	 * @param delayMillis
	 */
	public static void postDelayed(Runnable task, long delayMillis)
	{
		getMainHandler().postDelayed(task, delayMillis);
	}
	/**
	 * 移除队列中任务
	 * @param task
	 */
	public static void removeCallbacks(Runnable task){
		getMainHandler().removeCallbacks(task);
	}

	/**
	 * 在主线程中执行任务
	 * 
	 * @param task
	 */
	public static void post(Runnable task)
	{
		getMainHandler().post(task);
	}



    public static void startAnimtation(final View view){
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1f);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        //animator1.setRepeatCount(0);
        animator1.setDuration(500);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationY", -50f, 0f);
        animator2.setRepeatMode(ValueAnimator.REVERSE);
        //animator.setRepeatCount(0);
        animator2.setDuration(300);

        mAnimatorSet.playTogether(animator1,animator2);

        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                AnimatorSet mAnimatorSet2 = new AnimatorSet();
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
                animator1.setRepeatMode(ValueAnimator.REVERSE);
                //animator1.setRepeatCount(0);
                animator1.setDuration(300);

                ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationY", 0f, 50f);
                animator2.setRepeatMode(ValueAnimator.REVERSE);
                //animator.setRepeatCount(0);
                animator2.setDuration(300);

                mAnimatorSet2.playTogether(animator1,animator2);
                mAnimatorSet2.setStartDelay(2000);
                mAnimatorSet2.start();
            }
        });

        mAnimatorSet.start();
    }

    public static void alterTv(TextView tv){
        Typeface typeface= Typeface.createFromAsset(App.getInstance().getAssets(),"dinpro2.otf");
        tv.setTypeface(typeface);
    }

}
