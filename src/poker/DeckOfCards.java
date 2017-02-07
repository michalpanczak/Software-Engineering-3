package poker;
/*Program which initializes the deck of 52 cards allowing them to be shuffled, reset etc, it also deals cards and takes discarded cards back into the deck making sure they are not dealt again.*/

import java.util.Random;

public class DeckOfCards {
	private PlayingCard deck[] = new PlayingCard[PlayingCard.SIZE_OF_DECK];			//Initialize array of cards.
	private int topOfDeck = 51;
	private int discardPile = 51;
	public DeckOfCards(){
		int count = 0;												//count, k, and i are just counter variables for loops.
		for(int k = 0; k<4; k++){
			for(int i = 0; i<13; i++){
				if(i==0){
					deck[count] = new PlayingCard(PlayingCard.TYPEARRAY[i], PlayingCard.SUITARRAY[k], i + 1, 14);	//Check if it's an ace because it has an exception for game value.
				}
				else {
					deck[count] = new PlayingCard(PlayingCard.TYPEARRAY[i], PlayingCard.SUITARRAY[k], i+ 1, i+ 1);
				}
				count++;
			}
		}

	}
	
	public void shuffle(){											//Pick two random spaces in the array and swap them, do this 52^2 times.
		for(int i = 1;i <= 52*52; i++){
			Random rn = new Random();
			int swap1 = rn.nextInt(52);
			int swap2 = rn.nextInt(52);
			PlayingCard tmp = deck[swap1];
			deck[swap1] = deck[swap2];
			deck[swap2] = tmp;
		}
	}
	
	public void reset(){											//Reinitializes and reshuffles the deck.
		DeckOfCards newDeck = new DeckOfCards(); 
		newDeck.shuffle();
		deck = newDeck.deck;
		topOfDeck = 51;
		discardPile = 51;
	}
	
	PlayingCard dealNext(){											//Gives a player the next card to be dealt, the pointer to the position of next card to be dealt is then moved,
		if(topOfDeck < 0){											//if there are no cards left to be dealt return null.
			System.out.println("Deck is empty");
			return null;
		}
		else{
			PlayingCard topCard = deck[topOfDeck];
			topOfDeck--;
			return topCard;
		}
	}
	
	void returnCard(PlayingCard discarded){							//Returns a discarded card back to the deck but due to the way the pointer for next card to be dealt works
		deck[discardPile] = discarded;								//we are guaranteed that a discarded card added back to the deck won't be played again unless deck is reset.
		discardPile--;
	}

	public static void main(String[] args){
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		
		PlayingCard test1 = deck.dealNext();
		System.out.println(test1.toString());
		PlayingCard test2 = deck.dealNext();
		System.out.println(test2.toString());
		PlayingCard test3 = deck.dealNext();
		System.out.println(test3.toString());
		deck.returnCard(test1);
		deck.returnCard(test2);
		PlayingCard test4 = deck.dealNext();
		System.out.println(test4.toString());
		
		System.out.println("Now reset whole deck and print it out");
		
		deck.reset();
		
		for(int i = 0; i < 53; i++){
			PlayingCard cardPlayed = deck.dealNext();
			if(cardPlayed != null){
			System.out.println(cardPlayed.toString());
			}
			}
	}
}







