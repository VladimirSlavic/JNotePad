package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider{
	/**
	 * Current language
	 */
	private String language;
	/**
	 * ResourceBundle
	 */
	private ResourceBundle bundle;
	/**
	 * Path for the key-value language definitions.
	 */
	private static final String BASE = LocalizationProvider.class.getPackage().getName() + ".prijevodi";
	/**
	 * Singleton LocalizationProvider
	 */
	private final static LocalizationProvider localizationProvider = new LocalizationProvider();
	
	/**
	 * Public constructor.
	 */
	private LocalizationProvider() {
		super();
		setLanguage("en");
	}
	
	/**
	 * Getter for the singleton instance of the localization provider.
	 * @return
	 */
	public synchronized static LocalizationProvider getInstance(){
		return localizationProvider;
	}
	
	@Override
	public String getString(String key) {	
		
		String text = "Uknown encoding";
		
		try {
			text = new String(bundle.getString(key).getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		};
		
		return text;
	}
	
	/**
	 * Method for setting the language.
	 * @param lang desired language
	 */
	public synchronized void setLanguage(String lang){
		if (lang.equalsIgnoreCase(this.language)) {
            return; 
        }

        this.language = lang;
        final Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle(BASE, locale);

        fire();
	}
	
	/**
	 * Getter of the current language.
	 * @return
	 */
	public String getLanguage(){
		return language;
	}
}
