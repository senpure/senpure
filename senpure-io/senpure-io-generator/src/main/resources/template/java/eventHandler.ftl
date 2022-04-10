package ${javaHandlerPackage};

import ${javaPackage}.${javaName};
import com.senpure.io.event.handler.AbstractEventHandler;
import org.springframework.stereotype.Component;

/**<#if hasExplain>
 * ${explain} 事件处理器
 *</#if>
${sovereignty}
 * @time ${.now?datetime}
 */
@Component
public class ${javaHandlerName} extends AbstractEventHandler<${javaName}> {

    @Override
    public void execute(${javaName} event) {
        //TODO 请在这里写下你处理${name}Event的代码，并删除该条注释

    }

    @Override
    public int eventId() {
                //${.now?datetime} ${id?c}
        return ${javaName}.EVENT_ID;
    }

    @Override
    public ${javaName} newEmptyEvent() {
        return new ${javaName}();
    }
}