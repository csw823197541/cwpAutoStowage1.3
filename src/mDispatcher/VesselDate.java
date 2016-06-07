package mDispatcher;

import java.util.Date;

/**
 * Created by csw on 2016/5/31 12:20.
 * explain：
 */
public class VesselDate {

    private String vesselId;    //船舶Id
    private Date planStartWorkTime;     //计划开工时间
    private Date planEndWorkTime;       //计划完工时间

    public String getVesselId() {
        return vesselId;
    }

    public void setVesselId(String vesselId) {
        this.vesselId = vesselId;
    }

    public Date getPlanStartWorkTime() {
        return planStartWorkTime;
    }

    public void setPlanStartWorkTime(Date planStartWorkTime) {
        this.planStartWorkTime = planStartWorkTime;
    }

    public Date getPlanEndWorkTime() {
        return planEndWorkTime;
    }

    public void setPlanEndWorkTime(Date planEndWorkTime) {
        this.planEndWorkTime = planEndWorkTime;
    }
}
