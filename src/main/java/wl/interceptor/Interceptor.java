package wl.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import wl.entity.LogInfo;

public class Interceptor implements HandlerInterceptor {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        LogInfo logInfo = (LogInfo) (request.getSession()
                .getAttribute("logUser"));
        String uri = request.getRequestURI();
        if (logInfo == null) {
            response.sendRedirect("/ccms1/");
            return false;
        }
        return true;
    }
}
