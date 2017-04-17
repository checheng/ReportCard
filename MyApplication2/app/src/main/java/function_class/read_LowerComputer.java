package function_class;

import android.os.Handler;

import java.math.BigDecimal;

import static sockettest.example.com.myapplication.Data.thehostnumber;

/**
 * Created by Administrator on 2017/2/27.
 */

public class read_LowerComputer {

    //7~41位地址请求返回之灵，界面3
    private String rquest7_41 = "03 00 07 00 23";
    //42~82位地址请求返回指令，界面4
    private String rquest42_82 = "03 00 2A 00 29";
    private String[] netInfo=new String[15],zzInfo=new String[4];
    private String zzposition;
    //90~111位地址请求返回指令，界面1
    private String rquest90_111 = "03 00 5a 00 16";
    //160~190位地址请求返回指令，界面1
    private String rquest160_190 = "03 00 a0 00 1f";
    //112~159位地址请求返回指令，界面2
    private String rquest112_159 = "03 00 70 00 30";

    //对字符串进行添加主机位至帧头，添加crc16校验至帧尾
    public String pretreatment(String s) {
        s = thehostnumber(s);
        s = crc16.crc16(s);
        return s;
    }


    //起始地址，长度，字节长度,内容
    public String theWriteCode(String address,String length){
        String mString = "03";
        StringBuffer theCommand= new StringBuffer();
        theCommand.append(thehostnumber(mString));
        theCommand.append(address).append(length);
        String s = crc16.crc16(theCommand.toString());
        return s;
    }


    /**
     * 发送请求部分
     */
    //请求具体内容
    public String theAskCode(int i) {
        String s = "";
        switch (i) {
            case 7:
                s = pretreatment(rquest7_41);
                break;
            case 42:
                s = pretreatment(rquest42_82);
                break;
            case 90:
                s = pretreatment(rquest90_111);
                break;
            case 160:
                s = pretreatment(rquest160_190);
                break;
            case 112:
                s = pretreatment(rquest112_159);
                break;
            default:
                break;
        }
        return s;
    }

    public void setContentMessage(String text) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String res = thehostnumber(rquest7_41);
                    res = crc16.crc16(res);
                 /*   boolean isSend = iBackService.sendMessage(res);//Send Content by socket*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    /**
     * 处理接收部分
     */
    private String[] form160_190 = {
            "\\", "A相", "B相", "C相", "N相",
            "系统电压", "0", "0", "0", "\\",
            "负载电流", "0", "0", "0", "0",
            "补偿电流", "0", "0", "0", "0",
            "系统电流", "0", "0", "0", "0",
            "有功功率", "0", "0", "0", "0",
            "无功功率", "0", "0", "0", "0",
            "功率因数", "0", "0", "0", "0",
            "不平衡度U", "不平衡度I", "输出功率", "系统频率", "\\",
            "0", "0", "0", "0", "\\",
    };
    private String[] form112_159 = {
            " ", "A相", "B相", "C相",
            "总畸变率I", "0", "0", "0",
            "2次畸变", "0", "0", "0",
            "3次畸变", "0", "0", "0",
            "4次畸变", "0", "0", "0",
            "5次畸变", "0", "0", "0",
            "6次畸变", "0", "0", "0",
            "7次畸变", "0", "0", "0",
            "8次畸变", "0", "0", "0",
            "9次畸变", "0", "0", "0",
            "10次畸变", "0", "0", "0",
            "11次畸变", "0", "0", "0",
            "12次畸变", "0", "0", "0",
            "13次畸变", "0", "0", "0",
            "14次畸变", "0", "0", "0",
            "15次畸变", "0", "0", "0",
            "总畸变率U", "0", "0", "0",
    };
    private String[] form90_111 = {
            "\\","单元一", "单元二","故障信息","0",
            "上电压",  "0", "0", "相序信息", "0",
            "下电压","0", "0","输入状态", "0",
            "总电压",  "0", "0","输出状态", "0",
            "温度", "0", "0","充电状态", "0",
            "稳压指令","0", "0", "运行状态", "0",
            "\\","输出状态","故障信息","重启时间","0",
            "TSC","0", "0", "周期点数","0",
            "\\","\\","\\", "模块温度","0",
    };

    private GridViewDataObject[] form42_62 = {
            new GridViewDataObject("目标\n因数", "0"),
            new GridViewDataObject("TSC下\n额定容量", "0"),
            new GridViewDataObject("TSC下\n支路数量", "0"),
            new GridViewDataObject("TSC下\n过压门限", "0"),
            new GridViewDataObject("TSC下\n过压延时", "0"),
            new GridViewDataObject("TSC下\n欠压门限", "0"),
            new GridViewDataObject("TSC下\n欠压延时", "0"),
            new GridViewDataObject("TSC下\n投切间隔", "0"),
            new GridViewDataObject("直流\n过压", "0"),
            new GridViewDataObject("过压\n延时", "0"),
            new GridViewDataObject("直流\n欠压", "0"),
            new GridViewDataObject("欠压\n延时", "0"),
            new GridViewDataObject("交流\n过压", "0"),
            new GridViewDataObject("交流\n欠压", "0"),
            new GridViewDataObject("输出\n过流", "0"),
            new GridViewDataObject("模块\n过温°", "0"),
            new GridViewDataObject("额定\n容量", "0"),
            new GridViewDataObject("平衡\n容量%", "0"),
            new GridViewDataObject("无功\n容量%", "0"),
            new GridViewDataObject("谐波\n容量%", "0"),
            new GridViewDataObject("装置\nCT比", "0"),
    };

    private GridViewDataObject [] form7_41 = {
            //7-13
            new GridViewDataObject("运行模式","0001"),new GridViewDataObject("X","0"),
            new GridViewDataObject("重启时间","0"),new GridViewDataObject("重启使能","0"),
            new GridViewDataObject("单元1使能","0"),new GridViewDataObject("单元2使能","0"),
            new GridViewDataObject("x","0"),
            //14-24
            new GridViewDataObject("参考电压","0"), new GridViewDataObject("直流给定","0"),
            new GridViewDataObject("直流比例","0"), new GridViewDataObject("直流积分","0"),
            new GridViewDataObject("重复比例","0"), new GridViewDataObject("重复积分","0"),
            new GridViewDataObject("均压比利","0"), new GridViewDataObject("均压积分","0"),
            new GridViewDataObject("超前周期","0"), new GridViewDataObject("电流倍数","0"),
            new GridViewDataObject("电流比例","0"),
            //25-29
            new GridViewDataObject("CT位置","0"),new GridViewDataObject("CT比例","0"),
            new GridViewDataObject("平衡使能","0"),new GridViewDataObject("无功使能","0"),
            new GridViewDataObject("谐波使能","0"),
            //30-40
            new GridViewDataObject("3次补偿","0"), new GridViewDataObject("4次补偿","0"), new GridViewDataObject("5次补偿","0"),
            new GridViewDataObject("6次补偿", "0"), new GridViewDataObject("7次补偿","0"),new GridViewDataObject("8次补偿","0"),
            new GridViewDataObject("9次补偿","0"),new GridViewDataObject("10次补偿","0"),new GridViewDataObject("11次补偿","0"),
            new GridViewDataObject( "12次补偿","0"), new GridViewDataObject("13次补偿","0"),
            //41
            new GridViewDataObject("全部补偿","0")
    };



    //接收到的内容
    public String[] retrunContent(String s) {
        String[] content;
        boolean match;
        try {
            //校验crc
            match = crc16.verify(s);
            int obtianlength = Integer.parseInt(s.substring(4, 6), 16);
//            Log.w("456", "长度为：" + obtianlength);
            if (match) {
                switch (obtianlength) {
                    case 62:
                        //将160~190地址位数据赋值到字符串数组
                        content = evaluateString(RetrunForRequest(s), 160);
                        return content;
                    case 44:
                        //将90~111地址位数据赋值到字符串数组
                        content = evaluateString(RetrunForRequest(s), 90);
                        return content;
                    case 96:
                        content = evaluateString(RetrunForRequest(s), 112);
                        return content;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GridViewDataObject[] retrunContentGridViewDataObject(String s) {
        GridViewDataObject[] content;
        boolean match;
        try {
            //校验crc
            match = crc16.verify(s);
            int obtianlength = Integer.parseInt(s.substring(4, 6), 16);
//            Log.w("123", "长度为：" + obtianlength);
            if (match) {
                switch (obtianlength) {
                    case 82:
                        //将42~82地址位数据赋值到字符串数组
                        content = evalueateGridViewDataObject(RetrunForRequest(s), 42);
                        return content;
                    case 70:
                        //将7~41地址位数据赋值到字符串数组
                        content = evalueateGridViewDataObject(RetrunForRequest(s), 7);
                        return content;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //将获取的帧数据进行分割，得到字符串数组结果
    public String[] RetrunForRequest(String s) {
        String[] strings90_111 = new String[22];
        String[] strings160_190 = new String[31];
        String[] strings42_82 = new String[41];
        String[] strings7_41 = new String[35];
        String[] strings112_159 = new String[48];
        try {

            //前三位为主机位、功能码，字节长度忽略不计,末尾crc16校验去除
            s = s.substring(6, s.length() - 4);
            int length = s.length();
//            Log.w("字节长度为", String.valueOf(length));
            //循环赋值到字符串数组

            if (length % 2 == 0 && length > 0) {
                if (length == 88) {
                    for (int i = 0; i < length / 4 - 1; i++) {
                        strings90_111[i] = s.substring(4 * i, 4 * i + 4);
                    }
                    return strings90_111;
                }
                if (length == 124) {
                    for (int i = 0; i < length / 4; i++) {
                        strings160_190[i] = s.substring(4 * i, 4 * i + 4);
                    }
                    return strings160_190;
                }
                if (length == 164) {
                    for (int i = 0; i < length / 4; i++) {
                        strings42_82[i] = s.substring(4 * i, 4 * i + 4);
                    }
                    return strings42_82;
                }
                if (length == 140) {
                    for (int i = 0; i < length / 4; i++) {
                        strings7_41[i] = s.substring(4 * i, 4 * i + 4);
                    }
                    return strings7_41;
                }
                if (length == 192) {
                    for (int i = 0; i < length / 4; i++) {
                        strings112_159[i] = s.substring(4 * i, 4 * i + 4);
                    }
//                    Log.w("111",strings112_159.length+"");
                    return strings112_159;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //将String数组内容填充到String数组表格中
    public String[] evaluateString(String[] strings, int address) {
        try {
            switch (address) {
                case 160:
                               /* "\\",
                                "A相",
                                "B相",
                                "C相",
                                "N相",
                                "系统电压",
                                //格式1*/
                    form160_190[6] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[0]), 0.1, 1));
                    form160_190[7] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[1]), 0.1, 1));
                    form160_190[8] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[2]), 0.1, 1));
                             /*   "\\",
                                "负载电流",
                                //格式1*/
                    form160_190[11] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[6]), 0.1, 1));
                    form160_190[12] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[7]), 0.1, 1));
                    form160_190[13] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[8]), 0.1, 1));
                    form160_190[14] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[9]), 0.1, 1));
//                                "补偿电流",
//                                //格式1
                    form160_190[16] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[10]), 0.1, 1));
                    form160_190[17] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[11]), 0.1, 1));
                    form160_190[18] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[12]), 0.1, 1));
                    form160_190[19] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[13]), 0.1, 1));
//                                "系统电流",
//                                //格式1
                    form160_190[21] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[14]), 0.1, 1));
                    form160_190[22] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[15]), 0.1, 1));
                    form160_190[23] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[16]), 0.1, 1));
                    form160_190[24] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[17]), 0.1, 1));
                            /*    "有功功率",
                                //格式1*/
                    form160_190[26] = String.valueOf(divisionRetainDecimal(plus_inus(strings[18]), 0.1, 1));
                    form160_190[27] = String.valueOf(divisionRetainDecimal(plus_inus(strings[19]), 0.1, 1));
                    form160_190[28] = String.valueOf(divisionRetainDecimal(plus_inus(strings[20]), 0.1, 1));
                    form160_190[29] = String.valueOf(divisionRetainDecimal(plus_inus(strings[21]), 0.1, 1));
                              /*  "无功功率",
                                //格式1*/
                    form160_190[31] = String.valueOf(divisionRetainDecimal(plus_inus(strings[22]), 0.1, 1));
                    form160_190[32] = String.valueOf(divisionRetainDecimal(plus_inus(strings[23]), 0.1, 1));
                    form160_190[33] = String.valueOf(divisionRetainDecimal(plus_inus(strings[24]), 0.1, 1));
                    form160_190[34] = String.valueOf(divisionRetainDecimal(plus_inus(strings[25]), 0.1, 1));
                           /*     "功率因数",
                                //格式3*/
                    form160_190[36] = String.valueOf(divisionRetainDecimal(plus_inus(strings[26]), 0.001, 3));
                    form160_190[37] = String.valueOf(divisionRetainDecimal(plus_inus(strings[27]), 0.001, 3));
                    form160_190[38] = String.valueOf(divisionRetainDecimal(plus_inus(strings[28]), 0.001, 3));
                    form160_190[39] = String.valueOf(divisionRetainDecimal(plus_inus(strings[29]), 0.001, 3));
                        /*    "不平衡度U", "不平衡度I", "输出功率", "系统频率", "\\",*/
                    form160_190[45] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[4]), 0.1, 1));
                    form160_190[46] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[5]), 0.1, 1));
//                    Log.w("ww",strings[30]+strings[29]+strings[28]);
                    form160_190[47] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[30]), 0.1, 1));
                    form160_190[48] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[3]), 0.01, 2));
                    return form160_190;
                case 90:
                           /* "\\",
                             "单元一",
                             "单元二",
                            "故障信息", */
                    form90_111[4] = strings[6];//hex类型
                          /*  "上电压", */
                    form90_111[6] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[0]), 0.1, 1));
                    form90_111[7] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[3]), 0.1, 1));
                    //       "相序信息",
                    form90_111[9] = String.valueOf(hexStringToInt(strings[7]));//整型
                    //      "下电压",10
                    form90_111[11] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[1]), 0.1, 1));
                    form90_111[12] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[4]), 0.1, 1));
                    //      "输入状态",13
                    form90_111[14] = strings[8];//hex类型
                    //     "总电压",15
                    form90_111[16] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[2]), 0.1, 1));
                    form90_111[17] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[5]), 0.1, 1));
                    //   "输出状态",18
                    form90_111[19] = strings[9];//hex类型
                    // "温度",20
                    form90_111[21] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[19]), 0.1, 1));
                    form90_111[22] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[20]), 0.1, 1));//格式1
                    //    "充电状态",23
                    form90_111[24] = strings[10];//hex类型
                    //  "稳压指令",25
                    form90_111[26] = String.valueOf(plus_inus(strings[15]));
                    form90_111[27] = String.valueOf(plus_inus(strings[16]));//整型
                    //  "运行状态",28
                    form90_111[29] = strings[11];//hex类型
                       /*     "\\",30
                             "输出状态",31
                             "故障信息",32
                            "重启时间",33
                            */
                    form90_111[34] = String.valueOf(hexStringToInt(strings[14]));//整型
                    //  "TSC",35
                    form90_111[36] = strings[12];
                    form90_111[37] = strings[13];//hex类型
                    //   "周期点数",38
                    form90_111[39] = String.valueOf(hexStringToInt(strings[17]));//整型
                    //     "\\", "\\", "\\",
                    //"模块温度",
                    form90_111[44] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[18]), 0.1, 1));//格式1
                    return form90_111;
                case 112:
                    int j = 5;
                    for (int i = 0; i < strings.length; i++) {
                        form112_159[j] = String.valueOf(divisionRetainDecimal(hexStringToInt(strings[i]), 0.1, 1));
                        j++;
                        if ((i+1) % 3 == 0 && i > 0) j++;
                    }
//                    Log.w("123",""+form112_159.length);
                    for (int i = 0; i < form112_159.length; i++) {
//                        Log.w("123",form112_159[i]);
                    }
                    return form112_159;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //将String数组内容填充到GridViewDataObject数组表格中
    public GridViewDataObject[] evalueateGridViewDataObject(String[] strings,int address){
        try {
            switch (address) {
                case 42:
                    for(int i = 0;i<form42_62.length;i++){
                        form42_62[i].setValue(String.valueOf(hexStringToInt(strings[i])));
                    }
                  /*  form42_62[0].setValue(String.valueOf(hexStringToInt(strings[1])));//TSC下\n额定容量
                    form42_62[1].setValue(String.valueOf(divisionRetainDecimal(hexStringToInt(strings[0]), 0.01, 2)));//目标\n因数 格式2
                    form42_62[2].setValue(String.valueOf(hexStringToInt(strings[16])));//额定\n容量
                    form42_62[3].setValue(String.valueOf(hexStringToInt(strings[2])));//"TSC下\n支路数量"
                    form42_62[4].setValue(String.valueOf(hexStringToInt(strings[8])));//"直流\n过压"
                    form42_62[5].setValue(String.valueOf(hexStringToInt(strings[14])));//输出\n过流
                    form42_62[6].setValue(String.valueOf(hexStringToInt(strings[3])));//TSC下\\n过压门限
                    form42_62[7].setValue(String.valueOf(hexStringToInt(strings[9])));//过压\n延时
                    form42_62[8].setValue(String.valueOf(hexStringToInt(strings[15])));//模块\n过温
                    form42_62[9].setValue(String.valueOf(hexStringToInt(strings[4])));//TSC下\n过压延时
                    form42_62[10].setValue(String.valueOf(hexStringToInt(strings[10])));//直流\n欠压
                    form42_62[11].setValue(String.valueOf(hexStringToInt(strings[17])));//平衡\n容量
                    form42_62[12].setValue(String.valueOf(hexStringToInt(strings[5])));//TSC下\n欠压门限
                    form42_62[13].setValue(String.valueOf(hexStringToInt(strings[11])));//欠压\n延时
                    form42_62[14].setValue(String.valueOf(hexStringToInt(strings[18])));//无功\n容量
                    form42_62[15].setValue(String.valueOf(hexStringToInt(strings[6])));//TSC下\\n欠压延时
                    form42_62[16].setValue(String.valueOf(hexStringToInt(strings[12])));//交流\n过压
                    form42_62[17].setValue(String.valueOf(hexStringToInt(strings[19])));//谐波\n容量
                    form42_62[18].setValue(String.valueOf(hexStringToInt(strings[7])));//TSC下\n投切间隔
                    form42_62[19].setValue(String.valueOf(hexStringToInt(strings[13])));//交流\n欠压
                    form42_62[20].setValue(String.valueOf(hexStringToInt(strings[20])));//装置\nCT比*/
                    zzposition = strings[21];//装置信息下拉单
                    zzInfo[0] = strings[22];//并联个数
                    zzInfo[1] = strings[23];//运行个数
                    zzInfo[2] = strings[24];//主地址位
                    zzInfo[3] = strings[25];//通信地址
                    for (int i =0;i<15;i++){
                        netInfo[i]=strings[i+26];
                    }
                    return form42_62;
                case 7:
                    for (int i = 0; i < 35; i++) {
                        form7_41[i].setValue(String.valueOf(hexStringToInt(strings[i])));
                    }
                    return form7_41;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getNetInfo() {
        return netInfo;
    }

    public String[] getZzInfo() {
        return zzInfo;
    }

    public String getZzposition() {
        return zzposition;
    }

    //两个十六进制字符串变为10进制int
    public int hexStringToInt(String Str) {
        String A = Str.substring(0, 2);
        String B = Str.substring(2, 4);
        int result, a, b;
        a = Integer.parseInt(A, 16);
        b = Integer.parseInt(B, 16);
        result = a * 256 + b;
        return result;
    }

     // 将两位16进制字符串转为带正负号的值
    public int plus_inus(String string) {
        int value = Integer.parseInt(string, 16);
//        Log.w("int", String.valueOf(value));
        if (value / 32768 > 0) {
            value = value % 32768 - 32768;
        }
        return value;
    }

    //除法运算，保留位数
    public double divisionRetainDecimal(int theString,double dividend,int digit){
        double f=theString*dividend;
        BigDecimal bigDecimal = new BigDecimal(f);
        double f1 = bigDecimal.setScale(digit,BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }



}
