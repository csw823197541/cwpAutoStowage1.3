package generateResult;

import importDataInfo.*;
import importDataProcess.ImportData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Created by leko on 2016/1/22.
 */
public class GenerateMoveInfoResult {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<MoveInfo> getMoveInfoResult(List<VoyageInfo> voyageInfoList, List<PreStowageData> preStowageDataList, List<CwpResultMoveInfo> cwpResultMoveInfoList, List<AutoStowResultInfo> autoStowResultInfoList){
        List<MoveInfo> moveInfoList = new ArrayList<>();

        Map<String, AutoStowResultInfo> autoStowResult = new HashMap<>();        //自动配载结果,根据船箱位找到配载的箱子信息
        for(AutoStowResultInfo autoStowResultInfo : autoStowResultInfoList) {
            autoStowResult.put(autoStowResultInfo.getVesselPosition(), autoStowResultInfo);
        }

        Long voyId = voyageInfoList.get(0).getVOTVOYID().longValue();

        //将预配信息进行处理，根据船箱位得到卸船的箱号信息
        Map<String, PreStowageData> preStowageDataMap = new HashMap<>();
        for(PreStowageData preStowageData : preStowageDataList) {
            String bayId = preStowageData.getVBYBAYID();    //倍号
            String rowId = preStowageData.getVRWROWNO();    //排号
            String tieId = preStowageData.getVTRTIERNO();   //层号
            String vp = bayId + rowId + tieId;
            if("D".equals(preStowageData.getLDULD())) {
                if(!preStowageDataMap.containsKey(vp)) {
                    preStowageDataMap.put(vp, preStowageData);
                }
            }
        }

        List<CwpResultMoveInfo> cwpResultMoveInfoList1 = cwpResultMoveInfoList;  //cwp输出结果，以一个箱子为一条记录
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
            Date lastStartTime = null;
            try {
                lastStartTime = sdf.parse("1979-01-01 00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < valueList.size(); i++) {
                CwpResultMoveInfo cwpResultMoveInfo = valueList.get(i);
                Date startTime = cwpResultMoveInfo.getWorkingStartTime();
                if(startTime.compareTo(lastStartTime) != 0) {
                    moveID += 1;
                }
                String craneID = cwpResultMoveInfo.getCRANEID();    //桥机号
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
                moveInfo.setWorkingStartTime(cwpResultMoveInfo.getWorkingStartTime());
                moveInfo.setWorkingEndTime(cwpResultMoveInfo.getWorkingEndTime());

                AutoStowResultInfo stowResult = autoStowResult.get(vesselP);
                if(stowResult != null && "L".equals(LD)) {
                    moveInfo.setExFromPosition(stowResult.getAreaPosition());
                    moveInfo.setUnitId(stowResult.getUnitID());
                    moveInfo.setUnitLength(stowResult.getSize());
                } else {    //预配位上卸船的箱号
                    PreStowageData preStowageData = preStowageDataMap.get(vesselP);
                    moveInfo.setUnitId(preStowageData.getContainerNum());
                    moveInfo.setUnitLength(preStowageData.getSIZE());
                }
                moveInfo.setVoyId(voyId);
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
            Date currentTime = current.getWorkingStartTime();
            for(int j = i; j < valueList.size(); j++) {
                CwpResultMoveInfo min = valueList.get(j);
                Date minTime = min.getWorkingStartTime();
                if(minTime.compareTo(currentTime) < 0) {
                   current = min;
                }
            }
            returnList.add(current);
        }
        return returnList;
    }
}
