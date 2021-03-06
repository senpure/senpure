package com.senpure.base.criteria;

import com.senpure.base.criterion.Criteria;
import com.senpure.base.model.Menu;

import java.io.Serializable;

/**
 * @author senpure
 * @version 2020-5-22 17:34:33
 */
public class MenuCriteria extends Criteria implements Serializable {
    private static final long serialVersionUID = 32275000L;

    //(主键)
    private Integer id;
    //乐观锁，版本控制
    private Integer version;
    private Integer parentId;
    //table [senpure_menu][column = parent_id] criteriaOrder
    private String parentIdOrder;
    private String text;
    private String icon;
    private String uri;
    private String config;
    private Integer sort;
    private Boolean databaseUpdate;
    //不登录也有的菜单
    private Boolean directView;
    private String i18nKey;
    //table [senpure_menu][column = i18n_key] criteriaOrder
    private String i18nKeyOrder;
    private String description;
    //服务名(多个服务可能共用一个数据库来存放权限)
    private String serverName;
    //table [senpure_menu][column = server_name] criteriaOrder
    private String serverNameOrder;

    public static Menu toMenu(MenuCriteria criteria, Menu menu) {
        menu.setId(criteria.getId());
        menu.setParentId(criteria.getParentId());
        menu.setText(criteria.getText());
        menu.setIcon(criteria.getIcon());
        menu.setUri(criteria.getUri());
        menu.setConfig(criteria.getConfig());
        menu.setSort(criteria.getSort());
        menu.setDatabaseUpdate(criteria.getDatabaseUpdate());
        menu.setDirectView(criteria.getDirectView());
        menu.setI18nKey(criteria.getI18nKey());
        menu.setDescription(criteria.getDescription());
        menu.setServerName(criteria.getServerName());
        menu.setVersion(criteria.getVersion());
        return menu;
    }

    public Menu toMenu() {
        Menu menu = new Menu();
        return toMenu(this, menu);
    }

    /**
     * 将MenuCriteria 的有效值(不为空),赋值给 Menu
     *
     * @return Menu
     */
    public Menu effective(Menu menu) {
        if (getId() != null) {
            menu.setId(getId());
        }
        if (getParentId() != null) {
            menu.setParentId(getParentId());
        }
        if (getText() != null) {
            menu.setText(getText());
        }
        if (getIcon() != null) {
            menu.setIcon(getIcon());
        }
        if (getUri() != null) {
            menu.setUri(getUri());
        }
        if (getConfig() != null) {
            menu.setConfig(getConfig());
        }
        if (getSort() != null) {
            menu.setSort(getSort());
        }
        if (getDatabaseUpdate() != null) {
            menu.setDatabaseUpdate(getDatabaseUpdate());
        }
        if (getDirectView() != null) {
            menu.setDirectView(getDirectView());
        }
        if (getI18nKey() != null) {
            menu.setI18nKey(getI18nKey());
        }
        if (getDescription() != null) {
            menu.setDescription(getDescription());
        }
        if (getServerName() != null) {
            menu.setServerName(getServerName());
        }
        if (getVersion() != null) {
            menu.setVersion(getVersion());
        }
        return menu;
    }

    @Override
    protected void beforeStr(StringBuilder sb) {
        sb.append("MenuCriteria{");
        if (id != null) {
            sb.append("id=").append(id).append(",");
        }
        if (version != null) {
            sb.append("version=").append(version).append(",");
        }
        if (parentId != null) {
            sb.append("parentId=").append(parentId).append(",");
        }
        if (text != null) {
            sb.append("text=").append(text).append(",");
        }
        if (icon != null) {
            sb.append("icon=").append(icon).append(",");
        }
        if (uri != null) {
            sb.append("uri=").append(uri).append(",");
        }
        if (config != null) {
            sb.append("config=").append(config).append(",");
        }
        if (sort != null) {
            sb.append("sort=").append(sort).append(",");
        }
        if (databaseUpdate != null) {
            sb.append("databaseUpdate=").append(databaseUpdate).append(",");
        }
        if (directView != null) {
            sb.append("directView=").append(directView).append(",");
        }
        if (i18nKey != null) {
            sb.append("i18nKey=").append(i18nKey).append(",");
        }
        if (description != null) {
            sb.append("description=").append(description).append(",");
        }
        if (serverName != null) {
            sb.append("serverName=").append(serverName).append(",");
        }
    }

    /**
     * get (主键)
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * set (主键)
     *
     * @return
     */
    public MenuCriteria setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getParentId() {
        return parentId;
    }


    public MenuCriteria setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    /**
     * get table [senpure_menu][column = parent_id] criteriaOrder
     *
     * @return
     */
    public String getParentIdOrder() {
        return parentIdOrder;
    }

    /**
     * set table [senpure_menu][column = parent_id] criteriaOrder DESC||ASC
     *
     * @return
     */
    public MenuCriteria setParentIdOrder(String parentIdOrder) {
        this.parentIdOrder = parentIdOrder;
        putSort("parent_id", parentIdOrder);
        return this;
    }

    public String getText() {
        return text;
    }


    public MenuCriteria setText(String text) {
        if (text != null && text.trim().length() == 0) {
            this.text = null;
            return this;
        }
        this.text = text;
        return this;
    }

    public String getIcon() {
        return icon;
    }


    public MenuCriteria setIcon(String icon) {
        if (icon != null && icon.trim().length() == 0) {
            this.icon = null;
            return this;
        }
        this.icon = icon;
        return this;
    }

    public String getUri() {
        return uri;
    }


    public MenuCriteria setUri(String uri) {
        if (uri != null && uri.trim().length() == 0) {
            this.uri = null;
            return this;
        }
        this.uri = uri;
        return this;
    }

    public String getConfig() {
        return config;
    }


    public MenuCriteria setConfig(String config) {
        if (config != null && config.trim().length() == 0) {
            this.config = null;
            return this;
        }
        this.config = config;
        return this;
    }

    public Integer getSort() {
        return sort;
    }


    public MenuCriteria setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public Boolean getDatabaseUpdate() {
        return databaseUpdate;
    }


    public MenuCriteria setDatabaseUpdate(Boolean databaseUpdate) {
        this.databaseUpdate = databaseUpdate;
        return this;
    }

    /**
     * get 不登录也有的菜单
     *
     * @return
     */
    public Boolean getDirectView() {
        return directView;
    }

    /**
     * set 不登录也有的菜单
     *
     * @return
     */
    public MenuCriteria setDirectView(Boolean directView) {
        this.directView = directView;
        return this;
    }

    public String getI18nKey() {
        return i18nKey;
    }


    public MenuCriteria setI18nKey(String i18nKey) {
        if (i18nKey != null && i18nKey.trim().length() == 0) {
            this.i18nKey = null;
            return this;
        }
        this.i18nKey = i18nKey;
        return this;
    }

    /**
     * get table [senpure_menu][column = i18n_key] criteriaOrder
     *
     * @return
     */
    public String getI18nKeyOrder() {
        return i18nKeyOrder;
    }

    /**
     * set table [senpure_menu][column = i18n_key] criteriaOrder DESC||ASC
     *
     * @return
     */
    public MenuCriteria setI18nKeyOrder(String i18nKeyOrder) {
        this.i18nKeyOrder = i18nKeyOrder;
        putSort("i18n_key", i18nKeyOrder);
        return this;
    }

    public String getDescription() {
        return description;
    }


    public MenuCriteria setDescription(String description) {
        if (description != null && description.trim().length() == 0) {
            this.description = null;
            return this;
        }
        this.description = description;
        return this;
    }

    /**
     * get 服务名(多个服务可能共用一个数据库来存放权限)
     *
     * @return
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * set 服务名(多个服务可能共用一个数据库来存放权限)
     *
     * @return
     */
    public MenuCriteria setServerName(String serverName) {
        if (serverName != null && serverName.trim().length() == 0) {
            this.serverName = null;
            return this;
        }
        this.serverName = serverName;
        return this;
    }

    /**
     * get table [senpure_menu][column = server_name] criteriaOrder
     *
     * @return
     */
    public String getServerNameOrder() {
        return serverNameOrder;
    }

    /**
     * set table [senpure_menu][column = server_name] criteriaOrder DESC||ASC
     *
     * @return
     */
    public MenuCriteria setServerNameOrder(String serverNameOrder) {
        this.serverNameOrder = serverNameOrder;
        putSort("server_name", serverNameOrder);
        return this;
    }

    /**
     * get 乐观锁，版本控制
     *
     * @return
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * set 乐观锁，版本控制
     *
     * @return
     */
    public MenuCriteria setVersion(Integer version) {
        this.version = version;
        return this;
    }

}