package mDispatcher;

import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2016/5/31 11:05.
 * explain：接口传入或传出的指令对象,一关一个对象
 */
public class MDMove {

    private String MDMoveKey;   //MDMove唯一健值，由船舶Id.舱Id.桥机号.作业顺序组成
    private MDMoveStatus moveStatus;    //指令对象的状态
    private Integer reStartTime;    //指令执行的开始时间，相对时间
    private Integer reEndTime;  //指令执行结束时间，相对时间
    private Date startTime;     //指令执行的开始时间（绝对时间）
    private Date endTime;       //指令执行结束时间，绝对时间

    List<MDBaseMove> moveList;

    public String getMDMoveKey() {
        return MDMoveKey;
    }

    public void setMDMoveKey(String MDMoveKey) {
        this.MDMoveKey = MDMoveKey;
    }

    public MDMoveStatus getMoveStatus() {
        return moveStatus;
    }

    public void setMoveStatus(MDMoveStatus moveStatus) {
        this.moveStatus = moveStatus;
    }

    public Integer getReStartTime() {
        return reStartTime;
    }

    public void setReStartTime(Integer reStartTime) {
        this.reStartTime = reStartTime;
    }

    public Integer getReEndTime() {
        return reEndTime;
    }

    public void setReEndTime(Integer reEndTime) {
        this.reEndTime = reEndTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<MDBaseMove> getMoveList() {
        return moveList;
    }

    public void setMoveList(List<MDBaseMove> moveList) {
        this.moveList = moveList;
    }
}
