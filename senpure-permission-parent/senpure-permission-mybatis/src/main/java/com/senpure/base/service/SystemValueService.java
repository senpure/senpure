package com.senpure.base.service;

import com.senpure.base.model.SystemValue;
import com.senpure.base.result.SystemValuePageResult;
import com.senpure.base.criteria.SystemValueCriteria;
import com.senpure.base.mapper.SystemValueMapper;
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
@CacheConfig(cacheNames = "systemValue")
public class SystemValueService extends BaseService {

    @Resource
    private SystemValueMapper systemValueMapper;

    @CacheEvict(key = "#id")
    public void clearCache(Long id) {
    }

    @CacheEvict(allEntries = true)
    public void clearCache() {
    }

    @Cacheable(key = "#id", unless = "#result == null")
    public SystemValue find(Long id) {
        return systemValueMapper.find(id);
    }

    @Cacheable(key = "#id", unless = "#result == null")
    public SystemValue findOnlyCache(Long id) {
        return null;
    }

    @CachePut(key = "#id", unless = "#result == null")
    public SystemValue findSkipCache(Long id) {
        return systemValueMapper.find(id);
    }

    public int count() {
        return systemValueMapper.count();
    }

    public List<SystemValue> findAll() {
        return systemValueMapper.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#id")
    public boolean delete(Long id) {
        int result = systemValueMapper.delete(id);
        return result == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#criteria.id", allEntries = true)
    public int delete(SystemValueCriteria criteria) {
        return systemValueMapper.deleteByCriteria(criteria);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(SystemValue systemValue) {
        systemValue.setId(idGenerator.nextId());
        int result = systemValueMapper.save(systemValue);
        return result == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public int save(List<SystemValue> systemValues) {
        if (systemValues == null || systemValues.size() == 0) {
            return 0;
        }
        for (SystemValue systemValue : systemValues) {
            systemValue.setId(idGenerator.nextId());
        }
        return systemValueMapper.saveList(systemValues);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(SystemValueCriteria criteria) {
        criteria.setId(idGenerator.nextId());
        int result = systemValueMapper.save(criteria.toSystemValue());
        return result == 1;
    }

    /**
     * ?????????????????????OptimisticLockingFailureException
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(key = "#systemValue.id")
    public boolean update(SystemValue systemValue) {
        int updateCount = systemValueMapper.update(systemValue);
        if (updateCount == 0) {
            throw new OptimisticLockingFailureException(systemValue.getClass() + ",[" + systemValue.getId() + "],???????????????,?????????[" + systemValue.getVersion() + "]");
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
    public int update(SystemValueCriteria criteria) {
        int updateCount = systemValueMapper.updateByCriteria(criteria);
        if (updateCount == 0 && criteria.getVersion() != null
                && criteria.getId() != null) {
            throw new OptimisticLockingFailureException(criteria.getClass() + ",[" + criteria.getId() + "],???????????????,?????????[" + criteria.getVersion() + "]");
        }
        return updateCount;
    }

    @Transactional(readOnly = true)
    public SystemValuePageResult findPage(SystemValueCriteria criteria) {
        SystemValuePageResult result = SystemValuePageResult.success();
        //?????????????????????
        if (criteria.getId() != null) {
            SystemValue systemValue = systemValueMapper.find(criteria.getId());
            if (systemValue != null) {
                List<SystemValue> systemValues = new ArrayList<>(16);
                systemValues.add(systemValue);
                result.setTotal(1);
                result.setSystemValues(systemValues);
            } else {
                result.setTotal(0);
            }
            return result;
        }
        int total = systemValueMapper.countByCriteria(criteria);
        result.setTotal(total);
        if (total == 0) {
            return result;
        }
        //????????????????????????
        checkPage(criteria, total);
        List<SystemValue> systemValues = systemValueMapper.findByCriteria(criteria);
        result.setSystemValues(systemValues);
        return result;
    }

    public List<SystemValue> find(SystemValueCriteria criteria) {
        //?????????????????????
        if (criteria.getId() != null) {
            List<SystemValue> systemValues = new ArrayList<>(16);
            SystemValue systemValue = systemValueMapper.find(criteria.getId());
            if (systemValue != null) {
                systemValues.add(systemValue);
            }
            return systemValues;
        }
        return systemValueMapper.findByCriteria(criteria);
    }

    public SystemValue findOne(SystemValueCriteria criteria) {
        //?????????????????????
        if (criteria.getId() != null) {
            return systemValueMapper.find(criteria.getId());
        }
        List<SystemValue> systemValues = systemValueMapper.findByCriteria(criteria);
        if (systemValues.size() == 0) {
            return null;
        }
        return systemValues.get(0);
    }

    public List<SystemValue> findByType(String type) {
        SystemValueCriteria criteria = new SystemValueCriteria();
        criteria.setUsePage(false);
        criteria.setType(type);
        return systemValueMapper.findByCriteria(criteria);
    }

    public SystemValue findByKey(String key) {
        SystemValueCriteria criteria = new SystemValueCriteria();
        criteria.setUsePage(false);
        criteria.setKey(key);
        List<SystemValue> systemValues = systemValueMapper.findByCriteria(criteria);
        if (systemValues.size() == 0) {
            return null;
        }
        return systemValues.get(0);
    }

}