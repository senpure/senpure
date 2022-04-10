--[[
${sovereignty}
version   ${.now?datetime}
--]]

<#list enums as bean>
    <#include "enumField.ftl">
</#list>