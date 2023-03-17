package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.CandidateAlarmEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 候选告警时间Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface CandidateAlarmEventDao {

    /**
     * 保存
     *
     * @param candidateAlarmEvent 候选告警事件
     * @return 影响行数
     */
    int save(CandidateAlarmEvent candidateAlarmEvent);

    /**
     * 加载
     *
     * @param id 候选ID
     * @return 候选告警事件
     */
    CandidateAlarmEvent load(@Param("id") Long id);

    /**
     * 加载复合条件的一个事件
     *
     * @param alarmItemId 告警ItemId
     * @param timeAfter   大于时间
     * @return 候选告警事件
     */
    CandidateAlarmEvent loadOneBy(@Param("alarmItemId") Long alarmItemId, @Param("timeAfter") Date timeAfter);

}
