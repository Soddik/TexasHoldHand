package com.soddik.entity;

import com.soddik.exception.*;
import com.soddik.parser.HandParser;

import java.util.*;
import java.util.function.Function;

import static com.soddik.entity.Combination.*;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


public final class PokerHand implements Comparable<PokerHand> {
    private final Integer[][] cards;
    private final List<Integer> nonCombinationCards = new ArrayList<>();
    private final Combination combination;
    private Integer combinationValue = 0;

    public PokerHand(String hand, HandParser parser) {
        parser.parseString(hand);
        this.cards = parser.getCards();
        this.combination = CombinationValidator
                .isStraightOrFlush()
                .or(CombinationValidator.isFourOfAKind())
                .or(CombinationValidator.isFullHouse())
                .or(CombinationValidator.isThreeOfAKind())
                .or(CombinationValidator.isPairs())
                .or(CombinationValidator.highCard())
                .apply(this);

        if (nonCombinationCards.size() > 0) {
            sortNonCombinationCards();
        }
    }

    public PokerHand(Integer[][] cards) {
        this.cards = cards;
        this.combination = CombinationValidator
                .isStraightOrFlush()
                .or(CombinationValidator.isFourOfAKind())
                .or(CombinationValidator.isFullHouse())
                .or(CombinationValidator.isThreeOfAKind())
                .or(CombinationValidator.isPairs())
                .or(CombinationValidator.highCard())
                .apply(this);

        if (nonCombinationCards.size() > 0) {
            sortNonCombinationCards();
        }
    }

    public Integer[][] getCards() {
        return cards.clone();
    }

    public Combination getCombination() {
        return combination;
    }

    public List<Integer> getNonCombinationCards() {
        return List.copyOf(nonCombinationCards);
    }

    public Integer getCombinationValue() {
        return combinationValue;
    }

    private void addNonCombinationCard(Integer value) {
        nonCombinationCards.add(value);
    }

    @Override
    public int compareTo(PokerHand hand) {
        int combinationOrdinalDiff = hand.getCombination().ordinal() - this.getCombination().ordinal();
        if (combinationOrdinalDiff != 0) {
            return hand.getCombination().ordinal() - this.getCombination().ordinal() > 0 ? 1 : -1;
        } else {
            return compareByCombinationValue(hand);
        }
    }

    private int compareByCombinationValue(PokerHand hand) {
        int combinationValueDiff = hand.getCombinationValue() - this.getCombinationValue();
        if (combinationValueDiff != 0) {
            return hand.getCombinationValue() - this.getCombinationValue() > 0 ? 1 : -1;
        } else {
            return compareNonCombinationCards(hand);
        }
    }

    private int compareNonCombinationCards(PokerHand hand) {
        int result = 0;
        for (int index = hand.getNonCombinationCards().size() - 1; index >= 0; index--) {
            if (!hand.getNonCombinationCards().get(index).equals(this.getNonCombinationCards().get(index))) {
                result = hand.getNonCombinationCards().get(index) - this.getNonCombinationCards().get(index) < 0 ? -1 : 1;
                return result;
            }
        }
        return result;
    }

    private void sortNonCombinationCards() {
        nonCombinationCards.sort(Integer::compareTo);
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
        sb.append("Hand[ Combination: ");
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
                    default ->
                            throw new UnexpectedCardAttributeValueException(String.format("Unexpected card value %s", card[0]));
                };

                sb.append(strValue);
            }

            String kind = switch (card[1]) {
                case 20 -> "S";
                case 21 -> "H";
                case 22 -> "D";
                case 23 -> "C";
                default ->
                        throw new UnexpectedCardAttributeKindException(String.format("Unexpected card kind %s", card[1]));
            };

            sb.append(kind).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    private interface CombinationValidator extends Function<PokerHand, Combination> {
        static CombinationValidator isStraightOrFlush() {
            return hand -> {
                boolean isSameSuit = stream(hand.cards)
                        .allMatch(card -> Objects.equals(card[1], hand.cards[0][1]));
                boolean isStraight = hand.cards[hand.cards.length - 1][0] - hand.cards[0][0] < 5
                        && countValues(hand).keySet().size() == 5;

                if (isSameSuit && isStraight) {
                    hand.combinationValue = hand.cards[hand.cards.length - 1][0];
                    return stream(hand.cards)
                            .mapToInt(card -> card[0])
                            .sum() == 60 ? ROYAL_FLUSH : STRAIGHT_FLUSH;
                } else if (isSameSuit) {
                    hand.combinationValue = stream(hand.cards)
                            .mapToInt(card -> card[0])
                            .max()
                            .getAsInt();
                    countValues(hand).keySet().stream()
                            .filter(key -> key < hand.combinationValue)
                            .forEach(hand::addNonCombinationCard);
                    return FLUSH;
                } else if (isStraight) {
                    hand.combinationValue = hand.cards[hand.cards.length - 1][0];
                    return STRAIGHT;
                }
                return UNKNOWN;
            };
        }

        static CombinationValidator isFourOfAKind() {
            return hand -> {
                Map<Integer, Long> map = countValues(hand);
                long limit = 4;
                boolean isFourOfAKind = map.entrySet()
                        .stream()
                        .anyMatch(entry -> entry.getValue() == limit);
                if (isFourOfAKind) {
                    setCombinationValue(hand, map, limit);
                }
                return isFourOfAKind ? FOUR_OF_A_KIND : UNKNOWN;
            };
        }

        static CombinationValidator isFullHouse() {
            return hand -> {
                Map<Integer, Long> map = countValues(hand);
                boolean isFullHouse = map.keySet().size() == 2;
                if (isFullHouse) {
                    hand.combinationValue = map.entrySet().stream()
                            .filter(entry -> entry.getValue() == 3)
                            .mapToInt(Map.Entry::getKey)
                            .findFirst()
                            .getAsInt();
                }
                return isFullHouse ? FULL_HOUSE : UNKNOWN;
            };
        }

        static CombinationValidator isThreeOfAKind() {
            return hand -> {
                Map<Integer, Long> map = countValues(hand);
                long limit = 3;
                boolean isThreeOfAKind = map.entrySet()
                        .stream()
                        .anyMatch(entry -> entry.getValue() == limit);
                if (isThreeOfAKind) {
                    setCombinationValue(hand, map, limit);
                    addNonCombinationCard(hand, map, limit);
                }
                return isThreeOfAKind ? THREE_OF_A_KIND : UNKNOWN;
            };
        }

        static CombinationValidator isPairs() {
            return hand -> {
                Map<Integer, Long> map = stream(hand.getCards())
                        .collect(groupingBy(card -> card[0], counting()));
                long limit = 2;
                int counter = (int) map.entrySet()
                        .stream()
                        .filter(entry -> entry.getValue() == limit)
                        .count();

                switch (counter) {
                    case 1 -> {
                        setCombinationValue(hand, map, limit);
                        addNonCombinationCard(hand, map, limit);
                        return PAIR;
                    }
                    case 2 -> {
                        setCombinationValue(hand, map, limit);
                        map.entrySet().stream()
                                .filter(entry -> entry.getValue() == 2)
                                .mapToInt(Map.Entry::getKey)
                                .filter(key -> key < hand.combinationValue)
                                .forEach(key -> hand.nonCombinationCards.add((int) Math.pow(key, 4)));
                        map.entrySet().stream()
                                .filter(entry -> entry.getValue() != 2)
                                .mapToInt(Map.Entry::getKey)
                                .forEach(hand::addNonCombinationCard);
                        return TWO_PAIRS;
                    }
                    default -> {
                        return UNKNOWN;
                    }
                }
            };
        }

        static CombinationValidator highCard() {
            return hand -> {
                hand.combinationValue = stream(hand.getCards()).mapToInt(card -> card[0])
                        .max()
                        .getAsInt();
                return HIGH_CARD;
            };
        }

        static Map<Integer, Long> countValues(PokerHand hand) {
            return stream(hand.cards)
                    .collect(groupingBy(card -> card[0], counting()));
        }

        static void addNonCombinationCard(PokerHand hand, Map<Integer, Long> map, long limit) {
            map.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() != limit)
                    .mapToInt(Map.Entry::getKey)
                    .forEach(hand::addNonCombinationCard);
        }

        static void setCombinationValue(PokerHand hand, Map<Integer, Long> map, long limit) {
            hand.combinationValue = map.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == limit)
                    .mapToInt(Map.Entry::getKey)
                    .max()
                    .getAsInt();
        }

        default CombinationValidator or(CombinationValidator other) {
            return hand -> {
                Combination value = this.apply(hand);
                return value.equals(UNKNOWN) ? other.apply(hand) : value;
            };
        }
    }
}