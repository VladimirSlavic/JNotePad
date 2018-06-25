package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.JMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Custom JMenu with localizable options.
 * @author vladimir
 *
 */
@SuppressWarnings("serial")
public class LocalizedJMenu extends JMenu {

	/**
	 * Public constructor
	 * @param key key of text to be showing in the jmenu
	 * @param provider current localization provied
	 */
	public LocalizedJMenu(final String key, ILocalizationProvider provider) {
		setText(provider.getString(key));
		provider.addLocalizationListener(new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				setText(provider.getString(key));
				
			}
		});
	}
}
