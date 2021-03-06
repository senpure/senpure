package com.senpure.base.service;

import com.senpure.base.model.RoleValue;
import com.senpure.base.result.RoleValuePageResult;
import com.senpure.base.criteria.RoleValueCriteria;
import com.senpure.base.mapper.RoleValueMapper;
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
@CacheConfig(cacheNames = "roleValue")
public class RoleValueService extends BaseService {

    @Resource
    private RoleValueMapper roleValueMapper;

    @CacheEvict(key = "#id")
    public void clearCache(Long id) {
    }

    @CacheEvict(allEntries = true)
    public void clearCache() {
    }

    @Cacheable(key = "#id", unless = "#result == null")
    public RoleValue find(Long id) {
        return roleValueMapper.find(id);
    }

    @Cacheable(key = "#id", unless = "#result == null")
    public RoleValue findOnlyCache(Long id) {
        return null;
    }

    @CachePut(key = "#id", unless = "#result == null")
    public RoleValue findSkipCache(Long id) {
        return roleValueMapper.find(id);
    }

    public int count() {
        return roleValueMapper.count();
    }

    public List<RoleValue> findAll() {
        return roleValueMapper.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#id")
    public boolean delete(Long id) {
        int result = roleValueMapper.delete(id);
        return result == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#criteria.id", allEntries = true)
    public int delete(RoleValueCriteria criteria) {
        return roleValueMapper.deleteByCriteria(criteria);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(RoleValue roleValue) {
        roleValue.setId(idGenerator.nextId());
        int result = roleValueMapper.save(roleValue);
        return result == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public int save(List<RoleValue> roleValues) {
        if (roleValues == null || roleValues.size() == 0) {
            return 0;
        }
        for (RoleValue roleValue : roleValues) {
            roleValue.setId(idGenerator.nextId());
        }
        return roleValueMapper.saveList(roleValues);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(RoleValueCriteria criteria) {
        criteria.setId(idGenerator.nextId());
        int result = roleValueMapper.save(criteria.toRoleValue());
        return result == 1;
    }

    /**
     * ?????????????????????OptimisticLockingFailureException
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#roleValue.id")
    public boolean update(RoleValue roleValue) {
        int updateCount = roleValueMapper.update(roleValue);
        if (updateCount == 0) {
            throw new OptimisticLockingFailureException(roleValue.getClass() + ",[" + roleValue.getId() + "],???????????????,?????????[" + roleValue.getVersion() + "]");
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
    public int update(RoleValueCriteria criteria) {
        int updateCount = roleValueMapper.updateByCriteria(criteria);
        if (updateCount == 0 && criteria.getVersion() != null
                && criteria.getId() != null) {
            throw new OptimisticLockingFailureException(criteria.getClass() + ",[" + criteria.getId() + "],???????????????,?????????[" + criteria.getVersion() + "]");
        }
        return updateCount;
    }

    @Transactional(readOnly = true)
    public RoleValuePageResult findPage(RoleValueCriteria criteria) {
        RoleValuePageResult result = RoleValuePageResult.success();
        //?????????????????????
        if (criteria.getId() != null) {
            RoleValue roleValue = roleValueMapper.find(criteria.getId());
            if (roleValue != null) {
                List<RoleValue> roleValues = new ArrayList<>(16);
                roleValues.add(roleValue);
                result.setTotal(1);
                result.setRoleValues(roleValues);
            } else {
                result.setTotal(0);
            }
            return result;
        }
        int total = roleValueMapper.countByCriteria(criteria);
        result.setTotal(total);
        if (total == 0) {
            return result;
        }
        //????????????????????????
        checkPage(criteria, total);
        List<RoleValue> roleValues = roleValueMapper.findByCriteria(criteria);
        result.setRoleValues(roleValues);
        return result;
    }

    public List<RoleValue> find(RoleValueCriteria criteria) {
        //?????????????????????
        if (criteria.getId() != null) {
            List<RoleValue> roleValues = new ArrayList<>(16);
            RoleValue roleValue = roleValueMapper.find(criteria.getId());
            if (roleValue != null) {
                roleValues.add(roleValue);
            }
            return roleValues;
        }
        return roleValueMapper.findByCriteria(criteria);
    }

    public RoleValue findOne(RoleValueCriteria criteria) {
        //?????????????????????
        if (criteria.getId() != null) {
            return roleValueMapper.find(criteria.getId());
        }
        List<RoleValue> roleValues = roleValueMapper.findByCriteria(criteria);
        if (roleValues.size() == 0) {
            return null;
        }
        return roleValues.get(0);
    }

    public List<RoleValue> findByRoleId(Long roleId) {
        RoleValueCriteria criteria = new RoleValueCriteria();
        criteria.setUsePage(false);
        criteria.setRoleId(roleId);
        return roleValueMapper.findByCriteria(criteria);
    }

    public RoleValue findByKey(String key) {
        RoleValueCriteria criteria = new RoleValueCriteria();
        criteria.setUsePage(false);
        criteria.setKey(key);
        List<RoleValue> roleValues = roleValueMapper.findByCriteria(criteria);
        if (roleValues.size() == 0) {
            return null;
        }
        return roleValues.get(0);
    }

}