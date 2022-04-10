package com.senpure.io.server.protocol.message;

import com.senpure.io.protocol.CompressMessage;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.ArrayList;

/**
 * 框架错误提示
 * 
 * @author senpure
 * @time 2021-6-1 19:22:23
 */
public class SCFrameworkErrorMessage extends CompressMessage {

    public static final int MESSAGE_ID = 500;
    //错误码
    private String code;
    //提示内容
    private String message;
    //参数
    private List<String> args = new ArrayList<>(16);

    public void copy(SCFrameworkErrorMessage source) {
        this.code = source.getCode();
        this.message = source.getMessage();
        this.args.clear();
        this.args.addAll(source.getArgs());
    }

    /**
     * 写入字节缓存
     */
    @Override
    public void write(ByteBuf buf) {
        serializedSize();
        //错误码
        if (code != null) {
            writeString(buf, 11, code);
        }
        //提示内容
        if (message != null) {
            writeString(buf, 19, message);
        }
        //参数
        for (String value : args) {
            writeString(buf, 27, value);
        }
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
                //错误码
                case 11:// 1 << 3 | 3
                    code = readString(buf);
                    break;
                //提示内容
                case 19:// 2 << 3 | 3
                    message = readString(buf);
                    break;
                //参数
                case 27:// 3 << 3 | 3
                    args.add(readString(buf));
                    break;
                default://skip
                    skip(buf, tag);
                    break;
            }
        }
    }

    private int serializedSize = -1;

    @Override
    public int serializedSize() {
        int size = serializedSize;
        if (size != -1) {
            return size;
        }
        size = 0;
        //错误码
        if (code != null) {
             //tag size 11
             size += computeStringSize(1, code);
        }
        //提示内容
        if (message != null) {
             //tag size 19
             size += computeStringSize(1, message);
        }
        //参数
        for (String value : args) {
            //tag size 27
            size += computeStringSize(1, value);
        }
        serializedSize = size ;
        return size ;
    }

    /**
     * get 错误码
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * set 错误码
     */
    public SCFrameworkErrorMessage setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * get 提示内容
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * set 提示内容
     */
    public SCFrameworkErrorMessage setMessage(String message) {
        this.message = message;
        return this;
    }

     /**
      * get 参数
      *
      * @return
      */
    public List<String> getArgs() {
        return args;
    }

     /**
      * set 参数
      */
    public SCFrameworkErrorMessage setArgs(List<String> args) {
        if (args == null) {
            this.args = new ArrayList<>(16);
            return this;
        }
        this.args = args;
        return this;
    }

    @Override
    public int messageType() {
        return MESSAGE_TYPE_SC;
    }

    @Override
    public int messageId() {
        return 500;
    }

    @Override
    public String toString() {
        return "SCFrameworkErrorMessage[500]{"
                + "code=" + code
                + ",message=" + message
                + ",args=" + args
                + "}";
    }

    @Override
    public String toString(String indent) {
        //7 + 3 = 10 个空格
        String nextIndent = "          ";
        //最长字段长度 7
        indent = indent == null ? "" : indent;
        StringBuilder sb = new StringBuilder();
        sb.append("SCFrameworkErrorMessage").append("[500]").append("{");
        //错误码
        sb.append("\n");
        sb.append(indent).append("code    = ").append(code);
        //提示内容
        sb.append("\n");
        sb.append(indent).append("message = ").append(message);
        //参数
        sb.append("\n");
        sb.append(indent).append("args    = ");
        appendValues(sb,args,indent,nextIndent);
        sb.append("\n");
        sb.append(indent).append("}");
        return sb.toString();
    }

}