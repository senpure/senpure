<#global "int"="Integer"/>
<#include "method.ftl"/>
<#list fields as field>
<#if field.hasExplain>
    //${field.explain}
</#if>
<#if field.list >
    private List<${javaType2ListType(field.javaType)}> ${field.name} = new ArrayList<>(<#if field.capacity gt 0>${field.capacity}</#if>);
<#else>
    <#if !field.baseField&&field.bean.enum>
    private ${field.javaType} ${field.name} = ${field.bean.javaName}.${field.bean.defaultField.name};
        <#else >
    private ${field.javaType} ${field.name};
    </#if>
</#if>
</#list>

    public void copy(${javaName} source) {
<#list fields as field>
    <#if field.list >
        this.${field.name}.clear();
        <#if field.baseField ||field.bean.enum>
            <#if field.bytes>
        copyBytes(source.get${field.name?cap_first}(),this.${field.name});
            <#else>
        this.${field.name}.addAll(source.get${field.name?cap_first}());
            </#if>
        <#else>
        for (${field.javaType} ${lowerCamelCase(field.javaType)} : source.get${field.name?cap_first}()) {
            ${field.javaType} temp${field.javaType} = new ${field.javaType}();
            temp${field.javaType}.copy(${lowerCamelCase(field.javaType)});
        }
        </#if>
    <#else>
        <#if field.baseField ||field.bean.enum>
            <#if field.fieldType="boolean">
        this.${field.name} = source.is${field.name?cap_first}();
            <#elseif field.bytes>
        if (source.get${field.name?cap_first}() != null) {
            this.${field.name} = copyBytes(source.get${field.name?cap_first}());
        } else {
            this.${field.name} = null;
        }
            <#else>
        this.${field.name} = source.get${field.name?cap_first}();
            </#if>
        <#else>
        if (source.get${field.name?cap_first}() != null) {
            ${field.javaType} temp${field.name?cap_first} = new ${field.javaType}();
            temp${field.name?cap_first}.copy(source.get${field.name?cap_first}());
            this.${field.name} = temp${field.name?cap_first};
        } else {
            this.${field.name} = null;
            }
        </#if>
    </#if>
    </#list>
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
<#list fields as field>
    <#if field.hasExplain>
        //${field.explain}
    </#if>
    <#if field.list >
        <#if field.baseField>
            <#if field.listPacked>
        if (${field.name}.size() > 0) {
            writeVar32(buf, ${field.tag});
            writeVar32(buf, ${field.name}SerializedSize);
            for (${javaType2ListType(field.javaType)} value : ${field.name}) {
             write${baseFieldType2MethodName(field.fieldType)}(buf, value);
            }
        }
            <#else >
        for (${javaType2ListType(field.javaType)} value : ${field.name}) {
            write${baseFieldType2MethodName(field.fieldType)}(buf, ${field.tag}, value);
        }
            </#if>
        <#else ><#--bean -->
            <#if field.bean.enum>
        if (${field.name}.size() > 0) {
            writeVar32(buf, ${field.tag});
            writeVar32(buf, ${field.name}SerializedSize);
            for (${field.javaType} value : ${field.name}) {
                writeVar32(buf, value.getValue());
            }
        }
            <#else>
        for (${field.javaType} value : ${field.name}) {
             writeBean(buf, ${field.tag}, value);
        }
            </#if>
        </#if>
    <#else ><#--不是list-->
        <#if field.baseField>
            <#if javaTypeNullAble(field.javaType)>
        if (${field.name} != null) {
            write${baseFieldType2MethodName(field.fieldType)}(buf, ${field.tag}, ${field.name});
        }
            <#else>
        write${baseFieldType2MethodName(field.fieldType)}(buf, ${field.tag}, ${field.name});
            </#if>
        <#else>
        if (${field.name} != null) {
            <#if field.bean.enum>
            writeVar32(buf, ${field.tag}, ${field.name}.getValue());
            <#else>
            writeBean(buf, ${field.tag}, ${field.name});
            </#if>
        }
        </#if>
    </#if>
</#list>
    }

    /**
     * 读取字节缓存
     */
    @Override
    public void read(ByteBuf buf, int endIndex) {
        while (true) {
            int tag = readTag(buf, endIndex);
            switch (tag) {
                case 0://end
                    return;
<#list fields as field>
<#if field.hasExplain>
                //${field.explain}
</#if>
                case ${field.tag}:// ${field.index} << 3 | ${field.wireType}
    <#if field.list>
        <#if field.baseField>
            <#if field.listPacked>
                    int ${field.name}DataSize = readVar32(buf);
                    int receive${field.name?cap_first}DataSize = 0;
                    do {
                        ${field.javaType} temp${field.name?cap_first}Value = read${baseFieldType2MethodName(field.fieldType)}(buf);
                        receive${field.name?cap_first}DataSize += compute${baseFieldType2MethodName(field.fieldType)}Size(temp${field.name?cap_first}Value);
                        ${field.name}.add(temp${field.name?cap_first}Value);
                    }
                    while (receive${field.name?cap_first}DataSize < ${field.name}DataSize);
            <#else>
                    ${field.name}.add(read${baseFieldType2MethodName(field.fieldType)}(buf));
            </#if>
                <#else ><#--bean-->
                    <#if field.bean.enum>
                    int ${field.name}DataSize = readVar32(buf);
                    int receive${field.name?cap_first}DataSize = 0;
                    do {
                        int temp${field.name?cap_first} = readVar32(buf);
                        receive${field.name?cap_first}DataSize += computeVar32Size(temp${field.name?cap_first});
                        ${field.name}.add(${field.javaType}.get${field.bean.javaName}(temp${field.name?cap_first}));
                    }
                    while(receive${field.name?cap_first}DataSize < ${field.name}DataSize );
                    <#else >
                    ${field.javaType} temp${field.name?cap_first}Bean = new ${field.javaType}();
                    readBean(buf,temp${field.name?cap_first}Bean);
                    ${field.name}.add(temp${field.name?cap_first}Bean);
                    </#if>
        </#if><#--bean-->
    <#else><#--不是list-->
        <#if field.baseField>
                    ${field.name} = read${baseFieldType2MethodName(field.fieldType)}(buf);
        <#else>
            <#if field.bean.enum>
                    ${field.name} = ${field.javaType}.get${field.bean.javaName}(readVar32(buf)) ;
            <#else>
                    ${field.name} = new ${field.javaType}();
                    readBean(buf,${field.name});
            </#if>
        </#if>
    </#if>
                    break;
</#list>
                default://skip
                    skip(buf, tag);
                    break;
            }
        }
    }

    private int serializedSize = -1;
<#list fields as field>
    <#if field.list >
        <#if field.baseField >
            <#if field.listPacked>
    private int ${field.name}SerializedSize = 0;
            </#if>
        <#else>
            <#if field.bean.enum>
    private int ${field.name}SerializedSize = 0;
            </#if>
        </#if>
    </#if>
</#list>

    @Override
    public int serializedSize() {
        int size = serializedSize;
        if (size != -1) {
            return size;
        }
        size = 0;
<#list fields as field>
    <#if field.hasExplain>
        //${field.explain}
    </#if>
    <#if field.list>
        <#if field.baseField>
            <#if field.listPacked>
        if (${field.name}.size() > 0 ) {
            int ${field.name}DataSize = 0;
            for (${javaType2ListType(field.javaType)} value : ${field.name}) {
                ${field.name}DataSize += compute${baseFieldType2MethodName(field.fieldType)}Size(value);
            }
            ${field.name}SerializedSize = ${field.name}DataSize;
            //tag size ${field.tag}
            size += ${var32Size(field.tag)};
            size += ${field.name}SerializedSize;
            size += computeVar32Size(${field.name}SerializedSize);
        }
        <#--
        this.${field.name}SerializedSize = compute${baseFieldType2MethodName(field.fieldType)}Size(${field.name});
        //tag size ${field.tag}
        size += computePackedSize(${var32Size(field.tag)}, ${field.name}SerializedSize);
        -->
            <#else >
        for (${javaType2ListType(field.javaType)} value : ${field.name}) {
            //tag size ${field.tag}
            size += compute${baseFieldType2MethodName(field.fieldType)}Size(${var32Size(field.tag)}, value);
        }
            </#if><#--String-->
        <#else ><#--bean-->
            <#if field.bean.enum>
        if (${field.name}.size() > 0 ) {
            int ${field.name}DataSize = 0;
            for (${field.javaType} value : ${field.name}) {
                ${field.name}DataSize += computeVar32Size(value.getValue());
            }
            ${field.name}SerializedSize = ${field.name}DataSize;
            //tag size ${field.tag}
            size += ${var32Size(field.tag)};
            size += ${field.name}SerializedSize;
            size += computeVar32Size(${field.name}SerializedSize);
        }
        <#--this.${field.name}SerializedSize = computeEnumSize(${field.name},value -> computeVar32Size(value.getValue()));
        //tag size ${field.tag}
        size += computePackedSize(${var32Size(field.tag)}, ${field.name}SerializedSize);
        -->
            <#else>
        for (${field.javaType} value : ${field.name}) {
            size += computeBeanSize(${var32Size(field.tag)}, value);
        }
            </#if>
        </#if><#--bean-->
    <#else><#--不是list-->
         <#if field.baseField>
             <#if javaTypeNullAble(field.javaType)>
        if (${field.name} != null) {
             //tag size ${field.tag}
             size += compute${baseFieldType2MethodName(field.fieldType)}Size(${var32Size(field.tag)}, ${field.name});
        }
             <#else>
        //tag size ${field.tag}
        size += compute${baseFieldType2MethodName(field.fieldType)}Size(${var32Size(field.tag)}, ${field.name});
             </#if>
         <#else>
        if (${field.name} != null) {
             //tag size ${field.tag}
             <#if field.bean.enum>
            size += computeVar32Size(${var32Size(field.tag)}, ${field.name}.getValue());
             <#else >
            size += computeBeanSize(${var32Size(field.tag)}, ${field.name});
             </#if>
        }
         </#if>
    </#if>
</#list>
        serializedSize = size ;
        return size ;
    }

<#list fields as field>
    <#if field.list>
        <#if field.hasExplain&&field.explain?length gt 1>
     /**
      * get ${field.explain}
      *
      * @return
      */
        </#if>
    public List<${javaType2ListType(field.javaType)}> get${field.name?cap_first}() {
        return ${field.name};
    }

        <#if field.hasExplain&&field.explain?length gt 1>
     /**
      * set ${field.explain}
      */
        </#if>
    public ${name} set${field.name?cap_first}(List<${javaType2ListType(field.javaType)}> ${field.name}) {
        if (${field.name} == null) {
            this.${field.name} = new ArrayList<>(<#if field.capacity gt 0>${field.capacity}</#if>);
            return this;
        }
        this.${field.name} = ${field.name};
        return this;
    }
    <#else>
        <#if field.hasExplain&&field.explain?length gt 1>
    /**
     * <#if field.javaType="boolean">is<#else>get</#if> ${field.explain}
     *
     * @return
     */
        </#if>
    public ${field.javaType} <#if field.javaType="boolean">is<#else>get</#if>${field.name?cap_first}() {
        return ${field.name};
    }

        <#if field.hasExplain&&field.explain?length gt 1>
    /**
     * set ${field.explain}
     */
        </#if>
    public ${name} set${field.name?cap_first}(${field.javaType} ${field.name}) {
        this.${field.name} = ${field.name};
        return this;
    }
    </#if>
    <#if field_has_next>

    </#if>
</#list>