package com.cst8334_group_one_solitaire.commands;

import com.cst8334_group_one_solitaire.beans.Card;
import com.cst8334_group_one_solitaire.beans.Game;

public class FlipCard implements Command {
	
private Game game;
private Card cardToFlip;
	
	public FlipCard(Game game, Card cardToFlip) {
		this.game = game;
		this.cardToFlip = cardToFlip;
	}

	@Override
	public void execute() {
		game.flipCard(cardToFlip);
	}

	@Override
	public void undo() {
		game.flipCard(cardToFlip);
	}

}
