package com.cst8334_group_one_solitaire.commands;

import com.cst8334_group_one_solitaire.beans.Game;

public class DrawCard implements Command{
	
	private Game game;
	
	public DrawCard (Game game) {
		this.game = game;
	}

	@Override
	public void execute() {
		game.drawFromDeck();
		
	}

	@Override
	public void undo() {
		game.addToDeck();
		
	}

}
