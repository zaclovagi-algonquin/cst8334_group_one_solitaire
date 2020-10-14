package com.cst8334_group_one_solitaire.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import com.cst8334_group_one_solitaire.beans.Card;

public class CardTest {
	
	private Card describedClass = new Card(0, 0);

	
	@Test
	public void shouldReturnCorrectSuit() {
		int expectedResult = 0;
		
		int actualResult = describedClass.getSuit();
		
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void shouldReturnCorrectRank() {
		int expectedResult = 0;
		
		int actualResult = describedClass.getRank();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void shouldDefaultToFaceUp() {
		assertFalse(describedClass.isFaceUp());
	}
	
	@Test
	public void flipMethodShouldCorrectlyFlipCard() {
		describedClass.flip();
		
		assertTrue(describedClass.isFaceUp());
	}
	
	@Test
	public void getColorShouldReturnRedForHeart() {
		assertEquals(describedClass.getColor(), Color.RED);
	}
}
