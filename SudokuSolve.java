package sudoku;

import java.util.Arrays;
import java.util.TreeSet;

public class SudokuSolve {
	public class Quadrant {
		int rmin, rmax, cmin, cmax;

		/**
		 * 
		 * @param rmin row minimum bound
		 * @param rmax row maximum bound
		 * @param cmin column minimum bound
		 * @param cmax column maximum bound
		 */
		public Quadrant(int rmin, int rmax, int cmin, int cmax) {
			this.rmin = rmin;
			this.rmax = rmax;
			this.cmin = cmin;
			this.cmax = cmax;
		}

		public boolean contains(int[][] board, int target) {
			int[] seen = new int[10];
			for (int row = rmin; row < rmax; row++) {
				for (int col = cmin; col < cmax; col++) {
					int cell = board[row][col];
					if (cell > 0 && cell <= 9)
						seen[cell]++;
				}
			}
			return seen[target] >= 1;
		}
	}

	// defining constants for later use. these define the bounds of our quadrants.
	Quadrant TOPLEFT = new Quadrant(0, 3, 0, 3);
	Quadrant TOPMID = new Quadrant(3, 6, 0, 3);
	Quadrant TOPRIGHT = new Quadrant(6, 9, 0, 3);
	Quadrant MIDLEFT = new Quadrant(0, 3, 3, 6);
	Quadrant MIDMID = new Quadrant(3, 6, 3, 6);
	Quadrant MIDRIGHT = new Quadrant(6, 9, 3, 6);
	Quadrant BOTTOMLEFT = new Quadrant(0, 3, 6, 9);
	Quadrant BOTTOMMID = new Quadrant(3, 6, 6, 9);
	Quadrant BOTTOMRIGHT = new Quadrant(6, 9, 6, 9);
	public final Quadrant[] quads = { TOPLEFT, TOPMID, TOPRIGHT, MIDLEFT, MIDMID, MIDRIGHT, BOTTOMLEFT, BOTTOMMID,
			BOTTOMRIGHT };

	public boolean solve(int[][] board) {
		boolean playable = playableBoard(board);
		if (!playable)
			return false;

		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (isEmpty(board, r, c)) {
					for (int tryNum = 1; tryNum < 10; tryNum++) {

						boolean isAnswer = helperSolve(board, r, c, tryNum);
						if (isAnswer) {
							for (int i = 0; i < 9; i++) {
								System.out.println(Arrays.toString(board[i]));
							}
							return isAnswer;
						}

					}

				}

			}
		}
		return false;

	}

	public boolean playableBoard(int[][] board) {
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (board[r][c] > 9 || board[r][c] < 0) {
					return false;
				}
			}
		}

		// Check to see if there is more than one of the same number in the same row
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				for (int cc = 0; cc < 9; cc++) {
					if (cc == c)
						continue;
					if (board[r][c] == 0 || board[r][cc] == 0)
						continue;
					if (board[r][c] == board[r][cc])
						return false;
				}
			}
		}

		// Check to see if there is more than one of the same number in the same column
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				for (int rr = 0; rr < 9; rr++) {
					if (rr == r)
						continue;
					if (board[r][c] == 0 || board[rr][c] == 0)
						continue;
					if (board[r][c] == board[rr][c])
						return false;
				}
			}
		}

		// Check all of the quadrants and make sure that there are no duplicate numbers
		// in the quadrant.
		boolean check;
		for (Quadrant quad : quads) {
			check = checkQuadrant(board, quad);
			if (!check)
				return false;
		}
		return true;
	}

	/*
	 * Checks board quadrant for duplicate values. Returns false if there are
	 * duplicates.
	 * 
	 * @param board
	 * @param quadrant
	 * @return boolean
	 */
	public boolean checkQuadrant(int[][] board, Quadrant quad) {
		int rmin = quad.rmin;
		int rmax = quad.rmax;
		int cmin = quad.cmin;
		int cmax = quad.cmax;
		for (int r = rmin; r < rmax; r++) {
			for (int c = cmin; c < cmax; c++) {
				for (int rr = rmin; rr < rmax; rr++) {
					for (int cc = cmin; cc < cmax; cc++) {
						if (r == rr && c == cc)
							continue;
						if (board[r][c] == 0 || board[rr][cc] == 0)
							continue;
						if (board[r][c] == board[rr][cc])
							return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Recursive helper function utilizing recursive backtracking to find a solution.
	 * @param board
	 * @param row
	 * @param col
	 * @param tryNum
	 * @return
	 */
	private boolean helperSolve(int[][] board, int row, int col, int tryNum) {
		/*
		 * Given a number to put in, I should check 1. tryNum is not a duplicate in
		 * Sub-Square 2. tryNum is not a duplicate in Col (horizontal) 3. tryNum is not
		 * a duplicate in Row (vertical) Return false above cases.
		 */
		for(Quadrant quad : quads) {
			// if cell in quadrant, and the quadrant contains the value, return false.
			if(determineQuadrant(row, col) == quad && quad.contains(board, tryNum)) return false;
		}

		// 2. check column for the number we want to place, tryNum
		if(columnContains(board, col, tryNum)) return false;

		// 3. check row (vertically for duplicate)
		if(rowContains(board, row, tryNum)) return false;

		// At this point, I should add the tryNum to the board.
		board[row][col] = tryNum;

		// After adding, check did I just win
		if (isWon(board)) {
			return true;
		}

		// Now I need to do the recursion.
		outterLoop: for (int r = row; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (isEmpty(board, r, c)) {

					for (int numToTry = 1; numToTry < 10; numToTry++) {
						boolean isAnswer = helperSolve(board, r, c, numToTry);
						if (numToTry == 9 && isAnswer == false) {
							break outterLoop;
						}
						if (isAnswer != false) {
							return true;
						}

					}

				}

			}
		}

		board[row][col] = 0;
		return false;

	}

	private boolean isWon(int[][] board) {
		for (int r = 8; r >= 0; r--) {
			for (int c = 8; c >= 0; c--) {
				if (board[r][c] == 0) {// If any empty cell detected, return false.
					return false;
				}
			}
		}
		return true;
	}

	private boolean isEmpty(int[][] board, int row, int col) {

		if (board[row][col] == 0) {// if empty, return true.
			return true;
		} else {
			return false;
		}
	}

	public int[][] NewGameBoard() {
		int[][] newboard = new int[10][10];
		for (int row = 0; row <= 9; row++) {
			for (int col = 0; col <= 9; col++) {
				TreeSet<Integer> options = new TreeSet<>();
				for (int i = 1; i <= 9; i++)
					options.add(i);
				// remove options which are invalid
				Quadrant quad = determineQuadrant(row, col);
				for (int option : options) {
					// checking quadrant. removing options present.
					if (quad.contains(newboard, option))
						options.remove(option);
					// checking column
					if (columnContains(newboard, col, option))
						options.remove(option);
					// checking row
					if (rowContains(newboard, row, option))
						options.remove(option);
					// randomly select one of the available options.
				}
				int selectionIndex = (int) Math.random() * options.size();

				// int selection = options.toarray(integer[] a)[]; //

			}
		}
		return null;
	}

	/**
	 * @param row
	 * @param col
	 * @return Quadrant in which the row, col pair exists.
	 */
	public Quadrant determineQuadrant(int row, int col) {
		for (Quadrant quad : quads) {
			if (row >= quad.rmin && row < quad.rmax && col >= quad.cmin && col < quad.cmax)
				return quad;
		}
		throw new IllegalArgumentException(); // ?? out of bounds?
	}

	/**
	 * Return true if the specified column contains the target value
	 * 
	 * @param board
	 * @param col
	 * @param target
	 * @return
	 */
	public boolean columnContains(int[][] board, int col, int target) {
		for (int row = 0; row < 9; row++) {
			if (board[row][col] == target) {
				return true;
			}

		}
		return false;
	}

	/**
	 * returns true if specified row contains the target
	 * 
	 * @param board
	 * @param row
	 * @param target
	 * @return
	 */
	public boolean rowContains(int[][] board, int row, int target) {
		for (int col = 0; col < 9; col++) {
			if (board[row][col] == target) {
				return true;
			}
		}
		return false;
	}
}
