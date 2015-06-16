package com.example.gabriel.greed;

import android.content.Intent;
import android.media.Image;
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
    private int currentPlayer = 0;
    private ImageButton[] dices = new ImageButton[6];
    private Button saveB,scoreB, throwB;

    @Override
    protected void onDestroy(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greed);

        //Handle the players retrieved from addplayers.
        ArrayList<String> names = savedInstanceState.getStringArrayList("Players");
        players = new LinkedList<>();
        if(names == null || names.size() == 0){
            //...
        } else {

            for(String n : names){
                players.add(new GreedPlayer(n));
            }
        }

        initiateButtons();


    }



    private void initiateButtons(){

        dices[0] = (ImageButton) findViewById(R.id.imageButton00);
        dices[1] = (ImageButton) findViewById(R.id.imageButton01);
        dices[2] = (ImageButton) findViewById(R.id.imageButton02);
        dices[3] = (ImageButton) findViewById(R.id.imageButton10);
        dices[4] = (ImageButton) findViewById(R.id.imageButton11);
        dices[5] = (ImageButton) findViewById(R.id.imageButton12);
        saveB  = (Button) findViewById(R.id.greed_save);
        scoreB = (Button) findViewById(R.id.greed_score);
        throwB = (Button) findViewById(R.id.greed_throw);
        addListeners();
    }

    private void addListeners(){

        dices[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.get(currentPlayer).toggleDiceSelected(0);
                //dices[0].setBackground(players.get(currentPlayer).isDiceSelected()?

                );
            }
        });
        dices[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.get(currentPlayer).toggleDiceSelected(1);
            }
        });
        dices[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.get(currentPlayer).toggleDiceSelected(2);
            }
        });
        dices[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.get(currentPlayer).toggleDiceSelected(3);
            }
        });
        dices[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.get(currentPlayer).toggleDiceSelected(4);
            }
        });
        dices[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.get(currentPlayer).toggleDiceSelected(5);
            }
        });
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
