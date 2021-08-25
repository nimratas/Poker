SOURCE CODE :
Source code consists of three classes: Poker.java, PokerRules.java, Player.java. These files are
in the folder named "Poker".

INSTRUCTIONS TO COMPILE & RUN :
After extracting all three source code files,
Please compile and run the code from the directory where this zip is extracted by running the
following command:
1: javac Poker/Poker.java
2: java Poker/Poker ( or, if running the run_tests script then use command
`./run_tests "java Poker/Poker"`. In this case, the run_tests script and the Poker
folder should be in the same directory.)

DESIGN:
1. The entry point of the game is in Poker.java. The main method calls readInput() method
to read the input from stdin and validate it.
There are various checks that have been placed in order to only accept valid form of inputs.
 ErrorMessages like : "Number of players can be from 1 to 7" will be shown
 if 1<= no of players < 8 is not true. There are other Error messages for other types of input checks.

2. After input passes all checks, parseCards() is called which performs other checks like valid suit,
rank etc. followed by creating all Players if all checks pass. The Player class holds 3 cards,
an id (id is the 1st character in each line after number of players) and logic to track
order of the cards held by the player.

3. After players are created the findWinner() method in class Poker_Rules is called by the main method.
Poker_Rules is the "judger" class as it contains all the rules that make a winning hand.
Poker_Rules stores handLevel or priority of each best hand as an enum object :
handLevel{HIGH_CARD, PAIR, FLUSH, STRAIGHT, THREEOFAKIND, STRAIGHT_FLUSH}.
Each enum value represents a score associated with each best hand. The findWinner() determines handLevel
score of each player and sends this score data to the breakTies() method. The breakTies() method goes
through scores of each player/hand and picks the one(s) with the highest score(s). If there is a tie
between two or more players it is resolved in this routine by going through the rules to resolve ties
based on the rules mentioned in the README provided with the assessment. This method returns a list
winners to the findWinner() method. The findWinner() then returns this list to the main method which
then writes it out.


LIMITATION:
1. The input is read in a strictly case-sensitive format. i.e 2h and As will be excepted as valid cards,
instead of, 2H and as/AS/aS.
This was done keeping in mind the explicit instructions mentioned in the README containing instructions.




