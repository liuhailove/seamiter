package com.shopee.seamiter.core.metric;


import com.shopee.seamiter.core.statistic.base.WindowWrap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统计节点
 *
 * @author honggang.liu
 */
public class StatisticNode {

    /**
     * 用于统计的滑动窗口
     */
    private final BucketLeapArray rollingCounterArray;

    public StatisticNode(int sampleCount, int windowLengthInMs) {
        rollingCounterArray = new BucketLeapArray(sampleCount, windowLengthInMs * sampleCount);
    }

    /**
     * 总请求数<时间戳，请求数>
     *
     * @return 总请求数
     */
    public Map<Long, Long> totalRequest() {
        Map<Long, Long> totalRequestMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Long count = totalRequestMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0L;
            }
            count += metricBucketWindowWrap.value().pass() + metricBucketWindowWrap.value().block();
            totalRequestMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        return totalRequestMap;
    }

    /**
     * 阻塞请求数<时间戳，请求数>
     *
     * @return 阻塞请求数
     */
    public Map<Long, Long> blockRequest() {
        Map<Long, Long> blockRequestMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Long count = blockRequestMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0L;
            }
            count += metricBucketWindowWrap.value().block();
            blockRequestMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        return blockRequestMap;
    }

    /**
     * 阻塞请求QPS<时间戳，请求数>
     *
     * @return 阻塞请求QPS
     */
    public Map<Long, Double> blockQps() {
        Map<Long, Double> blockQpsMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Double count = blockQpsMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0.0;
            }
            count += metricBucketWindowWrap.value().block();
            blockQpsMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        blockQpsMap.replaceAll((k, v) -> blockQpsMap.get(k) / this.rollingCounterArray.windowLengthInSecond);
        return blockQpsMap;
    }

    /**
     * 总请求QPS<时间戳，请求数>
     *
     * @return 总请求QPS
     */
    public Map<Long, Double> totalQps() {
        Map<Long, Double> totalQpsMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        Map<Long, Double> passQpsMap = passQps();
        Map<Long, Double> blockQpsMap = blockQps();
        for (Map.Entry<Long, Double> entry : passQpsMap.entrySet()) {
            totalQpsMap.put(entry.getKey(), passQpsMap.get(entry.getKey()) + blockQpsMap.get(entry.getKey()));
        }
        return totalQpsMap;
    }

    /**
     * 总成功数<时间戳，请求数>
     *
     * @return 总成功数
     */
    public Map<Long, Long> totalSuccess() {
        Map<Long, Long> totalSuccessMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Long count = totalSuccessMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0L;
            }
            count += metricBucketWindowWrap.value().success();
            totalSuccessMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        return totalSuccessMap;
    }

    /**
     * 异常QPS<时间戳，请求数>
     *
     * @return 异常QPS
     */
    public Map<Long, Double> exceptionQps() {
        Map<Long, Double> exceptionQpsMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Double count = exceptionQpsMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0.0;
            }
            count += metricBucketWindowWrap.value().exception();
            exceptionQpsMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        exceptionQpsMap.replaceAll((k, v) -> exceptionQpsMap.get(k) / this.rollingCounterArray.windowLengthInSecond);
        return exceptionQpsMap;
    }

    /**
     * 异常QPS<时间戳，请求数>
     *
     * @return 异常QPS
     */
    public Map<Long, Long> totalException() {
        Map<Long, Long> totalExceptionMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Long count = totalExceptionMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0L;
            }
            count += metricBucketWindowWrap.value().exception();
            totalExceptionMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        return totalExceptionMap;
    }

    public Map<Long, Double> passQps() {
        Map<Long, Double> passQpsMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Double count = passQpsMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0.0;
            }
            count += metricBucketWindowWrap.value().pass();
            passQpsMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        passQpsMap.replaceAll((k, v) -> passQpsMap.get(k) / this.rollingCounterArray.windowLengthInSecond);
        return passQpsMap;
    }

    public Map<Long, Long> totalPass() {
        Map<Long, Long> totalPassMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Long count = totalPassMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0L;
            }
            count += metricBucketWindowWrap.value().pass();
            totalPassMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        return totalPassMap;
    }

    public Map<Long, Double> successQps() {
        Map<Long, Double> successQpsMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Double count = successQpsMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0.0;
            }
            count += metricBucketWindowWrap.value().success();
            successQpsMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        successQpsMap.replaceAll((k, v) -> successQpsMap.get(k) / this.rollingCounterArray.windowLengthInSecond);
        return successQpsMap;
    }

    public void addPassRequest(long timestamp, long count) {
        WindowWrap<MetricBucket> metricBucket = rollingCounterArray.currentWindow(timestamp);
        metricBucket.value().addPass((int) count);
    }

    public void increaseBlockQps(long timestamp, long count) {
        WindowWrap<MetricBucket> metricBucket = rollingCounterArray.currentWindow(timestamp);
        metricBucket.value().addBlock((int) count);
    }

    public void increaseExceptionQps(long timestamp, long count) {
        WindowWrap<MetricBucket> metricBucket = rollingCounterArray.currentWindow(timestamp);
        metricBucket.value().addException((int) count);
    }

    public void increaseThreadNum(long timestamp, int count) {
        WindowWrap<MetricBucket> metricBucket = rollingCounterArray.currentWindow(timestamp);
        metricBucket.value().addThread(count);
    }

    public void addSuccess(long timestamp, int count) {
        WindowWrap<MetricBucket> metricBucket = rollingCounterArray.currentWindow(timestamp);
        metricBucket.value().addSuccess(count);
    }

    public void addRT(long timestamp, long rt) {
        WindowWrap<MetricBucket> metricBucket = rollingCounterArray.currentWindow(timestamp);
        metricBucket.value().addRT(rt);
    }

    public Map<Long, Double> avgRt() {
        Map<Long, Double> avgRtMap = new LinkedHashMap<>(rollingCounterArray.listAll().size());
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Double count = avgRtMap.get(metricBucketWindowWrap.windowStart());
            if (count == null) {
                count = 0.0;
            }
            count += metricBucketWindowWrap.value().success();
            avgRtMap.put(metricBucketWindowWrap.windowStart(), count);
        }
        for (WindowWrap<MetricBucket> metricBucketWindowWrap : rollingCounterArray.listAll()) {
            Double count = avgRtMap.get(metricBucketWindowWrap.windowStart());
            if (count == 0) {
                avgRtMap.put(metricBucketWindowWrap.windowStart(), count);
            } else {
                avgRtMap.put(metricBucketWindowWrap.windowStart(), metricBucketWindowWrap.value().rt() * 1.0 / count);
            }
        }
        return avgRtMap;
    }
}
