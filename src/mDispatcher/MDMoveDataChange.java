package mDispatcher;

import importDataInfo.MoveInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by csw on 2016/5/31 13:04.
 * explain：
 */
public class MDMoveDataChange {

    public static List<MDMove> changeToMDMove(List<MoveInfo> moveInfoList) {
        List<MDMove> mdMoveList = new ArrayList<>();

        //遍历传入的指令，转换成发箱服务所用的指令对象
        for(MoveInfo moveInfo : moveInfoList) {

            Long voyId = moveInfo.getVoyId();
            String craneId = moveInfo.getBatchId();
            Integer moveSeq = moveInfo.getMoveId();
            Date startTime = moveInfo.getWorkingStartTime();
            Date endTime = moveInfo.getWorkingEndTime();

            List<MDBaseMove> moveList = new ArrayList<>();
            MDBaseMove mdBaseMove = new MDBaseMove();

            MDMove mdMove = new MDMove();

        }
        return mdMoveList;
    }
}
