package com.senpure.base.service;

import com.senpure.base.model.AccountRole;
import com.senpure.base.result.AccountRolePageResult;
import com.senpure.base.criteria.AccountRoleCriteria;
import com.senpure.base.mapper.AccountRoleMapper;
import com.senpure.base.exception.OptimisticLockingFailureException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;

/**
 * @author senpure
 * @version 2020-5-22 16:52:02
 */
@Service
@CacheConfig(cacheNames = "accountRole")
public class AccountRoleService extends BaseService {

    @Resource
    private AccountRoleMapper accountRoleMapper;

    @CacheEvict(key = "#id")
    public void clearCache(Long id) {
    }

    @CacheEvict(allEntries = true)
    public void clearCache() {
    }

    @Cacheable(key = "#id", unless = "#result == null")
    public AccountRole find(Long id) {
        return accountRoleMapper.find(id);
    }

    @Cacheable(key = "#id", unless = "#result == null")
    public AccountRole findOnlyCache(Long id) {
        return null;
    }

    @CachePut(key = "#id", unless = "#result == null")
    public AccountRole findSkipCache(Long id) {
        return accountRoleMapper.find(id);
    }

    public int count() {
        return accountRoleMapper.count();
    }

    public List<AccountRole> findAll() {
        return accountRoleMapper.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#id")
    public boolean delete(Long id) {
        int result = accountRoleMapper.delete(id);
        return result == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#criteria.id", allEntries = true)
    public int delete(AccountRoleCriteria criteria) {
        return accountRoleMapper.deleteByCriteria(criteria);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(AccountRole accountRole) {
        accountRole.setId(idGenerator.nextId());
        int result = accountRoleMapper.save(accountRole);
        return result == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public int save(List<AccountRole> accountRoles) {
        if (accountRoles == null || accountRoles.size() == 0) {
            return 0;
        }
        for (AccountRole accountRole : accountRoles) {
            accountRole.setId(idGenerator.nextId());
        }
        return accountRoleMapper.saveList(accountRoles);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(AccountRoleCriteria criteria) {
        criteria.setId(idGenerator.nextId());
        int result = accountRoleMapper.save(criteria.toAccountRole());
        return result == 1;
    }

    /**
     * ?????????????????????OptimisticLockingFailureException
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#accountRole.id")
    public boolean update(AccountRole accountRole) {
        int updateCount = accountRoleMapper.update(accountRole);
        if (updateCount == 0) {
            throw new OptimisticLockingFailureException(accountRole.getClass() + ",[" + accountRole.getId() + "],???????????????,?????????[" + accountRole.getVersion() + "]");
        }
        return true;
    }

    /**
     * ????????????????????????????????????????????????????????????OptimisticLockingFailureException
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#criteria.id", allEntries = true)
    public int update(AccountRoleCriteria criteria) {
        int updateCount = accountRoleMapper.updateByCriteria(criteria);
        if (updateCount == 0 && criteria.getVersion() != null
                && criteria.getId() != null) {
            throw new OptimisticLockingFailureException(criteria.getClass() + ",[" + criteria.getId() + "],???????????????,?????????[" + criteria.getVersion() + "]");
        }
        return updateCount;
    }

    @Transactional(readOnly = true)
    public AccountRolePageResult findPage(AccountRoleCriteria criteria) {
        AccountRolePageResult result = AccountRolePageResult.success();
        //?????????????????????
        if (criteria.getId() != null) {
            AccountRole accountRole = accountRoleMapper.find(criteria.getId());
            if (accountRole != null) {
                List<AccountRole> accountRoles = new ArrayList<>(16);
                accountRoles.add(accountRole);
                result.setTotal(1);
                result.setAccountRoles(accountRoles);
            } else {
                result.setTotal(0);
            }
            return result;
        }
        int total = accountRoleMapper.countByCriteria(criteria);
        result.setTotal(total);
        if (total == 0) {
            return result;
        }
        //????????????????????????
        checkPage(criteria, total);
        List<AccountRole> accountRoles = accountRoleMapper.findByCriteria(criteria);
        result.setAccountRoles(accountRoles);
        return result;
    }

    public List<AccountRole> find(AccountRoleCriteria criteria) {
        //?????????????????????
        if (criteria.getId() != null) {
            List<AccountRole> accountRoles = new ArrayList<>(16);
            AccountRole accountRole = accountRoleMapper.find(criteria.getId());
            if (accountRole != null) {
                accountRoles.add(accountRole);
            }
            return accountRoles;
        }
        return accountRoleMapper.findByCriteria(criteria);
    }

    public AccountRole findOne(AccountRoleCriteria criteria) {
        //?????????????????????
        if (criteria.getId() != null) {
            return accountRoleMapper.find(criteria.getId());
        }
        List<AccountRole> accountRoles = accountRoleMapper.findByCriteria(criteria);
        if (accountRoles.size() == 0) {
            return null;
        }
        return accountRoles.get(0);
    }

    public List<AccountRole> findByAccountId(Long accountId) {
        AccountRoleCriteria criteria = new AccountRoleCriteria();
        criteria.setUsePage(false);
        criteria.setAccountId(accountId);
        return accountRoleMapper.findByCriteria(criteria);
    }

    public List<AccountRole> findByRoleId(Long roleId) {
        AccountRoleCriteria criteria = new AccountRoleCriteria();
        criteria.setUsePage(false);
        criteria.setRoleId(roleId);
        return accountRoleMapper.findByCriteria(criteria);
    }

}