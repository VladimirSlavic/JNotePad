package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import hr.fer.zemris.java.hw11.jnotepadpp.MvpJNotepadApp.View;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.utility.Util;

/**
 * JNotepad presenter representing all business logic for the JNotepade. Handles
 * all operations from starting the application, opening and closing tabs to
 * closing the entire application.
 * 
 * @author vladimir
 *
 */
public class JNotepadAppPresenter implements MvpJNotepadApp.Presenter {

	/**
	 * Reference to the current view.
	 */
	private MvpJNotepadApp.View view;

	/**
	 * represents if the is copied of cut text, it is stored here in order to be
	 * pasted elsewhere.
	 */
	private String currentCutOrCopiedText;

	/**
	 * Public constructor
	 * 
	 * @param view
	 *            the view
	 */
	public JNotepadAppPresenter(View view) {
		super();
		this.view = view;
	}

	@Override
	public void saveFile(TabFile file) {

		if (!file.getIsChanged())
			return;

		try {
			Document doc = file.getTextArea().getDocument();
			Files.write(file.getFilePath(), file.getTextArea().getText().getBytes(StandardCharsets.UTF_8));
			file.setChanged(false);
			view.updateTab(file);
			view.showMessage(file.getFilePath() + " " + LocalizationProvider.getInstance().getString(SUCCESSFUL_SAVE),
					LocalizationProvider.getInstance().getString(SUCCESS));
		} catch (Exception ex) {
			view.showErrorMessage(LocalizationProvider.getInstance().getString(SAVE_ERROR) + file.getFilePath() + ".",
					LocalizationProvider.getInstance().getString(ERROR));
		}
	}

	@Override
	public void saveAsFile(TabFile file) {
		try {
			Files.write(file.getFilePath(), file.getTextArea().getText().getBytes(StandardCharsets.UTF_8));
			file.setChanged(false);
			view.updateTab(file);
			view.showMessage(file.getFilePath() + " " + LocalizationProvider.getInstance().getString(SUCCESSFUL_SAVE),
					LocalizationProvider.getInstance().getString(SUCCESS));
		} catch (Exception e) {
			view.showErrorMessage(LocalizationProvider.getInstance().getString(SAVE_ERROR) + file.getFilePath(),
					LocalizationProvider.getInstance().getString(ERROR));
		}
	}

	@Override
	public ImageIcon getImageIcon(TabFile file) {
		String path = "icons/";

		if (file.getIsChanged()) {
			path += "modified.png";
		} else {
			path += "saved.png";
		}

		return new ImageIcon(this.getClass().getResource(path));

	}

	@Override
	public void cut(TabFile file) {
		cutOrCopy(true, file);
	}

	@Override
	public void copy(TabFile file) {
		cutOrCopy(false, file);
	}

	private void cutOrCopy(boolean isCut, TabFile file) {

		if (file == null)
			return;

		JTextArea editor = file.getTextArea();
		Document doc = editor.getDocument();

		int offset = 0;
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

		if (len == 0)
			return;

		offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());

		try {

			currentCutOrCopiedText = doc.getText(offset, len);

			if (isCut) {
				doc.remove(offset, len);
			}
		} catch (BadLocationException ignoreable) {
		}
	}

	@Override
	public void paste(TabFile file) {

		if (file == null)
			return;

		if (currentCutOrCopiedText == null || currentCutOrCopiedText.isEmpty()) {
			return;
		}

		try {

			JTextArea editor = file.getTextArea();
			Document doc = editor.getDocument();

			int offset = 0;
			int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

			offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());

			if (len == 0) {
				doc.insertString(offset, currentCutOrCopiedText, null);
				return;
			}

			doc.remove(offset, len);
			doc.insertString(offset, currentCutOrCopiedText, null);

		} catch (BadLocationException ignorable) {
		}
	}

	@Override
	public void stats(TabFile file) {

		if (file == null)
			return;

		String content = file.getTextArea().getText();

		long numberOfLines = content.split("\n").length;
		long numberOfLetter = content.length();
		long numberOfNonBlanks = Util.getNumberOfNonBlankCharacters(content);

		LocalizationProvider lp = LocalizationProvider.getInstance();

		String stats = String.format(lp.getString(NUMBER_OF_LINES) + ": %d\n" + lp.getString(NUMBER_OF_CHARS) + ": %d\n"
				+ lp.getString(NUMBER_OF_NONBLANKS) + ": %d", numberOfLines, numberOfLetter, numberOfNonBlanks);

		view.showMessage(stats, lp.getString(JNotepadPP.STATS));

	}

	@Override
	public void toUppercase(TabFile file) {
		convertString(file, String::toUpperCase);
	}

	@Override
	public void toLowerCase(TabFile file) {
		convertString(file, String::toLowerCase);
	}

	@Override
	public void invertText(TabFile file) {
		convertString(file, e -> {
			return Util.invertText(e);
		});
	}

	/**
	 * Generic method for converting the string as specified by the user.
	 * 
	 * @param file
	 *            current tab being modified
	 * @param transformer
	 *            transformer function determining 'how' the string will be
	 *            transformed: invert, lowercase, uppercase etc.
	 */
	private void convertString(TabFile file, TextTrasnformer transformer) {

		if (file == null)
			return;

		JTextArea editor = file.getTextArea();
		Document doc = editor.getDocument();

		int offset = 0;
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());

		if (len == 0)
			return;

		offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());

		try {

			String text = transformer.transform(doc.getText(offset, len));
			doc.remove(offset, len);
			doc.insertString(offset, text, null);

		} catch (BadLocationException ignoreable) {
		}
	}

	@Override
	public void sortDesc(TabFile file) {
		Locale locale = new Locale(LocalizationProvider.getInstance().getLanguage());
		Collator colator = Collator.getInstance(locale);

		Comparator<String> lineComparator = (o1, o2) -> {

			return colator.compare(o1, o2);
		};
		sort(file, lineComparator);
	}

	/**
	 * Generic method for sorting the selected lines in the tab. Can sort as
	 * specified by the comparator.
	 * 
	 * @param file
	 *            current tab under evalutation.
	 * @param comparator
	 *            comparator.
	 */
	private void sort(TabFile file, Comparator<String> comparator) {
		if (file == null)
			return;

		try {

			int currentLength = Math
					.abs(file.getTextArea().getCaret().getDot() - file.getTextArea().getCaret().getMark());
			int offset = Math.min(file.getTextArea().getCaret().getDot(), file.getTextArea().getCaret().getMark());

			SelectedLines info = getSelectedLines(file, currentLength, offset);
			Document doc = file.getTextArea().getDocument();

			if (info == null || info.selectedLines.isEmpty())
				return;

			if (info.wasLastLineSelected) {
				info.selectedLines.set(info.selectedLines.size() - 1,
						info.selectedLines.get(info.selectedLines.size() - 1) + "\n");
			}

			info.selectedLines.sort(comparator);

			info.selectedLines.set(info.selectedLines.size() - 1,
					info.selectedLines.get(info.selectedLines.size() - 1).replaceAll("\n", ""));

			int n = info.selectedLines.size();
			int i = 0;
			doc.remove(offset, currentLength);

			while (i < n) {
				int len = info.selectedLines.get(i).length();
				doc.insertString(offset, info.selectedLines.get(i), null);
				i++;
				offset += len;
			}

		} catch (BadLocationException ignorable) {
		}
	}

	@Override
	public void sortAsc(TabFile file) {
		Locale locale = new Locale(LocalizationProvider.getInstance().getLanguage());
		Collator colator = Collator.getInstance(locale);

		Comparator<String> lineComparator = (o1, o2) -> {

			return colator.compare(o1, o2);
		};
		sort(file, lineComparator.reversed());
	}

	interface TextTrasnformer {
		String transform(String text);
	}

	@Override
	public void unique(TabFile file) {

		if (file == null)
			return;

		try {

			JTextArea area = file.getTextArea();
			int currentLength = Math
					.abs(file.getTextArea().getCaret().getDot() - file.getTextArea().getCaret().getMark());
			int offset = Math.min(file.getTextArea().getCaret().getDot(), file.getTextArea().getCaret().getMark());

			SelectedLines info = getSelectedLines(file, currentLength, offset);
			Document doc = file.getTextArea().getDocument();

			Map<String, Integer> linesToBeRemoved = new LinkedHashMap<>();
			info.selectedLines.forEach(e -> {
				linesToBeRemoved.put(e, 0);
			});

			if (linesToBeRemoved.isEmpty())
				return;

			int startOffset = 0;
			int lenOfDoc = doc.getLength();
			currentLength = 0;

			while (true) {
				int line = area.getLineOfOffset(startOffset);
				int lineStart = area.getLineStartOffset(line);
				int lineEnd = area.getLineEndOffset(line);
				currentLength += lineEnd - lineStart;

				String ln = doc.getText(lineStart, lineEnd - lineStart);
				if (linesToBeRemoved.get(ln) != null && linesToBeRemoved.get(ln) > 0) {
					doc.remove(startOffset, lineEnd - lineStart);
					continue;
				}

				linesToBeRemoved.put(ln, 1);

				if (currentLength >= lenOfDoc) {
					break;
				}

				startOffset += lineEnd - lineStart;
			}

		} catch (BadLocationException ignorable) {
		}

	}

	/**
	 * Method for getting all the selected lines within a file.
	 * 
	 * @param file
	 *            current file
	 * @param length
	 *            current length of the entire selection.
	 * @param offset
	 *            offset in the selection.
	 * @return All selected lines including if the last line is selected.
	 */
	private SelectedLines getSelectedLines(TabFile file, int length, int offset) {
		if (file == null)
			return null;

		JTextArea area = file.getTextArea();

		int lastLineIndex = (int) (Util.getNumberOfLines(area.getText()) - 1);
		boolean isLastLineSelected = false;

		try {
			int currentLength = 0;
			Document doc = area.getDocument();
			List<String> linesText = new ArrayList<>();
			int startOffset = offset;

			while (true) {
				int line = area.getLineOfOffset(startOffset);
				int lineStart = area.getLineStartOffset(line);
				int lineEnd = area.getLineEndOffset(line);
				currentLength += lineEnd - lineStart;
				linesText.add(doc.getText(lineStart, lineEnd - lineStart));

				if (lastLineIndex == line) {
					isLastLineSelected = true;
				}

				if (currentLength >= length) {
					break;
				}
				startOffset += lineEnd - lineStart;
			}

			return new SelectedLines(linesText, isLastLineSelected);

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Structure for selected lines and if the last line is included in such a
	 * selection.
	 * 
	 * @author vladimir
	 *
	 */
	private static class SelectedLines {
		/**
		 * List of selected lines.
		 */
		private List<String> selectedLines;
		/**
		 * flag if last line was selected.
		 */
		private Boolean wasLastLineSelected;

		/**
		 * Public constructor
		 * 
		 * @param selectedLines
		 * @param wasLastLineSelected
		 */
		public SelectedLines(List<String> selectedLines, Boolean wasLastLineSelected) {
			super();
			this.selectedLines = selectedLines;
			this.wasLastLineSelected = wasLastLineSelected;
		}

	}

	@Override
	public void openNewTab(TabFile tab, List<TabFile> openedFiles) {
		byte[] data = null;
		String text = "";

		try {

			if (tab.getFilePath() != null) {
				data = Files.readAllBytes(tab.getFilePath());
				text = new String(data, StandardCharsets.UTF_8);
			}

		} catch (Exception exc) {
			view.showErrorMessage(
					LocalizationProvider.getInstance().getString(READING_ERROR) + " " + tab.getFilePath() + ".",
					LocalizationProvider.getInstance().getString(ERROR));
			openedFiles.remove(openedFiles.size() - 1);
			return;
		}

		tab.getTextArea().setText(text);
		Path tabPath = tab.getFilePath();
		view.addTab(tab, tabPath);
	}

	@Override
	public void close(TabFile currentFile, List<TabFile> openedFiles) {
		if (currentFile == null)
			return;

		if (currentFile.getIsChanged()) {
			int result = JOptionPane.showConfirmDialog((JNotepadPP) view,
					LocalizationProvider.getInstance().getString(SAVE_THIS_FILE),
					LocalizationProvider.getInstance().getString(SAVE), JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				saveFile(currentFile);
			}
		}

		int currentTabSize = openedFiles.size();
		if (currentTabSize == 1) {
			view.removeTab(0);
			openedFiles.remove(0);
			return;
		}

		int indexOfCurrentActive = openedFiles.indexOf(currentFile);
		int nextTabIndex = (indexOfCurrentActive + 1) % openedFiles.size();
		view.setTab(nextTabIndex);
		openedFiles.remove(indexOfCurrentActive);
		view.removeTab(indexOfCurrentActive);
	}

	@Override
	public void exit(List<TabFile> openedFiles) {
		for (int i = 0, n = openedFiles.size(); i < n; i++) {
			TabFile t = openedFiles.get(i);
			view.setTab(i);

			if (!t.getIsChanged()) {
				continue;
			}

			String file = t.getFilePath() == null ? LocalizationProvider.getInstance().getString(NEW_FILE)
					: t.getFilePath().toString();
			int result = JOptionPane.showConfirmDialog((JNotepadPP) view,
					LocalizationProvider.getInstance().getString(DO_YOU_WISH_SAVE) + file,
					LocalizationProvider.getInstance().getString(SAVE), JOptionPane.INFORMATION_MESSAGE);

			if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}

			if (result == JOptionPane.NO_OPTION) {
				continue;
			}

			if (t.getFilePath() == null) {
				JFileChooser fc = new JFileChooser();
				if (fc.showSaveDialog((JNotepadPP) view) != JFileChooser.APPROVE_OPTION) {
					view.showMessage(LocalizationProvider.getInstance().getString(NOTHING_SELECTED),
							LocalizationProvider.getInstance().getString(ERROR));
					continue;
				}

				t.setFilePath(fc.getSelectedFile().toPath());
				saveAsFile(t);
				continue;
			}

			saveFile(t);
		}
		view.destroyProgram();
	}

	@Override
	public void openDocument(List<TabFile> openedFiles, int tabCounter) {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(LocalizationProvider.getInstance().getString(OPEN_FILE));

		if (fc.showOpenDialog((JNotepadPP) view) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File fileName = fc.getSelectedFile();
		Path filePath = fileName.toPath();

		if (!Files.isReadable(filePath)) {
			view.showMessage(filePath + LocalizationProvider.getInstance().getString(NOT_READABLE),
					LocalizationProvider.getInstance().getString(ERROR));
			return;
		}

		TabFile fileForTab = new TabFile(filePath, tabCounter);

		if (!openedFiles.contains(fileForTab)) {
			openedFiles.add(fileForTab);
			openNewTab(fileForTab, openedFiles);
			return;
		}

		view.setTab(openedFiles.indexOf(fileForTab));
	}

	/**
	 * Constrants for different language settings.
	 */
	private static final String SAVE_THIS_FILE = "save_this_file";
	private static final String SAVE_ERROR = "save_error";
	private static final String ERROR = "error";
	private static final String SUCCESS = "success";
	private static final String SUCCESSFUL_SAVE = "successful_save";
	private static final String DO_YOU_WISH_SAVE = "do_you_want_save";
	private static final String SAVE = "save";
	private static final String NEW_FILE = "new_file";
	private static final String NOT_READABLE = "not_readable";
	private static final String NOTHING_SELECTED = "nothing_selected";
	private static final String READING_ERROR = "read_error";
	private static final String OPEN_FILE = "open_file";
	private static final String NUMBER_OF_LINES = "number_of_lines";
	private static final String NUMBER_OF_CHARS = "number_of_chars";
	private static final String NUMBER_OF_NONBLANKS = "number_of_non_blanks";
}
