package mog.processOrder;

import mog.entity.*;
import mog.processType.WorkType;

import java.util.*;

/**
 * Created by csw on 2016/8/2 9:31.
 * Explain: 编写作业序列
 */
public class POChooser {

    //判断MOSlotBlock中所有箱子是否都被编序
    public boolean isAllMOSlotStackEmpty(MOSlotBlock moSlotBlock) {
        boolean result = false;

        for (MOSlotPosition moSlotPosition: moSlotBlock.getSlotPositions()) {
            MOSlot moSlot = moSlotBlock.getMOSlot(moSlotPosition);
            if (moSlot != null) {
                if(moSlot.getMoveType() != null) {
                    if (moSlot.getMoveOrderSeq() == -1) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    //判断栈顶有没有该作业工艺的slot
    public boolean isContinueSameTP(WorkType wt, MOSlotBlock moSlotBlock) {
        boolean result = false;

        Map<Integer, MOSlotStack> bay01 = moSlotBlock.getBay01();
        Map<Integer, MOSlotStack> bay03 = moSlotBlock.getBay03();

        for (MOSlotStack moSlotStack : bay01.values()) {
            MOSlot moSlot = moSlotStack.getTopMOSlot();
            if (moSlot != null) {
                if (moSlot.getMoveOrderSeq() == -1) {//没有编过MoveOrder
                    MOContainer moContainer = moSlot.getMoContainer();
                    Set<MOSlotPosition> moSlotPositionSet = moSlot.getMoSlotPositionSet();
                    if(moContainer != null && !moSlotPositionSet.isEmpty()) {
                        if (wt.size.equals(moContainer.size) && wt.n == moSlotPositionSet.size()) {
                            result = true;
                        }
                    }
                }
            }
        }

        for (MOSlotStack moSlotStack : bay03.values()) {
            MOSlot moSlot = moSlotStack.getTopMOSlot();
            if (moSlot != null) {
                if (moSlot.getMoveOrderSeq() == -1) {//没有编过MoveOrder
                    MOContainer moContainer = moSlot.getMoContainer();
                    Set<MOSlotPosition> moSlotPositionSet = moSlot.getMoSlotPositionSet();
                    if(moContainer != null && !moSlotPositionSet.isEmpty()) {
                        if (wt.size.equals(moContainer.size) && wt.n == moSlotPositionSet.size()) {
                            result = true;
                        }
                    }
                }
            }
        }

        return result;
    }

    //处理卸船的编MoveOrder过程
    public MOSlotBlock processD(Integer seq, WorkType wt, Map<Integer, MOSlotStack> bay, MOSlotBlock moSlotBlock) {

        //按从偶数排开始遍历栈顶
        for (int j = 0; j < moSlotBlock.getRowSeqList().size(); j++) {
            int row = moSlotBlock.getRowSeqList().get(j);
            MOSlotStack moSlotStack = bay.get(row);
            MOSlot moSlotTop = moSlotStack.getTopMOSlot();
            if (moSlotTop != null) {
                if (moSlotTop.getMoveOrderSeq() == -1) {
                    MOContainer moContainer = moSlotTop.getMoContainer();
                    Set<MOSlotPosition> moSlotPositionSet = moSlotTop.getMoSlotPositionSet();
                    if(moContainer != null && !moSlotPositionSet.isEmpty()) {
                        if (wt.size.equals(moContainer.size) && wt.n == moSlotPositionSet.size()) {
                            //从moSlotPositionSet中先获取slot，然后编序编MoveOrder
                            for (MOSlotPosition moSlotPosition : moSlotPositionSet) {
                                MOSlot moSlot = moSlotBlock.getMOSlot(moSlotPosition);
                                moSlot.setMoveOrderSeq(seq);
                            }
                            seq++;
                            //编完序后，栈顶标记下移
                            moSlotStack.topTierNoDownBy2();
                        }
                    }
                } else {//已经编了序号，下移栈顶
                    moSlotStack.topTierNoDownBy2();
                }
            }
        }

        return moSlotBlock;
    }

    public MOSlotBlock processOrderAD(MOSlotBlock moSlotBlock, Integer seq) {

        Map<Integer, MOSlotStack> bay01 = moSlotBlock.getBay01();
        Map<Integer, MOSlotStack> bay03 = moSlotBlock.getBay03();

        WorkType[] workTypes = new WorkType[]{new WorkType(1, "20"), new WorkType(2, "20"),
                new WorkType(1, "40"), new WorkType(2, "40")};

        while (isAllMOSlotStackEmpty(moSlotBlock)) {
            //对栈顶元素进行编序
            for (int i = 0; i < workTypes.length; i++) {
                WorkType wt = workTypes[i];
                while (isContinueSameTP(wt, moSlotBlock)) {
//                    this.processD(seq, wt, bay01, moSlotBlock);
//                    this.processD(seq, wt, bay03, moSlotBlock);
                    //按从偶数排开始遍历栈顶
                    for (int j = 0; j < moSlotBlock.getRowSeqList().size(); j++) {
                        int row = moSlotBlock.getRowSeqList().get(j);
                        MOSlotStack moSlotStack = bay01.get(row);
                        MOSlot moSlotTop = moSlotStack.getTopMOSlot();
                        if (moSlotTop != null) {
                            if (moSlotTop.getMoveOrderSeq() == -1) {
                                MOContainer moContainer = moSlotTop.getMoContainer();
                                Set<MOSlotPosition> moSlotPositionSet = moSlotTop.getMoSlotPositionSet();
                                if(moContainer != null && !moSlotPositionSet.isEmpty()) {
                                    if (wt.size.equals(moContainer.size) && wt.n == moSlotPositionSet.size()) {
                                        //从moSlotPositionSet中先获取slot，然后编序编MoveOrder
                                        for (MOSlotPosition moSlotPosition : moSlotPositionSet) {
                                            MOSlot moSlot = moSlotBlock.getMOSlot(moSlotPosition);
                                            moSlot.setMoveOrderSeq(seq);
                                        }
                                        seq++;
                                        //编完序后，栈顶标记下移
                                        moSlotStack.topTierNoDownBy2();
                                    }
                                }
                            } else {//已经编了序号，下移栈顶
                                moSlotStack.topTierNoDownBy2();
                            }
                        }
                    }
                    //按从偶数排开始遍历栈顶
                    for (int j = 0; j < moSlotBlock.getRowSeqList().size(); j++) {
                        int row = moSlotBlock.getRowSeqList().get(j);
                        MOSlotStack moSlotStack = bay03.get(row);
                        MOSlot moSlotTop = moSlotStack.getTopMOSlot();
                        if (moSlotTop != null) {
                            if (moSlotTop.getMoveOrderSeq() == -1) {
                                MOContainer moContainer = moSlotTop.getMoContainer();
                                Set<MOSlotPosition> moSlotPositionSet = moSlotTop.getMoSlotPositionSet();
                                if(moContainer != null && !moSlotPositionSet.isEmpty()) {
                                    if (wt.size.equals(moContainer.size) && wt.n == moSlotPositionSet.size()) {
                                        //从moSlotPositionSet中先获取slot，然后编序编MoveOrder
                                        for (MOSlotPosition moSlotPosition : moSlotPositionSet) {
                                            MOSlot moSlot = moSlotBlock.getMOSlot(moSlotPosition);
                                            moSlot.setMoveOrderSeq(seq);
                                        }
                                        seq++;
                                        //编完序后，栈顶标记下移
                                        moSlotStack.topTierNoDownBy2();
                                    }
                                }
                            } else {//已经编了序号，下移栈顶
                                moSlotStack.topTierNoDownBy2();
                            }
                        }
                    }
                }
            }
        }

        return moSlotBlock;
    }

    public MOSlotBlock processOrderBD(MOSlotBlock moSlotBlock, int seq) {

        return moSlotBlock;
    }

    public MOSlotBlock processOrderBL(MOSlotBlock moSlotBlock, int seq) {

        Map<Integer, MOSlotStack> bay01 = moSlotBlock.getBay01();
        Map<Integer, MOSlotStack> bay03 = moSlotBlock.getBay03();

        WorkType[] workTypes = new WorkType[]{new WorkType(1, "20"), new WorkType(2, "20"),
                new WorkType(1, "40"), new WorkType(2, "40")};


        return moSlotBlock;
    }

    public MOSlotBlock processOrderAL(MOSlotBlock moSlotBlock) {

        return moSlotBlock;
    }
}
