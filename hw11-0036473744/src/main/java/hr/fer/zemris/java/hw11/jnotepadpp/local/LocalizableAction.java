package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Class that is basically just an ordinary abstract action with Localizable features supporting various languages.
 * @author vladimir
 *
 */
public abstract class LocalizableAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Public constructor
	 * @param key key of the word to be displayed in a specific language
	 * @param descKey key for the description of the action
	 * @param provider current localization provider.
	 */
	public LocalizableAction(String key, String descKey, ILocalizationProvider provider) {
		String translation = provider.getString(key);
		String descTranslation = provider.getString(descKey);
		putValue(Action.NAME,translation);
		putValue(Action.SHORT_DESCRIPTION, descTranslation);
		
		provider.addLocalizationListener(new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				String translation = provider.getString(key);
				String descTrans = provider.getString(descKey);
				putValue(Action.NAME,translation);
				putValue(Action.SHORT_DESCRIPTION, descTrans);
			}
		});
		
	}

	@Override
	public abstract void actionPerformed(ActionEvent e);

}
