<#if bean.hasExplain>
    --[[
    ${bean.explain}
    --]]
</#if>
${bean.lua.namespace}.${bean.lua.name} = {
<#list bean.fields as field>
    --[Comment]
    <#if field.hasExplain>
    --${field.explain}
    </#if>
    ${field.name} = ${field.index},
</#list>
    UNKNOWN = -1,
    ${lowerCamelCase(bean.lua.name)}Values = {
<#list bean.fields as field>
                            <#--${bean.lua.namespace}.${bean.lua.name}.${field.name}-->
        [${field.index}] = ${field.index}<#if field_has_next>,</#if>
</#list>
    },
    ${lowerCamelCase(bean.lua.name)}Str = {
<#list bean.fields as field>
        [${field.index}] = "${field.name}"<#if field_has_next>,</#if>
</#list>
    },
    checkReadValue = function(value)
        local v = ${bean.lua.namespace}.${bean.lua.name}.${lowerCamelCase(bean.lua.name)}Values[value]
        if v then
            return v
        else
            return -1;
        end
    end,
    valueToStr = function(value)
        local v = ${bean.lua.namespace}.${bean.lua.name}.${lowerCamelCase(bean.lua.name)}Str[value]
        if v then
            return v
        else
            return "UNKNOWN";
        end
    end
}
