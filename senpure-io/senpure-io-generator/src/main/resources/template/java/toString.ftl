<#include "method.ftl">

    @Override
    public String toString() {
        return "${name}<#if type!="NA">[${id?c}]</#if>{"
<#list fields as field>
                + "<#if field_index gt 0>,</#if>${field.name}=" + <#if field.bytes&&!field.list>bytesToString(${field.name})<#else>${field.name}</#if>
</#list>
                + "}";
    }

    @Override
    public String toString(String indent) {
<#if hasNextIndent>
        //${fieldMaxLen} + 3 = ${fieldMaxLen+3} 个空格
        String nextIndent = "<#list 1..fieldMaxLen+3 as i> </#list>";
</#if>
<#if fields?size gt 0>
        //最长字段长度 ${fieldMaxLen}
</#if>
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("${name}")<#if type!="NA">.append("[${id?c}]")</#if>.append("{");
<#list fields as field>
    <#if field.hasExplain>
        //${field.explain}
    </#if>
    <#if field.list>
        sb.append("\n");
        sb.append(indent).append("${field.name?right_pad(fieldMaxLen)} = ");
        <#--int ${field.name}Size = ${field.name}.size();-->
        <#--
        if (${field.name}.size() > 0) {
            sb.append("[");
            for (${javaType2ListType(field.javaType)} value : ${field.name}) {
                sb.append("\n");
        <#if field.baseField||field.bean.enum>
                sb.append(nextIndent);
                sb.append(indent).append(value);
        <#else>
                sb.append(nextIndent);
                sb.append(indent).append(value.toString(indent + nextIndent));
        </#if>
            }
            sb.append("\n");
            sb.append(nextIndent);
            sb.append(indent).append("]");
        } else {
            sb.append("[]");
        }-->
        <#if field.baseField||field.bean.enum>
        appendValues(sb,${field.name},indent,nextIndent);
        <#else >
        appendBeans(sb,${field.name},indent,nextIndent);
        </#if>
    <#else >
        sb.append("\n");
        <#if field.baseField>
        sb.append(indent).append("${field.name?right_pad(fieldMaxLen)} = ").append(<#if field.bytes>bytesToString(${field.name})<#else>${field.name}</#if>);
        <#else>
        sb.append(indent).append("${field.name?right_pad(fieldMaxLen)} = ");
        if (${field.name} != null){
            <#if field.bean.enum>
            sb.append(${field.name});
                <#else >
            sb.append(${field.name}.toString(indent+nextIndent));
                </#if>
        } else {
            sb.append("null");
        }
        <#--
           <#if field.bean.enum>
       append(sb, ${field.name});
           <#else>
       append(sb, ${field.name}, indent, nextIndent);
           </#if>-->
        </#if>
    </#if>
</#list>
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

