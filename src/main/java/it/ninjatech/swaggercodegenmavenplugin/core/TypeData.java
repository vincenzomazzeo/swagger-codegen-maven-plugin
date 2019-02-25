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

/**
 * Wrapper class containing the fully qualified name and the name of a class
 * that must be added to the data type mapping.
 * 
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
public final class TypeData {

	/** The fully qualified name. */
	private final String fullyQualifiedName;

	/** The name. */
	private final String name;

	/**
	 * Instantiates a new Type Data.
	 *
	 * @param fullyQualifiedName
	 *            Fully qualified name
	 * @param name
	 *            Name
	 */
	protected TypeData(String fullyQualifiedName, String name) {
		this.fullyQualifiedName = fullyQualifiedName;
		this.name = name;
	}

	/**
	 * Returns the fully qualified name.
	 *
	 * @return Fully qualified name
	 */
	protected String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	/**
	 * Returns the name.
	 *
	 * @return Name
	 */
	protected String getName() {
		return name;
	}

}
