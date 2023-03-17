//package com.shopee.seamiter.core.metric;
//
//import com.shopee.seamiter.core.concurrent.NamedThreadFactory;
//import com.shopee.seamiter.dao.*;
//import com.shopee.seamiter.domain.MachineInfo;
//import com.shopee.seamiter.domain.RealResourceMetric;
//import com.shopee.seamiter.domain.ResourceMetric;
//import com.shopee.seamiter.domain.SystemMetric;
//import com.shopee.seamiter.util.StringUtil;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.concurrent.FutureCallback;
//import org.apache.http.entity.ContentType;
//import org.apache.http.impl.client.DefaultRedirectStrategy;
//import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
//import org.apache.http.impl.nio.client.HttpAsyncClients;
//import org.apache.http.impl.nio.reactor.IOReactorConfig;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.Resource;
//import java.net.ConnectException;
//import java.net.SocketTimeoutException;
//import java.nio.charset.Charset;
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicLong;
//import java.util.stream.Collectors;
//
///**
// * 抓取机器的度量数据
// *
// * @author honggang.liu
// */
//@Component
//public class MetricFetcher {
//
//    /**
//     * 没有数据
//     */
//    public static final String NO_METRICS = "No metrics";
//
//    /**
//     * http ok
//     */
//    private static final int HTTP_OK = 200;
//
//    /**
//     * 最大抓取时间间隔
//     */
//    private static final long MAX_LAST_FETCH_INTERVAL_MS = 1000 * 15;
//
//    /**
//     * 抓取间隔
//     */
//    private static final long FETCH_INTERVAL_MS = 6 * 1000;
//
//    /**
//     * 默认字符
//     */
//    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
//
//    /**
//     * 默认抓取路径
//     */
//    private static final String METRIC_URL_PATH = "metric";
//
//    /**
//     * LOGGER
//     */
//    private static final Logger LOGGER = LoggerFactory.getLogger(MetricFetcher.class.getSimpleName());
//
//    /**
//     * 间隔
//     */
//    private static final long INTERVAL_MS = 1000;
//
//    /**
//     * 资源抓取时间Map
//     */
//    private Map<String, AtomicLong> appLastFetchTime = new ConcurrentHashMap<>();
//
//    private CloseableHttpAsyncClient httpclient;
//
//    /**
//     * 应用信息DAO
//     */
//    @Resource
//    private AppInfoDao appInfoDao;
//
//    /**
//     * 应用机器信息
//     */
//    @Resource
//    private MachineInfoDao machineInfoDao;
//
//    /**
//     * metric dao
//     */
//    @Resource
//    private ResourceMetricDao resourceMetricDao;
//
//    /**
//     * 实时资源统计DAO
//     */
//    @Resource
//    private RealResourceMetricDao realResourceMetricDao;
//
//    /**
//     * 系统资源信息
//     */
//    @Resource
//    private SystemMetricDao systemMetricDao;
//
//    @SuppressWarnings("PMD.ThreadPoolCreationRule")
//    private ScheduledExecutorService fetchScheduleService = Executors.newScheduledThreadPool(1,
//            new NamedThreadFactory("metrics-fetch-task", true));
//    private ExecutorService fetchService;
//    private ExecutorService fetchWorker;
//
//    public MetricFetcher() {
//        int cores = Runtime.getRuntime().availableProcessors() * 2;
//        long keepAliveTime = 0;
//        int queueSize = 2048;
//        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
//        fetchService = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
//                new NamedThreadFactory("metrics-fetchService", true), handler);
//        fetchWorker = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
//                new NamedThreadFactory("metrics-fetchWorker", true), handler);
//        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setConnectTimeout(3000)
//                .setSoTimeout(3000)
//                .setIoThreadCount(cores)
//                .build();
//        httpclient = HttpAsyncClients.custom()
//                .setRedirectStrategy(new DefaultRedirectStrategy() {
//                    @Override
//                    protected boolean isRedirectable(final String method) {
//                        return false;
//                    }
//                }).setMaxConnTotal(4000)
//                .setMaxConnPerRoute(1000)
//                .setDefaultIOReactorConfig(ioReactorConfig)
//                .build();
//        httpclient.start();
//        start();
//    }
//
//    private void start() {
//        fetchScheduleService.scheduleAtFixedRate(() -> {
//            try {
//                fetchAllApp();
//            } catch (Exception e) {
//                LOGGER.info("fetchAllApp error:", e);
//            }
//        }, 1000, INTERVAL_MS, TimeUnit.MILLISECONDS);
//    }
//
//    /**
//     * 遍历全部APP，然后获取这个APP下机器的全部metric
//     */
//    private void fetchAllApp() {
//        List<String> apps = appInfoDao.getAppNames();
//        if (apps == null) {
//            return;
//        }
//        for (final String app : apps) {
//            fetchService.submit(() -> {
//                try {
//                    doFetchAppMetric(app);
//                } catch (Exception e) {
//                    LOGGER.error("fetchAppMetric error", e);
//                }
//            });
//        }
//    }
//
//    /**
//     * 抓取应用Metric
//     *
//     * @param app 应用名称
//     */
//    private void doFetchAppMetric(final String app) {
//        long now = System.currentTimeMillis();
//        long lastFetchMs = now - MAX_LAST_FETCH_INTERVAL_MS;
//        if (appLastFetchTime.containsKey(app)) {
//            lastFetchMs = Math.max(lastFetchMs, appLastFetchTime.get(app).get() + 1000);
//        }
//        // trim milliseconds
//        lastFetchMs = lastFetchMs / 1000 * 1000;
//        long endTime = lastFetchMs + FETCH_INTERVAL_MS;
//        if (endTime > now - 1000 * 2) {
//            // too near
//            return;
//        }
//        // update last_fetch in advance.
//        appLastFetchTime.computeIfAbsent(app, a -> new AtomicLong()).set(endTime);
//        final long finalLastFetchMs = lastFetchMs;
//        final long finalEndTime = endTime;
//        System.out.println("last:"+new Date(finalLastFetchMs)+";end:"+new Date(finalEndTime));
//        try {
//            // do real fetch async
//            fetchWorker.submit(() -> {
//                try {
//                    fetchOnce(app, finalLastFetchMs, finalEndTime, 5);
//                } catch (Exception e) {
//                    LOGGER.info("fetchOnce(" + app + ") error", e);
//                }
//            });
//        } catch (Exception e) {
//            LOGGER.info("submit fetchOnce(" + app + ") fail, intervalMs [" + lastFetchMs + ", " + endTime + "]", e);
//        }
//    }
//
//    /**
//     * 抓取[startTime,endTime]之间的metric,包含两个边缘
//     *
//     * @param app            应用名称
//     * @param startTime      开始时间戳
//     * @param endTime        结束时间戳
//     * @param maxWaitSeconds 最大等待时间
//     */
//    private void fetchOnce(String app, long startTime, long endTime, int maxWaitSeconds) {
//        if (maxWaitSeconds <= 0) {
//            throw new IllegalArgumentException("maxWaitSeconds must > 0, but " + maxWaitSeconds);
//        }
//        // TODO 应用存活判断
//        List<MachineInfo> machineInfos = machineInfoDao.getMachineDetailByApp(0, Integer.MAX_VALUE, app, null);
//        LOGGER.debug("enter fetchOnce({}), machines.size()={}, time intervalMs [{}, {}]",
//                app, machineInfos == null ? 0 : machineInfos.size(), startTime, endTime);
//        if (CollectionUtils.isEmpty(machineInfos)) {
//            return;
//        }
//        final String msg = "fetch";
//        AtomicLong unhealthy = new AtomicLong();
//        final AtomicLong success = new AtomicLong();
//        final AtomicLong fail = new AtomicLong();
//        // app_resource_timeSecond->metric
//        final Map<String, ResourceMetric> metricMap = new ConcurrentHashMap<>(16);
//        // app_resource_ip_port_timeSecond->metric
//        final Map<String, SystemMetric> systemMetricMap = new ConcurrentHashMap<>(16);
//        final CountDownLatch latch = new CountDownLatch(machineInfos.size());
//        for (final MachineInfo machine : machineInfos) {
//            // auto remove
//            if (machine.isDead()) {
//                latch.countDown();
//                // machine移除 TODO 此处需要优化，不能直接删除，需要等待一段时间
////                machineInfoDao.registryDelete(machine.getApp(), machine.getIp(), machine.getPort());
//                LOGGER.info("Dead machine removed: {}:{} of {}", machine.getIp(), machine.getPort(), app);
//                continue;
//            }
//            if (!machine.isHealthy()) {
//                latch.countDown();
//                unhealthy.incrementAndGet();
//                continue;
//            }
//            if(app.equals("gococo-server")){
//                System.out.println("hello");
//            }
//            final String url = "http://" + machine.getIp() + ":" + machine.getPort() + "/" +
//                    METRIC_URL_PATH
//                    + "?startTime=" + startTime + "&endTime=" + endTime + "&refetch=" + false;
//            final HttpGet httpGet = new HttpGet(url);
//            httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
//            httpclient.execute(httpGet, new FutureCallback<HttpResponse>() {
//                @Override
//                public void completed(HttpResponse httpResponse) {
//                    try {
//                        handleResponse(httpResponse, machine, metricMap, systemMetricMap);
//                        success.incrementAndGet();
//                    } catch (Exception e) {
//                        LOGGER.error(msg + " metric " + url + " error:", e);
//                    } finally {
//                        latch.countDown();
//                    }
//                }
//
//                @Override
//                public void failed(Exception e) {
//                    latch.countDown();
//                    fail.incrementAndGet();
//                    httpGet.abort();
//                    if (e instanceof SocketTimeoutException) {
//                        LOGGER.error("Failed to fetch metric from <{}>: socket timeout", url);
//                    } else if (e instanceof ConnectException) {
//                        LOGGER.error("Failed to fetch metric from <{}> (ConnectionException: {})", url, e.getMessage());
//                    } else {
//                        LOGGER.error(msg + " metric " + url + " error", e);
//                    }
//
//                }
//
//                @Override
//                public void cancelled() {
//                    latch.countDown();
//                    fail.incrementAndGet();
//                    httpGet.abort();
//                }
//            });
//        }
//        try {
//            latch.await(maxWaitSeconds, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            LOGGER.info(msg + " metric, wait http client error:", e);
//        }
//        writeMetric(metricMap, systemMetricMap);
//    }
//
//    /**
//     * 度量指标写入
//     *
//     * @param map 资源Map
//     */
//    private void writeMetric(Map<String, ResourceMetric> map, Map<String, SystemMetric> systemMetricMap) {
//        if (map.isEmpty() && systemMetricMap.isEmpty()) {
//            return;
//        }
//        Date date = new Date();
//        for (ResourceMetric entity : map.values()) {
//            entity.setGmtCreate(date);
//            entity.setGmtModified(date);
//            // 更新实时统计
//            int ret = realResourceMetricDao.update(RealResourceMetric.trans(entity));
//            if (ret < 1) {
//                realResourceMetricDao.save(RealResourceMetric.trans(entity));
//            }
//        }
//        List<ResourceMetric> resourceMetrics = map.values().stream().filter(r -> !shouldFilterOut(r.getResource())).collect(Collectors.toList());
//        if (!CollectionUtils.isEmpty(resourceMetrics)) {
//            resourceMetricDao.saveList(resourceMetrics);
//        }
//        if (!CollectionUtils.isEmpty(systemMetricMap)) {
//            // 写入全局统计资源
//            systemMetricDao.saveList(new ArrayList<>(systemMetricMap.values()));
//        }
//    }
//
//    /**
//     * 响应处理
//     *
//     * @param response  请求响应
//     * @param machine   机器信息
//     * @param metricMap metricMap
//     * @throws Exception
//     */
//    private void handleResponse(final HttpResponse response, MachineInfo machine, Map<String, ResourceMetric> metricMap, Map<String, SystemMetric> systemMetricMap) throws Exception {
//        int code = response.getStatusLine().getStatusCode();
//        if (code != HTTP_OK) {
//            return;
//        }
//        Charset charset = null;
//        try {
//            String contentTypeStr = response.getFirstHeader("Content-type").getValue();
//            if (StringUtil.isNotBlank(contentTypeStr)) {
//                ContentType contentType = ContentType.parse(contentTypeStr);
//                charset = contentType.getCharset();
//            }
//        } catch (Exception ignore) {
//        }
//        String body = EntityUtils.toString(response.getEntity(), charset != null ? charset : DEFAULT_CHARSET);
//        if (StringUtil.isEmpty(body) || body.startsWith(NO_METRICS)) {
//            return;
//        }
//        String[] lines = body.split("\n");
//        handleBody(lines, machine, metricMap, systemMetricMap);
//    }
//
//    private void handleBody(String[] lines, MachineInfo machine, Map<String, ResourceMetric> map, Map<String, SystemMetric> systemMetricMap) {
//        if (lines.length < 1) {
//            return;
//        }
//        for (String line : lines) {
//            try {
//                MetricNode node = MetricNode.fromThinString(line);
//                String systemKey = buildSystemMetricKey(machine.getApp(), node.getResource(), machine.getIp(), machine.getPort(), node.getTimestamp());
//
//                if (RES_EXCLUSION_SET.contains(node.getResource())) {
//                    // aggregation metrics by app_resource_timeSecond, ignore ip and port.
//                    SystemMetric systemMetric = systemMetricMap.computeIfAbsent(systemKey, s -> {
//                        SystemMetric initResourceMetric = new SystemMetric();
//                        initResourceMetric.setApp(machine.getApp());
//                        initResourceMetric.setTs(new Date(node.getTimestamp()));
//                        initResourceMetric.setPassQps(0L);
//                        initResourceMetric.setIp(machine.getIp());
//                        initResourceMetric.setPort(machine.getPort());
//                        initResourceMetric.setResource(node.getResource());
//                        initResourceMetric.setClassification(node.getClassification());
//                        return initResourceMetric;
//                    });
//                    systemMetric.addPassQps(node.getPassQps());
//                    continue;
//                }
//                String key = buildMetricKey(machine.getApp(), node.getResource(), node.getTimestamp());
//                // aggregation metrics by app_resource_timeSecond, ignore ip and port.
//                ResourceMetric resourceMetric = map.computeIfAbsent(key, s -> {
//                    ResourceMetric initResourceMetric = new ResourceMetric();
//                    initResourceMetric.setApp(machine.getApp());
//                    initResourceMetric.setTs(new Date(node.getTimestamp()));
//                    initResourceMetric.setPassQps(0L);
//                    initResourceMetric.setBlockQps(0L);
//                    initResourceMetric.setRtAndSuccessQps(0, 0L);
//                    initResourceMetric.setExceptionQps(0L);
//                    initResourceMetric.setConcurrency(0);
//                    initResourceMetric.setResource(node.getResource());
//                    return initResourceMetric;
//                });
//                resourceMetric.addPassQps(node.getPassQps());
//                resourceMetric.addBlockQps(node.getBlockQps());
//                resourceMetric.addRtAndSuccessQps(node.getRt(), node.getSuccessQps());
//                resourceMetric.addExceptionQps(node.getExceptionQps());
//                resourceMetric.setConcurrency(node.getConcurrency());
//                resourceMetric.setTs(new Date(node.getTimestamp()));
//                resourceMetric.addCount(1);
//                resourceMetric.setClassification(node.getClassification());
//            } catch (Exception e) {
//                LOGGER.warn("handleBody line exception, machine: {}, line: {}", machine.toLogString(), line);
//            }
//        }
//    }
//
//    private String buildMetricKey(String app, String resource, long timestamp) {
//        return app + "__" + resource + "__" + (timestamp / 1000);
//    }
//
//    private String buildSystemMetricKey(String app, String resource, String ip, Integer port, long timestamp) {
//        return app + "__" + resource + "__" + ip + "__" + port + "__" + (timestamp / 1000);
//    }
//
//    private boolean shouldFilterOut(String resource) {
//        return RES_EXCLUSION_SET.contains(resource);
//    }
//
//    private static final Set<String> RES_EXCLUSION_SET = new HashSet<String>() {{
//        add(TOTAL_IN_RESOURCE_NAME);
//        add(SYSTEM_LOAD_RESOURCE_NAME);
//        add(CPU_USAGE_RESOURCE_NAME);
//    }};
//
//    /**
//     * A virtual resource identifier for total inbound statistics (since 1.5.0).
//     */
//    public final static String TOTAL_IN_RESOURCE_NAME = "__total_inbound_traffic__";
//
//    /**
//     * A virtual resource identifier for cpu usage statistics (since 1.6.1).
//     */
//    public final static String CPU_USAGE_RESOURCE_NAME = "__cpu_usage__";
//
//    /**
//     * A virtual resource identifier for system load statistics (since 1.6.1).
//     */
//    public final static String SYSTEM_LOAD_RESOURCE_NAME = "__system_load__";
//
//}
