package cn.edu.bupt.ZigbeeMapper.transform;

import cn.edu.bupt.ZigbeeMapper.data.DeviceTokenRelation;
import cn.edu.bupt.ZigbeeMapper.method.GatewayMethodImpl;
import cn.edu.bupt.ZigbeeMapper.service.DeviceTokenRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

@Component
@RestController
@RequestMapping("api/v1/device")
public class GetIP implements CommandLineRunner {
    //设置端口号
    private final static int PORT = 9090;
    //设置广播地址
    private static final String HOSTNAME = "255.255.255.255";
    public static GatewayMethodImpl gatewayMethod = new GatewayMethodImpl();

    @Autowired
    private DeviceTokenRelationService deviceTokenRelationService;

    //添加新网关
    @RequestMapping(value = "/getNewGateway", method = RequestMethod.GET)
    @ResponseBody
    public void run(String... var1) throws Exception {
        try {
            DatagramSocket socket = new DatagramSocket();
//            socket.setSoTimeout(10000);
            InetAddress host = InetAddress.getByName(HOSTNAME);

            //封装下发的指令
            byte[] data = "GETIP\\r\\n".getBytes();
            //指定包要发送的目的地
            DatagramPacket request = new DatagramPacket(data, data.length, host, PORT);

            //为接收的数据包创建空间
            byte[] receiveData = new byte[1024];
            DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);

            //广播
            socket.send(request);

            //接收返回的数据包
            while(true) {
                socket.receive(response);
                String result = new String(response.getData(), 0, response.getLength(), "ASCII");
                String[] stringArray = result.split("\r\n");
                if(stringArray.length == 2) {
                    String bindHost = stringArray[0].substring(3);
                    String snid = stringArray[1].substring(3);
                    final int port = 8001;
                    if(!Tool.getChannelMap().containsKey(bindHost + ":8001")) {
                        Tool.getSnidMap().put(bindHost+":8001",snid);
                        new MyThread(bindHost,port,deviceTokenRelationService).start();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
