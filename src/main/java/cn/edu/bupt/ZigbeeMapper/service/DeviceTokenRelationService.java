package cn.edu.bupt.ZigbeeMapper.service;

import cn.edu.bupt.ZigbeeMapper.data.DeviceTokenRelation;
import cn.edu.bupt.ZigbeeMapper.mapper.DeviceTokenRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceTokenRelationService {

    @Autowired
    private DeviceTokenRelationMapper deviceTokenRelationMapper;

    public DeviceTokenRelation getRelotionByIEEEAndEndPoint(String IEEE, Integer endPoint){
        return deviceTokenRelationMapper.getRelotionByIEEEAndEndPoint(IEEE, endPoint);
    }

    public Boolean addARelation(DeviceTokenRelation deviceTokenRelation){
        Integer i = deviceTokenRelationMapper.addARelation(deviceTokenRelation);
        return i==1;
    }
}
