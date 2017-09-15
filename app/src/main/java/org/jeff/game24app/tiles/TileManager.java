package org.jeff.game24app.tiles;

import android.view.View;

import org.jeff.game24app.MainActivity;
import org.jeff.game24app.solver.Operation;
import org.jeff.game24app.solver.Rational;

public class TileManager {

    /** The number of NumberTiles selected **/
    private int numsSelectedLen;
    private NumberTile[] numsSelected;
    private int numExists;
    private OperationTile opSelected;
    private View.OnClickListener numListener;
    private View.OnClickListener opListener;
    private MainActivity activity;

    public TileManager(MainActivity a) {
        numsSelectedLen = 0;
        numsSelected = new NumberTile[2];
        numExists = 4;
        opSelected = null;
        setupListeners();
        activity = a;
    }

    private void setupListeners() {
        numListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberTile numTile = (NumberTile)v;
                if (!numTile.exists()) return;
                int selected = numsSelectedLen;
                if (numTile.isSelected) {
                    selected--;
                } else {
                    selected++;
                }
                if (selected <= 2) {
                    numTile.toggle();
                    if (selected < numsSelectedLen) {
                        for (int i = 0; i < numsSelectedLen; i++) {
                            if (numsSelected[i] == numTile) {
                                numsSelected[i] = null;
                                System.arraycopy(numsSelected, i + 1,
                                        numsSelected, i, numsSelected.length - i - 1);
                            }
                        }
                    } else if (selected > numsSelectedLen) {
                        numsSelected[numsSelectedLen] = numTile;
                    }
                    numsSelectedLen = selected;

                    if (readyForOperation()) {
                        completeOperation();
                    }
                }
            }
        };

        opListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperationTile opTile = (OperationTile)v;
                opTile.toggle();
                if (opTile.isSelected) {
                    if (opSelected != null) {
                        opSelected.toggle();
                    }
                    opSelected = opTile;

                    if (readyForOperation()) {
                        completeOperation();
                    }
                } else {
                    opSelected = null;
                }
            }
        };
    }

    public View.OnClickListener getNumListener() {
        return numListener;
    }

    public View.OnClickListener getOpListener() {
        return opListener;
    }

    private boolean readyForOperation() {
        return numsSelectedLen == 2 && opSelected != null;
    }

    private void completeOperation() {
        NumberTile numTile0 = numsSelected[0];
        NumberTile numTile1 = numsSelected[1];

        //delete the first tile and update the second
        Rational num0 = numTile0.getValue();
        Rational num1 = numTile1.getValue();
        Operation.ArithmeticOp op = opSelected.getOp();
        boolean complete = false;
        //don't divide by 0
        if (!(op == Operation.ArithmeticOp.DIVIDE && num1.getNumerator() == 0)) {
            Operation operation = new Operation(num0, num1, op);
            Rational result = operation.evaluate();
            numTile1.setValue(result);
            numTile0.setExists(false);
            numExists--;
            if (result.equals(Rational.CONST_24) && numExists == 1) {
                complete = true;
            }
        }

        //reset selections
        numTile0.toggle();
        numTile1.toggle();
        opSelected.toggle();
        numsSelected[0] = null;
        numsSelected[1] = null;
        numsSelectedLen = 0;
        opSelected = null;
        if (complete) {
            activity.newPuzzle();
        }
    }

    public void reset() {
        numsSelectedLen = 0;
        numsSelected[0] = null;
        numsSelected[1] = null;
        numExists = 4;
        opSelected = null;
    }

}