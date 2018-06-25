package hr.fer.zemris.java.hw11.jnotepadpp.local;
import java.util.*;

/**
 * Abstract represenation of a localization provider.
 * @author vladimir
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider{
	
	/**
	 * All current listeners.
	 */
	private List<ILocalizationListener> listeners;
	
	/**
	 * Public constructor
	 */
	public AbstractLocalizationProvider() {
		listeners = new ArrayList<>();
	}
	

	@Override
	public abstract String getString(String key);

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		if(listener == null){
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		
		if(!listeners.contains(listener)){
			listeners.add(listener);	
		}
		
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		if(listener == null){
			throw new IllegalArgumentException("Cannot remove null reference from list of data listeners.");
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.add(listener);
		
	}

	/**
	 * Method notifying all listeners on current changes.
	 */
	public void fire(){
		listeners.forEach(e->{
			e.localizationChanged();
		});
	}
	
}
