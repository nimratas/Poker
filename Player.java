package Poker;

import java.util.Arrays;

public class Player {
    int id;
    String[] cards = new String[3];
    PokerRules pr = null;
    int firstHighCard = -1;
    int secondHighCard = -1;
    int thirdHighCard = -1;
    int[] ranks = new int[3];

    public Player(int i, String[] c, PokerRules p) {
        this.id = i;
        this.cards = c;
        this.pr = p;
        ranks = helper(cards);
    }
    public int[] helper(String[] cards) {
        int[] ranks = new int[3];
        int i = 0;
        for (String s: cards) {
            if(Character.isDigit(s.charAt(0))) {
                ranks[i] = s.charAt(0)-'0';
            }
            else{
                ranks[i] = pr.getRank(s.charAt(0));
            }
            i++;
        }
        Arrays.sort(ranks);
        if(ranks[0] == 2 && ranks[1] == 3 && ranks[2] == 14) {
            ranks[0] = 1;
            ranks[1] = 2;
            ranks[2] = 3;
        }
        this.firstHighCard = ranks[2];
        this.secondHighCard = ranks[1];
        this.thirdHighCard = ranks[0];

        return ranks;
    }
    public int[] getRanks() {
        return ranks;
    }

    public String[] getCards() {
        return cards;
    }

}
