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

/**
 * <p>
 * Enumeration for Date Libraries.
 * </p>
 *
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
public enum DateLibrary {

        /** Legacy library. */
		LEGACY("legacy"),

		/** Java8 library. */
		JAVA8("java8"),

		/** Java8 JSR-310 library. */
		JAVA8_LOCAL_DATE_TIME("java8-localdatetime"),

		/** Joda library. */
		JODA("joda"),

		/** ThreeTen backport library. */
		THREE_TEN("threetenbp");

	/** Value. */
	private final String value;

	/**
	 * Instantiates a new DateLibrary.
	 *
	 * @param value
	 *            value
	 */
	private DateLibrary(String value) {
		this.value = value;
	}

	/**
	 * Returns the value
	 * 
	 * @return The value
	 */
	public String getValue() {
		return this.value;
	}

}
