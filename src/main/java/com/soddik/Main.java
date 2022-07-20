package com.soddik;

import com.soddik.dto.PokerHand;
import com.soddik.generator.HandGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<PokerHand> hands = new ArrayList<>();
        HandGenerator handGenerator = new HandGenerator();
        Map<String, List<Integer>> deck = handGenerator.generateDeck();
        for (int index = 0; index < 10; index++) {
            hands.add(handGenerator.generateHand(deck));
        }

        Collections.shuffle(hands);

        hands.sort(PokerHand::compareTo);
        hands.forEach(System.out::println);
    }
}