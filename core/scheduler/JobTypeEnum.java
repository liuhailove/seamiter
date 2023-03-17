package com.shopee.seamiter.core.scheduler;

/**
 * job类型
 *
 * @author honggang.liu
 */
public enum JobTypeEnum {
    /**
     * 告警类型
     */
    ALARM("alarm"),
    /**
     * 报表类型
     */
    REPORT("report");

    /**
     * 标题
     */
    private String title;

    JobTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static JobTypeEnum match(String name, JobTypeEnum defaultItem) {
        for (JobTypeEnum item : JobTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }

    public static JobTypeEnum match(String name) {
        for (JobTypeEnum item : JobTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
