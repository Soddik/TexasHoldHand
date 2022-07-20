package com.soddik;

import com.soddik.dto.PokerHand;
import com.soddik.generator.DeckGenerator;
import com.soddik.generator.HandGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DeckGenerator deckGenerator = new DeckGenerator();
        HandGenerator handGenerator = new HandGenerator(deckGenerator);

        List<PokerHand> hands = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            hands.add(handGenerator.generateHand());
        }
        Collections.shuffle(hands);
        hands.sort(PokerHand::compareTo);
        hands.forEach(System.out::println);
    }
}