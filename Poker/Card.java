//Andrea De La Cruz
//Lab 6  Poker3
//3/24/2020
//COSC 1174
class Card {
	/***************
	 * 00 = Ace    *
	 * 10 = Jack   *
	 * 11 = Queen  *
	 * 12 = King   *
	 ***************/
	protected int value;								//asigns the actual card value 
	protected int suit;   								// 0 = spades // 1 = Hearts // 2 = Diamonds // 3 = Clubs
	protected int number;								//stores the card number
	protected static int[][] ranks = new int[14][2];	//array used to compare hands to, to ascertain what the hand is
	protected static int [][] suitCount = new int[5][2];//array used to compare suits to
	protected boolean discard = true;					//keeps track of if the card will be discarded on draw
	protected static int rank;							//used to return the rank(pair,straight,flush) of the hand

	public void assignCard(int newCard) {
		number = newCard - 1;						//gets the card number
		value = number % 13;						//assigns the card value
		if (value == 0 && number == 52) value = 13; //sets joker to 13
		if (value == 1 && number == 53) value = 13; //sets joker to 13
		suit = number / 13; 						//assigns the card suit
	}

	/*
	 * CHECKRANK METHOD ACTIONS:
	 * 	Creates a 2 dimensional array with 14 rows and 2 columns. The first column is set to the values of the cards 0-13 (0 is Ace, 12 is King, and 13 is Joker)
	 * 	The second column is set to 0 at first.
	 * 	A loop is then run to compare the 14 values in the first column to the values of the 5 cards in the player's hand.
	 * 	If a value matches, the column in that row is incremented by 1... indicating that there is one of those cards in the player's hand.
	 * 	Example: if the loop is at value 0(Ace) and it finds a 0(Ace) in the player's hand, then ranks[0][1] is incremented by 1
	 * 	After the ranks array is ran through the loop to find out what cards are in the player's hand, it and the suit variable are used to see if the hand is a winner.
	 * 	If a winning hand is found, the rank variable is updated and it continues with more checks. At the end of the checks, rank is returned.
	 */
	public static int checkRank(Card [] cards) {

		//sets the first column to 0-13 and the second column to 0
		for (int i = 0; i<14; i++) {
			ranks[i][1] = 0;
			ranks[i][0] = i;
		}

		//sets up the suit array 0-3, and 0
		for (int i = 0; i<5; i++) {
			suitCount[i][1] = 0;
			suitCount[i][0] = i;
		}

		//compares the ranks array with player's hand
		for (int i = 0; i < 5; i++) {
			System.out.print(cards[i].value + " ");
			for(int j = 0; j<14;j++) {
				if(cards[i].value == ranks[j][0]) {
					ranks[j][1]++;
				}
			}
			if (i == 4)
				System.out.println();	
		}

		/*********************************************************************************************
		 *          IF THERE IS     1  JOKER 
		 **********************************************************************************************/

		if(ranks[13][1] == 1) {

			/**********************************************************
			 * CHECKS FOR PAIRS, THREE/FOUR OF A KIND, AND FULL HOUSE *
			 **********************************************************/
			int check4 = 0;
			int check3 = 0;
			int check2 = 0;
			boolean checkOne = true;

			for(int i = 0; i<13; i++) {
				if(ranks[i][1] > 1) {
					checkOne = false;
				}
				if(ranks[i][1] == 4) {
					check4++;
				}
				if(ranks[i][1] == 3) {
					check3++;
				}
				if(ranks[i][1] == 2) {
					check2++;
				}
			}

			if (checkOne == true && (ranks[0][1] == 1 || ranks[10][1] == 1 || ranks[11][1] == 1 || ranks[12][1] == 1)) rank = 1; // pair
			if (check2 == 1) rank = 3;					// 3 of kind
			if (check2 == 2) rank = 6;					// fullhouse
			if (check3 == 1 || check4 == 1) rank = 7;	// 4 of kind

			/**********************
			 * CHECKS FOR A FLUSH *
			 **********************/
			boolean flush = false;
			for(int i = 0; i<5; i++) {
				suitCount[cards[i].suit][1]++;
			}
			for(int i = 0; i<4; i++) {
				if (suitCount[i][1] >= 4) {
					flush = true;
					rank = 5;	//flush
				}
			}

			/*****************************************
			 * CHECKS FOR A STRAIGHT/ROYAL STRAIGHT  *
			 *****************************************/
			boolean royalStraight = false;
			boolean straight = false;
			int straightCount = 0;
			int cardCount = 0;
			int trigger;
			if (checkOne == true) {
				for(int i = 0; i < 13; i++) {
					if((ranks[ 0][1] == 1 && ranks[10][1] == 1 && ranks[11][1] == 1 && ranks[12][1] == 1) || 
					   (ranks[ 0][1] == 1 && ranks[ 9][1] == 1 && ranks[11][1] == 1 && ranks[12][1] == 1) ||
					   (ranks[ 0][1] == 1 && ranks[ 9][1] == 1 && ranks[10][1] == 1 && ranks[12][1] == 1) ||
					   (ranks[ 0][1] == 1 && ranks[ 9][1] == 1 && ranks[10][1] == 1 && ranks[11][1] == 1) ||
					   (ranks[ 9][1] == 1 && ranks[10][1] == 1 && ranks[11][1] == 1 && ranks[12][1] == 1) )
						{
							royalStraight = true;
							rank = 4;
						}
					
					else if(ranks[i][1] == 1) {
						trigger = i;
						for(int j = trigger; j<13; j++) {
							if(cardCount == 4) break;
							if(ranks[j][1] == 1) {straightCount++; cardCount++;}
							if(ranks[j][1] == 0) straightCount--;
						}
						if (straightCount < 3) break;
						if (straightCount >= 3) {
							rank = 4;
							straight = true;
							break;
						}


					}
				}
			}

			/*******************************
			 * CHECKS FOR A STRAIGHT FLUSH *
			 *******************************/
			if(straight == true && flush == true) {
				rank = 8;
			}

			/*************************************
			 * CHECKS FOR A ROYAL STRAIGHT FLUSH *
			 *************************************/
			if(royalStraight == true && flush == true) {
				rank = 9;
			}
			
			
			int result = rank;
			rank = 0;
			return result;	
		} //end if( 1 joker)

		/**********************************************************************************************
		 *          IF    2  JOKERS
		 **********************************************************************************************/
		else if (ranks[13][1] == 2) {

			/**********************************************************
			 * SINCE 2 JOKERS THERE CAN ONLY BE 3/4 OF KIND           *
			 **********************************************************/

			int check3 = 0;
			int check2 = 0;
			boolean checkOne = true;

			for(int i = 0; i<13; i++) {
				if(ranks[i][1] > 1) {
					checkOne = false;
				}
				if(ranks[i][1] == 3) {
					check3++;
				}
				if(ranks[i][1] == 2) {
					check2++;
				}
			}

			if (check2 == 1 || check3 == 1) rank = 7;	// 4 of kind
			
			/*****************************************
			 * CHECKS FOR A STRAIGHT/ROYAL STRAIGHT  *
			 *****************************************/
			boolean royalStraight = false;
			boolean straight = false;
			int straightCount = 0;
			int trigger;
			int cardCount = 0;
			if (checkOne == true) {
				for(int i = 0; i < 13; i++) {
					if((ranks[ 0][1] == 1 && ranks[11][1] == 1 && ranks[12][1] == 1) || (ranks[0][1] == 1 && ranks[10][1] == 1 && ranks[12][1] == 1) ||
					   (ranks[ 0][1] == 1 && ranks[ 9][1] == 1 && ranks[12][1] == 1) || (ranks[0][1] == 1 && ranks[ 9][1] == 1 && ranks[10][1] == 1) ||
					   (ranks[10][1] == 1 && ranks[11][1] == 1 && ranks[12][1] == 1) || (ranks[9][1] == 1 && ranks[11][1] == 1 && ranks[12][1] == 1) ||
					   (ranks[ 9][1] == 1 && ranks[10][1] == 1 && ranks[12][1] == 1) || (ranks[9][1] == 1 && ranks[10][1] == 1 && ranks[11][1] == 1) ||
					   (ranks[ 0][1] == 1 && ranks[ 9][1] == 1 && ranks[11][1] == 1))
						{
							royalStraight = true;
							rank = 4;
						}
					
					else if(ranks[i][1] == 1) {
						trigger = i;
						for(int j = trigger; j<13; j++) {
							if(cardCount == 3) break;
							if(ranks[j][1] == 1) {straightCount++; cardCount++;}
							if(ranks[j][1] == 0) straightCount--;
						}
						if (straightCount < 1) break;
						if (straightCount >= 1) {
							rank = 4;
							straight = true;
							break;
						}


					}
					else rank = 3; // 3 of a kind
				}
			}
			
			/**********************
			 * CHECKS FOR A FLUSH *
			 **********************/
			boolean flush = false;
			for(int i = 0; i<5; i++) {
				suitCount[cards[i].suit][1]++;
			}
			for(int i = 0; i<4; i++) {
				if (suitCount[i][1] >= 3) {
					flush = true;
					rank = 5;	//flush
				}
			}

			/*******************************
			 * CHECKS FOR A STRAIGHT FLUSH *
			 *******************************/
			if(straight == true && flush == true) {
				rank = 8;
			}

			/*************************************
			 * CHECKS FOR A ROYAL STRAIGHT FLUSH *
			 *************************************/
			if(royalStraight == true && flush == true) {
				rank = 9;
			}

			
			int result = rank;
			rank = 0;
			return result;	
		} //end joker2 if()
		
		/**********************************************************************************************
		 *          IF    3  JOKERS
		 **********************************************************************************************/
		else if (ranks[13][1] == 3) {
			boolean checkOne = true;
			for(int i = 0; i<13; i++) {
				if(ranks[i][1] > 1) {
					checkOne = false;
				}
			}
			
			/**********************
			 * CHECKS FOR A FLUSH *
			 **********************/
			boolean flush = false;
			for(int i = 0; i<5; i++) {
				suitCount[cards[i].suit][1]++;
			}
			for(int i = 0; i<4; i++) {
				if (suitCount[i][1] >= 2) {
					flush = true;
				}
			}
			
			/*****************************************
			 * CHECKS FOR A STRAIGHT/ROYAL STRAIGHT  *
			 *****************************************/
			boolean royalStraight = false;
			boolean straight = false;
			int straightCount = 0;
			int trigger;
			int cardCount = 0;
			if (checkOne == true) {
				for(int i = 0; i < 13; i++) {
					if((ranks[ 0][1] == 1 && ranks[12][1] == 1) || (ranks[ 0][1] == 1 && ranks[11][1] == 1) ||
					   (ranks[ 0][1] == 1 && ranks[10][1] == 1) || (ranks[ 0][1] == 1 && ranks[ 9][1] == 1) ||
					   (ranks[11][1] == 1 && ranks[12][1] == 1) || (ranks[ 9][1] == 1 && ranks[12][1] == 1) ||
					   (ranks[10][1] == 1 && ranks[11][1] == 1) || (ranks[11][1] == 1 && ranks[10][1] == 1) )
						{
							royalStraight = true;
						}
					
					else if(ranks[i][1] == 1) {
						trigger = i;
						for(int j = trigger; j<13; j++) {
							if(cardCount == 2) break;
							if(ranks[j][1] == 1) {straightCount++; cardCount++;}
							if(ranks[j][1] == 0) straightCount--;
						}
						if (straightCount < -1) break;
						if (straightCount >= -1) {
							straight = true;
							break;
						}


					}
					
				}
				if(straight == true && flush == true) rank = 8;
				else if (royalStraight == true && flush == true) rank = 9;
				else rank = 7; // 4 of a kind
			}
			else rank = 7;
			
			
			int result = rank;
			rank = 0;
			return result;
		}//end joker3 if()
		
		/**********************************************************************************************
		 *          IF    4  JOKERS
		 **********************************************************************************************/
		else if (ranks[13][1] == 4) {
			if(ranks[0][1] == 1 || ranks[9][1] == 1 || ranks[10][1] == 1 || ranks[11][1] == 1 || ranks[12][1] == 1) rank = 9;
			else rank = 8;
			
			int result = rank;
			rank = 0;
			return result;
			
		}//end joker4 if()
		
		/**********************************************************************************************
		 *          IF    5  JOKERS
		 **********************************************************************************************/
		else if (ranks[13][1] == 5) {
			rank = 9;
			int result = rank;
			rank = 0;
			return result;
		}//end joker5 if()
		
		/**********************************************************************************************
		 *          IF    NO  JOKERS
		 **********************************************************************************************/
		
		/**************************************
		 * CHECKS FOR A PAIR (JACK OR HIGHER) *
		 **************************************/
		else {
			int checkWinPair = 0;
			for(int j = 0; j<13;j++) {
				if(ranks[j][1] == 2 && (ranks[j][0] == 0 || ranks[j][0] >= 10)) {
					checkWinPair++;
				}
			}
			if ( checkWinPair == 1)  rank = 1;

			/**********************
			 * CHECKS FOR 2 PAIRS *
			 **********************/
			int check2Pair = 0;
			for(int j = 0; j<13;j++) {
				if(ranks[j][1] == 2) {
					check2Pair++;
				}
			}
			if ( check2Pair == 2) rank = 2;


			/**************************************************
			 * CHECKS FOR THREE/FOUR OF A KIND AND FULL HOUSE *
			 **************************************************/
			int check4 = 0;
			int check3 = 0;
			int check2 = 0;

			for(int j = 0; j<13;j++) {
				if(ranks[j][1] == 4) {
					check4++;
				}
				if(ranks[j][1] == 3) {
					check3++;
				}
				if(ranks[j][1] == 2) {
					check2++;
				}

			}
			if (check4 == 1) rank = 7;
			if ( check3 == 1 && check2 == 1) rank = 6;
			else if (check3 == 1) rank = 3;


			/*****************************************
			 * CHECKS FOR A STRAIGHT/ROYAL STRAIGHT  *
			 *****************************************/
			boolean straight = false;
			boolean royalStraight = false;
			int lowest;
			for(int j = 0; j<13;j++) {
				if(ranks[j][1] == 1) {
					lowest = ranks[j][0];

					if(lowest >= 9) {
						break;
					}
					else if(ranks[lowest+1][1] == 1 && ranks[lowest+2][1] == 1 && ranks[lowest+3][1] == 1 && ranks[lowest+4][1] == 1) {
						straight = true;
						rank = 4;
					}
				}
				else if(ranks[0][1]==1 && ranks[9][1]==1 && ranks[10][1]==1 && ranks[11][1]==1 && ranks[12][1]==1) {
					straight = true;
					royalStraight = true;
					rank = 4;
				}
			}

			/**********************
			 * CHECKS FOR A FLUSH *
			 **********************/
			boolean flush = false;
			if((cards[0].suit == cards[1].suit) && (cards[1].suit == cards[2].suit) && 
					(cards[2].suit == cards[3].suit) && (cards[3].suit == cards[4].suit)) {
				flush = true;
				rank = 5;
			}

			/*******************************
			 * CHECKS FOR A STRAIGHT FLUSH *
			 *******************************/
			if(straight == true && flush == true) {
				rank = 8;
			}

			/*************************************
			 * CHECKS FOR A ROYAL STRAIGHT FLUSH *
			 *************************************/
			if(royalStraight == true && flush == true) {
				rank = 9;
			}

			int result = rank;
			rank = 0;
			return result;
		}

	}
}