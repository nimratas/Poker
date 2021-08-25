package Poker;

import java.util.*;

public class Poker {
    static String[] cards;
    static String errorMessage = "";
    static int nop = 0;
    static int[] ids;

    public static void readInput(Scanner scan) {

        String s = "";
        //number of players
        try {
            int n = scan.nextInt();
            nop = n;
            if (n < 1 || n > 7) {
                errorMessage = "Number of players can be from 1 to 7";
                System.out.println(errorMessage);
                scan.close();
                return;
            }
            cards = new String[n];
            ids = new int[n];
            int i = 0;
            int j = 0;
            while (i < n) {
                s = scan.nextLine();
                if (!s.isEmpty()) {
                    //because 1st character in the string '0 2d 4g 7s' is the jth player
                    if (!Character.isDigit(s.charAt(0))) {
                        errorMessage = "Enter index of player. Please refer to instructions for proper format of input!";
                        System.out.println(errorMessage);
                        return;
                    }
                    j = (s.charAt(0) - '0');
                    if (s.trim().length() != 10) {
                        errorMessage = "Invalid input: " + s + "\nPlease refer to instructions for proper format of input!";
                        System.out.println(errorMessage);
                        return;
                    }
                    ids[i] = j;
                    cards[i] = s.substring(2);
                    i++;
                }
            }
        } catch (InputMismatchException e) {
            errorMessage = "Number of players expected." +
                    "\nPlease refer to instructions for proper format of input!";
            System.out.println(errorMessage);
            return;
        }
        scan.close();
    }

    public static Player[] parseCards(PokerRules pr) {
        ArrayList<String[]> readCards = new ArrayList<>();
        Player[] players = new Player[nop];
        HashSet<String> setOfCards = new HashSet<>(); //to check duplicates
        if (cards != null && cards.length > 0) {
            //sanity checks : 1)number of cards per player should be 3.
            // 2)each card should be in 2d format
            String[] c = new String[3];
            for (int i = 0; i < cards.length; i++) {
                c = cards[i].split(" "); //split input into individual card
                if (c.length != 3) {
                    errorMessage = "Enter 3 cards. Please refer to instructions for format of input!";
                    System.out.println(errorMessage);
                    return null;
                }
                readCards.add(i, c); //need to maintain index/order
            }
            for (int i = 0; i < readCards.size(); i++) {
                c = readCards.get(i);

                for (String s : c) {
                    if (s.length() != 2) {
                        errorMessage = "Invalid card format: " + s +
                                "\nPlease refer to instructions for proper format of input!";
                        return null;
                    }
                    if (!Character.isDigit(s.charAt(0)) && !pr.validRank(s.charAt(0))) {
                        errorMessage = "Invalid rank value: " + s.charAt(0) +
                                "\nPlease refer to for proper format of input!";
                        System.out.println(errorMessage);
                        return null;
                    } else if (Character.isDigit(s.charAt(0)) && (s.charAt(0) - '0') <= 1 && (s.charAt(0) - '0') >= 10) {
                        errorMessage = "Invalid rank value: " + s.charAt(0) +
                                "\nPlease refer to for proper format of input!";
                        System.out.println(errorMessage);
                        return null;
                    }
                    if (!pr.validSuit(s.charAt(1))) {
                        errorMessage = "Invalid suit value: " + s +
                                "\nPlease refer to for find proper format of input!";
                        System.out.println(errorMessage);
                        return null;
                    }
                    if (setOfCards.contains(s)) {
                        errorMessage = "Duplicate cards: " + s +
                                "\nPlease refer to for proper format of input!";
                        System.out.println(errorMessage);
                        return null;
                    } else {
                        setOfCards.add(s);
                    }
                }

                Player p = new Player(ids[i], c, pr); //make player object and add it to the players[]
                players[i] = p;

            }
        }
        return players;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        readInput(scan);
        PokerRules pr;
        pr = new PokerRules();
        ArrayList<Player> winners = new ArrayList<>();
        Player[] players = null;
        if (errorMessage.isEmpty()) {
            players = parseCards(pr);
        } else {
            return;
        }
        if (players != null && players.length != 0) {
            winners = pr.findWinner(players);
        }
        if (!winners.isEmpty()) {
            //write contents of arrayList
            int[] ids = new int[winners.size()];
            int i = 0;
            for (Player p : winners) {
                ids[i] = p.id;
                i++;
            }
            Arrays.sort(ids);
            for (int id : ids) {
                System.out.print(id + " ");
            }
        }
    }
}