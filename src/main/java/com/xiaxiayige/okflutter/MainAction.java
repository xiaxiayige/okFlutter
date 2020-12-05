package com.xiaxiayige.okflutter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;


public class MainAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        SearchForm searchForm=new SearchForm(e.getProject());
        searchForm.setVisible(true);
    }
}
