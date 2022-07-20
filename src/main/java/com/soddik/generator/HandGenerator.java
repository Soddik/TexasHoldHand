package com.soddik.generator;

import com.soddik.dto.PokerHand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.soddik.exception.CardAttributeException.UnexpectedCardAttributeValueException;

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
                default -> throw new UnexpectedCardAttributeValueException(String.format("Unexpected card value %s", value));
            };

            sb.append(strValue);
        }
        sb.append(randomKind);
        sb.append(" ");
    }
}