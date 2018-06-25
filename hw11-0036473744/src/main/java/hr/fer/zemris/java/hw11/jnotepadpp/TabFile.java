package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import javax.swing.JTextArea;

/**
 * Class representing a tab in the JTabbedPane.
 * @author vladimir
 *
 */
public class TabFile {

	/**
	 * Current content of the tab
	 */
	private String content;
	/**
	 * Path of the doucment 
	 */
	private Path filePath;
	/**
	 * Text area of the tab
	 */
	private JTextArea textArea;
	/**
	 * flag if the tab has been changed
	 */
	private boolean isChanged;
	/**
	 * Index of the tab.
	 */
	private int tabIndex;
	
	/**
	 * Public constructor
	 * @param index index of the tab.
	 */
	public TabFile(int index) {
		this(null, index);
	}

	/**
	 * Public constructor
	 * @param filePath path of the file opened in the tab
	 * @param index index of the tab
	 */
	public TabFile(Path filePath, int index) {
		this(filePath, null, index);
	}
	
	/**
	 * Public constructor
	 * @param filePath path of the file opened in the tab
	 * @param content content of the tab.
	 * @param index index of the current tab.
	 */
	public TabFile(Path filePath, String content, int index) {
		this.filePath = filePath;
		this.content = content;
		this.tabIndex = index;
		textArea = new JTextArea();
		textArea.setText(content);
		isChanged = false;
		textArea.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				setChanged(true);
			}		
		});
	}
	
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	

	/**
	 * @param isChanged the isChanged to set
	 */
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	/**
	 * @return the filePath
	 */
	public Path getFilePath() {
		return filePath;
	}

	/**
	 * @return isChanged
	 */
	public boolean getIsChanged(){
		return isChanged;
	}
	
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the textArea
	 */
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public String getContent(){
		return textArea.getText();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((textArea == null) ? 0 : textArea.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabFile other = (TabFile) obj;

		if(other.tabIndex != this.tabIndex){
			return false;
		}
		
		if (filePath == null) {
			return other.filePath == null;
		} else if (!filePath.equals(other.filePath))
			return false;
		
		return true;
	}
	
//	public ImageIcon getImageIcon(){
//		
//		String path = "icons/";
//		
//		if(isChanged){
//			path += "modified.png";
//		}else{
//			path += "saved.png";
//		}
//				
//		return new ImageIcon(
//                this.getClass().getResource(path));
//	}
	
	
}
