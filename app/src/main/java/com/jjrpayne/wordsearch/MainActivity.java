package com.jjrpayne.wordsearch;

import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    MyRecyclerViewAdapter adapter;
    String letters[];
    TextView wordTextView[];
    RecyclerView grid;
    int wordsFound;
    int wordIndices[][];
    boolean found[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        letters = new String[10*10];
        String[] words = {"SWIFT", "KOTLIN", "OBJECTIVEC", "VARIABLE", "JAVA", "MOBILE"};
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random r = new Random();

        wordTextView = new TextView[words.length];
        wordTextView[0] = findViewById(R.id.word0);
        wordTextView[1] = findViewById(R.id.word1);
        wordTextView[2] = findViewById(R.id.word2);
        wordTextView[3] = findViewById(R.id.word3);
        wordTextView[4] = findViewById(R.id.word4);
        wordTextView[5] = findViewById(R.id.word5);

        wordsFound = 0;
        if(savedInstanceState != null)
            wordsFound = savedInstanceState.getInt("wordsFound");

        if(savedInstanceState == null) {
            found = new boolean[words.length];
            for (int i = 0; i < words.length; i++) {
                wordTextView[i].setText(words[i]);
                found[i] = false;
            }
        } else {
            found = savedInstanceState.getBooleanArray("found");
            for (int i=0; i<words.length; i++){
                wordTextView[i].setText(words[i]);
                if(found[i])
                    wordTextView[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }

        wordIndices = new int[words.length][];
        for(int i=0; i<words.length; i++){
            wordIndices[i] = new int[words[i].length()];
        }
        for(int i=0; i<wordIndices[0].length; i++){
            wordIndices[0][i] = 27+10*i;
        }
        for(int i=0; i<wordIndices[1].length; i++){
            wordIndices[1][i] = 82+i;
        }
        for(int i=0; i<wordIndices[2].length; i++){
            wordIndices[2][i] = 10+i;
        }
        for(int i=0; i<wordIndices[3].length; i++){
            wordIndices[3][i] = 21+i*10;
        }
        for(int i=0; i<wordIndices[4].length; i++){
            wordIndices[4][i] = 43+i;
        }
        for(int i=0; i<wordIndices[5].length; i++){
            wordIndices[5][i] = 38+i*10;
        }


        for(int i=0; i<wordIndices.length; i++){
            for (int j=0; j<wordIndices[i].length; j++){
                letters[wordIndices[i][j]] = Character.toString(words[i].charAt(j));
            }
        }

        if(savedInstanceState == null) {
            for (int i = 0; i < letters.length; i++) {
                if (letters[i] == null)
                    letters[i] = Character.toString(alphabet.charAt(r.nextInt(alphabet.length())));
            }
        } else {
            letters = savedInstanceState.getStringArray("letters");
        }

        grid = findViewById(R.id.grid);
        int columns = 10;
        grid.setLayoutManager(new GridLayoutManager(this, columns));
        adapter = new MyRecyclerViewAdapter(this, letters);
        adapter.setClickListener(this);
        grid.setAdapter(adapter);

    }


    @Override
    public void onItemClick(View view, int position){

        if(wordsFound == found.length)
            return;

        int word = -1;
        for(int i=0; i<wordIndices.length; i++){
            for(int j=0; j<wordIndices[i].length; j++){
                if(position == wordIndices[i][j]){
                    word = i;
                    break;
                }
            }
        }
        if(word == -1)
            return;

        if(found[word])
            return;

        wordsFound++;
        if(wordsFound == found.length){
            AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(this);
            myAlertBuilder.setTitle("Congratulations!");
            myAlertBuilder.setMessage("You found all of the words!");
            myAlertBuilder.setPositiveButton("OK", null);
            myAlertBuilder.show();
        }
        found[word] = true;
        wordTextView[word].setTextColor(getResources().getColor(R.color.colorPrimary));
        for(int i=0; i<wordIndices[word].length; i++){
            letters[wordIndices[word][i]] = letters[wordIndices[word][i]].toLowerCase();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putBooleanArray("found", found);
        outstate.putInt("wordsFound", wordsFound);
        outstate.putStringArray("letters", letters);
    }


}
