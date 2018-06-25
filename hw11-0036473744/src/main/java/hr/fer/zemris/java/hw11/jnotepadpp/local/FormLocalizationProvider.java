package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Localization bridge implementation enabling the program to close without the worry of still existing pointers to the jframe.
 * @author vladimir
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge{

	/**
	 * Public constructor
	 * @param provider current provider
	 * @param frame frame
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}
			
		});
	}
}
