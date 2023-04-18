package com.soddik.generator;

import com.soddik.entity.PokerHand;
import com.soddik.exception.UnexpectedCardAttributeValueException;
import com.soddik.parser.HandParser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHandGenerator {
    private final DeckGenerator deckGenerator;

    public RandomHandGenerator(DeckGenerator deckGenerator) {
        this.deckGenerator = deckGenerator;
    }

    public PokerHand generateHand() {
        Map<String, List<Integer>> deck = deckGenerator.generateDeck();
        StringBuilder sb = new StringBuilder();
        for (int cardIndex = 0; cardIndex < 5; cardIndex++) {
            List<String> actualKinds = deckGenerator.getKinds().stream()
                    .filter(kind -> deck.get(kind).size() != 0)
                    .toList();
            String randomKind = actualKinds.get(ThreadLocalRandom.current().nextInt(0, actualKinds.size()));
            addRandomCard(deck, sb, randomKind);
        }
        deckGenerator.clearDeck();

        return new PokerHand(sb.toString().trim(), new HandParser());
    }

    private void addRandomCard(Map<String, List<Integer>> deck, StringBuilder sb, String randomKind) {
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
                        throw new UnexpectedCardAttributeValueException(String.format("Unexpected card value %s", value));
            };

            sb.append(strValue);
        }
        sb.append(randomKind);
        sb.append(" ");
    }
}
