package ${javaPackage};

<#list singleField?values as field>
    <#if !field.baseField>
        <#if field.bean.javaPackage!=javaPackage>
import ${field.bean.javaPackage}.${field.bean.javaName};
        </#if>
    </#if>
</#list >
<#if type="CS">
import com.senpure.io.protocol.Message;
    <#else >
</#if>
import io.netty.buffer.ByteBuf;

<#list fields as field>
    <#if field.list>
import java.util.List;
import java.util.ArrayList;

        <#break>
    </#if>
</#list>
/**<#if hasExplain>
 * ${explain}
 * </#if>
${sovereignty}
 * @time ${.now?datetime}
 */
<#assign name>${javaName}</#assign>
public class ${name} extends <#if type="CS">Message<#else>OrderMessage</#if> {

    public static final int MESSAGE_ID = ${id?c};
<#include "compressField.ftl">

    @Override
    public int messageId() {
        return ${id?c};
    }
<#include "toString.ftl">
}