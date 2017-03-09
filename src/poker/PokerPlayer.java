package poker;

import java.util.Random;

public class PokerPlayer {
	private HandOfCards playerHand; 
	private DeckOfCards gameDeck;
	
	public PokerPlayer(DeckOfCards deck){
		this.gameDeck = deck;
		this.playerHand = new HandOfCards(gameDeck);
	}

	
	public int discard(){
		int cardsDiscarded = 0;
		Random r = new Random();
		for(int i = 0; i < HandOfCards.SIZE_OF_HAND; i++){
			if(cardsDiscarded == 3) return cardsDiscarded;
			//System.out.println("test");
			if(playerHand.getDiscardProbability(i) == 0) i++;
			else if(playerHand.getDiscardProbability(i) == 100){
				gameDeck.returnCard(playerHand.getCardFromHand(i));
				playerHand.insertCardIntoHand(gameDeck.dealNext(), i);
				cardsDiscarded++;
				playerHand.sort();
				i++;
			}
			else if(playerHand.getDiscardProbability(i) <= r.nextInt(101)){
					gameDeck.returnCard(playerHand.getCardFromHand(i));
					playerHand.insertCardIntoHand(gameDeck.dealNext(), i);
					cardsDiscarded++;
					playerHand.sort();
					i++;
				}
			else i++;
		}
		return cardsDiscarded;
	}
	
	public static void main(String[] args){
		DeckOfCards newDeck = new DeckOfCards();
		newDeck.shuffle();
		PokerPlayer player1 = new PokerPlayer(newDeck);
		for(int j = 0; j < HandOfCards.SIZE_OF_HAND; j++){
			System.out.println(player1.playerHand.getCardFromHand(j).toString());
		}
		int noOfDiscardedCards = player1.discard();
		System.out.println("\nUsing random number generator the player decided to discard: " + noOfDiscardedCards + " cards. \nThis is what the new hand looks like:\n");
		
		for(int j = 0; j < HandOfCards.SIZE_OF_HAND; j++){
			System.out.println(player1.playerHand.getCardFromHand(j).toString());
		}
		
		//System.out.println("Here: " + player1.playerHand.getDiscardProbability(0));
		
		
		
	}

}



