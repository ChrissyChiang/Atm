package com.chrissy.atm;

import android.graphics.drawable.Icon;

public class Function {
    String name;
    int Icon;


    public Function(String name, int icon) {
        this.name = name;
        Icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }
}
