package generateResult;

import importDataInfo.*;
import importDataProcess.ImportData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leko on 2016/1/22.
 */
public class GenerateMoveInfoResult {

    public static List<MoveInfo> getMoveInfoResult(List<CwpResultMoveInfo> cwpResultMoveInfoList, List<AutoStowResultInfo> autoStowResultInfoList){
        List<MoveInfo> moveInfoList = new ArrayList<>();

        //Map<String,List<PreStowageData>> moveOrderRecords = ImportData.moveOrderRecords;   //根据舱和moveorder确定具体位置
        Map<String, String[]> autoStowResult = ImportData.autoStowResult;        //自动配载结果,根据船箱位找到配载的箱子信息
        List<CwpResultMoveInfo> cwpResultMoveInfoList1 = cwpResultMoveInfoList;  //cwp输出结果，以一个箱子为一条记录

        Map<String, Integer> craneOrderMap = new HashMap<>();
        Map<String, Integer> craneMoveOrderMap = new HashMap<>();

        //将数据以不同的桥机进行分组，并且按开始时间排序
        Map<String, List<CwpResultMoveInfo>> craneMap = new HashMap<>();     //桥机分组
        for(CwpResultMoveInfo cwpResultMoveInfo : cwpResultMoveInfoList1) {
            String craneId = cwpResultMoveInfo.getCRANEID();
            if(craneMap.get(craneId) == null) {
                List<CwpResultMoveInfo> cwpResultMoveInfos = new ArrayList<>();
                craneMap.put(craneId, cwpResultMoveInfos);
            }
            craneMap.get(craneId).add(cwpResultMoveInfo);
        }

        for(List<CwpResultMoveInfo> valueList : craneMap.values()) {
            //调用排序算法，按开始时间排序
            valueList = sortByStartTime(valueList);
            int moveID = 0;
            int lastStartTime = -1;
            for(int i = 0; i < valueList.size(); i++) {
                CwpResultMoveInfo cwpResultMoveInfo = valueList.get(i);
                Integer startTime = cwpResultMoveInfo.getWORKINGSTARTTIME();
                if(startTime != lastStartTime) {
                    moveID += 1;
                }
                String craneID = cwpResultMoveInfo.getCRANEID();    //桥机号
                Integer endTime = cwpResultMoveInfo.getWORKINGENDTIME();
                String LD = cwpResultMoveInfo.getLDULD();
                String moveType = cwpResultMoveInfo.getMOVETYPE();
                String vesselP = cwpResultMoveInfo.getVesselPosition();

                MoveInfo moveInfo = new MoveInfo();
                moveInfo.setBatchId(craneID);
                moveInfo.setMoveKind(LD);
                moveInfo.setVesselPosition(vesselP);
                moveInfo.setExToPosition(vesselP);
                moveInfo.setMoveId(moveID);
                moveInfo.setMoveType(moveType);
                moveInfo.setGkey(craneID + "@" + moveID);
                moveInfo.setWORKINGSTARTTIME(startTime);
                moveInfo.setWORKINGENDTIME(endTime);
                moveInfo.setWorkingStartTime(cwpResultMoveInfo.getWorkingStartTime());
                moveInfo.setWorkingEndTime(cwpResultMoveInfo.getWorkingEndTime());

                String[] stowResult = autoStowResult.get(vesselP);
                if(stowResult != null && "L".equals(LD)) {
                    moveInfo.setExFromPosition(stowResult[0]);
                    moveInfo.setUnitId(stowResult[1]);
                    moveInfo.setUnitLength(stowResult[2]);
                } else {    //预配位上卸船的箱号
                    Map<String, List<PreStowageData>> stringListMap = ImportData.preStowageDataMap;
                    if(stringListMap != null) {
                        List<PreStowageData> preStowageDataList = stringListMap.get(vesselP);
                        if(preStowageDataList != null) {
                            for(PreStowageData preStowageData : preStowageDataList) {
                                if("D".equals(preStowageData.getLDULD())) {
                                    moveInfo.setUnitId(preStowageData.getContainerNum());
                                }
                            }
                        }
                    }
                }
                moveInfoList.add(moveInfo);
                lastStartTime = startTime;
            }
        }
        return moveInfoList;
    }

    private static List<CwpResultMoveInfo> sortByStartTime(List<CwpResultMoveInfo> valueList) {

        List<CwpResultMoveInfo> returnList = new ArrayList<>();

        for(int i = 0; i < valueList.size(); i++) {
            CwpResultMoveInfo current = valueList.get(i);
            for(int j = i; j < valueList.size(); j++) {
                CwpResultMoveInfo min = valueList.get(j);
                if(current.getWORKINGSTARTTIME() > min.getWORKINGSTARTTIME()) {
                   current = min;
                }
            }
            returnList.add(current);
        }

        return returnList;
    }
}
