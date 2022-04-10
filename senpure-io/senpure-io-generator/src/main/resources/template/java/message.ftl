package ${javaPackage};

<#list singleField?values as field>
    <#if !field.baseField>
        <#if field.bean.javaPackage!=javaPackage>
import ${field.bean.javaPackage}.${field.bean.javaName};
        </#if>
    </#if>
</#list >
import com.senpure.io.protocol.CompressMessage;
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
public class ${name} extends CompressMessage {

    public static final int MESSAGE_ID = ${id?c};
<#include "compressField.ftl">

    @Override
    public int messageType() {
        return MESSAGE_TYPE_<#if type="CS">CS<#else>SC</#if>;
    }

    @Override
    public int messageId() {
        return ${id?c};
    }
<#include "toString.ftl">
}