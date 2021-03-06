package com.senpure.base.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author senpure
 * @version 2020-5-22 16:52:02
 */
@ApiModel
public class AccountValue implements Serializable {
    private static final long serialVersionUID = 846733638L;

    //(主键)
    private Long id;
    //乐观锁，版本控制
    @ApiModelProperty(hidden = true )
    private Integer version;
    @ApiModelProperty(example = "key", position = 1)
    private String key;
    @ApiModelProperty(example = "value", position = 2)
    private String value;
    @ApiModelProperty(example = "description", position = 3)
    private String description;
    //(外键,modelName:Account,tableName:senpure_account)
    @ApiModelProperty(value = "(外键,modelName:Account,tableName:senpure_account)", dataType = "long", example = "666666", position = 4)
    private Long accountId;

    /**
     * get (主键)
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * set (主键)
     *
     * @return
     */
    public AccountValue setId(Long id) {
        this.id = id;
        return this;
    }


    public String getKey() {
        return key;
    }


    public AccountValue setKey(String key) {
        this.key = key;
        return this;
    }


    public String getValue() {
        return value;
    }


    public AccountValue setValue(String value) {
        this.value = value;
        return this;
    }


    public String getDescription() {
        return description;
    }


    public AccountValue setDescription(String description) {
        this.description = description;
        return this;
    }


    /**
     * get (外键,modelName:Account,tableName:senpure_account)
     *
     * @return
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * set (外键,modelName:Account,tableName:senpure_account)
     *
     * @return
     */
    public AccountValue setAccountId(Long accountId) {
        this.accountId = accountId;
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
    public AccountValue setVersion(Integer version) {
        this.version = version;
        return this;
    }


    @Override
    public String toString() {
        return "AccountValue{"
                + "id=" + id
                + ",version=" + version
                + ",key=" + key
                + ",value=" + value
                + ",description=" + description
                + ",accountId=" + accountId
                + "}";
    }

}