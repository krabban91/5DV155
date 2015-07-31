package se.umu.gaan0015.greed;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gabriel on 6/15/2015.
 * Describes a dice in the game GreedActivity
 */
public class DieModel implements Parcelable {

    private int sideUp;
    private boolean active = true;
    private boolean selected = false;

    /**
     * Creates an instance of a dice. Also rolls it once.
     */
    public DieModel(){
        rollDice();
    }

    /**
     * Gives a boolean. Is this DieModel selected (yes, no?)
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
     * Gives a boolean. Is this DieModel active (yes, no?)
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


    /*
    -------------------------------------------
    Following is made for saving session between rotations and reactivations.
    */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sideUp);
        dest.writeInt(this.active ? 1 : 0);
        dest.writeInt(this.selected ? 1 : 0);
    }

    public static final Parcelable.Creator<DieModel> CREATOR
            = new Parcelable.Creator<DieModel>() {
        public DieModel createFromParcel(Parcel in) {
            return new DieModel(in);
        }

        public DieModel[] newArray(int size) {
            return new DieModel[size];
        }
    };

    /** recreate object from parcel */
    private DieModel(Parcel in) {
        this.sideUp = in.readInt();
        this.active = in.readInt() == 1;
        this.selected = in.readInt() == 1;
    }


    //--------------------------------------------------


}
