package mDispatcher;

/**
 * Created by csw on 2016/5/31 10:33.
 * explain：
 */
public enum MDMoveStatus {

    WAITING("待发送", 0), SEND("已发送", 1), PROCESSING("正在执行", 2),
    COMPLETE("执行完毕", 3), PAUSE("暂停发送", 4), CANCEL("取消", 5);

    private String name;
    private Integer code;

    MDMoveStatus(String name, Integer code) {

        this.name = name;
        this.code = code;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
