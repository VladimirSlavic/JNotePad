package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.components.LocalizedJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.components.StatusBar;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * JNotePadPP represents a class for a view of a notepad type application.
 * Implementation offers simple functionality such as sorting selected text,
 * information on document such as number of selected lines, count of chars in a
 * document, non-blank chars. Paste, copy, cut, open, new file command. Saving
 * and save as commands also. Also offers multiple language features; currently
 * supported only Croatian, German and English.
 * 
 * @author vladimir
 *
 */
@SuppressWarnings("serial")
public class JNotepadPP extends JFrame implements MvpJNotepadApp.View, CaretListener {
	/**
	 * List of all current opened tabs.
	 */
	private List<TabFile> openedFiles;
	/**
	 * current tab
	 */
	private TabFile currentFile;
	/**
	 * tabs
	 */
	private JTabbedPane tabs;
	/**
	 * to uppen menu
	 */
	private JMenuItem toUpperJMenu;
	/**
	 * to lower menu
	 */
	private JMenuItem toLowerJMenu;
	/**
	 * to invert menu
	 */
	private JMenuItem invertJMenu;
	/**
	 * presenter
	 */
	private MvpJNotepadApp.Presenter presenter;
	/**
	 * Counter of only new tabs..
	 */
	private static int tabCounter = 0;
	/**
	 * status bar at bottom of screen.
	 */
	private StatusBar statusbar;
	/**
	 * Provider of the current localization for the application.
	 */
	private final FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

	/***
	 * Public constructor.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(50, 50);
		setSize(900, 900);
		
		presenter = new JNotepadAppPresenter(this);
		openedFiles = new ArrayList<>();
		
		initGui();
		createActions();
		createMenus();
		createToolbars();
	}

	/**
	 * Method for initializing the GUI of the application.
	 */
	private void initGui() {
		tabs = new JTabbedPane();

		statusbar = new StatusBar();
		getContentPane().add(statusbar, BorderLayout.PAGE_END);

		add(tabs);
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}

		});

		tabs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabs.getSelectedIndex();
				if (selectedIndex == -1) {
					currentFile = null;
					return;
				}

				currentFile = openedFiles.get(selectedIndex);
				setTitle(currentFile.getFilePath() == null ? flp.getString(NEW) : currentFile.getFilePath().toString());
				statusbar.updateStatusBar(currentFile, false);
			}
		});
	}

	/**
	 * Method for creating all current actions in the application.
	 */
	private void createActions() {
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

		saveFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

		saveAsFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		saveAsFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

		newFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newFileAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);

		statAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control B"));
		statAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);

		closeAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		closeAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);

		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
	}

	/**
	 * Method for creating all the menus in the application.
	 */
	private void createMenus() {
		JMenuBar menubar = new JMenuBar();

		JMenu fileMenu = new LocalizedJMenu(FILE, flp);

		menubar.add(fileMenu);
		fileMenu.add(new JMenuItem(newFileAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveFileAction));
		fileMenu.add(new JMenuItem(saveAsFileAction));
		fileMenu.add(new JMenuItem(closeAction));

		fileMenu.addSeparator();

		fileMenu.add(exitAction);

		JMenu editMenu = new LocalizedJMenu(EDIT, flp);
		menubar.add(editMenu);
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(cutAction));
		editMenu.add(new JMenuItem(pasteAction));
		editMenu.add(new JMenuItem(statAction));

		JMenu languagesMenu = new LocalizedJMenu(LANGUAGE, flp);

		menubar.add(languagesMenu);
		languagesMenu.add(new JMenuItem(croLanguageAction));
		languagesMenu.add(new JMenuItem(enLanguageAction));
		languagesMenu.add(new JMenuItem(deLanguageAction));

		JMenu toolsMenu = new LocalizedJMenu(TOOLS, flp);
		JMenu changeCaseMenu = new LocalizedJMenu(CHANGE_CASE, flp);
		JMenu sortMenu = new LocalizedJMenu(SORT, flp);
		toolsMenu.add(changeCaseMenu);
		toolsMenu.add(sortMenu);
		toolsMenu.add(uniqueAction);

		toUpperJMenu = new JMenuItem(toUpperAction);
		toLowerJMenu = new JMenuItem(toLowerAction);
		invertJMenu = new JMenuItem(invertTextAction);
		toUpperJMenu.setEnabled(false);
		toLowerJMenu.setEnabled(false);
		invertJMenu.setEnabled(false);

		changeCaseMenu.add(toUpperJMenu);
		changeCaseMenu.add(toLowerJMenu);
		changeCaseMenu.add(invertJMenu);

		sortMenu.add(new JMenuItem(sortDescAction));
		sortMenu.add(sortAscAction);

		menubar.add(toolsMenu);

		setJMenuBar(menubar);
	}

	/**
	 * Method for creating the toolbar.
	 */
	private void createToolbars() {

		JToolBar toolbar = new JToolBar(flp.getString(TOOLBAR));

		toolbar.add(new JButton(newFileAction));
		toolbar.add(new JButton(openDocumentAction));
		toolbar.add(new JButton(saveFileAction));
		toolbar.add(new JButton(saveAsFileAction));

		toolbar.addSeparator();

		toolbar.add(new JButton(copyAction));
		toolbar.add(new JButton(cutAction));
		toolbar.add(new JButton(pasteAction));

		toolbar.addSeparator();
		
		toolbar.add(new JButton(statAction));
		
		toolbar.addSeparator();

		toolbar.add(new JButton(closeAction));
		toolbar.add(new JButton(exitAction));
		getContentPane().add(toolbar, BorderLayout.PAGE_START);

	}

	/**
	 * Action for opening document.
	 */
	private Action openDocumentAction = new LocalizableAction(OPEN, OPEN_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.openDocument(openedFiles, tabCounter);
		}
	};

	/**
	 * Action for opening a new file.
	 */
	private Action newFileAction = new LocalizableAction(NEW, NEW_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			TabFile newFile = new TabFile(tabCounter);
			newFile.setChanged(true);
			openedFiles.add(newFile);
			presenter.openNewTab(newFile, openedFiles);
		}
	};

	/**
	 * Action for saving document.
	 */
	private Action saveFileAction = new LocalizableAction(SAVE, SAVE_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (currentFile != null && currentFile.getFilePath() != null) {
				presenter.saveFile(currentFile);
				return;
			}

			if (currentFile != null && currentFile.getFilePath() == null) {
				saveAsFileAction.actionPerformed(null);
			}
		}
	};

	/**
	 * Action for saving document to specified location with specified name.
	 */
	private Action saveAsFileAction = new LocalizableAction(SAVE_AS, SAVE_AS_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {

			if(currentFile == null) return;
			
			JFileChooser fc = new JFileChooser();
			if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				showMessage(flp.getString(NOTHING_SELECTED), flp.getString(ERROR));
				return;
			}

			currentFile.setFilePath(fc.getSelectedFile().toPath());
			presenter.saveAsFile(currentFile);
		}
	};

	/**
	 * Action for closing tab.
	 */
	private Action closeAction = new LocalizableAction(CLOSE, CLOSE_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.close(currentFile, openedFiles);
		}
	};

	/**
	 * Action for exiting application tab.
	 */
	private Action exitAction = new LocalizableAction(EXIT, EXIT_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.exit(openedFiles);
		}
	};

	@Override
	public void removeTab(int index) {
		tabs.removeTabAt(index);
	}

	@Override
	public void setTab(int index) {
		tabs.setSelectedIndex(index);
	}
	
	@Override
	public void destroyProgram() {
		statusbar.closingProgram();
		dispose();
	}

	@Override
	public void addTab(TabFile tab, Path path) {
		tabs.addTab(path != null ? path.toString() : flp.getString(NEW), new JScrollPane(tab.getTextArea()));
		tabs.setSelectedIndex(tabs.getTabCount() - 1);
		tabs.setToolTipText(path != null ? path.toString() : "");
		tabs.setIconAt(tabs.getTabCount() - 1, presenter.getImageIcon(tab));//tab.getImageIcon()
		currentFile.getTextArea().getDocument().addDocumentListener(documentListener);
		currentFile.getTextArea().addCaretListener(this);
		tabCounter++;
	}

	@Override
	public void showMessage(String message, String messageType) {
		JOptionPane.showMessageDialog(JNotepadPP.this, message, messageType, JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void showErrorMessage(String message, String messageType) {
		JOptionPane.showMessageDialog(JNotepadPP.this, message, messageType, JOptionPane.ERROR_MESSAGE);

	}

	@Override
	public void updateTab(TabFile tabFile) {
		
		if (openedFiles.contains(tabFile)) {
			int index = openedFiles.indexOf(tabFile);
			tabs.setIconAt(index, presenter.getImageIcon(tabFile));//presenter.getImageIcon(tab tabFile.getImageIcon()
			tabs.setTitleAt(index, tabFile.getFilePath() == null ? flp.getString(NEW) : tabFile.getFilePath().toString());
			repaint();
		}
	}

	/**
	 * Action for cutting selected text in document.
	 */
	private Action cutAction = new LocalizableAction(CUT, CUT_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.cut(currentFile);
			repaint();
		}
	};

	/**
	 * Action for copying selected text in document.
	 */
	private Action copyAction = new LocalizableAction(COPY, COPY_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.copy(currentFile);
		}
	};

	/**
	 * Action for pasting selected text in document.
	 */
	private Action pasteAction = new LocalizableAction(PASTE, PASTE_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.paste(currentFile);
		}
	};

	/**
	 * Action for getting statistics on the current tab.
	 */
	private Action statAction = new LocalizableAction(STATS, STATS_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.stats(currentFile);
		}
	};

	@Override
	public void caretUpdate(CaretEvent e) {
		statusbar.updateStatusBar(currentFile, false);
		boolean enabled = Math.abs(e.getDot() - e.getMark()) == 0 ? false : true;
		toUpperJMenu.setEnabled(enabled);
		toLowerJMenu.setEnabled(enabled);
		invertJMenu.setEnabled(enabled);
	}

	/**
	 * Action for setting the Croatian language.
	 */
	private Action croLanguageAction = new LocalizableAction(LANGUAGE_HR, LANGUAGE_HR, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage(HR_EXTENSION);
		}
	};

	private Action deLanguageAction = new LocalizableAction(LANGUAGE_DE, LANGUAGE_DE, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage(DE_EXTENSION);
		}
	};
	
	/**
	 * Action for setting the English language.
	 */
	private Action enLanguageAction = new LocalizableAction(LANGUAGE_EN, LANGUAGE_EN, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage(EN_EXTENSION);
		}
	};

	/**
	 * Action for converting selected text to upper case.
	 */
	private Action toUpperAction = new LocalizableAction(UPPER_CASE_ACTION, UPPER_CASE_ACTION_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.toUppercase(currentFile);

		}
	};

	/**
	 * Action for converting selected text to lower case.
	 */
	private Action toLowerAction = new LocalizableAction(LOWER_CASE_ACTION, LOWER_CASE_ACTION_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.toLowerCase(currentFile);

		}
	};

	/**
	 * Action for converting selected text to inverted case.
	 */
	private Action invertTextAction = new LocalizableAction(INVERT_CASE_ACTION, INVERT_CASE_ACTION_DESC, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.invertText(currentFile);

		}
	};

	/**
	 * Action for sorting selected text in ascending order.
	 */
	private Action sortAscAction = new LocalizableAction(ASC_SORT, ASC_SORT, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.sortAsc(currentFile);

		}
	};

	/**
	 * Action for sorting selected text in descending order.
	 */
	private Action sortDescAction = new LocalizableAction(DESC_SORT, DESC_SORT, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.sortDesc(currentFile);

		}
	};

	/**
	 * Action for removing same lines based on selected text except for the first occurent of the line.
	 */
	private Action uniqueAction = new LocalizableAction(UNIQUE, UNIQUE, flp) {

		@Override
		public void actionPerformed(ActionEvent e) {
			presenter.unique(currentFile);

		}
	};
	
	/**
	 * Document listener.
	 */
	private DocumentListener documentListener = new DocumentListener() {

		@Override
		public void removeUpdate(DocumentEvent e) {
			currentFile.setChanged(true);
			updateTab(currentFile);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			currentFile.setChanged(true);
			updateTab(currentFile);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {

		}
	};

	/**
	 * Main method
	 * @param args command line arguments.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}

	/**
	 * All constants related to internationalization.
	 */
	private static final String OPEN = "open";
	private static final String SAVE = "save";
	private static final String SAVE_AS = "save_as";
	private static final String CLOSE = "close";
	private static final String EXIT = "exit";
	private static final String NEW = "new";
	private static final String COPY = "copy";
	private static final String CUT = "cut";
	private static final String PASTE = "paste";
	public static final String STATS = "stats";
	private static final String EDIT = "edit";
	private static final String LANGUAGE_HR = "language_hr";
	private static final String LANGUAGE_EN = "language_en";
	private static final String NEW_DESC = "new_description";
	private static final String OPEN_DESC = "open_description";
	private static final String SAVE_DESC = "save_description";
	private static final String SAVE_AS_DESC = "save_as_description";
	private static final String COPY_DESC = "copy_description";
	private static final String CUT_DESC = "cut_description";
	private static final String PASTE_DESC = "paste_description";
	private static final String STATS_DESC = "stats_description";
	private static final String EXIT_DESC = "exit_description";
	private static final String CLOSE_DESC = "close_description";
	private static final String HR_EXTENSION = "hr";
	private static final String EN_EXTENSION = "en";
	private static final String FILE = "file";
	private static final String LANGUAGE = "language";
	private static final String TOOLS = "tools";
	private static final String CHANGE_CASE = "change_case";
	private static final String UPPER_CASE_ACTION = "upper_case_action";
	private static final String UPPER_CASE_ACTION_DESC = "upper_case_action_desc";
	private static final String LOWER_CASE_ACTION = "lower_case_action";
	private static final String LOWER_CASE_ACTION_DESC = "lower_case_action_desc";
	private static final String INVERT_CASE_ACTION = "invert_case_action";
	private static final String INVERT_CASE_ACTION_DESC = "invert_case_action_desc";
	private static final String SORT = "sort";
	private static final String ASC_SORT = "ascending";
	private static final String DESC_SORT = "descending";
	private static final String UNIQUE = "unique";
	private static final String LANGUAGE_DE = "language_de";
	private static final String DE_EXTENSION = "de";
	private static final String NOTHING_SELECTED = "nothing_selected";
	private static final String ERROR = "error";
	private static final String TOOLBAR = "Toolbar";
}
