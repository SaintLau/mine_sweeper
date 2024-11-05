package pt.com.lau.mf.vision;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import pt.com.lau.mf.exception.ExitException;
import pt.com.lau.mf.exception.ExplosionException;
import pt.com.lau.mf.model.Board;

public class TerminalBoard {

	/*
	 *  Class que vamos usar na App.java
	 *  
	 *  - vai interagir com o user
	 * */
	
	
	private Board board;
	private Scanner scanner = new Scanner(System.in);

	public TerminalBoard(Board board) {
		this.board = board;
		startGame();
	}

	private void startGame() {
		try {
			boolean keepGoing = true;
			
			while(keepGoing) {
				gameCicle();
				
				System.out.println("Another Game? (Y/n ");
				String answer = scanner.nextLine();
				
				if("n".equalsIgnoreCase(answer)) {
					keepGoing = false;
				} else {
					board.restart();
				}
			}
			
		} catch(ExitException e) {
			System.out.println("See you next time, Sweeper");
		} finally {
			scanner.close();
		}
		
	}

	// fluxo principal do jogo
	private void gameCicle() {
		try {
			// objectivo alcançado? 
			while(!board.objectiveReached()) {
				System.out.println(board);
//				System.out.println(board.toString);  tambem pode ser
				
				String digit = digitValue("Digit (x, y): ");
			// 	System.out.println(Arrays.toString(digit.split(",")));
				Iterator<Integer> xy = Arrays.stream(digit.split(",")) // abrir o quadrado ou desmarcar
						.map(e -> Integer.parseInt(e.trim())).iterator();
				
/*				System.out.println(xy.next());
				System.out.println(xy.next());
	*/			
				digit = digitValue("1 - Open game or 2 - (Un)Mark: ");
				
				if("1".equals(digit)) {
					board.open(xy.next(), xy.next());
				} else if ("2".equals(digit)) {
					board.changeMark(xy.next(), xy.next());
				}
				
			}
			System.out.println(board);
			System.out.println("You win");
		} catch (ExplosionException e) {
			// como temos a exception de mostrar as bombas quando se perde:
			System.out.println(board);
			System.out.println("You lose!");
		}
	}
	
	private String digitValue(String text) {
		System.out.print(text);
		String digit = scanner.nextLine();
		
		if("exit".equalsIgnoreCase(digit)) {
			throw new ExitException();
		}
		return digit;
	}
	
}
