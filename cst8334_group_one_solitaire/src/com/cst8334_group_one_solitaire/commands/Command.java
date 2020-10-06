package com.cst8334_group_one_solitaire.commands;

/**
 * Interface used to create commands
 */
public interface Command {
	
	public void execute();
	public void undo();

}
