<#include "method.ftl">
/**
 *
 * ${sovereignty}
 * version   ${.now?datetime}
 */
<#list fileNames as fileName>
require("${fileName}")
</#list>
