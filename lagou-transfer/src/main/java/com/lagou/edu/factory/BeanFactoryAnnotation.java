package com.lagou.edu.factory;
import com.alibaba.druid.util.StringUtils;
import com.lagou.edu.annotation.Autowired;
import com.lagou.edu.annotation.Service;

import com.lagou.edu.annotation.Transactional;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.service.TransferService;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 工厂类，生产对象（使用注解反射技术）
 * @author lane
 * @date 2021年03月28日 下午6:59
 */
public class BeanFactoryAnnotation {

    /**
     * 任务一：读取解析anno，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */
    //仿照xml ioc思想存储对象的集合
    private static Map<String,Object> map = new HashMap<>();
    //解析anno，生成对象存入map当中
    static{

        try{
            //反射扫描包下所有的对象
            Reflections reflections = new Reflections("com.lagou.edu");
            //获取所有的注解class对象
            Set<Class<?>> clazzs = reflections.getTypesAnnotatedWith(Service.class);
            //遍历生成每一个对象
            for (Class clazz:clazzs ) {
                Object obj = clazz.newInstance();
                Service service = (Service) clazz.getAnnotation(Service.class);
                //判断是否在注解上加上自己的value值
                if (StringUtils.isEmpty(service.value())){
                    String[] className = clazz.getName().split("\\.");
                    map.put(className[className.length-1],obj);
                }else{
                    //使用自己定义的ID值
                    map.put(service.value(),obj);
                }
            }
            // 实例化完成之后维护对象的依赖关系，检查哪些对象需要传值进入，根据它的配置Autowired注解，我们传入相应的值
            for (Map.Entry<String,Object> entry:map.entrySet()) {
                Object object = entry.getValue();
                Class clazz = object.getClass();
                //获取每一个属性
                Field[] declaredFields = clazz.getDeclaredFields();
                //遍历每一个属性，判断是否有Autowired注解
                for (Field field:declaredFields) {
                    //判断是否使用注解
                    if(field.isAnnotationPresent(Autowired.class) &&field.getAnnotation(Autowired.class).required()){
                        //获取要注入的类
                        String[] fieldNames = field.getType().getName().split("\\.");
                        String fieldName = fieldNames[fieldNames.length-1];
                        Method[] methods = clazz.getMethods();
                        //遍历查找该属性是否有set方法，若有则调用注入该属性对象
                        for (Method method:methods) {
                            if (method.getName().equalsIgnoreCase("set"+fieldName)){
                                Object o = map.get(fieldName);
                                System.out.println(o.toString());

                                method.invoke(object,o);

                            }
                        }


                    }
                }

                //判断该类是否有@Transactional注解,有则用代理对象替换掉原来的对象
                if (clazz.isAnnotationPresent(Transactional.class)){
                    ProxyFactory proxyFactory = (ProxyFactory)map.get("ProxyFactory");
                    Class[] interfaces = clazz.getInterfaces();
                    if(interfaces!=null&interfaces.length>0){
                        //jdk动态代理
                        object = proxyFactory.getJdkProxy(object);
                    }else {
                        //cglib动态代理
                        object = proxyFactory.getCglibProxy(object);
                    }

                }
                //注入，代理之后对象重新存放入map内
                map.put(entry.getKey(),object);
            }


        }catch (Exception e){

            e.printStackTrace();
        }

    }
    //对外提供根据名称获取bean对象方法
    public static Object getBeanAnno(String beanName){
        return map.get(beanName);
    }

}
