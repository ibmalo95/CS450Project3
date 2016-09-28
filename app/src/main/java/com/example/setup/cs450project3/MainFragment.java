package com.example.setup.cs450project3;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ArrayList<Card> gameState = new ArrayList();
    private HashMap<Integer, Integer> picture = new HashMap();
    private ArrayList<Card> compare = new ArrayList();
    private Handler handler = new Handler();
    private TextView clickCount = null;


    public MainFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        Resources res = getResources();
        clickCount = (TextView) root.findViewById(res.getIdentifier("click_count", "id", getActivity().getPackageName()));

        // TODO: get the total children of the gridlayout and replace with 16
        for (int i = 1; i <= 16; i++) {
            final int imageNumber = i;
            int level = initGame();
            int id = res.getIdentifier("image" + i, "id", getActivity().getPackageName());
            System.out.println(id);
            ImageButton ib = (ImageButton) root.findViewById(id);

            gameState.add(new Card(ib, level, 0));

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Increment click count
                    Integer value = Integer.parseInt(clickCount.getText().toString()) + 1;
                    clickCount.setText(value.toString());
                    // Get drawable and set to chosen image(max-level)
                    Drawable d = ((ImageButton) view).getDrawable();
                    d.setLevel(gameState.get(imageNumber-1).getLevel());
                    gameState.get(imageNumber-1).flip();
                    // When two images are flipped over see if they match
                    compare.add(gameState.get(imageNumber - 1));
                    if (compare.size() == 2) {
                        comparison(compare.get(0), compare.get(1));
                    }
                }
            });
        }

        return root;
    }

    public Integer initGame() {
        Random randomizer = new Random();
        int picLevel = randomizer.nextInt(8) + 1;
        while (picture.containsKey(picLevel) && picture.get(picLevel) == 2) {
            picLevel = randomizer.nextInt(8) + 1;
        }
        if (picture.containsKey(picLevel)) {
            int newValue = picture.get(picLevel) + 1;
            picture.put(picLevel, newValue);
            return picLevel;
        }
        else {
            picture.put(picLevel, 1);
            // set level of gameState(i) to picLevel
            return picLevel;
        }
    }

    public void comparison(Card one, Card two) {
        final Card card1 = one;
        final Card card2 = two;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (card1.getLevel() == card2.getLevel()) {
                    // keep flipped over
                    card1.getIb().setOnClickListener(null);
                    card2.getIb().setOnClickListener(null);
                    compare.clear();
                }
                else {
                    Drawable drawable1 = (card1.getIb()).getDrawable();
                    Drawable drawable2 = (card2.getIb()).getDrawable();
                    drawable1.setLevel(0);
                    drawable2.setLevel(0);
                    card1.unflip();
                    card2.unflip();
                    compare.clear();

                }

            }
        }, 1000);
    }

}
