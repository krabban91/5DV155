package com.example.gabriel.greed;

/**
 * Created by Gabriel on 6/15/2015.
 */
public class Dice {
    private int sideUp;
    private boolean active;
    private boolean selected;

    public Dice(){
        rollDice();
    }

    public boolean isSelected(){return selected;}

    public boolean isActive(){ return active;}

    public boolean setSelected(boolean selected){
        if(active==false){
            return false;
        }
        this.selected = selected;
        return true;
    }

    public boolean toggleSelected(){
        return setSelected(!isSelected());
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public int getNumber(){
        return sideUp;
    }

    public int rollDice(){
        sideUp = (int)(Math.random()*6 + 1); //rolling dice (1-6 as outcome)
        return getNumber();
    }
}
