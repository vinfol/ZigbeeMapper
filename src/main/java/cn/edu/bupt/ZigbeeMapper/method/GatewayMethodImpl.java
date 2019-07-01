package cn.edu.bupt.ZigbeeMapper.method;

import cn.edu.bupt.ZigbeeMapper.data.Device;

import cn.edu.bupt.ZigbeeMapper.data.DeviceTokenRelation;
import cn.edu.bupt.ZigbeeMapper.data.Gateway;
import cn.edu.bupt.ZigbeeMapper.mqtt.Config;
import cn.edu.bupt.ZigbeeMapper.mqtt.DataMessageClient;
import cn.edu.bupt.ZigbeeMapper.service.DeviceTokenRelationService;
import cn.edu.bupt.ZigbeeMapper.transform.OutBoundHandler;
import cn.edu.bupt.ZigbeeMapper.transform.*;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GatewayMethodImpl extends OutBoundHandler implements GatewayMethod {

    @Autowired
    private DeviceTokenRelationService deviceTokenRelationService;

    byte[] sendMessage = new byte[1024];


    //获取网关下所有的设备
    @Override
    public void getAllDevice(String IP) throws Exception {
        byte[] bytes = new byte[8];

        int index = 0;
        bytes[index++] = (byte) 0x08;
        bytes[index++] = (byte) 0x00;

        //snid
        byte[] snid = Tool.toBytes(Tool.getSnidMap().get(IP));
        for (int i = snid.length - 1; i >= 0; i--) {
            bytes[index++] = (byte) snid[i];
        }

        bytes[index++] = (byte) 0xFE;
        bytes[index] = (byte) 0x81;

        Tool.getChannelMap().get(IP).writeAndFlush(bytes);
    }

    //获取网关信息
    @Override
    public void getGatewayInfo(String IP) throws Exception {
        byte[] bytes = new byte[8];

        int index = 0;
        bytes[index++] = (byte) 0x08;
        bytes[index++] = (byte) 0x00;

        //snid
        byte[] snid = Tool.toBytes(Tool.getSnidMap().get(IP));

        for (int i = snid.length - 1; i >= 0; i--) {
            bytes[index++] = (byte) snid[i];
        }

        bytes[index++] = (byte) 0xFE;
        bytes[index] = (byte) 0x9D;

        Tool.getChannelMap().get(IP).writeAndFlush(bytes);
    }

    //获取设备状态
    public void getDeviceState(Device device, String IP) {
        byte[] bytes = new byte[21];

        int index = 0;
        bytes[index++] = (byte) 0x15;
        bytes[index++] = (byte) 0x00;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFE;
        bytes[index++] = (byte) 0x85;
        bytes[index++] = (byte) 0x0C;
        bytes[index++] = (byte) 0x02;
        System.arraycopy(Tool.toBytes(device.getShortAddress()), 0, bytes, index, Tool.toBytes(device.getShortAddress()).length);
        index = index + Tool.toBytes(device.getShortAddress()).length;
        for (int i = 0; i < 6; i++) {
            bytes[index++] = (byte) 0x00;
        }
        bytes[index++] = device.getEndpoint();
        for (int i = 0; i < 2; i++) {
            bytes[index++] = (byte) 0x00;
        }
        Tool.getChannelMap().get(IP).writeAndFlush(bytes);
    }

    @Override
    public void getDeviceBright(Device device, String IP) {
        byte[] bytes = new byte[21];

        int index = 0;
        bytes[index++] = (byte) 0x15;
        bytes[index++] = (byte) 0x00;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFE;
        bytes[index++] = (byte) 0x86;
        bytes[index++] = (byte) 0x0C;
        bytes[index++] = (byte) 0x02;
        System.arraycopy(Tool.toBytes(device.getShortAddress()), 0, bytes, index, Tool.toBytes(device.getShortAddress()).length);
        index = index + Tool.toBytes(device.getShortAddress()).length;
        for (int i = 0; i < 6; i++) {
            bytes[index++] = (byte) 0x00;
        }
        bytes[index++] = device.getEndpoint();
        for (int i = 0; i < 2; i++) {
            bytes[index++] = (byte) 0x00;
        }

        sendMessage = Tool.getSendContent(12, bytes);
        Tool.getChannelMap().get(IP).writeAndFlush(sendMessage);
    }

    @Override
    public void getDeviceHue(Device device, String IP) {
        byte[] bytes = new byte[21];

        int index = 0;
        bytes[index++] = (byte) 0x15;
        bytes[index++] = (byte) 0x00;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFF;
        bytes[index++] = (byte) 0xFE;
        bytes[index++] = (byte) 0x87;
        bytes[index++] = (byte) 0x0C;
        bytes[index++] = (byte) 0x02;
        System.arraycopy(Tool.toBytes(device.getShortAddress()), 0, bytes, index, Tool.toBytes(device.getShortAddress()).length);
        index = index + Tool.toBytes(device.getShortAddress()).length;
        for (int i = 0; i < 6; i++) {
            bytes[index++] = (byte) 0x00;
        }
        bytes[index++] = device.getEndpoint();
        for (int i = 0; i < 2; i++) {
            bytes[index++] = (byte) 0x00;
        }

        sendMessage = Tool.getSendContent(12, bytes);
        Tool.getChannelMap().get(IP).writeAndFlush(sendMessage);
    }

    //获取设备信息
    @Override
    public void getDeviceInfo(Device device, String IP) {
        byte[] bytes = new byte[21];

        int index = 0;
        bytes[index++] = (byte) 0x15;
        bytes[index++] = (byte) 0x00;
        for (int i = 0; i < 4; i++) {
            bytes[index++] = (byte) 0xFF;
        }
        bytes[index++] = (byte) 0xFE;
        bytes[index++] = (byte) 0x93;
        bytes[index++] = (byte) 0x0C;
        System.arraycopy(Tool.toBytes(device.getShortAddress()), 0, bytes, index, Tool.toBytes(device.getShortAddress()).length);
        index = index + Tool.toBytes(device.getShortAddress()).length;
        bytes[index++] = (byte) device.getEndpoint();
        for (int i = 0; i < 8; i++) {
            bytes[index++] = (byte) 0x00;
        }
        bytes[index] = (byte) 0x00;

        Tool.getChannelMap().get(IP).writeAndFlush(bytes);
    }

    @Override
    public void changeDeviceName(Device device, String name, String IP) {
        byte[] bytes = new byte[name.getBytes().length + 13];

        int index = 0;
        bytes[index++] = (byte) (0xFF & (name.getBytes().length + 13));
        bytes[index++] = (byte) 0x00;
        for (int i = 0; i < 4; i++) {
            bytes[index++] = (byte) 0xFF;
        }
        bytes[index++] = (byte) 0xFE;
        bytes[index++] = (byte) 0x94;
        bytes[index++] = (byte) (0xFF & (name.getBytes().length + 4));
        System.arraycopy(Tool.toBytes(device.getShortAddress()), 0, bytes, index, Tool.toBytes(device.getShortAddress()).length);
        index = index + Tool.toBytes(device.getShortAddress()).length;
        bytes[index++] = device.getEndpoint();
        bytes[index++] = (byte) (0xFF & name.getBytes().length);
        System.arraycopy(name.getBytes(), 0, bytes, index, name.getBytes().length);

        sendMessage = Tool.getSendContent(12, bytes);
        Tool.getChannelMap().get(IP).writeAndFlush(sendMessage);
    }

    @Override
    public void deleteDevice(Device device, String IP) {
        byte[] bytes = new byte[21];

        int index = 0;
        bytes[index++] = (byte) 0x15;
        bytes[index++] = (byte) 0x00;
        for (int i = 0; i < 4; i++) {
            bytes[index++] = (byte) 0xFF;
        }
        bytes[index++] = (byte) 0xFE;
        bytes[index++] = (byte) 0x95;
        bytes[index++] = (byte) 0x0C;
        bytes[index++] = (byte) 0x02;
        System.arraycopy(Tool.toBytes(device.getShortAddress()), 0, bytes, index, Tool.toBytes(device.getShortAddress()).length);
        index = index + Tool.toBytes(device.getShortAddress()).length;
        System.arraycopy(Tool.toBytes(device.getIEEE()), 0, bytes, index, Tool.toBytes(device.getIEEE()).length);
        index = index + Tool.toBytes(device.getIEEE()).length;
        bytes[index] = device.getEndpoint();

        sendMessage = Tool.getSendContent(12, bytes);
        Tool.getChannelMap().get(IP).writeAndFlush(sendMessage);
    }


    //设置设备状态
    @Override
    public void setDeviceState(Device controlDevice, byte state, String IP) {
        System.out.println("进入setDeviceState");
        byte[] bytes = new byte[22];

        int index = 0;
        bytes[index++] = (byte) 0x16;
        bytes[index++] = (byte) 0x00;

        //snid
        bytes[index++] = (byte) 0x82;
        bytes[index++] = (byte) 0x65;
        bytes[index++] = (byte) 0x2e;
        bytes[index++] = (byte) 0x11;

        //控制信息
        bytes[index++] = (byte) 0xFE;
        bytes[index++] = (byte) 0x82;
        bytes[index++] = (byte) 0x0D;
        bytes[index++] = (byte) 0x02;

        //shortAddress
        System.arraycopy(Tool.toBytes(controlDevice.getShortAddress()), 0, bytes, index, Tool.toBytes(controlDevice.getShortAddress()).length);
        index += Tool.toBytes(controlDevice.getShortAddress()).length;

//        bytes[index++] = (byte) 0xCE;
//        bytes[index++] = (byte) 0x4E;


        for (int i = 0; i < 6; i++) {
            bytes[index++] = (byte) 0x00;
        }

        bytes[index++] = controlDevice.getEndpoint();
//        bytes[index++] = (byte) 0x10;

        bytes[index++] = (byte) 0x00;
        bytes[index++] = (byte) 0x00;

        bytes[index] = state;
        System.out.println(Arrays.toString(bytes));


        System.out.println("下发指令");
        Tool.getChannelMap().get(IP).writeAndFlush(bytes);
    }


    //设备回调
    @Override
    public void device_CallBack(Device device, String IP, DeviceTokenRelationService deviceTokenRelationService) throws Exception {
        DeviceTokenRelation deviceTokenRelation = deviceTokenRelationService.getRelotionByShortAddressAndEndPoint(device.getShortAddress(), Integer.parseInt(String.valueOf(device.getEndpoint())));
//        System.out.println("deviceTokenRelation:" + device + ",IP:" + IP);
        if (deviceTokenRelation == null) {
            String type = Tool.deviceId2Type(device.getDeviceId());
            DeviceTokenRelation newDeviceTokenRelation = new DeviceTokenRelation(device.getIEEE(), Integer.parseInt(String.valueOf(device.getEndpoint())), type, device.getShortAddress(), IP);
//            System.out.println("newDeviceTokenRelation:" + newDeviceTokenRelation);
            deviceTokenRelationService.addARelation(newDeviceTokenRelation);
        }
// else{
//            if(!device.getShortAddress().equals(deviceTokenRelation.getShortAddress())){  // update shortAddress
//                deviceTokenRelationService.updateShortAddress(device.getShortAddress(), device.getIEEE());
//                DataMessageClient.publishAttribute(device.toString());
//                JsonObject jsonObject = new JsonObject();
//            }
//
//            DataMessageClient.publishAttribute(device.toString());
//
//            JsonObject jsonObject = new JsonObject();
//            if(device.getOnlineState()==3){
//                jsonObject.addProperty("online",1D);
//            }else{
//                jsonObject.addProperty("online",0D);
//            }
//            DataMessageClient.publishData(jsonObject.toString());
//        }

    }


    //设置设备状态回调
    @Override
    public void setDeviceState_CallBack() {
        System.out.println("change Device Status succeed! ");
    }

    //网关回调
    @Override
    public void gateway_CallBack(Gateway gateway) {
        System.out.println(gateway.toString());
    }


    //设备状态回调
    @Override
    public void deviceState_CallBack(Device device,DeviceTokenRelationService deviceTokenRelationService) {
        DeviceTokenRelation deviceTokenRelation = deviceTokenRelationService.getRelotionByShortAddressAndEndPoint(device.getShortAddress(), Integer.parseInt(String.valueOf(device.getEndpoint())));
//        System.out.println(device.getShortAddress() + "-" + device.getEndpoint() + ":" + device.getState());
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", device.getState());

            System.out.println(device.getIEEE());
            //System.out.println(jsonObject.toString());
            String topic = Config.DeviceETPrefix + deviceTokenRelation.getIEEE() + Config.DeviceETStateUpdateSuffix;
            DataMessageClient.publishData(topic,jsonObject.toString());
        } catch (Exception e) {
            System.out.println("数据表中无对应token" + e);
        }
    }

    @Override
    public void getDeviceInfo_CallBack(Device device, String data) {
        System.out.println(device.toString() + "data: "+ data               );
    }


    @Override
    public void data_CallBack(String shortAddress, int endPoint, JsonObject data, DeviceTokenRelationService deviceTokenRelationService) throws Exception {
        DeviceTokenRelation deviceTokenRelation = deviceTokenRelationService.getRelotionByShortAddressAndEndPoint(shortAddress, endPoint);
        try{
//            String sceneId = data.get("sceneId").getAsString();
//            String gatewayName = deviceTokenRelation.getGatewayName();
//            Scene mainScene = sceneService.getSceneByGatewayAndSceneId(gatewayName, sceneId);
//            List<Integer> side_scene_ids = sceneRelationService.getSceneRelation(mainScene.getScene_id());
//            for(Integer side_scene_id: side_scene_ids){
//                Scene sideScene = sceneService.getSceneBySceneId(side_scene_id);
//                GatewayGroup gatewayGroup = gatewayGroupService.getGatewayGroup(sideScene.getGatewayName());
//                GatewayMethod gatewayMethod = new GatewayMethodImpl();
//                gatewayMethod.callScene(sideScene.getSceneId(), gatewayGroup.getIp());
//            }
            return;


        }catch (NullPointerException e){

        }

        if(deviceTokenRelation!=null) {
            String topic = Config.DeviceETPrefix + deviceTokenRelation.getIEEE() + Config.DeviceETStateUpdateSuffix;
//            List<DeviceTokenRelation> deviceTokenRelations = deviceTokenRelationService.getRelationByIEEE(deviceTokenRelation.getIEEE());
//            for (DeviceTokenRelation deviceTokenRelation1 : deviceTokenRelations) {
                String jsonStr = data.toString();
                DataMessageClient.publishData(topic, jsonStr);
//            }
        }
    }


}
