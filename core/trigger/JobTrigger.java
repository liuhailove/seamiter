package com.shopee.seamiter.core.trigger;

import com.shopee.seamiter.core.scheduler.JobTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * trigger
 *
 * @author honggang.liu
 */
public class JobTrigger {
    private static Logger logger = LoggerFactory.getLogger(JobTrigger.class.getSimpleName());


    public static void trigger(final Long appId,
                               final Long jobId,
                               final Long businessId,
                               final String jobType,
                               TriggerTypeEnum triggerType) {
        JobTypeEnum jobTypeEnum = JobTypeEnum.match(jobType);
        if (jobTypeEnum == null) {
            return;
        }

    }

    private static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
