package com.example.gabriel.greed;

/**
 * Created by Gabriel on 6/15/2015.
 */
public class GreedPlayer {
    private final String name;
    private int score = 0;
    private int currentRound = 0;
    private Dice[] dices = new Dice[6];

    public GreedPlayer(String name){
        this.name = name;
        for(int i = 0; i <6; i++){
            dices[i] = new Dice();
        }
    }

    public String getName(){
        return this.name;
    }

    public int getTotalScore(){
        return this.score;
    }
    public boolean isDiceSelected(int i){
        return dices[i].isSelected();
    }

    public boolean isDiceActive(int i){
        return dices[i].isActive();
    }

    public boolean selectDice(int i){
        return dices[i].setSelected(true);
    }

    public boolean toggleDiceSelected(int i) {
        return dices[i].toggleSelected();
    }

    public int getImgNoForDice(int i){
        return dices[i].getImageNo();
    }

    public Dice[] getDices(){
        return this.dices;
    }

    public void rollDices(){
        for(Dice d : dices){
            if(d.isActive()){
                d.rollDice();
            }
        }
    }

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
                digits[d.getNumber()-1]++;
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
            // U stupid?
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

    public void startRound(){
        currentRound = 0;
        turnDices(true);
    }
    public void endRound(){
        if(currentRound >= 300){
            score +=currentRound;
        }
        turnDices(false);
    }


    public void turnDices(boolean on){
        for(Dice d : dices){
            d.setActive(on);
            d.setSelected(!on);
        }
    }


    public int getCurrentRoundScore(){
        return this.currentRound;
    }


}
