package poker;

/*Simple Program to initialize a deck of cards (52) cards creating a card object with certain values for every card in the deck.*/

public class PlayingCard {
	static public final char[] SUITARRAY = {'H','D','C', 'S'};
	static public final String[] TYPEARRAY= {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	static public final int SIZE_OF_DECK = 52;
	
	private String cardType;
	private char cardSuit;
	private int faceValue;
	private int gameValue;

	public PlayingCard(String card_type, char card_suit, int face_value, int game_value) {		//This is the constructor.
		this.cardType = card_type;
		this.cardSuit = card_suit;
		this.faceValue = face_value;
		this.gameValue = game_value;
	}
	
	public String toString(){					//Returns a string containing type of card and its suit.
		return cardType+cardSuit;
	}
	
	/*public static void main(String[] args){
		PlayingCard deck[] = new PlayingCard[SIZE_OF_DECK];			//Initialize array of cards.
		int count = 0;												//count, k, and i are just counter variables for loops.
		for(int k = 0; k<4; k++){
			for(int i = 0; i<13; i++){
				if(i==0){
					deck[count] = new PlayingCard(TYPEARRAY[i], SUITARRAY[k], i + 1, 14);	//Check if it's an ace because it has an exception for game value.
				}
				else {
					deck[count] = new PlayingCard(TYPEARRAY[i], SUITARRAY[k], i+ 1, i+ 1);
				}
				count++;
				
			}
		}
		for(PlayingCard card: deck){
			System.out.println(card.toString());
	}
	}*/
	
	
	public int getFaceValue(){			//Allows user to see face value of card.
		return faceValue;
	}
	
	public char getCardSuit(){			//Allows user to see suit of card.
		return cardSuit;
	}
	
	public int getGameValueOfCard(){			//Allows user to see game value of card.
		return gameValue;
	}
	
	public String getCardType(){		//Allows user to see type of card.
		return cardType;
	}
	
}


			
		
