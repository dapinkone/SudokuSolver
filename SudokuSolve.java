package sudoku;

import java.util.Arrays;

public class SudokuSolve {

	public boolean solve(int[][] board) {

		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (isEmpty(board, r, c)) {
					for (int tryNum = 1; tryNum < 10; tryNum++) {

						boolean isAnswer = helperSolve(board, r, c, tryNum);
						if (isAnswer != false) {
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

	private boolean helperSolve(int[][] board, int row, int col, int tryNum) {
		/*
		 * Given a number to put in, I should check 1. tryNum is not a duplicate in
		 * Sub-Square 2. tryNum is not a duplicate in Col (horizontal) 3. tryNum is not
		 * a duplicate in Row (vertical) Return false above cases.
		 */

		// 1st square --> top-left
		if (row <= 2 && col <= 2) {// 1b
			for (int r1 = 0; r1 < 3; r1++) {
				for (int c1 = 0; c1 < 3; c1++) {
					if (board[r1][c1] == tryNum) {
						return false;
					}
				}
			}
		}
		// 2nd square --> top-middle
		if (row <= 2 && col > 2 && col <= 5) {
			for (int r2 = 0; r2 < 3; r2++) {
				for (int c2 = 3; c2 < 6; c2++) {
					if (board[r2][c2] == tryNum) {
						return false;
					}
				}
			}
		}
		// 3rd square --> top-right
		if (row <= 2 && col > 5) {
			for (int r3 = 0; r3 < 3; r3++) {
				for (int c3 = 6; c3 < 9; c3++) {
					if (board[r3][c3] == tryNum) {
						return false;
					}
				}
			}

		}
		// 4th square --> middle-left
		if (row > 2 && row <= 5 && col <= 2) {
			for (int r4 = 3; r4 < 6; r4++) {
				for (int c4 = 0; c4 < 3; c4++) {
					if (board[r4][c4] == tryNum) {
						return false;
					}
				}
			}

		}
		// 5th square --> middle-middle
		if (row > 2 && row <= 5 && col > 2 && col <= 5) {
			for (int r5 = 3; r5 < 6; r5++) {
				for (int c5 = 3; c5 < 6; c5++) {
					if (board[r5][c5] == tryNum) {
						return false;
					}
				}
			}
		}
		// 6th square --> middle-right
		if (row > 2 && row <= 5 && col > 5) {
			for (int r6 = 3; r6 < 6; r6++) {
				for (int c6 = 6; c6 < 9; c6++) {
					if (board[r6][c6] == tryNum) {
						return false;
					}
				}
			}

		}
		// 7th square --> bottom-left
		if (row > 5 && col <= 2) {
			for (int r7 = 6; r7 < 9; r7++) {
				for (int c7 = 0; c7 < 3; c7++) {
					if (board[r7][c7] == tryNum) {
						return false;
					}
				}
			}

		}
		// 8th square --> bottom-middle
		if (row > 5 && col > 2 && col <= 5) {
			for (int r8 = 6; r8 < 9; r8++) {
				for (int c8 = 3; c8 < 6; c8++) {
					if (board[r8][c8] == tryNum) {
						return false;
					}
				}
			}

		}
		// 9th square --> bottom-right
		if (row > 5 && col > 5) {
			for (int r9 = 6; r9 < 9; r9++) {
				for (int c9 = 6; c9 < 9; c9++) {
					if (board[r9][c9] == tryNum) {
						return false;
					}
				}
			}

		}

		// 2. check col (horizontally for duplicate)
		for (int rowChecker = row; rowChecker < row + 1; rowChecker++) {
			for (int horiChecker = 0; horiChecker < 9; horiChecker++) {
				if (board[rowChecker][horiChecker] == tryNum) {
					return false;
				}
			}
		}

		// 3. check row (vertically for duplicate)
		for (int colChecker = col; colChecker < col + 1; colChecker++) {
			for (int vertiChecker = 0; vertiChecker < 9; vertiChecker++) {
				if (board[vertiChecker][colChecker] == tryNum) {
					return false;
				}
			}
		}

		// At this point, I should add the tryNum to the board.
		board[row][col] = tryNum;
		// for(int i = 0; i < 9; i ++) {
		// System.out.println(Arrays.toString(board[i]));
		// }
		// System.out.println();
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
}
