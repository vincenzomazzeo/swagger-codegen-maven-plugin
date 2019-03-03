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
package it.ninjatech.swaggercodegenmavenplugin.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.maven.plugin.logging.Log;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.yaml.snakeyaml.Yaml;

import it.ninjatech.swaggercodegenmavenplugin.configuration.DataTypeMapping;

/**
 * <p>
 * Handler for resolving the {@link DataTypeMapping} configuration.
 * </p>
 * 
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
public class DataTypeMappingHandler {

	/**
	 * Handles the {@link DataTypeMapping} configuration.
	 *
	 * @param log
	 *            Log
	 * @param dataTypeMapping
	 *            Data type mapping
	 * @return the Resolved Map
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected static Map<String, TypeData> handle(Log log, DataTypeMapping dataTypeMapping) throws IOException {
		Map<String, TypeData> result = null;

		if (dataTypeMapping != null) {
			result = new HashMap<>();

			handle(log, dataTypeMapping, result);
		}
		else {
			result = Collections.emptyMap();
		}

		return result;
	}

	/**
	 * Handle the {@link DataTypeMapping} configuration.
	 *
	 * @param log
	 *            Log
	 * @param dataTypeMapping
	 *            Data type mapping
	 * @param typeMap
	 *            the Resolved Map
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void handle(Log log, DataTypeMapping dataTypeMapping, Map<String, TypeData> typeMap) throws IOException {
		handleDirectMap(typeMap, dataTypeMapping.getDirectMap());
		handlePackages(log, typeMap, dataTypeMapping.getPackages());
		handleExternalResources(log, typeMap, dataTypeMapping.getExternalResources());
	}

	/**
	 * Handles the Direct Mapping.
	 *
	 * @param typeMap
	 *            the Resolved Map
	 * @param directMap
	 *            Direct Mapping
	 */
	private static void handleDirectMap(Map<String, TypeData> typeMap, Map<String, String> directMap) {
		for (Entry<String, String> directMapEntry : directMap.entrySet()) {
			String alias = directMapEntry.getKey();
			typeMap.put(alias, getTypeData(directMapEntry.getValue()));
		}
	}

	/**
	 * Handles the Packages to scan.
	 *
	 * @param log
	 *            Log
	 * @param typeMap
	 *            the Resolved Map
	 * @param packages
	 *            Packages to scan
	 */
	private static void handlePackages(Log log, Map<String, TypeData> typeMap, Set<String> packages) {
		for (String package_ : packages) {
			log.info(String.format("# Scanning package %s", package_));
			ClassPathScanningCandidateComponentProvider scanner = new CustomClassPathScanningCandidateComponentProvider();
			scanner.addIncludeFilter(new ClassTypeFilter(package_));
			Set<BeanDefinition> components = scanner.findCandidateComponents(package_);
			for (BeanDefinition component : components) {
				TypeData typeData = getTypeData(component.getBeanClassName());
				typeMap.put(typeData.getName(), typeData);
			}
		}
	}

	/**
	 * Handles the External Resources.
	 * 
	 *
	 * @param log
	 *            Log
	 * @param typeMap
	 *            the Resolved Map
	 * @param externalResources
	 *            External Resources
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void handleExternalResources(Log log, Map<String, TypeData> typeMap, Set<URL> externalResources) throws IOException {
		for (URL externalResource : externalResources) {
			log.info(String.format("--- External Resource -> %s ---", externalResource.toString()));
			DataTypeMapping dataTypeMapping = null;
			try (InputStream is = externalResource.openStream()) {
				Yaml yaml = new Yaml();
				dataTypeMapping = yaml.loadAs(is, DataTypeMapping.class);
			}
			if (dataTypeMapping != null) {
				handle(log, dataTypeMapping, typeMap);
			}
		}
	}

	/**
	 * Returns the type data.
	 *
	 * @param fullyQualifiedName
	 *            Fully qualified name
	 * @return Type data
	 */
	private static TypeData getTypeData(String fullyQualifiedName) {
		TypeData result = null;

		int index = fullyQualifiedName.lastIndexOf('.');
		String name = fullyQualifiedName.substring(index + 1);
		result = new TypeData(fullyQualifiedName, name);

		return result;
	}

	/**
	 * Private constructor.
	 */
	private DataTypeMappingHandler() {
	}

}
