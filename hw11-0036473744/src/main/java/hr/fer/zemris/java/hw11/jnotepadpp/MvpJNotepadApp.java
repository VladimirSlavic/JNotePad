package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * Interface representing the MVP/MVC model as specified by google. To increase
 * readability and usability, it separates business logic to basic changes in
 * the GUI.
 * 
 * @author vladimir
 *
 */
public interface MvpJNotepadApp {

	/**
	 * Interface representing the view of the JNotepade. Holds all methods
	 * relating to changes in the state of the notepad and the GUI itself.
	 * 
	 * @author vladimir
	 *
	 */
	interface View {

		/**
		 * Opens a JOptionMenu in the GUI for normal messages.
		 * 
		 * @param message
		 *            the message to be shown
		 * @param messageType
		 *            message type
		 */
		void showMessage(String message, String messageType);

		/**
		 * Opens a JOptionMenu in the GUI for error message.
		 * 
		 * @param message
		 *            the message to be shown
		 * @param messageType
		 *            message type
		 */
		void showErrorMessage(String message, String messageType);

		/**
		 * Method for updating the current tab in the GUI.
		 * 
		 * @param tabFile
		 *            current tab
		 */
		void updateTab(TabFile tabFile);

		/**
		 * Method for adding a tab to the JNotepad.
		 * 
		 * @param tab
		 *            current tab
		 * @param path
		 *            path of the current tab.
		 */
		void addTab(TabFile tab, Path path);

		/**
		 * Method for removing a tab at specified index.
		 * 
		 * @param index
		 *            index of tab
		 */
		void removeTab(int index);

		/**
		 * Method for setting a tab to be the current tab from all existing tabs
		 * based on the index.
		 * 
		 * @param index
		 *            index of tab.
		 */
		void setTab(int index);

		/**
		 * Ends the execution of the program and calls dispose on the Frame
		 * itself.
		 */
		void destroyProgram();

	}

	/**
	 * Business logic controller.
	 * 
	 * @author vladimir
	 *
	 */
	interface Presenter {

		/**
		 * Method for saving the specified tab
		 * 
		 * @param file
		 *            tab to be saved
		 */
		void saveFile(TabFile file);

		/**
		 * Save as function for saving the file to a different place in the
		 * computer and specifying its file name.
		 * 
		 * @param file
		 *            tab to be saved
		 */
		void saveAsFile(TabFile file);

		/**
		 * Method for returing image icons of the tab
		 * 
		 * @param file
		 *            current tab
		 * @return returns the Icon for the tab
		 */
		ImageIcon getImageIcon(TabFile file);

		/**
		 * Cut command based on the opened active tab.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void cut(TabFile file);

		/**
		 * Copy command based on the opened active tab.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void copy(TabFile file);

		/**
		 * Paste command based on the opened active tab.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void paste(TabFile file);

		/**
		 * Statistics command based on the opened active tab. Opens a message
		 * representing all the information of the current tab. NUmber of chars,
		 * non-blank chars and number of lines.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void stats(TabFile file);

		/**
		 * toUppercase command based on the opened active tab. All selected text
		 * in the tab is converted to uppercase.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void toUppercase(TabFile file);

		/**
		 * toLowerCase command based on the opened active tab.All selected text
		 * in the tab is converted to lowercase.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void toLowerCase(TabFile file);

		/**
		 * invertText command based on the opened active tab. Inverts the case
		 * of the letters based on selected text.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void invertText(TabFile file);

		/**
		 * sortDesc command based on the opened active tab. Sorts selected lines
		 * in descending order.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void sortDesc(TabFile file);

		/**
		 * sortAsc command based on the opened active tab. Sorts selected lines
		 * in ascending order.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void sortAsc(TabFile file);

		/**
		 * Unique command based on the opened active tab. Based on highlighted
		 * line, removes all lines that are copies except for its first
		 * occurrence.
		 * 
		 * @param file
		 *            current tab on which the command is being executed.
		 */
		void unique(TabFile file);

		/**
		 * Method for opening a new tab in the JNotePad
		 * 
		 * @param file
		 *            tab to be opened.
		 * @param openedFiles
		 *            list of all currently opened tabs.
		 */
		void openNewTab(TabFile file, List<TabFile> openedFiles);

		/**
		 * Close command for closing the specified tab.
		 * 
		 * @param currentFile
		 *            tab to be closed
		 * @param openedFiles
		 *            list of all tabs.
		 */
		void close(TabFile currentFile, List<TabFile> openedFiles);

		/**
		 * Exit command. Exits the application and closes all tabs. First offers
		 * to save all 'unsaved' files.
		 * 
		 * @param openedFiles all currently opened tabs.
		 */
		void exit(List<TabFile> openedFiles);

		/**
		 * open's a new document in the JNotePad with no specified path.
		 * @param openedFiles list of all currently opened tabs
		 * @param tabCounter current tab count.
		 */
		void openDocument(List<TabFile> openedFiles, int tabCounter);
	}

}
