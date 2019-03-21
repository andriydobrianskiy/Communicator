package com.Utils;

import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;

import java.util.function.Function;

public class TooltipTableRow<T> extends TableRow<T> {
    private Function<T, String> toolTipStringFunction;
    public TooltipTableRow(Function<T, String> toolTipStringFunction){
        this.toolTipStringFunction = toolTipStringFunction;
    }
    @Override
    protected void updateItem(T item, boolean emty){
        super.updateItem(item, emty);
        if(item == null){
            setTooltip(null);
        }else {
            Tooltip tooltip = new Tooltip(toolTipStringFunction.apply(item));
            setTooltip(tooltip);
        }
    }
}
