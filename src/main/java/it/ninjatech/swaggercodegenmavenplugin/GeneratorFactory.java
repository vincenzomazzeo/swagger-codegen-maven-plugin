package it.ninjatech.swaggercodegenmavenplugin;

import java.io.File;
import java.net.URL;
import java.util.Map.Entry;

import org.apache.maven.plugin.logging.Log;

import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.config.CodegenConfigurator;

public final class GeneratorFactory {

	private static GeneratorFactory instance;
	
	protected static GeneratorFactory getInstance() {
		return instance == null ? instance = new GeneratorFactory() : instance;
	}
	
	private final CodegenConfigurator codegenConfigurator;
	private Log log;
	private boolean configured;
	
	private GeneratorFactory() {
		this.codegenConfigurator  = new CodegenConfigurator();
		this.configured = false;
	}
	
	protected void setLog(Log log) {
		this.log = log;
	}
	
	protected boolean isConfigured() {
		return this.configured;
	}

	protected void configure(Class<?> lang, boolean verbose, File outputFolder, String modelPackage, String apiPackage, DataTypeMapping dataTypeMapping) {
		this.codegenConfigurator.setLang(lang.getName());
		this.codegenConfigurator.setVerbose(verbose);
		this.codegenConfigurator.setOutputDir(outputFolder.getAbsolutePath());
		this.codegenConfigurator.setModelPackage(modelPackage);
		this.codegenConfigurator.setApiPackage(apiPackage);
		handleDataTypeMapping(dataTypeMapping);
		
		this.configured = true;
	}
	
	protected DefaultGenerator make(URL source) {
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
	
	private void handleDataTypeMapping(DataTypeMapping dataTypeMapping) {
		if (dataTypeMapping != null) {
			for (Entry<String, String> importMapping : dataTypeMapping.getImportMapping().entrySet()) {
				addImportMapping(importMapping.getKey(), importMapping.getValue());
			}
			for (String packageName : dataTypeMapping.getPackages()) {
				addPackages(packageName);
			}
			for (URL externalResource : dataTypeMapping.getExternalResources()) {
				addExternalResource(externalResource);
			}
		}
	}
	
	private void addImportMapping(String alias, String typeFQN) {
		int index = typeFQN.lastIndexOf('.');
		String type = typeFQN.substring(index + 1);
		
		this.codegenConfigurator.addTypeMapping(alias, type);
		this.codegenConfigurator.addImportMapping(alias, typeFQN);
		
		this.log.info(String.format("Added %s -> %s to Data Type Mapping", alias, typeFQN));
	}
	
	private void addPackages(String packageName) {
		// TODO add recursion
	}
	
	private void addExternalResource(URL externalResource) {
		// TODO
	}
	
}
