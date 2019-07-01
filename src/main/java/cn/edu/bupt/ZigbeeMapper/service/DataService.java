package cn.edu.bupt.ZigbeeMapper.service;

import cn.edu.bupt.ZigbeeMapper.data.Device;
import cn.edu.bupt.ZigbeeMapper.data.Gateway;
import cn.edu.bupt.ZigbeeMapper.method.GatewayMethod;
import cn.edu.bupt.ZigbeeMapper.method.GatewayMethodImpl;
import cn.edu.bupt.ZigbeeMapper.transform.Tool;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DataService {

    private static List<Object> list = new LinkedList<Object>();

    private GatewayMethod gatewayMethod = new GatewayMethodImpl();

    public static void cleatList() {
        list.clear();
    }

    Integer length;
    String shortAddress;
    int endPoint;
    String[] shortAddresses;
    int[] endPoints;
    int taskNameLength;

    public void resolution(byte[] bytes,String IP,DeviceTokenRelationService deviceTokenRelationService) throws Exception {
        System.out.println("进入");
        byte Response = bytes[0];
        switch (Response) {
            case 0x01:
//                System.out.println(Tool.bytesToHexString(bytes));
                Device device = new Device();
                length = Integer.parseInt(String.valueOf(bytes[1]));
                device.setShortAddress(byte2HexStr(Arrays.copyOfRange(bytes, 2, 4)));
                device.setEndpoint(bytes[4]);
                device.setProfileId(byte2HexStr(Arrays.copyOfRange(bytes, 5, 7)));
                device.setDeviceId(byte2HexStr(Arrays.copyOfRange(bytes, 7, 9)));
                device.setState(bytes[9]==0x01);
                Integer nameLength = Integer.parseInt(String.valueOf(bytes[10]));
                if(nameLength==0){
                    device.setName("");
                }else{
                    device.setName(bytesToAscii(Arrays.copyOfRange(bytes, 11, 11+nameLength)));
                }
                device.setOnlineState(bytes[11+nameLength]);
                device.setIEEE(byte2HexStr(Arrays.copyOfRange(bytes, 12+nameLength, 20+nameLength)));
                Integer snLength = Integer.parseInt(String.valueOf(bytes[20+nameLength]));
                if(snLength==0){
                    device.setSnid("");
                }else {
                    device.setSnid(bytesToAscii(Arrays.copyOfRange(bytes, 21+nameLength, 21+nameLength+snLength)));
                }
                device.setZoneType( byte2HexStr(Arrays.copyOfRange(bytes, 21+nameLength+snLength, 23+nameLength+snLength)));
                device.setElectric(Double.valueOf(String.valueOf(bytes[23+nameLength+snLength])));
                device.setRecentState(Arrays.copyOfRange(bytes, length-4, length));
                gatewayMethod.device_CallBack(device,IP,deviceTokenRelationService);
                break;
            case 0x15:
                Gateway gateway = new Gateway();
                length = Integer.parseInt(String.valueOf(bytes[1]));
                gateway.setVersion(bytesToAscii(Arrays.copyOfRange(bytes, 2, 7)));
                gateway.setSnid(byte2HexStr(Arrays.copyOfRange(bytes, 7, 11)));
                gateway.setUsername(bytesToAscii(Arrays.copyOfRange(bytes, 11, 31)));
                gateway.setPassword(bytesToAscii(Arrays.copyOfRange(bytes, 31, 51)));
                gateway.setDeviceNumber(Integer.parseInt(String.valueOf(bytes[51])));
                gateway.setGroupNumber(Integer.parseInt(String.valueOf(bytes[52])));
                gateway.setTimerNumber(Integer.parseInt(String.valueOf(bytes[53])));
                gateway.setSceneNumber(Integer.parseInt(String.valueOf(bytes[54])));
                gateway.setMissionNumber(Integer.parseInt(String.valueOf(bytes[55])));
                gateway.setCompileVersionNumber(byte2HexStr(Arrays.copyOfRange(bytes, 61, 65)));
                System.out.println("完成解析");
                gatewayMethod.gateway_CallBack(gateway);
                break;
            case 0x07:
                break;
            case 0x09:
                break;
            case 0x0A:
                break;
            case 0x0B:
                break;
            case 0x0C:
                break;
            case 0x0D:
                break;
            case 0x0E:
                break;
            case 0x11:
                break;
            case 0x20:
                break;
            case 0x21:
                break;
            case 0x24:
                break;
            case 0x25:
                break;
            case 0x27:
                break;
            case 0x29:
                break;
            case (byte) 0x31:
            case (byte) 0xAF:
            case 0x70:
                Double temperature;
                Integer humidity;
                Integer pm;
                Integer alarm;
                Integer attribute_value_length;
                Integer illumination;
                Double onlineStatus;
                JsonObject data = new JsonObject();
                String sceneSelectorUseSceneId;

                length = Integer.parseInt(String.valueOf(bytes[1]));
                String shortAddress = byte2HexStr(Arrays.copyOfRange(bytes, 2, 4));
                Integer endPoint = Integer.parseInt(String.valueOf(bytes[4]));
                String clusterId = byte2HexStr(Arrays.copyOfRange(bytes, 5, 7));
//                System.out.println("clusterId: " + clusterId);
//                System.out.println(Tool.bytesToHexString(bytes));
                switch (clusterId) {
                    case "0204":  // 温度传感器上报数据
                        for (int i = 0; i < Integer.parseInt(String.valueOf(bytes[7])); i++) {
                            if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("0000")) {
                                if (bytes[10 + i * 5] == 0x29) {
                                    //System.out.println(dataBytesToInt(Arrays.copyOfRange(bytes, 11+i*5, 13+i*5)));
                                    BigDecimal b = new BigDecimal((double) dataBytesToInt(Arrays.copyOfRange(bytes, 11 + i * 5, 13 + i * 5)) / (double) 100);
                                    temperature = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    //System.out.println(temperature);
                                    data.addProperty("temperature", temperature);
                                }
                            } else if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("1100")) { // TODO 旧版本API文档表示 0204是温湿度
                                if (bytes[10 + i * 5] == 0x29) {
                                    humidity = dataBytesToInt(Arrays.copyOfRange(bytes, 11 + i * 5, 13 + i * 5));
                                    data.addProperty("humidity", humidity.doubleValue());
                                }
                            }
                        }
                        break;

                    case "0504":  // 湿度传感器上报数据
                        for (int i = 0; i < Integer.parseInt(String.valueOf(bytes[7])); i++) {
                            if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("0000")) {  // 0x0000表示湿度测量值
                                if (bytes[10 + i * 5] == 0x29) {  // 数据类型: uint16
                                    humidity = dataBytesToInt(Arrays.copyOfRange(bytes, 11 + i * 5, 13 + i * 5));
                                    data.addProperty("humidity", humidity.doubleValue());
                                }
                            }
                        }
                        break;

                    case "1504":  // PM2.5上报
                        String[] PM = {"PM1.0", "PM2.5", "PM10"};
                        for (int i = 0; i < Integer.parseInt(String.valueOf(bytes[7])); i++) {
                            if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("0100")) {
                                if (bytes[10 + i * 5] == 0x21) {
                                    pm = dataBytesToInt(Arrays.copyOfRange(bytes, 11 + i * 5, 13 + i * 5));
                                    data.addProperty("PM1.0", pm.doubleValue());
                                }
                            } else if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("0000")) {
                                if (bytes[10 + i * 5] == 0x21) {
                                    pm = dataBytesToInt(Arrays.copyOfRange(bytes, 11 + i * 5, 13 + i * 5));
                                    data.addProperty("PM2.5", pm.doubleValue());
                                }
                            } else if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("0200")) {
                                if (bytes[10 + i * 5] == 0x21) {
                                    pm = dataBytesToInt(Arrays.copyOfRange(bytes, 11 + i * 5, 13 + i * 5));
                                    data.addProperty("PM10", pm.doubleValue());
                                }
                            }
                        }
                        break;

                    case "0604":
                        for (int i = 0; i < Integer.parseInt(String.valueOf(bytes[7])); i++) {
                            if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("0000")) {
                                if (bytes[10 + i * 5] == 0x21) {
                                    alarm = Integer.parseInt(String.valueOf(bytes[11 + i * 5]));
                                    data.addProperty("PIR_status", alarm.doubleValue());
                                }
                            }
                        }
                        break;

                    case "0005":  // 报警设备
                        for (int i = 0; i < Integer.parseInt(String.valueOf(bytes[7])); i++) {
                            String message_type = byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5));
                            if (message_type.equals("8000")) {  // 有设备上传报警状态
                                if (bytes[10 + i * 5] == 0x21) {  // 属性数据类型为 uint16 -> 0xffff
//                                    alarm = dataBytesToInt(Arrays.copyOfRange(bytes, 11+i*5, 13+i*5));
                                    String[] attribute_array = {"alarm", "alarm", "tamper", "battery", "surpervision", "restore", "trouble", "ac"};
                                    byte[] attribute_value = byteToBit(bytes[11 + i * 5]);
                                    if (attribute_value[0] == 0x1 || attribute_value[1] == 0x1) {  // bit0 和 bit1 表示报警状态
                                        data.addProperty("alarm", 1D);
                                    } else {
                                        data.addProperty("alarm", 0D);
                                    }
                                    for (int j = 2; j < 8; j++) {  // 暂时只考虑低位字节，高位字节全0不考虑
                                        data.addProperty(attribute_array[j], (double) attribute_value[j]);//
                                    }
                                }
                            }else if (message_type.equals("8100")) {  // 设备注册成功
                                // 数据类型 -> 这里用字节数表示（默认为无符号整型）
                                attribute_value_length = (int)(bytes[10 + i * 5]) - 31;
                                // 注册成功后的 ZoneID
                                String zone_id = byte2HexStr(Arrays.copyOfRange(bytes, 11 + i * 5, 13 + i * 5));
                                // 设备的ZoneType
                                String zone_type = byte2HexStr(Arrays.copyOfRange(bytes, 13 + i * 5, 15 + i * 5));
                                data.addProperty("zone_id", zone_id);
                                data.addProperty("zone_type", zone_type);

                            } else if (message_type.equals("0100")) { // ZoneType 返回
                                byte data_type = bytes[10 + i * 5];
                                String zone_type = byte2HexStr(Arrays.copyOfRange(bytes, 11+i*5, 13+i*5));
                                data.addProperty("zone_type", zone_type);
                            }
                        }
                        break;
                    case "0004":  // 光照传感器
                        for(int i = 0; i<Integer.parseInt(String.valueOf(bytes[7])); i++) {
                            if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("0000")) {
                                if (bytes[10 + i * 5] == 0x21) {
                                    illumination = dataBytesToInt(Arrays.copyOfRange(bytes, 11+i*5, 13+i*5));
                                    data.addProperty("illumination", illumination.doubleValue());
                                }
                            }
                        }
                        break;

                    case "F0F0":
                        for(int i = 0; i<Integer.parseInt(String.valueOf(bytes[7])); i++){
                            if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("F0F0")) {
                                if (bytes[10 + i * 5] == 0x20) {
                                    sceneSelectorUseSceneId = byte2HexStr(Arrays.copyOfRange(bytes, 11 + i * 5, 12 + i * 5))+"00";
                                    data.addProperty("sceneId", sceneSelectorUseSceneId);
                                }
                            }
                        }
                        break;

                    case "EEFB":  // 入网报告
                        for(int i = 0; i<Integer.parseInt(String.valueOf(bytes[7])); i++) {
                            if (byte2HexStr(Arrays.copyOfRange(bytes, 8 + i * 5, 10 + i * 5)).equals("D0F0")) {
                                if (bytes[10 + i * 5] == 0x20) {
                                    if(Integer.parseInt(String.valueOf(bytes[11+i*5])) == 3){
                                        onlineStatus = 1D;
                                    }else {
                                        onlineStatus = 0D;
                                    }
                                    data.addProperty("online", onlineStatus);
                                }
                            }
                        }
                        break;

                    case "0101":
                        int amount = Integer.parseInt(String.valueOf(bytes[7]));
                        if(byte2HexStr(Arrays.copyOfRange(bytes, 8 , 10)).equals("F5F0")){
                            String dataType;
                            if(bytes[10]==0x42){
                                dataType = "String";
                            }
                            int length = (int) (bytes[11] & 0xFF);
                            if(bytes[12]==0x20){
                                switch(bytes[13]){
                                    case 0x00:
                                        data.addProperty("unlock method", "password");
                                        break;
                                    case 0x02:
                                        data.addProperty("unlock method", "fingerprint");
                                        break;
                                    case 0x03:
                                        data.addProperty("unlock method", "card");
                                        break;
                                    case 0x04:
                                        data.addProperty("unlock method", "remote");
                                        break;
                                    case 0x05:
                                        data.addProperty("unlock method", "multiple ways");
                                        break;
                                }
                                data.addProperty("operate", Integer.parseInt(String.valueOf(bytes[14])));
                                data.addProperty("userId",byte2HexStr(Arrays.copyOfRange(bytes, 15 , 17)));
                                data.addProperty("eventTime", byte2HexStr(Arrays.copyOfRange(bytes, 18 , 22)));
                                int lockStateLength = (int) (bytes[22] & 0xFF);
                                byte[] lockState = byteToBit(bytes[23]);
                                String lockStateValue ="";
                                for(int i =0;i<8;i++){
                                    if(lockState[i]==0x01){
                                        switch(i){
                                            case 0:
                                                lockStateValue = lockStateValue+"|Enable the door lock to open normally";
                                                break;
                                            case 1:
                                                lockStateValue = lockStateValue+"|Disable the door lock to open normally";
                                                break;
                                            case 3:
                                                lockStateValue = lockStateValue+"|Verify the administrator to enter the menu";
                                                break;
                                            case 4:
                                                lockStateValue = lockStateValue+"|Double verification mode";
                                                break;
                                            case 7:
                                                lockStateValue = lockStateValue+"|Duress alarm";
                                                break;
                                        }
                                    }
                                }
                                data.addProperty("lockState",lockStateValue);
                            }else if(bytes[12]==0x01){
                                data.addProperty("operate",2);
                                if(bytes[13]!=0x00){
                                    return;
                                }
                            }else if(bytes[12]==0x00){
                                data.addProperty("operate",1);
                                if(bytes[13]!=0x00){
                                    return;
                                }
                            }
                        }
                        break;

                    case "0100":  // 电压值上报
                        System.out.println("bytes:" + Tool.bytesToHexString(bytes));
                        int atrribute_report_count = Integer.parseInt(String.valueOf(bytes[7]));
                        // 电池电压
                        if(byte2HexStr(Arrays.copyOfRange(bytes, 8 , 10)).equals("2000")){
                            if (bytes[10] == 0x20) {
                                double battery_voltage = ((int) (bytes[11] & 0xFF)) / 10D;
                                data.addProperty("voltage", battery_voltage);
                            }
                        }
                        // 电池电量
                        if(byte2HexStr(Arrays.copyOfRange(bytes, 12 , 14)).equals("2100")){
                            if (bytes[14] == 0x20) {
                                double electricPercent = ((int) (bytes[15] & 0xFF)) / 2D;
                                data.addProperty("electric(%)", electricPercent);
                            }
                        }
                        // 电池状态
                        if(byte2HexStr(Arrays.copyOfRange(bytes, 16 , 18)).equals("3E00")){
                            if (bytes[18] == 0x1B) {
                                // 电池欠压
                                if(byte2HexStr(Arrays.copyOfRange(bytes, 15 , 19)).equals("00000001")){
                                    data.addProperty("lowPowerAlarm",true);
                                }
                                // 电池正常
                                else if(byte2HexStr(Arrays.copyOfRange(bytes, 15 , 19)).equals("00000000")){
                                    data.addProperty("lowPowerAlarm",false);
                                }
                            }
                        }

                        break;
                    case "0900":  // TODO 0x0009 表示这条数据包指示设备的一些报警信息
                        for (int i = 0; i<Integer.parseInt(String.valueOf(bytes[7])); i++) { // 表示 x 个属性上报
                            // 报警命令帧类型 (0x0501安防遥控器，0xf5f0遥控器)
                            String alarm_frame_type = byte2HexStr(Arrays.copyOfRange(bytes, 8, 10));
                            // 剩余数据长度
                            int remaining_data_length = (int)(bytes[11]);
                            // 遥控器发出的命令类型 (0x00是 ARM 命令)
                            byte instruction_type = bytes[12];
                            // 报警模式 (0x00 撤防， 0x01在家布防， 0x02夜间布防， 0x03布防)
                            byte alarm_type = bytes[13];
                            // 密码长度
                            int password_length = bytes[14];
                            // 密码
                            String password = AsciiStringToString(byte2HexStr(Arrays.copyOfRange(bytes, 15, 15+password_length)));
                            // ZoneID
                            byte zone_id = bytes[15+password_length];
                        }
                        break;
                }
//                System.out.println(data);
//                System.out.println(endPoint);
//                System.out.println(shortAddress);
//                System.out.println("-----------------");
                break;
        }
    }


    /**
     * ASCII码字符串转数字字符串
     * @param
     * @return 字符串
     */
    public static String AsciiStringToString(String content) {
        String result = "";
        int length = content.length() / 2;
        for (int i = 0; i < length; i++) {
            String c = content.substring(i * 2, i * 2 + 2);
            int a = hexStringToAlgorism(c);
            char b = (char) a;
            String d = String.valueOf(b);
            result += d;
        }
        return result;
    }
    /**
     * 十六进制字符串转十进制
     * @param hex 十六进制字符串
     * @return 十进制数值
     */
    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }

    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
//			sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    public static byte[] byteToBit(byte n) {
        byte[] bit = new byte[8];
        for (int i = 0; i < 8; i++) {
            bit[i] = (byte) ((n >> i) & 0x1);
        }
        return bit;
    }

    public static int dataBytesToInt(byte[] src) {
        int value;
        value = (int) ((src[0] & 0xFF)
                | ((src[1] & 0xFF) << 8));
        return value;
    }

    public static String bytesToAscii(byte[] bytes) {
        return bytesToAscii(bytes, 0, bytes.length);
    }

    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
            return null;
        }
        if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
//        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            asciiStr = new String(data, "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return asciiStr;
    }

}
