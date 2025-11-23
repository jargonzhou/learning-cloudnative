package com.spike.springcloud.txn.seata.app;

import com.spike.springcloud.txn.seata.domain.service.BusinessService;
import com.spike.springcloud.txn.seata.domain.service.impl.BusinessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @GetMapping("/txn")
    public Object txn(@RequestParam(value = "mockFail", required = false) boolean mockFail) {
        businessService.purchase(BusinessServiceImpl.USER_ID,
                BusinessServiceImpl.COMMODITY_CODE,
                1,
                mockFail);
        return "ok";
    }
}
