package it.ninjatech.swaggercodegenmavenplugin;

import org.apache.maven.plugins.annotations.Mojo;

import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.DefaultGenerator;

@Mojo(name = "generate-api")
public class GenerateApiMojo extends AbstractGenerateMojo {

	@Override
	protected void postDefaultGeneratorSetup(DefaultGenerator defaultGenerator) {
		defaultGenerator.setGeneratorPropertyDefault(CodegenConstants.APIS, Boolean.TRUE.toString());
	}
	
}
