package com.emel.expandingfab;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    FloatingActionButton fabCancel;
    FloatingActionButton fabApply;
    CoordinatorLayout cl;

    private boolean expanded = false;
    private float offset1;
    private float offset2;
    private boolean cancel_action = false;
    private boolean apply_action = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cl = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        cl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                cl.getViewTreeObserver().removeOnPreDrawListener(this);
                offset1 = fab.getHeight() * 0.8f;
                offset2 = fab.getHeight() * 1.4f;
                return true;
            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });

        fabCancel = (FloatingActionButton) findViewById(R.id.fab_cancel);
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_action = true;
                toggleFab();
            }
        });

        fabApply = (FloatingActionButton) findViewById(R.id.fab_apply);
        fabApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply_action = true;
                toggleFab();
            }
        });

    }


    private void collapseFab() {
        fab.setImageResource(R.drawable.ic_more_vert_white_24dp);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(fabCancel, offset1),
                createCollapseAnimator(fabApply, offset2), rotateAnticlockwiseAnimator(fab, 90));
        if(cancel_action || apply_action){
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (cancel_action) {
                        cancel_action = false;
                        Snackbar.make(cl, "Cancel...", Snackbar.LENGTH_LONG).show();
                    } else if (apply_action) {
                        apply_action = false;
                        Snackbar.make(cl, "Applied !", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        animatorSet.start();
        animateFab();
    }

    private void expandFab() {
        fab.setImageResource(R.drawable.ic_chevron_right_white_24dp);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(fabCancel, offset1),
                createExpandAnimator(fabApply, offset2), rotateClockwiseAnimator(fab, 90));
        animatorSet.start();
        animateFab();
    }

    private static final String TRANSLATION_Y = "translationY";
    private static final String ROTATION = "rotation";

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, -offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, -offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator rotateClockwiseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, ROTATION, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator rotateAnticlockwiseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, ROTATION, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void animateFab() {
        Drawable drawable = fab.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private void toggleFab(){
        expanded = !expanded;
        if (expanded) {
            expandFab();
        } else {
            collapseFab();
        }
    }


}
