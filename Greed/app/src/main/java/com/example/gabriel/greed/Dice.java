package com.example.gabriel.greed;

/**
 * Created by Gabriel on 6/15/2015.
 */
public class Dice {
    private int sideUp;
    private boolean active = true;
    private boolean selected = false;

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

    public int getImageNo(){
        System.out.println("yoyoyoyoyoyoyoy: "+getNumber());
        if(isActive()){
            return isSelected()?
                    Dice.imgSeleceted[getNumber()-1] :
                    Dice.imgActive[getNumber()-1];
        }
        return Dice.imgInactive[this.getNumber()-1];
    }
}
