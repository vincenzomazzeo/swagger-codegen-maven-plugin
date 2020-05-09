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

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

/**
 * <p>
 * Type Filter used to filter the classes during the package scanning. <br>
 * Filters all the classes belonging to the <i>base package</i> passed as input and all the classes of its sub packages if the "**" wildcard has been
 * specified.
 * </p>
 * 
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
public class ClassTypeFilter implements TypeFilter {

    /** The Constant RECURSIVE_PATTERN. */
    private static final String RECURSIVE_PATTERN = ".**";

    /** Recursive flag. */
    private final boolean recursive;

    /** Base package. */
    private final String basePackage;

    /**
     * Instantiates a new Class Type Filter.
     *
     * @param basePackage
     *            Base package
     */
    protected ClassTypeFilter(String basePackage) {
        this.recursive = basePackage.endsWith(RECURSIVE_PATTERN);
        this.basePackage = this.recursive ? basePackage.substring(0, basePackage.length() - RECURSIVE_PATTERN.length()) : basePackage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.core.type.filter.TypeFilter#match(org.springframework.
     * core.type.classreading.MetadataReader,
     * org.springframework.core.type.classreading.MetadataReaderFactory)
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        return match(metadataReader.getClassMetadata());
    }

    /**
     * Matching method. <br>
     * Return <i>true</i> if the class belongs to the base package or one of its sub packages if the "**" wildcard has been specified.
     *
     * @param metadata
     *            Metadata of the class to check
     * @return true, if successful
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public boolean match(ClassMetadata metadata) throws IOException {
        boolean result = false;

        try {
            Class<?> type = Class.forName(metadata.getClassName());
            if (metadata.isAbstract() || metadata.isConcrete()) {
                if (this.recursive) {
                    result = StringUtils.startsWith(type.getPackage().getName(),
                                                    this.basePackage);
                } else {
                    result = StringUtils.equals(type.getPackage().getName(),
                                                this.basePackage);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
