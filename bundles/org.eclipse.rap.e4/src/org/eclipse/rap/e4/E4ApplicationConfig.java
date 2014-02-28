package org.eclipse.rap.e4;

public class E4ApplicationConfig {
	private String xmiURI;
	private boolean persistState;
	private boolean clearPersistedState;
	private String lifecycleURI;
	private String presentationURI;
	
	public E4ApplicationConfig(String xmiURI, String lifecycleURI, String presentationURI, boolean persistState, boolean clearPersistedState) {
		this.xmiURI = xmiURI;
		this.lifecycleURI = lifecycleURI;
		this.presentationURI = presentationURI;
		this.persistState = persistState;
		this.clearPersistedState = clearPersistedState;
	}
	
	public String getLifecycleURI() {
		return lifecycleURI;
	}
	
	public boolean isPersistState() {
		return persistState;
	}
	
	public boolean isClearPersistedState() {
		return clearPersistedState;
	}
	
	public String getPresentationURI() {
		return presentationURI;
	}
	
	public String getXmiURI() {
		return xmiURI;
	}
	
	public static E4ApplicationConfig create(String xmiURI) {
		return new E4ApplicationConfig(xmiURI, null, null, true, false);
	}
}
