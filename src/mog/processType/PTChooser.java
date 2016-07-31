package mog.processType;

import mog.entity.MOSlot;
import mog.entity.MOSlotBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuminhang on 16/4/11.
 */
public class PTChooser {

    private MOSlotBlock moSlotBlock;

    private List<IProcessType> PTSeq;

    public PTChooser() {
        PTSeq = new ArrayList<>();
        PTSeq.add(new PT20Single());
        PTSeq.add(new PT20Dual());
        PTSeq.add(new PT40Single());
        PTSeq.add(new PT40Dual());
    }

    public MOSlotBlock choosePT() { //选择作业工艺

        //Block层遍历,按层顺序
        for (int i = 0; i < moSlotBlock.getTierNoListAsc().size(); i++) {
            int tierNo = moSlotBlock.getTierNoListAsc().get(i); //获取层号
            //这层按顺序遍历
            List<MOSlot> moSlotList01Bay = moSlotBlock.getMOSlotsByTierOn01Bay(tierNo);
            List<MOSlot> moSlotList03Bay = moSlotBlock.getMOSlotsByTierOn03Bay(tierNo);

            //按序判断作业工艺
            for(MOSlot moSlot : moSlotList01Bay) {

            }
        }

        return moSlotBlock;

    }

    public MOSlotBlock getMoSlotBlock() {
        return moSlotBlock;
    }

    public void setMoSlotBlock(MOSlotBlock moSlotBlock) {
        this.moSlotBlock = moSlotBlock;
    }

}
