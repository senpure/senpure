package ${javaHandlerPackage};

import ${javaPackage}.${javaName};
import com.senpure.io.server.provider.handler.AbstractProviderMessageHandler;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**<#if hasExplain>
 * ${explain}处理器
 *</#if>
${sovereignty}
 * @time ${.now?datetime}
 */
@Component
public class ${javaHandlerName} extends AbstractProviderMessageHandler<${javaName}> {

    @Override
    public void execute(Channel channel, long token, long userId, ${javaName} message) {
        //TODO 请在这里写下你的代码

    }

    @Override
    public int messageId() {
                //${.now?datetime} ${id?c}
        return ${javaName}.MESSAGE_ID;
    }

    @Override
    public ${javaName} newEmptyMessage() {
        return new ${javaName}();
    }
}