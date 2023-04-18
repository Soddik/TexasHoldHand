package com.soddik.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DeckGenerator {
    private final List<String> kinds = List.of("S", "H", "D", "C");
    private final Map<String, List<Integer>> deck = new HashMap<>();

    public DeckGenerator() {
    }

    public Map<String, List<Integer>> generateDeck() {
        for (String kind : kinds) {
            List<Integer> cards = new ArrayList<>();
            for (int index = 2; index < 15; index++) {
                cards.add(index);
            }
            deck.put(kind, cards);
        }
        return deck;
    }

    public List<String> getKinds() {
        return new ArrayList<>(kinds);
    }

    public void clearDeck(){
        deck.clear();
    }
}