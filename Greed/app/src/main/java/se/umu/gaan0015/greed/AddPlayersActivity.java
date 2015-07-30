package se.umu.gaan0015.greed;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Activity used to find out who that is playing.
 */
public class AddPlayersActivity extends ActionBarActivity{
    private ArrayAdapter<String> adapter;
    private Button addB, remB, startGame;
    private EditText editName;
    private ListView nameList;

    private ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        initiateComponents();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_players, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putStringArrayList("NAMES", this.names);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        this.names = savedInstanceState.getStringArrayList("NAMES");
        initiateComponents();
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




    private void initiateComponents(){
        //Initialize components.
        nameList = (ListView)findViewById(R.id.nameListView);
        addB = (Button)findViewById(R.id.addPlayer_button);
        remB = (Button)findViewById(R.id.addPlayer_buttonRemove);
        startGame = (Button)findViewById(R.id.addPlayer_start);
        editName = (EditText)findViewById(R.id.addPlayer_editText);
        addB.setEnabled(editName.getText().length()>0);
        remB.setEnabled(names.size()>0);
        startGame.setEnabled(names.size()>=2);
        adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.name_listview_item, R.id.namelistViewItemcontent, names);
        nameList.setAdapter(adapter);

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
                Intent intent = new Intent(getApplicationContext(), GreedActivity.class);
                LinkedList<GreedPlayerModel> players = new LinkedList<GreedPlayerModel>();
                intent.putExtra(getString(R.string.intent_players),names);

                startActivity(intent);
            }
        });

    }
}
