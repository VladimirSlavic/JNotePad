package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * LocalizationProviderBridge which is a decorator for some other
 * IlocalizationProvider. This class offers two additional methods: connect()
 * and disconnect(), and it manages a connection status. It listens for
 * localization changes so that, when it receives the notification, it will
 * notify all listeners that are registered as its listeners.
 * 
 * @author vladimir
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * localization provider
	 */
	private final ILocalizationProvider provider;
	/**
	 * 	listener
	 */
	private final ILocalizationListener listener = () -> fire();
	/**
	 * Flag if component is still connected
	 */
	private boolean connected;

	/**
	 * Public constructor
	 * @param provider current provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		super();
		this.provider = provider;
	}

	/**
	 * Method for a component to connect
	 */
	public void connect() {
		if (connected)
			return;

		provider.addLocalizationListener(listener);
	}

	/**
	 * Method for a component to disconnect
	 */
	public void disconnect() {
		if (!connected)
			return;

		provider.removeLocalizationListener(listener);
	}

	@Override
	public String getString(String key) {
		return provider.getString(key);
	}
}
