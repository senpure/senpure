<#assign baseFieldTypeList=["int", "long", "sint", "slong", "fixed32", "fixed64", "float", "double","boolean","string","String"] />

<#assign baseFieldTypeMethodSpecialMap = {"int":"var32", "long": "var64", "byte": "var32", "short": "var32"} />
<#function baseFieldType2MethodName fieldType>
    <#local temp>${baseFieldTypeMethodSpecialMap[fieldType]!fieldType}</#local>
    <#return temp?cap_first>
</#function>

<#assign baseFieldTypeDtsSpecialMap = {"string":"string", "String": "String", "boolean": "boolean"} />
<#function baseFieldType2DtsType fieldType>
    <#return baseFieldTypeDtsSpecialMap[fieldType]!"number">
</#function>

<#function field2DtsType field>
<#if field.baseField>
    <#return baseFieldType2DtsType(field.fieldType)>
    <#elseif field.bean.enum>
        <#return "number">
    <#else >
    <#return field.bean.js.name>
</#if>
</#function>