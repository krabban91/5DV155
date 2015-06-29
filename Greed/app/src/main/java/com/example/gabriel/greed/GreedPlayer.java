package com.example.gabriel.greed;

/**
 * Created by Gabriel on 6/15/2015.
 * Used to represent a contenstant in Greed
 */
public class GreedPlayer {
    //instance variables
    private final String name;
    private int score = 0;
    private int currentRound = 0;
    private Dice[] dices = new Dice[6];
    //--------------

    /**
     * Constructor. Creates an instance.
     * @param name The name of the contestant.
     */
    public GreedPlayer(String name){
        this.name = name;
        for(int i = 0; i <6; i++){
            dices[i] = new Dice();
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
        return dices[index].toggleSelected();
    }

    /**
     * Gives the resid for the image that can represent the state of the dice.
     * @param index index of the dice.
     * @return The resid to use to find a drawable
     */
    public int getImgNoForDice(int index){
        return dices[index].getImageNo();
    }

    /**
     * Performs the action rollDice for all active dices.
     */
    public void rollDices(){
        for(Dice d : dices){
            if(d.isActive()){
                d.rollDice();
            }
        }
    }

    /**
     * Gives a boolean value if there are dices left.
     * @param thatAreNotSelected <tt>true</tt> if there is to be an deeper check of nonselected dice.
     * @return <tt>true</tt> if there are any dices left.
     */
    public boolean hasDicesLeft(boolean thatAreNotSelected){
        for (Dice d : dices){
            if (d.isActive() && (thatAreNotSelected ?
                                    !d.isSelected() : true)){
                return true;
            }
        }
        return false;
    }

    /**
     * Computes the score of current throw, adds it to currentRound and returns the current throw score.
     * @return the score of the current throw.
     */
    public int collectScore(){
        //calculate score
        int[] digits = {0,0,0,0,0,0};
        for (Dice d : dices){
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
                            digits[i]=0;
                        }else{
                            //Shouldn't be allowed.
                            //selecting something that doesn't award any points.
                            invalidChoice = true;
                            digits[i]=0;
                            //return -1;
                        }

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
            // Having a score but still selecting more dices.
            // u stupid?
            return -1;
        }

        //remove dices
        for (Dice d : dices){
            if(d.isSelected()){
                d.setSelected(false);
                d.setActive(false);
            }
        }
        currentRound += throwScore;
        return throwScore;
    }

    /**
     * Starts a new round for the player. resets the dices and current round score.
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
     * @param on <tt>true</tt> if dices are to be active.
     */
    public void turnDices(boolean on){
        for(Dice d : dices){
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
}
