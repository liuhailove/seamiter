package com.shopee.seamiter.controller;

/**
 * metric
 *
 * @author honggang.liu
 */

import com.shopee.seamiter.service.ResourceMetricService;
import com.shopee.seamiter.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * @author leyou
 */
@Controller
@RequestMapping(value = "/metric", produces = MediaType.APPLICATION_JSON_VALUE)
public class MetricController {

    /**
     * 资源度量service
     */
    @Resource
    private ResourceMetricService resourceMetricService;

    @ResponseBody
    @RequestMapping("/queryTopResourceMetric")
    public Map<String, Object> queryTopResourceMetric(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer limit,
                                                      @RequestParam(required = false) String app, @RequestParam(required = false) String resource,
                                                      @RequestParam(required = false) String startTimeStr, @RequestParam(required = false) String endTimeStr) throws ParseException {


        Date endTime;
        Date startTime;
        if (endTimeStr == null) {
            endTime = DateUtils.addSeconds(new Date(),10);
        } else {
            endTime = DateUtils.parseDate(endTimeStr, new String[]{DateUtil.DATETIME_FORMAT});
        }
        if (startTimeStr == null) {
            startTime = DateUtils.addSeconds(endTime, -300);
        } else {
            startTime = DateUtil.parseDateTime(startTimeStr);
        }
        return resourceMetricService.queryResourceMetric((page - 1) * limit, limit, app, resource, startTime, endTime);
    }



}
