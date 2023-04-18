package com.soddik;

import com.soddik.entity.PokerHand;
import com.soddik.generator.DeckGenerator;
import com.soddik.generator.RandomHandGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getSimpleName());
    public static void main(String[] args) {
        DeckGenerator deckGenerator = new DeckGenerator();
        RandomHandGenerator randomHandGenerator = new RandomHandGenerator(deckGenerator);

        List<PokerHand> hands = new ArrayList<>();
        for (int index = 0; index < 6; index++) {
            hands.add(randomHandGenerator.generateHand());
        }
        Collections.shuffle(hands);
        hands.sort(PokerHand::compareTo);

        hands.forEach(hand -> logger.info(hand.toString()));
    }
}