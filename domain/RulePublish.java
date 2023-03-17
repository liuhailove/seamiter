package com.shopee.seamiter.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则发布
 *
 * @author honggang.liu
 */
public class RulePublish implements Serializable {

    /**
     * 流控规则
     */
    public static final Integer FLOW_RULE_TYPE = 1;

    /**
     * 降级规则
     */
    public static final Integer DEGRADE_RULE_TYPE = 2;

    /**
     * 热点规则
     */
    public static final Integer HOT_PARAM_RULE_TYPE = 3;

    /**
     * Mock规则
     */
    public static final Integer MOCK_RULE_TYPE = 4;

    /**
     * 系统规则
     */
    public static final Integer SYSTEM_RULE_TYPE = 5;

    /**
     * 授权规则
     */
    public static final Integer AUTHORITY_RULE_TYPE = 6;

    /**
     * 重试规则
     */
    public static final Integer RETRY_RULE_TYPE = 7;

    /**
     * 灰度规则
     */
    public static final Integer GRAY_RULE_TYPE = 8;

    /**
     * 为止规则
     */
    public static final Integer UNKNOWN_RULE_TYPE = 10000;

    public RulePublish(Integer ruleType, Date gmtCreate) {
        this.ruleType = ruleType;
        this.version = "v_0";
        this.gmtCreate = gmtCreate;
    }

    public RulePublish() {
    }

    /**
     * 默认规则
     */
    public static final RulePublish DefaultFlowRule = new RulePublish(FLOW_RULE_TYPE, new Date());
    public static final RulePublish DefaultDegradeRule = new RulePublish(DEGRADE_RULE_TYPE, new Date());
    public static final RulePublish DefaultHotParamRule = new RulePublish(HOT_PARAM_RULE_TYPE, new Date());
    public static final RulePublish DefaultMockRule = new RulePublish(MOCK_RULE_TYPE, new Date());
    public static final RulePublish DefaultSystemRule = new RulePublish(SYSTEM_RULE_TYPE, new Date());
    public static final RulePublish DefaultAuthorityRule = new RulePublish(AUTHORITY_RULE_TYPE, new Date());

    public static final RulePublish GetByRuleType(int ruleType) {
        if (ruleType == FLOW_RULE_TYPE) {
            return DefaultFlowRule;
        }
        if (ruleType == DEGRADE_RULE_TYPE) {
            return DefaultDegradeRule;
        }
        if (ruleType == HOT_PARAM_RULE_TYPE) {
            return DefaultHotParamRule;
        }
        if (ruleType == MOCK_RULE_TYPE) {
            return DefaultMockRule;
        }
        if (ruleType == SYSTEM_RULE_TYPE) {
            return DefaultSystemRule;
        }
        if (ruleType == AUTHORITY_RULE_TYPE) {
            return DefaultAuthorityRule;
        }
        return new RulePublish(UNKNOWN_RULE_TYPE, new Date());

    }

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 应用名称
     */
    private String app;

    /**
     * 规则类型
     * （1：流控规则-FLOW_RULE，
     * 2：降级规则-DEGRADE_RULE
     * 3：热点规则-HOT_PARAM_RULE，
     * 4：Mock规则-MOCK_RULE，
     * 5：系统规则-SYSTEM_RULE,
     * 6: 授权规则-AUTHORITY_RULE
     * 7: 待定）
     */
    private Integer ruleType;

    /**
     * 当前版本
     * v-time-uuid
     */
    private String version;

    /**
     * 是否已删除
     */
    private Boolean delete;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

}
