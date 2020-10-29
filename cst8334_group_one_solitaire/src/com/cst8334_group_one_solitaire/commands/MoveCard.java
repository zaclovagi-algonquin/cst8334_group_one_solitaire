package com.cst8334_group_one_solitaire.commands;

import com.cst8334_group_one_solitaire.beans.CardPile;
import com.cst8334_group_one_solitaire.beans.Game;

public class MoveCard implements Command {

    private final Game game;
    private final CardPile fromPile;
    private final CardPile toPile;
    private final int score;

    public MoveCard(Game game, CardPile fromPile, CardPile toPile, int score) {
        this.fromPile = fromPile;
        this.toPile = toPile;
        this.game = game;
        this.score = score;
    }


    @Override
    public void execute() {
        game.moveCard(fromPile, toPile);
        game.setScore(score);
    }

    @Override
    public void undo() {
        game.moveCard(toPile, fromPile);
        game.setScore(-score);
    }


}
