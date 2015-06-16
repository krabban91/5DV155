package com.example.gabriel.greed;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.LinkedList;


public class AddPlayers extends ActionBarActivity{
    private ArrayList<String> names = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Button addB, remB, startGame;
    private EditText editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        ListView l = (ListView)findViewById(R.id.nameListView);
        addB = (Button)findViewById(R.id.addPlayer_button);
        remB = (Button)findViewById(R.id.addPlayer_buttonRemove);
        startGame = (Button)findViewById(R.id.addPlayer_start);
        editName = (EditText)findViewById(R.id.addPlayer_editText);
        addB.setEnabled(false);
        remB.setEnabled(false);
        startGame.setEnabled(false);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, names);
        l.setAdapter(adapter);

        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names.add(editName.getText().toString());
                remB.setEnabled(true);
                if (names.size() > 1) {
                    startGame.setEnabled(true);
                }
                editName.setText("");
                adapter.notifyDataSetChanged();
            }
        });

        remB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(names.size()>0) {
                    names.remove(names.size() - 1);
                    if(names.size()<=1){
                        startGame.setEnabled(false);
                    }
                    adapter.notifyDataSetChanged();
                } else{
                    remB.setEnabled(false);
                }
            }
        });

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //..
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //..
            }

            @Override
            public void afterTextChanged(Editable s) {
                addB.setEnabled(s.length() > 0);
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Greed.class);
                LinkedList<GreedPlayer> players = new LinkedList<GreedPlayer>();
                intent.putExtra("Players",names);

                startActivity(intent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_players, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
