//Andrea De La Cruz
//Lab 6  Poker3
//3/24/2020
//COSC 1174
public class Deck {
	protected int numDecks = 1;		//stores how many decks will be included in this deck
	protected int [] deck;			//the array that holds values 1-13 (4 times per numDecks)
	protected int count = 0;		//counter that keeps track of which card to pull (top of deck)
	
	protected static int[] payAMT = new int[10];				// 0 = loss // 1 = Pair Jack+ // 2 = 2 Pair // 3 = 3 of Kind // 4 = Straight
	protected static String[] handRank = new  String[10];	// 5 = Flush // 6 = Full House // 7 = 4 of Kind // 8 = Straight Flush // 9 = Royal Flush

	//default constructor that creates a deck with cards 1-54
	Deck() {
		this(1);
	}
	
	//argument constructor that creates a deck of a selected size
	Deck(int num) {
		
		//sets the size of the array deck
		this.numDecks = num;
		deck = new int[54 * num];
		
		//creates a deck of cards based on amount of decks chosen
		for(int i = 0; i < deck.length; i++) { deck[i] = (i % 54) + 1;}
		
		//sets all values of the payouts
		payAMT[0] = 0;
		payAMT[1] = 1;
		payAMT[2] = 2;
		payAMT[3] = 3;
		payAMT[4] = 4;
		payAMT[5] = 6;
		payAMT[6] = 9;
		payAMT[7] = 25;
		payAMT[8] = 50;
		payAMT[9] = 250;
		
		//sets all the Names of the ranked Hands
		handRank[0] = "Sorry, No Winner";
		handRank[1] = "Pair J+ Wins: $";
		handRank[2] = "2 Pair Wins: $";
		handRank[3] = "3 of a Kind Wins: $";
		handRank[4] = "Straight Wins: $";
		handRank[5] = "Flush Wins: $";
		handRank[6] = "Full House Wins: $";
		handRank[7] = "4 of a Kind Wins: $";
		handRank[8] = "Straight Flush Wins: $";
		handRank[9] = "Royal Flush Wins: $";
	}
	
	//getters and setters
	public int[] getDeck() {
		return deck;
	}

	public void setDeck(int[] deck) {
		this.deck = deck;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	//shuffles deck and resets count
	public void resetDeck() {
		shuffle();
		this.count = 0;
	}
	
	//method to shuffle the deck
	public void shuffle() {
		for(int k = 0; k < 5; k++) {
			for (int i = deck.length - 1; i > 0; i--)
			{
				int j = (int)(Math.random() * (i+1));
				int temp = deck[i];
				deck[i] = deck[j];
				deck[j] = temp;
			}
		}
	}
	
	//method to reset the cards to the default order (1-52)
	public void defaultDeck() {
		for(int i = 0; i < deck.length; i++) { deck[i] = i+1;}
	}
}
