package it.ninjatech.swaggercodegenmavenplugin;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class DataTypeMapping {

	private Map<String, String> importMapping;
	private Set<String> packages;
	private Set<URL> externalResources;
	
	public Map<String, String> getImportMapping() {
		return this.importMapping != null ? this.importMapping : Collections.emptyMap();
	}
	
	public void setImportMapping(Map<String, String> importMapping) {
		this.importMapping = importMapping;
	}
	
	public Set<String> getPackages() {
		return this.packages != null ? this.packages : Collections.emptySet();
	}
	
	public void setPackages(Set<String> packages) {
		this.packages = packages;
	}
	
	public Set<URL> getExternalResources() {
		return this.externalResources != null ? this.externalResources : Collections.emptySet();
	}
	
	public void setExternalResources(Set<URL> externalResources) {
		this.externalResources = externalResources;
	}
	
}
