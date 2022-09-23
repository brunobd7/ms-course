package com.dantas.hrworker.resources;

import com.dantas.hrworker.entities.Worker;
import com.dantas.hrworker.repositories.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/workers")
@RefreshScope
public class WorkerResource {

    private static Logger logger = LoggerFactory.getLogger(WorkerResource.class);

    @Value("${test.config}") //VALUE REFERS TO CONFIG INTO REMOTE GIT REPOSITORY
    private String testConfig;

    @Autowired
    private Environment env;

    @Autowired
    private WorkerRepository workerRepository;


    @GetMapping
    private ResponseEntity<List<Worker>> listAll(){
        List<Worker> workerList = workerRepository.findAll();
        return ResponseEntity.ok(workerList);
    }

    @GetMapping(value = "/configs")
    private ResponseEntity<Void> getConfigs(){
        logger.info("CONFIG => "+ testConfig);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<Worker> findById(@PathVariable Long id){

//        try {
//            Thread.sleep(3000L);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        //PRINT EXECUTION PORT WHERE REQUESTs ARE RUNNING
        logger.info("PORT => " + env.getProperty("local.server.port"));

        Worker workerFounded = workerRepository.findById(id).orElse(new Worker());
        return ResponseEntity.ok(workerFounded);
    }
}


