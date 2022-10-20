package enums;

/**
 * @author yeyeye
 * @Date 2022/10/19 23:33
 */
public enum SocketMsgInf {
    /**
     * 描述和状态
     */
    CONNECTION_TRUE("CONNECTION",true),
    CONNECTION_FALSE("CONNECTION",false),
    SERVER_ERROR("SERVER_ERROR",false),
    SERVER_CLOSE("ALIVE",false),
    OPERATE_NOTFOUND("OPERATE",false),
    MATCH_SUCCESS("MATCH_SUCCESS",true),
    MATCH_FAILED("MATCH_FAILED",false),
    NOT_MATCH("NOT_MATCH",false),
    JSON_ERROR("JSON_ERROR",false);



    private String name;

    private Boolean status;

    SocketMsgInf(String name, Boolean status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
