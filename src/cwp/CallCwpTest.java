package cwp;

import java.io.File;
import java.net.URI;


public class CallCwpTest {

	static {
        System.loadLibrary("cwp_to_java3");
    }
      
    public static native String callCwp(String str1, String str2, String str3, String str4, String str5, String str6);
    
    public static String cwp(String craneJsonStr, String hatchJsonStr, String moveJsonStr, String craneSize, String MoZhi, String CanImproveCoEffi) {
    	String cranes = craneJsonStr;
        String hatches = hatchJsonStr;
        String moves = moveJsonStr;
    	String str = callCwp(cranes, hatches, moves, craneSize, MoZhi, CanImproveCoEffi);
//        System.out.println(str);
        return str;
    }

}
