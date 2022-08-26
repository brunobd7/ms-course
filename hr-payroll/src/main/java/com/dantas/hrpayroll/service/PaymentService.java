package com.dantas.hrpayroll.service;

import com.dantas.hrpayroll.entities.Payment;
import com.dantas.hrpayroll.entities.Worker;
import com.dantas.hrpayroll.feignclients.WorkerFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    @Autowired
    private WorkerFeignClient feignClient;

    public Payment getPayment(Long workerId, Integer days){

        Worker worker = feignClient.findById(workerId).getBody();
        return new Payment(worker.getName(), worker.getDailyIncome(), days);
    }
}
