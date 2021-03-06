package org.jeff.game24app.tiles;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.support.annotation.IntDef;
import android.view.View;

import org.jeff.game24app.R;
import org.jeff.game24app.game.BaseGameActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HintManager {

    @IntDef({NUM0, OP, NUM1, INACTIVE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {
    }

    private static final int NUM0 = 0;
    private static final int OP = 1;
    private static final int NUM1 = 2;
    private static final int INACTIVE = 3;
    private @State
    int state;

    private NumberTile numDummy;
    private OperationTile opDummy;
    private NumberTile hintNum0, hintNum1;
    private OperationTile hintOp;
    private View darkView, numGroup, opGroup;
    private Animator blinkAnimator;

    public HintManager(BaseGameActivity a) {
        numDummy = a.findViewById(R.id.num_dummy);
        numDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        opDummy = a.findViewById(R.id.op_dummy);
        opDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        darkView = a.findViewById(R.id.dark_view);
        numGroup = a.findViewById(R.id.num_tile_group);
        opGroup = a.findViewById(R.id.op_tile_group);
        blinkAnimator = AnimatorInflater.loadAnimator(a, R.animator.fade);
        state = INACTIVE;
    }

    public void startHint(NumberTile num0, OperationTile op, NumberTile num1) {
        hintNum0 = num0;
        hintOp = op;
        hintNum1 = num1;
        update();
    }

    private void update() {
        //Order of hint is: num0, op, num1
        switch (state) {
            case INACTIVE:
                state = NUM0;
                darkView.setVisibility(View.VISIBLE);
                numDummy.setVisibility(View.VISIBLE);
                numDummy.setX(hintNum0.getLeft() + numGroup.getLeft());
                numDummy.setY(hintNum0.getTop() + numGroup.getTop());
                numDummy.getLayoutParams().width = hintNum0.getWidth();
                numDummy.getLayoutParams().height = hintNum0.getHeight();
                numDummy.setValue(hintNum0.getValue());
                numDummy.requestLayout();
                blinkAnimator.setTarget(numDummy);
                blinkAnimator.start();
                break;
            case NUM0:
                hintNum0.performClick();
                state = OP;
                numDummy.setVisibility(View.GONE);
                opDummy.setVisibility(View.VISIBLE);
                opDummy.setX(hintOp.getLeft() + opGroup.getLeft());
                opDummy.setY(hintOp.getTop() + opGroup.getTop());
                opDummy.getLayoutParams().width = hintOp.getWidth();
                opDummy.getLayoutParams().height = hintOp.getHeight();
                opDummy.setOp(hintOp.getOp());
                opDummy.requestLayout();
                blinkAnimator.end();
                blinkAnimator.setTarget(opDummy);
                blinkAnimator.start();
                break;
            case OP:
                hintOp.performClick();
                state = NUM1;
                opDummy.setVisibility(View.GONE);
                numDummy.setVisibility(View.VISIBLE);
                numDummy.setX(hintNum1.getLeft() + numGroup.getLeft());
                numDummy.setY(hintNum1.getTop() + numGroup.getTop());
                numDummy.getLayoutParams().width = hintNum1.getWidth();
                numDummy.getLayoutParams().height = hintNum1.getHeight();
                numDummy.setValue(hintNum1.getValue());
                blinkAnimator.end();
                blinkAnimator.setTarget(numDummy);
                blinkAnimator.start();
                break;
            case NUM1:
                hintNum1.performClick();
                reset();
                break;
        }
    }

    public void reset() {
        state = INACTIVE;
        hintNum0 = null;
        hintNum1 = null;
        hintOp = null;
        numDummy.setVisibility(View.GONE);
        opDummy.setVisibility(View.GONE);
        darkView.setVisibility(View.GONE);
        blinkAnimator.end();
    }
}
