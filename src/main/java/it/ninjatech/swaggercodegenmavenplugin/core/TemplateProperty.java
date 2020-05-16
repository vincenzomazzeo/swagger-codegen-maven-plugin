package it.ninjatech.swaggercodegenmavenplugin.core;

import java.util.List;

public class TemplateProperty {

    private final String baseType;
    private final List<String> templates;

    public TemplateProperty(String baseType, List<String> templates) {
        this.baseType = baseType;
        this.templates = templates;
    }

    public String getBaseType() {
        return this.baseType;
    }

    public List<String> getTemplates() {
        return this.templates;
    }

}
