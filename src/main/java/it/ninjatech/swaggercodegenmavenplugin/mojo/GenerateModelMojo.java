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

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.DefaultGenerator;

/**
 * <p>
 * Maven MOJO to generate the Model classes.
 * <br />
 * It is activated by the goal <strong>generate-model</strong> during the
 * <strong>generate sources</strong> phase.
 * </p>
 * 
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.0.0
 */
@Mojo(name = "generate-model", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateModelMojo extends AbstractGenerateMojo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.ninjatech.swaggercodegenmavenplugin.mojo.AbstractGenerateMojo#
	 * postDefaultGeneratorSetup(io.swagger.codegen.DefaultGenerator)
	 */
	@Override
	protected void postDefaultGeneratorSetup(DefaultGenerator defaultGenerator) {
		defaultGenerator.setGeneratorPropertyDefault(CodegenConstants.MODELS, Boolean.TRUE.toString());
	}

}
