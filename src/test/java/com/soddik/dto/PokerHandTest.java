package com.soddik.dto;

import com.soddik.exception.CardAttributeException;
import com.soddik.exception.CardAttributeException.UnexpectedCardAttributeKindException;
import com.soddik.exception.CardAttributeException.UnexpectedCardAttributeValueException;
import com.soddik.exception.HandCardAmountException;
import com.soddik.exception.HandCardAmountException.UniqueCardException;
import com.soddik.generator.DeckGenerator;
import com.soddik.generator.HandGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.soddik.dto.PokerHand.HandValue.*;

class PokerHandTest {
    private static List<PokerHand> hands;

    @BeforeEach
    void setUp() {
        hands = new ArrayList<>();
    }

    @Test
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
        hands.forEach(System.out::println);
    }

    @Test
    void sortAllWithDuplicates() {
        PokerHand straightFlush = new PokerHand("9C JC KC QC TC");
        PokerHand straightFlush1 = new PokerHand("9S JS 8S QS TS");
        PokerHand four = new PokerHand("JC JS JD JH 6C");
        PokerHand four1 = new PokerHand("QC QS QD QH 7C");
        PokerHand fullHouse = new PokerHand("JC JS JD 6H 6C");
        PokerHand fullHouse1 = new PokerHand("QC QS QD 7H 7C");
        PokerHand flush = new PokerHand("AC 2C KC QC TC");
        PokerHand flush1 = new PokerHand("AS 3S KS QS TS");
        PokerHand straight = new PokerHand("3S 2C 4H 5C 6D");
        PokerHand straight1 = new PokerHand("3S 4C 5C 6S 7S");
        PokerHand three = new PokerHand("JC JS JD 3H 6C");
        PokerHand three2 = new PokerHand("JC JS JD 3H 7C");
        PokerHand two = new PokerHand("JC JS 3D 3H 6C");
        PokerHand two1 = new PokerHand("QC QS 4D 4H 6C");
        PokerHand two2 = new PokerHand("QD QH 4S 4C 7C");
        PokerHand pair = new PokerHand("JC JS 4D 3H 6C");
        PokerHand pair2 = new PokerHand("JC JS 5D 2H 6C");
        PokerHand high = new PokerHand("AC QS TD 3H 6C");
        PokerHand high1 = new PokerHand("KC QC TS 4H 5C");

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

        hands.add(pair);
        hands.add(pair2);
        hands.add(high);
        hands.add(high1);

        Collections.shuffle(hands);

        hands.sort(PokerHand::compareTo);

        Assertions.assertEquals(STRAIGHT_FLUSH, hands.get(0).getCombination());
        Assertions.assertEquals(STRAIGHT_FLUSH, hands.get(1).getCombination());


        Assertions.assertEquals(FOUR_OF_A_KIND, hands.get(2).getCombination());
        Assertions.assertEquals(FOUR_OF_A_KIND, hands.get(3).getCombination());

        Assertions.assertEquals(FULL_HOUSE, hands.get(4).getCombination());
        Assertions.assertEquals(FULL_HOUSE, hands.get(5).getCombination());

        Assertions.assertEquals(FLUSH, hands.get(6).getCombination());
        Assertions.assertEquals(FLUSH, hands.get(7).getCombination());

        Assertions.assertEquals(STRAIGHT, hands.get(8).getCombination());
        Assertions.assertEquals(STRAIGHT, hands.get(9).getCombination());

        Assertions.assertEquals(THREE_OF_A_KIND, hands.get(10).getCombination());
        Assertions.assertEquals(THREE_OF_A_KIND, hands.get(11).getCombination());

        Assertions.assertEquals(TWO_PAIRS, hands.get(12).getCombination());
        Assertions.assertEquals(TWO_PAIRS, hands.get(13).getCombination());
        Assertions.assertEquals(TWO_PAIRS, hands.get(14).getCombination());

        Assertions.assertEquals(PAIR, hands.get(15).getCombination());
        Assertions.assertEquals(PAIR, hands.get(16).getCombination());

        Assertions.assertEquals(HIGH_CARD, hands.get(17).getCombination());
        Assertions.assertEquals(HIGH_CARD, hands.get(18).getCombination());
    }


    @Test
    void checkRoyalFlush() {
        PokerHand royalFlush = new PokerHand("AC JC KC QC TC");
        PokerHand royalFlushHigher = new PokerHand("AS JS KS QS TS");

        hands.add(royalFlush);
        hands.add(royalFlushHigher);

        Assertions.assertEquals(ROYAL_FLUSH, royalFlush.getCombination());
        Assertions.assertEquals(ROYAL_FLUSH, royalFlushHigher.getCombination());
        Assertions.assertEquals(royalFlush.getCombinationValue(), royalFlushHigher.getCombinationValue());
    }

    @Test
    void checkStraightFlush() {
        PokerHand straightFlush = new PokerHand("9C JC KC QC TC");
        PokerHand straightFlushLower = new PokerHand("9S JS 8S QS TS");

        hands.add(straightFlush);
        hands.add(straightFlushLower);

        Assertions.assertEquals(STRAIGHT_FLUSH, straightFlush.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, straightFlushLower.getCombination());
        Assertions.assertTrue(straightFlush.getCombinationValue() > straightFlushLower.getCombinationValue());
    }

    @Test
    void checkFour() {
        PokerHand four = new PokerHand("JC JS JD JH 6C");//+
        PokerHand fourHigher = new PokerHand("QC QS QD QH 7C");//+

        hands.add(four);
        hands.add(fourHigher);

        Assertions.assertEquals(FOUR_OF_A_KIND, four.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, fourHigher.getCombination());
        Assertions.assertTrue(four.getCombinationValue() < fourHigher.getCombinationValue());
    }

    @Test
    void checkFullHouse() {
        PokerHand fullHouse = new PokerHand("JC JS JD 6H 6C");
        PokerHand fullHouseHigher = new PokerHand("QC QS QD 7H 7C");

        hands.add(fullHouse);
        hands.add(fullHouseHigher);

        Assertions.assertEquals(FULL_HOUSE, fullHouse.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, fullHouseHigher.getCombination());
        Assertions.assertTrue(fullHouse.getCombinationValue() < fullHouseHigher.getCombinationValue());
    }

    @Test
    void checkFlush() {
        PokerHand flush = new PokerHand("AC 2C KC QC TC");
        PokerHand flushHigher = new PokerHand("AS 3S KS QS TS");

        hands.add(flush);
        hands.add(flushHigher);

        Assertions.assertEquals(FLUSH, flush.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, flushHigher.getCombination());
        Assertions.assertTrue(flush.getCombinationValue() < flushHigher.getCombinationValue());
    }

    @Test
    void checkStraight() {
        PokerHand straight = new PokerHand("3S 2C 4H 5C 6D");
        PokerHand straightHigher = new PokerHand("3S 4C 5C 6S 7S");

        hands.add(straight);
        hands.add(straightHigher);

        Assertions.assertEquals(STRAIGHT, straight.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, straightHigher.getCombination());
        Assertions.assertTrue(straight.getCombinationValue() < straightHigher.getCombinationValue());
    }

    @Test
    void checkThree() {
        PokerHand three = new PokerHand("JC JS JD 3H 6C");
        PokerHand threeHigh = new PokerHand("JC JS JD 3H 7C");

        hands.add(three);
        hands.add(threeHigh);

        Assertions.assertEquals(THREE_OF_A_KIND, three.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, threeHigh.getCombination());
        Assertions.assertEquals(1, three.compareTo(threeHigh));
    }

    @Test
    void checkTwoPair() {
        PokerHand two = new PokerHand("JC JS 3D 3H 6C");
        PokerHand twoAvg = new PokerHand("QC QS 4D 4H 6C");
        PokerHand twoHigh = new PokerHand("QD QH 4S 4C 7C");

        hands.add(two);
        hands.add(twoAvg);
        hands.add(twoHigh);

        Assertions.assertEquals(TWO_PAIRS, two.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, twoAvg.getCombination());
        Assertions.assertTrue(two.getCombinationValue() < twoAvg.getCombinationValue());
        Assertions.assertEquals(1, twoAvg.compareTo(twoHigh));
    }

    @Test
    void checkPair() {
        PokerHand pair = new PokerHand("JC JS 4D 2H 6C");
        PokerHand pairHigh = new PokerHand("JC JS 4D 3H 6C");

        hands.add(pair);
        hands.add(pairHigh);

        Assertions.assertEquals(PAIR, pairHigh.getCombination());
        Assertions.assertNotEquals(HIGH_CARD, pair.getCombination());
        Assertions.assertEquals(1, pair.compareTo(pairHigh));
    }

    @Test
    void checkHigh() {
        PokerHand high = new PokerHand("AC QS TD 3H 6C");
        PokerHand lowerHigh = new PokerHand("KC QC TS 4H 5C");

        hands.add(high);
        hands.add(lowerHigh);

        Assertions.assertEquals(HIGH_CARD, high.getCombination());
        Assertions.assertNotEquals(UNKNOWN, lowerHigh.getCombination());
        Assertions.assertTrue(high.getCombinationValue() > lowerHigh.getCombinationValue());
    }

    @Test
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
    void stressTest() {
        System.out.println("START: " + LocalDateTime.now());
        for (int index = 0; index < 1_000_000; index++) {
            DeckGenerator deckGenerator = new DeckGenerator();
            HandGenerator handGenerator = new HandGenerator(deckGenerator);
            List<PokerHand> hands = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                hands.add(handGenerator.generateHand());
            }
            Collections.shuffle(hands);
            hands.sort(PokerHand::compareTo);
        }

        System.out.println("END: " + LocalDateTime.now());
    }
}