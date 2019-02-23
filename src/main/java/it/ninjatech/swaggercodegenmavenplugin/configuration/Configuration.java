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

import java.io.File;
import java.net.URL;
import java.util.List;

import io.swagger.codegen.languages.AbstractJavaCodegen;

/**
 * <p>
 * Configuration of the Plug-in.
 * </p>
 *
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
public class Configuration {

	/** AbstractJavaCodegen class to use for generating code. */
	private Class<? extends AbstractJavaCodegen> javaCodegen;

	/** Verbose output. */
	private boolean verbose;

	/** Output folder. */
	private File outputFolder;

	/** Source SWAGGER files to elaborate. */
	private List<URL> sourceFiles;

	/** Package for Model classes. */
	private String modelPackage;

	/** Package for API interfaces. */
	private String apiPackage;

	/** Data type mapping. */
	private DataTypeMapping dataTypeMapping;

	/**
	 * Returns the AbstractJavaCodegen.
	 *
	 * @return AbstractJavaCodegen
	 */
	public Class<? extends AbstractJavaCodegen> getJavaCodegen() {
		return javaCodegen;
	}

	/**
	 * Sets the AbstractJavaCodegen.
	 *
	 * @param AbstractJavaCodegen
	 * @return The instance of the Configuration
	 */
	public Configuration setJavaCodegen(Class<? extends AbstractJavaCodegen> javaCodegen) {
		this.javaCodegen = javaCodegen;

		return this;
	}

	/**
	 * Checks if to produce verbose output.
	 *
	 * @return true, if verbose is enabled
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Sets the verbose output.
	 *
	 * @param verbose
	 * @return The instance of the Configuration
	 */
	public Configuration setVerbose(boolean verbose) {
		this.verbose = verbose;

		return this;
	}

	/**
	 * Returns the output folder.
	 *
	 * @return Output folder
	 */
	public File getOutputFolder() {
		return outputFolder;
	}

	/**
	 * Sets the output folder.
	 *
	 * @param Output folder
	 * @return The instance of the Configuration
	 */
	public Configuration setOutputFolder(File outputFolder) {
		this.outputFolder = outputFolder;

		return this;
	}

	/**
	 * Returns the SWAGGER source files to elaborate.
	 *
	 * @return SWAGGER source files
	 */
	public List<URL> getSourceFiles() {
		return sourceFiles;
	}

	/**
	 * Sets the SWAGGER source files to elaborate.
	 *
	 * @param SWAGGER source files
	 * @return The instance of the Configuration
	 */
	public Configuration setSourceFiles(List<URL> sourceFiles) {
		this.sourceFiles = sourceFiles;

		return this;
	}

	/**
	 * Returns the package for Model classes.
	 *
	 * @return Package for Model classes
	 */
	public String getModelPackage() {
		return modelPackage;
	}

	/**
	 * Sets the package for Model classes.
	 *
	 * @param Package for Model classes
	 * @return The instance of the Configuration
	 */
	public Configuration setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;

		return this;
	}

	/**
	 * Returns the package for API interfaces.
	 *
	 * @return Package for API interfaces
	 */
	public String getApiPackage() {
		return apiPackage;
	}

	/**
	 * Sets the package for API interfaces.
	 *
	 * @param Package for API interfaces
	 * @return The instance of the Configuration
	 */
	public Configuration setApiPackage(String apiPackage) {
		this.apiPackage = apiPackage;

		return this;
	}

	/**
	 * Return the data type mapping.
	 *
	 * @return Data type mapping
	 */
	public DataTypeMapping getDataTypeMapping() {
		return dataTypeMapping;
	}

	/**
	 * Sets the data type mapping.
	 *
	 * @param Data type mapping
	 * @return The instance of the Configuration
	 */
	public Configuration setDataTypeMapping(DataTypeMapping dataTypeMapping) {
		this.dataTypeMapping = dataTypeMapping;

		return this;
	}

}
