package hr.fer.zemris.java.hw11.jnotepadpp.components;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import hr.fer.zemris.java.hw11.jnotepadpp.TabFile;
import hr.fer.zemris.java.hw11.jnotepadpp.utility.Util;

/**
 * Custom toolbar for bottom of the JNotePad.
 * 
 * @author vladimir
 *
 */
@SuppressWarnings("serial")
public class StatusBar extends JToolBar {

	/**
	 * Current time and date
	 */
	private JLabel timeAndDate;
	/**
	 * Current line
	 */
	private JLabel currentLine;
	/**
	 * Current column
	 */
	private JLabel currentColumn;
	/**
	 * Current selected text
	 */
	private JLabel currentSelected;
	/**
	 * Current length of text
	 */
	private JLabel currentLength;
	/**
	 * has a stop been requested in order to finalize the program.
	 */
	private boolean stopRequested = false;
	/**
	 * Current time
	 */
	private LocalDateTime currTime;

	/**
	 * Constants defined previously defined variables. All are self explanatory.
	 */
	private static final String COL = "Col: ";
	private static final String LINE = "Ln: ";
	private static final String SELECTED = "Sel: ";
	private static final String LENGTH = "Len: ";

	/**
	 * Public constructor
	 */
	public StatusBar() {

		timeAndDate = new JLabel();
		currentLine = new JLabel(LINE + "0");
		currentColumn = new JLabel(COL + "0");
		currentSelected = new JLabel(SELECTED + "0");
		currentLength = new JLabel(LENGTH + "0");

		add(currentLength);

		add(Box.createHorizontalGlue());

		add(currentLine);
		addSeparator();
		add(currentColumn);
		addSeparator();
		add(currentSelected);
		addSeparator();
		add(timeAndDate);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		Thread radnik = new Thread(() -> {

			while (!stopRequested) {
				SwingUtilities.invokeLater(() -> {
					currTime = LocalDateTime.now();
					timeAndDate.setText(dtf.format(currTime));
					repaint();
				});

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});

		radnik.setDaemon(true);
		radnik.start();
	}

	/**
	 * Destroy the program.
	 */
	public void closingProgram() {
		stopRequested = true;
	}

	/**
	 * method to update the status bar based on the operations the user is doing
	 * in the opened tab. E.g. backspacing and removing text or typing new
	 * characters in the document.
	 * 
	 * @param file
	 *            current opened tab
	 * @param isRemove
	 *            is it a remove operation or adding operation.
	 */
	public void updateStatusBar(TabFile file, boolean isRemove) {

		JTextArea ta = file.getTextArea();

		try {
			int caretOffset = ta.getCaretPosition();
			caretOffset = isRemove ? (caretOffset - 1) : caretOffset;
			int lineNumber = ta.getLineOfOffset(caretOffset);
			currentLine.setText(LINE + String.valueOf(lineNumber + 1));

			int startOffset = ta.getLineStartOffset(lineNumber);
			int col = caretOffset - startOffset;
			currentColumn.setText(COL + String.valueOf(col + 1));

			int len = Math.abs(ta.getCaret().getDot() - ta.getCaret().getMark());
			currentSelected.setText(SELECTED + String.valueOf(len));

			currentLength.setText(LENGTH + String.valueOf(Util.getNumberOfCharacters(ta.getText()) + 1));
			repaint();

		} catch (BadLocationException e) {
		}
	}
}
