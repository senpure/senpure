package com.senpure.io.generator.ui.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * LuaController
 *
 * @author senpure
 * @time 2019-08-16 09:55:11
 */

public class LuaController implements Initializable {

    private Logger logger = LoggerFactory.getLogger(getClass());

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
    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

}
