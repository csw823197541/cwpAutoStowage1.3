package mDispatcher;

/**
 * Created by csw on 2016/5/31 10:58.
 * explain：每个MDMove具体到哪几个箱子的对象
 */
public class MDBaseMove {

    private String containerId;     //箱号
    private String exFromPosition;      //计划提箱位置
    private String exToPosition;       //计划放箱位置

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getExFromPosition() {
        return exFromPosition;
    }

    public void setExFromPosition(String exFromPosition) {
        this.exFromPosition = exFromPosition;
    }

    public String getExToPosition() {
        return exToPosition;
    }

    public void setExToPosition(String exToPosition) {
        this.exToPosition = exToPosition;
    }
}
