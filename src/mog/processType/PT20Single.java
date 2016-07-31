package mog.processType;

import mog.entity.MOSlotBlock;
import mog.entity.MOSlotPosition;

/**
 * Created by liuminhang on 16/4/11.
 */
public class PT20Single implements IProcessType{
    @Override
    public boolean canDo(MOSlotPosition moSlotPosition, MOSlotBlock moSlotBlock) {
        return false;
    }
}
