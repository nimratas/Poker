package Poker;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;

public class PokerRules {

    public enum HandLevel { HIGH_CARD, PAIR, FLUSH, STRAIGHT, TOK, STRAIGHT_FLUSH };

    private HashMap<Character, Integer> rankValue = new HashMap<>();
    private HashSet<Character> suits = new HashSet<>();
    private ArrayList<Player> winners = new ArrayList<>();

    public PokerRules() {
        rankValue.put('T', 10);
        rankValue.put('J', 11);
        rankValue.put('Q', 12);
        rankValue.put('K', 13);
        rankValue.put('A', 14);
        suits.add('h');
        suits.add('d');
        suits.add('s');
        suits.add('c');
    }

    public boolean validSuit(Character c) {
        return suits.contains(c);
    }
    public boolean validRank(Character c) {
        return rankValue.containsKey(c);
    }
    public int getRank(Character c) {

        return rankValue.get(c);
    }
    public Player[] getPlayerList(Player[] players) {
       return players;
    }
    private int getScore(HandLevel handLevel) {
        switch (handLevel) {
            case STRAIGHT_FLUSH: return 5;
            case TOK: return 4;
            case STRAIGHT: return 3;
            case FLUSH: return 2;
            case PAIR: return 1;
            case HIGH_CARD: return 0;
        }
        return -1;
    }

    public ArrayList<Player> findWinner(Player[] players) {
        String[] cards;
        HandLevel handLevel = null;
        int score = -1;
        int[] scores = new int[players.length];
        int i = 0;
        int[] ranks = new int[3];
        for(Player p : players) {
            cards = p.getCards();
            if (cards != null && cards.length == 3) {
                //check for best hand
                ranks = p.getRanks();
                if (ranks.length != 0) {

                    if (hasStraightFlush(cards, ranks)) {
                        handLevel = HandLevel.STRAIGHT_FLUSH;
                    } else if (hasThreeOfKind(ranks)) {
                        handLevel = HandLevel.TOK;
                    } else if (hasStraight(ranks)) {
                        handLevel = HandLevel.STRAIGHT;
                    } else if (hasFlush(cards)) {
                        handLevel = HandLevel.FLUSH;
                    } else if (hasPair(ranks)) {
                        handLevel = HandLevel.PAIR;
                    } else {
                        handLevel = HandLevel.HIGH_CARD;
                    }
                }
                if(handLevel != null) {
                    score = getScore(handLevel);
                }
                scores[i] = score;
            } else{
                System.out.println("players.cards are empty");
            }
            i++;
        }
        winners = breakTies(players, scores);

        return winners;
    }
    public boolean hasStraightFlush(String[] cards, int[] ranks) {
        /*
        A straight flush is a hand that is both a straight and a flush
         */
        boolean a = hasFlush(cards);
        boolean b = hasStraight(ranks);

        return a && b;
    }
    public boolean hasStraight(int[] ranks) {
        /*cards are of format:
        n, n+1, n+2
         */
        return (ranks[2] == ranks[1]+1 && ranks[1] == ranks[0]+1);
    }
    public boolean hasFlush(String[] cards) {
        /*
        Same suit : Ac 4c 8c
         */
        char a = cards[0].charAt(1);
        char b = cards[1].charAt(1);
        char c = cards[2].charAt(1);
        return (a == b) && (a == c);
    }
    public boolean hasThreeOfKind(int[] ranks) {
        /*4c 4h 4d*/
        return ranks[0] == ranks[1] && ranks[0] == ranks[2];
    }

    public boolean hasPair(int[] ranks) {
        return ranks[0] == ranks[1] || ranks[0] == ranks[2] || ranks[1] == ranks[2];
    }

    public ArrayList<Player> breakTies(Player[] players, int[] scores) {
        //ties should be broken according to the maxscore
        ArrayList<Player> p = new ArrayList<>();
        //find maxScore from score[] and find which players have that score and add them to p
        int maxScore = -1;
        for (int i=0; i < scores.length; i++) {
            maxScore = Math.max(maxScore, scores[i]);
        }
        for (int i=0; i<scores.length; i++) {
            if (scores[i] == maxScore) {
                p.add(players[i]);
            }
        }
        //get tied cards with same max score
        ArrayList<Player> winners = new ArrayList<>();
        if (p.size() > 1) {
            if (maxScore == 5 || maxScore == 4 || maxScore == 3) {
                //Straight flush, Same suit/ThreeOfAKind, Straight - find 1st highest card -
                // only need to check first high card rest all will be same because they will be a run/straight
                //1stHighestCard
                Player player = null ;
                int highCard = p.get(0).firstHighCard;
                for(int i=0; i<p.size(); i++) {
                    player = p.get(i);
                    if (player.firstHighCard > highCard) {
                        winners.clear();
                        winners.add(player);
                        highCard = player.firstHighCard;
                    }
                    else if (player.firstHighCard == highCard) {
                        winners.add(player);
                    }
                }
                return winners;
            }
            else {
                //lets make a 2d array of size p.size() x 3
                //each player's ranks will look like a row in the matrix
                int[][] matrix = new int[p.size()][3];
                for(int i=0; i<p.size(); i++) {
                    matrix[i][0] = p.get(i).firstHighCard;
                    matrix[i][1] = p.get(i).secondHighCard;
                    matrix[i][2] = p.get(i).thirdHighCard;
                }
                if(maxScore != 1){
                    //maxScore == 0 || maxscore == 2

                    int highcard;
                    for (int col = 0; col < 3; col++) {
                        highcard = matrix[0][col];
                        winners.clear();
                        for(int row = 0; row < p.size(); row++) {
                            if (matrix[row][col] > highcard) {
                                winners.clear();
                                winners.add(p.get(row));
                                highcard = matrix[row][col];
                            }
                            else if( matrix[row][col] == highcard) {
                                winners.add(p.get(row));
                            }
                        }
                        if (winners.size() == 1) {
                            return winners;
                        }
                    }
                    return winners;
                }
                else {
                    //maxScore == 1 i.e a Pair
                    ArrayList<Integer> highPairRows = new ArrayList<>();
                    int highPair = 0;
                    int currentPair = 0;
                    for (int row = 0; row < p.size(); row++) {
                        //first find out pair
                        if(matrix[row][0] == matrix[row][1])  {
                            currentPair = matrix[row][0];
                        }
                        else{
                            currentPair = matrix[row][2];
                        }
                        //second: compare pair and add row number of highest pair number
                        if(currentPair > highPair) {
                            highPairRows.clear();
                            highPairRows.add(row);
                            highPair = currentPair;
                        }
                        else if(currentPair == highPair) {
                            highPairRows.add(row);
                        }
                    }
                    if(highPairRows.size() == 1) {
                        int i = highPairRows.get(0);
                        winners.add(p.get(i));
                        if (winners.size() == 1) {
                            return winners;
                        }
                    }
                    //more than one rows have same high pair
                    int nonPairCard = 0;
                    int maxCard = 0;
                    ArrayList<Integer> nonPairCards = new ArrayList<>();
                    winners.clear();
                    for(int i=0; i<highPairRows.size(); i++) {
                        if (matrix[i][0] == matrix[i][1]) {
                            nonPairCard = matrix[i][2];
                        }
                        else{
                            nonPairCard = matrix[i][0];
                        }
                        if (nonPairCard > maxCard) {

                            nonPairCards.clear();
                            nonPairCards.add(i);
                            maxCard = nonPairCard;
                        }
                        else if(nonPairCard == maxCard) {
                            nonPairCards.add(i);
                        }
                    }
                    for(int i=0; i<nonPairCards.size(); i++) {
                        winners.add(p.get(nonPairCards.get(i)));
                    }
                    return winners;
                }
            }
        }
        return p;
    }
}
