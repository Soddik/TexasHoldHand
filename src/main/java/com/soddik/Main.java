package com.soddik;

import com.soddik.dto.PokerHand;
import com.soddik.generator.HandGenerator;

import java.util.ArrayList;
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


       // List<TestHand> hands = init();
        hands.forEach(System.out::println);
    }


    private static List<PokerHand> init() {
        List<PokerHand> pokerHands = new ArrayList<>();

        PokerHand royalFlush = new PokerHand("AC JC KC QC TC");
        PokerHand straightFlush = new PokerHand("9C JC KC QC TC");
        PokerHand four = new PokerHand("JC JS JD JH 6C");
        PokerHand fullHouse = new PokerHand("JC JS JD 6H 6C");
        PokerHand flush = new PokerHand("AC 2C KC QC TC");
        PokerHand straight = new PokerHand("3S 2C 4H 5C 6D");
        PokerHand three = new PokerHand("JC JS JD 3H 6C");
        PokerHand two = new PokerHand("JC JS 3D 3H 6C");
        PokerHand one = new PokerHand("JC JS 4D 3H 6C");
        PokerHand high = new PokerHand("AC QS TD 3H 6C");

        pokerHands.add(royalFlush);
        pokerHands.add(straightFlush);
        pokerHands.add(four);
        pokerHands.add(fullHouse);
        pokerHands.add(flush);
        pokerHands.add(straight);
        pokerHands.add(three);
        pokerHands.add(two);
        pokerHands.add(one);
        pokerHands.add(high);

        return pokerHands;
    }

    private static int applyAsInt(String[] card) {
        int value = 0;
        for (byte b : card[0].getBytes()) {
            value = (value << 8) + (b & 0xFF);
        }

        return value;
    }
}