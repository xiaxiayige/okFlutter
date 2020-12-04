package com.xiaxiayige.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;


public class MainAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("Hello " + e);
        SearchForm searchForm=new SearchForm();
        searchForm.setVisible(true);

    }
}
