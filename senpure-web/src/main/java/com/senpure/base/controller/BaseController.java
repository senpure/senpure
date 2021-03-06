package com.senpure.base.controller;


import com.senpure.base.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BaseController {

    protected Logger logger;

    public BaseController() {
        logger = LoggerFactory.getLogger(getClass());
    }

    protected LocaleResolver localeResolver;

    public LocaleResolver getLocaleResolver() {
        return localeResolver;
    }

    @Autowired
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    protected ModelAndView addActionResult(HttpServletRequest request, ModelAndView modelAndView, ResultMap result) {
        return addActionResult(request, modelAndView, result, true);
    }

    protected ModelAndView addActionResult(HttpServletRequest request, ModelAndView modelAndView, ResultMap result,
                                           boolean i18n) {
        if (i18n && result.getMessage() == null) {
            logger.debug("localeResolver" + localeResolver);
            ResultHelper.wrapMessage(result, localeResolver.resolveLocale(request));
        }

        return modelAndView.addObject(WebConstant.ACTION_RESULT_MODEL_VIEW_KEY, result);
    }



    protected ModelAndView view(HttpServletRequest request, String view) {

        return addActionResult(request, new ModelAndView(view), ResultMap.success(), false);
    }

    protected ModelAndView view(HttpServletRequest request, String view, ResultMap result) {

        return addActionResult(request, new ModelAndView(view), result, false);
    }



    protected ModelAndView result(HttpServletRequest request, String view, ResultMap result) {

        return addActionResult(request, new ModelAndView(view), result, true);
    }

    protected ModelAndView success(HttpServletRequest request, String view) {

        return addActionResult(request, new ModelAndView(view), ResultMap.success(), true);
    }

    protected ResultMap success(HttpServletRequest request) {

        return ResultHelper.wrapMessage(ResultMap.success(), localeResolver.resolveLocale(request));

    }

    protected ModelAndView dim(HttpServletRequest request, String view) {

        return addActionResult(request, new ModelAndView(view), ResultMap.dim(), true);
    }

    protected ModelAndView dim(HttpServletRequest request) {

        return addActionResult(request, new ModelAndView("dim"), ResultMap.dim(), true);
    }

    protected ModelAndView incorrect(HttpServletRequest request, BindingResult result,
                                     String view) {
        // LocaleContextHolder.getLocale();
        return addFormatIncorrectResult(request, result, new ModelAndView(view));
    }

    protected ModelAndView addFormatIncorrectResult(HttpServletRequest request, BindingResult result,
                                                    ModelAndView modelAndView) {
        // LocaleContextHolder.getLocale();
        return modelAndView.addObject(WebConstant.ACTION_RESULT_MODEL_VIEW_KEY, incorrect(request, result));
    }

    protected void incorrect(HttpServletRequest request, BindingResult bindingResult, ActionResult result) {
        Locale locale = localeResolver.resolveLocale(request);
        Map<String, String> validators = new HashMap<>();
        incorrect(locale, bindingResult, validators);
        result.setValidators(validators);
    }

    protected ResultMap incorrect(HttpServletRequest request, BindingResult bindingResult) {
        Locale locale = localeResolver.resolveLocale(request);
        Map<String, String> validators = new HashMap<>();
        incorrect(locale, bindingResult, validators);
        ResultMap rm = ResultMap.result(Result.FORMAT_INCORRECT);
        rm.put(ResultMap.VALIDATORS_KEY, validators);
        ResultHelper.wrapMessage(rm, locale);
        return rm;
    }

    private void incorrect(Locale locale, BindingResult bindingResult, Map<String, String> validators) {
        List<ObjectError> es = bindingResult.getAllErrors();
        //FieldError
        for (ObjectError e : es) {
            Object[] args = e.getArguments();
            MessageSourceResolvable sr = (MessageSourceResolvable) args[0];
            String[] codes = sr.getCodes();
            String key = codes[codes.length - 1];
            if (key.endsWith("Valid")) {
                key = key.replace("Valid", "");
            }
            validators.put(key, e.getDefaultMessage());
        }
        logger.warn("validators {} ", validators);
    }

    protected ModelAndView formatIncorrect(HttpServletRequest request, BindingResult validResult, String view) {
        logger.warn("???????????????");
        return addFormatIncorrectResult(request, validResult, new ModelAndView(view));
    }


    protected ResultMap wrapMessage(HttpServletRequest request, ResultMap resultMap) {
        ResultHelper.wrapMessage(resultMap, localeResolver.resolveLocale(request));
        return resultMap;
    }

    protected ResultMap wrapMessage(HttpServletRequest request, ResultMap resultMap, Object... args) {
        ResultHelper.wrapMessage(resultMap, localeResolver.resolveLocale(request), args);
        return resultMap;
    }


    protected Locale getLocale(HttpServletRequest request) {
        return localeResolver.resolveLocale(request);
    }
}
