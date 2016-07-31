package mog.test;

import generateResult.GenerateBayPositionQuery;
import generateResult.GenerateGroupResult;
import generateResult.GeneratePreStowageFromKnowStowage6;
import importDataInfo.*;
import importDataProcess.*;
import mog.entity.MOContainer;
import mog.entity.MOSlot;
import mog.entity.MOSlotBlock;
import mog.entity.MOSlotPosition;
import mog.processType.PTChooser;
import utils.FileUtil;
import viewFrame.*;

import java.io.File;
import java.util.*;

/**
 * Created by csw on 2016/7/31 15:41.
 * Explain:
 */
public class MoveOrderTest {

    public static void main(String[] args) {
        String vo = FileUtil.readFileToString(new File("7.20data/Cwpvoyage.txt")).toString();

        String sh = FileUtil.readFileToString(new File("7.20data/vslstr.txt")).toString();

        String cr = FileUtil.readFileToString(new File("7.20data/crane.txt")).toString();

        String co = FileUtil.readFileToString(new File("7.20data/containers.txt")).toString();

        String ca = FileUtil.readFileToString(new File("7.20data/area.txt")).toString();

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
        String pr = FileUtil.readFileToString(new File("7.20data/cwpperstowage.txt")).toString();
        List<PreStowageData> preStowageDataList = PreStowageDataProcess.getPreStowageInfo(pr);

//        //去掉过境的箱子
//        List<PreStowageData> preStowageDataListNew = new ArrayList<>();
//        for(PreStowageData preStowageData : preStowageDataList) {
//            if(preStowageData.getTHROUGHFLAG().equals("N") || preStowageData.getTHROUGHFLAG() == null) {
//                preStowageDataListNew.add(preStowageData);
//            }
//        }
        System.out.println("总共有多少个位置：" + preStowageDataList.size());
        //将数据放在不同的舱位里
        List<String> VHTIDs = new ArrayList<>();//存放舱位ID
        for(PreStowageData preStowageData : preStowageDataList) {
            if(!VHTIDs.contains(preStowageData.getVHTID())) {
                VHTIDs.add(preStowageData.getVHTID());
            }
        }
        Collections.sort(VHTIDs);
        System.out.println( "舱位数：" + VHTIDs.size());

        Map<String, List<PreStowageData>> stringListMap1 = new HashMap<>();//放在不同的舱位的预配数据
        Map<String, List<VesselStructureInfo>> stringListMap2 = new HashMap<>();//放在不同的舱位的船舶结构数据
        for(String str : VHTIDs) {
            List<PreStowageData> dataList1 = new ArrayList<>();
            for(PreStowageData preStowageData : preStowageDataList) {
                if(str.equals(preStowageData.getVHTID())) {
                    dataList1.add(preStowageData);
                }
            }
            stringListMap1.put(str, dataList1);
            //不同舱位的船舶结构
            List<VesselStructureInfo> dataList2 = new ArrayList<>();
            for(VesselStructureInfo vesselStructureInfo : vesselStructureInfoList) {
                if(str.equals(vesselStructureInfo.getVHTID())) {
                    dataList2.add(vesselStructureInfo);
                }
            }
            stringListMap2.put(str, dataList2);
        }

//        for(String str : VHTIDs) {//逐舱遍历
            List<PreStowageData> preStowageList = stringListMap1.get("40066");
            List<VesselStructureInfo> vesselStructureList = stringListMap2.get("40066");

            //根据船舶结构初始化block
            List<MOSlotPosition> moSlotPositionList = new ArrayList<>();
            for(VesselStructureInfo vesselStructureInfo : vesselStructureList) {
                int bayInt = Integer.valueOf(vesselStructureInfo.getVBYBAYID());
                int rowInt = Integer.valueOf(vesselStructureInfo.getVRWROWNO());
                int tierInt = Integer.valueOf(vesselStructureInfo.getVTRTIERNO());
                String poss = bayInt + "." + rowInt + "." + tierInt;
                moSlotPositionList.add(new MOSlotPosition(bayInt, rowInt, tierInt));
            }
            MOSlotBlock moSlotBlock = MOSlotBlock.buildEmptyMOSlotBlock(moSlotPositionList);

            //将预配的数据初始化到block里面去
            for(PreStowageData preStowageData : preStowageList) {
                int bayInt = Integer.valueOf(preStowageData.getVBYBAYID());
                int rowInt = Integer.valueOf(preStowageData.getVRWROWNO());
                int tierInt = Integer.valueOf(preStowageData.getVTRTIERNO());
                MOSlotPosition moSlotPosition = new MOSlotPosition(bayInt, rowInt, tierInt);
                String containerNo = preStowageData.getContainerNum();
                String type = preStowageData.getCTYPECD();
                int weightKg = 0;
                String eof = null;
                String size = preStowageData.getSIZE();
                MOContainer moContainer = new MOContainer(containerNo, type, weightKg, eof, size);
                MOSlot moSlot = new MOSlot(moSlotPosition);
                moSlotBlock.putMOSlot(moSlotPosition, moSlot);
                moSlotBlock.putMOContainer(moSlotPosition, moContainer);
            }

            //开始调用block生成作业工艺的方法
            PTChooser ptChooser = new PTChooser();
            ptChooser.setMoSlotBlock(moSlotBlock);
            moSlotBlock = ptChooser.choosePT();

//        }


//        PreStowageDataFrame preStowageFrame2 = new PreStowageDataFrame(resultList);
//        preStowageFrame2.setVisible(true);
    }
}
