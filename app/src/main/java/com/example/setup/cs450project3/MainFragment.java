package com.example.setup.cs450project3;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.os.Handler;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private ArrayList<Card> gameState;
    private HashMap<Integer, Integer> picture;
    private ArrayList<Card> compare;
    private Handler handler = null;
    private TextView clickCount = null;
    private Integer count;

    SoundPool sp = null;
    int bloopSound = 0;
    int wrongSound = 0;
    int matchSound = 0;


    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        handler = new Handler();
        gameState = new ArrayList();
        picture = new HashMap();
        compare = new ArrayList();
        count = 0;

        this.sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        this.bloopSound = this.sp.load(getContext(), R.raw.bloop, 1);
        this.wrongSound = this.sp.load(getContext(), R.raw.wrong, 1);
        this.matchSound = this.sp.load(getContext(), R.raw.match, 1);

        initGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(root);
        updateImgButtons(root);
        gameWon(root);

        return root;
    }

    // initiate gameState
    public void initGame() {
        Random randomizer = new Random();
        for (int i = 1; i <= 16; i++) {
            int picLevel = randomizer.nextInt(16) + 1;
            // if pictures size is eqaul to 8 and piclevel is not one of the keys
            while ((picture.containsKey(picLevel) && picture.get(picLevel) == 2) || (picture.size() == 8 && !picture.containsKey(picLevel))) {
                picLevel = randomizer.nextInt(16) + 1;
            }
            // if picLevel in pictures
            if (picture.containsKey(picLevel)) {
                int newValue = picture.get(picLevel) + 1;
                picture.put(picLevel, newValue);
                gameState.add(new Card(0, picLevel, 0));

            } else {
                picture.put(picLevel, 1);
                gameState.add(new Card(0, picLevel, 0));
            }
        }
        System.out.println(gameState.toString());

    }

    public void initViews(View root) {
        final View root1 = root;

        this.clickCount = (TextView) root.findViewById(R.id.click_count);
        this.clickCount.setText(count.toString());
        Resources res = getResources();
        Log.i("gamestate:", "newState");
        // TODO: get the total children of the gridlayout and replace with 16
        for (int i = 1; i <= 16; i++) {
            final int imageNumber = i;

            int id = res.getIdentifier("image" + i, "id", getActivity().getPackageName());


            // should only happen the first time onCreateView is called
            if ((gameState.get(i-1).getId()) == 0) {
                gameState.get(i-1).setId(id);
            }
            ImageButton ib = (ImageButton) root.findViewById(gameState.get(i-1).getId());

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Play sound when clicked
                    sp.play(bloopSound, 1f, 1f, 1, 0, 1);

                    // Increment click count
                    count = count + 1;
                    clickCount.setText(count.toString());

                    // Get drawable and set to chosen image(max-level)
                    Drawable d = ((ImageButton) view).getDrawable();
                    d.setLevel(gameState.get(imageNumber-1).getLevel());
                    gameState.get(imageNumber-1).flip();

                    // When two images are flipped over see if they match
                    compare.add(gameState.get(imageNumber - 1));
                    if (compare.size() == 2) {
                        ((MainActivity)getActivity()).loading();
                        comparison(compare.get(0), compare.get(1), root1);
                    }
                }
            });
        }
    }

    public void updateImgButtons(View root) {
        for (int i = 0; i < gameState.size(); i++) {
            Card card = gameState.get(i);
            card.updateState((ImageButton) root.findViewById(card.getId()));
        }
    }

    public void comparison(Card one, Card two, View root) {
        final Card card1 = one;
        final Card card2 = two;
        final View root1 = root;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (card1.getLevel() == card2.getLevel()) {
                    sp.play(matchSound, 1f, 1f, 1, 0, 1);
                    // keep flipped over
                    ((ImageButton) root1.findViewById(card1.getId())).setOnClickListener(null);
                    ((ImageButton) root1.findViewById(card2.getId())).setOnClickListener(null);
                    compare.clear();
                    gameWon(root1);
                }
                else {
                    sp.play(wrongSound, 1f, 1f, 1, 0, 1);
                    Drawable drawable1 = ((ImageButton) root1.findViewById(card1.getId())).getDrawable();
                    Drawable drawable2 = ((ImageButton) root1.findViewById(card2.getId())).getDrawable();
                    drawable1.setLevel(0);
                    drawable2.setLevel(0);
                    card1.unflip();
                    card2.unflip();
                    compare.clear();
                }
                ((MainActivity)getActivity()).stopLoading();
            }
        }, 1000);
    }

    public void gameWon(View root) {
        final View view = root;
        Button playAgain = (Button) view.findViewById(R.id.play_again);
        int won = 0;
        for (int i = 0; i < gameState.size(); i++) {
            if (gameState.get(i).getState() == 1) {
                won++;
            }
        }
        if (won == gameState.size()) {
            playAgain.setVisibility(View.VISIBLE);
            playAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reset(view);
                }
            });
        }
    }

    public void reset(View view) {
        for (int i = 0; i < gameState.size(); i++) {
            Drawable d = ((ImageButton) view.findViewById(gameState.get(i).getId())).getDrawable();
            d.setLevel(0);
        }
        gameState.clear();
        picture.clear();
        count = 0;
        clickCount.setText(count.toString());
        initGame();
        initViews(view);

    }


    @Override
    public void onPause() {
        super.onPause();

    }

}
