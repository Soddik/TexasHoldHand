package com.soddik.parser;

import com.soddik.exception.*;

import java.util.HashSet;
import java.util.Set;

public class HandParser {
    private Integer[][] cards;

    public HandParser() {
        this.cards = new Integer[5][2];
    }

    public void parseString(String hand) {
        if (hand == null) {
            throw new NullPointerException("Entry string cannot be null");
        }

        String[] strCards = hand.split(" ");
        uniquenessOfCardsInHandCheck(strCards);
        if (strCards.length == 5) {
            createHandAndSort(strCards);
        } else {
            throw new CardAmountException(
                    String.format("There must be exactly 5 cards in the hand, but there are %s", strCards.length));
        }
    }

    private void createHandAndSort(String[] strCards) {
        for (int index = 0; index < strCards.length; index++) {
            String[] card = strCards[index].split("");
            cards[index] = createCard(card);
        }
        sortCardsByValue(cards);
    }

    private Integer[] createCard(String[] card) {
        if (card.length == 2) {
            Integer value = switch (card[0]) {
                case "A" -> 14;
                case "K" -> 13;
                case "Q" -> 12;
                case "J" -> 11;
                case "T" -> 10;
                default -> validateNumericValue(card[0]);
            };

            Integer kind = switch (card[1]) {
                case "S" -> 20;
                case "H" -> 21;
                case "D" -> 22;
                case "C" -> 23;
                default ->
                        throw new UnexpectedCardAttributeKindException(String.format("Unexpected card kind %s", card[1]));
            };

            return new Integer[]{value, kind};
        } else {
            throw new CardAttributeAmountException(
                    String.format("The card should contain 2 attributes, but contains %s", card.length));
        }
    }

    private Integer validateNumericValue(String value) {
        if (value.matches("[2-9]")) {
            int num = Integer.parseInt(value);
            if (num > 1 && num < 10) {
                return num;
            }
        }
        throw new UnexpectedCardAttributeValueException(String.format("Unexpected card value %s", value));
    }

    private void uniquenessOfCardsInHandCheck(String[] cards) {
        Set<String> set = new HashSet<>();
        for (String card : cards) {
            if (!set.contains(card)) {
                set.add(card);
            } else {
                throw new UniqueCardException(
                        String.format("Unique cards must be in hand, but there is a duplicate with card: %s", card));
            }
        }
    }

    private void sortCardsByValue(Integer[][] cards) {
        for (int i = 0; i < cards.length; i++) {
            int pos = i;
            int value = cards[i][0];
            int kind = cards[i][1];
            for (int j = i + 1; j < cards.length; j++) {
                if (cards[j][0] < value) {
                    pos = j;
                    value = cards[j][0];
                    kind = cards[j][1];
                }
            }
            cards[pos][0] = cards[i][0];
            cards[pos][1] = cards[i][1];

            cards[i][0] = value;
            cards[i][1] = kind;
        }
    }

    public Integer[][] getCards() {
        Integer[][] copy = cards;
        clear();
        return copy;
    }

    private void clear() {
        this.cards = new Integer[5][2];
    }
}
