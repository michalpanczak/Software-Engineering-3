package poker;
import java.util.Scanner;

public class HandOfCards {
	static public final int SIZE_OF_HAND = 5;						//I chose very large integers as I'm using high numbers for the ranks
	static public final int ROYAL_FLUSH_DEFAULT = 10000000;			//which I use to weigh (balance realistically) the score of a given hand.
	static public final int STRAIGHT_FLUSH_DEFAULT = 9000000;		//I decided to use these weigh rank constants and make them round numbers
	static public final int FOUR_OF_A_KIND_DEFAULT = 8000000;		//as it made it easier and clearer for me to see how much balancing exactly I needed
	static public final int FULL_HOUSE_DEFAULT = 7000000;			//when writing out the equations on paper rather than using ^2, ^3 etc.
	static public final int FLUSH_DEFAULT = 6000000;
	static public final int STRAIGHT_DEFAULT = 5000000;
	static public final int THREE_OF_A_KIND_DEFAULT = 4000000;
	static public final int TWO_PAIR_DEFAULT = 3000000;
	static public final int ONE_PAIR_DEFAULT = 2000000;
	static public final int HIGH_HAND_DEFAULT = 1000000;
	static public final int WEIGH_RANK_4 = 10000;
	static public final int WEIGH_RANK_3 = 1000;
	static public final int WEIGH_RANK_2 = 100;
	static public final int WEIGH_RANK_1 = 10;


	
	private PlayingCard[] hand = new PlayingCard[SIZE_OF_HAND];
	private DeckOfCards deck;
	
	public HandOfCards(DeckOfCards deckOfCards){				//Constructor.
		this.deck = deckOfCards;
		for(int i = 0; i < SIZE_OF_HAND; i++){
			this.hand[i] = deck.dealNext();
		}
		this.sort();
	}
	
	public PlayingCard getCardFromHand(int cardLocation){
		return hand[cardLocation];
	}
	
	public void insertCardIntoHand(PlayingCard card, int position){
		hand[position] = card;
	}
	
	/*I got the odds for different probabilities of outcomes after discarding a card improving a hand the user currently holds.
	 * I converted the odds to percentage using this website: 'http://www.calculatorsoup.com/calculators/games/odds.php'
	 * For some methods I left it at 0 (don't discard card) even if in for example four of a kind a user might want to discard kicker
	 * card despite it not making a difference (no tie possible in four of a kind hence kicker doesn't matter) this can be changed later
	 * for an example if a bot might want to bluff. In some methods value returned is 100 as for example in high hand if no 4 flush or broken
	 * straight are there, discarding the weakest card is best option.*/
	
	
	public int	getDiscardProbability(int cardPosition){
		if(cardPosition > 4) return 0;
		
		else if(getGameValue() >= ROYAL_FLUSH_DEFAULT){
			return 0;
		}
		
		else if(getGameValue() >= STRAIGHT_FLUSH_DEFAULT){
			return 0;
		}
		
		else if(getGameValue() >= FOUR_OF_A_KIND_DEFAULT){
			return 0;
		}
		
		else if(getGameValue() >= FULL_HOUSE_DEFAULT){
			return 0;
		}
		
		else if(getGameValue() >= FLUSH_DEFAULT){
			return 0;
		}
		
		else if(getGameValue() >= STRAIGHT_DEFAULT){
			if(this.is4Flush(cardPosition) != 0){
				return this.is4Flush(cardPosition);
			}
			else return 0;
		}
		
		else if(getGameValue() >= THREE_OF_A_KIND_DEFAULT){
			if(threeOfAKindProbabilityDiscard(cardPosition) != 0){
				return threeOfAKindProbabilityDiscard(cardPosition);
			}
			else if(is4Flush(cardPosition) != 0){
				return is4Flush(cardPosition);
			}
			else if(isBrokenStraight(cardPosition) != 0){
				return isBrokenStraight(cardPosition);
			}
			else return threeOfAKindProbabilityDiscard(cardPosition);
		}
		
		else if(getGameValue() >= TWO_PAIR_DEFAULT){
			if(twoPairProbabilityDiscard(cardPosition) != 0){
				return twoPairProbabilityDiscard(cardPosition);
			}
			else if(is4Flush(cardPosition) != 0){
				return is4Flush(cardPosition);
			}
			else if(isBrokenStraight(cardPosition) != 0){
				return isBrokenStraight(cardPosition);
			}
			else return 0;
		}
		
		else if(getGameValue() >= ONE_PAIR_DEFAULT){
			if(is4Flush(cardPosition) != 0){			//In my opinion it's worth ruining the one pair when we have 4-flush in order to achieve flush.
				return is4Flush(cardPosition);
			}
			else if(onePairProbabilityDiscard(cardPosition) != 0){
				return onePairProbabilityDiscard(cardPosition);
			}
			else if(isBrokenStraight(cardPosition) != 0){
				return isBrokenStraight(cardPosition);
			}
			else return 0;
		}
		
		else if(getGameValue() >= HIGH_HAND_DEFAULT){
			if(is4Flush(cardPosition) != 0){
				return is4Flush(cardPosition);
			}
			else if(isBrokenStraight(cardPosition) != 0){
				return isBrokenStraight(cardPosition);
			}
			else if(highHandProbabilityDiscard(cardPosition) != 0){
				return highHandProbabilityDiscard(cardPosition);
			}
			else return 0;
		}
		return 0;
		}
	
	private int threeOfAKindProbabilityDiscard(int cardPosition){
		if(hand[cardPosition].getGameValueOfCard() != hand[2].getGameValueOfCard()){	//Check if its the kicker card being discarded because hand[2] will always be part of three of a kind in 5 card hand.
			return 10;		//probability of turning into four of a kind OR full house.
		}
		return 0;
	}
	
	private int twoPairProbabilityDiscard(int cardPosition){				//Second and fourth cards will always be parts of pairs in a two pair hand in 5 card combination hence just compare to them .
		if(hand[cardPosition].getGameValueOfCard() != hand[1].getGameValueOfCard() && hand[cardPosition].getGameValueOfCard()!= hand[3].getGameValueOfCard()){ 
					return 9;												//Probability of turning two pair into full house when discarding the kicker card;
		}
		return 0;
	}																									
		
	
	private int onePairProbabilityDiscard(int cardPosition){
		for(int i = 0; i < SIZE_OF_HAND; i++){
			if(cardPosition != i && hand[cardPosition].getGameValueOfCard() == hand[i].getGameValueOfCard()){
				return 0;											//If selected card is part of the pair do not discard.
			}
		}
		return 29;											//Probability of turning one pair into others when discarding your cards, one pair can turn into 2 pair, 3 of a kind, 4 of a kind or full house.
	}
	
	private int highHandProbabilityDiscard(int cardPosition){
		if(cardPosition == 4){								//If card is last card in hand (smallest game value) then discard it otherwise don't, this is because there's no point discarding cards of higher values before we discard cards of lower game values.
			return 100;
		}
		return 0;
	}
	
	private int is4Flush(int cardPosition){
		int suitCount = 1;
		char suitType = ' ';
		for(int i = 0; i < SIZE_OF_HAND-1; i++){
			if(hand[i].getCardSuit() == hand[i+1].getCardSuit()){
				suitCount++;
				suitType = hand[i].getCardSuit();
			}
		}
		if(suitCount == 3 && hand[cardPosition].getCardSuit() != suitType){		//If only one card is of wrong suit suitcard will be 3 as bad card doesn't satisfy the if statement twice, once when card before it is compared to it and once when it is being compared to card after it.
			return 19;		//Probability of turning 4 flush into flush.
		}
		else return 0;	
		}
	
	private int isBrokenStraight(int cardPosition){
		boolean isThisBrokenStraight = true;			//Boolean check which is false when one of broken flush conditions is not satisfied.
		int checkForInsideStraight = 0;					//If there's a broken straight inside (e.g 2, 3, 5, 6, Q) iterate that way we can differentiate it from outside straight which has a different probability (e.g 4, 5, 6, 7, K) here discarding king can give us either 2 or 8 to satisfy. 
		int checkForSameRank = 0;						//In case the cause of a broken straight is a pair of some card this allows us to differentiate from other types broken straights so we can deal with it accordingly.		
		if(cardPosition == 0){							//In broken straight discarding either first card, last card or part of a pair of some cards can fix it into normal straight hence why we deal with only those three.
			for(int i = 1; i < SIZE_OF_HAND - 2; i++){
				if(hand[i].getGameValueOfCard() - hand[i + 1].getGameValueOfCard() != 1){
					isThisBrokenStraight = false;
				}
			}
			if(isThisBrokenStraight && hand[cardPosition].getGameValueOfCard() - hand[cardPosition + 1].getGameValueOfCard() != 1){
				return 17;		//Probability if broken straight outside.
			}
			isThisBrokenStraight = true;
			for(int i = 1; i < SIZE_OF_HAND - 1; i++){
				if(hand[i].getGameValueOfCard() - hand[i + 1].getGameValueOfCard() == 2){
					checkForInsideStraight++;
				}
				else if(hand[i].getGameValueOfCard() - hand[i + 1].getGameValueOfCard() != 1){
					isThisBrokenStraight = false;
				}
			}
			if(checkForInsideStraight == 1 && isThisBrokenStraight){
				return 9;		//Probability if broken straight inside.
			}
			checkForInsideStraight = 0;	
		}
		else if(cardPosition == 4){
			for(int i = 0; i < SIZE_OF_HAND - 2; i++){
				if(hand[i].getGameValueOfCard() - hand[i + 1].getGameValueOfCard() != 1){
					isThisBrokenStraight = false;
				}
			}
			if(isThisBrokenStraight && hand[cardPosition + 1].getGameValueOfCard() - hand[cardPosition].getGameValueOfCard() != 1){
				return 17;		//Probability of broken straight outside.
			}
			isThisBrokenStraight = true;
			for(int i = 0; i < SIZE_OF_HAND - 3; i++){
				if(hand[i].getGameValueOfCard() - hand[i + 1].getGameValueOfCard() == 2){
					checkForInsideStraight++;
				}
				else if(hand[i].getGameValueOfCard() - hand[i + 1].getGameValueOfCard() != 1){
					isThisBrokenStraight = false;
				}
			}
			if(checkForInsideStraight == 1 && isThisBrokenStraight){
				return 9;		//Probability of broken straight inside.
			}
		}
		
		else{
			for(int i = 0; i < SIZE_OF_HAND - 1; i++){
				if(hand[i].getGameValueOfCard() == hand[cardPosition].getGameValueOfCard()){
					checkForSameRank ++;			//If cause of broken straight is some pair then do following:
				}
				else if(hand[i].getGameValueOfCard() - hand[i + 1].getGameValueOfCard() != 1){
					isThisBrokenStraight = false;
				}
				}
			if(checkForSameRank == 2 && isThisBrokenStraight){			//If both conditions are true
				return 17;												//Return probability for broken straight outside because (e.g 10, J, Q, Q, K discarding Q can give us either A or 9 to satisfy).
			}
		}
		return 0;
	}
	
	public int getGameValue(){										//For the first few possible hands not much calculation was needed as there is only one kicker card possible in case of a draw.
		int handValueWeighed = 0;
		if(this.isRoyalFlush()){
			return ROYAL_FLUSH_DEFAULT + this.sumAllWeighedValues();
		}
		else if(this.isStraightFlush()){
			return STRAIGHT_FLUSH_DEFAULT + this.sumAllWeighedValues();
		}
		else if(this.isFourOfAKind()){
			return FOUR_OF_A_KIND_DEFAULT + this.sumAllWeighedValues();
		}
		else if(this.isFullHouse()){
			return FULL_HOUSE_DEFAULT + this.sumAllWeighedValues();
		}
		else if(this.isFlush()){
			return FLUSH_DEFAULT + this.sumAllWeighedValues();
		}
		else if(this.isStraight()){
			return STRAIGHT_DEFAULT + this.sumAllWeighedValues();
		}
		else if(this.isThreeOfAKind()){								//Here the different if statements look for the cards which make up three of a king so we can identify the kicker cards.
			if(hand[0].getCardType() == hand[2].getCardType()){		//Then each kicker card is multiplied by decreasing rank values, this works by weighing the values i.e first kicker card is more important than second
				handValueWeighed = hand[0].getGameValueOfCard() * WEIGH_RANK_3 + 	//hence its value should be higher than the kicker card that it is followed by.
						hand[1].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[2].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[0].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[0].getGameValueOfCard() * WEIGH_RANK_1;
			}
			else if (hand[1].getCardType() == hand[3].getCardType()){
				handValueWeighed = hand[1].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[2].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[3].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[0].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[4].getGameValueOfCard() * WEIGH_RANK_1;
			}
			else if (hand[2].getCardType() == hand[4].getCardType()){
				handValueWeighed = hand[2].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[3].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[4].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[0].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[1].getGameValueOfCard() * WEIGH_RANK_1;
			}
			
			return THREE_OF_A_KIND_DEFAULT + handValueWeighed;
		}
		else if(this.isTwoPair()){			//Again we look to identify the pair using if statements so we can increase pair values by a lot and then decreasingly increase kicke card values depending on their importance.
			if(hand[0].getCardType() == hand[1].getCardType() && hand[2].getCardType() == hand[3].getCardType()){
				handValueWeighed = hand[0].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[1].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[2].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[3].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[4].getGameValueOfCard() * WEIGH_RANK_1;
			}
			else if(hand[1].getCardType() == hand[2].getCardType() && hand[3].getCardType() == hand[4].getCardType()){
				handValueWeighed = hand[1].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[2].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[3].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[4].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[0].getGameValueOfCard() * WEIGH_RANK_1;
					}
			else if(hand[0].getCardType() == hand[1].getCardType() && hand[3].getCardType() == hand[4].getCardType()){
				handValueWeighed = hand[0].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[1].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[3].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[4].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[2].getGameValueOfCard() * WEIGH_RANK_1;
			}
			return TWO_PAIR_DEFAULT + handValueWeighed;
		}
		else if(this.isOnePair()){			//Again if statements looking to identify which cards make up pairs and which are kicker cards.
			if(hand[0].getCardType() == hand[1].getCardType()){
				handValueWeighed = hand[0].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[1].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[2].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[3].getGameValueOfCard() * WEIGH_RANK_1 +
						hand[4].getGameValueOfCard();
			}
			else if(hand[1].getCardType() == hand[2].getCardType()){
				handValueWeighed = hand[1].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[2].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[0].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[3].getGameValueOfCard() * WEIGH_RANK_1 +
						hand[4].getGameValueOfCard();
				
			}
			else if(hand[2].getCardType() == hand[3].getCardType()){
				handValueWeighed = hand[2].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[3].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[0].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[1].getGameValueOfCard() * WEIGH_RANK_1 +
						hand[4].getGameValueOfCard();
				
			}
			else if(hand[3].getCardType() == hand[4].getCardType()){
				handValueWeighed = hand[3].getGameValueOfCard() * WEIGH_RANK_3 + 
						hand[4].getGameValueOfCard() * WEIGH_RANK_3 +
						hand[0].getGameValueOfCard() * WEIGH_RANK_2 +
						hand[1].getGameValueOfCard() * WEIGH_RANK_1 +
						hand[2].getGameValueOfCard();
			}
			return ONE_PAIR_DEFAULT + handValueWeighed;
		}
		else if(this.isHighHand()){		//Since cards are sorted already the highest card in hand has its value increased then the following cards are increased by decreasing numbers.
			handValueWeighed = hand[0].getGameValueOfCard() * 5 + 
					hand[1].getGameValueOfCard() * 4 +
					hand[2].getGameValueOfCard() * 3 +
					hand[3].getGameValueOfCard() * 2 +
					hand[4].getGameValueOfCard();
			return HIGH_HAND_DEFAULT + handValueWeighed;			//I didn't use weigh_rank constants here because it would cause hassle overlapping point wise with one pair hence I simply used smaller integers to multiply card values by.
		}
		return 0;
	}
	
	private int sumAllWeighedValues(){			//This is the method used for first few combinations it simply multiplies all hand cards by highest rank value since no kicker cards are possible in those combinations as they use all 5 cards.
		int sum = 0;
		for(int i=0; i<SIZE_OF_HAND - 1; i++){
			sum += this.hand[i].getGameValueOfCard() * WEIGH_RANK_4;
		}
		return sum;
	}
	
	public DeckOfCards getDeck(){				//method to return the deck currently used.
		return this.deck;
	}
	
	public void sort(){
		PlayingCard temp;
		for (int i = 0; i < SIZE_OF_HAND; i++) {
	        for (int j = 1; j < (SIZE_OF_HAND - i); j++) {

	            if (hand[j - 1].getGameValueOfCard() < hand[j].getGameValueOfCard()) {
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
			 if(hand[i].getGameValueOfCard() - hand[i+ 1].getGameValueOfCard() != 1){
				 return false;
			 }
		 }
		 return true;
	}
}
	
	//public static void main(String[] args){
		//Scanner reader = new Scanner(System.in);
		/*In the loop is testing for up to 50 hands for two players requiring user input for discard.)*/
		/*Underneath is manual testing specifically for broken straights two outside and one inside I included manual testing as it's hard to test it using randomly drawn cards.*/
		/*for(int j = 0; j <= 1; j++){
			DeckOfCards deck = new DeckOfCards();
			deck.shuffle();
			HandOfCards newHand = new HandOfCards(deck);
			System.out.println("Player 1:");
			for(int i = 0; i < SIZE_OF_HAND; i++){
				System.out.println(newHand.hand[i].toString());
			}
			HandOfCards newHand2 = new HandOfCards(deck);
			System.out.println("Player 2:");
			for(int i = 0; i < SIZE_OF_HAND; i++){
				System.out.println(newHand2.hand[i].toString());
			}
			System.out.println("Enter which card you'd like to discard for player 1");
			System.out.println("Probability in percentage of improving your hand is : " + newHand.getDiscardProbability(reader.nextInt()));
			System.out.println("Enter which card you'd like to discard for player 2");
			System.out.println("Probability in percentage of improving your hand is : " + newHand2.getDiscardProbability(reader.nextInt()));
			
		}
		
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		HandOfCards testHand = new HandOfCards(deck);
		testHand.hand[4] = new PlayingCard("2", 'S', 2, 2);		//Discard card 8 of spades to get probability of fixing this broken straight, this one is an inside straight.
		testHand.hand[3] = new PlayingCard("3", 'D', 3, 3);
		testHand.hand[2] = new PlayingCard("5", 'H', 5, 5);
		testHand.hand[1] = new PlayingCard("6", 'S', 6, 6);
		testHand.hand[0] = new PlayingCard("8", 'S', 7, 7);
		
		for(int i = 0; i < SIZE_OF_HAND; i++){
			System.out.println(testHand.hand[i].toString());
		}
		System.out.println("Enter which card you'd like to discard: ");
		System.out.println("Probability in percentage of improving your hand is : " + testHand.getDiscardProbability(reader.nextInt()));

		
		HandOfCards testHand2 = new HandOfCards(deck);
		testHand2.hand[2] = new PlayingCard("Q", 'S', 12, 12);		//Discard one of the queens to test probability of broken straight being fixed discarding a card thats part of a pair.
		testHand2.hand[0] = new PlayingCard("K", 'D', 13, 13);
		testHand2.hand[1] = new PlayingCard("Q", 'H', 12, 12);
		testHand2.hand[3] = new PlayingCard("J", 'S', 11, 11);
		testHand2.hand[4] = new PlayingCard("10", 'S', 10, 10);
		
		for(int i = 0; i < SIZE_OF_HAND; i++){
			System.out.println(testHand2.hand[i].toString());
		}
		System.out.println("Enter which card you'd like to discard: ");
		System.out.println("Probability in percentage of improving your hand is : " + testHand2.getDiscardProbability(reader.nextInt()));

		
		HandOfCards testHand3 = new HandOfCards(deck);
		
		testHand3.hand[0] = new PlayingCard("K", 'D', 13, 13);		//Discard card 2 of spades to test outside broken straight.
		testHand3.hand[1] = new PlayingCard("Q", 'H', 12, 12);
		testHand3.hand[2] = new PlayingCard("J", 'S', 11, 11);
		testHand3.hand[3] = new PlayingCard("10", 'S', 10, 10);
		testHand3.hand[4] = new PlayingCard("2", 'S', 2, 2);
		
		for(int i = 0; i < SIZE_OF_HAND; i++){
			System.out.println(testHand3.hand[i].toString());
		}
		System.out.println("Enter which card you'd like to discard: ");
		System.out.println("Probability in percentage of improving your hand is : " + testHand3.getDiscardProbability(reader.nextInt()));
		
HandOfCards testHand4 = new HandOfCards(deck);
		
		testHand4.hand[0] = new PlayingCard("K", 'D', 13, 13);		//Discard card 9 of spades to test is4Flush.
		testHand4.hand[1] = new PlayingCard("Q", 'D', 12, 12);
		testHand4.hand[2] = new PlayingCard("9", 'S', 9, 9);
		testHand4.hand[3] = new PlayingCard("7", 'D', 7, 7);
		testHand4.hand[4] = new PlayingCard("2", 'D', 2, 2);
		
		for(int i = 0; i < SIZE_OF_HAND; i++){
			System.out.println(testHand4.hand[i].toString());
		}
		System.out.println("Enter which card you'd like to discard: ");
		System.out.println("Probability in percentage of improving your hand is : " + testHand4.getDiscardProbability(reader.nextInt()));

	}
}
*/
