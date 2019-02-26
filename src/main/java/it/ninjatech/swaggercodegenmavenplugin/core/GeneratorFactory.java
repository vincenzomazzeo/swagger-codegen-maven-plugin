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
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.logging.Log;

import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.config.CodegenConfigurator;
import io.swagger.codegen.languages.AbstractJavaCodegen;
import io.swagger.codegen.languages.features.BeanValidationFeatures;
import it.ninjatech.swaggercodegenmavenplugin.configuration.Configuration;

/**
 * <p>
 * Factory for {@link CodegenConfigurator}. <br>
 * It is a singleton that configures the {@link CodegenConfigurator} once.
 * </p>
 * 
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
public final class GeneratorFactory {

	/** Singleton instance. */
	private static GeneratorFactory instance;

	/**
	 * Returns the Singleton instance.
	 *
	 * @param log
	 *            Log
	 * @param configuration
	 *            Configuration
	 * @return Singleton instance
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static GeneratorFactory getInstance(Log log, Configuration configuration) throws IOException {
		return instance == null ? instance = new GeneratorFactory(log, configuration) : instance;
	}

	/** The log. */
	private final Log log;

	/** The Codegen Configurator. */
	private final CodegenConfigurator codegenConfigurator;

	/**
	 * Instantiates a new Generator Factory.
	 *
	 * @param log
	 *            Log
	 * @param configuration
	 *            Configuration
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private GeneratorFactory(Log log, Configuration configuration) throws IOException {
		this.log = log;
		this.codegenConfigurator = new CodegenConfigurator();

		log.info(configuration.toString());

		configure(configuration);
	}

	/**
	 * Makes a new instance of the {@link DefaultGenerator} from the {@link CodegenConfigurator} set with the source passed as input.
	 *
	 * @param source
	 *            SWAGGER source
	 * @return Default Generator
	 */
	public DefaultGenerator make(URL source) {
		DefaultGenerator result = null;

		this.codegenConfigurator.setInputSpec(source.toString());

		ClientOptInput input = this.codegenConfigurator.toClientOptInput();

		result = new DefaultGenerator();
		result.opts(input);
		result.setGeneratorPropertyDefault(CodegenConstants.APIS, Boolean.FALSE.toString());
		result.setGeneratorPropertyDefault(CodegenConstants.MODELS, Boolean.FALSE.toString());
		result.setGeneratorPropertyDefault(CodegenConstants.SUPPORTING_FILES, Boolean.FALSE.toString());
		result.setGeneratorPropertyDefault(CodegenConstants.MODEL_TESTS, Boolean.FALSE.toString());
		result.setGeneratorPropertyDefault(CodegenConstants.MODEL_DOCS, Boolean.FALSE.toString());
		result.setGeneratorPropertyDefault(CodegenConstants.API_TESTS, Boolean.FALSE.toString());
		result.setGeneratorPropertyDefault(CodegenConstants.API_DOCS, Boolean.FALSE.toString());

		return result;
	}

	/**
	 * Configures the {@link CodegenConfigurator} with the passed {@link Configuration}.
	 *
	 * @param configuration
	 *            Configuration
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void configure(Configuration configuration) throws IOException {
		this.codegenConfigurator.setLang(configuration.getJavaCodegen().getName());
		this.codegenConfigurator.setVerbose(configuration.isVerbose());
		this.codegenConfigurator.setOutputDir(configuration.getOutputFolder().getAbsolutePath());
		this.codegenConfigurator.setModelPackage(configuration.getModelPackage());
		this.codegenConfigurator.setApiPackage(configuration.getApiPackage());
		this.codegenConfigurator.addAdditionalProperty(AbstractJavaCodegen.DATE_LIBRARY, configuration.getDateLibrary().getValue());
		this.codegenConfigurator.addAdditionalProperty(BeanValidationFeatures.USE_BEANVALIDATION, configuration.isEnableBeanValidation());
		this.codegenConfigurator.addAdditionalProperty(AbstractJavaCodegen.JAVA8_MODE, configuration.isEnableJava8());
		this.codegenConfigurator.addAdditionalProperty(Codegen.FORCE_JDK8_OFF, !configuration.isEnableJava8());

		Map<String, TypeData> typeMapping = DataTypeMappingHandler.handle(this.log, configuration.getDataTypeMapping());
		if (!typeMapping.isEmpty()) {
			this.log.info("Data Type Mapping");
			for (Entry<String, TypeData> typeMappingEntry : typeMapping.entrySet()) {
				this.codegenConfigurator.addTypeMapping(typeMappingEntry.getKey(), typeMappingEntry.getValue().getName());
				this.codegenConfigurator.addImportMapping(typeMappingEntry.getKey(), typeMappingEntry.getValue().getFullyQualifiedName());

				this.log.info(String.format("    %s -> %s", typeMappingEntry.getKey(), typeMappingEntry.getValue().getFullyQualifiedName()));
			}
		}
	}

}
