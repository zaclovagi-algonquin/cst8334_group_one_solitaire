package com.cst8334_group_one_solitaire.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.cst8334_group_one_solitaire.beans.Board;
import com.cst8334_group_one_solitaire.beans.CardPile;

class BoardTest {

	private Board describedClass = new Board(13);
	private CardPile[] pile = describedClass.allPiles();
	
	@Test
	public void shouldInitializeAllPilesEmpty() {
		describedClass = new Board(13);
		
		assertEquals(pile.length, 13);
		assertTrue(describedClass.stockPile.isEmpty());
		assertTrue(describedClass.talon.isEmpty());
		for(CardPile cp : describedClass.foundations) {
			assertTrue(cp.isEmpty());
		}
		for(CardPile cp : describedClass.tableau) {
			assertTrue(cp.isEmpty());
		}
	}
	
	@Test
	public void shuffleMethodShouldPopulateTableau() {
		describedClass = new Board(13);
		describedClass.shuffle();
		
		for(CardPile cp : describedClass.tableau) {
			assertFalse(cp.isEmpty());
		}
	}
	
	@Test
	public void shuffleMethodShouldFlipTopCardsInTableau() {
		describedClass = new Board(13);
		describedClass.shuffle();
		
		for(CardPile cp : describedClass.tableau) {
			assertTrue(cp.inspectTop().isFaceUp());
		}
	}
	
	@Test
	public void shuffleMethodShouldPopulateStockPile() {
		describedClass = new Board(13);
		describedClass.shuffle();
		
		assertFalse(describedClass.stockPile.isEmpty());
	}
	
	@Test
	public void shuffleMethodShouldNotFlipTopStockPileCard() {
		describedClass = new Board(13);
		describedClass.shuffle();
		
		assertFalse(describedClass.stockPile.inspectTop().isFaceUp());
	}

}
