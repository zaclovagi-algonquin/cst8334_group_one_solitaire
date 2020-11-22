package com.cst8334_group_one_solitaire.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cst8334_group_one_solitaire.beans.Board;
import com.cst8334_group_one_solitaire.beans.Card;
import com.cst8334_group_one_solitaire.beans.Game;

class GameTest {
	Game game = Game.getInstance();
	@BeforeEach
	public void setUp() {
		Game.board = new Board(13);
		Game.board.shuffle();
	}
	
	@Test
	public void stockPileClickedShouldRemoveTopStockCardWhenStockNotEmpty() {
		int expectedStockPileSize = Game.board.stockPile.size() - 1;

		game.select(1200,  55);
		
		assertEquals(Game.board.stockPile.size(), expectedStockPileSize);
	}
	
	@Test
	public void stockPileClickedShouldAddRemovedCardToTalonWhenStockNotEmpty() {
		int expectedTalonSize = Game.board.talon.size() + 1;

		game.select(1200, 55);
		
		assertEquals(Game.board.talon.size(), expectedTalonSize);
	}
	
	@Test
	public void stockPileClickedShouldFlipTalonCardUpWhenStockNotEmpty() {
		game.select(1200, 55);
		assertTrue(Game.board.talon.inspectTop().isFaceUp());
	}

	@Test
	public void stockPileClickedShouldEmptyTalonWhenStockIsEmpty() {
		while(!Game.board.stockPile.isEmpty()) {
			Card card = Game.board.stockPile.removeTop(true);
			card.flip();
			Game.board.talon.addCard(card, false);
		}

		game.select(1200, 55);
		
		assertEquals(Game.board.talon.size(), 0);
		
	}
	
	@Test
	public void stockPileClickedShouldFaceDownAllStockPileCards() {
		while(!Game.board.stockPile.isEmpty()) {
			Card card = Game.board.stockPile.removeTop(true);
			card.flip();
			Game.board.talon.addCard(card, false);
		}

		game.select(1200, 55);
		
		assertFalse(Game.board.stockPile.inspectTop().isFaceUp());
	}
	
	@Test
	public void talonClickedShouldMoveAceHeartsToFoundation() {
		Card cardToMove = new Card(0, 0); //ace of hearts should move to foundation if talon is clicked
		Game.board.talon.addCard(cardToMove, false);

		game.select(1075, 55);
		
		assertTrue(Game.board.talon.isEmpty());
		assertEquals(Game.board.foundations[0].inspectTop().toString(), cardToMove.toString());
	}
	
	@Test
	public void talonClickedShouldMoveTwoHeartsToTableauPileWithThreeClub() {
		Card cardToMove = new Card(0, 1); //ace of hearts
		Card tableauOneTop = new Card(3, 2); //two of clubs
		Game.board.talon.addCard(cardToMove, false);
		Game.board.tableau[0].addCard(tableauOneTop, true);

		game.select(1075, 55);
		
		assertTrue(Game.board.talon.isEmpty());
		assertEquals(Game.board.tableau[0].inspectTop().toString(), cardToMove.toString());

	}
	
	//test to ensure that stack move from talon bug does not happen again
	@Test
	public void talonClickedShouldNotMoveStack() {
		Card cardNotToMove = new Card(1, 2); //two of spade
		cardNotToMove.flip();
		Card cardToMove = new Card(0, 1); //ace of hearts
		cardToMove.flip();
		Card tableauOneTop = new Card(0, 3); //three of clubs
		Game.board.talon.addCard(cardNotToMove, false);
		Game.board.talon.addCard(cardToMove, false);
		Game.board.tableau[0].addCard(tableauOneTop, true);

		game.select(1075, 55);
		
		assertEquals(2, Game.board.talon.size()); //no move made
		assertEquals(Game.board.tableau[0].inspectTop().toString(), tableauOneTop.toString());
	}
	
	@Test
	public void tableauClickedShouldMoveKingToEmptyTableauPile() {
		Card cardToMove = new Card(0, 12); //king of hearts
		cardToMove.flip();

		
		while(!Game.board.tableau[1].isEmpty()) {
			Game.board.tableau[1].removeTop(true);
		}
		
		Game.board.tableau[0].inspectTop().flip();
		Game.board.tableau[0].addCard(cardToMove, true);

		game.select(15, Card.HEIGHT + 65);

		assertEquals(Game.board.tableau[0].size(), 1);
		assertFalse(Game.board.tableau[0].inspectTop().isFaceUp());
		assertEquals(Game.board.tableau[1].size(), 1);
		assertEquals(Game.board.tableau[1].inspectTop().toString(), cardToMove.toString());
		
	}
	
	@Test
	public void tableauClickedShouldMoveAceOfHeartsToFoundation() {
		Card cardToMove = new Card(0, 0); //ace of hearts
		cardToMove.flip();

		Game.board.tableau[0].inspectTop().flip();
		Game.board.tableau[0].addCard(cardToMove, true);

		game.select(15, Card.HEIGHT + 65);

		assertEquals(Game.board.tableau[0].size(), 1);
		assertFalse(Game.board.tableau[0].inspectTop().isFaceUp());
		assertEquals(Game.board.foundations[0].size(), 1);
		assertEquals(Game.board.foundations[0].inspectTop().toString(), cardToMove.toString());
	}
	
	@Test
	public void tableauClickedShouldMoveTwoHeartsToTableauWithThreeClub() {
		Card twoHearts = new Card(0, 1); //two of hearts
		Card threeClub = new Card(3, 2); //three of club
		twoHearts.flip();
		threeClub.flip();

		
		while(!Game.board.tableau[1].isEmpty()) {
			Game.board.tableau[1].removeTop(true);
		}
		
		Game.board.tableau[0].inspectTop().flip();
		Game.board.tableau[0].addCard(twoHearts, true);
		Game.board.tableau[1].addCard(threeClub, true);

		game.select(15, Card.HEIGHT + 65);

		assertEquals(Game.board.tableau[0].size(), 1);
		assertFalse(Game.board.tableau[0].inspectTop().isFaceUp());
		assertEquals(Game.board.tableau[1].size(), 2);
		assertEquals(Game.board.tableau[1].inspectTop().toString(), twoHearts.toString());
	}
}
