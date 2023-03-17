package com.shopee.seamiter.controller;

import com.shopee.seamiter.config.AdminConfig;
import com.shopee.seamiter.domain.SystemMetric;
import com.shopee.seamiter.service.ResourceMetricService;
import com.shopee.seamiter.util.DateUtil;
import com.shopee.seamiter.util.Result;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 首页controller
 *
 * @author honggang.liu
 */
@RestController
@RequestMapping(value = "/")
public class IndexController {

    /**
     * 资源度量
     */
    @Resource
    private ResourceMetricService resourceMetricService;

    /**
     * 统计应用的QPS，如果时间戳为空，则取近5min数据
     *
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 统计数据
     */
    @ResponseBody
    @GetMapping("/queryAppMetric")
    public Map<String, Object> queryAppMetric(@RequestParam String app,
                                              @RequestParam(required = false) String startTimeStr,
                                              @RequestParam(required = false) String endTimeStr) throws ParseException {
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        return resourceMetricService.queryAppMetric(app, startTime, endTime);
    }

    /**
     * 单位时间内，按照top pass QPS 进行汇总排序
     *
     * @param limit        默认个数
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 过滤数据
     * @throws ParseException
     */
    @ResponseBody
    @GetMapping("/queryTopQpsPass")
    public Map<String, Object> queryTopQpsPass(@RequestParam(required = false, defaultValue = "10") Integer limit,
                                               @RequestParam(required = false) String app, @RequestParam(required = false) String startTimeStr,
                                               @RequestParam(required = false) String endTimeStr) throws ParseException {
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        return Result.ofMap(resourceMetricService.queryTopQpsPass(limit, app, startTime, endTime), limit);
    }

    /**
     * 单位时间内，按照top success QPS 进行汇总排序
     *
     * @param limit        默认个数
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 过滤数据
     * @throws ParseException
     */
    @ResponseBody
    @GetMapping("/queryTopQpsSuccess")
    public Map<String, Object> queryTopQpsSuccess(@RequestParam(required = false, defaultValue = "10") Integer limit,
                                                  @RequestParam(required = false) String app, @RequestParam(required = false) String startTimeStr,
                                                  @RequestParam(required = false) String endTimeStr) throws ParseException {
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        return Result.ofMap(resourceMetricService.queryTopQpsSuccess(limit, app, startTime, endTime), limit);
    }

    /**
     * 单位时间内，按照top rt 进行排序
     *
     * @param limit        默认个数
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 过滤数据
     * @throws ParseException
     */
    @ResponseBody
    @GetMapping("/queryTopRt")
    public Map<String, Object> queryTopRt(@RequestParam(required = false, defaultValue = "10") Integer limit,
                                          @RequestParam(required = false) String app, @RequestParam(required = false) String startTimeStr,
                                          @RequestParam(required = false) String endTimeStr) throws ParseException {
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        return Result.ofMap(resourceMetricService.queryTopRt(limit, app, startTime, endTime), limit);
    }

    /**
     * 查询应用的Rt，如果时间戳为空，则取近5min数据
     *
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 统计数据
     */
    @ResponseBody
    @GetMapping("/queryAppRt")
    public Map<String, Object> queryAppRt(@RequestParam String app,
                                          @RequestParam(required = false) String startTimeStr,
                                          @RequestParam(required = false) String endTimeStr) throws ParseException {
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        return resourceMetricService.queryAppRt(app, startTime, endTime);
    }

    /**
     * 查询异常统计，如果时间戳为空，则取近5min数据
     *
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 统计数据
     */
    @ResponseBody
    @GetMapping("/queryExceptionMetric")
    public Map<String, Object> queryExceptionMetric(@RequestParam String app,
                                                    @RequestParam(required = false) String startTimeStr,
                                                    @RequestParam(required = false) String endTimeStr) throws ParseException {
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }

        return resourceMetricService.queryExceptionMetric(app, startTime, endTime);
    }

    /**
     * 查询应用的CPU，如果时间戳为空，则取近5min数据
     *
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 统计数据
     */
    @ResponseBody
    @GetMapping("/queryAppCpu")
    public Map<String, Object> queryAppCpu(@RequestParam String app,
                                           @RequestParam(required = false) String startTimeStr,
                                           @RequestParam(required = false) String endTimeStr) throws ParseException {
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        return resourceMetricService.queryAppCpu(app, startTime, endTime);
    }

    /**
     * 查询应用的CPU，如果时间戳为空，则取近5min数据
     *
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 统计数据
     */
    @ResponseBody
    @GetMapping("/queryAppStatus")
    public Map<String, Object> queryAppStatus(@RequestParam String app,
                                              @RequestParam(required = false) String startTimeStr,
                                              @RequestParam(required = false) String endTimeStr) throws ParseException {
        // TODO
        endTimeStr = "2022-07-06 08:50:04";
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        return resourceMetricService.queryAppStatus(app, startTime, endTime);
    }

    /**
     * 单位时间内，按照top CPU 进行排序
     *
     * @param limit        默认个数
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 过滤数据
     * @throws ParseException
     */
    @ResponseBody
    @GetMapping("/queryTopCpu")
    public Map<String, Object> queryTopCpu(@RequestParam(required = false, defaultValue = "10") Integer limit,
                                           @RequestParam(required = false) String app, @RequestParam(required = false) String startTimeStr,
                                           @RequestParam(required = false) String endTimeStr) throws ParseException {
        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = new Date();
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        List<SystemMetric> systemMetrics = resourceMetricService.queryTop3Cpu(app, startTime, endTime);
        if (CollectionUtils.isEmpty(systemMetrics)) {
            return Result.ofMap(Collections.emptyList(), 0);
        }
        systemMetrics = systemMetrics.stream().filter(r -> {
            r.setUsage(String.format("%.2f", (float) r.getPassQps() / 10000));
            return true;
        }).collect(Collectors.toList());
        return Result.ofMap(systemMetrics, systemMetrics.size());
    }

    /**
     * 单位时间内，按照top CPU 进行排序
     *
     * @param limit        默认个数
     * @param app          应用名称
     * @param startTimeStr 开始时间
     * @param endTimeStr   截止时间
     * @return 过滤数据
     * @throws ParseException
     */
    @ResponseBody
    @GetMapping("/queryProtectEvent")
    public Map<String, Object> queryProtectEvent(@RequestParam(required = false, defaultValue = "10") Integer limit,
                                                 @RequestParam(required = false) String app, @RequestParam(required = false) String startTimeStr,
                                                 @RequestParam(required = false) String endTimeStr) throws ParseException {
        class ProtectEvent {
            private String eventType;
            private String gmtCreateTime;

            public void setEventType(String eventType) {
                this.eventType = eventType;
            }

            public String getEventType() {
                return this.eventType;
            }

            public void setGmtCreateTime(String gmtCreateTime) {
                this.gmtCreateTime = gmtCreateTime;
            }

            public String getGmtCreateTime() {
                return this.gmtCreateTime;
            }
        }
        List<ProtectEvent> protectEventList = new ArrayList<>();
        ProtectEvent p1 = new ProtectEvent();
        p1.setEventType("限流");
        p1.setGmtCreateTime("2022-08-30 08:20:20");

        ProtectEvent p2 = new ProtectEvent();
        p2.setEventType("熔断");
        p2.setGmtCreateTime("2022-08-30 08:21:20");

        protectEventList.add(p1);
        protectEventList.add(p2);
        return Result.ofMap(protectEventList, 2);
    }

    @GetMapping("env")
    @ResponseBody
    public Map<String, Object> env() {
        Map<String, Object> maps = new HashMap<>(2);
        // 消息
        maps.put("code", Result.SUCCESS_CODE);
        maps.put("data", AdminConfig.getAdminConfig().getEnv());
        return maps;
    }

}
