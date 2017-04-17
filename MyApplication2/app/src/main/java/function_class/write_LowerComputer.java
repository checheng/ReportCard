package function_class;

import static sockettest.example.com.myapplication.Data.thehostnumber;

/**
 * Created by Administrator on 2017/3/2.
 */

public class write_LowerComputer {

    private StringBuffer theCommand= new StringBuffer();
    private String mString = "10";


    //起始地址，长度，字节长度,内容
    public String theWriteCode(String address,String length,String bytelength,String content){
        theCommand.append(thehostnumber(mString));
        theCommand.append(address).append(length).append(bytelength).append(content);
        String s = crc16.crc16(theCommand.toString());
        theCommand.delete(0,theCommand.length());
        return s;
    }

}
