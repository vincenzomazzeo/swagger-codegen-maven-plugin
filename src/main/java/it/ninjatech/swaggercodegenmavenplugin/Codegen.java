package it.ninjatech.swaggercodegenmavenplugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.languages.SpringCodegen;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.models.properties.PropertyBuilder.PropertyId;
import io.swagger.util.Json;

public final class Codegen extends SpringCodegen {

	private static final String X_TYPE = "x-type";
	private static final String X_SUPER_CLASS = "x-superClass";
	
	@Override
	public CodegenType getTag() {
      return CodegenType.SERVER;
    }
    
	@Override
    public String getName() {
      return "NinjaTech Codegen";
    }
    
    @Override
    public String getHelp() {
      return "Generates Java Interfaces and Models.";
    }
    
    @Override
    public void processOpts() {
      setInterfaceOnly(true); // Excludes the Controllers Generation
      
      super.processOpts();
    }
    
    @Override
    public String getSwaggerType(Property property) {
      String swaggerType = super.getSwaggerType(property);
      
      if ("string".equalsIgnoreCase(swaggerType) && property.getVendorExtensions().containsKey(X_TYPE)) {
          swaggerType = (String)property.getVendorExtensions().get(X_TYPE);
      }
      else if ("string".equalsIgnoreCase(swaggerType) && property.getFormat() != null && property.getFormat().startsWith("x-")) {
    	  swaggerType = property.getFormat().substring("x-".length());
      }
      
      return swaggerType;
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> postProcessModels(Map<String, Object> objs) {
    	// Super Class Management
		List<Map<String, ?>> modelMaps = (List<Map<String, ?>>)objs.get("models");
		
		for (Map<String, ?> modelsMap : modelMaps) {
			CodegenModel model = (CodegenModel)modelsMap.get("model");
			if (model.vendorExtensions.containsKey(X_SUPER_CLASS)) {
				List<String> superClasses = (List<String>)model.vendorExtensions.get(X_SUPER_CLASS);
				if (superClasses.size() != 1) {
					throw new RuntimeException(String.format("%s extensions must have one and only one value", X_SUPER_CLASS));
				}
				String superClass = superClasses.get(0);
				addImport(objs, superClass);
				model.parent = superClass;
			}
		}
		
		return super.postProcessModels(objs);
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
		Map<String, ?> operationLists = (Map<String, ?>)objs.get("operations");
		List<CodegenOperation> operations = (List<CodegenOperation>)operationLists.get("operation");
		
		for (CodegenOperation operation : operations) {
			if (this.typeMapping.containsKey(operation.returnBaseType)) {
				operation.returnBaseType = this.typeMapping.get(operation.returnBaseType);
			}
			if (operation.responses != null) {
				for (CodegenResponse response : operation.responses) {
					if (this.typeMapping.containsKey(response.baseType)) {
						response.baseType = this.typeMapping.get(response.baseType);
					}
				}
			}
			
			// In case of external types in body this is the only point where to add the imports.
			if (operation.getHasBodyParam() && operation.bodyParam.vendorExtensions.containsKey(X_TYPE)) {
				CodegenParameter bodyParam = operation.bodyParam;
				addImport(objs, bodyParam.baseType);
			}
		}
		
		return super.postProcessOperations(objs);
	}

	@Override
	public void postProcessParameter(CodegenParameter parameter) {
		if (parameter.isBodyParam && StringUtils.isNotBlank(parameter.jsonSchema)) {
			try {
				JsonNode rootNode = Json.mapper().readTree(parameter.jsonSchema);
				JsonNode schemaNode = rootNode.findPath("schema");
				if (!schemaNode.isMissingNode()) {
					JsonNode xTypeNode = schemaNode.findPath(X_TYPE);
					if (!xTypeNode.isMissingNode()) {
						Map<String, Object> vendorExtensions = Collections.singletonMap(X_TYPE, xTypeNode.asText());
						Property property = PropertyBuilder.build(parameter.baseType.toLowerCase(), null, Collections.singletonMap(PropertyId.VENDOR_EXTENSIONS, vendorExtensions));
						String type = getSwaggerType(property);
						parameter.baseType = type;
						parameter.dataType = this.typeMapping.get(type);
						parameter.isPrimitiveType = true;
						parameter.vendorExtensions.putAll(vendorExtensions);
					}
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		super.postProcessParameter(parameter);
	}
	
	@SuppressWarnings("unchecked")
	private void addImport(Map<String, Object> objs, String alias) {
		List<Map<String, String>> imports = (List<Map<String, String>>)objs.get("imports");
		String aliasFQN = this.importMapping.get(alias);
		
		if (aliasFQN == null) {
			throw new RuntimeException(String.format("Missing import mapping for %s", alias));
		}
		
		boolean importAlreadyPresent = false;
		for (Map<String, String> import_ : imports) {
			if (import_.containsValue(aliasFQN)) {
				importAlreadyPresent = true;
				break;
			}
		}
		
		if (!importAlreadyPresent) {
			imports.add(Collections.singletonMap("import", aliasFQN));
		}
	}
    
}
