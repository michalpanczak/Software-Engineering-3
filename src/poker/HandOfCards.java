package poker;

public class HandOfCards {
	static public final int SIZE_OF_HAND = 5;
	
	private PlayingCard[] hand = new PlayingCard[SIZE_OF_HAND];
	private DeckOfCards deck;
	
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
	
	boolean isRoyalFlush(){
		if(isSuitsEqual()){
			if(hand[0].getCardType() == "A" && isGameValueDiff1()){ 
				return true;
			}
		}
		return false;
	}
	
	boolean isStraightFlush(){
		if(isSuitsEqual()){
			if(isGameValueDiff1()){
				return true;
			}
		}
		return false;
	}
	boolean isFourOfAKind(){
		if(hand[0].getCardType() == hand[3].getCardType() || hand[1].getCardType() == hand[4].getCardType()){
			return true;
		}
		return false;
	}
	
	boolean isThreeOfAKind(){
		if(hand[0].getCardType() == hand[2].getCardType() || hand[1].getCardType() == hand[3].getCardType() ||
				hand[2].getCardType() == hand[4].getCardType()){
			return true;
		}
		return false;
	}
	
	boolean isFullHouse(){
		if((hand[0].getCardType() == hand[2].getCardType() && hand[3].getCardType() == hand[4].getCardType()) ||
				hand[0].getCardType() == hand[1].getCardType() && hand[2].getCardType() == hand[4].getCardType()){
			return true;
		}
		return false;
	}
	
	boolean isStraight(){
		if(!isSuitsEqual() && isGameValueDiff1()){
			return true;
		}
		return false;
	}
	
	boolean isFlush(){
		if(isSuitsEqual()){
			return true;
		}
		return false;
	}
	
	boolean isTwoPair(){
		if((hand[0].getCardType() == hand[1].getCardType() && hand[2].getCardType() == hand[3].getCardType())||
				(hand[1].getCardType() == hand[2].getCardType() && hand[3].getCardType() == hand[4].getCardType())||
				hand[0].getCardType() == hand[1].getCardType() && hand[3].getCardType() == hand[4].getCardType()){
			return true;
		}
		return false;
		
	}
	boolean isOnePair(){
		if(hand[0].getCardType() == hand[1].getCardType() || hand[1].getCardType() == hand[2].getCardType() || 
				hand[2].getCardType() == hand[3].getCardType() || hand[3].getCardType() == hand[4].getCardType()){
			return true;
		}
		return false;
	}
	
	boolean isHighHand(){						//Always returns true as player will always be able to play high card.
		return true;
	}
	
	private boolean isSuitsEqual(){				//Checks if all cards in the hand have the same suit value.
        for(int i=1; i<SIZE_OF_HAND; i++){
            if(hand[0].getCardSuit() != hand[i].getCardSuit()){
                return false;
            }
        }

        return true;
    }
	
	private boolean isGameValueDiff1(){			//Checks if the cards are in rank order another words check if diference in game value between each card is 1.
		 for(int i=0; i<SIZE_OF_HAND - 1; i++){	//Use this method when checking for straight/straight flush/royal flush etc.
			 if(hand[i].getGameValue() - hand[i+ 1].getGameValue() != 1){
				 return false;
			 }
		 }
		 return true;
	}
	
	public static void main(String[] args){
		//for(int j = 0; j < 1000000; j++){					//use this loop to test many instances of HandOfCards to see if all possible hands work.			
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		HandOfCards newHand = new HandOfCards(deck);
		for(int i = 0; i < SIZE_OF_HAND; i++){
			System.out.println(newHand.hand[i].toString());
		}
		
		if(newHand.isRoyalFlush()){
			System.out.println("Royal Flush");
		}
		else if(newHand.isStraightFlush()){
			System.out.println("Straight Flush");
		}
		else if(newHand.isFourOfAKind()){
			System.out.println("Four of a Kind");
		}
		else if(newHand.isFullHouse()){
			System.out.println("Full House");
		}
		else if(newHand.isFlush()){
			System.out.println("Flush");
		}
		else if(newHand.isStraight()){
			System.out.println("Straight");
		}
		else if(newHand.isThreeOfAKind()){
			System.out.println("Three of a Kind");
		}
		else if(newHand.isTwoPair()){
			System.out.println("Two Pair");
		}
		else if(newHand.isOnePair()){
			System.out.println("One Pair");
		}
		else{
			if(newHand.isHighHand()){
				System.out.println("High Hand");
			}
		}
	//}
	}


}
