package com.soddik.dto;

import com.soddik.exception.CardAttributeException;
import com.soddik.exception.HandCardAmountException;

import java.util.*;
import java.util.function.Function;

import static com.soddik.dto.PokerHand.Combination.*;
import static com.soddik.exception.CardAttributeException.UnexpectedCardAttributeKindException;
import static com.soddik.exception.CardAttributeException.UnexpectedCardAttributeValueException;
import static com.soddik.exception.HandCardAmountException.UniqueCardException;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


public final class PokerHand implements Comparable<PokerHand> {
    private final Integer[][] cards = new Integer[5][2];
    private final List<Integer> nonCombinationCards = new ArrayList<>();
    private final Combination combination;
    private Integer combinationValue = 0;

    public PokerHand(String hand) {
        parseString(hand);
        this.combination = CombinationValidator
                .isStraightOrFlush()
                .or(CombinationValidator.isFourOfAKind())
                .or(CombinationValidator.isFullHouse())
                .or(CombinationValidator.isThreeOfAKind())
                .or(CombinationValidator.isPairs())
                .or(CombinationValidator.highCard())
                .apply(this);
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

    private void parseString(String string) {
        String[] strCards = string.split(" ");
        if (strCards.length == 5) {
            createHandAndSort(strCards);
        } else {
            throw new HandCardAmountException(
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

            uniquenessOfCardsInHandCheck(value, kind);
            return new Integer[]{value, kind};
        } else {
            throw new CardAttributeException(
                    String.format("The card should contain 2 attributes, but it contains %s", card.length));
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

    private void uniquenessOfCardsInHandCheck(int value, int kind) {
        for (Integer[] card : cards) {
            if (card[0] != null && card[1] != null) {
                if (card[0] == value && card[1] == kind) {
                    throw new UniqueCardException(
                            String.format("Unique cards must be in hand, but there is a duplicate with value: %s kind: %s", value, kind));
                }
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

    private void addNonCombinationCard(Integer value) {
        nonCombinationCards.add(value);
    }

    @Override
    public int compareTo(PokerHand hand) {
        return hand.combination.ordinal() - this.combination.ordinal() != 0
                ? hand.combination.ordinal() - this.combination.ordinal()
                : compareByCombinationValue(hand);
    }

    private int compareByCombinationValue(PokerHand hand) {
        return hand.combinationValue - this.combinationValue != 0
                ? hand.combinationValue - this.combinationValue
                : compareNonCombinationCards(hand);
    }

    private int compareNonCombinationCards(PokerHand hand) {
        return getSumOfNonCombinationCards(hand) > getSumOfNonCombinationCards(this) ? 1 : -1;
    }

    private int getSumOfNonCombinationCards(PokerHand hand) {
        return hand.getNonCombinationCards()
                .stream()
                .mapToInt(value -> value)
                .sum();
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
                        throw new UnexpectedCardAttributeKindException(String.format("Unexpected card kind %s", card[0]));
            };

            sb.append(kind).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    private interface CombinationValidator extends Function<PokerHand, Combination> {
        static CombinationValidator isStraightOrFlush() {
            return hand -> {
                boolean isOneKind = stream(hand.cards)
                        .allMatch(card -> Objects.equals(card[1], hand.cards[0][1]));
                boolean isStraight = hand.cards[hand.cards.length - 1][0] - hand.cards[0][0] < 5;

                if (isOneKind && isStraight) {
                    hand.combinationValue = hand.cards[hand.cards.length - 1][0];
                    return stream(hand.cards)
                            .mapToInt(card -> card[0])
                            .sum() == 60 ? ROYAL_FLUSH : STRAIGHT_FLUSH;
                } else if (isOneKind) {
                    hand.combinationValue = stream(hand.cards).mapToInt(card -> card[0]).sum();
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
                Map<Integer, Long> map = countKinds(hand);
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
                Map<Integer, Long> map = countKinds(hand);
                boolean isFullHouse = map.keySet().size() == 2;
                if (isFullHouse) {
                    hand.combinationValue = map.keySet().stream()
                            .mapToInt(i -> i)
                            .sum();
                }
                return isFullHouse ? FULL_HOUSE : UNKNOWN;
            };
        }

        static CombinationValidator isThreeOfAKind() {
            return hand -> {
                Map<Integer, Long> map = countKinds(hand);
                long limit = 3;
                boolean isThreeOfAKind = map.entrySet()
                        .stream()
                        .anyMatch(entry -> entry.getValue() == limit);
                if (isThreeOfAKind) {
                    addNonCombinationCardAndSetValue(hand, map, limit);
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
                        addNonCombinationCardAndSetValue(hand, map, limit);
                        return PAIR;
                    }
                    case 2 -> {
                        addNonCombinationCardAndSetValue(hand, map, limit);
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

        static Map<Integer, Long> countKinds(PokerHand hand) {
            return stream(hand.cards)
                    .collect(groupingBy(card -> card[0], counting()));
        }

        static void addNonCombinationCardAndSetValue(PokerHand hand, Map<Integer, Long> map, long limit) {
            setCombinationValue(hand, map, limit);
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

    public enum Combination {
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