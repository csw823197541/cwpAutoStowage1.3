package importDataProcess;

import generateResult.*;
import importDataInfo.*;
import utils.FileUtil;
import viewFrame.*;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by csw on 2016/1/21.
 */
public class Test6_30 {
    public static void main(String[] args) {

        String vo = FileUtil.readFileToString(new File("6.30data/Cwpvoyage.txt")).toString();

        String sh = FileUtil.readFileToString(new File("6.30data/vslstr.txt")).toString();

        String cr = FileUtil.readFileToString(new File("6.30data/crane.txt")).toString();

        String co = FileUtil.readFileToString(new File("6.30data/containers.txt")).toString();

        String ca = FileUtil.readFileToString(new File("6.30data/area.txt")).toString();

        //航次
        List<VoyageInfo> voyageInfoList = VoyageInfoProcess.getVoyageInfo(vo);
        VoyageFrame voyageFrame = new VoyageFrame(voyageInfoList);
        voyageFrame.setVisible(true);

        //船舶结构
        List<VesselStructureInfo> vesselStructureInfoList = VesselStructureInfoProcess.getVesselStructureInfo(sh);
        ImportData.vesselStructureInfoList = vesselStructureInfoList;
        VesselStructureFrame vesselStructureFrame = new VesselStructureFrame(vesselStructureInfoList);
        vesselStructureFrame.setVisible(true);

        //测试产生查询倍位绝对坐标的方法
        Map<String, Double> bayPositionMap = GenerateBayPositionQuery.getBayPositionMap(voyageInfoList, vesselStructureInfoList);


//        //桥机
        List<CraneInfo> craneInfoList = CraneInfoProcess.getCraneInfo(cr);
        CraneFrame craneFrame = new CraneFrame(craneInfoList);
        craneFrame.setVisible(true);

//        //在场箱
        List<ContainerInfo> containerInfoList = ContainerInfoProcess.getContainerInfo(co);
        ContainerFrame containerFrame = new ContainerFrame(containerInfoList);
        containerFrame.setVisible(true);

//        //箱区
        List<ContainerAreaInfo> containerAreaInfoList = ContainerAreaInfoProcess.getContainerAreaInfo(ca);
        ContainerAreaFrame containerAreaFrame = new ContainerAreaFrame(containerAreaInfoList);
        containerAreaFrame.setVisible(true);

//        //属性组
        List<GroupInfo> groupInfoList = GenerateGroupResult.getGroupResult(containerInfoList);
        GroupFrame groupFrame = new GroupFrame( groupInfoList);
        groupFrame.setVisible(true);

        //实配图
        String pr = FileUtil.readFileToString(new File("6.30data/cwpperstowage.txt")).toString();
        List<PreStowageData> preStowageDataList = PreStowageDataProcess.getPreStowageInfo(pr);
        //测试根据实配图生成预配图
        List<PreStowageData> resultList = GeneratePreStowageFromKnowStowage6.getPreStowageResult(preStowageDataList);
        System.out.println(resultList.size());
        PreStowageDataFrame preStowageFrame2 = new PreStowageDataFrame(resultList);
        preStowageFrame2.setVisible(true);

        //调用cwp算法得到结果
        List<CwpResultInfo> cwpResultInfoList = GenerateCwpResult.getCwpResult(voyageInfoList, vesselStructureInfoList, craneInfoList, resultList);
        CwpResultFrame cwpResultFrame = new CwpResultFrame(cwpResultInfoList, craneInfoList, null);
        cwpResultFrame.setVisible(true);

        //对cwp结果进行处理，将连续作业的cwp块放到一起，以及对作业于某个舱所有的桥机进行编顺序，和某桥机作业舱的顺序
        List<CwpResultInfo> cwpResultInfoTransformList =  CwpResultInfoTransform.getTransformResult(cwpResultInfoList);

        //目前现对cwp结果进行处理，得到每一个Move的输出对象，即对现在算法结果进行拆分
        List<CwpResultMoveInfo> cwpResultInfoToMoveList = CwpResultInfoToMove.getCwpMoveInfoResult(cwpResultInfoList);
//        CwpResultMoveInfoFrame cwpResultMoveInfoFrame = new CwpResultMoveInfoFrame(cwpResultInfoToMoveList);
//        cwpResultMoveInfoFrame.setVisible(true);

        //为了测试数据，从文件中读取cwp结果
//        String cwpRe = FileUtil.readFileToString(new File("6.30data/cswRe(1).txt")).toString();
//        List<CwpResultMoveInfo> cwpResultMoveInfoList = CwpResultMoveInfoProcess.getCwpResultMoveInfoList(cwpRe);
//        CwpResultMoveInfoFrame cwpResultMoveInfoFrame1 = new CwpResultMoveInfoFrame(cwpResultMoveInfoList);
//        cwpResultMoveInfoFrame1.setVisible(true);

        //测试自动配载算法
        List<AutoStowResultInfo> autoStowInfoList = GenerateAutoStowResult.getAutoStowResult(groupInfoList, containerInfoList, containerAreaInfoList, resultList, cwpResultInfoToMoveList);

        List<MoveInfo> moveInfoList = GenerateMoveInfoResult.getMoveInfoResult(voyageInfoList, resultList, cwpResultInfoToMoveList, autoStowInfoList);
        MoveFrame moveFrame = new MoveFrame(moveInfoList);
        moveFrame.setVisible(true);

        //可视化显示配载结果
        VesselImageFrame vesselImageFrame = new VesselImageFrame(vesselStructureInfoList);
        vesselImageFrame.setVisible(true);

    }
}
