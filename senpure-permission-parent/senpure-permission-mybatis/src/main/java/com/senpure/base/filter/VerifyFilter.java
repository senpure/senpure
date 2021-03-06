package com.senpure.base.filter;

import com.senpure.base.PermissionConstant;
import com.senpure.base.Result;
import com.senpure.base.ResultHelper;
import com.senpure.base.ResultMap;
import com.senpure.base.controller.LoginController;
import com.senpure.base.model.Account;
import com.senpure.base.model.Permission;
import com.senpure.base.model.URIPermission;
import com.senpure.base.service.AuthorizeService;
import com.senpure.base.service.PermissionService;
import com.senpure.base.service.URIPermissionService;
import com.senpure.base.struct.LoginedAccount;
import com.senpure.base.struct.MergePermission;
import com.senpure.base.util.Http;
import com.senpure.base.util.StringUtil;
import com.senpure.base.verify.ResourcesVerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Component
public class VerifyFilter extends OncePerRequestFilter implements OrderedFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String loginURI = "/authorize/loginView";
    private final String loginAction = "/authorize/login";
    @Resource
    private URIPermissionService uriPermissionService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private AuthorizeService authorizeService;
    @Resource
    private ResourcesVerifyService resourcesVerifyService;

    @Resource
    private LoginController loginController;
    @Resource
    protected LocaleResolver localeResolver;
    private final List<PatternsRequestCondition> patternsRequestConditions = new ArrayList<>();

    private boolean ready;

    @Override
    protected void initFilterBean() {
        // initPatterns();
    }

    public void initPatterns() {
        List<URIPermission> uriPermissions = uriPermissionService.findAll();
        Map<Long, Set<String>> map = new HashMap<>(128);
        for (URIPermission uriPermission : uriPermissions) {
            Set<String> patterns = map.computeIfAbsent(uriPermission.getPermissionId(), k -> new HashSet<>());
            String uri = uriPermission.getUriAndMethod();
            int index = StringUtil.indexOf(uriPermission.getUriAndMethod(), "[", 1, true);
            if (index > 0) {
                uri = uri.substring(0, index);
            }
            patterns.add(uri);
        }
        map.values().forEach(strings -> {
            String[] patterns = new String[strings.size()];
            strings.toArray(patterns);
            PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(patterns);
            patternsRequestConditions.add(patternsRequestCondition);
        });
        for (PatternsRequestCondition patternsRequestCondition : patternsRequestConditions) {
            logger.info("verify patterns {}", patternsRequestCondition);
        }

        ready = true;
    }


    private void toLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultMap result = ResultMap.result(Result.ACCOUNT_NOT_LOGIN_OR_SESSION_TIMEOUT);
        RequestDispatcher dispatcher = request.getRequestDispatcher(loginURI);
        ResultHelper.wrapMessage(result, localeResolver.resolveLocale(request));
        afterLogin(request, result, false);
        dispatcher.forward(new HttpMethodRequestWrapper(request, "GET"), response);
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!ready) {
            toLogin(request, response);
            return;
        }
        LoginedAccount account = Http.getSubject(request, LoginedAccount.class);
        if (account != null) {
            Account lastAccount = authorizeService.findAccount(account.getId());
            boolean login = account.getLoginTime() < lastAccount.getLoginTime();
            //  && !account.getLoginIP().equals(accountVo.getIp());
            if (login && !request.getRequestURI().equals(loginAction)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher(loginURI);
                ResultMap result = ResultMap.result(Result.ACCOUNT_OTHER_LOGIN);
                ResultHelper.wrapMessage(result, localeResolver.resolveLocale(request), lastAccount.getIp() == null ? "UNKNOWN" : lastAccount.getIp());
                logger.info("????????????????????????????????????????????????,??????????????????{}", request.getRequestURI());
                logger.debug(result.toString());
                afterLogin(request, result, false);
                dispatcher.forward(new HttpMethodRequestWrapper(request, "GET"), response);
                return;
            }
        }
        //PatternsRequestCondition match = null;
        List<String> matches = Collections.emptyList();
        logger.trace("??????{}", request.getRequestURI());
        for (PatternsRequestCondition patterns : patternsRequestConditions) {
            matches = patterns.getMatchingPatterns(request.getRequestURI());
            if (matches.size() > 0) {
                logger.trace("{}  {}", matches, request.getRequestURI());
                break;
            }
        }
        if (matches.size() > 0) {
            //String bestMatch = match.getPatterns().iterator().next();
            String bestMatch = matches.get(0);
            if (bestMatch.endsWith("/")) {
                bestMatch = bestMatch.substring(0, bestMatch.length() - 1);
            }
            logger.debug("{} match {}", request.getRequestURI(), bestMatch);
            List<URIPermission> uriPermissions = uriPermissionService.findByUriAndMethodOnlyCache(bestMatch + "[" + request.getMethod() + "]");
            if (uriPermissions.size() == 0) {
                logger.debug("{}:{} > {} {}", request.getMethod(), request.getRequestURI(), "???????????????????????????", bestMatch);
                // Assert.error("error ["+bestMatch+"]????????????????????????");
            } else {


                if (account == null) {
                    logger.debug("{} > {}", request.getRequestURI(), "??????????????????????????????");
                    account = loginController.autoLogin(request);
                    if (account == null) {
                        toLogin(request, response);
                        return;
                    } else {
                        logger.debug("auto ????????????");
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append(account.getAccount())
                        .append("[").append(account.getName()).append("] ")
                        .append(request.getMethod())
                        .append("  ")
                        .append(request.getRequestURI());

                boolean pass = false;
                List<Permission> needPermissions = new ArrayList<>();
                for (URIPermission uriPermission : uriPermissions) {
                    needPermissions.add(permissionService.find(uriPermission.getPermissionId()));
                }
                String resourceVerifyName = null;
                String resourceTarget = null;
                String permissionName = null;
                for (Permission permission : needPermissions) {
                    logger.debug("{} ?????? [ {} ] ??????[{},{}] ", sb.toString(), permission.getName(),
                            permission.getReadableName(), permission.getType());
                    permissionName = permission.getName();
                    if (permission.getType().equals(PermissionConstant.PERMISSION_TYPE_NORMAL)) {
                        pass = hasPermission(account, permission.getName());
                    } else if (permission.getType().equals(PermissionConstant.PERMISSION_TYPE_OWNER)) {
                        pass = hasPermission(account, permission.getName());
                        if (pass && permission.getOffset() != null) {
                            String[] offsets = permission.getOffset().split(",");
                            String[] verifyNames = permission.getVerifyName().split(",");
                            for (int i = 0; i < offsets.length; i++) {
                                String resourceId;
                                String uri = request.getRequestURI();
                                int offset = Integer.parseInt(offsets[i]);
                                int first = StringUtil.indexOf(bestMatch, "{", offset);
                                if (first < 0) {
                                    continue;
                                }
                                int formIndex = -1;
                                int count = 0;
                                while (true) {
                                    formIndex = bestMatch.indexOf("/", formIndex + 1);
                                    if (formIndex > first || formIndex < 0) {
                                        break;
                                    } else {
                                        count++;
                                    }
                                }
                                first = StringUtil.indexOf(uri, "/", count);
                                int second = uri.indexOf("/", first + 1);
                                if (second > 0) {
                                    resourceId = uri.substring(first + 1, second);
                                } else {
                                    resourceId = uri.substring(first + 1);
                                }
                                logger.debug("resourceId = {}", resourceId);
                                if (resourceId.equals(PermissionConstant.ALL_OPTION_STRING)) {
                                    pass = true;
                                } else {
                                    pass = resourcesVerifyService.verify(verifyNames[i], account.getId(), resourceId);
                                }
                                if (!pass) {
                                    resourceVerifyName = verifyNames[i];
                                    resourceTarget = resourceId;
                                    break;
                                }
                            }
                        }
                    }
                    if (pass) {
                        break;
                    }
                }
                if (!pass) {
                    List<Object> args = new ArrayList<>();
                    args.add(permissionName);
                    if (resourceVerifyName != null) {
                        logger.warn("{}[{}]  {}:{} ??????????????????{} >{}", account.getAccount(), account.getName(),
                                request.getMethod(), request.getRequestURI(), resourceTarget, resourceTarget);
                        args.add(resourceVerifyName);
                        args.add(resourceTarget);
                    } else {
                        logger.warn("{}[{}] ???????????? {}:{}", account.getAccount(), account.getName(),
                                request.getMethod(), request.getRequestURI());
                    }
                    request.setAttribute("lackArgs", args);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/authorize/forbidden");
                    dispatcher.forward(new HttpMethodRequestWrapper(request, "GET"), response);
                    return;
                }
            }
        } else {
            logger.trace("{} > {}", request.getRequestURI(), "?????????????????????????????????????????????");
        }
        filterChain.doFilter(request, response);
    }


    private boolean hasPermission(LoginedAccount account, String permissionName) {
        int size = account.getPermissions().size();
        for (int i = 0; i < size; i++) {
            MergePermission mergePermission = account.getPermissions().get(i);
            if (mergePermission.getName().equals(permissionName)) {
                if (mergePermission.getExpiry() > 0 && mergePermission.getExpiry() > System.currentTimeMillis()) {
                    account.getPermissions().remove(i);
                    return false;
                }
                return true;
            }
        }
        return account.getAccount().equalsIgnoreCase(PermissionConstant.NAME);
    }

    private void afterLogin(HttpServletRequest request, ResultMap result, boolean checkLogin) {
        request.setAttribute("checkLogin", checkLogin);
        request.setAttribute("args", result);
        if ("get".equalsIgnoreCase(request.getMethod())) {
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            if (query != null) {
                uri = uri + "?" + query;
            }
            logger.debug("get ?????????????????????????????????....,uri=" + uri);
            Http.setToSession(request, "loginToURI", uri);
        } else {
            String referer = request.getHeader("referer");
            if (referer != null) {
                logger.debug("???" + referer + "??????????????????????????????????????????????????????");
                //Http.set(request, "loginReferer", true);
                request.setAttribute("loginReferer", true);
            }
        }
    }


    @Override
    public void destroy() {

    }

    @Override
    public int getOrder() {
        return 0;
    }


    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method.toUpperCase(Locale.ENGLISH);
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
