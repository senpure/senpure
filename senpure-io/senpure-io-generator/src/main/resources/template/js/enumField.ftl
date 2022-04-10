<#if bean.hasExplain>
/**
 *
 *${bean.explain}
 */
</#if>
${bean.js.namespace}.${bean.js.name} = {
<#list bean.fields as field>
    <#if field.hasExplain>
    //${field.explain}
    </#if>
    ${field.name}: ${field.index},
</#list>
    UNKNOWN: -1,
    ${lowerCamelCase(bean.js.name)}Values: {
<#list bean.fields as field>
                            <#--${bean.js.namespace}.${bean.js.name}.${field.name}-->
        ${field.index}: ${field.index}<#if field_has_next>,</#if>
</#list>
    },
    ${lowerCamelCase(bean.js.name)}Str: {
<#list bean.fields as field>
        ${field.index}: "${field.name}"<#if field_has_next>,</#if>
</#list>
    },
    checkReadValue: function (value) {
        var v = ${bean.js.namespace}.${bean.js.name}.${lowerCamelCase(bean.js.name)}Values[value];
        if (v != null) {
            return v;
        } else {
            return -1;
        }
    },
    valueToStr: function (value) {
        var v = ${bean.js.namespace}.${bean.js.name}.${lowerCamelCase(bean.js.name)}Str[value];
        if (v != null) {
            return v;
        } else {
            return "UNKNOWN";
       }
    }
};
