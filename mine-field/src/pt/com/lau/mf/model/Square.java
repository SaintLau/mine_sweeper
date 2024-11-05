package pt.com.lau.mf.model;

import java.util.ArrayList;
import java.util.List;

import pt.com.lau.mf.exception.ExplosionException;

public class Square {
	
	// cada quadradinho tem uma linha e coluna
	private final int row;
	private final int column;
	
	private boolean open; // por padrao começa fechado -> primitivo boolean
	private boolean mine; // quadrado minado
	private boolean marked; // tem mina e deixa a bandeirinha
	
	// quando clica no campo o quadrado expande até vizinhança ser segura
	private List<Square> neighbours = new ArrayList<>(); // relacao 1 para n consigo proprio

	
	 Square(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	 // ver que números estão à volta
	boolean addNeighbour(Square neighbour) {
		boolean differentRow = row != neighbour.row;
		boolean differentColumn = column != neighbour.column;
		// se forem diferentes temos diagonal
		boolean diagonal = differentRow && differentColumn;
		
		// cal difference:
		int deltaRow = Math.abs(row - neighbour.row);
		int deltaColumn = Math.abs(column - neighbour.column);
		
		// sum
		int deltaGeneral = deltaRow + deltaColumn;
		
		// dois cenarios
		if(deltaGeneral == 1 && !diagonal) {
			neighbours.add(neighbour);
			return true;
		} else if (deltaGeneral == 2 && diagonal){
			neighbours.add(neighbour);
			return true;
		} else {
			return false;
		}
	}
	
	// alternar entre a bandeira e abrir o quadrado
	void changeMark() {
		if(!open) {
			marked = !marked;
		}
	}
	
	// 
	boolean open() {
		if(!open && !marked) {
			open = true;
			
			if(mine) { // aqui fomos criar uma exception 
				throw new ExplosionException(); 
			} 
			if(neighbourSafe()) { // se a zona à volta é segura
				neighbours.forEach(n -> n.open()); // RECURSÃO (chamar o metodo dentro dele). Se vizinhança segura então abre
			}
			return true; // quadrado aberto
		} else {
		return false; // nao abriu campo, não significa que tenha mesmo mina
		}
	}
	
	// quadrados sem mina à volta
	boolean neighbourSafe() {
		return neighbours.stream().noneMatch(n -> n.mine); // n de neighbour...Se FALSE é bomba
	}
	
	// método se tem mina 
	void mined() {
		mine = true;
	}
	
	public boolean isMined() {
		return mine;
	}
	
	public boolean isMarked() { // funciona como um getter
		return marked;
	}
	
	// metodo para mostrar as minas no fim do jogo: atraves de set and getter automatios e tirar o public
	boolean isOpen() {
		return open;
	}
	
	void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isClosed() {
		return !open;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	// se para quadrado os objectivos foram alcançados: abrir campo seguro e marcar campo com bomba E isto para todos os campos do tabuleiros
	boolean objectiveReached() {
		boolean unveil = !mine && open;
		boolean protect = mine && marked; // campo da bandeira
		return unveil || protect; // se campo está desvendado ou protegido, atingiu objectivo para o quadrado. Para o objectivo final, é isto para todos os quadrados
	}
	
	// quantidades de minas na vizinhança
	long minesInNeighbor() {
		return neighbours.stream().filter(v -> v.mine).count();
	}
	
	// restart game
	void restart() {
		open = false;
		mine = false;
		marked = false;
	}
	
	@Override
	// definir método toString por mostrar mina no terminal 
	public String toString() {
		if(marked) {
			return "x";
		} else if (open && mine) {
			return "*";
		} else if (open && minesInNeighbor() > 0) {
			return Long.toString(minesInNeighbor());
		} else if (open) {
			return " ";
		} else {
			return "?";
		}
	}
}
