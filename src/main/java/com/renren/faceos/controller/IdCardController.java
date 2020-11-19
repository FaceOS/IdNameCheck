package com.renren.faceos.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.renren.faceos.domain.IdName;
import com.renren.faceos.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/idCard")
public class IdCardController {
    protected final Logger logger = LoggerFactory.getLogger(IdCardController.class);


    @PostMapping("/cxzx")
    @ResponseBody
    public String cxzx(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String name = jsonObject.getString("Name");
        String checkType = jsonObject.getString("CheckType");
        String faceBase64 = jsonObject.getString("FaceBase64");
        String cardNo = jsonObject.getString("CardNo");

        IdName idName = new IdName();
        idName.setLoginName("faceos");
        idName.setPwd("faceos");

        IdName.ParamBean paramBean = new IdName.ParamBean();
        paramBean.setName(name);
        paramBean.setIdCard(cardNo);

        String url = "";
        if (checkType.equals("1")) {
            url = "https://49.233.242.197:8313/CreditFunc/v2.1/IDNameCheck";
            idName.setServiceName("IDNameCheck");
        } else {
            url = "https://49.233.242.197:8313/CreditFunc/v2.1/IdNamePhotoCheck";
            idName.setServiceName("IdNamePhotoCheck");
            paramBean.setImage(faceBase64);
        }
        idName.setParam(paramBean);

        try {
            return HttpClientUtils.httpPost(url, JSON.toJSONString(idName));
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
