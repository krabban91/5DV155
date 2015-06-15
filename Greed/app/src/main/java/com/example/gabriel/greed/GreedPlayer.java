package com.example.gabriel.greed;

/**
 * Created by Gabriel on 6/15/2015.
 */
public class GreedPlayer {
    private String name;
    private int score = 0;
    private int currentRound = 0;
    private Dice[] dices = new Dice[6];

    public GreedPlayer(String name){
        this.name = name;
        for(Dice d : dices){
            d = new Dice();
        }
    }

    public boolean selectDice(int i){
        return dices[i].setSelected(true);
    }

    public Dice[] getDices(){
        return this.dices;
    }

    public void rollDices(){
        for(Dice d : dices){
            if(d.isActive() && !d.isSelected()){
                d.rollDice();
            }
        }
    }

    /**
     * Computes the score of current throw, adds it to currentRound and returns the current throw score.
     * @return the score of the current throw.
     */
    public int collectScore(){
        int[] digits = {0,0,0,0,0,0};
        for (Dice d : dices){
            if(d.isSelected()){
                digits[d.getNumber()-1]++;
                d.setActive(false);
            }
        }
        int throwScore = 0;

        //Check ladder
        boolean ladder = true;
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
            for (int i = 0; i++ < digits.length;) {
                while (digits[i]>0){
                    if (digits[i] > 2){ //triple
                        throwScore += (i==0?10:i+1)*100;
                        digits[i]-=3;
                    }else if (i == 0 || i == 4){
                        throwScore += (i==0?100:50)*digits[i];
                        digits[i]=0;
                    }
                }
            }
        }
        currentRound += throwScore;
        return throwScore;
    }

    public void newRound(boolean resetCurrentScore){
        if(resetCurrentScore){
            this.currentRound = 0;
        }
        for(Dice d : dices){
            d.setActive(true);
            d.setSelected(false);
        }
    }

    public int getCurrentRoundScore(){
        return this.currentRound;
    }


}
