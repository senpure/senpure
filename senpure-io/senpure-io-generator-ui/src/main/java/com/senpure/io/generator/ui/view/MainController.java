package com.senpure.io.generator.ui.view;

import com.senpure.base.AppEvn;
import com.senpure.io.generator.Constant;
import com.senpure.io.generator.executor.CheckException;
import com.senpure.io.generator.executor.Executor;
import com.senpure.io.generator.executor.ExecutorContext;
import com.senpure.io.generator.habit.*;
import com.senpure.io.generator.model.Bean;
import com.senpure.io.generator.model.Enum;
import com.senpure.io.generator.model.Event;
import com.senpure.io.generator.model.Message;
import com.senpure.io.generator.reader.IoProtocolReader;
import com.senpure.io.generator.reader.IoReader;
import com.senpure.io.generator.ui.UiContext;
import com.senpure.io.generator.ui.appender.TextAreaAppender;
import com.senpure.io.generator.ui.model.FileConverter;
import com.senpure.io.generator.ui.model.FileData;
import com.senpure.io.generator.ui.model.ProtocolData;
import com.senpure.io.generator.util.CheckUtil;
import com.senpure.io.generator.util.NoteUtil;
import com.senpure.io.generator.util.TemplateUtil;
import com.senpure.javafx.Controller;
import com.senpure.javafx.JavafxView;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * MainController
 * 以后有机会在分模块吧.
 *
 * @author senpure
 * @time 2019-07-05 14:19:11
 */
@Controller
public class MainController implements Initializable , JavafxView {


    private Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    private ChoiceBox<String> projectName;

    @FXML
    private TableView<FileData> tableViewFileView;

    @FXML
    private TableColumn<FileData, String> tableName;
    @FXML
    private TableColumn<FileData, String> tablePath;

    @FXML
    private TableView<ProtocolData> tableViewProtocolView;

    @FXML
    private TableColumn<ProtocolData, String> tableProtocolName;
    @FXML
    private TableColumn<ProtocolData, Boolean> tableProtocolCheckBok;
    @FXML
    private TableColumn<ProtocolData, String> tableProtocolType;
    @FXML
    private TableColumn<ProtocolData, String> tableProtocolExplain;

    @FXML
    private TabPane tabPaneConfig;

    @FXML
    private Accordion accordionMessage;
    @FXML
    private TitledPane titledPaneProtocolConfig;
    @FXML
    private TitledPane titledPaneProtocolView;
    @FXML
    private TextArea textAreaLog;
    @FXML
    private Button btnDelRecord;

    //java--↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @FXML
    private TextField textFieldJavaProtocolCodeRootPath;
    @FXML
    private TextField textFieldJavaEventHandlerCodeRootPath;
    @FXML
    private TextField textFieldJavaCSMessageHandlerCodeRootPath;
    @FXML
    private TextField textFieldJavaSCMessageHandlerCodeRootPath;

    @FXML
    private ChoiceBox<File> choiceJavaCSMessageHandler;
    @FXML
    private ChoiceBox<File> choiceJavaSCMessageHandler;
    @FXML
    private ChoiceBox<File> choiceJavaMessage;
    @FXML
    private ChoiceBox<File> choiceJavaBean;
    @FXML
    private ChoiceBox<File> choiceJavaEnum;
    @FXML
    private ChoiceBox<File> choiceJavaEvent;
    @FXML
    private ChoiceBox<File> choiceJavaEventHandler;


    @FXML
    private CheckBox checkJavaEnum;
    @FXML
    private CheckBox checkJavaBean;
    @FXML
    private CheckBox checkJavaMessage;
    @FXML
    private CheckBox checkJavaCSMessageHandler;
    @FXML
    private CheckBox checkJavaSCMessageHandler;
    @FXML
    private CheckBox checkJavaCSMessageHandlerOverwrite;

    @FXML
    private CheckBox checkJavaSCMessageHandlerOverwrite;

    @FXML
    private CheckBox checkJavaEvent;
    @FXML
    private CheckBox checkJavaEventHandler;

    @FXML
    private CheckBox checkJavaEventHandlerOverwrite;

    //java--↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    //lua--↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @FXML
    private TextField textFieldLuaProtocolCodeRootPath;

    @FXML
    private TextField textFieldLuaSCMessageHandlerCodeRootPath;

    @FXML
    private ChoiceBox<File> choiceLuaSCMessageHandler;
    @FXML
    private ChoiceBox<String> choiceLuaType;
    @FXML
    private ChoiceBox<File> choiceLuaRequire;
    @FXML
    private ChoiceBox<File> choiceLuaProtocol;

    @FXML
    private CheckBox checkLuaRequire;
    @FXML
    private CheckBox checkLuaProtocol;
    @FXML
    private CheckBox checkLuaSCMessageHandler;

    @FXML
    private CheckBox checkLuaRequireOverwrite;
    @FXML
    private CheckBox checkLuaSCMessageHandlerOverwrite;
    @FXML
    private CheckBox checkLuaAppendNamespace;
    //lua--↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    //js--↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    @FXML
    private TextField textFieldJsProtocolCodeRootPath;

    @FXML
    private TextField textFieldJsSCMessageHandlerCodeRootPath;

    @FXML
    private TextField textFieldJsDtsCodeRootPath;


    @FXML
    private ChoiceBox<File> choiceJsSCMessageHandler;
    @FXML
    private ChoiceBox<String> choiceJsType;
    @FXML
    private ChoiceBox<File> choiceJsRequire;
    @FXML
    private ChoiceBox<File> choiceJsProtocol;

    @FXML
    private ChoiceBox<File> choiceJsDts;

    @FXML
    private CheckBox checkJsRequire;
    @FXML
    private CheckBox checkJsProtocol;
    @FXML
    private CheckBox checkJsDts;
    @FXML
    private CheckBox checkJsSCMessageHandler;

    @FXML
    private CheckBox checkJsRequireOverwrite;
    @FXML
    private CheckBox checkJsSCMessageHandlerOverwrite;
    @FXML
    private CheckBox checkJsAppendNamespace;
    //js--↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    private FileChooser fileChooserIoFile;
    private DirectoryChooser directoryChooser;
    // private DirectoryChooser directoryChooserJavaBeanCodeRootPath;
    // private DirectoryChooser directoryChooserJavaEventHandlerCodeRootPath;
    // private DirectoryChooser directoryChooserJavaCSMessageHandlerCodeRootPath;
    //  private DirectoryChooser directoryChooserJavaSCMessageHandlerCodeRootPath;
    private ResourceBundle resources;
    private Habit habit;
    private JavaConfig javaConfig;
    private LuaConfig luaConfig;
    private JavaScriptConfig jsConfig;
    private ProjectConfig config;

    private Set<File> protocolFiles = new HashSet<>();

    private List<Enum> enums = new ArrayList<>();
    private List<Bean> beans = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    private boolean allChoose = true;
    private volatile boolean usePreView = false;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TextAreaAppender.setTextArea(textAreaLog);
        this.resources = resources;
        configValue();
        initTableView();
        initChooser();
        intProjectName();
        loadValueByConfig();
        projectName.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) -> switchProject(newValue));
    }


    private void configValue() {
        habit = HabitUtil.getHabit();
        config = HabitUtil.getUseConfig();
        javaConfig = config.getJavaConfig();
        luaConfig = config.getLuaConfig();
        jsConfig = config.getJsConfig();

    }

    private void intProjectName() {
        projectName.getItems().clear();
        for (ProjectConfig habitConfig : habit.getConfigs()) {
            projectName.getItems().add(habitConfig.getProjectName());
            if (habitConfig.getProjectName().equals(config.getProjectName())) {
                projectName.getSelectionModel().select(habitConfig.getProjectName());
            }
        }

    }

    private void loadValueByConfig() {
        initTextFieldValue();
        initTemplate();
        initPlane();
        initProtocolFiles();
    }

    private void initTableView() {
        tableName.setCellValueFactory(param -> param.getValue().nameProperty());
        tablePath.setCellValueFactory(param -> param.getValue().pathProperty());
        //----
        tableProtocolName.setCellValueFactory(param -> param.getValue().nameProperty());
        tableProtocolCheckBok.setCellFactory(CheckBoxTableCell.forTableColumn(tableProtocolCheckBok));
        tableProtocolCheckBok.setCellValueFactory(param -> param.getValue().generateProperty());
        tableProtocolType.setCellValueFactory(param -> param.getValue().typeProperty());
        tableProtocolExplain.setCellValueFactory(param -> param.getValue().explainProperty());
    }

    private void initTextFieldValue() {
        textFieldJavaProtocolCodeRootPath.setText(javaConfig.getProtocolCodeRootPath());
        textFieldJavaEventHandlerCodeRootPath.setText(javaConfig.getEventHandlerCodeRootPath());
        textFieldJavaCSMessageHandlerCodeRootPath.setText(javaConfig.getCsMessageHandlerCodeRootPath());
        textFieldJavaSCMessageHandlerCodeRootPath.setText(javaConfig.getScMessageHandlerCodeRootPath());

        textFieldLuaProtocolCodeRootPath.setText(luaConfig.getProtocolCodeRootPath());
        textFieldLuaSCMessageHandlerCodeRootPath.setText(luaConfig.getScMessageHandlerCodeRootPath());

        textFieldJsProtocolCodeRootPath.setText(jsConfig.getProtocolCodeRootPath());
        textFieldJsSCMessageHandlerCodeRootPath.setText(jsConfig.getScMessageHandlerCodeRootPath());
        textFieldJsDtsCodeRootPath.setText(jsConfig.getDtsCodeRootPath());
    }

    private void initChooser() {
        fileChooserIoFile = new FileChooser();
        fileChooserIoFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("io", "*.io")
        );

        directoryChooser = new DirectoryChooser();
        // directoryChooserIoDirectory.setInitialDirectory(new File(config.getProtocolDirectoryChooserPath()));


//        directoryChooserJavaEventHandlerCodeRootPath = new DirectoryChooser();
//        directoryChooserJavaEventHandlerCodeRootPath.setInitialDirectory(new File(javaConfig.getJavaEventHandlerCodeRootChooserPath()));
//        directoryChooserJavaBeanCodeRootPath = new DirectoryChooser();
//        directoryChooserJavaBeanCodeRootPath.setInitialDirectory(new File(javaConfig.getJavaProtocolCodeRootChooserPath()));

    }

    private boolean initTemplate = false;

    private void initTemplate() {
        if (initTemplate) {
            return;
        }
        initTemplate = true;
        initJavaTemplate();
        initLuaTemplate();
        initJsTemplate();
    }

    private void initJavaTemplate() {
        File javaFolder = new File(TemplateUtil.templateDir(), "java");
        File[] files = javaFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                // logger.info("{} = {}",file.getName(),file.getName().toLowerCase().endsWith("eventHandler.ftl".toLowerCase()));
                if (file.getName().toLowerCase().endsWith("eventHandler.ftl".toLowerCase())) {
                    choiceJavaEventHandler.getItems().add(file);
                    if (file.getName().equals(javaConfig.getEventHandlerTemplate())) {
                        choiceJavaEventHandler.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("handler.ftl")) {
                    choiceJavaCSMessageHandler.getItems().add(file);
                    if (file.getName().equals(javaConfig.getCsMessageHandlerTemplate())) {
                        choiceJavaCSMessageHandler.getSelectionModel().select(file);
                    }
                    choiceJavaSCMessageHandler.getItems().add(file);
                    if (file.getName().equals(javaConfig.getScMessageHandlerTemplate())) {
                        choiceJavaSCMessageHandler.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("bean.ftl")) {
                    choiceJavaBean.getItems().add(file);
                    if (file.getName().equals(javaConfig.getBeanTemplate())) {
                        choiceJavaBean.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("message.ftl")) {
                    choiceJavaMessage.getItems().add(file);
                    if (file.getName().equals(javaConfig.getMessageTemplate())) {
                        choiceJavaMessage.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("event.ftl")) {
                    choiceJavaEvent.getItems().add(file);
                    if (file.getName().equals(javaConfig.getEventTemplate())) {
                        choiceJavaEvent.getSelectionModel().select(file);
                    }

                } else if (file.getName().toLowerCase().endsWith("enum.ftl")) {
                    choiceJavaEnum.getItems().add(file);
                    if (file.getName().equals(javaConfig.getEnumTemplate())) {
                        choiceJavaEnum.getSelectionModel().select(file);
                    }
                }
            }
        }
        FileConverter fileConverter = new FileConverter();
        choiceJavaCSMessageHandler.setConverter(fileConverter);
        choiceJavaSCMessageHandler.setConverter(fileConverter);
        choiceJavaEnum.setConverter(fileConverter);
        choiceJavaBean.setConverter(fileConverter);
        choiceJavaMessage.setConverter(fileConverter);
        choiceJavaEvent.setConverter(fileConverter);
        choiceJavaEventHandler.setConverter(fileConverter);

        checkJavaEnum.setSelected(javaConfig.isGenerateEnum());
        checkJavaBean.setSelected(javaConfig.isGenerateBean());
        checkJavaMessage.setSelected(javaConfig.isGenerateMessage());
        checkJavaEvent.setSelected(javaConfig.isGenerateEvent());
        checkJavaCSMessageHandler.setSelected(javaConfig.isGenerateCSMessageHandler());
        checkJavaSCMessageHandler.setSelected(javaConfig.isGenerateSCMessageHandler());
        checkJavaEventHandler.setSelected(javaConfig.isGenerateEventHandler());

        //覆盖操作不读取
        // checkJavaCSMessageHandlerOverwrite.setSelected(javaConfig.isJavaCSMessageHandlerOverwrite());
        // checkJavaSCMessageHandlerOverwrite.setSelected(javaConfig.isJavaSCMessageHandlerOverwrite());
        // checkJavaEventHandlerOverwrite.setSelected(javaConfig.isJavaEventHandlerOverwrite());
    }

    private void initLuaTemplate() {
        File luaFolder = new File(TemplateUtil.templateDir(), "lua");
        File[] files = luaFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith("handler.ftl".toLowerCase())) {
                    choiceLuaSCMessageHandler.getItems().add(file);
                    if (file.getName().equals(luaConfig.getScMessageHandlerTemplate())) {
                        choiceLuaSCMessageHandler.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("require.ftl".toLowerCase())) {
                    choiceLuaRequire.getItems().add(file);
                    if (file.getName().equals(luaConfig.getRequireTemplate())) {
                        choiceLuaRequire.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("protocol.ftl".toLowerCase())) {
                    choiceLuaProtocol.getItems().add(file);
                    if (file.getName().equals(luaConfig.getProtocolTemplate())) {
                        choiceLuaProtocol.getSelectionModel().select(file);
                    }
                }
            }
        }
        checkLuaAppendNamespace.setSelected(luaConfig.isAppendNamespace());

        choiceLuaType.getItems().add(LuaConfig.TYPE_MIX);
        choiceLuaType.getItems().add(LuaConfig.TYPE_FILE);
        choiceLuaType.getItems().add(LuaConfig.TYPE_NAMESPACE);
        choiceLuaType.getSelectionModel().select(luaConfig.getType());

        FileConverter fileConverter = new FileConverter();
        choiceLuaSCMessageHandler.setConverter(fileConverter);
        choiceLuaRequire.setConverter(fileConverter);
        choiceLuaProtocol.setConverter(fileConverter);


        checkLuaProtocol.setSelected(luaConfig.isGenerateProtocol());
        checkLuaRequire.setSelected(luaConfig.isGenerateRequire());
        checkLuaSCMessageHandler.setSelected(luaConfig.isGenerateSCMessageHandler());


    }

    private void initJsTemplate() {
        File luaFolder = new File(TemplateUtil.templateDir(), "js");
        File[] files = luaFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith("handler.ftl".toLowerCase())) {
                    choiceJsSCMessageHandler.getItems().add(file);
                    if (file.getName().equals(jsConfig.getScMessageHandlerTemplate())) {
                        choiceJsSCMessageHandler.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("require.ftl".toLowerCase())) {
                    choiceJsRequire.getItems().add(file);
                    if (file.getName().equals(jsConfig.getRequireTemplate())) {
                        choiceJsRequire.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("protocol.ftl".toLowerCase())) {
                    choiceJsProtocol.getItems().add(file);
                    if (file.getName().equals(jsConfig.getProtocolTemplate())) {
                        choiceJsProtocol.getSelectionModel().select(file);
                    }
                } else if (file.getName().toLowerCase().endsWith("dts.ftl".toLowerCase())) {
                    choiceJsDts.getItems().add(file);
                    if (file.getName().equals(jsConfig.getDtsTemplate())) {
                        choiceJsDts.getSelectionModel().select(file);
                    }
                }
            }
        }
        checkJsAppendNamespace.setSelected(jsConfig.isAppendNamespace());

        choiceJsType.getItems().add(ScriptLanguageConfig.TYPE_MIX);

        choiceJsType.getItems().add(ScriptLanguageConfig.TYPE_FILE);
        choiceJsType.getItems().add(ScriptLanguageConfig.TYPE_NAMESPACE);

        choiceJsType.getSelectionModel().select(jsConfig.getType());

        FileConverter fileConverter = new FileConverter();
        choiceJsSCMessageHandler.setConverter(fileConverter);
        choiceJsProtocol.setConverter(fileConverter);
        choiceJsRequire.setConverter(fileConverter);
        choiceJsDts.setConverter(fileConverter);


        checkJsProtocol.setSelected(jsConfig.isGenerateProtocol());
        checkJsDts.setSelected(jsConfig.isGenerateDts());
        checkJsRequire.setSelected(jsConfig.isGenerateRequire());
        checkJsSCMessageHandler.setSelected(jsConfig.isGenerateSCMessageHandler());


    }

    private void initPlane() {
        accordionMessage.setExpandedPane(titledPaneProtocolConfig);
        //accordionMessage.setExpandedPane(titledPaneProtocolView);
        tabPaneConfig.getSelectionModel().select(config.getTabPaneConfigIndex());
    }

    private void initProtocolFiles() {
        protocolFiles.clear();
        tableViewFileView.getItems().clear();
        if (config.getProtocolFiles() != null) {
            for (ProtocolFile protocolFile : config.getProtocolFiles()) {
                File file = new File(protocolFile.getPath());
                if (file.getName().endsWith(".io")) {
                    if (file.exists()) {
                        addFileToView(file);
                    }
                }
            }
        }
        protocolViewClear();
    }

    private File checkFile(String path) {
        if (path == null) {
            return FileSystemView.getFileSystemView().getHomeDirectory();
        }
        return new File(path);
    }

    public void addProtocolFile() {
        logger.debug("增加协议文件");
        File temp = checkFile(config.getProtocolFileChooserPath());
        if (temp.exists()) {
            fileChooserIoFile.setInitialDirectory(temp);
        } else {
            fileChooserIoFile.setInitialDirectory(null);
        }

        List<File> files = fileChooserIoFile.showOpenMultipleDialog(UiContext.getPrimaryStage());
        if (files != null) {
            fileChooserIoFile.setInitialDirectory(files.get(0).getParentFile());
            config.setProtocolFileChooserPath(files.get(0).getParentFile().toString());

            files.forEach(file ->
                    {
                        logger.debug("文件全路径:{}", file.getAbsolutePath());
                        addFileToView(file);
                    }

            );
            //logger.debug("文件全路径:{}",file.getAbsolutePath());
        } else {
            logger.debug("没有选择任何文件");
        }

    }

    public void addProtocolDirectory() {
        logger.debug("增加协议文件夹");
        File temp = checkFile(config.getProtocolDirectoryChooserPath());
        if (temp.exists()) {
            directoryChooser.setInitialDirectory(temp);
        } else {
            directoryChooser.setInitialDirectory(null);
        }
        File file = directoryChooser.showDialog(UiContext.getPrimaryStage());
        if (file != null) {
            config.setProtocolDirectoryChooserPath(file.getParent());
            int count = addDirectoryToView(file);
            if (count == 0) {
                logger.debug("没有符合条件的文件");
            }
        } else {
            logger.debug("没有选择任何文件");
        }

    }

    private int addDirectoryToView(File file) {
        int count = 0;
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    count += addDirectoryToView(f);
                }
                if (f.getName().endsWith(".io")) {
                    count++;
                    logger.debug("文件全路径:{}", f.getAbsolutePath());
                    addFileToView(f);
                }
            }
        }
        return count;
    }

    private void addFileToView(File file) {
        if (protocolFiles.add(file)) {
            FileData fileData = new FileData(file);
            tableViewFileView.getItems().add(fileData);
        } else {
            logger.warn("已经存在{}->{}", file.getName(), file.getAbsolutePath());
        }
    }

    public void removeFile() {
        int index = tableViewFileView.getSelectionModel().getFocusedIndex();

        if (index > -1) {
            FileData fileData = tableViewFileView.getItems().remove(index);
            protocolFiles.remove(fileData.getFile());

            logger.debug("移除 {} -> {}", fileData.getName(), fileData.getPath());
            if (tableViewProtocolView.getItems().size() > 0) {
                logger.info("清除协议预览");
                protocolViewClear();
            }
        } else {
            logger.warn("没有选择任何文件");
        }
    }

    public void removeAllFile() {
        tableViewFileView.getItems().clear();
        protocolFiles.clear();
    }


    private void protocolViewClear() {
        tableViewProtocolView.getItems().clear();
        enums.clear();
        beans.clear();
        messages.clear();
        events.clear();
    }

    public void protocolView() {

        if (protocolFiles.size() == 0) {
            logger.warn("没有选择协议文件");
            return;
        }
        logger.info("正在准备数据.....");
        executorService.execute(() -> {
            protocolViewClear();
            IoReader.getInstance().clear();
            List<ProtocolData> protocolDatas = new ArrayList<>();
            boolean error = false;
            StringBuilder errorBuilder = new StringBuilder();
            for (File file : protocolFiles) {
                IoProtocolReader ioProtocolReader;
                try {
                    ioProtocolReader = IoReader.getInstance().read(file);
                } catch (Exception e) {
                    // logger.error(e.getMessage());
                    if (errorBuilder.length() > 0) {
                        errorBuilder.append("\n");
                    }
                    errorBuilder.append(e.getMessage());
                    error = true;
                    continue;
                }
                if (ioProtocolReader.isSyntaxError()) {
                    if (errorBuilder.length() > 0) {
                        errorBuilder.append("\n");
                    }
                    errorBuilder.append(ioProtocolReader.getFilePath()).append(" 语法错误\n").append(ioProtocolReader.getSyntaxErrorMessage());

                    // logger.error("{} 出现语法错误 ", ioProtocolReader.getFilePath());
                    error = true;
                    continue;
                }
                enums.addAll(ioProtocolReader.getEnums());
                beans.addAll(ioProtocolReader.getBeans());
                messages.addAll(ioProtocolReader.getMessages());
                events.addAll(ioProtocolReader.getEvents());
                for (Bean bean : ioProtocolReader.getEnums()) {
                    ProtocolData protocolData = new ProtocolData(bean, "enum");
                    protocolDatas.add(protocolData);
                }
                for (Bean bean : ioProtocolReader.getBeans()) {
                    ProtocolData protocolData = new ProtocolData(bean, "bean");
                    protocolDatas.add(protocolData);
                }
                for (Bean bean : ioProtocolReader.getMessages()) {
                    if (bean.getType().equals(Constant.ENTITY_TYPE_CS_MESSAGE)) {
                        ProtocolData protocolData = new ProtocolData(bean, "csMessage");
                        protocolDatas.add(protocolData);
                    } else {
                        ProtocolData protocolData = new ProtocolData(bean, "scMessage");
                        protocolDatas.add(protocolData);
                    }

                }
                for (Bean bean : ioProtocolReader.getEvents()) {
                    ProtocolData protocolData = new ProtocolData(bean, "event");
                    protocolDatas.add(protocolData);
                }

            }

            if (error) {
                protocolViewClear();
                logger.error("协议错误\n{}", errorBuilder.toString());
                //  logger.error("协议文件语法或格式不对请仔细检查修改");
                return;
            }
            if (protocolDatas.size() == 0) {
                logger.warn("没有可读消息");
                return;
            } else {
                logger.info("数据准备完成!");
            }
            accordionMessage.setExpandedPane(titledPaneProtocolView);
            for (ProtocolData protocolData : protocolDatas) {
                if (tableViewProtocolView.getItems().contains(protocolData)) {
                    protocolData.getBean().setGenerate(false);
                } else {
                    tableViewProtocolView.getItems().add(protocolData);
                }
            }
            usePreView = true;
        });


    }

    public void tableViewProtocolViewClick() {

        ProtocolData protocolData =
                tableViewProtocolView.getSelectionModel().getSelectedItem();
        if (protocolData != null) {
            protocolData.setGenerate(!protocolData.isGenerate());
        }
    }

    public void chooseBeanAndEnum() {
        logger.info("选择上bean/enum");
        for (ProtocolData item : tableViewProtocolView.getItems()) {
            if ("bean".equals(item.getType()) || "enum".equals(item.getType())) {
                item.setGenerate(true);
            }
        }
    }

    public void chooseCSMessage() {
        logger.info("选择上csMessage");
        for (ProtocolData item : tableViewProtocolView.getItems()) {
            if ("csMessage".equals(item.getType())) {
                item.setGenerate(true);
            }
        }
    }

    public void chooseSCMessage() {
        logger.info("选择上scMessage");
        for (ProtocolData item : tableViewProtocolView.getItems()) {
            if ("scMessage".equals(item.getType())) {
                item.setGenerate(true);
            }
        }
    }

    public void chooseEvent() {
        logger.info("选择上event");
        for (ProtocolData item : tableViewProtocolView.getItems()) {
            if ("event".equals(item.getType())) {
                item.setGenerate(true);
            }
        }
    }

    public void chooseAll() {
        allChoose = !allChoose;
        logger.debug(allChoose ? "选择所有" : "取消所有");
        tableViewProtocolView.getItems().forEach(item -> item.setGenerate(allChoose));
    }

    public void expandProtocolConfig() {
        accordionMessage.setExpandedPane(titledPaneProtocolConfig);
    }

    private void choiceProtocolCodeRootPath(AbstractLanguageConfig config, TextField textField) {
        File temp = checkFile(config.getProtocolCodeRootChooserPath());
        if (temp.exists()) {
            directoryChooser.setInitialDirectory(temp);
        } else {
            directoryChooser.setInitialDirectory(null);
        }
        File file = directoryChooser.showDialog(UiContext.getPrimaryStage());
        if (file != null) {
            config.setProtocolCodeRootChooserPath(file.getParent());
            textField.setText(file.getAbsolutePath());
        }
    }

    public void choiceJavaProtocolCodeRootPath() {
        choiceProtocolCodeRootPath(javaConfig, textFieldJavaProtocolCodeRootPath);
    }

    public void choiceJavaCSMessageHandlerCodeRootPath() {
        File temp = checkFile(javaConfig.getCsMessageHandlerCodeRootChooserPath());
        if (temp.exists()) {
            directoryChooser.setInitialDirectory(temp);
        } else {
            directoryChooser.setInitialDirectory(null);
        }
        File file = directoryChooser.showDialog(UiContext.getPrimaryStage());
        if (file != null) {
            javaConfig.setCsMessageHandlerCodeRootChooserPath(file.getParent());
            textFieldJavaCSMessageHandlerCodeRootPath.setText(file.getAbsolutePath());
        }
    }

    private void choiceSCMessageHandlerCodeRootPath(AbstractLanguageConfig config, TextField textField) {
        File temp = checkFile(config.getScMessageHandlerCodeRootChooserPath());
        if (temp.exists()) {
            directoryChooser.setInitialDirectory(temp);
        } else {
            directoryChooser.setInitialDirectory(null);
        }
        File file = directoryChooser.showDialog(UiContext.getPrimaryStage());
        if (file != null) {
            config.setScMessageHandlerCodeRootChooserPath(file.getParent());
            textField.setText(file.getAbsolutePath());
        }
    }

    public void choiceJavaSCMessageHandlerCodeRootPath() {
        choiceSCMessageHandlerCodeRootPath(javaConfig, textFieldJavaSCMessageHandlerCodeRootPath);
    }

    public void choiceJavaEventHandlerCodeRootPath() {
        File temp = checkFile(javaConfig.getEventHandlerCodeRootChooserPath());
        if (temp.exists()) {
            directoryChooser.setInitialDirectory(temp);
        } else {
            directoryChooser.setInitialDirectory(null);
        }
        File file = directoryChooser.showDialog(UiContext.getPrimaryStage());
        if (file != null) {
            javaConfig.setEventHandlerCodeRootChooserPath(file.getParent());
            textFieldJavaEventHandlerCodeRootPath.setText(file.getAbsolutePath());
        }
    }


    public void choiceLuaProtocolCodeRootPath() {

        choiceProtocolCodeRootPath(luaConfig, textFieldLuaProtocolCodeRootPath);
    }


    public void choiceLuaSCMessageHandlerCodeRootPath() {
        choiceSCMessageHandlerCodeRootPath(luaConfig, textFieldLuaSCMessageHandlerCodeRootPath);
    }

    public void choiceJsProtocolCodeRootPath() {
        choiceProtocolCodeRootPath(jsConfig, textFieldJsProtocolCodeRootPath);
    }

    public void choiceJsSCMessageHandlerCodeRootPath() {
        choiceSCMessageHandlerCodeRootPath(jsConfig, textFieldJsSCMessageHandlerCodeRootPath);
    }

    public void choiceJsDtsCodeRootPath() {
        File temp = checkFile(jsConfig.getDtsCodeRootChooserPath());
        if (temp.exists()) {
            directoryChooser.setInitialDirectory(temp);
        } else {
            directoryChooser.setInitialDirectory(null);
        }
        File file = directoryChooser.showDialog(UiContext.getPrimaryStage());
        if (file != null) {
            jsConfig.setDtsCodeRootChooserPath(file.getParent());
            textFieldJsDtsCodeRootPath.setText(file.getAbsolutePath());
        }
    }

    public void updateProjectName() {
        TextInputDialog dialog = new TextInputDialog(projectName.getSelectionModel().getSelectedItem());
        dialog.setTitle("修改操作");
        dialog.setHeaderText("修改项目名 ");
        dialog.setContentText(resources.getString("label.input.project.name"));
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String value = result.get().trim();
            if (checkProjectName(value)) {
                updateProjectName(value);
            }

        } else {
            logger.warn("请输入项目名");
        }
    }

    public void updateProjectNameByChose() {
        File temp = checkFile(projectName.getSelectionModel().getSelectedItem());
        if (temp.exists()) {
            directoryChooser.setInitialDirectory(temp.getParentFile());
        } else {
            directoryChooser.setInitialDirectory(null);
        }
        File file = directoryChooser.showDialog(UiContext.getPrimaryStage());
        if (file != null) {
            if (checkProjectName(file.getName())) {
                updateProjectName(file.getName());
                fileVale(file, config);
                loadValueByConfig();
            }
        }
    }

    private boolean checkProjectName(String value) {
        value = value.trim();
        if (value.length() == 0) {
            logger.warn("请输入项目名");
            return false;
        }
        for (ProjectConfig habitConfig : habit.getConfigs()) {
            if (Objects.equals(habitConfig.getProjectName(), value)) {
                logger.warn("{} 项目名存在,请重新更换一个", value);
                return false;
            }
        }
        return true;
    }

    private void updateProjectName(String value) {
        config.setProjectName(value);
        habit.setUserProject(value);
        String old = projectName.getSelectionModel().getSelectedItem();
        projectName.getItems().remove(old);
        projectName.getItems().add(value);
        projectName.getSelectionModel().select(value);
        File file = CheckUtil.getProjectNameFile(old);
        if (file.exists()) {
            if (file.renameTo(CheckUtil.getProjectNameFile(value))) {
                logger.warn("重命名记录文件失败");
            }
        }

    }

    public void createProjectByChose() {
        File temp = checkFile(projectName.getSelectionModel().getSelectedItem());
        if (temp.exists()) {
            directoryChooser.setInitialDirectory(temp.getParentFile());
        } else {
            directoryChooser.setInitialDirectory(null);
        }
        File file = directoryChooser.showDialog(UiContext.getPrimaryStage());
        if (file != null) {
            ProjectConfig projectConfig = createProject(file.getName());
            if (projectConfig != null) {
                fileVale(file, projectConfig);
                projectName.getItems().add(projectConfig.getProjectName());
                projectName.getSelectionModel().select(projectConfig.getProjectName());
            }
        }
    }

    public void createProjectByCopy() {
        String name = getUsableProjectName(config.getProjectName() + "-copy");
        createProject(name, config -> {
            baseConfigValue(config);
            javaConfigValue(config.getJavaConfig());
            luaConfigValue(config.getLuaConfig());
            jsConfigValue(config.getJsConfig());
            projectName.getItems().add(config.getProjectName());
            projectName.getSelectionModel().select(config.getProjectName());
        });
    }

    private String getUsableProjectName(String name) {
        int value = 0;
        boolean next = true;
        do {
            if (value > 0) {
                name = name + value;
            }
            boolean notFind = true;
            for (ProjectConfig habitConfig : habit.getConfigs()) {
                if (Objects.equals(habitConfig.getProjectName(), name)) {
                    value++;
                    notFind = false;
                    break;
                }
            }
            if (notFind) {
                next = false;
            }

        } while (next);
        return name;
    }

    public void createProject(String name, Consumer<ProjectConfig> consumer) {
        TextInputDialog dialog = new TextInputDialog(name);
        dialog.setTitle("新建项目");
        dialog.setHeaderText("新建项目 ");
        dialog.setContentText(resources.getString("label.input.project.name"));
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String projectName = result.get().trim();
            if (projectName.length() > 0) {
                ProjectConfig projectConfig = createProject(projectName);
                if (projectConfig != null) {
                    consumer.accept(projectConfig);
                }
            } else {
                logger.warn("请输入项目名");
            }

        } else {
            logger.warn("请输入项目名");
        }
    }

    public void createProject() {
        String name = getUsableProjectName("myProject");
        createProject(name, config -> {
            projectName.getItems().add(config.getProjectName());
            projectName.getSelectionModel().select(config.getProjectName());
        });
    }

    private ProjectConfig createProject(String name) {
        for (ProjectConfig habitConfig : habit.getConfigs()) {
            if (Objects.equals(habitConfig.getProjectName(), name)) {
                logger.warn("{} 项目名存在,请重新更换一个", name);
                return null;
            }
        }
        ProjectConfig projectConfig = new ProjectConfig();
        HabitUtil.configInitValue(projectConfig);

        projectConfig.setProjectName(name);
        habit.getConfigs().add(projectConfig);
        return projectConfig;
    }

    public void deleteProject() {
        if (habit.getConfigs().size() <= 1) {
            logger.warn("至少需要保留一个项目,请使用修改功能");
            return;
        }

        boolean remove = false;
        String value = null;
        for (int i = 0; i < habit.getConfigs().size(); i++) {
            ProjectConfig projectConfig = habit.getConfigs().get(i);
            if (Objects.equals(projectConfig.getProjectName(), config.getProjectName())) {
                value = config.getProjectName();
                habit.getConfigs().remove(i);
                remove = true;
                break;
            }
        }
        if (remove) {
            habit.setUserProject(habit.getConfigs().get(0).getProjectName());
            projectName.getItems().remove(value);
            projectName.getSelectionModel().select(habit.getUserProject());

        } else {
            logger.error("删除项目失败");
        }

    }


    private void switchProject(String name) {
        //   logger.info("切换项目----------");
        updateConfig();
        habit.setUserProject(name);
        configValue();
        loadValueByConfig();

    }

    private void fileVale(File file, ProjectConfig config) {
        config.setProtocolFileChooserPath(file.getAbsolutePath());
        config.setProtocolDirectoryChooserPath(file.getAbsolutePath());
        config.setProjectName(file.getName());

        JavaConfig javaConfig = config.getJavaConfig();
        File codeFile = new File(file, "src/main/java");
        javaConfig.setProtocolCodeRootPath(codeFile.getAbsolutePath());
        javaConfig.setProtocolCodeRootChooserPath(file.getAbsolutePath());
        javaConfig.setCsMessageHandlerCodeRootPath(codeFile.getAbsolutePath());
        javaConfig.setCsMessageHandlerCodeRootChooserPath(file.getAbsolutePath());
        javaConfig.setScMessageHandlerCodeRootPath(codeFile.getAbsolutePath());
        javaConfig.setScMessageHandlerCodeRootChooserPath(file.getAbsolutePath());
        javaConfig.setEventHandlerCodeRootPath(codeFile.getAbsolutePath());
        javaConfig.setEventHandlerCodeRootChooserPath(file.getAbsolutePath());

        LuaConfig luaConfig = config.getLuaConfig();
        luaConfig.setProtocolCodeRootPath(file.getAbsolutePath());
        luaConfig.setProtocolCodeRootPath(file.getAbsolutePath());
        luaConfig.setScMessageHandlerCodeRootPath(file.getAbsolutePath());
        luaConfig.setScMessageHandlerCodeRootChooserPath(file.getAbsolutePath());


        JavaScriptConfig jsConfig = config.getJsConfig();

        jsConfig.setProtocolCodeRootPath(file.getAbsolutePath());
        jsConfig.setDtsCodeRootPath(new File(file, "@types").getAbsolutePath());
        jsConfig.setProtocolCodeRootPath(file.getAbsolutePath());
        jsConfig.setScMessageHandlerCodeRootPath(file.getAbsolutePath());
        jsConfig.setScMessageHandlerCodeRootChooserPath(file.getAbsolutePath());
    }

    public void clearLog() {
        textAreaLog.clear();
        //  btnDelRecord.setVisible(true);
        // animation(textAreaLog);
        // animation(btnDelRecord);
    }

    public void openLog() {
        File logFile = new File(AppEvn.getClassRootPath(), "logs/generator.log");
        if (!AppEvn.classInJar(getClass())) {
            logFile = new File(System.getProperty("user.dir"), "logs/generator.log");
        }
        if (AppEvn.isWindowsOS()) {

            NoteUtil.openNote(logFile, 12);
        } else {
            logger.debug("日志文件路径{} ", logFile.getAbsoluteFile());
        }
    }


    public void executorContext(Consumer<ExecutorContext> consumer) {

        if (tableViewProtocolView.getItems().size() == 0 || !usePreView) {
            if (protocolFiles.size() == 0) {
                logger.warn("没有选择协议文件");
                return;
            }
            logger.info("正在准备数据......");
            executorService.execute(() -> {
                ExecutorContext executorContext;
                IoReader.getInstance().clear();
                boolean error = false;
                StringBuilder errorBuilder = new StringBuilder();
                List<IoProtocolReader> ioProtocolReaders = new ArrayList<>();
                for (File file : protocolFiles) {
                    IoProtocolReader ioProtocolReader;
                    try {
                        ioProtocolReader = IoReader.getInstance().read(file);
                        ioProtocolReaders.add(ioProtocolReader);
                    } catch (Exception e) {
                        // logger.error(e.getMessage());
                        if (errorBuilder.length() > 0) {
                            errorBuilder.append("\n");
                        }
                        errorBuilder.append(e.getMessage());
                        error = true;
                        continue;
                    }
                    if (ioProtocolReader.isSyntaxError()) {
                        error = true;
                        if (errorBuilder.length() > 0) {
                            errorBuilder.append("\n");
                        }
                        errorBuilder.append(ioProtocolReader.getFilePath()).append(" 语法错误\n").append(ioProtocolReader.getSyntaxErrorMessage());

                        // logger.error("{} 语法错误", ioProtocolReader.getFilePath());
                    }
                }
                if (error) {
                    logger.error("协议错误\n{}", errorBuilder.toString());
                    return;
                }
                executorContext = new ExecutorContext();
                executorContext.setProjectName(config.getProjectName());
                for (IoProtocolReader ioProtocolReader : ioProtocolReaders) {
                    executorContext.getEnums().addAll(ioProtocolReader.getEnums());
                    executorContext.getBeans().addAll(ioProtocolReader.getBeans());
                    executorContext.getMessages().addAll(ioProtocolReader.getMessages());
                    executorContext.getEvents().addAll(ioProtocolReader.getEvents());
                }
                logger.info("数据准备完成!");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                consumer.accept(executorContext);
            });

        } else {
            logger.info("使用预览数据......");
            if (enums.size() + beans.size() + events.size() + messages.size() == 0) {
                logger.warn("未选择任何可生产的对象");
                return;
            }
            //JavaConfig javaConfig = new JavaConfig();
            ExecutorContext executorContext;
            executorContext = new ExecutorContext();
            executorContext.setProjectName(config.getProjectName());
            for (Enum anEnum : enums) {
                if (anEnum.isGenerate()) {
                    executorContext.getEnums().add(anEnum);
                }
            }
            for (Bean bean : beans) {
                if (bean.isGenerate()) {
                    executorContext.getBeans().add(bean);
                }
            }
            for (Message message : messages) {
                if (message.isGenerate()) {
                    executorContext.getMessages().add(message);
                }
            }
            for (Event event : events) {
                if (event.isGenerate()) {
                    executorContext.getEvents().add(event);
                }
            }
            usePreView = false;
            executorService.execute(() -> consumer.accept(executorContext));
        }
    }

    private void generateCode(Executor executor) {
        try {
            executor.generate();
            logger.info("代码生成完成");
        } catch (CheckException e) {
            Platform.runLater(() -> {
                btnDelRecord.setVisible(true);
                btnDelRecord.requestFocus();
                animation(btnDelRecord);

            });
            logger.error("代码检查不通过");
        } catch (Exception e) {
            logger.error("代码生成失败", e);
        }
    }

    public void generateJavaCode() {
        executorContext(executorContext -> {
            javaConfigValue(javaConfig);
            executorContext.addLanguageConfig(javaConfig);
            Executor executor = new Executor(executorContext);
            generateCode(executor);
        });


    }

    public void generateLuaCode() {
        executorContext(executorContext -> {
            luaConfigValue(luaConfig);
            executorContext.addLanguageConfig(luaConfig);
            Executor executor = new Executor(executorContext);
            generateCode(executor);
        });


    }

    public void generateJsCode() {
        executorContext(executorContext -> {
            jsConfigValue(jsConfig);
            executorContext.addLanguageConfig(jsConfig);
            Executor executor = new Executor(executorContext);
            generateCode(executor);
        });


    }

    private void animation(Node node) {
        Path path = new Path();//创建一个路径对象
        double x;
        double y;
        x = 50;
        y = 10;
        //logger.debug("x = {} y ={}",x,y);
        int value = 5;
        path.getElements().add(new MoveTo(x, y));//从哪个位置开始动画，一般来说给组件的默认位置就行
        path.getElements().add(new LineTo(x - value, y + value));//添加一个向左移动的路径
        path.getElements().add(new LineTo(x + value, y + value));//添加一个向右移动的路径  这样就完成第一遍摇头
        path.getElements().add(new LineTo(x - value, y + value));//添加一个向左移动的路径 第二遍
        path.getElements().add(new LineTo(x + value, y + value));//添加一个向右移动的路径  这样就完成第二遍摇头

        path.getElements().add(new LineTo(x, y + value));//最后移动到原来的位置
        PathTransition pathTransition = new PathTransition();//创建一个动画对象
        pathTransition.setDuration(Duration.seconds(1.2));//动画持续时间 0.5秒
        pathTransition.setPath(path);//把我们设置好的动画路径放入里面
        pathTransition.setNode(node);//给动画添加组件，让某个组件来完成这个动画
        pathTransition.setCycleCount(1);//执行1遍
        pathTransition.play();//执行动画
    }

    public void btnDelRecordKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            delRecordFile();
        }
    }

    public void delRecordFile() {
        CheckUtil.closeCheck();
        File file = CheckUtil.getProjectNameFile(config.getProjectName());
        if (file.exists()) {
            if (file.delete()) {
                logger.info("删除记录文件 {}", file.getAbsoluteFile());
            } else {
                logger.info("删除记录文件失败 {}", file.getAbsoluteFile());
            }
        }
        btnDelRecord.setVisible(false);
    }

    public void updateConfig() {
        baseConfigValue(config);
        //java
        javaConfigValue(javaConfig);
        //java
        //lua
        luaConfigValue(luaConfig);
        //lua
        jsConfigValue(jsConfig);
    }

    private void baseConfigValue(ProjectConfig config) {
        config.setTabPaneConfigIndex(tabPaneConfig.getSelectionModel().getSelectedIndex());
        List<ProtocolFile> protocolFiles = new ArrayList<>();
        for (FileData item : tableViewFileView.getItems()) {
            ProtocolFile protocolFile = new ProtocolFile();
            protocolFile.setName(item.getFile().getName());
            protocolFile.setPath(item.getFile().getAbsolutePath());
            protocolFiles.add(protocolFile);
        }
        config.setProtocolFiles(protocolFiles);
    }

    private void javaConfigValue(JavaConfig javaConfig) {
        javaConfig.setEventHandlerCodeRootPath(textFieldJavaEventHandlerCodeRootPath.getText());
        javaConfig.setProtocolCodeRootPath(textFieldJavaProtocolCodeRootPath.getText());
        javaConfig.setCsMessageHandlerCodeRootPath(textFieldJavaCSMessageHandlerCodeRootPath.getText());
        javaConfig.setScMessageHandlerCodeRootPath(textFieldJavaSCMessageHandlerCodeRootPath.getText());

        javaConfig.setEnumTemplate(choiceJavaEnum.getSelectionModel().getSelectedItem().getName());
        javaConfig.setBeanTemplate(choiceJavaBean.getSelectionModel().getSelectedItem().getName());
        javaConfig.setMessageTemplate(choiceJavaMessage.getSelectionModel().getSelectedItem().getName());
        javaConfig.setEventTemplate(choiceJavaEvent.getSelectionModel().getSelectedItem().getName());
        javaConfig.setCsMessageHandlerTemplate(choiceJavaCSMessageHandler.getSelectionModel().getSelectedItem().getName());
        javaConfig.setScMessageHandlerTemplate(choiceJavaSCMessageHandler.getSelectionModel().getSelectedItem().getName());
        javaConfig.setEventHandlerTemplate(choiceJavaEventHandler.getSelectionModel().getSelectedItem().getName());

        javaConfig.setEventHandlerOverwrite(checkJavaEventHandlerOverwrite.isSelected());
        javaConfig.setCsMessageHandlerOverwrite(checkJavaCSMessageHandlerOverwrite.isSelected());
        javaConfig.setScMessageHandlerOverwrite(checkJavaSCMessageHandlerOverwrite.isSelected());

        javaConfig.setGenerateBean(checkJavaBean.isSelected());
        javaConfig.setGenerateEnum(checkJavaEnum.isSelected());
        javaConfig.setGenerateEvent(checkJavaEvent.isSelected());
        javaConfig.setGenerateMessage(checkJavaMessage.isSelected());
        javaConfig.setGenerateEventHandler(checkJavaEventHandler.isSelected());
        javaConfig.setGenerateCSMessageHandler(checkJavaCSMessageHandler.isSelected());
        javaConfig.setGenerateSCMessageHandler(checkJavaSCMessageHandler.isSelected());
    }

    private void luaConfigValue(LuaConfig luaConfig) {

        luaConfig.setType(choiceLuaType.getSelectionModel().getSelectedItem());

        luaConfig.setAppendNamespace(checkLuaAppendNamespace.isSelected());

        luaConfig.setProtocolCodeRootPath(textFieldLuaProtocolCodeRootPath.getText());
        luaConfig.setScMessageHandlerCodeRootPath(textFieldLuaSCMessageHandlerCodeRootPath.getText());

        luaConfig.setProtocolTemplate(choiceLuaProtocol.getSelectionModel().getSelectedItem().getName());
        luaConfig.setScMessageHandlerTemplate(choiceLuaSCMessageHandler.getSelectionModel().getSelectedItem().getName());
        luaConfig.setRequireTemplate(choiceLuaRequire.getSelectionModel().getSelectedItem().getName());

        luaConfig.setGenerateProtocol(checkLuaProtocol.isSelected());
        luaConfig.setGenerateSCMessageHandler(checkLuaSCMessageHandler.isSelected());
        luaConfig.setGenerateRequire(checkLuaRequire.isSelected());

        luaConfig.setScMessageHandlerOverwrite(checkLuaSCMessageHandlerOverwrite.isSelected());
        luaConfig.setRequireOverwrite(checkLuaRequireOverwrite.isSelected());
    }

    private void jsConfigValue(JavaScriptConfig jsConfig) {

        jsConfig.setType(choiceJsType.getSelectionModel().getSelectedItem());

        jsConfig.setAppendNamespace(checkJsAppendNamespace.isSelected());

        jsConfig.setProtocolCodeRootPath(textFieldJsProtocolCodeRootPath.getText());
        jsConfig.setDtsCodeRootPath(textFieldJsDtsCodeRootPath.getText());
        jsConfig.setScMessageHandlerCodeRootPath(textFieldJsSCMessageHandlerCodeRootPath.getText());

        jsConfig.setProtocolTemplate(choiceJsProtocol.getSelectionModel().getSelectedItem().getName());
        jsConfig.setDtsTemplate(choiceJsDts.getSelectionModel().getSelectedItem().getName());
        jsConfig.setScMessageHandlerTemplate(choiceJsSCMessageHandler.getSelectionModel().getSelectedItem().getName());
        jsConfig.setRequireTemplate(choiceJsRequire.getSelectionModel().getSelectedItem().getName());


        jsConfig.setGenerateProtocol(checkJsProtocol.isSelected());
        jsConfig.setGenerateDts(checkJsDts.isSelected());
        jsConfig.setGenerateSCMessageHandler(checkJsSCMessageHandler.isSelected());
        jsConfig.setGenerateRequire(checkJsRequire.isSelected());

        jsConfig.setScMessageHandlerOverwrite(checkJsSCMessageHandlerOverwrite.isSelected());
        jsConfig.setRequireOverwrite(checkJsRequireOverwrite.isSelected());
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
