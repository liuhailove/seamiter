package com.shopee.seamiter.controller;

import com.shopee.seamiter.domain.Platform;
import com.shopee.seamiter.service.PlatformService;
import com.shopee.seamiter.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job role controller
 *
 * @author honggang.liu
 */
@Controller
@RequestMapping("/platform")
public class PlatformController {

    private Logger logger = LoggerFactory.getLogger(PlatformController.class.getSimpleName());

    /**
     * 平台管理
     */
    @Resource
    public PlatformService platformService;

    /**
     * 环境信息
     */
    @Value("${spring.profiles.active}")
    private String env;

    @GetMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer limit,
                                        @RequestParam(required = false) String platformName,
                                        @RequestParam(required = false) String env,
                                        @RequestParam(required = false) String region,
                                        @RequestParam(required = false) List<String> platformNames
    ) {

        int start = 0;
        int length = 10;
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        // page query
        List<Platform> list = platformService.pageList(start, length, platformName, env, region, platformNames);
        int listCount = platformService.pageListCount(start, length, platformName, env, region, platformNames);
        return Result.ofMap(list, listCount);
    }

    @GetMapping("/pageListWithEnv")
    @ResponseBody
    public Map<String, Object> pageListWithEnv(@RequestParam(required = false, defaultValue = "1") Integer page,
                                               @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {

        List<String> envs = new ArrayList<>();
        if (env.contains("dev")) {
            envs.add("dev");
            envs.add("test");
        } else if (env.contains("test")) {
            envs.add("test");
            envs.add("uat");
        } else if (env.contains("uat")) {
            envs.add("uat");
            envs.add("live");
        } else if (env.contains("live")) {
            envs.add("live");
        }
        int start = 0;
        int length = 10;
        if (page != null && limit != null) {
            length = limit;
            start = (page - 1) * limit;
        }
        List<Platform> totalList = new ArrayList<>();
        int totalCount = 0;
        for (String en : envs) {
            totalList.addAll(platformService.pageListWithEnv(start, length, en));
            totalCount += platformService.pageListCountWithEnv(start, length, en);
        }
        return Result.ofMap(totalList, totalCount);
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<?> save(@RequestBody Platform platform) {
        logger.info("PlatformController.save，param:{}", platform);
        return platformService.save(platform);
    }

    @PostMapping("/update")
    @ResponseBody
    public Result<?> update(@RequestBody Platform platform) {
        logger.info("PlatformController.update，param:{}", platform);
        return platformService.update(platform);
    }

    @GetMapping("/remove")
    @ResponseBody
    public Result<?> remove(String platformName) {
        return platformService.remove(platformName);
    }

    @GetMapping("/stop")
    @ResponseBody
    public Result<?> stop(String platformName) {
        Platform platform = platformService.loadByName(platformName);
        platform.setPlatStatus(0);
        platform.setUpdateTime(new Date());
        return platformService.update(platform);
    }

    @GetMapping("/start")
    @ResponseBody
    public Result<?> start(String platformName) {
        Platform platform = platformService.loadByName(platformName);
        platform.setPlatStatus(1);
        platform.setUpdateTime(new Date());
        return platformService.update(platform);
    }

    @GetMapping("/loadById")
    @ResponseBody
    public Result<Platform> loadById(int id) {
        Platform platform = platformService.load(id);
        return platform != null ? Result.ofSuccess(platform) : Result.ofFail(500, null);
    }

    @GetMapping("/loadByPlatformName")
    @ResponseBody
    public Result<Platform> loadByPlatformName(String platformName) {
        Platform platform = platformService.loadByName(platformName);
        return platform != null ? Result.ofSuccess(platform) : Result.ofFail();
    }
}
