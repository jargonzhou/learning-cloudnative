package com.spike.springcloud.framework.domain.woker;

import com.spike.springcloud.commons.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    @Autowired
    private Map<String, IWorker> workerMap;

    @GetMapping
    public Result<List<String>> getWorks() {
        return Result.success(workerMap.keySet().stream().toList());
    }

    @PostMapping("/run/{name}")
    public Result<?> runWorker(@PathVariable String name) {
        final String workerName = IWorker.BEAN_NAME_PREFIX + name;
        if (!workerMap.containsKey(workerName)) {
            return Result.fail("Not Found!");
        }

        return Result.success(workerMap.get(workerName).work());
    }
}
