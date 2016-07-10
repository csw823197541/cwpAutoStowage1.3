package generateResult;

import autoStow.CallAutoStow;
import importDataInfo.*;
import importDataProcess.AutoStowInputProcess;
import importDataProcess.ImportData;
import importDataProcess.PreStowageInfoProcess;
import utils.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leko on 2016/1/22.
 */
public class GenerateAutoStowResult {

    //调用自动配载
    public static List<AutoStowResultInfo> getAutoStowResult(List<GroupInfo> groupInfoList, List<ContainerInfo> containerInfoList, List<ContainerAreaInfo> containerAreaInfoList, List<PreStowageData> preStowageDataList, List<CwpResultMoveInfo> cwpResultMoveInfoList) {
        List<AutoStowResultInfo> autoStowResultInfoList = new ArrayList<AutoStowResultInfo>();

        //处理在场箱信息
        String containerStr = PreStowageInfoProcess.getContainerString(containerInfoList);
        containerStr = containerStr.substring(0, containerStr.length() - 1);
//        containerStr = AutoStowInputProcess.getContainerJsonStr(containerInfoList);

        //处理箱区信息
        String containerAreaStr = PreStowageInfoProcess.getContainerAreaString(containerAreaInfoList);
        containerAreaStr = containerAreaStr.substring(0, containerAreaStr.length() - 1);
//        containerAreaStr = AutoStowInputProcess.getContainerAreaJsonStr(containerAreaInfoList);

        //处理预配信息
        String preStowageStr = PreStowageInfoProcess.getPreStowageString(groupInfoList, preStowageDataList);
        preStowageStr = preStowageStr.substring(0, preStowageStr.length() - 1);
//        preStowageStr = AutoStowInputProcess.getPreStowageJsonStr(preStowageDataList);

        try {//将自动配载要用的结果写在文件里，让算法去读这个文件
            FileUtil.writeToFile("toAutoStowData/Container.txt", containerStr);
            FileUtil.writeToFile("toAutoStowData/PreStowage.txt", preStowageStr);
            FileUtil.writeToFile("toAutoStowData/ContainerArea.txt", containerAreaStr);
//            FileUtil.writeToFile("toAutoStowJsonData/Container.txt", containerStr);
//            FileUtil.writeToFile("toAutoStowJsonData/PreStowage.txt", preStowageStr);
//            FileUtil.writeToFile("toAutoStowJsonData/ContainerArea.txt", containerAreaStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //处理cwp输出信息
        String cwpResultStr = PreStowageInfoProcess.getCwpResultString(cwpResultMoveInfoList);
        cwpResultStr = cwpResultStr.substring(0, cwpResultStr.length() - 1);
//        cwpResultStr = AutoStowInputProcess.getCwpResultJsonStr(cwpResultMoveInfoList);

        try {//将自动配载要用的结果写在文件里
            FileUtil.writeToFile("toAutoStowData/CwpOutput.txt", cwpResultStr);
//            FileUtil.writeToFile("toAutoStowJsonData/CwpOutput.txt", cwpResultStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String autoStowStr = null;
        if (containerStr != null && containerAreaStr != null && preStowageStr != null && cwpResultStr != null) {
            //调用自动配载算法
            autoStowStr = CallAutoStow.autoStow(containerStr, containerAreaStr, preStowageStr, cwpResultStr);

            try {//将自动配载要用的结果写在文件里
                FileUtil.writeToFile("toAutoStowData/autoStowResult.txt", autoStowStr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("自动配载算法返回的结果：" + autoStowStr);
            if (autoStowStr != null) {
                autoStowResultInfoList = getAutoStowResult(autoStowStr, preStowageDataList, containerInfoList);
            } else {
                System.out.println("自动配载算法没有返回结果！");
            }
        } else {
            System.out.println("自动配载算法需要的4个参数信息中有空的，不能调用算法！");
        }
        return autoStowResultInfoList;
    }


    public static List<AutoStowResultInfo> getAutoStowResult(String autoStowStr, List<PreStowageData> preStowageDataList, List<ContainerInfo> containerInfoList) {

        List<AutoStowResultInfo> autoStowResultInfoList = new ArrayList<>();
        Map<String, String[]> stringMap = new HashMap<>();
        try {
            Long voyId = containerInfoList.get(0).getIYCVOYID().longValue();

            String stowResult = autoStowStr;
            String[] str = stowResult.split("#");
            System.out.println(str.length);
            for (int i = 0; i < str.length; i++) {
                String[] weiZi = str[i].split(",")[0].split("%");
                String xiangHao = str[i].split(",")[1];
                String cxw = str[i].split(",")[2];
                String wz = weiZi[1] + "" + weiZi[3] + "" + weiZi[2];//key,船上的位置：倍排层
                AutoStowResultInfo autoStowResultInfo = new AutoStowResultInfo();
                autoStowResultInfo.setVoyId(voyId);
                autoStowResultInfo.setVesselPosition(wz);
                String[] value = new String[3];//value,0放箱区位置，1放箱号，2放尺寸
                if (!cxw.startsWith(" unStowed")) {
                    String[] cangxiangwei = cxw.trim().split("%");
                    value[0] = cangxiangwei[0] + "" + cangxiangwei[1] + "" + cangxiangwei[2] + "" + cangxiangwei[3];
                    value[1] = xiangHao;
                    autoStowResultInfo.setUnStowedReason("stowed ok");
                } else {
                    value[0] = "?";
                    value[1] = "?";
                    autoStowResultInfo.setUnStowedReason(cxw);
                }
                autoStowResultInfo.setAreaPosition(value[0].trim());
                autoStowResultInfo.setUnitID(value[1].trim());
                if (Integer.valueOf(weiZi[1]) % 2 != 0) {//倍为基数
                    value[2] = "20";
                } else {
                    value[2] = "40";
                }
                autoStowResultInfo.setSize(value[2]);
                autoStowResultInfoList.add(autoStowResultInfo);
                stringMap.put(wz, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImportData.autoStowResult = stringMap;
        return autoStowResultInfoList;
    }

}