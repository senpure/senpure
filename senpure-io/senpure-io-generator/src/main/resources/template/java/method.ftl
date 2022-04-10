<#assign baseFieldTypeList=["int", "long", "sint", "slong", "fixed32", "fixed64", "float", "double","boolean","string","String"] />

<#assign baseFieldTypeMethodSpecialMap = {"int":"var32", "long": "var64", "byte": "var32", "short": "var32"} />
<#function baseFieldType2MethodName fieldType>
    <#local temp>${baseFieldTypeMethodSpecialMap[fieldType]!fieldType}</#local>
    <#return temp?cap_first>
</#function>
<#assign javaType2ListTypeSpecialMap = {"int":"Integer","byte []":"byte[]"} />
<#function javaType2ListType javaType>
    <#local temp>${javaType2ListTypeSpecialMap[javaType]!javaType?cap_first}</#local>
    <#return temp>
</#function>
<#assign javaTypeNullAbleSpecialMap = {"String":"true","byte []":"true"} />
<#function javaTypeNullAble javaType>
    <#if javaTypeNullAbleSpecialMap[javaType]??>
        <#return true>
        <#else >
            <#return false>
    </#if>

</#function>


