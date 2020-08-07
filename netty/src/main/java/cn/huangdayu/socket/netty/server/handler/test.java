package cn.huangdayu.socket.netty.server.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class test {
    public static void main(String[] args)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method[] methods = SampleClass.class.getMethods();
        SampleClass sampleObject = new SampleClass();
        methods[1].invoke(sampleObject, "data");
        System.out.println(methods[0].invoke(sampleObject));
    }
}

class SampleClass {
    private String sampleField;

    public String getSampleField() {
        System.out.println("--------0--------"+sampleField);
        return sampleField;
    }

    public void setSampleField(String sampleField) {
        System.out.println("--------1--------"+sampleField);
        this.sampleField = sampleField;
    }
}
