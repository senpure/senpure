package com.senpure.base.service;

import com.senpure.base.PermissionConstant;
import com.senpure.base.Result;
import com.senpure.base.ResultMap;
import com.senpure.base.criteria.*;
import com.senpure.base.model.*;
import com.senpure.base.result.RolePageResult;
import com.senpure.base.struct.*;
import com.senpure.base.util.Assert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class AuthorizeService extends BaseService {

    @Resource
    private AccountService accountService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private AccountRoleService accountRoleService;

    @Resource
    private SystemValueService systemValueService;
    @Resource
    private AccountValueService accountValueService;
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionMenuService permissionMenuService;

    @Resource
    private URIPermissionService uriPermissionService;
    @Resource
    private ContainerPermissionService containerPermissionService;
    @Resource
    private RolePermissionService rolePermissionService;
    @Resource
    private ContainerService containerService;
    @Resource
    private RoleService roleService;

    private final List<Runnable> initWorks = new ArrayList<>();
    private List<Menu> directMenus = new ArrayList<>();

    public List<Menu> getDirectMenus() {
        return directMenus;
    }

    public void setDirectMenus(List<Menu> directMenus) {
        this.directMenus = directMenus;
    }

    public Account findAccount(Long accountId) {
        return accountService.find(accountId);
    }

    @Transactional(rollbackOn = Exception.class)
    public ResultMap login(LoginCriteria criteria) {
        AccountCriteria accountCriteria = new AccountCriteria();
        accountCriteria.setAccount(criteria.getAccount());
        Account account = accountService.findOne(accountCriteria);
        if (account == null) {
            return ResultMap.result(Result.ACCOUNT_NOT_EXIST);
        }
        if (!account.getPassword().equals(criteria.getPassword())) {
            return ResultMap.result(Result.PASSWORD_INCORRECT);
        }


        long now = System.currentTimeMillis();
        account.setLoginDate(new Date(now));
        account.setLoginTime(now);

        account.setIp(criteria.getLoginIP());
        account.setOnline(true);
        account.setLoginType(criteria.getLoginType());
        accountService.update(account);
        LoginedAccount loginedAccount = new LoginedAccount();
        loginedAccount.setId(account.getId());
        loginedAccount.setAccount(account.getAccount());
        loginedAccount.setName(account.getName());
        loginedAccount.setLoginIP(criteria.getLoginIP());
        loginedAccount.setLoginTime(now);
        loginedAccount.setContainerId(account.getContainerId());
        loginedAccount.getNormalValueMap().putAll(loadConfig(account.getId()));
        loginedAccount.getPermissions().addAll(loadPermission(account.getId()));
        loginedAccount.getViewMenus().addAll(needMenus(loginedAccount.getPermissions()));
        return ResultMap.success().put("account", loginedAccount);
    }


    @Transactional(rollbackOn = Exception.class)
    public void logout(LoginedAccount account) {

        Account a = accountService.find(account.getId());
        a.setOnline(false);

        accountService.update(a);

    }

    private Map<String, NormalValue> loadConfig(Long accountId) {
        Map<String, NormalValue> map = new HashMap<>(16);
        SystemValueCriteria systemValueCriteria = new SystemValueCriteria();
        systemValueCriteria.setUsePage(false);
        systemValueCriteria.setType(PermissionConstant.VALUE_TYPE_ACCOUNT_DEFAULT);
        List<SystemValue> systemValues = systemValueService.find(systemValueCriteria);
        systemValues.forEach(systemValue -> {

            NormalValue normalValue = new NormalValue();
            normalValue.setId(systemValue.getId());
            normalValue.setKey(systemValue.getKey());
            normalValue.setValue(systemValue.getValue());
            normalValue.setNormal(false);
            map.put(systemValue.getKey(), normalValue);
        });
        AccountValueCriteria accountValueCriteria = new AccountValueCriteria();
        accountValueCriteria.setUsePage(false);
        accountValueCriteria.setAccountId(accountId);
        List<AccountValue> accountValues = accountValueService.find(accountValueCriteria);
        accountValues.forEach(accountValue -> {
            NormalValue normalValue = new NormalValue();
            normalValue.setId(accountValue.getId());
            normalValue.setKey(accountValue.getKey());
            normalValue.setValue(accountValue.getValue());
            normalValue.setVersion(accountValue.getVersion());
            normalValue.setNormal(true);
            map.put(accountValue.getKey(), normalValue);
        });
        return map;
    }

    private Collection<com.senpure.base.menu.Menu> needMenus(List<MergePermission> mergePermissions) {
        Map<Integer, com.senpure.base.menu.Menu> viewMenus = new HashMap<>(16);
        for (MergePermission mergePermission : mergePermissions) {
            PermissionMenuCriteria permissionMenuCriteria = new PermissionMenuCriteria();
            permissionMenuCriteria.setUsePage(false);
            permissionMenuCriteria.setPermissionName(mergePermission.getName());
            PermissionMenu permissionMenu = permissionMenuService.findOne(permissionMenuCriteria);
            if (permissionMenu != null) {
                mergeMenu(permissionMenu.getMenuId(), viewMenus);
            }
        }
        for (Menu menu : directMenus) {

            mergeMenu(menu.getId(), viewMenus);
        }
        return viewMenus.values();
    }

    public void mergeMenu(int menuId, Map<Integer, com.senpure.base.menu.Menu> viewMenus) {

        if (!viewMenus.containsKey(menuId)) {
            Menu m = menuService.find(menuId);
            com.senpure.base.menu.Menu menu = new com.senpure.base.menu.Menu();
            menu.setIcon(m.getIcon());
            menu.setId(m.getId());
            menu.setText(m.getText());
            menu.setUri(m.getUri());
            menu.setSort(m.getSort());
            viewMenus.put(menuId, menu);
            if (m.getParentId() == null) {
                menu.setParentId(0);
            } else {
                menu.setParentId(m.getParentId());
                mergeMenu(m.getParentId(), viewMenus);
            }
        }
    }

    private Collection<MergePermission> loadPermission(Long accountId) {
        AccountRoleCriteria accountRoleCriteria = new AccountRoleCriteria();
        accountRoleCriteria.setAccountId(accountId);
        accountRoleCriteria.setUsePage(false);
        List<AccountRole> accountRoles = accountRoleService.find(accountRoleCriteria);
        Map<Long, MergePermission> mergePermissionMap = new HashMap<>(16);
        long now = System.currentTimeMillis();
        for (AccountRole accountRole : accountRoles) {
            Long arExpiry = accountRole.getExpiryTime();
            arExpiry = arExpiry == null ? 0 : arExpiry;
            if (arExpiry > 0 && arExpiry < now) {
                continue;
            }
            RolePermissionCriteria rolePermissionCriteria = new RolePermissionCriteria();
            rolePermissionCriteria.setUsePage(false);
            rolePermissionCriteria.setRoleId(accountRole.getRoleId());
            List<RolePermission> rolePermissions = rolePermissionService.find(rolePermissionCriteria);
            for (RolePermission rolePermission : rolePermissions) {
                Long rpExpiry = rolePermission.getExpiryTime();
                rpExpiry = rpExpiry == null ? 0 : rpExpiry;
                if (rpExpiry > 0 && rpExpiry < now) {
                    continue;
                }
                Permission permission = permissionService.find(rolePermission.getPermissionId());
                long lessExpiry;
                if (arExpiry == 0) {
                    lessExpiry = rpExpiry;
                } else if (rpExpiry == 0) {
                    lessExpiry = arExpiry;
                } else {
                    lessExpiry = arExpiry < rpExpiry ? arExpiry : rpExpiry;
                }
                MergePermission old = mergePermissionMap.get(permission.getId());
                if (old != null) {
                    if (old.getExpiry() > 0) {
                        if (lessExpiry == 0) {
                            old.setExpiry(0);
                        } else {
                            old.setExpiry(Math.max(old.getExpiry(), lessExpiry));
                        }
                    }
                } else {
                    MergePermission mergePermission = new MergePermission();
                    mergePermission.setId(permission.getId());
                    mergePermission.setName(permission.getName());
                    mergePermission.setExpiry(lessExpiry);
                    mergePermission.setType(permission.getType());
                    mergePermissionMap.put(permission.getId(), mergePermission);
                }
            }
        }
        return mergePermissionMap.values();
    }


    public void syncPermission(String serverName, Collection<Permission> permissions, Map<String, List<URIPermission>> uriPermissionMap) {
        List<Permission> beforePermissions = permissionService.findAll();
        List<Permission> updatePermissions = new ArrayList<>(128);
        List<Permission> savePermissions = new ArrayList<>(128);
        List<URIPermission> saveURIPermissions = new ArrayList<>(128);
        for (Permission permission : permissions) {
            boolean save = true;
            for (Permission beforePermission : beforePermissions) {
                if (permission.getName().equals(beforePermission.getName())) {
                    save = false;
                    if (beforePermission.getDatabaseUpdate()) {
                        logger.info("{},??????????????????????????????????????????????????????????????????????????????????????????\ncode{}\ndatabase{}",
                                beforePermission.getName(), permission, beforePermission);
                    } else {
                        boolean update = true;
                        //?????????????????????
                        if (!permission.getServerName().equals(beforePermission.getServerName())) {
                            //?????????????????????????????????????????????
                            if (beforePermission.getServerName().equals(PermissionConstant.DEFAULT_SERVER_NAME)) {
                                logger.info("??????{}???????????? {} --> {}", beforePermission.getName(), beforePermission.getServerName(), permission.getServerName());
                            } else {
                                //????????????????????????????????????
                                logger.info("??????{}  {} ?????? {} ?????????", beforePermission.getName(), permission.getServerName(), beforePermission.getServerName());
                                update = false;
                            }
                        }
                        if (update) {
                            permission.setId(beforePermission.getId());
                            permission.setVersion(beforePermission.getVersion());
                            logger.debug("{},??????{}", permission.getName(), permission);
                            updatePermissions.add(permission);
                        }
                    }
                    //??????permission???????????????uriPermission????????????save
                    List<URIPermission> uriPermissions = uriPermissionMap.get(permission.getName());
                    for (URIPermission uriPermission : uriPermissions) {
                        boolean saveUri = true;

                        List<URIPermission> beforeURIPermissions = uriPermissionService.findByUriAndMethod(uriPermission.getUriAndMethod());
                        for (URIPermission beforeURIPermission : beforeURIPermissions) {
                            if (beforeURIPermission.getPermissionId().longValue() == beforePermission.getId()) {
                                saveUri = false;
                                uriPermission.setPermissionId(beforePermission.getId());
                            }
                        }
                        if (saveUri) {
                            uriPermission.setPermissionId(beforePermission.getId());
                            saveURIPermissions.add(uriPermission);
                        }
                    }
                    break;
                }
            }
            if (save) {
                savePermissions.add(permission);
                logger.debug("{},??????{}", permission.getName(), permission);
            }
        }

        if (updatePermissions.size() > 0) {
            for (Permission permission : updatePermissions) {

                permissionService.update(permission);
            }
        }
        if (savePermissions.size() > 0) {
            permissionService.save(savePermissions);
            for (Permission permission : savePermissions) {
                List<URIPermission> uriPermissions = uriPermissionMap.get(permission.getName());
                for (URIPermission uriPermission : uriPermissions) {
                    uriPermission.setPermissionId(permission.getId());
                    saveURIPermissions.add(uriPermission);
                }
            }
        }
        if (saveURIPermissions.size() > 0) {
            for (URIPermission uriPermission : saveURIPermissions) {
                logger.debug("??????{} > {}", uriPermission.getUriAndMethod(), uriPermission.getPermissionId());
            }
            uriPermissionService.save(saveURIPermissions);
        }
        deleteDirtyPermission(beforePermissions, permissions);
        deleteDirtyUri(serverName, uriPermissionMap);

    }

    private void deleteDirtyPermission(List<Permission> beforePermissions, Collection<Permission> permissions) {
        for (Permission beforePermission : beforePermissions) {
            if (beforePermission.getDatabaseUpdate()) {
                continue;
            }
            boolean dirty = true;
            for (Permission permission : permissions) {
                if (!beforePermission.getServerName().equals(permission.getServerName())
                        || permission.getName().equals(beforePermission.getName())) {
                    dirty = false;
                    break;
                }
            }
            if (dirty) {
                logger.info("??????????????? {}", beforePermission);
                ContainerPermissionCriteria containerPermissionCriteria = new ContainerPermissionCriteria();
                containerPermissionCriteria.setPermissionId(beforePermission.getId());
                RolePermissionCriteria rolePermissionCriteria = new RolePermissionCriteria();
                rolePermissionCriteria.setPermissionId(beforePermission.getId());
                URIPermissionCriteria uriPermissionCriteria = new URIPermissionCriteria();
                uriPermissionCriteria.setPermissionId(beforePermission.getId());
                PermissionMenuCriteria permissionMenuCriteria = new PermissionMenuCriteria();
                permissionMenuCriteria.setPermissionName(beforePermission.getName());
                int result = rolePermissionService.delete(rolePermissionCriteria);
                logger.info("??????RolePermission {} ???", result);
                result = containerPermissionService.delete(containerPermissionCriteria);
                logger.info("??????ContainerPermission {} ???", result);
                result = uriPermissionService.delete(uriPermissionCriteria);
                logger.info("??????URIPermission {} ???", result);
                result = permissionMenuService.delete(permissionMenuCriteria);
                logger.info("?????? permissionMenu {} ???", result);
                permissionService.delete(beforePermission.getId());

            }
        }
    }

    private void deleteDirtyUri(String serverName, Map<String, List<URIPermission>> uriPermissionMap) {
        AtomicReference<List<URIPermission>> uriPermissions = new AtomicReference<>(new ArrayList<>());
        uriPermissionMap.forEach((key, uris) -> uriPermissions.get().addAll(uris));
        List<URIPermission> beforeUris = uriPermissionService.findAll();
        for (URIPermission before : beforeUris) {
            if (before.getDatabaseUpdate()) {
                continue;
            }
            boolean dirty = true;
            for (URIPermission uriPermission : uriPermissions.get()) {
                if (uriPermission.getPermissionId().longValue() == before.getPermissionId()
                        && uriPermission.getUriAndMethod().equals(before.getUriAndMethod())) {
                    dirty = false;
                    break;
                }
            }
            if (dirty) {
                Permission permission = permissionService.find(before.getPermissionId());
                if (permission == null || permission.getServerName().equals(serverName)) {
                    logger.info("??????????????????URIPermission {} ", before);
                    uriPermissionService.delete(before.getId());
                }
            }
        }
    }

    public void syncMenu(Collection<Menu> menus) {
        List<Menu> beforeMenus = menuService.findAll();
        deleteDirtyMenu(menus, beforeMenus);
        List<Menu> updateMenus = new ArrayList<>(56);
        List<Menu> saveMenus = new ArrayList<>(56);
        for (Menu menu : menus) {
            boolean save = true;
            for (Menu beforeMenu : beforeMenus) {
                if (beforeMenu.getId().intValue() == menu.getId().intValue()) {
                    if (!beforeMenu.getServerName().equals(menu.getServerName())) {
                        if (beforeMenu.getServerName().equals(PermissionConstant.DEFAULT_SERVER_NAME)) {
                            logger.info("??????{}???????????? {} --> {}", beforeMenu.getText(), beforeMenu.getServerName(), menu.getServerName());

                        } else {
                            Assert.error("???????????????id???????????????????????? " + beforeMenu.getId() + " now " + menu.getServerName() + " before " + beforeMenu.getServerName());
                        }
                    }
                    save = false;
                    if (!beforeMenu.getDatabaseUpdate()) {
                        menu.setVersion(beforeMenu.getVersion());
                        logger.debug("??????{}", menu);
                        updateMenus.add(menu);
                    }
                    break;
                }
            }
            if (save) {
                logger.debug("??????{}", menu);
                saveMenus.add(menu);
            }
        }
        if (updateMenus.size() > 0) {
            for (Menu menu : updateMenus) {
                menuService.update(menu);
            }
        }
        if (saveMenus.size() > 0) {
            menuService.save(saveMenus);
        }

    }

    public void syncMenuPermission(Collection<PermissionMenu> permissionMenus) {
        List<PermissionMenu> beforePermissionMenus = permissionMenuService.findAll();
        List<PermissionMenu> savePermissionMenus = new ArrayList<>(56);
        List<PermissionMenu> updatePermissionMenus = new ArrayList<>(56);
        for (PermissionMenu permissionMenu : permissionMenus) {
            boolean save = true;
            for (PermissionMenu beforePermissionMenu : beforePermissionMenus) {
                if (beforePermissionMenu.getMenuId().intValue() == permissionMenu.getMenuId()) {
                    save = false;
                    if (!beforePermissionMenu.getDataBaseUpdate()) {
                        permissionMenu.setId(beforePermissionMenu.getId());
                        permissionMenu.setVersion(beforePermissionMenu.getVersion());
                        logger.debug("??????{}", permissionMenu);
                        updatePermissionMenus.add(permissionMenu);
                    }
                    break;
                }
            }
            if (save) {
                logger.debug("??????{}", permissionMenu);
                savePermissionMenus.add(permissionMenu);
            }
        }
        if (updatePermissionMenus.size() > 0) {
            for (PermissionMenu permissionMenu : updatePermissionMenus) {
                permissionMenuService.update(permissionMenu);
            }
        }
        if (savePermissionMenus.size() > 0) {
            permissionMenuService.save(savePermissionMenus);
        }

    }

    private void deleteDirtyMenu(Collection<Menu> menus, List<Menu> beforeMenus) {

        for (Menu beforeMenu : beforeMenus) {
            if (beforeMenu.getDatabaseUpdate()) {
                continue;
            }
            boolean dirty = true;
            for (Menu menu : menus) {
                if (menu.getId().intValue() == beforeMenu.getId().intValue()) {
                    dirty = false;
                    break;
                }
            }
            if (dirty) {
                logger.info("??????????????? {}", beforeMenu);
                PermissionMenuCriteria criteria = new PermissionMenuCriteria();
                criteria.setMenuId(beforeMenu.getId());
                int result = permissionMenuService.delete(criteria);
                logger.info("?????? PermissionMenu {} ???", result);
                menuService.delete(beforeMenu.getId());
            }
        }

    }

    public void loadStatic() {

        uriPermissionService.putCacheByUriAndMethod();
        for (Runnable runnable : initWorks) {
            runnable.run();
        }
    }

    public void addInitWork(Runnable work) {
        initWorks.add(work);
    }

    public boolean createContainer(ContainerCriteria criteria, long accountId, int parentId) {
        Container container = criteria.toContainer();
        Date date = new Date();
        container.setCreateTime(date.getTime());
        container.setCreateDate(date);
        container.setRelation(accountId);
        container.setParentId(parentId);
        Container parent = containerService.find(parentId);
        if (parent == null) {
            return false;
        }
        container.setContainerStructure(parent.getContainerStructure() + PermissionConstant.CONTAINER_SEPARATOR + parent.getId());
        return containerService.save(container);
    }

    public ResultMap containerHasPermissions(int containerId) {
        ContainerPermissionCriteria criteria = new ContainerPermissionCriteria();
        criteria.setUsePage(false);
        criteria.setContainerId(containerId);
        List<ContainerPermission> containerPermissions = containerPermissionService.find(criteria);
        Container container = containerService.find(containerId);
        criteria.setContainerId(container.getParentId());
        List<ContainerPermission> parentPermission = containerPermissionService.find(criteria);

        long now = System.currentTimeMillis();
        List<Long> myPermissions = new ArrayList<>(32);
        containerPermissions.forEach(containerPermission -> {
            if (containerPermission.getExpiryTime() == null
                    || containerPermission.getExpiryTime() == 0 ||
                    containerPermission.getExpiryTime() > now) {
                myPermissions.add(containerPermission.getPermissionId());
            }
        });
        List<HasPermission> hasPermissions = new ArrayList<>();
        parentPermission.forEach(containerPermission -> {
                    if (containerPermission.getExpiryTime() == null
                            || containerPermission.getExpiryTime() == 0 ||
                            containerPermission.getExpiryTime() > now) {
                        HasPermission hasPermission = new HasPermission();
                        hasPermission.copy(permissionService.find(containerPermission.getPermissionId()));
                        if (myPermissions.contains(hasPermission.getId())) {
                            hasPermission.setHas(true);
                        }
                        hasPermissions.add(hasPermission);
                    }
                }
        );
        hasPermissions.sort(Comparator.comparing(Permission::getSort));

        return ResultMap.success().putItems(hasPermissions);
    }


    public void containerPermission(int containerId, long permissionId, boolean award) {
        ContainerPermissionCriteria criteria = new ContainerPermissionCriteria();
        criteria.setContainerId(containerId);
        criteria.setPermissionId(permissionId);
        ContainerPermission containerPermission = containerPermissionService.findOne(criteria);
        if (award) {
            if (containerPermission == null) {
                containerPermission = criteria.toContainerPermission();
                containerPermission.setExpiryTime(PermissionConstant.FOREVER_TIME);
                containerPermission.setExpiryDate(PermissionConstant.FOREVER_DATE);
                containerPermissionService.save(containerPermission);
            }
        } else {

            //hibernate???????????????
            List<Role> roles = roleService.findByContainerId(containerId);
            ContainerPermission finalContainerPermission = containerPermission;
            roles.forEach(role -> {
                RolePermissionCriteria rolePermissionCriteria = new RolePermissionCriteria();
                rolePermissionCriteria.setPermissionId(finalContainerPermission.getId());
                rolePermissionCriteria.setRoleId(role.getId());
                rolePermissionService.delete(rolePermissionCriteria);
            });
            containerPermissionService.delete(containerPermission.getId());


        }
    }

    public boolean createAccount(AccountCriteria criteria) {
        long now = System.currentTimeMillis();
        Account account = criteria.toAccount();
        accountService.defaultAccount(now, account);
        return accountService.save(account);
    }


    public ResultMap hasRole(Long accountId) {

        List<AccountRole> accountRoles = accountRoleService.findByAccountId(accountId);
        List<Long> myRoles = new ArrayList<>();
        accountRoles.forEach(accountRole -> {
            if (accountRole.getExpiryTime() == null || accountRole.getExpiryTime() == 0 ||
                    accountRole.getExpiryTime() > System.currentTimeMillis()) {
                myRoles.add(accountRole.getRoleId());
            }
        });
        List<Role> roles = roleService.findByContainerId(accountService.find(accountId).getContainerId());


        List<HasRole> hasRoles = new ArrayList<>(32);
        roles.forEach(role -> {
            HasRole hasRole = new HasRole();
            hasRole.copy(role);
            if (myRoles.contains(role.getId())) {

                hasRole.setHas(true);
            }
            hasRoles.add(hasRole);

        });

        hasRoles.sort(Comparator.comparing(HasRole::getId));
        return ResultMap.success().putItems(hasRoles);
    }

    public void accountRole(long accountId, long roleId, boolean award) {

        AccountRoleCriteria criteria = new AccountRoleCriteria();
        criteria.setAccountId(accountId);
        criteria.setRoleId(roleId);
        AccountRole accountRole = accountRoleService.findOne(criteria);
        if (award) {
            if (accountRole == null) {
                accountRole = criteria.toAccountRole();
                accountRole.setExpiryTime(PermissionConstant.FOREVER_TIME);
                accountRole.setExpiryDate(PermissionConstant.FOREVER_DATE);
                accountRoleService.save(accountRole);
            }
        } else {
            if (accountRole != null) {
                accountRoleService.delete(accountRole.getId());
            }
        }
    }


    public RolePageResult loadRoles(RoleCriteria criteria) {
        return roleService.findPage(criteria);
    }

    public void createRole(RoleCriteria criteria) {
        Date now = new Date();
        criteria.setCreateDate(now);
        if (criteria.getDescription() == null) {
            criteria.setDescription(criteria.getName());
        }
        roleService.save(criteria);
    }

    public ResultMap roleHasPermission(long roleId) {
        List<ContainerPermission> containerPermissions = containerPermissionService.findByContainerId(roleService.find(roleId).getContainerId());
        List<RolePermission> rolePermissions = rolePermissionService.findByRoleId(roleId);
        List<Long> myPermissions = new ArrayList<>(32);
        long now = System.currentTimeMillis();
        rolePermissions.forEach(containerPermission -> {
            if (containerPermission.getExpiryTime() == null
                    || containerPermission.getExpiryTime() == 0 ||
                    containerPermission.getExpiryTime() > now) {
                myPermissions.add(containerPermission.getPermissionId());
            }
        });
        List<HasPermission> hasPermissions = new ArrayList<>();
        containerPermissions.forEach(containerPermission -> {
                    if (containerPermission.getExpiryTime() == null
                            || containerPermission.getExpiryTime() == 0 ||
                            containerPermission.getExpiryTime() > now) {
                        HasPermission hasPermission = new HasPermission();
                        hasPermission.copy(permissionService.find(containerPermission.getPermissionId()));
                        if (myPermissions.contains(hasPermission.getId())) {
                            hasPermission.setHas(true);
                        }
                        hasPermissions.add(hasPermission);
                    }
                }
        );
        hasPermissions.sort(Comparator.comparing(Permission::getSort));

        return ResultMap.success().putItems(hasPermissions);
    }

    public void rolePermission(long roleId, long permissionId, boolean award) {
        RolePermissionCriteria criteria = new RolePermissionCriteria();
        criteria.setRoleId(roleId);
        criteria.setPermissionId(permissionId);
        if (award) {
            RolePermission rolePermission = rolePermissionService.findOne(criteria);
            if (rolePermission == null) {
                rolePermission = criteria.toRolePermission();
                rolePermission.setExpiryTime(PermissionConstant.FOREVER_TIME);
                rolePermission.setExpiryDate(PermissionConstant.FOREVER_DATE);
                rolePermissionService.save(rolePermission);
            }
        } else {
            rolePermissionService.delete(criteria);
        }


    }

    public void updatePassword(long accountId, String password) {

        AccountCriteria criteria = new AccountCriteria();
        criteria.setId(accountId);
        criteria.setPassword(password);
        accountService.update(criteria);
    }
}
