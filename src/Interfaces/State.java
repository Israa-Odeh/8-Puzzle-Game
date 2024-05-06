/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author user
 */
public class State {
	int [][] board = new int [3][3];
	State parent;
	String func;
	int cost;
	int heurestic;
	int f;
	
	public State() {
		
	}
        
	public State(int [][] board, State p, String func, int c, int h) {
		// TODO Auto-generated constructor stub
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				this.board[i][j] = board [i][j];
			}
		}
		parent = p;
		this.func = func;
		cost = c;
		heurestic = h;
		f = c + h;
	}
	
	//we need to override the 'equals' func.
}
