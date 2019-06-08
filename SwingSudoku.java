package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import sudoku.SudokuSolve.Quadrant;

public class SwingSudoku extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTField[][] board = new JTField[9][9];

	class JTField extends JTextField {
		private static final long serialVersionUID = 1L;
		public int row;
		public int col;

		public JTField(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingSudoku frame = new SwingSudoku();
		// frame.pack();
		// TODO: refactor to enable proper horizontal/vertical scaling
		frame.setResizable(false);
		frame.setVisible(true);

	}

	/**
	 * Create the frame.
	 */
	public SwingSudoku() {
		int[][] rawboard = new int[9][9];
		class SolveButtonEventListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent event) {
				// on click of the 'solve' button.
				// build a board from the text fields
				int[][] rawboard = new int[9][9];
				// TODO: only need to update the cell which action was performed on.
				for (int row = 0; row < 9; row++) {
					for (int col = 0; col < 9; col++) {
						try {
							String txt = board[row][col].getText();
							if (txt.contentEquals(""))
								rawboard[row][col] = 0;
							else
								rawboard[row][col] = Integer.parseInt(txt);
						} catch (Exception e) {
							// invalid inputs TODO: error msg in gui?
							System.out.println("Invalid integer input");
							break;
						}
					}
				}
				// the solve algorithm mutates state, so we're going to need a deep copy to work
				// around that.
				int[][] deepcopy = new int[9][9];
				for (int row = 0; row < 9; row++) {
					for (int col = 0; col < 9; col++) {
						deepcopy[row][col] = rawboard[row][col];
					}
				}
				// try to solve board.
				SudokuSolve solver = new SudokuSolve();
				boolean success = solver.solve(rawboard);
				if (!success) {
					// invalid/unsolvable board.
					// TODO: message user about it.
					System.out.println("Invalid input board state. Board not solvable.");
				} else { /// successful solve?
					// our deepcopy should have the original board state.
					// and our rawboard should now hold a valid solution state.
					// boardDelta will determine what changes were made
					// animate board will display them in a pretty fashion.
					animateBoard(boardDelta(deepcopy, rawboard));
				}

			}

		}
		class TextFieldEventListener implements ActionListener, FocusListener {

			@Override
			public void focusLost(FocusEvent event) {
				// get the calling text field.
//				JTField t = (JTField) event.getSource();
//				String text = t.getText();
				// if (text.equals(""))
				// return;

				// revalidate board colors
				validateColors(rawboard);
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// required for FocusListener. i don't care about it.
			}

			@Override
			public void actionPerformed(ActionEvent event) {

			}

		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		JPanel textboxgrid = new JPanel();
		textboxgrid.setLayout(new GridLayout(9, 9));

		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				JTField field = new JTField(row, col);
				TextFieldEventListener listener = new TextFieldEventListener();
				field.addActionListener(listener);
				field.addFocusListener(listener);
				field.setSize(200, 200);
				field.setPreferredSize(new Dimension(200, 70));
				field.setAlignmentY(CENTER_ALIGNMENT);
				field.setHorizontalAlignment(JTextField.CENTER);
				field.setFont(field.getFont().deriveFont(java.awt.Font.PLAIN, 40));
				board[row][col] = field;

				textboxgrid.add(field);
			}
		}
		contentPane.add(textboxgrid, BorderLayout.NORTH);

		JPanel buttonsgrid = new JPanel();
		buttonsgrid.setLayout(new GridLayout(0, 2));
		JButton submit = new JButton();
		submit.setText("Solve");
		submit.addActionListener(new SolveButtonEventListener());

		buttonsgrid.add(submit);

		JButton randomButton = new JButton();
		randomButton.setText("Random Board");
		buttonsgrid.add(randomButton);

		contentPane.add(buttonsgrid, BorderLayout.SOUTH);

		setContentPane(contentPane);
		setDefaultColors();
	}

	public void setDefaultColors() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				setColor(row, col, Color.white);
				if (row < 3 || row > 5)
					setColor(row, col, Color.LIGHT_GRAY);
				if (col > 2 && col < 6) {
					if (board[row][col].getBackground() == Color.LIGHT_GRAY) {
						setColor(row, col, Color.white);
					} else {
						setColor(row, col, Color.LIGHT_GRAY);
					}
				}
			}
		}

	}

	public int[] getQuadrant(int row, int col) {
		// return the boundaries in the form [minrow, maxrow, mincol, maxcol]
		return null;
	}

	/**
	 * @param minRow
	 * @param maxRow
	 * @param minCol
	 * @param maxCol
	 * @param target
	 * @return boolean
	 */
	public boolean searchQuadrant(int minRow, int maxRow, int minCol, int maxCol, int target) {
		// returns true if target is found within these boundaries.
		// if (row >= northBound && row <= southBound && col >= westBound && col <=
		// eastBound) {
		for (Integer row = minRow; row <= maxRow; row++) {
			for (Integer col = minCol; col <= maxCol; col++) {
				try {
					if (Integer.parseInt(board[row][col].getText()) == target) {
						return true;
					}
				} catch (Exception e) {
					continue; // just an empty board spot, not a concern.
				}

			}
		}
		return false;
	}

	public void setColor(int row, int col, Color color) {
		board[row][col].setBackground(color);
	}

	public void setRowColor(int row, Color color) {
		for (JTextField field : board[row]) {
			field.setBackground(color);
		}
	}

	public void setColColor(int col, Color color) {
		for (int row = 0; row < 9; row++) {
			board[row][col].setBackground(color);
		}
	}
	public void setQuadColor(Quadrant quad, Color color) {
		for (int row = quad.rmin; row < quad.rmax; row++) {
			for (int col = quad.cmin; col < quad.cmax; col++) {
				setColor(row, col, color);
			}
		}
	}
	
	public void setBoard(int[][] rawboard) {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				board[row][col].setText(Integer.toString(rawboard[row][col]));
			}
		}
	}

	public int[][] boardDelta(int[][] rawboard, int[][] solvedBoard) {
		// given a board state
		// TODO: refactor to return a list of coordinates rather than a full boardstate?
		int[][] delta = new int[9][9];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				// if 'board' and 'solvedboard' aren't the same, i assume it was changed/solved,
				// and return the solution.
				// otherwise, return 0 for an empty cell, as was the standard proposed in the
				// solver class.
				delta[row][col] = (rawboard[row][col] == solvedBoard[row][col]) ? 0 : solvedBoard[row][col];
			}
		}
		return delta;
	}

	public void animateBoard(int[][] boardDelta) {
		setDefaultColors(); // clean up any colors in prep for animation.
		// in order to not lock up the gui, we need to spawn a new thread
		// to handle the changes.
		class UpdaterThread extends Thread {
			public void run() {
				for (int row = 0; row < 9; row++) {
					for (int col = 0; col < 9; col++) {
						int cell = boardDelta[row][col];
						if (cell == 0)
							continue; // empty cell. there were no changes.
						for (int i = 1; i <= cell; i++) {
							// System.out.println(row + " " + col);
							// it'd be cool to cycle through all the numbers which were attempted in the
							// cell here.
							board[row][col].setText(Integer.toString(i));
							board[row][col].repaint();
							// board[row][col].repaint();
							// too fast! slow it down so i can see!
							long starttime = System.currentTimeMillis();

							while (System.currentTimeMillis() != starttime + 30)
								continue; // this is super inefficient?
						}
					}
				}
			}
		}
		UpdaterThread t = new UpdaterThread();
		t.start();
	}

	public void validateColors(int[][] rawboard) {
		setDefaultColors();
		SudokuSolve solver = new SudokuSolve();

		for (int row = 0; row < 9; row++) {
			int[] rowseen = new int[10]; // has the row seen these numbers before?

			for (int col = 0; col < 9; col++) {
				JTField f = board[row][col];
				String c = f.getText();
				int i = 0;
				if (c.equals(""))
					continue;
				try {
					i = Integer.parseInt(c);
				} catch (Exception e) { // invalid text
					// System.out.println("invalid 312");
					setRowColor(row, Color.red);
					setColColor(col, Color.red);
				}
				if (i > 9 || i <= 0) { // invalid entry.
//					System.out.println("317");
					setRowColor(row, Color.red);
					setColColor(col, Color.red);
				} else {
					rowseen[i]++;
				}

				// check the rows
				int[] colseen = new int[10];
				for (int j = 0; j < 9; j++) { // record numbers in the column.
					int l = 0;
					String t = board[j][col].getText();
					if (t.equals(""))
						continue;
					try {
						l = Integer.parseInt(t);
					} catch (Exception e) { // invalid text
						// System.out.println("332 " + e.toString());
						setRowColor(row, Color.red);
						setColColor(col, Color.red);
					}
					if (l <= 9 && l > 0)
						colseen[l]++;
				}
				for (int item : colseen) {
					if (item > 1) {
						// System.out.println("340");
						setColColor(col, Color.red);
					}
				}
			}
			// any in this row exist more than once?
			for (int item : rowseen) {
				if (item > 1) { // previously seen this number in this row!
					setRowColor(row, Color.red);
				}
			}
		}

		for (Quadrant quad : solver.quads) {
			// validate the quadrant
			int[] quadseen = new int[10];
			for (int row = quad.rmin; row < quad.rmax; row++) {
				for (int col = quad.cmin; col < quad.cmax; col++) {
					int val = 0;
					String txt = board[row][col].getText();
					if (txt.equals(""))
						continue;
					try {
						val = Integer.parseInt(txt);
					} catch (Exception e) {
						setQuadColor(quad, Color.red);
					}
					if (val <= 9 && val > 0)
						quadseen[val]++;
				}
			}
			for (int item : quadseen) {
				if (item > 1) { // previously seen this number, color the quadrant.
					setQuadColor(quad, Color.red);
				}
			}
		}
	}
}
