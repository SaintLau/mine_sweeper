package pt.com.lau.mf;

import pt.com.lau.mf.model.Board;
import pt.com.lau.mf.vision.TerminalBoard;

public class App {
	
	public static void main(String[] args) {
		
		Board board = new Board(6, 6, 6); // vai imprimir um campo com 6 linhas, 6 colunas e 6 minas
		
		new TerminalBoard(board); // ao iniciar por aqui, o jogo começa logo
		
		/*
		board.open(3, 3);
		board.changeMark(4, 4);
		board.changeMark(4, 5);
		
		System.out.println(board);
		*/
	}

}