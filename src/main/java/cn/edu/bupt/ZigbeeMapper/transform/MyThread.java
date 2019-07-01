package cn.edu.bupt.ZigbeeMapper.transform;

import cn.edu.bupt.ZigbeeMapper.service.DeviceTokenRelationService;

public class MyThread extends Thread{

    private final String host;
    private final int port;
    private DeviceTokenRelationService deviceTokenRelationService;

    public MyThread(String host, int port,DeviceTokenRelationService deviceTokenRelationService) {
        this.host = host;
        this.port = port;
        this.deviceTokenRelationService = deviceTokenRelationService;
    }

    @Override
    public void run() {
        try {
//            new EchoClient(host,port,deviceTokenRelationService).start();
            new EchoClient(host,port,deviceTokenRelationService).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
