package com.senpure.base;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultMap extends HashMap<String, Object> {
    private static final long serialVersionUID = -3076981336664920688L;
    public static final String RESULT_KEY = "code";
    public static final String MESSAGE_KEY = "message";
    public static final String FILE_KEY = "file";
    public static final String FILE_NAME_KEY = "fileName";
    public static final String DELETE_FILE_KEY = "deleteFile";
    public static final String VALIDATORS_KEY = "validators";
    public static final String TOTAL_KEY = "total";
    public static final String ITEMS_KEY = "items";
    public static final String ITEM_KEY = "item";
    public static final String PAGE_KEY = "page";
    public static final String MESSAGE_ARGS_KEY = "messageArgs";

    public static ResultMap success() {
        return new ResultMap(Result.SUCCESS);
    }

    public static ResultMap dim() {
        return new ResultMap(Result.ERROR_DIM);
    }

    public static ResultMap failure() {
        return new ResultMap(Result.FAILURE);
    }

    public static ResultMap notExist() {
        return new ResultMap(Result.TARGET_NOT_EXIST);
    }

    public static ResultMap result(int code) {
        return new ResultMap(code);
    }

    public static ResultMap copy(Map<String, Object> map) {

        ResultMap resultMap = failure();
        resultMap.putAll(map);

        return resultMap;
    }

    private boolean clientFormat;
    private List<Object> args;
    private String itemName = ITEM_KEY;
    private String itemsName = ITEMS_KEY;

    private ResultMap() {
        super();

    }

    public ResultMap(int code) {
        super.put(RESULT_KEY, code);
    }

    /**
     * @param key   <b>key的值会覆盖</b>
     * @param value
     * @return
     */
    @Override
    public ResultMap put(String key, Object value) {

//        if (key.equals(RESULT_KEY)) {
//            Assert.error("错误操作 put key is code ");
//        }
        super.put(key, value);
        return this;
    }


    public ResultMap putTotal(int total) {
        super.put(TOTAL_KEY, total);
        return this;
    }

    public ResultMap putItems(List value) {
        super.put(itemsName, value);
        return this;
    }

    public ResultMap putItems(String itemsName, List value) {
        this.itemsName = itemsName;
        return putItems(value);
    }

    public ResultMap putItem(Object value) {
        super.put(itemName, value);
        return this;
    }

    public ResultMap putItem(String itemName, Object value) {
        this.itemsName = itemName;
        return putItem(value);
    }

    public ResultMap putMessage(String value) {
        super.put(MESSAGE_KEY, value);
        return this;
    }

    public ResultMap addMessageArgs(List<Object> value) {
        if (args == null) {
            args = new ArrayList<>();
        }
        args.addAll(value);
        if (clientFormat) {
            super.put(MESSAGE_ARGS_KEY, args);
        }
        return this;
    }

    public ResultMap addMessageArgs(Object value) {
        if (args == null) {
            args = new ArrayList<>();
        }
        args.add(value);
        if (clientFormat) {
            super.put(MESSAGE_ARGS_KEY, args);
        }
        return this;
    }

    public ResultMap remove(String key) {

        super.remove(key);
        return this;
    }

    public boolean isSuccess() {
        Integer code = (Integer) super.get(RESULT_KEY);
        return code == Result.SUCCESS;
    }

    public int getCode() {
        Integer code = (Integer) super.get(RESULT_KEY);
        return code == null ? Result.FAILURE : code;
    }

    public String getMessage() {

        return (String) super.get(MESSAGE_KEY);
    }

    public boolean isDelete() {
        Boolean d = (Boolean) get(DELETE_FILE_KEY);
        return d == null ? false : d;
    }

    public File getFile() {
        return (File) get(FILE_KEY);

    }

    public String getFileName() {
        String name = (String) get(FILE_NAME_KEY);
        if (name == null) {
            File file = getFile();
            if (file != null) {
                return file.getName();
            }
        }
        return name;
    }

    public long getTotal() {
        Object total = get(TOTAL_KEY);
        return total == null ? 0 : Long.parseLong(total.toString());
    }

    public int getPage() {
        Integer page = (Integer) get(PAGE_KEY);
        return page == null ? 0 : page;
    }

    public int getItemSize() {
        List item = (List) get(ITEMS_KEY);
        if (item == null) {
            return 0;
        }
        return item.size();
    }


    public boolean isClientFormat() {
        return clientFormat;
    }

    public void setClientFormat(boolean clientFormat) {
        this.clientFormat = clientFormat;
    }


    public List<Object> getArgs() {
        return args;
    }

}
