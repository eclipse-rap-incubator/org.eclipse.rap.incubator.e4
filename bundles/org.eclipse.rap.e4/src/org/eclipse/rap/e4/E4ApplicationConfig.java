package org.eclipse.rap.e4;

public class E4ApplicationConfig {
	private String xmiURI;
	private boolean persistState;
	private boolean clearPersistedState;
	private String lifecycleURI;
	private String presentationURI;
	private String modelResourcehandlerURI;
	private boolean defaultPush;
	
	public E4ApplicationConfig(String xmiURI, String lifecycleURI, String presentationURI, String modelResourcehandlerURI, boolean persistState, boolean clearPersistedState, boolean defaultPush) {
		this.xmiURI = xmiURI;
		this.lifecycleURI = lifecycleURI;
		this.presentationURI = presentationURI;
		this.modelResourcehandlerURI = modelResourcehandlerURI;
		this.persistState = persistState;
		this.clearPersistedState = clearPersistedState;
		this.defaultPush = defaultPush;
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
	
	public String getModelResourcehandlerURI() {
		return modelResourcehandlerURI;
	}
	
	public String getXmiURI() {
		return xmiURI;
	}
	
	public boolean isDefaultPush() {
		return defaultPush;
	}
	
	public static E4ApplicationConfig create(String xmiURI) {
		return new E4ApplicationConfig(xmiURI, null, null, null, true, false, true);
	}
	
	public static E4ApplicationConfig create(String xmiURI, String lifecycleURI) {
		return new E4ApplicationConfig(xmiURI, lifecycleURI, null, null, true, false, true);
	}
	
	public static E4ApplicationConfig create(String xmiURI, boolean defaultPush) {
		return new E4ApplicationConfig(xmiURI, null, null, null, true, false, true);
	}
	
	public static E4ApplicationConfig create(String xmiURI, String lifecycleURI, boolean defaultPush) {
		return new E4ApplicationConfig(xmiURI, lifecycleURI, null, null, true, false, true);
	}
}
