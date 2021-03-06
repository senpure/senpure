package ${servicePackage};

import ${modelPackage}.${name};
import ${criteriaPackage}.${name}${config.criteriaSuffix};
import ${mapperPackage}.${name}${config.mapperSuffix};
import ${resultPackage}.${name}${config.resultPageSuffix};
<#if version??>
import com.senpure.base.exception.OptimisticLockingFailureException;
</#if>
<#if modelPackage !="com.senpure.base.model">
import com.senpure.base.service.BaseService;
</#if>
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 该类对 [${name}]的增删查改有本地缓存,只缓存主键，不缓存查询缓存
 * <li>find(${id.clazzType} ${id.name}):按主键缓存</li>
 * <li>findAll():按主键缓存</li>
 * <li>delete(${id.clazzType} ${id.name}):按主键清除缓存</li>
 * <li>delete(${name?cap_first}Criteria criteria):清除<strong>所有</strong>${name}缓存</li>
 * <li>update(${name?cap_first} ${nameRule(name)}):按主键清除缓存</li>
 * <li>update(${name?cap_first}Criteria criteria):有主键时按主键移除缓存，没有主键时清除<strong>所有</strong>${name}缓存 </li>
 *
${sovereignty}
 * @version ${.now?datetime}
 */
@Service
public class ${name}${config.serviceSuffix} extends BaseService {

    private ConcurrentMap<String, ${name}> localCache = new ConcurrentHashMap(128);
    @Resource
    private ${name}${config.mapperSuffix} ${nameRule(name)}${config.mapperSuffix};

    private String cacheKey(${id.clazzType} ${id.name}) {
        return "${nameRule(name)}:" + ${id.name};
    }

    public void clearCache(${id.clazzType} ${id.name}) {
        localCache.remove(cacheKey(${id.name}));
    }

    public void clearCache() {
        localCache.clear();
    }

    /**
     * 按主键本地缓存
     *
     * @return
     */
    public ${name} find(${id.clazzType} ${id.name}) {
        String cacheKey = cacheKey(${id.name});
        ${name} ${nameRule(name)} = localCache.get(cacheKey);
        if (${nameRule(name)} == null) {
            ${nameRule(name)} = ${nameRule(name)}${config.mapperSuffix}.find(${id.name});
            if (${nameRule(name)} != null) {
                localCache.putIfAbsent(cacheKey, ${nameRule(name)});
                return localCache.get(cacheKey);
            }
        }
        return ${nameRule(name)};
    }

    public ${name} findOnlyCache(${id.clazzType} ${id.name}) {
        return localCache.get(cacheKey(${id.name}));
    }

    public ${name} findSkipCache(${id.clazzType} ${id.name}) {
        return ${nameRule(name)}${config.mapperSuffix}.find(${id.name});
    }

    public int count() {
        return ${nameRule(name)}${config.mapperSuffix}.count();
    }

    /**
     * 每一个结果会按主键本地缓存
     *
     * @return
     */
    public List<${name}> findAll() {
        List<${name}> ${pluralize(nameRule(name))} = ${nameRule(name)}${config.mapperSuffix}.findAll();
        for (${name} ${nameRule(name)} : ${pluralize(nameRule(name))}) {
            localCache.put(cacheKey(${nameRule(name)}.get${id.name?cap_first}()), ${nameRule(name)});
        }
        return ${pluralize(nameRule(name))};
    }

    /**
     * 按主键清除本地缓存
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(${id.clazzType} ${id.name}) {
        localCache.remove(cacheKey(${id.name}));
        int result = ${nameRule(name)}${config.mapperSuffix}.delete(${id.name});
        return result == 1;
    }

    /**
     * 会清除<strong>所有<strong>${name}缓存
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(${name?cap_first}Criteria criteria) {
        int result = ${nameRule(name)}${config.mapperSuffix}.deleteByCriteria(criteria);
        if (result > 0) {
            clearCache();
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(${name?cap_first} ${nameRule(name)}) {
<#if !id.databaseId >
    <#if id.clazzType =="Long" || id.clazzType =="long">
        ${nameRule(name)}.set${id.name?cap_first}(idGenerator.nextId());
    <#else >
        <#--
        //TODO 注意是否有主键
        //${nameRule(name)}.set${id.name?cap_first}();
        -->
        checkPrimaryKey(${nameRule(name)}, ${nameRule(name)}.get${id.name?cap_first}());
    </#if>
</#if>
        int result = ${nameRule(name)}${config.mapperSuffix}.save(${nameRule(name)});
        return result == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public int save(List<${name?cap_first}> ${pluralize(nameRule(name))}) {
        if (${pluralize(nameRule(name))} == null || ${pluralize(nameRule(name))}.size() == 0) {
            return 0;
        }
<#if !id.databaseId >
    <#if id.clazzType =="Long" || id.clazzType =="long">
        for (${name} ${nameRule(name)} : ${pluralize(nameRule(name))}) {
            ${nameRule(name)}.set${id.name?cap_first}(idGenerator.nextId());
        }
    <#else >
        for (${name} ${nameRule(name)} : ${pluralize(nameRule(name))}) {
            //${nameRule(name)}.set${id.name?cap_first}();
            checkPrimaryKey(${nameRule(name)}, ${nameRule(name)}.get${id.name?cap_first}());
        }
    </#if>
</#if>
        int size = ${nameRule(name)}${config.mapperSuffix}.saveList(${pluralize(nameRule(name))});
        return size;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean save(${name?cap_first}Criteria criteria) {
<#if !id.databaseId >
    <#if id.clazzType =="Long" || id.clazzType =="long">
        criteria.set${id.name?cap_first}(idGenerator.nextId());
    <#else >
        <#--
        //TODO 注意是否有主键
        //criteria.set${id.name?cap_first}();
        -->
        checkPrimaryKey(criteria, criteria.get${id.name?cap_first}());
    </#if>
</#if>
        int result = ${nameRule(name)}${config.mapperSuffix}.save(criteria.to${name?cap_first}());
        return result == 1;
    }

<#if version??>
    /**
     * 按主键移除缓存<br>
     * 更新失败会抛出OptimisticLockingFailureException
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean update(${name?cap_first} ${nameRule(name)}) {
        localCache.remove(cacheKey(${nameRule(name)}.get${id.name?cap_first}()));
        int updateCount = ${nameRule(name)}${config.mapperSuffix}.update(${nameRule(name)});
        if (updateCount == 0) {
            throw new OptimisticLockingFailureException(${nameRule(name)}.getClass() + ",[" + ${nameRule(name)}.get${id.name?cap_first}() + "],版本号冲突,版本号[" + ${nameRule(name)}.get${version.name?cap_first}() + "]");
        }
        return true;
    }

    /**
     * 有主键时按主键移除缓存，没有主键时清空<strong>所有</strong>${name}缓存<br>
     * 当版本号，和主键不为空时，更新失败会抛出OptimisticLockingFailureException
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(${name?cap_first}Criteria criteria) {
        if (criteria.get${id.name?cap_first}() != null) {
            localCache.remove(cacheKey(criteria.get${id.name?cap_first}()));
        }
        int updateCount = ${nameRule(name)}${config.mapperSuffix}.updateByCriteria(criteria);
        if (updateCount == 0 && criteria.get${version.name?cap_first}() != null
                && criteria.get${id.name?cap_first}() != null) {
            throw new OptimisticLockingFailureException(criteria.getClass() + ",[" + criteria.get${id.name?cap_first}() + "],版本号冲突,版本号[" + criteria.get${version.name?cap_first}() + "]");
        }
        if (criteria.get${id.name?cap_first}() != null && updateCount > 0) {
            localCache.clear();
        }
        return updateCount;
    }
<#else >
    /**
     * 按主键移除缓存<br>
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean update(${name?cap_first} ${nameRule(name)}) {
        localCache.remove(cacheKey(${nameRule(name)}.get${id.name?cap_first}()));
        int updateCount = ${nameRule(name)}${config.mapperSuffix}.update(${nameRule(name)});
        if (updateCount == 0) {
            return false;
        }
        return true;
    }
</#if>

    @Transactional(readOnly = true)
    public ${name}${config.resultPageSuffix} findPage(${name?cap_first}Criteria criteria) {
        ${name}${config.resultPageSuffix} pageResult = ${name}${config.resultPageSuffix}.success();
        //是否是主键查找
        <#if id.javaNullable>
        if (criteria.get${id.name?cap_first}() != null) {
        <#else>
        if (criteria.get${id.name?cap_first}() > 0) {
        </#if>
            ${name} ${nameRule(name)} = ${nameRule(name)}${config.mapperSuffix}.find(criteria.get${id.name?cap_first}());
            if (${nameRule(name)} != null) {
                List<${name}> ${pluralize(nameRule(name))} = new ArrayList<>(16);
                ${pluralize(nameRule(name))}.add(${nameRule(name)});
                pageResult.setTotal(1);
                pageResult.set${pluralize(nameRule(name))?cap_first}(${pluralize(nameRule(name))});
            } else {
            pageResult.setTotal(0);
            }
            return pageResult;
        }
        int total = ${nameRule(name)}${config.mapperSuffix}.countByCriteria(criteria);
        pageResult.setTotal(total);
        if (total == 0) {
            return pageResult;
        }
        //检查页数是否合法
        checkPage(criteria, total);
        List<${name}> ${pluralize(nameRule(name))} = ${nameRule(name)}${config.mapperSuffix}.findByCriteria(criteria);
        pageResult.set${pluralize(nameRule(name))?cap_first}(${pluralize(nameRule(name))});
        return pageResult;
    }

    public List<${name}> find(${name?cap_first}Criteria criteria) {
        //是否是主键查找
        <#if id.javaNullable>
        if (criteria.get${id.name?cap_first}() != null) {
        <#else>
        if (criteria.get${id.name?cap_first}() > 0) {
        </#if>
            List<${name}> ${pluralize(nameRule(name))} = new ArrayList<>(16);
            ${name} ${nameRule(name)} = ${nameRule(name)}${config.mapperSuffix}.find(criteria.get${id.name?cap_first}());
            if (${nameRule(name)} != null) {
                ${pluralize(nameRule(name))}.add(${nameRule(name)});
            }
            return ${pluralize(nameRule(name))};
        }
        return ${nameRule(name)}${config.mapperSuffix}.findByCriteria(criteria);
    }

    public ${name} findOne(${name?cap_first}Criteria criteria) {
        //是否是主键查找
        <#if id.javaNullable>
        if (criteria.get${id.name?cap_first}() != null) {
        <#else>
        if (criteria.get${id.name?cap_first}() > 0 ) {
        </#if>
            return ${nameRule(name)}${config.mapperSuffix}.find(criteria.get${id.name?cap_first}());
        }
        List<${name}> ${pluralize(nameRule(name))} = ${nameRule(name)}${config.mapperSuffix}.findByCriteria(criteria);
        if (${pluralize(nameRule(name))}.size() == 0) {
            return null;
        }
        return ${pluralize(nameRule(name))}.get(0);
    }
<#list findModeFields as field>

    public <#if field.findOne>${name}<#else >List<${name}></#if> findBy${field.name?cap_first}(${field.clazzType} ${field.name}) {
        ${name}${config.criteriaSuffix} criteria = new ${name}${config.criteriaSuffix}();
        criteria.setUsePage(false);
        criteria.set${field.name?cap_first}(${field.name});
    <#if field.findOne>
        List<${name}> ${pluralize(nameRule(name))} = ${nameRule(name)}${config.mapperSuffix}.findByCriteria(criteria);
        if (${pluralize(nameRule(name))}.size() == 0) {
            return null;
        }
        return ${pluralize(nameRule(name))}.get(0);
    <#else>
        return ${nameRule(name)}${config.mapperSuffix}.findByCriteria(criteria);
    </#if>
    }
</#list>

}