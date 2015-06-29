package com.example.gabriel.greed;

import android.content.Context;
import java.util.ArrayList;


/**
 * Class to represent the complete game of Greed.
 */
public class GreedModel {
    /**
     * Created by Gabriel on 2015-06-24.
     */
    public enum GreedEnum{
        INVALIDMOVE,    //scoreAction
        NEWPLAYER,      //scoreAction, saveAction
        CONTINUE,       //scoreAction
        NEWDICE,        //scoreAction
        DICETHROWN,     //throwAction
        GAMEOVER,       //saveAction
    }
    private GreedEnum state;
    private ArrayList<GreedPlayer> players;
    private GreedPlayer currentPlayer;
    private int saveLimit;
    private boolean canSave = false;

    public GreedModel(ArrayList<String> names, int saveLimit) throws IllegalArgumentException{
        if(names == null || names.size() < 2){
            throw new IllegalArgumentException("At least two names are needed to play a game");
        }
        players = new ArrayList<>();
        for(String n : names){
            players.add(new GreedPlayer(n));
        }
        currentPlayer = players.get(0);

        this.saveLimit = (saveLimit < 0) ? 300 : saveLimit;
        state = GreedEnum.NEWPLAYER;
    }

    /**
     * Gives the state that the game currently is in.
     * @return a GreedModel.GreedEnum
     */
    public GreedEnum getState(){
        return this.state;
    }

    public GreedPlayer getCurrentPlayer(){
        return currentPlayer;
    }

    public boolean playerCanSave(){
        return this.canSave;
    }

    /**
     * The action to call when current player wants to throw a dice.
     * @param context a reference to the context to return correct strings.
     * @return The message to give to the user.
     */
    public String throwAction(Context context){
        currentPlayer.rollDices();
        this.state = GreedEnum.DICETHROWN;
        return context.getString(R.string.dice_thrown);
    }

    /**
     * The action to call when current player wants to collect the score.
     * @param context a reference to the context to return correct strings.
     * @return The message to give to the user.
     * */
    public String scoreAction(Context context){
        int val = currentPlayer.collectScore();
        if (val < 0) {
            this.state = GreedEnum.INVALIDMOVE;
            return context.getString(R.string.invalid_move);
            //invalid choice of dice
        }
        if (val == 0) {
            //round over
            currentPlayer.endRound(false);
            //next player
            nextPlayer();
            currentPlayer.startRound();
            this.state = GreedEnum.NEWPLAYER;
            return context.getString(R.string.round_fail);
        }
        //good play. continue.
        if (currentPlayer.hasDicesLeft(false)) {
            this.state = GreedEnum.CONTINUE;
            if(getCurrentPlayer().getCurrentRoundScore() >= saveLimit){
                canSave = true;
            }
            return context.getString(R.string.score_continue, val);
        }
        this.state = GreedEnum.NEWDICE;
        currentPlayer.turnDices(true);
        canSave = false;
        return context.getString(R.string.score_new_dice, val);
    }

    /**
     * The action to call when current player wants to save the round.
     * @param context a reference to the context to return correct strings.
     * @return The message to give to the user.
     */
    public String saveAction(Context context){
        //Save score.
        GreedPlayer p = currentPlayer;
        p.endRound(true);
        if(p.getScore()>=10000){
            this.state = GreedEnum.GAMEOVER;
            return context.getString(R.string.game_over, p.getName(), p.getScore());
        }else{
            this.state = GreedEnum.NEWPLAYER;
            nextPlayer();
            currentPlayer.startRound();
            return context.getString(R.string.round_end, p.getName(), p.getScore());
        }
    }

    /**
     * Changes the current player of the game to the following.
     */
    private void nextPlayer(){
        int i = players.indexOf(currentPlayer);
        currentPlayer = players.get((i+1)%players.size());
        this.canSave = false;
    }
}
