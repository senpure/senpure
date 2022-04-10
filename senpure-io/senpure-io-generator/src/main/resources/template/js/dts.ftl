//${sovereignty}
///<reference path="../@types/io.d.ts"/>
<#include "method.ftl"/>
<#list namespaces as namespace>
declare namespace ${namespace}{
    <#list enums as bean>
        <#if bean.js.namespace = namespace>
    namespace ${bean.js.name}{
            <#list bean.fields  as field>
        const ${field.name}: number;
            </#list>
    }
        </#if>
    </#list>
    <#list beans as bean>
        <#if bean.js.namespace = namespace>
    class ${bean.js.name}  extends io.Bean {

        <#include "dtsField.ftl"/>
    }
        </#if>
    </#list>
    <#list messages as bean>
        <#if bean.js.namespace = namespace>
            <#if bean.type="SC">
    namespace ${bean.js.name}{

        class MessageDecoder extends io.MessageDecoder {

        }
    }
            </#if>
    class ${bean.js.name}  extends io.Message {

        static MESSAGE_ID: number;

       <#include "dtsField.ftl"/>
    }
        </#if>
    </#list>
}
</#list>

