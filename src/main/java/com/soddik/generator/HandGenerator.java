package com.soddik.generator;

import com.soddik.dto.PokerHand;
import com.soddik.exception.CardAttributeException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class HandGenerator {
    private final DeckGenerator deckGenerator;

    public HandGenerator(DeckGenerator deckGenerator) {
        this.deckGenerator = deckGenerator;
        deckGenerator.generateDeck();
    }

    public PokerHand generateHand() {
        Map<String, List<Integer>> deck = deckGenerator.getDeck();
        StringBuilder sb = new StringBuilder();
        for (int cardIndex = 0; cardIndex < 5; cardIndex++) {
            List<String> actualKinds = deckGenerator.getKinds().stream()
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
                default ->
                        throw new CardAttributeException.UnexpectedCardAttributeValueException(String.format("Unexpected card value %s", value));
            };

            sb.append(strValue);
        }
        sb.append(randomKind);
        sb.append(" ");
    }
}
