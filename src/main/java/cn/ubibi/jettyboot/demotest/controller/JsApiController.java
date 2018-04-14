package cn.ubibi.jettyboot.demotest.controller;

import cn.ubibi.jettyboot.framework.ioc.Autowired;
import cn.ubibi.jettyboot.framework.rest.ControllerMethodHandler;
import cn.ubibi.jettyboot.framework.rest.RestContextHandler;
import cn.ubibi.jettyboot.framework.rest.annotation.Controller;
import cn.ubibi.jettyboot.framework.rest.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/jsApi")
public class JsApiController {


    @Autowired
    private RestContextHandler context;


    @GetMapping("/")
    public Object xxx(){
        List<ControllerMethodHandler> methods = context.getControllerMethodHandlers();

        String contextPath = context.getContextPath();


        Map<String,String> map = new HashMap<>();

        for (ControllerMethodHandler methodHandler:methods){
            methodHandler.getMethod();

            String methodName = methodHandler.getMethod().getName();


            methodHandler.getTargetPath();
            methodHandler.getSupportRequestMethod();



            String url = contextPath + methodHandler.getTargetPath();

            String paramsString = "";

            String functionCode = "function("+paramsString+"){" +
                    " return ajax('"+methodHandler.getSupportRequestMethod()+"','"+url+"')" +
                    "}";


            map.put(methodName,functionCode);



        }




        return map;


    }
}

