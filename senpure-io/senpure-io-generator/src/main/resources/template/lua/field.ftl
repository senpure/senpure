<#include "method.ftl" >
<#if bean.hasExplain>
--[[
    ${bean.explain}
--]]
</#if>
${bean.lua.namespace}.${bean.lua.name} = {
<#if bean.type !="NA">
    --[Comment]
    --message_id
    messageId = ${bean.id?c};
</#if>
<#list bean.fields as field>
    <#if field.list >
    --[Comment]
    --list:<#if field.baseField>${rightPad(field.javaType,8)}<#else>${field.bean.lua.namespace}.${field.bean.lua.name} </#if><#if field.hasExplain>${field.explain}</#if>
    <#else ><#--不是list-->
    --[Comment]
    --类型:<#if field.baseField>${rightPad(field.javaType,8)}<#else>${field.bean.lua.namespace}.${field.bean.lua.name} </#if><#if field.hasExplain>${field.explain}</#if>
    </#if>
    <#if field.list >
    ${field.name} = nil;
    <#else ><#--不是list-->
        <#if field.baseField>
            <#if field.javaType == "String">
    ${field.name} = "";
            <#elseif field.fieldType == "boolean">
    ${field.name} = false;
            <#else >
    ${field.name} = 0;
            </#if>
        <#else>
            <#if field.bean.enum>
    ${field.name}= ${field.bean.lua.namespace}.${field.bean.lua.name}.${field.bean.defaultField.name};
            <#else >
    ${field.name} = nil ;<#--bean 引用-->
            </#if>
        </#if>
    </#if>
</#list>
    serializedSize = -1;
<#list bean.fields as field>
    <#if field.list >
        <#if field.baseField >
            <#if field.javaType != "String" >
    ${field.name}SerializedSize = 0;
            </#if>
        <#else>
            <#if field.bean.enum>
    ${field.name}SerializedSize = 0;
            </#if>
        </#if>
    </#if>
</#list>
<#if bean.hasNextIndent>
    --缩进${bean.fieldMaxLen} + 3 = ${bean.fieldMaxLen+3} 个空格
    _next_indent = "<#list 1..bean.fieldMaxLen+3 as i> </#list>";
</#if>
    <#--
    --格式化时统一字段长度
    _filedPad = ${bean.fieldMaxLen} ;
    -->
}

--${bean.lua.namespace}.${bean.lua.name}构造方法
function ${bean.lua.namespace}.${bean.lua.name}:new()
<#assign objName>${lowerCamelCase(bean.lua.name)}</#assign>
    local ${objName} = setmetatable({}, {__index=self}) ;
<#list bean.fields as field>
    <#if field.list >
    --[Comment]
    --list:<#if field.baseField>${rightPad(field.javaType,7)}<#else>${field.bean.lua.namespace}${field.bean.lua.name} </#if><#if field.hasExplain>${field.explain}</#if>
    <#else ><#--不是list-->
    --[Comment]
    --类型:<#if field.baseField>${rightPad(field.javaType,7)}<#else>${field.bean.lua.namespace}${field.bean.lua.name} </#if><#if field.hasExplain>${field.explain}</#if>
    </#if>
    <#if field.list >
    ${objName}.${field.name} = nil;
    <#else ><#--不是list-->
        <#if field.baseField>
            <#if field.javaType == "String">
    ${objName}.${field.name} = "";
            <#elseif field.fieldType == "boolean">
    ${objName}.${field.name} = false;
            <#else >
    ${objName}.${field.name} = 0;
            </#if>
        <#else>
            <#if field.bean.enum>
    ${objName}.${field.name} = ${field.bean.lua.namespace}.${field.bean.lua.name}.${field.bean.defaultField.name};
            <#else >
    ${objName}.${field.name} = nil;<#--bean 引用-->
            </#if>
        </#if>
    </#if>
</#list>
    ${objName}.serializedSize = -1;
<#list bean.fields as field>
    <#if field.list >
        <#if field.baseField >
            <#if field.javaType != "String" >
    ${objName}.${field.name}SerializedSize = 0;
            </#if>
        <#else>
            <#if field.bean.enum>
    ${objName}.${field.name}SerializedSize = 0;
            </#if>
        </#if>
    </#if>
</#list>
    return ${objName};
end

--${bean.lua.namespace}.${bean.lua.name}写入字节缓存
function ${bean.lua.namespace}.${bean.lua.name}:write(buf)
    self:getSerializedSize(buf);
<#list bean.fields as field>
    <#if field.hasExplain>
    --${field.explain}
    </#if>
    <#if field.list >
    if self.${field.name} then
        local ${field.name}_len = #self.${field.name};
        <#if field.baseField>
            <#if field.javaType!="String">
        if ${field.name}_len > 0 then
            buf:WriteVar32(${field.tag});
            buf:WriteVar32(self.${field.name}SerializedSize);
            for i = 1, ${field.name}_len do
                buf:Write${baseFieldType2MethodName(field.fieldType)}(self.${field.name}[i]);
            end
        end
            <#else ><#--String-->
        for i = 1,${field.name}_len do
            buf:WriteString(${field.tag}, self.${field.name}[i]);
        end
            </#if>
        <#else ><#--bean -->
            <#if field.bean.enum>
            buf:WriteVar32(${field.tag});
            buf:WriteVar32(self.${field.name}SerializedSize);
            for i = 1,${field.name}_len do
                buf:WriteVar32(self.${field.name}[i]);
            end
            <#else>
        for i = 1,${field.name}_len do
            buf:WriteVar32(${field.tag});
            buf:WriteVar32(self.${field.name}[i]:getSerializedSize(buf));
            self.${field.name}[i]:write(buf);
        end
            </#if>
        </#if>
    end
    <#else ><#--不是list-->
        <#if field.baseField>
            <#if field.javaType="String">
    if self.${field.name} then
        buf:WriteString(${field.tag}, self.${field.name});
    end
                <#else>
    buf:Write${baseFieldType2MethodName(field.fieldType)}(${field.tag}, self.${field.name});
            </#if>
        <#else>
    if self.${field.name} then
            <#if field.bean.enum>
        buf:WriteVar32(${field.tag}, self.${field.name});
            <#else>
        buf:WriteVar32(${field.tag});
        buf:WriteVar32(self.${field.name}:getSerializedSize(buf));
        self.${field.name}:write(buf);
            </#if>
    end
        </#if>
    </#if>
</#list>
end

--${bean.lua.namespace}.${bean.lua.name}读取字节缓存
function ${bean.lua.namespace}.${bean.lua.name}:read(buf,endIndex)
    local switch = {
<#list bean.fields as field>
    <#if field.hasExplain>
        -- ${field.explain}
    </#if>
        -- ${field.index} << 3 | ${field.wireType}
        [${field.tag}] = function ()
    <#if field.list>
        <#if field.baseField>
            <#if field.fieldType!="String">
        local ${field.name}DataSize = buf:ReadVar32();
         self.${field.name} = {};
        local receive${field.name?cap_first}DataSize = 0;
        local read${field.name?cap_first}Index = 1;
        while(receive${field.name?cap_first}DataSize < ${field.name}DataSize ) do
            local temp${field.name?cap_first} = buf:Read${baseFieldType2MethodName(field.fieldType)}();
            receive${field.name?cap_first}DataSize = receive${field.name?cap_first}DataSize + buf:Compute${baseFieldType2MethodName(field.fieldType)}Size(temp${field.name?cap_first});
            self.${field.name}[read${field.name?cap_first}Index] = temp${field.name?cap_first};
            read${field.name?cap_first}Index = read${field.name?cap_first}Index + 1;
        end
            <#else ><#--String 打包方所不同-->
        self.${field.name} = self.${field.name} or {};
        self.read${field.name?cap_first}Index = self.read${field.name?cap_first}Index or 1;
        self.${field.name}[self.read${field.name?cap_first}Index] = buf:ReadString();
        self.read${field.name?cap_first}Index = self.read${field.name?cap_first}Index + 1;
            </#if><#--String-->
        <#else ><#--bean-->
            <#if field.bean.enum>
        local ${field.name}DataSize = buf:ReadVar32();
        self.${field.name} = {};
        local receive${field.name?cap_first}DataSize = 0;
        local read${field.name?cap_first}Index = 1;
        while(receive${field.name?cap_first}DataSize < ${field.name}DataSize ) do
            local temp${field.name?cap_first} = buf:ReadVar32();
            receive${field.name?cap_first}DataSize = receive${field.name?cap_first}DataSize + buf:ComputeVar32Size(temp${field.name?cap_first});
            self.${field.name}[read${field.name?cap_first}Index] = ${field.bean.lua.namespace}.${field.bean.lua.name}.checkReadValue(temp${field.name?cap_first});
            read${field.name?cap_first}Index = read${field.name?cap_first}Index + 1;
        end
                <#else >
        self.${field.name} = self.${field.name} or {};
        self.read${field.name?cap_first}Index = self.read${field.name?cap_first}Index or 1;
        local temp${field.bean.lua.namespace}${field.bean.name} = ${field.bean.lua.namespace}.${field.bean.lua.name}:new()
        temp${field.bean.lua.namespace}${field.bean.lua.name}:read(buf,buf:ReadVar32()+buf:ReaderIndex())
        self.${field.name}[self.read${field.name?cap_first}Index]  = temp${field.bean.lua.namespace}${field.bean.name};
        self.read${field.name?cap_first}Index  = self.read${field.name?cap_first}Index  + 1;
            </#if>
        </#if><#--bean-->
    <#else><#--不是list-->
        <#if field.baseField>
        self.${field.name} = buf:Read${baseFieldType2MethodName(field.fieldType)}();
        <#else>
            <#if field.bean.enum>
        self.${field.name}=${field.bean.lua.namespace}.${field.bean.lua.name}.checkReadValue(buf:ReadVar32());
            <#else >
        self.${field.name} = ${field.bean.lua.namespace}.${field.bean.lua.name}:new()
        self.${field.name}:read(buf,buf:ReadVar32()+buf:ReaderIndex())
            </#if>
        </#if>
    </#if>
    end <#if  field_has_next>,</#if>
</#list>
    }
    while(true) do
        local tag = buf:ReadTag(endIndex);
        if(tag == 0) then
            return;
        else
            local case = switch[tag];
            if(case) then
                case();
            else
                buf:Skip(tag);
            end
        end
    end
end

--${bean.lua.namespace}.${bean.lua.name}计算序列化大小
function ${bean.lua.namespace}.${bean.lua.name}:getSerializedSize(buf)
    local size = self.serializedSize;
    if (size > -1) then
        return size;
    end
    size = 0;
<#list bean.fields as field>
    <#if field.hasExplain>
    -- ${field.explain}
    </#if>
    <#if field.list>
        <#if field.baseField>
            <#if field.javaType!="String">
    if self.${field.name} then
        local ${field.name}_len = #self.${field.name}
        local ${field.name}DataSize = 0;
        if ${field.name}_len > 0 then
            for i = 1, ${field.name}_len do
                ${field.name}DataSize = ${field.name}DataSize + buf:Compute${baseFieldType2MethodName(field.fieldType)}Size(self.${field.name}[i] );
            end
        end
        self.${field.name}SerializedSize = ${field.name}DataSize;
        if ${field.name}DataSize > 0 then
            -- tag size 已经完成了计算 ${field.tag}
            size = size + ${var32Size(field.tag)} + self.${field.name}SerializedSize + buf:ComputeVar32Size(self.${field.name}SerializedSize);
        end
    end
            <#else ><#--String-->
        if self.${field.name} then
            local ${field.name}_len = #self.${field.name}
            if ${field.name}_len > 0 then
            for i = 1, ${field.name}_len do
                -- tag size 已经完成了计算 ${field.tag}
                size = size + ${var32Size(field.tag)} + buf:ComputeStringSize(self.${field.name}[i] );
            end
        end
    end
            </#if><#--String-->
        <#else ><#--bean-->
            <#if field.bean.enum>
    if self.${field.name} then
        local ${field.name}DataSize = 0;
        local ${field.name}_len = #self.${field.name}
        if ${field.name}_len > 0 then
            for i = 1, ${field.name}_len do
                ${field.name}DataSize = ${field.name}DataSize + buf:ComputeVar32Size(self.${field.name}[i] );
            end
        end
        self.${field.name}SerializedSize = ${field.name}DataSize;
        if ${field.name}DataSize > 0 then
            -- tag size 已经完成了计算 ${field.tag}
            size = size + ${var32Size(field.tag)} + self.${field.name}SerializedSize + buf:ComputeVar32Size(self.${field.name}SerializedSize);
        end
    end
                <#else >
    if self.${field.name} then
        local ${field.name}_len = #self.${field.name}
        if ${field.name}_len > 0 then
            for i = 1, ${field.name}_len do
                local ${field.name}BeanSize = self.${field.name}[i]:getSerializedSize(buf)
                -- tag size 已经完成了计算 ${field.tag}
                size = size + ${field.name}BeanSize + buf:ComputeVar32Size(${var32Size(field.tag)},${field.name}BeanSize);
            end
        end
    end
            </#if>
        </#if><#--bean-->
    <#else><#--不是list-->
        <#if field.baseField>
            <#if field.javaType="String">
    if self.${field.name} then
        -- tag size 已经完成了计算 ${field.tag}
        size = size + buf:ComputeStringSize(${var32Size(field.tag)}, self.${field.name});
    end
            <#else>
    -- tag size 已经完成了计算 ${field.tag}
    size = size + buf:Compute${baseFieldType2MethodName(field.fieldType)}Size(${var32Size(field.tag)}, self.${field.name});
            </#if>
        <#else>
            <#if field.bean.enum>
    -- tag size 已经完成了计算 ${field.tag}
    size = size + buf:ComputeVar32Size(${var32Size(field.tag)}, self.${field.name});
            <#else >
    if self.${field.name} then
        local ${field.name}BeanSize = self.${field.name}:getSerializedSize(buf)
        -- tag size 已经完成了计算 ${field.tag}
        size = size + ${field.name}BeanSize + buf:ComputeVar32Size(${var32Size(field.tag)}, ${field.name}BeanSize);
    end
            </#if>
        </#if>
    </#if>
</#list>
    self.serializedSize = size ;
    return size ;
end

--${bean.lua.namespace}.${bean.lua.name}格式化字符串
function ${bean.lua.namespace}.${bean.lua.name}:toString(_indent)
    _indent = _indent or ""
    local _str = ""
    _str = _str .. "${bean.lua.name}<#if bean.type != "NA">[${bean.id?c}]</#if>" .. "{"
<#list bean.fields as field>
    <#if field.hasExplain>
    --${field.explain}
    </#if>
    _str = _str .. "\n"
    <#if field.list>
    _str = _str .. _indent .. "${field.name?right_pad(bean.fieldMaxLen)}" .. " = "
    if self.${field.name} then
        local ${field.name}_len = #self.${field.name}
        if ${field.name}_len > 0 then
            _str = _str .. "["
            for i = 1,${field.name}_len do
                _str = _str .. "\n"
        <#if field.baseField>
                _str = _str .. self._next_indent
            <#if field.fieldType="boolean">
                _str = _str .. _indent .. tostring(self.${field.name}[i])
            <#else >
                _str = _str .. _indent .. self.${field.name}[i]
            </#if>
        <#else>
            <#if field.bean.enum>
                _str = _str .. self._next_indent
                _str = _str .._indent .. ${field.bean.lua.namespace}.${field.bean.lua.name}.valueToStr(self.${field.name}[i])
                <#else >
                _str = _str .. self._next_indent
                _str = _str .. _indent .. self.${field.name}[i]:toString(_indent .. self._next_indent)
            </#if>
        </#if>
            end
            _str = _str .. "\n"
            _str = _str .. self._next_indent
            _str = _str .. _indent .. "]"
        else<#-- len = 0-->
            _str = _str .. "[]"
        end
    else <#-- nil-->
        _str = _str .. "nil"
    end
    <#else ><#-- 不是list-->
        <#if field.baseField>
            <#if field.fieldType="boolean">
    _str = _str .. _indent .. "${field.name?right_pad(bean.fieldMaxLen)} = " .. tostring(self.${field.name})
            <#else >
    _str = _str .. _indent .. "${field.name?right_pad(bean.fieldMaxLen)} = " .. self.${field.name}
            </#if>
        <#else>
            <#if field.bean.enum>
    _str = _str .. _indent .. "${field.name?right_pad(bean.fieldMaxLen)} = " .. ${field.bean.lua.namespace}.${field.bean.lua.name}.valueToStr(self.${field.name})
                <#else >
    if self.${field.name} then
        _str = _str .. _indent .. "${field.name?right_pad(bean.fieldMaxLen)} = " .. self.${field.name}:toString(_indent .. self._next_indent)
    else
        _str = _str .. _indent .. "${field.name?right_pad(bean.fieldMaxLen)} = " .. "nil"
    end
            </#if>
        </#if>
    </#if>
</#list>
    _str =_str .. "\n"
    _str = _str .. _indent .. "}"
    return _str
end

<#list bean.fields as field>
    <#if field.list>
        <#if field.hasExplain&&field.explain?length gt 1>
--get ${field.explain}
        </#if>
function ${bean.lua.namespace}.${bean.lua.name}:get${field.name?cap_first}()
    return self.${field.name};
end
        <#if field.hasExplain&&field.explain?length gt 1>
-- set ${field.explain}
        </#if>
function ${bean.lua.namespace}.${bean.lua.name}:set${field.name?cap_first}(${field.name})
    if ${field.name} == nil then
        self.${field.name} = new Array(0);
        return self;
    end
    this.${field.name} = ${field.name};
    return self;
end
    <#else>
        <#if field.hasExplain&&field.explain?length gt 1>
--<#if field.fieldType="boolean"> is<#else>get</#if> ${field.explain}
        </#if>
function ${bean.lua.namespace}.${bean.lua.name}:<#if field.fieldType="boolean">is<#else>get</#if>${field.name?cap_first}()
    return self.${field.name};
end
        <#if field.hasExplain&&field.explain?length gt 1>
-- set ${field.explain}
        </#if>
function ${bean.lua.namespace}.${bean.lua.name}:set${field.name?cap_first}(${field.name})
    this.${field.name} = ${field.name};
    return self;
end
    </#if>

</#list>

