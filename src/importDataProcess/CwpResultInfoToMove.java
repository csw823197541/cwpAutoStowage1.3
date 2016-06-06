package importDataProcess;

import importDataInfo.CwpResultInfo;
import importDataInfo.CwpResultMoveInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by csw on 2016/5/27 12:28.
 * explain：
 */
public class CwpResultInfoToMove {

    public static List<CwpResultMoveInfo> getCwpMoveInfoResult(List<CwpResultInfo> cwpResultInfoListIn) {
        List<CwpResultMoveInfo> resultInfoList = new ArrayList<>();

        //舱.作业序列.作业工艺确定具体位置
        Map<String,List<String>> moveOrderRecords = ImportData.moveOrderRecords;

        for(CwpResultInfo cwpResultInfo : cwpResultInfoListIn) {

            int moveCount = cwpResultInfo.getMOVECOUNT();
            int startTime = cwpResultInfo.getREALWORKINGSTARTTIME();
            int endTime = cwpResultInfo.getWORKINGENDTIME();
            Date startTimeDate = cwpResultInfo.getWorkingStartTime();
            int singleMoveWorkTime = (endTime-startTime)/moveCount; //做一个move的时间

            String craneId = cwpResultInfo.getCRANEID();
            String hatchId = cwpResultInfo.getHATCHID();
            String vesselId = cwpResultInfo.getVESSELID();
            String moveType = cwpResultInfo.getMOVETYPE();
            String LD = cwpResultInfo.getLDULD();
            Double cranePosition = cwpResultInfo.getCranesPosition();
            String bayId = cwpResultInfo.getHATCHBWID();
            String size = Integer.valueOf(bayId)%2 == 0 ? "40" : "20";

            int startMoveOrder = cwpResultInfo.getStartMoveID();

            for(int i = 0; i < moveCount; i++) {   //有多少个move，就拆成几个对象

                int moveOrder = startMoveOrder + i;
                //舱.作业序列.作业工艺为关键字,查找船箱位
                String key = hatchId + "." + moveOrder + "." + moveType;
                List<String> vesselPosition = moveOrderRecords.get(key);
                if(vesselPosition != null) {
                    for(String vesselPositionStr : vesselPosition) {    //有多少个船箱位，结果就有多少个对象
                        CwpResultMoveInfo cwpResultMoveInfo = new CwpResultMoveInfo();

                        cwpResultMoveInfo.setMoveOrder(moveOrder);     //作业顺序
                        cwpResultMoveInfo.setWorkingStartTime(new Date(startTimeDate.getTime() + (i)*singleMoveWorkTime*1000));
                        cwpResultMoveInfo.setWorkingEndTime(new Date(startTimeDate.getTime() + (i+1)*singleMoveWorkTime*1000));
                        cwpResultMoveInfo.setWORKINGSTARTTIME(startTime + (i)*singleMoveWorkTime);
                        cwpResultMoveInfo.setWORKINGENDTIME(startTime + (i+1)*singleMoveWorkTime);
                        cwpResultMoveInfo.setMoveWorkTime(singleMoveWorkTime);

                        cwpResultMoveInfo.setCRANEID(craneId);
                        cwpResultMoveInfo.setHATCHID(hatchId);
                        cwpResultMoveInfo.setVESSELID(vesselId);
                        cwpResultMoveInfo.setMOVETYPE(moveType);
                        cwpResultMoveInfo.setLDULD(LD);
                        cwpResultMoveInfo.setSize(size);
                        cwpResultMoveInfo.setCranesPosition(cranePosition);
                        cwpResultMoveInfo.setHATCHBWID(bayId);

                        String vp = vesselPositionStr.split("\\.")[1] + "" + vesselPositionStr.split("\\.")[3] + "" + vesselPositionStr.split("\\.")[2];
                        cwpResultMoveInfo.setVesselPosition(vp);

                        resultInfoList.add(cwpResultMoveInfo);
                    }
                }

            }
        }

        return resultInfoList;

    }

    public static List<CwpResultMoveInfo> getOneMoveInfo(List<CwpResultMoveInfo> cwpResultMoveInfoList) {
        List<CwpResultMoveInfo> resultMoveInfoList = new ArrayList<>();

        List<String> hatchIdMoveOrder = new ArrayList<>();
        try{
            for(CwpResultMoveInfo cwpResultMoveInfo : cwpResultMoveInfoList) {
                String hatchId = cwpResultMoveInfo.getHATCHID();
                int moveOrder = cwpResultMoveInfo.getMoveOrder();
                String hm = hatchId + "" + moveOrder;
                if(!hatchIdMoveOrder.contains(hm)) {
                    hatchIdMoveOrder.add(hm);
                    resultMoveInfoList.add(cwpResultMoveInfo);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultMoveInfoList;
    }
}
