package wechatAPI;

import wechatAPI.annotation.BotCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created on 17-2-11.
 *
 * @author ing
 * @version 1
 */
public class LoadCommands {
    public static Map<String, Method> commands = new HashMap<>();

    public static void init() {
        Commands cmd = new Commands();
        Class c = cmd.getClass();
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            BotCommand bcmd = method.getAnnotation(BotCommand.class);
            commands.put(bcmd.value(), method);
        }
    }

    public static Map<String, Method> getCommands() {
        return commands;
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        init();
        // 动态调用静态方法测试
        commands.get("/天气").invoke(null, "北京");
    }
}
