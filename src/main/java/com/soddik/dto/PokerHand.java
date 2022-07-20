package com.soddik.dto;

import com.soddik.exception.CardAttributeException;
import com.soddik.exception.HandCardAmountException;
import com.soddik.exception.UnexpectedCardAttribute;
import com.soddik.exception.UniqueCardException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.soddik.dto.PokerHand.HandValue.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


public class PokerHand implements Comparable<PokerHand> {
    private final Integer[][] cards = new Integer[5][2];
    private final List<Integer> nonCombinationCards = new ArrayList<>();

    private final HandValue combination;
    private Integer combinationValue = 0;

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
                .or(HandValueValidator.highCard())
                .apply(this);

        nonCombinationCards.sort(Integer::compareTo);
    }

    public Integer[][] getCards() {
        return cards.clone();
    }

    public HandValue getCombination() {
        return combination;
    }

    public void addNonCombinationCard(Integer value) {
        nonCombinationCards.add(value);
    }

    public List<Integer> getNonCombinationCards() {
        return new ArrayList<>(nonCombinationCards);
    }

    public Integer getCombinationValue() {
        return combinationValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        PokerHand pokerHand = (PokerHand) o;
        return Arrays.deepEquals(getCards(), pokerHand.getCards())
                && Objects.equals(getNonCombinationCards(), pokerHand.getNonCombinationCards())
                && getCombination() == pokerHand.getCombination()
                && Objects.equals(getCombinationValue(), pokerHand.getCombinationValue());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getNonCombinationCards(), getCombination(), getCombinationValue());
        result = 31 * result + Arrays.deepHashCode(getCards());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hand: ");
        sb.append(combination).append(", ");
        sb.append("Cards: ");
        for (Integer[] card : cards) {
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

    @Override
    public int compareTo(PokerHand hand) {
        int result = hand.combination.ordinal() - this.combination.ordinal();

        if (result == 0) {
            result = deepCombinationCheck(hand);
        }

        return result;
    }

    private int deepCombinationCheck(PokerHand hand) {
        int result = hand.combinationValue - this.combinationValue;
        if (result == 0) {
            result = calcDiff(hand);
        }
        return result;
    }

    private int calcDiff(PokerHand hand) {
        int handMax = getSum(hand);

        int thisMax = getSum(this);

        return handMax - thisMax;
    }

    private int getSum(PokerHand hand) {
        return hand.getNonCombinationCards()
                .stream()
                .mapToInt(value -> value)
                .sum();
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

                    uniqueCheck(value, kind);

                    cards[index][0] = value;
                    cards[index][1] = kind;
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

    private Integer validateValue(String value) {
        if (value.matches("[2-9]")) {
            int num = Integer.parseInt(value);
            if (num > 1 && num < 10) {
                return num;
            }
        }

        throw new UnexpectedCardAttribute(String.format("Unexpected card value %s", value));
    }


    private void uniqueCheck(int value, int kind) {
        for (Integer[] card : cards) {
            if (card[0] != null && card[1] != null) {
                if (card[0] == value && card[1] == kind) {
                    throw new UniqueCardException(
                            String.format("Unique cards must be in hand, but there is a duplicate with value: %s kind: %s", value, kind));
                }
            }
        }
    }

    private void sortByValue(Integer[][] cards) {
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

    private interface HandValueValidator extends Function<PokerHand, HandValue> {
        static HandValueValidator isRoyalFlush() {
            return hand -> stream(hand.cards)
                    .allMatch(card -> Objects.equals(card[1], hand.cards[0][1]))
                    && stream(hand.cards)
                    .mapToInt(card -> card[0])
                    .sum() == 60 ? ROYAL_FLUSH : UNKNOWN;
        }

        static HandValueValidator isStraightFlush() {
            return hand -> {
                boolean isOneKind = stream(hand.getCards())
                        .allMatch(card -> Objects.equals(card[1], hand.getCards()[0][1]));
                if (isOneKind) {
                    int lastCardValue = hand.cards[hand.cards.length - 1][0];
                    int firstCardValue = hand.cards[0][0];
                    if (lastCardValue - firstCardValue < 5) {
                        hand.combinationValue = lastCardValue;
                        return STRAIGHT_FLUSH;
                    } else {
                        return UNKNOWN;
                    }
                } else {
                    return UNKNOWN;
                }
            };
        }

        static HandValueValidator isFourOfAKind() {
            return hand -> {
                Map<Integer, Long> map = stream(hand.getCards())
                        .collect(groupingBy(card -> card[0], counting()));

                boolean isFourOfAKind = map.entrySet()
                        .stream()
                        .anyMatch(entry -> entry.getValue() == 4L);

                if (isFourOfAKind) {
                    hand.combinationValue = map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() == 4L)
                            .mapToInt(Map.Entry::getKey)
                            .max()
                            .getAsInt();
                }

                return isFourOfAKind ? FOUR_OF_A_KIND : UNKNOWN;
            };
        }

        static HandValueValidator isFullHouse() {
            return hand -> {
                Map<Integer, Long> map = stream(hand.cards)
                        .collect(groupingBy(card -> card[0], counting()));
                boolean isFullHouse = map.keySet().size() == 2;
                if (isFullHouse) {
                    hand.combinationValue = map.keySet().stream()
                            .mapToInt(i -> i)
                            .sum();
                }
                return isFullHouse ? FULL_HOUSE : UNKNOWN;
            };
        }

        static HandValueValidator isFlush() {
            return hand -> {
                boolean isFlush = stream(hand.cards)
                        .allMatch(card -> Objects.equals(card[1], hand.cards[0][1]));
                if (isFlush) {
                    hand.combinationValue = stream(hand.cards).mapToInt(card -> card[0]).sum();
                }
                return isFlush ? FLUSH : UNKNOWN;
            };
        }

        static HandValueValidator isStraight() {
            return hand -> {
                int lastCardValue = hand.getCards()[hand.getCards().length - 1][0];
                int firstCardValue = hand.getCards()[0][0];
                boolean isStraight = lastCardValue - firstCardValue < 5;
                if (isStraight) {
                    hand.combinationValue = lastCardValue;
                }
                return isStraight ? STRAIGHT : UNKNOWN;
            };
        }

        static HandValueValidator isThreeOfAKind() {
            return hand -> {
                Map<Integer, Long> map = stream(hand.cards)
                        .collect(groupingBy(card -> card[0], counting()));
                boolean isThreeOfAKind = map.entrySet()
                        .stream()
                        .anyMatch(entry -> entry.getValue() == 3);

                if (isThreeOfAKind) {
                    hand.combinationValue = map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() == 3)
                            .mapToInt(Map.Entry::getKey)
                            .max()
                            .getAsInt();

                    map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() != 3)
                            .mapToInt(Map.Entry::getKey)
                            .forEach(hand::addNonCombinationCard);
                }
                return isThreeOfAKind ? THREE_OF_A_KIND : UNKNOWN;
            };
        }

        static HandValueValidator isTwoPair() {
            return hand -> {
                Map<Integer, Long> map = stream(hand.getCards())
                        .collect(groupingBy(card -> card[0], counting()));
                int counter = (int) map.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() == 2L)
                        .count();
                boolean isTwoPair = counter == 2;

                if (isTwoPair) {
                    hand.combinationValue = map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() == 2L)
                            .mapToInt(Map.Entry::getKey)
                            .max()
                            .getAsInt();

                    map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() != 2)
                            .mapToInt(Map.Entry::getKey)
                            .forEach(hand::addNonCombinationCard);
                }
                return isTwoPair ? TWO_PAIRS : UNKNOWN;
            };
        }

        static HandValueValidator isOnePair() {
            return hand -> {
                Map<Integer, Long> map = stream(hand.cards)
                        .collect(Collectors.groupingBy(card -> card[0], Collectors.counting()));
                boolean isPair = map.entrySet().stream().anyMatch(entry -> entry.getValue() > 1);
                if (isPair) {
                    hand.combinationValue = map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() == 2L)
                            .mapToInt(Map.Entry::getKey)
                            .max()
                            .getAsInt();

                    map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() != 2)
                            .mapToInt(Map.Entry::getKey)
                            .forEach(hand::addNonCombinationCard);
                }
                return isPair ? PAIR : UNKNOWN;
            };
        }

        static HandValueValidator highCard() {
            return hand -> {
                hand.combinationValue = stream(hand.getCards()).mapToInt(card -> card[0])
                        .max()
                        .getAsInt();
                return HIGH_CARD;
            };

        }

        default HandValueValidator or(HandValueValidator other) {
            return hand -> {
                HandValue value = this.apply(hand);
                return value.equals(UNKNOWN) ? other.apply(hand) : value;
            };
        }
    }

    public enum HandValue {
        UNKNOWN,
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