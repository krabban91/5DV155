package se.umu.gaan0015.greed;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gabriel on 6/15/2015.
 * Used to represent a contestant in Greed
 */
public class GreedPlayerModel implements Parcelable{
    //instance variables
    private final String name;
    private int score = 0;
    private int currentRound = 0;
    private DieModel[] dice = new DieModel[6];
    //--------------

    /**
     * Constructor. Creates an instance.
     * @param name The name of the contestant.
     */
    public GreedPlayerModel(String name){
        this.name = name;
        for(int i = 0; i <6; i++){
            dice[i] = new DieModel();
        }
    }

    /**
     * Returns the name of the player
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the total score of the player.
     */
    public int getScore(){
        return this.score;
    }

    /**
     * Selects/deselects the dice with the given index.
     * @param index index of the dice.
     * @return <tt>true</tt> if action was performable.
     */
    public boolean toggleDiceSelected(int index) {
        return dice[index].toggleSelected();
    }

    /**
     * Gives the die with given index.
     * @param index index of the die.
     * @return The die
     */
    public DieModel getDie(int index){
        return dice[index];
    }

    /**
     * Performs the action rollDice for all active dice.
     */
    public void rollDices(){
        for(DieModel d : dice){
            if(d.isActive()){
                d.rollDice();
            }
        }
    }

    /**
     * Gives a boolean value if there are dice left.
     * @param thatAreNotSelected <tt>true</tt> if there is to be an deeper check of nonselected dice.
     * @return <tt>true</tt> if there are any dice left.
     */
    public boolean hasDicesLeft(boolean thatAreNotSelected){
        for (DieModel d : dice){
            if (d.isActive() && (thatAreNotSelected ?
                                    !d.isSelected() : true)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the complete set of dice what the possible score can be.
     * @return The highest achieveable score with active dice.
     */
    public int possibleScore(){
        int[] digits = {0,0,0,0,0,0};
        for (DieModel d : dice){
            if(d.isActive()){
                digits[d.getValue()-1]++;
            }
        }
        int score = 0;

        //Check ladder
        boolean ladder = true;
        for (int i = 0; i <6 ; i++) {
            if(digits[i]!=1){
                ladder = false;
                break;
            }
        }
        if(ladder){
            score = 1000;
        }else{
            //Sum up all other kinds of scores
            for (int i = 0; i < digits.length;i++) {
                while (digits[i]>0){
                    if (digits[i] > 2){ //triple
                        score += (i==0?10:i+1)*100;
                        digits[i]-=3;
                    }
                    else { //single
                        if (i == 0 || i == 4){
                            score += (i==0?100:50)*digits[i];
                        }
                        digits[i]=0;
                    }
                }
            }
        }
        return score;
    }

    /**
     * Computes the score of current throw, adds it to currentRound and returns the current throw score.
     * @return the score of the current throw.
     */
    public int collectScore(boolean firstRound){
        //calculate score
        int[] digits = {0,0,0,0,0,0};
        for (DieModel d : dice){
            if(d.isSelected()){
                digits[d.getValue()-1]++;
            }
        }
        int throwScore = 0;

        //Check ladder
        boolean ladder = true, invalidChoice = false;
        for (int i = 0; i <6 ; i++) {
            if(digits[i]!=1){
                ladder = false;
                break;
            }
        }
        if(ladder){
            throwScore = 1000;
        }else{
            //Sum up all other kinds of scores
            for (int i = 0; i < digits.length;i++) {
                while (digits[i]>0){
                    if (digits[i] > 2){ //triple
                        throwScore += (i==0?10:i+1)*100;
                        digits[i]-=3;
                    }
                    else { //single
                        if (i == 0 || i == 4){
                            throwScore += (i==0?100:50)*digits[i];
                        }else{
                            //Shouldn't be allowed.
                            //selecting something that doesn't award any points.
                            invalidChoice = true;
                        }
                        digits[i]=0;
                    }
                }
            }
        }
        if(throwScore == 0){
            //not a valid throw or no points
            if(hasDicesLeft(true)){
                //re-evaluate the selection of dice.
                return -1;
            }else{
                //game over
                return 0;
            }
        }
        if(invalidChoice){
            // Having a score but still selecting more dice.
            // u stupid?
            return -1;
        }
        if(firstRound && throwScore < 300){
            //Score is not above 300.
            return -1;
        }

        //remove dice
        for (DieModel d : dice){
            if(d.isSelected()){
                d.setSelected(false);
                d.setActive(false);
            }
        }
        currentRound += throwScore;
        return throwScore;
    }

    /**
     * Starts a new round for the player. resets the dice and current round score.
     */
    public void startRound(){
        currentRound = 0;
        turnDices(true);
    }

    /**
     * Ends the round for the player.
     * @param collect <tt>true</tt> if the player gets to collect the points of the round.
     */
    public void endRound(boolean collect){
        if(collect){
            score +=currentRound;
        }
        turnDices(false);
    }

    /**
     * Sets the dice to active or inactive depending to param {on}
     * @param on <tt>true</tt> if dice are to be active.
     */
    public void turnDices(boolean on){
        for(DieModel d : dice){
            d.setActive(on);
            d.setSelected(!on);
        }
    }

    /**
     * Returns the score from current round.
     * @return an int from 0 and above.
     */
    public int getCurrentRoundScore(){
        return this.currentRound;
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
        dest.writeString(this.name);
        dest.writeInt(this.score);
        dest.writeInt(this.currentRound);
        dest.writeParcelableArray(this.dice, 0);
    }

    public static final Parcelable.Creator<GreedPlayerModel> CREATOR
            = new Parcelable.Creator<GreedPlayerModel>() {
        public GreedPlayerModel createFromParcel(Parcel in) {
            return new GreedPlayerModel(in);
        }

        public GreedPlayerModel[] newArray(int size) {
            return new GreedPlayerModel[size];
        }
    };

    /** recreate object from parcel */
    private GreedPlayerModel(Parcel in) {
        this.name = in.readString();
        this.score = in.readInt();
        this.currentRound = in.readInt();
        this.dice = (DieModel[])in.readParcelableArray(DieModel.class.getClassLoader());

    }

}
