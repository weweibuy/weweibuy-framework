//package com.weweibuy.framework.common.metric.http;
//
//import org.springframework.core.annotation.Order;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author durenhao
// * @date 2020/7/3 21:07
// **/
//@Order(-100)
//public class HttpMetricFilter extends OncePerRequestFilter {
//
//    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 1,
//            0L, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<Runnable>(5000), new ThreadPoolExecutor.DiscardPolicy());
//
//    private final HttpMetricOperator httpMetricOperator;
//
//    public HttpMetricFilter(HttpMetricOperator httpMetricOperator) {
//        this.httpMetricOperator = httpMetricOperator;
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String requestURI = request.getRequestURI();
//        String method = request.getMethod();
//        long startTime = System.nanoTime();
//        try {
//            filterChain.doFilter(request, response);
//        } finally {
//            EXECUTOR.execute(() -> httpMetricOperator.onRequestMetric(requestURI, method, startTime, response.getStatus()));
//        }
//
//    }
//
//
//
//}
