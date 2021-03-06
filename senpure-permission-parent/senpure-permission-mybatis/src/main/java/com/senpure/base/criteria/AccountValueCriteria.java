package com.senpure.base.criteria;

import com.senpure.base.criterion.Criteria;
import com.senpure.base.model.AccountValue;

import java.io.Serializable;

/**
 * @author senpure
 * @version 2020-5-22 16:52:02
 */
public class AccountValueCriteria extends Criteria implements Serializable {
    private static final long serialVersionUID = 846733638L;

    //(主键)
    private Long id;
    //乐观锁，版本控制
    private Integer version;
    private String key;
    //table [senpure_account_value][column = account_key] criteriaOrder
    private String keyOrder;
    private String value;
    private String description;
    //(外键,modelName:Account,tableName:senpure_account)
    private Long accountId;
    //table [senpure_account_value][column = account_id] criteriaOrder
    private String accountIdOrder;

    public static AccountValue toAccountValue(AccountValueCriteria criteria, AccountValue accountValue) {
        accountValue.setId(criteria.getId());
        accountValue.setKey(criteria.getKey());
        accountValue.setValue(criteria.getValue());
        accountValue.setDescription(criteria.getDescription());
        accountValue.setAccountId(criteria.getAccountId());
        accountValue.setVersion(criteria.getVersion());
        return accountValue;
    }

    public AccountValue toAccountValue() {
        AccountValue accountValue = new AccountValue();
        return toAccountValue(this, accountValue);
    }

    /**
     * 将AccountValueCriteria 的有效值(不为空),赋值给 AccountValue
     *
     * @return AccountValue
     */
    public AccountValue effective(AccountValue accountValue) {
        if (getId() != null) {
            accountValue.setId(getId());
        }
        if (getKey() != null) {
            accountValue.setKey(getKey());
        }
        if (getValue() != null) {
            accountValue.setValue(getValue());
        }
        if (getDescription() != null) {
            accountValue.setDescription(getDescription());
        }
        if (getAccountId() != null) {
            accountValue.setAccountId(getAccountId());
        }
        if (getVersion() != null) {
            accountValue.setVersion(getVersion());
        }
        return accountValue;
    }

    @Override
    protected void beforeStr(StringBuilder sb) {
        sb.append("AccountValueCriteria{");
        if (id != null) {
            sb.append("id=").append(id).append(",");
        }
        if (version != null) {
            sb.append("version=").append(version).append(",");
        }
        if (key != null) {
            sb.append("key=").append(key).append(",");
        }
        if (value != null) {
            sb.append("value=").append(value).append(",");
        }
        if (description != null) {
            sb.append("description=").append(description).append(",");
        }
        if (accountId != null) {
            sb.append("accountId=").append(accountId).append(",");
        }
    }

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
    public AccountValueCriteria setId(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }


    public AccountValueCriteria setKey(String key) {
        if (key != null && key.trim().length() == 0) {
            this.key = null;
            return this;
        }
        this.key = key;
        return this;
    }

    /**
     * get table [senpure_account_value][column = account_key] criteriaOrder
     *
     * @return
     */
    public String getKeyOrder() {
        return keyOrder;
    }

    /**
     * set table [senpure_account_value][column = account_key] criteriaOrder DESC||ASC
     *
     * @return
     */
    public AccountValueCriteria setKeyOrder(String keyOrder) {
        this.keyOrder = keyOrder;
        putSort("account_key", keyOrder);
        return this;
    }

    public String getValue() {
        return value;
    }


    public AccountValueCriteria setValue(String value) {
        if (value != null && value.trim().length() == 0) {
            this.value = null;
            return this;
        }
        this.value = value;
        return this;
    }

    public String getDescription() {
        return description;
    }


    public AccountValueCriteria setDescription(String description) {
        if (description != null && description.trim().length() == 0) {
            this.description = null;
            return this;
        }
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
    public AccountValueCriteria setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * get table [senpure_account_value][column = account_id] criteriaOrder
     *
     * @return
     */
    public String getAccountIdOrder() {
        return accountIdOrder;
    }

    /**
     * set table [senpure_account_value][column = account_id] criteriaOrder DESC||ASC
     *
     * @return
     */
    public AccountValueCriteria setAccountIdOrder(String accountIdOrder) {
        this.accountIdOrder = accountIdOrder;
        putSort("account_id", accountIdOrder);
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
    public AccountValueCriteria setVersion(Integer version) {
        this.version = version;
        return this;
    }

}