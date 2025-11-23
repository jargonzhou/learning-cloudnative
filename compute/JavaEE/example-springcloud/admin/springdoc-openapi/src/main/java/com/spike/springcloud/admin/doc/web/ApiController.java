package com.spike.springcloud.admin.doc.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "API", description = "API接口")
@RestController
@RequestMapping("/api")
public class ApiController {

    // may need this: https://springdoc.org/#migrating-from-springfox
    // @Tag
    // @Operation
    // @Parameter
    // @ApiResponse
    //
    // @Schema

    @Operation(summary = "GET", description = "GET请求",
            parameters = {@Parameter(name = "param", description = "param参数",
                    in = ParameterIn.QUERY, required = false)})
    @GetMapping
    public String get(@RequestParam(value = "param", required = false) String param) {
        return "get " + param;
    }

    @PostMapping
    public String post() {
        return "post";
    }

    @DeleteMapping
    public String delete() {
        return "delete";
    }
}
