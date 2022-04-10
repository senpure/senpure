ConsumerManager.regMessageDecoder(${bean.lua.namespace}.${bean.lua.name}.messageId,
        {
            getEmptyMessage = function()
                return ${bean.lua.namespace}.${bean.lua.name}:new();
            end
        },"${bean.lua.namespace}.${bean.lua.name}");
