package com.cst8334_group_one_solitaire.commands;
import com.cst8334_group_one_solitaire.beans.CardPile;
import com.cst8334_group_one_solitaire.beans.Game;

public class MoveCard implements Command{
	
	private Game game;
	private CardPile fromPile;
	private CardPile toPile;
	
	public MoveCard (Game game, CardPile fromPile, CardPile toPile) {
		this.fromPile = fromPile;
		this.toPile = toPile;
		this.game = game;
	}
	

	@Override
	public void execute() {
		game.moveCard(fromPile, toPile);
	}

	@Override
	public void undo() {
		game.moveCard(toPile, fromPile);
	}
	
	
	
}
