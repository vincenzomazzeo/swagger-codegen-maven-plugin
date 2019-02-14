package it.ninjatech.swaggercodegenmavenplugin;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import io.swagger.codegen.DefaultGenerator;

public abstract class AbstractGenerateMojo extends AbstractMojo {

	@Parameter(required = false, defaultValue = "false")
	private boolean verbose;

	@Parameter(required = true)
	private File outputFolder;
	
	@Parameter(required = true)
	private List<URL> sourceFiles;
	
	@Parameter(required = false)
    private String modelPackage;
	
	@Parameter(required = false)
    private String apiPackage;
	
	@Parameter(required = false)
	private DataTypeMapping dataTypeMapping;

	protected abstract void postDefaultGeneratorSetup(DefaultGenerator defaultGenerator);

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		GeneratorFactory generatorFactory = GeneratorFactory.getInstance();
		
		generatorFactory.setLog(getLog());
		if (!generatorFactory.isConfigured()) {
			generatorFactory.configure(Codegen.class, this.verbose, this.outputFolder, this.modelPackage, this.apiPackage, this.dataTypeMapping);
		}

		for (URL sourceFile : this.sourceFiles) {
			getLog().info(String.format("Processing %s", sourceFile.toString()));
			
			DefaultGenerator defaultGenerator = generatorFactory.make(sourceFile);
			
			postDefaultGeneratorSetup(defaultGenerator);
			
			defaultGenerator.generate();
		}
		
	}
	
}
