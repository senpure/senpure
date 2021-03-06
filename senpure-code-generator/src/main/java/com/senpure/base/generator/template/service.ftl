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

/**
${sovereignty}
 * @version ${.now?datetime}
 */
@Service
public class ${name}${config.serviceSuffix} extends BaseService {

    @Resource
    private ${name}${config.mapperSuffix} ${nameRule(name)}${config.mapperSuffix};

    public ${name} find(${id.clazzType} ${id.name}) {
        return ${nameRule(name)}${config.mapperSuffix}.find(${id.name});
    }

    public int count() {
        return ${nameRule(name)}${config.mapperSuffix}.count();
    }

    public List<${name}> findAll() {
        return ${nameRule(name)}${config.mapperSuffix}.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean delete(${id.clazzType} ${id.name}) {
        int result = ${nameRule(name)}${config.mapperSuffix}.delete(${id.name});
        return result == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public int delete(${name?cap_first}Criteria criteria) {
        return ${nameRule(name)}${config.mapperSuffix}.deleteByCriteria(criteria);
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
        <#--
        //TODO 注意是否有主键
        //${nameRule(name)}.set${id.name?cap_first}();
        -->
        for (${name} ${nameRule(name)} : ${pluralize(nameRule(name))}) {
            //${nameRule(name)}.set${id.name?cap_first}();
            checkPrimaryKey(${nameRule(name)}, ${nameRule(name)}.get${id.name?cap_first}());
        }
    </#if>
</#if>
        return ${nameRule(name)}${config.mapperSuffix}.saveList(${pluralize(nameRule(name))});
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
     * 更新失败会抛出OptimisticLockingFailureException
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean update(${name?cap_first} ${nameRule(name)}) {
        int updateCount = ${nameRule(name)}${config.mapperSuffix}.update(${nameRule(name)});
        if (updateCount == 0) {
            throw new OptimisticLockingFailureException(${nameRule(name)}.getClass() + ",[" + ${nameRule(name)}.get${id.name?cap_first}() + "],版本号冲突,版本号[" + ${nameRule(name)}.get${version.name?cap_first}() + "]");
        }
        return true;
    }

    /**
     * 当版本号，和主键不为空时，更新失败会抛出OptimisticLockingFailureException
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(${name?cap_first}Criteria criteria) {
        int updateCount = ${nameRule(name)}${config.mapperSuffix}.updateByCriteria(criteria);
        if (updateCount == 0 && criteria.get${version.name?cap_first}() != null
                && criteria.get${id.name?cap_first}() != null) {
            throw new OptimisticLockingFailureException(criteria.getClass() + ",[" + criteria.get${id.name?cap_first}() + "],版本号冲突,版本号[" + criteria.get${version.name?cap_first}() + "]");
        }
        return updateCount;
    }
<#else >
    @Transactional(rollbackFor = Exception.class)
    public boolean update(${name?cap_first} ${nameRule(name)}) {
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