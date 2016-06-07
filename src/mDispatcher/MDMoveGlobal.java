package mDispatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by csw on 2016/5/31 8:46.
 * explain：全局变量，有一个全局的Map，和操作Map数据的方法
 */
public class MDMoveGlobal {


    //所有指令的全局变量，用Map保存，key为vesselId.hatchId.craneId.moveOrder;
    private Map<String, MDMove> mdMoveGlobalMap = new HashMap<>();

    //所有全船期的全局变量
    private Map<String, VesselDate> vesselDateMap = new HashMap<>();

    private static MDMoveGlobal mdMoveGlobal = null;

    private MDMoveGlobal() {

    }

    //线程安全的单列模式
    public static synchronized MDMoveGlobal getInstance() {

        if(mdMoveGlobal != null) {
            mdMoveGlobal = new MDMoveGlobal();
        }
        return mdMoveGlobal;
    }

    /**
     * 初始化船期全局变量
     * @param vesselDate
     */
    public synchronized void addVesselDate(VesselDate vesselDate) {

    }

    /**
     * 初始化指令对象全局变量
     * @param mdMoveList
     */
    public synchronized void addMDMove(List<MDMove> mdMoveList) {

    }

    public List<MDMove> getAllMDMove() {

        return null;
    }

    public List<MDMove> getMDMoveByCrane() {

        return null;
    }

    public List<MDMove> getMDMoveByHatch() {

        return null;
    }

    public synchronized boolean removeMDMove(MDMove mdMove) {
        boolean flag = Boolean.FALSE;

        return flag;
    }

    public synchronized boolean changeMDMoveStatus(MDMove mdMove, MDMoveStatus moveStatus) {
        boolean flag = Boolean.FALSE;

        return flag;
    }

    public synchronized List<MDMove> fetchNextMDMove() {

        return null;
    }
}
