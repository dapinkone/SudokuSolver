package sudoku;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SwingSudoku extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTField[][] board = new JTField[9][9];;

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
		frame.setVisible(true);

	}

	/**
	 * Create the frame.
	 */
	public SwingSudoku() {

		class SolveButtonEventListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent event) {
				// on click of the 'solve' button.
				// build a board from the text fields
				int[][] rawboard = new int[9][9];
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
				// try to solve board.
				SudokuSolve solver = new SudokuSolve();
				boolean success = solver.solve(rawboard);
				if (!success) {
					// invalid/unsolvable board.
					// TODO: message user about it.
					System.out.println("Invalid input board state. Board not solvable.");
				} else { /// successful solve?
					setDefaultColors();
					setBoard(rawboard);
				}

			}

		}
		class TextFieldEventListener implements ActionListener, FocusListener {

			@Override
			public void focusLost(FocusEvent event) {
				// get the calling text field.
				JTField t = (JTField) event.getSource();
				String text = t.getText();
				if (text.equals(""))
					return;

				// TODO: throw these checks into methods.
				boolean rowmatch = false;
				for (JTField f : board[t.row]) {
					if (f.col == t.col)
						continue; // same text field.
					String c = f.getText();
					if (c.equals(""))
						continue;
					if (c.equalsIgnoreCase(text)) {
						rowmatch = true;
						break;
					}
				}

				boolean colmatch = false;
				for (int row = 0; row < 9; row++) {
					// check the column
					if (board[row][t.col].row == t.row)
						continue; // same text field.
					String c = board[row][t.col].getText();
					if (c.equals(""))
						continue;
					if (c.equalsIgnoreCase(text)) {
						colmatch = true;
						break;
					}
				}
				// colors!!!
				if (!colmatch) { // greens color first, so reds come out on top.
					setColColor(t.col, Color.green);
				}

				if (!rowmatch) {
					setRowColor(t.row, Color.green);
				}
				if (rowmatch) {
					setRowColor(t.row, Color.red);
				}
				if (colmatch) {
					setColColor(t.col, Color.red);
				}
//				setRowColor(t.row, Color.green);
//				setColColor(t.col, Color.green.darker());
//				setColor(t.row,t.col, Color.green.brighter());
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
		setBounds(100, 100, 720, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.5;
		constraints.gridx = 0;
		constraints.gridy = 0;

		constraints.fill = GridBagConstraints.HORIZONTAL;
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				JTField field = new JTField(row, col);
				TextFieldEventListener listener = new TextFieldEventListener();
				field.addActionListener(listener);
				field.addFocusListener(listener);
				field.setSize(200, 200);
				field.setAlignmentY(CENTER_ALIGNMENT);
				field.setHorizontalAlignment(JTextField.CENTER);
				field.setFont(field.getFont().deriveFont(java.awt.Font.PLAIN, 25));
				board[row][col] = field;

				constraints.gridy = row;
				constraints.gridx = col;
				contentPane.add(field, constraints);

			}
		}
		constraints.gridy = constraints.gridy + 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.PAGE_END;
		constraints.gridwidth = 4;
		constraints.gridx = 5;
		constraints.ipady = 40;
		JButton submit = new JButton();
		submit.setText("Solve");
		submit.addActionListener(new SolveButtonEventListener());
		contentPane.add(submit, constraints);

		constraints.gridx = 0;
		JButton randomButton = new JButton();
		randomButton.setText("Random Board");
		contentPane.add(randomButton, constraints);

		setContentPane(contentPane);
		setDefaultColors();
	}

	public void setDefaultColors() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
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

	public void setBoard(int[][] rawboard) {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				try {
					board[row][col].setText(Integer.toString(rawboard[row][col]));
				} catch (Exception e) {
					// invalid inputs TODO: error msg in gui?
					break;
				}
			}
		}
	}
}
