/*
MIT License

Copyright (c) 2019 Vincenzo Mazzeo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package it.ninjatech.swaggercodegenmavenplugin.configuration;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Data Type Mapping Configuration.
 * <br />
 * Used to map external Model classes. It is possible to define three types of
 * mapping:
 * <ul>
 * <li>
 * Direct Mapping: used to define explicitly the classes to map. For each
 * mapping an alias and the related fully qualified name of the class must be
 * defined.
 * </li>
 * <li>
 * Packages: used to define a list of packages to scan. All the classes
 * contained in each package are added to the mapping. To scan a package
 * recursively, the "**" wildcard must be added as subpackage.
 * </li>
 * <li>
 * External Resources: used to define a list of URL linking to YAML files, each
 * one containing a Data Type Mapping Configuration.
 * </li>
 * </ul>
 * </p>
 *
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
public class DataTypeMapping {

	/** Direct map. */
	private Map<String, String> directMap;

	/** Packages to scan. */
	private Set<String> packages;

	/** External resources. */
	private Set<URL> externalResources;

	/**
	 * Return the direct map.
	 *
	 * @return Direct map
	 */
	public Map<String, String> getDirectMap() {
		return this.directMap != null ? this.directMap : Collections.emptyMap();
	}

	/**
	 * Sets the direct map.
	 *
	 * @param Direct map
	 */
	public void setDirectMap(Map<String, String> directMap) {
		this.directMap = directMap;
	}

	/**
	 * Returns the packages to scan.
	 *
	 * @return Packages
	 */
	public Set<String> getPackages() {
		return this.packages != null ? this.packages : Collections.emptySet();
	}

	/**
	 * Sets the packages to scan.
	 *
	 * @param Packages
	 */
	public void setPackages(Set<String> packages) {
		this.packages = packages;
	}

	/**
	 * Returns the external resources.
	 *
	 * @return External resources
	 */
	public Set<URL> getExternalResources() {
		return this.externalResources != null ? this.externalResources : Collections.emptySet();
	}

	/**
	 * Sets the external resources.
	 *
	 * @param External resources
	 */
	public void setExternalResources(Set<URL> externalResources) {
		this.externalResources = externalResources;
	}

}
