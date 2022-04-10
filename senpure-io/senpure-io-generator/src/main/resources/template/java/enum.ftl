package ${javaPackage};

/**<#if hasExplain>
 * ${explain}
 * </#if>
${sovereignty}
 * @time ${.now?datetime}
 */
public enum ${javaName} {
<#list fields as field>
    ${field.name}(${field.index}),<#if field.hasExplain>//${field.explain}</#if>
</#list>
    UNKNOWN(-1);

    private int value;

    ${javaName}(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

<#list fields as field>
    public static final int ${field.name}_VALUE = ${field.index};
</#list>

    public static ${javaName}  get${javaName} (int value) {
        switch(value){
<#list fields as field>
        case ${field.name}_VALUE:
            return ${field.name};
</#list>
        default:
            return UNKNOWN;
        }
    }

}