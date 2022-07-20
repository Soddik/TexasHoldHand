# TexasHoldHand

### Application generates a deck and a hand from the deck and checks for combinations: Royal flush, Straight flush etc.

## Combination definition logic:
## Poker: Texas hold'em

### Royal flush:

1) Check kind of all cards
2) If we have ```only one kind``` of cards in hand, check the sum of the values of all cards
3) If ```sum = 60``` - Royal flush

### Straight flush

1) Check kind of all cards
2) If we have ```only one kind``` of cards in hand
3) Check the ```difference``` between the values of the ```first``` and ```last``` card, ```all cards sorted by value```
4) If ```difference < 5``` - Straight flush

### Four of a kind

1) Check kind of all cards
2) If ```4 cards``` have the ```same kind``` - Four of a kind

### Full house

1) Check kind of all cards
2) If we have ```only 2 kinds``` in hand - Full house

### Flush

1) Check kind of all cards
2) If we have ```only one kind``` of cards in hand - Flush

### Straight

1) Check the ```difference``` between the values of the ```first``` and ```last``` card, ```all cards sorted by value```

2) If ```difference < 5``` - Straight

### Three of a kind

1) Check kind of all cards
2) If ```3 cards``` have the ```same kind``` - Three of a kind

### Two pair

1) Check ```values``` of ```all cards```
2) If we have ```2 pairs``` of values - Two pair

### Pair

1) Check ```values``` of ```all cards```
2) If we have ```pair``` of values - Pair

### High card

1) If we don't have combination yet, take ```max value``` of the card in hand - High card

## Combination check hierarchy

1) Royal flush
2) Straight flush
3) Flush
4) Straight
5) Four of a kind
6) Full house
7) Three of a kind
8) Two pair
9) Pair
10) High card