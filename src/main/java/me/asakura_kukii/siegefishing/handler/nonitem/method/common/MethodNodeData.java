package me.asakura_kukii.siegefishing.handler.nonitem.method.common;

import java.util.ArrayList;
import java.util.List;

public class MethodNodeData {

    public MethodType mT;
    public String trigger;
    public List<Object> data = new ArrayList<>();
    public List<MethodNodeData> next = new ArrayList<>();

    public MethodNodeData() {
    }
}
