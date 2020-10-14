package com.cst8334_group_one_solitaire.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.cst8334_group_one_solitaire.beans.Card;
import com.cst8334_group_one_solitaire.beans.CardPile;

public class CardPileTest {

	private CardPile describedClass = new CardPile(4, 5, Card.WIDTH, Card.HEIGHT);

	@Test
	public void inspectTopShouldReturnCardOnTop() {
		Card cardToAdd = new Card(0, 0);
		Card otherCard = new Card(0, 1);
		
		describedClass.addCard(otherCard, true);
		describedClass.addCard(cardToAdd, true);
		
		String expectedResult = cardToAdd.toString();
		
		String actualResult = describedClass.inspectTop().toString();
		
		assertEquals(expectedResult, actualResult);
	}
	
	public void inspectTopShouldNotRemoveTopCard() {
		Card cardToAdd = new Card(0, 0);
		Card otherCard = new Card(0, 1);
		
		describedClass.addCard(otherCard, true);
		describedClass.addCard(cardToAdd, true);
		
		assertEquals(2, describedClass.size());
	}
	
	@Test
	public void isEmptyShouldReturnFalseWhenNotEmpty() {
		Card cardToAdd = new Card(0, 0);
		
		describedClass.addCard(cardToAdd, true);
		
		assertFalse(describedClass.isEmpty());
	}
	
	@Test
	public void removeTopShouldRemoveTopCardIfNotEmpty() {
		Card cardToAdd = new Card(0, 0);
		
		describedClass.addCard(cardToAdd, true);
		Card removedCard = describedClass.removeTop(true);
		
		assertEquals(cardToAdd.toString(), removedCard.toString());
		assertTrue(describedClass.isEmpty());		
	}
	
	@Test
	public void removeTopShouldReturnNullIfEmpty() {
		assertNull(describedClass.removeTop(true));
	}
	
	@Test
	public void isInAreaShouldReturnTrueIfInArea() {
		assertTrue(describedClass.isInArea(4, 5));
	}
	
	@Test
	public void isInAreaShouldReturnFalseIfNotInArea() {
		assertFalse(describedClass.isInArea(3, 4));
	}
	
}
