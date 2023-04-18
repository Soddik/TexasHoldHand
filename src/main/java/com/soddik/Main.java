package com.soddik;

import com.soddik.entity.PokerHand;
import com.soddik.generator.DeckGenerator;
import com.soddik.generator.RandomHandGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DeckGenerator deckGenerator = new DeckGenerator();
        RandomHandGenerator randomHandGenerator = new RandomHandGenerator(deckGenerator);

        List<PokerHand> hands = new ArrayList<>();
        for (int index = 0; index < 6; index++) {
            hands.add(randomHandGenerator.generateHand());
        }
        Collections.shuffle(hands);
        hands.sort(PokerHand::compareTo);
        //TODO impl logger
        hands.forEach(System.out::println);
    }
}