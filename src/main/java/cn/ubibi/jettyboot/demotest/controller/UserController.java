package cn.ubibi.jettyboot.demotest.controller;

import cn.ubibi.jettyboot.demotest.controller.parser.CurrentUser;
import cn.ubibi.jettyboot.demotest.controller.parser.UserInfoParser;
import cn.ubibi.jettyboot.demotest.controller.render.PageRender;
import cn.ubibi.jettyboot.demotest.dao.UserDAO;
import cn.ubibi.jettyboot.demotest.entity.UserEntity;
import cn.ubibi.jettyboot.framework.commons.cache.CacheMethod;
import cn.ubibi.jettyboot.framework.commons.model.Page;
import cn.ubibi.jettyboot.framework.ioc.Autowired;
import cn.ubibi.jettyboot.framework.jdbc.model.UpdateResult;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ubibi.jettyboot.framework.rest.annotation.*;
import cn.ubibi.jettyboot.framework.rest.ifs.HttpParsedRequest;
import cn.ubibi.jettyboot.framework.rest.ifs.RequestParser;
import cn.ubibi.jettyboot.framework.rest.impl.ResultRenderMisc;
import cn.ubibi.jettyboot.framework.rest.impl.VoidResult;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;


@Controller({"/user", "/vip"})
public class UserController {

    private static Logger logger = Log.getLogger(UserController.class);

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/hello11")
    public VoidResult hello11(HttpParsedRequest request,HttpServletResponse response){

        AsyncContext asyncContext = request.startAsync();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    response.setHeader("Content-type", "text/html;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("hello__");
                    response.getWriter().flush();
                    while (true){
                        Thread.sleep(1000);
                        response.getWriter().write("hello__");
                        response.getWriter().flush();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                asyncContext.complete();

            }
        }).start();

        System.out.println("111");
        return new VoidResult();
    }


    @GetMapping("/hello22")
    @AsyncMergeMethod(paramKey = {0})
    @CacheMethod(paramKey = {0})
    public String hello22(@RequestParam("name") String name) throws InterruptedException {
        Thread.sleep(10000);
        return "ok:" + name;
    }




    @GetMapping("/helloworld/:name")
    @CacheMethod(cacheKey = "user/helloworld", paramKey = {0})
    public String helloworld(@PathVariable("name") String name,HttpParsedRequest request) throws Exception {

        List<UserEntity> ss = userDAO.findByUsername(name);

        return "hello world : vip " + isVip(request) + " name " + name;
    }


    private boolean isVip(HttpParsedRequest request) {
        String target = request.getMatchedControllerPath();
        if (target.startsWith("/vip")) {
            return true;
        }
        return false;
    }



    @GetMapping("/test_page")
    public PageRender getmm212_page(@RequestParam("name") String name, @AspectVariable CurrentUser currentUser) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", name + "__" + this.getClass().getClassLoader().getClass().getName() + "___" + currentUser.getName());

        System.out.println("getmm212_page");
        Thread.sleep(5000);
        return new PageRender("test.html", map);
    }


    /**
     *
     * 考虑有一万个请求同时到达的场景，
     * 缓存是空的，第一个请求会真正去执行里面的方法，剩下的所有请求的线程立即释放，只保持请求连接，
     * 线程不会被占用。第一个请求执行的结果返回后，会把这个结果同时推给这一万个请求。同时放入缓存。
     *
     * 现在有缓存了。又来了一万个请求，同样，所有请求的线程立即释放，只保持请求连接，
     * 从缓存中取结果，同时返回给这一万个请求。
     *
     *
     * @param name
     * @return
     * @throws Exception
     */
    @GetMapping("/test_insert2")
    @AsyncMergeMethod(paramKey = {0})
    @CacheMethod(paramKey = {0})
    public UpdateResult getmm212(@RequestParam("name") String name) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("yaoli", 123);
        map.put("dai", 3);
        map.put("fid", 3);
        map.put("mid", 3);
        map.put("create_time", System.currentTimeMillis());
        map.put("update_time", System.currentTimeMillis());
        return userDAO.insertObject(map);
    }


    @GetMapping("/test_insert")
    public UpdateResult getmm21(@RequestParams UserInfoParser reqParser) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "name" + System.currentTimeMillis() + "_" + Math.random());
        map.put("yaoli", 123);
        map.put("dai", 3);
        map.put("fid", 3);
        map.put("mid", 3);
        map.put("create_time", System.currentTimeMillis());
        map.put("update_time", System.currentTimeMillis());
        return userDAO.insertObject(map);
    }


    @GetMapping("/test")
    public String getmm(UserInfoParser reqParser, HttpParsedRequest request, CurrentUser currentUser) throws Exception {
        new UserDAO().findAll();
        if (reqParser instanceof RequestParser) {
            System.out.println("111");
        }
        return "123---" + reqParser.getName() + "=====" + currentUser.getName();
    }


    @GetMapping(value = "/")
    public Page<UserEntity> getUserById3(
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "pageNo") int pageNo,
            HttpServletRequest request, HttpServletResponse response) throws Exception {



        long t1 = System.currentTimeMillis();
        Page<UserEntity> result = userDAO.findPage(pageNo, pageSize);

        long t2 = System.currentTimeMillis();

        logger.info("::" + (t2 - t1));
        return result;
    }


    @GetMapping(value = "/:uid")
    public Object getUserById(HttpParsedRequest params, HttpServletResponse response) throws Exception {

        String uid = params.getPathVariable("uid");
        String name = params.getParameter("name");

        String[] names = params.getParameterValues("name");

        Cookie cookie = new Cookie("aaa", "a2333");

        response.addCookie(cookie);

        return userDAO.findByUsername(uid);
    }


    @PostMapping("/new/:uid")
    public String getUserById2(HttpParsedRequest request) throws IOException, ServletException {
        String aaa = request.getContextPath();
        return "123saaa";
    }


    @RpcFunction
    public String getUserInfoById(Object a, Integer b, @AspectVariable CurrentUser currentUser) {
        // UserController.getUserInfoById(123);
        return "getUserInfoById:  " + (a.toString() + b);
    }


    @RpcFunction
    public String getUserInfoByName(String name, @AspectVariable CurrentUser currentUser) {
        // UserController.getUserInfoById(123);
        return "getUserInfoByName:  " + name;
    }


}
