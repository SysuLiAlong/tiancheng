package com.tiancheng.ms.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.tiancheng.ms.common.context.ContextHolder;
import com.tiancheng.ms.common.context.ContextUser;
import com.tiancheng.ms.common.dto.AjaxResult;
import com.tiancheng.ms.common.exception.BusinessException;
import com.tiancheng.ms.constant.ErrorCode;
import com.tiancheng.ms.constant.GlobalConstant;
import com.tiancheng.ms.service.UserService;
import com.tiancheng.ms.util.CookieUtil;
import com.tiancheng.ms.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Slf4j
@Component
@Order(1)
public class ApiAspect {

    @Autowired
    private UserService userService;

    @Autowired
    private ApiProperties apiProperties;

    @Pointcut("execution(* com.tiancheng.ms.controller.auth.*Controller.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getResponse();
        try {
            ContextHolder.setUser(null);
            String uri = req.getRequestURI();

            if (!isWhiteApi(uri)) {
                String token = getAccessToken(req);
                if (StringUtils.isEmpty(token)) {
                    throw new BusinessException(ErrorCode.FAIL_AUTH);
                } else {
                    ContextUser user = userService.getUserByToken(token);
                    if (user == null) {
                        throw new BusinessException(ErrorCode.FAIL_AUTH);
                    }
                    ContextHolder.setUser(user);
                }
            }
            Object result = pjp.proceed();
            if (result instanceof AjaxResult) {
                ResponseUtil.write(resp, JSONObject.toJSONString(result));
            } else {
                ResponseUtil.write(resp, JSONObject.toJSONString(AjaxResult.success(result)));
            }
        } catch (Exception ex) {
            log.error("接口执行异常：方法:" + pjp.getSignature() + ",参数:" + JSONObject.toJSONString(pjp.getArgs()), ex);
            if (ex instanceof BusinessException) {
                ResponseUtil.write(resp, JSONObject.toJSONString(AjaxResult.fail((BusinessException)ex)));
            } else if (ex instanceof IllegalArgumentException) {
                ResponseUtil.write(resp, JSONObject.toJSONString(AjaxResult.fail(ex.getMessage())));
            } else {
                ResponseUtil.write(resp, JSONObject.toJSONString(AjaxResult.fail("糟糕，网络开小差了，请刷新重试。")));
            }
        }
        return null;
    }

    /**
     * 获取参数的access_token
     *
     * @param req
     * @return
     */
    private String getAccessToken(HttpServletRequest req) {
        String token = req.getParameter(GlobalConstant.TOKEN);
        if (!StringUtils.isEmpty(token)) {
            return token;
        }
        token = req.getHeader(GlobalConstant.TOKEN);
        if (!StringUtils.isEmpty(token)) {
            return token;
        }

        return CookieUtil.getCookieValue(req, GlobalConstant.TOKEN);
    }

    public boolean isWhiteApi(String uri) {
        if (CollectionUtils.isEmpty(apiProperties.getIgnoreApis())) {
            return false;
        }
        if (apiProperties.getIgnoreApis().contains(uri)) {
            return true;
        }
        return false;
    }
}
