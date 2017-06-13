// Copyright 2012 Square, Inc.

package com.squareup.timessquare;

import java.util.Date;

/**
 * Describes the state of a particular date cell in a {@link MonthView}.
 */
public class MonthCellDescriptor {
    public enum RangeState {
        NONE, FIRST, MIDDLE, LAST
    }

    private Date date;
    private int value;
    private boolean isCurrentMonth;
    private boolean isSelected;
    private boolean isToday;
    private boolean isSelectable;
    private boolean isHighlighted;
    private RangeState rangeState;
    private boolean isHoliday = false;


    public MonthCellDescriptor(Date date,
                               boolean currentMonth,
                               boolean selectable,
                               boolean selected,
                               boolean today,
                               boolean highlighted,
                               boolean holiday,
                               int value,
                               RangeState rangeState) {

        this.date = date;
        isCurrentMonth = currentMonth;
        isSelectable = selectable;
        isHighlighted = highlighted;
        isSelected = selected;
        isToday = today;
        this.isHoliday = holiday;
        this.value = value;
        this.rangeState = rangeState;
    }

    public MonthCellDescriptor(Date date,
                               boolean currentMonth,
                               boolean selectable,
                               boolean selected,
                               boolean today,
                               boolean highlighted,
                               int value,
                               RangeState rangeState) {

        this(date, currentMonth, selectable, selected, today, highlighted, false, value, rangeState);
    }

    public Date getDate() {
        return date;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isToday() {
        return isToday;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public RangeState getRangeState() {
        return rangeState;
    }

    public void setRangeState(RangeState rangeState) {
        this.rangeState = rangeState;
    }

    public int getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "MonthCellDescriptor{"
                + "date="
                + date
                + ", value="
                + value
                + ", isCurrentMonth="
                + isCurrentMonth
                + ", isSelected="
                + isSelected
                + ", isToday="
                + isToday
                + ", isSelectable="
                + isSelectable
                + ", isHighlighted="
                + isHighlighted
                + ", rangeState="
                + rangeState
                + '}';
    }
}
