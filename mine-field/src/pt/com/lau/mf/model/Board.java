package pt.com.lau.mf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import pt.com.lau.mf.exception.ExplosionException;

public class Board {
	
	private int amountRows;
	private int amountColumns;
	private int amountMines;
	
	// constante:
	private final List<Square> squares = new ArrayList<>(); // quantidade de linhas e colunas 

	// construtor:  atalho cmd + option + S -> generate constructor using fields
	public Board(int amountRows, int amountColumns, int amountMines) {
		this.amountRows = amountRows;
		this.amountColumns = amountColumns;
		this.amountMines = amountMines;
		
		// inicializar o objecto para iniciar o jogo
		// 3 métodos:
		generateSquares();
		associateNeighbors();
		randomMines();
	}
	
	// funcoes para iniciar o jogo
	public void open(int row, int column) {
		try {
		// criar pipe line
		// propcurar dentro da lista
		squares.parallelStream()
				.filter(s -> s.getRow() == row && s.getColumn() == column)
				.findFirst() // apanha o primeiro elemento
				.ifPresent(s -> s.open());
	} catch(ExplosionException e) {
		// como temos a exception tratada  (try) podemos escrever algo antes e chamar apenas com o throw
		squares.forEach(s -> s.setOpen(true));
		throw e;
		}
	}
	
	// metodo para marcar o quadrado:
	public void changeMark(int row, int column) {
		squares.parallelStream()
				.filter(s -> s.getRow() == row && s.getColumn() == column)
				.findFirst() // apanha o primeiro elemento
				.ifPresent(s -> s.changeMark());
	} 

	private void generateSquares() {
		for (int i = 0; i < amountRows; i++) {
			for(int j = 0; j < amountColumns; j++) {
				squares.add(new Square(i, j)); // passa o valor as linhas e colunas
			}
		}
	}
	
	// percorrer a lista duas vezes para associar tudo
	private void associateNeighbors() {
		for( Square s1 : squares) {
			for (Square s2 : squares) {
				s1.addNeighbour(s2);
			}
		}
	}

	// sempre que restart game este vai
	private void randomMines() {
		 // Pode ser implementado de várias formas -> Do..While ou While...etc,
		
		long armedMines = 0; // passamos para long por termos o count no loop
		Predicate<Square> mined = s -> s.isMined();
		
		do {
			int random =(int)  (Math.random() * squares.size()); // quantos campos minados? Precisa de ir variando dentro do array. NECESSARIO O CAST POR DAR UM VALOR DECIMAR E PRECISAMOS DE UM INTEIRO
			squares.get(random).mined();
			// tem que vir no fim para que o número de minas seja de acordo com o pretendido
			armedMines = squares.stream().filter(mined).count(); // quantidade de campos minados conseguimos saber o n. de minas. COUNT devolve um LONG e temos que ou passar um cast() passar a variavel para long
		} while(armedMines < amountMines); // enquanto a quantidade de minas armadas for menor que a quantidade de minas que se tem como requisito
	}
	
	public boolean objectiveReached() {
		return squares.stream().allMatch(s -> s.objectiveReached()); // todos os campos tem objectivo alcançado? 
	}
	
	// restart e isso restart todos os campos
	public void restart() {
		squares.stream().forEach(s -> s.restart()); 
		randomMines(); // reinicia as minas
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		// adicionar os indices das colunas:
		sb.append("  "); // to align with board column
		for (int columns = 0;  columns < amountColumns; columns++) {
			sb.append(" " );
			sb.append(columns);
			sb.append(" " );
		}
		sb.append("\n");

		// rows:
		int i = 0;
		for(int r = 0; r < amountRows;  r++) {
			// add row number:
			sb.append(r);
			sb.append(" ");
			// columns
			for(int c = 0; c < amountColumns; c++) {
				sb.append(" ");
				sb.append(squares.get(i));
				sb.append(" ");
				i++;
			}
			sb.append("\n"); // para ter a quebra de linha e ter a forma de grid
		}
		
		return sb.toString();
	}

}
