package com.senpure.io.generator.util;

import com.senpure.base.AppEvn;
import com.senpure.base.util.JSON;
import com.senpure.io.generator.model.Enum;
import com.senpure.io.generator.model.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * 防止失误消息id重复 类名重复等
 *
 * @author senpure
 * @time 2019-06-11 20:52:18
 */
public class CheckUtil {


    private static Logger logger = LoggerFactory.getLogger(CheckUtil.class);
    private static String lastProjectName = "";
    private static RandomAccessFile randomFile;
    private static boolean flush;
    private static File record;
    private static Map<String, CheckObj> nameMap = new LinkedHashMap<>(128);
    private static Map<Integer, CheckObj> eventMap = new HashMap<>(128);
    private static Map<Integer, CheckObj> messageMap = new HashMap<>(128);

    public static String recordPath() {
        return record.getAbsolutePath();
    }


    public static File getProjectNameFile(String projectName) {
        return new File(AppEvn.getClassRootPath(), "record/" + projectName.replace(File.separator, "").replace(":", "") + ".txt");

    }

    public static void closeCheck() {
        lastProjectName = "";
        if (randomFile != null) {
            try {
                randomFile.close();
                randomFile = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkFlush();
        nameMap.clear();
        eventMap.clear();
        messageMap.clear();
    }

    public static void checkFlush() {
        if (flush) {
            List<String> lines = new ArrayList<>();
            for (CheckObj value : nameMap.values()) {
                lines.add(JSON.toJSONString(value));
            }
            try {
                FileUtils.writeLines(record, "utf-8", lines);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        flush = false;
    }

    public static void loadData(String projectName) {
        if (lastProjectName.equalsIgnoreCase(projectName)) {
            return;
        }
        closeCheck();
        lastProjectName = projectName;

        record = getProjectNameFile(projectName);
        if (!record.getParentFile().exists()) {
            record.getParentFile().mkdirs();
        }
        if (record.exists()) {
            try {
                List<String> lines = FileUtils.readLines(record,"utf-8");
                for (String line : lines) {
                    readLine(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void appendObj(CheckObj obj) {
        if (randomFile == null) {
            try {
                randomFile = new RandomAccessFile(record, "rw");
                long length = randomFile.length();
                randomFile.seek(length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            randomFile.write((JSON.toJSONString(obj) + "\n").getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean check(Message bean) {
        String name = bean.getNamespace() + "." + bean.getType() + bean.getName();
        CheckObj obj = messageMap.get(bean.getId());
        if (obj != null) {
            logger.debug("obj.name {}  name {}", obj.name, name);
            if (!name.equals(obj.name)) {
                logger.error("消息id重复 {} {}.{}  {}.{} ", bean.getId(), obj.filePath, obj.name, bean.getFilePath(), name);
                logger.info("可能是更改了消息id,如果确认以前的消息不需要,请删除{} 里面的相关记录", recordPath());
                return false;
            }
        }
        return check(name, bean, "message", bean.getId(), messageMap);
    }

    public static boolean check(Event bean) {
        String name = bean.getNamespace() + "." + bean.getName();
        CheckObj obj = eventMap.get(bean.getId());
        if (obj != null) {
            if (!name.equals(obj.name)) {
                logger.error("事件id重复 {} {}.{}  {}.{} ", bean.getId(), obj.filePath, obj.name, bean.getFilePath(), name);
                logger.info("可能是更改了事件id,如果确认以前的事件不需要,请删除{} 里面的相关记录", recordPath());
                return false;
            }
        }
        return check(name, bean, "event", bean.getId(), eventMap);
    }

    public static boolean check(Enum bean) {
        String name = bean.getNamespace() + "." + bean.getName();

        return check(name, bean, "enum", 0, null);
    }

    public static boolean check(Bean bean) {
        String name = bean.getNamespace() + "." + bean.getName();
        return check(name, bean, "bean", 0, null);
    }

    private static boolean check(String name, Bean bean, String head, int id, Map<Integer, CheckObj> idMap) {
        CheckObj obj = nameMap.get(name);
        if (obj != null) {
            if (!obj.filePath.equals(bean.getFilePath())) {
                logger.error("不同的文件含有相同的命名 {} \n{} \n{}", name, obj.filePath, bean.getFilePath());
                logger.info("可能是更改了命名,如果确认以前的命名不需要,请删除{} 里面的相关记录", recordPath());

                return false;
            }
            for (Field field : bean.getFields()) {
                Integer oldIndex = obj.fieldIndexMap.get(field.getName());
                if (oldIndex != null) {
                    if (oldIndex.intValue() != field.getIndex()) {
                        logger.error("{} {} field index发生了改变 {} -> {} ", name, field.getName(), oldIndex, field.getIndex());
                        logger.info("请注意index的书写,新增字段写在最后或者显示指定index的值\n" +
                                "如果确定更改index,请删除{} 里面的相关记录", recordPath());
                        return false;
                    }
                } else {
                    obj.fieldIndexMap.put(field.getName(), field.getIndex());
                    flush = true;
                }

            }
        } else {
            obj = new CheckObj();
            obj.setHead(head);
            obj.setName(name);
            for (Field field : bean.getFields()) {
                obj.fieldIndexMap.put(field.getName(), field.getIndex());
            }
            if (id > 0) {
                obj.setId(id);
            }
            obj.setFilePath(bean.getFilePath());
            nameMap.put(name, obj);
            if (idMap != null) {
                idMap.put(id, obj);
            }
            if (!flush) {
                appendObj(obj);
            }
        }
        return true;
    }

    public static void readLine(String line) {

        CheckObj obj = JSON.parseObject(line, CheckObj.class);
        String head = obj.head;
        String key = obj.name;
        if (head.equals("bean")) {
            nameMap.put(key, obj);
        } else if (head.equals("enum")) {
            nameMap.put(key, obj);
        } else if (head.equals("message")) {
            nameMap.put(key, obj);

            messageMap.put(obj.id, obj);
        } else if (head.equals("event")) {
            nameMap.put(key, obj);
            eventMap.put(obj.id, obj);
        }
    }


    private static class CheckObj {

        String head;
        Integer id;
        String name;

        String filePath;

        Map<String, Integer> fieldIndexMap = new LinkedHashMap<>();


        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Map<String, Integer> getFieldIndexMap() {
            return fieldIndexMap;
        }

        public void setFieldIndexMap(Map<String, Integer> fieldIndexMap) {
            this.fieldIndexMap = fieldIndexMap;
        }
    }


    public static void main(String[] args) {

        CheckObj obj = new CheckObj();
        obj.setHead("message");
        obj.setName("jkl");

        obj.id = 105;
        obj.setFilePath("ppp");


        System.out.println(JSON.toJSONString(obj));

    }
}
