package com.Utils.MiniFilterWindow;

import javafx.scene.control.Control;
import com.Utils.DictionaryProperties;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitComponents {
    private static Logger log = Logger.getLogger(InitComponents.class.getName());

    private DictionaryProperties window;
    private HashMap<Control, Method> components;

    private boolean firstInit = false;

    private Object object;

    public InitComponents() {

    }

    public InitComponents(DictionaryProperties window, Object object) {
        this.window = window;
        this.object = object;
    }

    public void setAllComponents(HashMap<Control, Method> components, boolean refresh) {
        try {
            this.components = components;
            components.keySet().stream().forEach(item -> invokeComponent(item));
            refreshData(refresh);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        } finally {
            firstInit = true;
        }
    }

    public void setComponentList(List<Control> list, boolean refresh) {
        if(firstInit) {
            list.stream().forEach(item -> invokeComponent(item));
            refreshData(refresh);
        }
    }

    public void setComponent(Control item, boolean refresh) {
        if(firstInit) {
            invokeComponent(item);
            refreshData(refresh);
        }
    }


    private void invokeComponent(Control item) {
        try {
            components.get(item).invoke(object);
        } catch (Exception e) {
            log.log(Level.SEVERE, "(" + window.getClass().getSimpleName() + ")" +
                    " Exception: " + e);
        }
    }

    private void refreshData(boolean refresh) {
        if(refresh) window.refreshData();
    }
}
