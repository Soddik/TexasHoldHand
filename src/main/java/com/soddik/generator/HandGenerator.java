package com.soddik.generator;

import com.soddik.dto.PokerHand;
import com.soddik.exception.UnexpectedCardAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class HandGenerator {
    private final List<String> kinds = List.of("S", "H", "D", "C");

    public Map<String, List<Integer>> generateDeck() {
        Map<String, List<Integer>> deck = new HashMap<>();
        for (String kind : kinds) {
            List<Integer> cards = new ArrayList<>();
            for (int index = 2; index < 15; index++) {
                cards.add(index);
            }

            deck.put(kind, cards);
        }

        return deck;
    }

    public PokerHand generateHand(Map<String, List<Integer>> deck) {
        StringBuilder sb = new StringBuilder();
        for (int cardIndex = 0; cardIndex < 5; cardIndex++) {
            List<String> actualKinds = kinds.stream()
                    .filter(kind -> deck.get(kind).size() != 0)
                    .toList();
            String randomKind = actualKinds.get(ThreadLocalRandom.current().nextInt(0, actualKinds.size()));
            getRandomCard(deck, sb, randomKind);
        }

        return new PokerHand(sb.toString().trim());
    }

    private void getRandomCard(Map<String, List<Integer>> deck, StringBuilder sb, String randomKind) {
        Integer value = deck.get(randomKind).get(ThreadLocalRandom.current().nextInt(0, deck.get(randomKind).size()));

        deck.get(randomKind).remove(value);

        if (value < 10) {
            sb.append(value);
        } else {
            String strValue = switch (value) {
                case 14 -> "A";
                case 13 -> "K";
                case 12 -> "Q";
                case 11 -> "J";
                case 10 -> "T";
                default -> throw new UnexpectedCardAttribute(String.format("Unexpected card value %s", value));
            };

            sb.append(strValue);
        }
        sb.append(randomKind);
        sb.append(" ");
    }

    public enum Kind {
        S(1, "SPADES"),
        H(2, "HEARTS"),
        D(3, "DIAMONDS"),
        C(4, "CLUBS");

        private final int intValue;
        private final String strValue;

        Kind(int intValue, String strValue) {
            this.intValue = intValue;
            this.strValue = strValue;
        }

        public int intValue() {
            return this.intValue;
        }

        public String strValue() {
            return this.strValue;
        }
    }

    public enum Value {
        TWO(2, "2"),
        THREE(3, "3"),
        FOUR(4, "4"),
        FIVE(5, "5"),
        SIX(6, "6"),
        SEVEN(7, "7"),
        EIGHT(8, "8"),
        NINE(9, "9"),
        TEN(10, "T"),
        JACK(11, "J"),
        QUEEN(12, "Q"),
        KING(13, "K"),
        ACE(14, "A");

        private static final Map<String, Value> STRING_VALUE = new HashMap<>();
        private static final Map<Integer, Value> INTEGER_VALUE = new HashMap<>();

        private final int intValue;
        private final String strValue;

        static {
            for (Value value : values()) {
                STRING_VALUE.put(value.strValue, value);
                INTEGER_VALUE.put(value.intValue, value);
            }
        }

        Value(int intValue, String strValue) {
            this.intValue = intValue;
            this.strValue = strValue;
        }

        public int intValue() {
            return this.intValue;
        }

        public String strValue() {
            return this.strValue;
        }

        public static Value valueOfInt(int num) {
            return INTEGER_VALUE.get(num);
        }

        public static Value valueOfString(String name) {
            return STRING_VALUE.get(name);
        }
    }
}