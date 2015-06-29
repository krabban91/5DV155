package com.example.gabriel.greed;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Greed extends ActionBarActivity {
    //Graphical items.
    private static Toast toast;
    private Button[] diceButtons = new Button[6];
    private Button saveButton, scoreButton, throwButton;
    private TextView playerLabel, currentScoreLabel, totalScoreLabel;

    // The game model
    private GreedModel gameModel;

    @Override
    protected void onDestroy(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greed);
        initiateComponents();
        ArrayList<String> names = null;
        //Handle the players retrieved from addplayers.
        if(getIntent().getExtras()!=null) {
            names = getIntent().getExtras().getStringArrayList(getString(R.string.intent_players));
        }

        try{
            this.gameModel = new GreedModel(names, 300);

            addListeners();
            updateInformation();
            updateDices();
            showToast(R.string.game_start);
        }catch(IllegalArgumentException iaE){
            // When this hits: click "New Game" again
        }
        prepareButtons();
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

    /**
     * Takes a String resource id and writes a short toast of it.
     * @param resid an int referring to an action string resource.
     */
    private void showToast(int resid){
        String s = getString(resid);
        showToast(s);
    }

    /**
     * Takes a String and writes a short toast of it.
     * @param s The String to show
     */
    private void showToast(String s){
        if(s == null){ return; }

        if(Greed.toast!= null){
            Greed.toast.cancel();
        }

        Greed.toast = Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT);

        Greed.toast.show();
    }

    /**
     * Adds action listeners to buttons in the activity.
     */
    private void addListeners(){
        for (int i = 0; i <diceButtons.length ; i++) {
            diceButtons[i].setOnClickListener(new DiceClickListener(this.gameModel, i, diceButtons[i]));
        }
        saveButton.setOnClickListener(new OnSaveClickListener());

        scoreButton.setOnClickListener(new OnScoreClickListener());

        throwButton.setOnClickListener(new OnThrowClickListener());
    }

    /**
     * Enables/disables buttons connected to score action.
     * @param enable <tt>true</tt> if score is to be possible to collect.
     */
    private void setEnabledScoreSelection(boolean enable){
        scoreButton.setEnabled(enable);
        for (Button b : diceButtons){
            b.setEnabled(enable);
        }
    }

    /**
     * Updates the background resource of all the dice buttons.
     */
    private  void updateDices(){
        if(gameModel == null){
            return;
        }
        for (int i = 0; i <diceButtons.length ; i++) {
            diceButtons[i].setBackgroundResource(gameModel.getCurrentPlayer().getImgNoForDice(i));
        }
    }

    /**
     * Updates the name and score to the current.
     */
    private void updateInformation(){
        if(gameModel == null){
            return;
        }
        GreedPlayer p = gameModel.getCurrentPlayer();
        playerLabel.setText(""+p.getName());
        currentScoreLabel.setText("" + p.getCurrentRoundScore());
        totalScoreLabel.setText("" + p.getScore());
    }

    /**
     * Prepares the buttons for a game or not.
     */
    private void prepareButtons(){
        // Enable/disable buttons
        this.setEnabledScoreSelection(false);
        this.saveButton.setEnabled(false);
        this.throwButton.setEnabled(!(gameModel == null ||
                gameModel.getState() == GreedModel.GreedEnum.GAMEOVER));
    }

    /*
     * Connects the xml objects to variables.
     */
    private void initiateComponents(){
        //labels
        this.playerLabel = (TextView) findViewById(R.id.playerValue);
        this.currentScoreLabel = (TextView) findViewById(R.id.scoreValue);
        this.totalScoreLabel = (TextView) findViewById(R.id.totalscoreValue);
        // -------------
        // Dice buttons
        this.diceButtons[0] = (Button) findViewById(R.id.imageButton00);
        this.diceButtons[1] = (Button) findViewById(R.id.imageButton01);
        this.diceButtons[2] = (Button) findViewById(R.id.imageButton02);
        this.diceButtons[3] = (Button) findViewById(R.id.imageButton10);
        this.diceButtons[4] = (Button) findViewById(R.id.imageButton11);
        this.diceButtons[5] = (Button) findViewById(R.id.imageButton12);
        // -------------
        // Action buttons
        this.saveButton = (Button) findViewById(R.id.greed_save);
        this.scoreButton = (Button) findViewById(R.id.greed_score);
        this.throwButton = (Button) findViewById(R.id.greed_throw);
    }

    /**
     * A Listener specialized for a score button
     */
    private class OnScoreClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String s = gameModel.scoreAction(getApplicationContext());
            showToast(s);
            switch (gameModel.getState()){
                case INVALIDMOVE:
                    //do nothing
                    break;
                case NEWPLAYER:
                case CONTINUE:
                case NEWDICE:
                {
                    saveButton.setEnabled(gameModel.playerCanSave());
                    throwButton.setEnabled(true);
                    updateInformation();
                    updateDices();
                    setEnabledScoreSelection(false);
                    break;
                }
            }
        }
    }
    /**
     * A Listener specialized for a save button
     */
    private class OnSaveClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            String s = gameModel.saveAction(getApplicationContext());
            showToast(s);
            switch (gameModel.getState()) {
                case GAMEOVER:
                    throwButton.setEnabled(false);
                    break;
                case NEWPLAYER:
                    throwButton.setEnabled(true);
                    break;
            }
            saveButton.setEnabled(false);
            setEnabledScoreSelection(false);
            updateInformation();
        }
    }
    /**
     * A Listener specialized for a throw button
     */
    private class OnThrowClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String s = gameModel.throwAction(getApplicationContext());
            showToast(s);
            switch (gameModel.getState()) {
                case DICETHROWN:
                    throwButton.setEnabled(false);
                    setEnabledScoreSelection(true);
                    saveButton.setEnabled(false);
                    updateDices();
                    break;
            }
        }
    }
}
