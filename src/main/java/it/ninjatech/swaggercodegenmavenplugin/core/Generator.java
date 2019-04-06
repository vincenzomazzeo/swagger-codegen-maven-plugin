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

import org.springframework.core.io.ClassPathResource;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.DefaultGenerator;

/**
 * <p>
 * Extension of the DefaultGenerator. <br>
 * </p>
 *
 * @author Vincenzo Mazzeo
 * @version 1.0
 * @since 1.1.0
 */
public class Generator extends DefaultGenerator {

	@Override
	public String getFullTemplateFile(CodegenConfig config, String templateFile) {
		if (templateFile.equals(config.apiTemplateFiles().keySet().stream().findFirst().get())) {
			ClassPathResource mustache = new ClassPathResource("swaggerCodegenMavenPluginApi.mustache");
			return mustache.getPath();
		}
		return super.getFullTemplateFile(config, templateFile);
	}

}
