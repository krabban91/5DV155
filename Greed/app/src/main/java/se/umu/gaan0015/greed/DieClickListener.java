package se.umu.gaan0015.greed;

import android.view.View;
import android.widget.Button;

/**
 * Created by Gabriel on 2015-06-24.
 * A Click listener specialized for a dice button.
 */
public class DieClickListener implements View.OnClickListener{
    private GreedModel model;
    private int diceIndex;
    private Button button;

    /**
     * creates a new instance. derp.
     * @param model The model to reflect upon.
     * @param diceIndex The index of the current dice.
     * @param button The xml item to be connected to.
     */
    public DieClickListener(GreedModel model, int diceIndex, Button button){
        this.model = model;
        this.diceIndex = diceIndex;
        this.button = button;
    }

    @Override
    public void onClick(View v) {
        GreedPlayerModel p = model.getCurrentPlayer();
        p.toggleDiceSelected(diceIndex);
        int image = DiceImageHandler.getImageNoForDie(p.getDie(diceIndex));
        button.setBackgroundResource(image);
    }
}
