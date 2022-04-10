       /**
        * copy
        */
       copy(from: ${bean.js.name}): void;

<#list bean.fields as field>
    <#if field.list>
        <#if field.hasExplain&&field.explain?length gt 1>
        /**
         * get ${field.explain}
         * @return
         */
        </#if>
        get${field.name?cap_first}():[${baseFieldType2DtsType(field.fieldType)}];

        <#if field.hasExplain&&field.explain?length gt 1>
        /**
         * set ${field.explain}
         * @return ${bean.js.name}
         */
        </#if>
        set${field.name?cap_first}(${field.name}:[${baseFieldType2DtsType(field.fieldType)}]): ${bean.js.name};

        <#else ><#--不是list-->
            <#if field.hasExplain&&field.explain?length gt 1>
        /**
         * <#if field.fieldType="boolean">is<#else>get</#if> ${field.explain}
         * @return
         */
            </#if>
        <#if field.fieldType="boolean">is<#else>get</#if>${field.name?cap_first}():${field2DtsType(field)};

        <#if field.hasExplain&&field.explain?length gt 1>
        /**
         * set ${field.explain}
         * @return ${bean.js.name}
         */
        </#if>
        set${field.name?cap_first}(${field.name}: ${field2DtsType(field)}): ${bean.js.name};

    </#if>
</#list>