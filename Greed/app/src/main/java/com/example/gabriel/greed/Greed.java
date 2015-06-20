package com.example.gabriel.greed;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;



public class Greed extends ActionBarActivity {

    private LinkedList<GreedPlayer> players;
    private GreedPlayer currentPlayer;
    private Button[] dices = new Button[6];
    private Button saveB,scoreB, throwB;
    private TextView playerLabel, currentScore, totalScore, information;

    @Override
    protected void onDestroy(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greed);
        initiateButtons();

        System.out.println("wth. " + savedInstanceState);
        System.out.println("wth. "+getIntent().getExtras());
        ArrayList<String> names = null;
        //Handle the players retrieved from addplayers.
        if(getIntent().getExtras()!=null) {
            System.out.println("something there");
            names = getIntent().getExtras().getStringArrayList("Players");
        }
        players = new LinkedList<>();
        if(names == null){
            //...

            System.out.println("Nothing there");
        } else if(names.size() != 0){

            for(String n : names){
                players.add(new GreedPlayer(n));
            }
            currentPlayer = players.get(0);
            System.out.println("players " + players.size());
            addListeners();
            updateInformation();
            updateDices();
            information.setText("The game has begun");
        }
    }

    private void initiateButtons(){
        // buttons
        playerLabel = (TextView) findViewById(R.id.playerValue);
        currentScore = (TextView) findViewById(R.id.scoreValue);
        totalScore = (TextView) findViewById(R.id.totalscoreValue);
        information = (TextView) findViewById(R.id.greed_information);
        dices[0] = (Button) findViewById(R.id.imageButton00);
        dices[1] = (Button) findViewById(R.id.imageButton01);
        dices[2] = (Button) findViewById(R.id.imageButton02);
        dices[3] = (Button) findViewById(R.id.imageButton10);
        dices[4] = (Button) findViewById(R.id.imageButton11);
        dices[5] = (Button) findViewById(R.id.imageButton12);
        saveB  = (Button) findViewById(R.id.greed_save);
        scoreB = (Button) findViewById(R.id.greed_score);
        throwB = (Button) findViewById(R.id.greed_throw);
        setEnabledScoreSelection(false);
        saveB.setEnabled(false);
        throwB.setEnabled(true);
        // labels
    }

    private void addListeners(){

        dices[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.toggleDiceSelected(0);
                dices[0].setBackgroundResource(currentPlayer.getImgNoForDice(0));
            }
        });

        dices[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.toggleDiceSelected(1);
                dices[1].setBackgroundResource(currentPlayer.getImgNoForDice(1));
            }
        });
        dices[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.toggleDiceSelected(2);
                dices[2].setBackgroundResource(currentPlayer.getImgNoForDice(2));
            }
        });
        dices[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.toggleDiceSelected(3);
                dices[3].setBackgroundResource(currentPlayer.getImgNoForDice(3));
            }
        });
        dices[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.toggleDiceSelected(4);
                dices[4].setBackgroundResource(currentPlayer.getImgNoForDice(4));
            }
        });
        dices[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayer.toggleDiceSelected(5);
                dices[5].setBackgroundResource(currentPlayer.getImgNoForDice(5));
            }
        });

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save score.
                GreedPlayer p = currentPlayer;
                p.endRound();
                if(p.getTotalScore()>=10000){
                    information.setText("Game Over! "+p.getName()+" won with "+
                            p.getTotalScore()+"points.");
                    throwB.setEnabled(false);
                }else{
                    information.setText("Round ended. "+p.getName()+" now has " +
                            p.getTotalScore() +" points");
                    //Next player.
                    nextPlayer();
                    currentPlayer.startRound();
                    updateInformation();
                    throwB.setEnabled(true);
                }
                saveB.setEnabled(false);
                setEnabledScoreSelection(false);
            }
        });

        scoreB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = currentPlayer.collectScore();
                if (val < 0) {
                    information.setText("That is an invalid move...");
                    //invalid choice of dice
                } else if (val == 0) {
                    //round over
                    information.setText("Round ended after failure.");
                    //Toast
                    currentPlayer.endRound();
                    updateDices();
                    //next player
                    nextPlayer();
                    currentPlayer.startRound();
                    updateInformation();
                    setEnabledScoreSelection(false);
                    saveB.setEnabled(false);
                    throwB.setEnabled(true);
                } else {
                    //good play. continue.
                    //No dices left. Receive them again.
                    if (!currentPlayer.hasDicesLeft(false)) {
                        currentPlayer.turnDices(true);
                        saveB.setEnabled(false);
                        information.setText("Good job. " + val + "points and more dices.");
                    } else if (currentPlayer.getCurrentRoundScore() >= 300) {
                        saveB.setEnabled(true);
                        information.setText("Good job." + val + "points");
                    }
                    throwB.setEnabled(true);
                    updateInformation();
                    updateDices();
                    setEnabledScoreSelection(false);
                }
            }
        });
        throwB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                information.setText("Dices thrown.");
                currentPlayer.rollDices();
                throwB.setEnabled(false);
                setEnabledScoreSelection(true);
                saveB.setEnabled(false);
                updateDices();
            }
        });
    }

    private void setEnabledScoreSelection(boolean enable){
        scoreB.setEnabled(enable);
        for (Button b : dices){
            b.setEnabled(enable);
        }
    }

    private  void updateDices(){
        if(currentPlayer == null){
            return;
        }

        dices[0].setBackgroundResource(currentPlayer.getImgNoForDice(0));
        dices[1].setBackgroundResource(currentPlayer.getImgNoForDice(1));
        dices[2].setBackgroundResource(currentPlayer.getImgNoForDice(2));
        dices[3].setBackgroundResource(currentPlayer.getImgNoForDice(3));
        dices[4].setBackgroundResource(currentPlayer.getImgNoForDice(4));
        dices[5].setBackgroundResource(currentPlayer.getImgNoForDice(5));
    }

    private void updateInformation(){
        if(currentPlayer == null){
            return;
        }
        playerLabel.setText(""+currentPlayer.getName());
        currentScore.setText(""+currentPlayer.getCurrentRoundScore());
        totalScore.setText(""+currentPlayer.getTotalScore());
    }

    private void nextPlayer(){
        int i = players.indexOf(currentPlayer);
        currentPlayer = players.get((i+1)%players.size());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_greed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_game_button) {
            Intent intent = new Intent(this, AddPlayers.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
