package poker;

public class HandOfCards {
	static public final int SIZE_OF_HAND = 5;
	
	private PlayingCard[] hand = new PlayingCard[SIZE_OF_HAND];
	private DeckOfCards deck = new DeckOfCards();
	
	public HandOfCards(DeckOfCards deckOfCards){
		this.deck = deckOfCards;
		for(int i = 0; i < SIZE_OF_HAND; i++){
			this.hand[i] = deck.dealNext();
		}
		this.sort();
		
		
		
	}
	
	public DeckOfCards getDeck(){				//method to return the deck currently used.
		return this.deck;
	}
	
	private void sort(){
		PlayingCard temp;
		for (int i = 0; i < SIZE_OF_HAND; i++) {
	        for (int j = 1; j < (SIZE_OF_HAND - i); j++) {

	            if (hand[j - 1].getGameValue() < hand[j].getGameValue()) {
	                temp = hand[j - 1];
	                hand[j - 1] = hand[j];
	                hand[j] = temp;
	            }

	        }
	    }
	}
	
	public static void main(String[] args){
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		HandOfCards newHand = new HandOfCards(deck);
		for(int i = 0; i < SIZE_OF_HAND; i++){
			System.out.println(newHand.hand[i].toString());
		}
	
	}
		


}
