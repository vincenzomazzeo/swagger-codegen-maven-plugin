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
package it.ninjatech.swaggercodegenmavenplugin.mojo;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import io.swagger.codegen.DefaultGenerator;
import it.ninjatech.swaggercodegenmavenplugin.configuration.Configuration;
import it.ninjatech.swaggercodegenmavenplugin.configuration.DataTypeMapping;
import it.ninjatech.swaggercodegenmavenplugin.configuration.DateLibrary;
import it.ninjatech.swaggercodegenmavenplugin.core.Codegen;
import it.ninjatech.swaggercodegenmavenplugin.core.GeneratorFactory;

/**
 * <p>
 * Abstract Maven MOJO for {@link GenerateApiMojo} and
 * {@link GenerateModelMojo}.
 * </p>
 * 
 * @author Vincenzo Mazzeo
 * @version 2.0
 * @since 1.0.0
 */
public abstract class AbstractGenerateMojo extends AbstractMojo {

    /** ID. */
    @Parameter(required = true, defaultValue = "false")
    private String id;

    /** Verbose output. */
    @Parameter(required = false, defaultValue = "false")
    private boolean verbose;

    /** Output folder. */
    @Parameter(required = true)
    private File outputFolder;

    /** Source SWAGGER files to elaborate. */
    @Parameter(required = true)
    private List<URL> sourceFiles;

    /** Package for Model classes. */
    @Parameter(required = false)
    private String modelPackage;

    /** Package for API interfaces. */
    @Parameter(required = false)
    private String apiPackage;

    /** Data type mapping. */
    @Parameter(required = false)
    private DataTypeMapping dataTypeMapping;

    /** Enable Java 8 */
    @Parameter(required = true, defaultValue = "false")
    private boolean enableJava8;

    /** Date Library to use */
    @Parameter(required = true, defaultValue = "JAVA8_LOCAL_DATE_TIME")
    private DateLibrary dateLibrary;

    /** Enable Bean Validation */
    @Parameter(required = true, defaultValue = "true")
    private boolean enableBeanValidation;

    /** Security Headers as method arguments */
    @Parameter(required = true, defaultValue = "true")
    private boolean securityHeadersAsArguments;

    /** Base Path as Root */
    @Parameter(required = true, defaultValue = "true")
    private boolean basePathAsRoot;

    /** API's Suffix */
    @Parameter(required = false)
    private String apiSuffix;

    /** Model's Name Suffix */
    @Parameter(required = false)
    private String modelNameSuffix;

    /**
     * Permits to the descending MOJO classes to apply custom settings to the Default Generator after the common setup.
     *
     * @param defaultGenerator
     *            Default Generator
     */
    protected abstract void postDefaultGeneratorSetup(DefaultGenerator defaultGenerator);

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            GeneratorFactory generatorFactory = GeneratorFactory.getInstance(getLog(), getConfiguration());

            for (URL sourceFile : this.sourceFiles) {
                getLog().info(String.format("Processing %s", sourceFile.toString()));

                DefaultGenerator defaultGenerator = generatorFactory.make(sourceFile);

                postDefaultGeneratorSetup(defaultGenerator);

                defaultGenerator.generate();
            }
        } catch (Exception e) {
            throw new MojoFailureException("Failure", e);
        }
    }

    /**
     * Returns the {@link Configuration} filled with the plug-in input parameters.
     *
     * @return {@link Configuration}
     */
    private Configuration getConfiguration() {
        Configuration result = new Configuration(this.id);

        result.setJavaCodegen(Codegen.class)
              .setVerbose(this.verbose)
              .setOutputFolder(this.outputFolder)
              .setSourceFiles(this.sourceFiles)
              .setModelPackage(this.modelPackage)
              .setApiPackage(this.apiPackage)
              .setDataTypeMapping(this.dataTypeMapping)
              .setEnableJava8(this.enableJava8)
              .setDateLibrary(this.dateLibrary)
              .setEnableBeanValidation(this.enableBeanValidation)
              .setSecurityHeadersAsArguments(this.securityHeadersAsArguments)
              .setBasePathAsRoot(this.basePathAsRoot)
              .setApiSuffix(this.apiSuffix)
              .setModelNameSuffix(this.modelNameSuffix);

        return result;
    }

}
