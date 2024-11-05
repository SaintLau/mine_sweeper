package pt.com.lau.mf.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.com.lau.mf.exception.ExplosionException;

public class SquareTest {

	private Square square;
	
	@BeforeEach
	void startSquare() {
		square = new Square(3, 3);
	}

	@Test
	void testNeighbourDistance1Left() {
		Square neighbour = new Square(3, 2);
		boolean result = square.addNeighbour(neighbour);
		assertTrue(result);
	}
	
	@Test
	void testNeighbourDistance1Right() {
		Square neighbour = new Square(3, 4);
		boolean result = square.addNeighbour(neighbour);
		assertTrue(result);
	}
	
	@Test
	void testNeighbourDistance1Up() {
		Square neighbour = new Square(2, 3);
		boolean result = square.addNeighbour(neighbour);
		assertTrue(result);
	}

	@Test
	void testNeighbourDistance1Down() {
		Square neighbour = new Square(4, 3);
		boolean result = square.addNeighbour(neighbour);
		assertTrue(result);
	}

	@Test
	void testNeighbourDiagonal() {
		Square neighbour = new Square(2, 2);
		boolean result = square.addNeighbour(neighbour);
		assertTrue(result);
	}

	@Test
	void testNotNeighbour() {
		Square neighbour = new Square(1, 1); // tem que ser falso
		boolean result = square.addNeighbour(neighbour);
		// assertTrue(result); // espera resultado verdadeiro
		assertFalse(result);
	}
	
	@Test
	// por padrao o valor é falso
	void testDefaultValueMarked () {
		assertFalse(square.isMarked());
	}

	@Test
	// se default é false, quando altera uma vez passa a true
	void testChangeMark () { 
		square.changeMark();
		assertTrue(square.isMarked());
	}
	
	@Test
	// se começa false, é chamado passa a true e quando chamado novamente passa a false
	void testChangeMarkTwoTimes() { 
		square.changeMark();
		square.changeMark();
		assertFalse(square.isMarked());
	}

	@Test
	// sem mina e sem estar marcado -> default
	void testOpenNotMineNotMarked() { 
		assertTrue(square.open());
	}
	
	@Test
	// sem mina e mas marcado -> default
	void testOpenNotMineButMarked() { 
		square.changeMark();
		assertFalse(square.open());
	}

	@Test
	// com mina e mas marcado 
	void testOpenMineAndMarked() { 
		square.changeMark();
		square.mined();
		assertFalse(square.open());
	}

	@Test
	// com mina e mas nao marcado -> GERA EXPLOSION e gera exception
	void testOpenMineAndNotMarked() { 
		square.mined();
		// se a exception for chamada, lança a exception
		assertThrows(ExplosionException.class, () -> {
			square.open();
		});
	}
	
	@Test
	// com vizinhos
	void testOpenWithneighbors() { 

		Square square11 = new Square(1, 1);
		Square square22 = new Square(2, 2);
		
		square22.addNeighbour(square11);
		square.addNeighbour(square22);
		
		square.open(); // abri o campo actual que é o (3,3)
		
		assertTrue(square22.isOpen() && square11.isOpen()); // enquanto a vizinhanca estiver segura, vai abrindo
	}
	
	@Test
	// com campo minado como vizinho
	void testOpenWithneighbors2() { 

		Square square11 = new Square(1, 1);
		
		Square square12 = new Square(1,1); // vai ter mina
		square12.mined(); // mina
		
		Square square22 = new Square(2, 2);
		square22.addNeighbour(square11);
		square22.addNeighbour(square12);
		
		square.addNeighbour(square22);
		square.open(); 
		
		/*
		 *  square11 é para estar fechado
		 * */
		
		assertTrue(square22.isOpen() && square11.isClosed()); 
	}
	
}
