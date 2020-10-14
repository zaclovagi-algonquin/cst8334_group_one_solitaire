package com.cst8334_group_one_solitaire.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.cst8334_group_one_solitaire.beans.Card;
import com.cst8334_group_one_solitaire.beans.Deck;

class DeckTest {

	@Test
	void initializationMethodShouldFillDeckWith52OrderedCards() {
		Card firstCard = new Card(0, 0);
		Deck.initialization();
		
		assertEquals(Deck.DECK.size(), 52);
		assertEquals(Deck.DECK.get(0).toString(), firstCard.toString());
	}

}
