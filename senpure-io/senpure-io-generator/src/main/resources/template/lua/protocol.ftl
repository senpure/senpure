--[[
${sovereignty}
version   ${.now?datetime}
<#list enums as bean>
    <#if bean_index==0>

    </#if>
enum    : ${rightPad(bean.lua.namespace+"."+bean.lua.name,namespaceAndNameMaxLen)}  ${bean.explain!}
</#list>
<#list beans as bean>
    <#if bean_index==0>

    </#if>
bean    : ${rightPad(bean.lua.namespace+"."+bean.lua.name,namespaceAndNameMaxLen)}  ${bean.explain!}
</#list>
<#list messages as bean>
    <#if bean_index==0>

    </#if>
message : ${rightPad(bean.lua.namespace+"."+bean.lua.name,namespaceAndNameMaxLen)}  ${rightPad(bean.id?c,messageIdMaxLen)}  ${bean.explain!}
</#list>
--]]

<#list namespaces as namespace>
${namespace} = ${namespace} or {}
</#list>

<#list enums as bean>
    <#include "enumField.ftl">
</#list>

<#list beans as bean>
    <#include "field.ftl">
</#list>

<#list messages as bean>
    <#include "field.ftl">
</#list>

<#list messages as bean>
    <#if bean.type == "SC">
     <#include "decoder.ftl">
    </#if>
</#list>
