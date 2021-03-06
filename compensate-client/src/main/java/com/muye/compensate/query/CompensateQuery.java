package com.muye.compensate.query;
import java.util.Date;


/**
 *
 * <pre>
 * Author muye	2018年07月31日 Created
 * </pre>
 */
public class CompensateQuery  extends BaseQuery {

    private static final long serialVersionUID = 1L;
    
	/**
	 * 
	 */
    private Long id;

	/**
	 * 
	 */
    private Date gmtCreate;

	/**
	 * 
	 */
    private Date gmtModified;

	/**
	 * 
	 */
    private Integer rowVersion;

	/**
	 * 
	 */
    private Integer rowStatus;

	/**
	 * 
	 */
    private String extAtt;

	/**
	 * 补偿任务所在app
	 */
    private String appName;

	/**
	 * 补偿类名
	 */
    private String classFullName;

	/**
	 * 补偿方法名
	 */
    private String methodName;

	/**
	 * 参数二进制流
	 */
    private String params;

    /**
     * 参数类型
     */
    private String paramTypes;

	/**
	 * 参数序列化协议
     * 0-java默认序列化
	 */
    private Integer codecProtocol;

	/**
	 * 当前状态
     * 0-待补偿
     * 1-补偿中
     * 2-已成功
     * 3-已失败
	 */
    private Integer status;

	/**
	 * 当前尝试次数
	 */
    private Integer currentNum;

	/**
	 * 最多尝试次数
     * -1表示不限次数
	 */
    private Integer maxNum;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Integer rowVersion) {
        this.rowVersion = rowVersion;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public String getExtAtt() {
        return extAtt;
    }

    public void setExtAtt(String extAtt) {
        this.extAtt = extAtt;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(String paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Integer getCodecProtocol() {
        return codecProtocol;
    }

    public void setCodecProtocol(Integer codecProtocol) {
        this.codecProtocol = codecProtocol;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }
}