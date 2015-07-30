package se.umu.gaan0015.greed;


/**
 * Created by Gabriel on 2015-07-29.
 */
public class DiceImageHandler {

    /**
     * Gives the resid for the image that can represent the state of the dice.
     * @return The resid to use to find a drawable
     */
    public static int getImageNoForDie(DieModel d){
        return d.isActive() ? (d.isSelected()?
                imgSeleceted[d.getValue()-1] :
                imgActive[d.getValue()-1]) :
                imgInactive[d.getValue()-1];
    }

    //Static variables for internal use. Good to have.
    private static final int[] imgActive = {
            R.drawable.white1,
            R.drawable.white2,
            R.drawable.white3,
            R.drawable.white4,
            R.drawable.white5,
            R.drawable.white6
    };
    private static final int[] imgInactive = {
            R.drawable.grey1,
            R.drawable.grey2,
            R.drawable.grey3,
            R.drawable.grey4,
            R.drawable.grey5,
            R.drawable.grey6
    };
    private static final int[] imgSeleceted = {
            R.drawable.red1,
            R.drawable.red2,
            R.drawable.red3,
            R.drawable.red4,
            R.drawable.red5,
            R.drawable.red6
    };
}
