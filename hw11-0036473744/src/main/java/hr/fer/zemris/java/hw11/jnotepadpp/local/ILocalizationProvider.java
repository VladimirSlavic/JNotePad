package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Base interface for method a localization method must support for his listeners.
 * @author vladimir
 *
 */
public interface ILocalizationProvider {
	/**
	 * Getting desired text in a specified language based on a key.
	 * @param key key of the text the user wishes to acquire.
	 * @return returns text for current localization settings.
	 */
	String getString(String key);
	
	/**
	 * Adds a localization listener.
	 * @param listener
	 */
	void addLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Removes a localization listener
	 * @param listener
	 */
	void removeLocalizationListener(ILocalizationListener listener);
}
