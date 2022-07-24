package com.soddik.dto;

import com.soddik.exception.CardAttributeException;
import com.soddik.exception.CardAttributeException.UnexpectedCardAttributeKindException;
import com.soddik.exception.CardAttributeException.UnexpectedCardAttributeValueException;
import com.soddik.exception.HandCardAmountException;
import com.soddik.exception.HandCardAmountException.UniqueCardException;
import com.soddik.generator.DeckGenerator;
import com.soddik.generator.HandGenerator;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.soddik.dto.PokerHand.Combination.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PokerHandTest {
    private static List<PokerHand> hands;

    @BeforeEach
    void setUp() {
        hands = new ArrayList<>();
    }

    @Test
    @Order(1)
    void sortAndPrintAll() {
        PokerHand royalFlush = new PokerHand("AC JC KC QC TC");
        PokerHand straightFlush = new PokerHand("9C JC KC QC TC");
        PokerHand four = new PokerHand("JC JS JD JH 6C");
        PokerHand fullHouse = new PokerHand("JC JS JD 6H 6C");
        PokerHand flush = new PokerHand("AC 2C KC QC TC");
        PokerHand straight = new PokerHand("3S 2C 4H 5C 6D");
        PokerHand three = new PokerHand("JC JS JD 3H 6C");
        PokerHand two = new PokerHand("JC JS 3D 3H 6C");
        PokerHand pair = new PokerHand("JC JS 4D 3H 6C");
        PokerHand high = new PokerHand("AC QS TD 3H 6C");

        hands.add(royalFlush);
        hands.add(straightFlush);
        hands.add(four);
        hands.add(fullHouse);
        hands.add(flush);
        hands.add(straight);
        hands.add(three);
        hands.add(two);
        hands.add(pair);
        hands.add(high);

        Collections.shuffle(hands);

        hands.sort(PokerHand::compareTo);

        Assertions.assertEquals(ROYAL_FLUSH, hands.get(0).getCombination());
        Assertions.assertEquals(STRAIGHT_FLUSH, hands.get(1).getCombination());
        Assertions.assertEquals(FOUR_OF_A_KIND, hands.get(2).getCombination());
        Assertions.assertEquals(FULL_HOUSE, hands.get(3).getCombination());
        Assertions.assertEquals(FLUSH, hands.get(4).getCombination());
        Assertions.assertEquals(STRAIGHT, hands.get(5).getCombination());
        Assertions.assertEquals(THREE_OF_A_KIND, hands.get(6).getCombination());
        Assertions.assertEquals(TWO_PAIRS, hands.get(7).getCombination());
        Assertions.assertEquals(PAIR, hands.get(8).getCombination());
        Assertions.assertEquals(HIGH_CARD, hands.get(9).getCombination());
        System.out.println("====Sorted====");
        hands.forEach(System.out::println);
    }

    @Test
    @Order(2)
    void sortAllWithDuplicates() {
        PokerHand royalFlush = new PokerHand("AC JC KC QC TC");
        PokerHand straightFlush = new PokerHand("9C JC KC QC TC");
        PokerHand straightFlush1 = new PokerHand("9S JS 8S QS TS");
        PokerHand four = new PokerHand("JC JS JD JH AC");
        PokerHand four1 = new PokerHand("QC QS QD QH 7C");
        PokerHand fullHouse = new PokerHand("JC JS JD AH AC");
        PokerHand fullHouse1 = new PokerHand("QC QS QD 2H 2C");
        PokerHand flush = new PokerHand("2C 3C 9C JC KC");
        PokerHand flush1 = new PokerHand("5S 6S 7S JS KS");
        PokerHand straight = new PokerHand("3S 2C 4H 5C 6D");
        PokerHand straight1 = new PokerHand("3S 4C 5C 6S 7S");
        PokerHand three = new PokerHand("JC JS JD 3H 9C");
        PokerHand three2 = new PokerHand("QC QS QD 2H 7C");
        PokerHand two = new PokerHand("JC JS 4D 4H 6C");
        PokerHand two1 = new PokerHand("QC QS 4D 4H 6C");
        PokerHand two2 = new PokerHand("QD QH 4S 4C 7C");
        PokerHand two3 = new PokerHand("3C 3S TD TH 4C");
        PokerHand two4 = new PokerHand("TD TH 2S 2C 9C");
        PokerHand pair = new PokerHand("JC JS 4D 3H 6C");
        PokerHand pair2 = new PokerHand("KC KS 4D 2H 6C");
        PokerHand pair3 = new PokerHand("7C 7S 2D 3H 5C");
        PokerHand pair4 = new PokerHand("7C 7S 4D 3H 5C");
        PokerHand high = new PokerHand("AC QS TD 3H 6C");
        PokerHand high1 = new PokerHand("KC QC TS 4H 5C");

        hands.add(royalFlush);

        hands.add(straightFlush);
        hands.add(straightFlush1);

        hands.add(four);
        hands.add(four1);

        hands.add(fullHouse);
        hands.add(fullHouse1);

        hands.add(flush);
        hands.add(flush1);

        hands.add(straight);
        hands.add(straight1);

        hands.add(three);
        hands.add(three2);

        hands.add(two);
        hands.add(two1);
        hands.add(two2);
        hands.add(two3);
        hands.add(two4);

        hands.add(pair);
        hands.add(pair2);
        hands.add(pair3);
        hands.add(pair4);

        hands.add(high);
        hands.add(high1);

        Collections.shuffle(hands);

        Collections.sort(hands);

        Assertions.assertEquals(ROYAL_FLUSH, hands.get(0).getCombination());

        Assertions.assertEquals(STRAIGHT_FLUSH, hands.get(1).getCombination());
        Assertions.assertEquals(STRAIGHT_FLUSH, hands.get(2).getCombination());

        Assertions.assertEquals(FOUR_OF_A_KIND, hands.get(3).getCombination());
        Assertions.assertEquals(FOUR_OF_A_KIND, hands.get(4).getCombination());

        Assertions.assertEquals(FULL_HOUSE, hands.get(5).getCombination());
        Assertions.assertEquals(FULL_HOUSE, hands.get(6).getCombination());

        Assertions.assertEquals(FLUSH, hands.get(7).getCombination());
        Assertions.assertEquals(FLUSH, hands.get(8).getCombination());

        Assertions.assertEquals(STRAIGHT, hands.get(9).getCombination());
        Assertions.assertEquals(STRAIGHT, hands.get(10).getCombination());

        Assertions.assertEquals(THREE_OF_A_KIND, hands.get(11).getCombination());
        Assertions.assertEquals(THREE_OF_A_KIND, hands.get(12).getCombination());

        Assertions.assertEquals(TWO_PAIRS, hands.get(13).getCombination());
        Assertions.assertEquals(TWO_PAIRS, hands.get(14).getCombination());
        Assertions.assertEquals(TWO_PAIRS, hands.get(15).getCombination());
        Assertions.assertEquals(TWO_PAIRS, hands.get(16).getCombination());
        Assertions.assertEquals(TWO_PAIRS, hands.get(17).getCombination());

        Assertions.assertEquals(PAIR, hands.get(18).getCombination());
        Assertions.assertEquals(PAIR, hands.get(19).getCombination());
        Assertions.assertEquals(PAIR, hands.get(20).getCombination());
        Assertions.assertEquals(PAIR, hands.get(21).getCombination());

        Assertions.assertEquals(HIGH_CARD, hands.get(22).getCombination());
        Assertions.assertEquals(HIGH_CARD, hands.get(23).getCombination());
        System.out.println("====Sorted====");
        hands.forEach(System.out::println);
    }


    @Test
    @Order(3)
    void checkRoyalFlush() {
        PokerHand royalFlush = new PokerHand("AC JC KC QC TC");
        PokerHand royalFlushHigher = new PokerHand("AS JS KS QS TS");

        hands.add(royalFlush);
        hands.add(royalFlushHigher);

        Assertions.assertNotEquals(UNKNOWN, royalFlush.getCombination());
        Assertions.assertEquals(ROYAL_FLUSH, royalFlush.getCombination());
        Assertions.assertEquals(ROYAL_FLUSH, royalFlushHigher.getCombination());
        Assertions.assertEquals(royalFlush.getCombinationValue(), royalFlushHigher.getCombinationValue());
    }

    @Test
    @Order(4)
    void checkStraightFlush() {
        PokerHand straightFlush = new PokerHand("9C JC KC QC TC");
        PokerHand straightFlushLower = new PokerHand("9S JS 8S QS TS");

        hands.add(straightFlush);
        hands.add(straightFlushLower);

        Assertions.assertNotEquals(UNKNOWN, straightFlush.getCombination());
        Assertions.assertEquals(STRAIGHT_FLUSH, straightFlush.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, straightFlushLower.getCombination());
        Assertions.assertEquals(1, straightFlushLower.compareTo(straightFlush));
    }

    @Test
    @Order(5)
    void checkFour() {
        PokerHand four = new PokerHand("JC JS JD JH 6C");//+
        PokerHand fourHigher = new PokerHand("QC QS QD QH 7C");//+

        hands.add(four);
        hands.add(fourHigher);

        Assertions.assertNotEquals(UNKNOWN, four.getCombination());
        Assertions.assertEquals(FOUR_OF_A_KIND, four.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, fourHigher.getCombination());
        Assertions.assertTrue(four.getCombinationValue() < fourHigher.getCombinationValue());
    }

    @Test
    @Order(6)
    void checkFullHouse() {
        PokerHand fullHouse = new PokerHand("JC JS JD 6H 6C");
        PokerHand fullHouseHigher = new PokerHand("QC QS QD 7H 7C");

        hands.add(fullHouse);
        hands.add(fullHouseHigher);

        Assertions.assertNotEquals(UNKNOWN, fullHouse.getCombination());
        Assertions.assertEquals(FULL_HOUSE, fullHouse.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, fullHouseHigher.getCombination());
        Assertions.assertTrue(fullHouse.getCombinationValue() < fullHouseHigher.getCombinationValue());
    }

    @Test
    @Order(7)
    void checkFlush() {
        PokerHand flush = new PokerHand("5S 6S 7S JS KS");
        PokerHand flushHigher = new PokerHand("2C 3C 9C JC KC");

        hands.add(flush);
        hands.add(flushHigher);

        Collections.sort(hands);
        hands.forEach(System.out::println);

        Assertions.assertNotEquals(UNKNOWN, flush.getCombination());
        Assertions.assertEquals(FLUSH, flush.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, flushHigher.getCombination());
        Assertions.assertEquals(1, flush.compareTo(flushHigher));
    }

    @Test
    @Order(8)
    void checkStraight() {
        PokerHand straight = new PokerHand("3S 2C 4H 5C 6D");
        PokerHand straightHigher = new PokerHand("3S 4C 5C 6S 7S");

        hands.add(straight);
        hands.add(straightHigher);

        Assertions.assertNotEquals(UNKNOWN, straight.getCombination());
        Assertions.assertEquals(STRAIGHT, straight.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, straightHigher.getCombination());
        Assertions.assertTrue(straight.getCombinationValue() < straightHigher.getCombinationValue());
    }

    @Test
    @Order(9)
    void checkThree() {
        PokerHand three = new PokerHand("JC JS JD 3H 6C");
        PokerHand threeHigh = new PokerHand("QC QS QD 3H 7C");

        hands.add(three);
        hands.add(threeHigh);

        Assertions.assertNotEquals(UNKNOWN, three.getCombination());
        Assertions.assertEquals(THREE_OF_A_KIND, three.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, threeHigh.getCombination());
        Assertions.assertEquals(1, three.compareTo(threeHigh));
    }

    @Test
    @Order(10)
    void checkTwoPair() {
        PokerHand two = new PokerHand("JC JS 3D 3H 6C");
        PokerHand twoHigh = new PokerHand("QC QS 4D 4H 6C");
        PokerHand twoAvg = new PokerHand("2S 2C 9C TH TD");
        PokerHand two1 = new PokerHand("3C 3S 4C TH TD");

        hands.add(two);
        hands.add(twoAvg);
        hands.add(twoHigh);
        hands.add(two1);


        Assertions.assertNotEquals(UNKNOWN, two.getCombination());
        Assertions.assertEquals(TWO_PAIRS, two.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, twoAvg.getCombination());
        Assertions.assertTrue(two.getCombinationValue() < twoHigh.getCombinationValue());
        Assertions.assertEquals(1, twoAvg.compareTo(twoHigh));
    }

    @Test
    @Order(11)
    void checkPair() {
        PokerHand pair = new PokerHand("JC JS 2D 3H 6C");
        PokerHand pairHigh = new PokerHand("JC JS 4D 3H 6C");

        hands.add(pair);
        hands.add(pairHigh);

        Assertions.assertNotEquals(UNKNOWN, pair.getCombination());
        Assertions.assertEquals(PAIR, pairHigh.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, pair.getCombination());
        Assertions.assertEquals(1, pair.compareTo(pairHigh));
    }

    @Test
    @Order(12)
    void checkHigh() {
        PokerHand high = new PokerHand("AC QS TD 3H 6C");
        PokerHand lowerHigh = new PokerHand("KC QC TS 4H 5C");

        hands.add(high);
        hands.add(lowerHigh);

        Assertions.assertNotEquals(UNKNOWN, high.getCombination());
        Assertions.assertEquals(HIGH_CARD, high.getCombination());
        Assertions.assertNotEquals(UNKNOWN, lowerHigh.getCombination());
        Assertions.assertTrue(high.getCombinationValue() > lowerHigh.getCombinationValue());
    }

    @Test
    @Order(13)
    void checkHandCardAmountException() {
        String msg = "";
        try {
            new PokerHand("AC QS TD 3H");
        } catch (HandCardAmountException e) {
            msg = e.getMessage();
        } finally {
            Assertions.assertEquals("There must be exactly 5 cards in the hand, but there are 4", msg);
        }
    }

    @Test
    @Order(14)
    void checkCardAttributeException() {
        String msg = "";
        try {
            new PokerHand("21S AS TD 3H 6C");
        } catch (CardAttributeException e) {
            msg = e.getMessage();
        } finally {
            Assertions.assertEquals("The card should contain 2 attributes, but it contains 3", msg);
        }
    }

    @Test
    @Order(15)
    void checkUnexpectedCardAttributeValueException() {
        String msg = "";
        try {
            new PokerHand("ZC QS TD 3H 6C");
        } catch (UnexpectedCardAttributeValueException e) {
            msg = e.getMessage();
        } finally {
            Assertions.assertEquals("Unexpected card value Z", msg);
        }
    }

    @Test
    @Order(16)
    void checkUnexpectedCardAttributeKindException() {
        String msg = "";
        try {
            new PokerHand("AX QS TD 3H 6C");
        } catch (UnexpectedCardAttributeKindException e) {
            msg = e.getMessage();
        } finally {
            Assertions.assertEquals("Unexpected card kind X", msg);
        }
    }

    @Test
    @Order(17)
    void checkUniqueCardException() {
        String msg = "";
        try {
            new PokerHand("AS AS TD 3H 6C");
        } catch (UniqueCardException e) {
            msg = e.getMessage();
        } finally {
            Assertions.assertEquals("Unique cards must be in hand, but there is a duplicate with value: 14 kind: 20", msg);
        }
    }

    @Test
    @Order(18)
    void checkNPE(){
        String msg = "";
        try {
            new PokerHand(null);
        } catch (NullPointerException e) {
            msg = e.getMessage();
        } finally {
            Assertions.assertEquals("Entry string cannot be null", msg);
        }
    }
}