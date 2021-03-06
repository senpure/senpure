package com.senpure.base.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author senpure
 * @version 2020-5-22 16:52:02
 */
@ApiModel
public class Role implements Serializable {
    private static final long serialVersionUID = 1021021432L;

    //(主键)
    private Long id;
    //乐观锁，版本控制
    @ApiModelProperty(hidden = true )
    private Integer version;
    @ApiModelProperty(example = "name", position = 1)
    private String name;
    @ApiModelProperty(dataType = "date-time", example = "2020-05-22 00:00:00", position = 2)
    private Date createDate;
    @ApiModelProperty(dataType = "long", example = "1590076800000", position = 3)
    private Long createTime;
    @ApiModelProperty(example = "description", position = 4)
    private String description;
    //(外键,modelName:Container,tableName:senpure_container)
    @ApiModelProperty(value = "(外键,modelName:Container,tableName:senpure_container)", dataType = "int", example = "666666", position = 5)
    private Integer containerId;

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
    public Role setId(Long id) {
        this.id = id;
        return this;
    }


    public String getName() {
        return name;
    }


    public Role setName(String name) {
        this.name = name;
        return this;
    }


    public Date getCreateDate() {
        return createDate;
    }


    public Role setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }


    public Long getCreateTime() {
        return createTime;
    }


    public Role setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }


    public String getDescription() {
        return description;
    }


    public Role setDescription(String description) {
        this.description = description;
        return this;
    }


    /**
     * get (外键,modelName:Container,tableName:senpure_container)
     *
     * @return
     */
    public Integer getContainerId() {
        return containerId;
    }

    /**
     * set (外键,modelName:Container,tableName:senpure_container)
     *
     * @return
     */
    public Role setContainerId(Integer containerId) {
        this.containerId = containerId;
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
    public Role setVersion(Integer version) {
        this.version = version;
        return this;
    }


    @Override
    public String toString() {
        return "Role{"
                + "id=" + id
                + ",version=" + version
                + ",name=" + name
                + ",createDate=" + createDate
                + ",createTime=" + createTime
                + ",description=" + description
                + ",containerId=" + containerId
                + "}";
    }

}