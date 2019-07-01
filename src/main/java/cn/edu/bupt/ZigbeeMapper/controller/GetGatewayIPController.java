package cn.edu.bupt.ZigbeeMapper.controller;


import cn.edu.bupt.ZigbeeMapper.transform.MyThread;
import cn.edu.bupt.ZigbeeMapper.transform.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


@RestController
@RequestMapping("api/v1/device")
public class GetGatewayIPController {

//    //设置端口号
//    private final static int PORT = 9090;
//    //设置广播地址
//    private static final String HOSTNAME = "255.255.255.255";
//
//    @RequestMapping(value = "/getNewGateway", method = RequestMethod.GET)
//    @ResponseBody
//    private boolean getNewGateway() {
//        try {
//            DatagramSocket socket = new DatagramSocket();
//            InetAddress host = InetAddress.getByName(HOSTNAME);
//
//            //封装下发的指令
//            byte[] data = "GETIP\\r\\n".getBytes();
//            //指定包要发送的目的地
//            DatagramPacket request = new DatagramPacket(data, data.length, host, PORT);
//
//            //为接收的数据包创建空间
//            byte[] receiveData = new byte[1024];
//            DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);
//
//            //广播
//            socket.send(request);
//
//            //接收返回的数据包
//            while(true) {
//                socket.receive(response);
//                String result = new String(response.getData(), 0, response.getLength(), "ASCII");
//                String[] stringArray = result.split("\r\n");
//                if(stringArray.length == 2) {
//                    String bindHost = stringArray[0].substring(3);
//                    String snid = stringArray[1].substring(5);
//                    final int port = 8001;
//                    System.out.println(bindHost);
//                    if(!Tool.getMap().containsKey(bindHost)) {
//                        new MyThread(bindHost,port).start();
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

}
