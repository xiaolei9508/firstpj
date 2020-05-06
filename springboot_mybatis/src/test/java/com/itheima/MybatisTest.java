package com.itheima;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.domain.DataStockStatus;
import com.itheima.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybatisApplication.class)
public class MybatisTest {

    //@Autowired
    //private UserMapper userMapper;

    @Test
    public void test(){
        //List<User> users = userMapper.queryUserList();
        //System.out.println(users);
        String json="{\"code\":200,\"data\":\"{'600036.XX':{'RT_LATEST':0.0,'RT_PCT_CJH':0.0}}\",\"message\":\"\",\"status\":\"Success\"}";
        JSONObject object = JSON.parseObject(json);
        String data = object.getString("data");
        String s1 = data.replaceAll("'", "\"");
        System.out.println(s1);
        JSONObject dataObj = JSON.parseObject(s1);
        for (String key:dataObj.keySet()){
            DataStockStatus dataStockStatus = new DataStockStatus();
            JSONObject jsonObject = JSON.parseObject(dataObj.getString(key));
            dataStockStatus.setRT_LATEST(jsonObject.getDouble("RT_LATEST"));
            dataStockStatus.setRT_PCT_CJH(jsonObject.getDouble("RT_PCT_CJH"));
            System.out.println("最后："+dataStockStatus);
        }
    }

}
