package com.example.gabriel.greed;

/**
 * Created by Gabriel on 6/15/2015.
 * Describes a dice in the game Greed
 */
public class Dice {

    private int sideUp;
    private boolean active = true;
    private boolean selected = false;

    /**
     * Creates an instance of a dice. Also rolls it once.
     */
    public Dice(){
        rollDice();
    }

    /**
     * Gives a boolean. Is this Dice selected (yes, no?)
     * @return <tt>true</tt> if selected by player
     */
    public boolean isSelected(){return selected;}

    /**
     * Sets the dice selection to the given value
     * @param selected Boolean, whether the dice is going to be selected.
     * @return <tt>true</tt> if it was an viable action to select/deselect dice.
     */
    public boolean setSelected(boolean selected){
        if(active==false){
            return false;
        }
        this.selected = selected;
        return true;
    }


    /**
     * Toggles the value of <tt>isSelected()</tt>
     * @return Value of setSelected.
     */
    public boolean toggleSelected(){
        return setSelected(!isSelected());
    }

    /**
     * Gives a boolean. Is this Dice active (yes, no?)
     * @return <tt>true</tt> if dice still is in game.
     */
    public boolean isActive(){ return active;}

    /**
     * Setting the dice to the given value.
     * @param active the value to set activity to.
     */
    public void setActive(boolean active){
        this.active = active;
    }

    /**
     * Gives the value of the dice.
     * @return An integer between 1-6.
     */
    public int getValue(){
        return sideUp;
    }

    /**
     * Sets the side up to a number 1-6 pseudo-randomly.
     * @return the value of the roll.
     */
    public int rollDice(){
        sideUp = (int)(Math.random()*6 + 1); //rolling dice (1-6 as outcome)
        return getValue();
    }

    /**
     * Gives the resid for the image that can represent the state of the dice.
     * @return The resid to use to find a drawable
     */
    public int getImageNo(){
        return isActive() ? (isSelected()?
                Dice.imgSeleceted[getValue()-1] :
                    Dice.imgActive[getValue()-1]) :
                Dice.imgInactive[this.getValue()-1];
    }

    //--------------------------------------------------

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
