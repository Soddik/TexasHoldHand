package com.soddik.dto;

import com.soddik.exception.CardAttributeException;
import com.soddik.exception.HandCardAmountException;
import com.soddik.exception.UnexpectedCardAttribute;
import com.soddik.exception.UniqueCardException;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.soddik.dto.PokerHand.HandValue.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


public class PokerHand {
    private final int[][] cards = new int[5][2];

    private final HandValue combination;

    public PokerHand(String hand) {
        parseString(hand);

        this.combination = HandValueValidator
                .isRoyalFlush()
                .or(HandValueValidator.isStraightFlush())
                .or(HandValueValidator.isFourOfAKind())
                .or(HandValueValidator.isFullHouse())
                .or(HandValueValidator.isFlush())
                .or(HandValueValidator.isStraight())
                .or(HandValueValidator.isThreeOfAKind())
                .or(HandValueValidator.isTwoPair())
                .or(HandValueValidator.isOnePair())
                .apply(this);
    }

    public int[][] getCards() {
        return cards.clone();
    }

    public HandValue getCombination() {
        return combination;
    }

    private Integer validateValue(String value) {
        if (value.matches("[2-9]")) {
            int num = Integer.parseInt(value);
            if (num > 1 && num < 10) {
                return num;
            }
        }

        throw new UnexpectedCardAttribute(String.format("Unexpected card value %s", value));
    }

    private void parseString(String string) {
        String[] strCards = string.split(" ");
        if (strCards.length == 5) {
            for (int index = 0; index < strCards.length; index++) {
                String[] card = strCards[index].split("");

                if (card.length == 2) {
                    Integer value = switch (card[0]) {
                        case "A" -> 14;
                        case "K" -> 13;
                        case "Q" -> 12;
                        case "J" -> 11;
                        case "T" -> 10;
                        default -> validateValue(card[0]);
                    };

                    Integer kind = switch (card[1]) {
                        case "S" -> 20;
                        case "H" -> 21;
                        case "D" -> 22;
                        case "C" -> 23;
                        default -> throw new UnexpectedCardAttribute(String.format("Unexpected card value %s", value));
                    };

                    if (uniqueCheck(value, kind)) {
                        cards[index][0] = value;
                        cards[index][1] = kind;
                    }
                } else {
                    throw new CardAttributeException(
                            String.format("The card should contain 2 attributes, but it contains %s", card.length));
                }
            }

            sortByValue(cards);
        } else {
            throw new HandCardAmountException(
                    String.format("There must be exactly 5 cards in the hand, but there are %s", strCards.length));
        }
    }

    private boolean uniqueCheck(int value, int kind) {
        for (int[] card : cards) {
            if (card[0] == value && card[1] == kind) {
                throw new UniqueCardException(
                        String.format("Unique cards must be in hand, but there is a duplicate with value: %s kind: %s", value, kind));
            }
        }
        return true;
    }

    private void sortByValue(int[][] cards) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }

        PokerHand pokerHand = (PokerHand) o;
        return Arrays.deepEquals(getCards(), pokerHand.getCards()) && getCombination() == pokerHand.getCombination();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getCombination());
        result = 31 * result + Arrays.deepHashCode(getCards());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hand: ");
        sb.append(combination).append(", ");
        sb.append("Cards: ");
        for (int[] card : cards) {
            if (card[0] < 10) {
                sb.append(card[0]);
            } else {
                String strValue = switch (card[0]) {
                    case 14 -> "A";
                    case 13 -> "K";
                    case 12 -> "Q";
                    case 11 -> "J";
                    case 10 -> "T";
                    default -> throw new UnexpectedCardAttribute(String.format("Unexpected card value %s", card[0]));
                };

                sb.append(strValue);
            }

            String kind = switch (card[1]) {
                case 20 -> "S";
                case 21 -> "H";
                case 22 -> "D";
                case 23 -> "C";
                default -> throw new UnexpectedCardAttribute(String.format("Unexpected card kind %s", card[0]));
            };

            sb.append(kind).append(" ");
        }
        return sb.toString().trim();
    }

    private interface HandValueValidator extends Function<PokerHand, HandValue> {
        static HandValueValidator isRoyalFlush() {
            return hand -> stream(hand.getCards())
                    .allMatch(card -> card[1] == hand.getCards()[0][1])
                    && stream(hand.getCards()).mapToInt(card -> card[0])
                    .sum() == 60 ? ROYAL_FLUSH : HIGH_CARD;
        }

        static HandValueValidator isStraightFlush() {
            return hand -> {
                boolean isOneKind = stream(hand.getCards())
                        .allMatch(card -> card[1] == hand.getCards()[0][1]);
                if (isOneKind) {
                    int lastCardValue = hand.getCards()[hand.getCards().length - 1][0];
                    int firstCardValue = hand.getCards()[0][0];

                    return lastCardValue - firstCardValue < 5 ? STRAIGHT_FLUSH : HIGH_CARD;
                } else {
                    return HIGH_CARD;
                }
            };
        }

        static HandValueValidator isFourOfAKind() {
            return hand -> {
                Map<Object, Long> map = stream(hand.getCards())
                        .collect(groupingBy(card -> card[0], counting()));
                for (Object key : map.keySet()) {
                    if (map.get(key) == 4L) {
                        return FOUR_OF_A_KIND;
                    }
                }
                return HIGH_CARD;
            };
        }

        static HandValueValidator isFullHouse() {
            return hand -> stream(hand.getCards())
                    .collect(groupingBy(card -> card[0], counting()))
                    .keySet()
                    .size() == 2 ? HandValue.FULL_HOUSE : HandValue.HIGH_CARD;
        }

        static HandValueValidator isFlush() {
            return hand -> stream(hand.getCards())
                    .allMatch(card -> card[1] == hand.getCards()[0][1]) ? FLUSH : HIGH_CARD;
        }

        static HandValueValidator isStraight() {
            return hand -> {
                int lastCardValue = hand.getCards()[hand.getCards().length - 1][0];
                int firstCardValue = hand.getCards()[0][0];

                return lastCardValue - firstCardValue < 5 ? STRAIGHT : HIGH_CARD;
            };
        }

        static HandValueValidator isThreeOfAKind() {
            return hand -> {
                Map<Object, Long> map = stream(hand.getCards())
                        .collect(groupingBy(card -> card[0], counting()));
                for (Object key : map.keySet()) {
                    if (map.get(key) == 3) {
                        return THREE_OF_A_KIND;
                    }
                }
                return HIGH_CARD;
            };
        }

        static HandValueValidator isTwoPair() {
            return pokerHand -> {
                Map<Object, Long> map = stream(pokerHand.getCards())
                        .collect(groupingBy(card -> card[0], counting()));

                int counter = 0;
                for (Object key : map.keySet()) {
                    if (Objects.equals(map.get(key), 2L)) {
                        counter++;
                    }
                }

                return counter == 2 ? TWO_PAIRS : HIGH_CARD;
            };
        }

        static HandValueValidator isOnePair() {
            return pokerHand -> {
                Map<Object, Long> map = stream(pokerHand.getCards())
                        .collect(Collectors.groupingBy(card -> card[0], Collectors.counting()));
                for (Object key : map.keySet()) {
                    if (map.get(key) > 1) {
                        return PAIR;
                    }
                }
                return HIGH_CARD;
            };
        }

        default HandValueValidator or(HandValueValidator other) {
            return pokerHand -> {
                HandValue value = this.apply(pokerHand);
                return value.equals(HIGH_CARD) ? other.apply(pokerHand) : value;
            };
        }
    }

    public enum HandValue {
        HIGH_CARD,
        PAIR,
        TWO_PAIRS,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }
}