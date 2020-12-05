package com.xiaxiayige.okflutter;

import com.intellij.notification.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.messages.MessageDialog;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.ui.components.panels.VerticalLayout;
import com.intellij.util.ui.UIUtil;
import com.xiaxiayige.okflutter.bean.PackagesItem;
import com.xiaxiayige.okflutter.respostory.SearchFlutterRespostory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchForm extends JFrame {
    private JPanel rootPanel;
    private JTextField editText;
    private JButton btnSearch;
    private JPanel contents;
    private String emptyText = "Nothing to show";
    private final int DEFAULT_WIDTH = 850;
    private final int DEFAULT_HEIGHT = 400;
    private Dimension defaultSize = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    private List<PackagesItem> dataList = new ArrayList<>();
    private Project project;
    public SearchForm(Project project) {
        this.project = project;
        initView();
        initListener();
        showData();
        setFullScreenCenter();

    }

    private void initListener() {
        btnSearch.addActionListener(e -> {
            emptyText="Searching...,Please wait";
            showData();
            String keyworld = editText.getText().trim();
            if(keyworld.length()==0){
                Messages.showMessageDialog("Please input keyWorld","tips",null);
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SearchFlutterRespostory.INSTANCE.search(keyworld, "")
                                .subscribe(packagesItems -> {
                                    dataList.addAll(packagesItems);
                                    showData();
                                }, throwable -> {
                                    throwable.printStackTrace();
                                    emptyText = throwable.getMessage();
                                    showData();
                                });
//
                    }
                }).start();
            }

        });
    }


    private void showData() {
        contents.removeAll();
        if(dataList==null || dataList.size()==0){
            JBList jbList = new JBList();
            jbList.setSize(contents.getSize());
            jbList.setEmptyText(emptyText);
            jbList.setBackground(Color.white);
            JBScrollPane scrollPane = new JBScrollPane(jbList);
            Dimension preferredSize = new Dimension(contents.getWidth() - 16, contents.getHeight() - 16*2);
            scrollPane.setPreferredSize(preferredSize);
            contents.add(scrollPane);
        }else{
            JPanel jbList = new JPanel();
            jbList.setLayout(new VerticalLayout(10));
            jbList.setSize(contents.getSize());
            emptyText="";
            for (PackagesItem packagesItem : dataList) {
                JPanel item = _buildItemPanel(packagesItem);
                jbList.add(item);
            }
            JBScrollPane scrollPane = new JBScrollPane(jbList);
            Dimension preferredSize = new Dimension(contents.getWidth() - 16, contents.getHeight() - 16*2);
            jbList.setBackground(Color.white);
            scrollPane.setPreferredSize(preferredSize);
            contents.add(scrollPane);
        }
        contents.revalidate();

    }

    @NotNull
    private JPanel _buildItemPanel(PackagesItem packagesItem) {
        JPanel item = new JPanel();
        item.setLayout(new HorizontalLayout(16));
        JBLabel name = new JBLabel("" + packagesItem.getResultName());
        name.setFont(new Font(null,Font.BOLD,18));
        item.add(name);
        item.add(new JLabel("Latest version: " + packagesItem.getVersion()));
        item.add(new JLabel("UpdateTime: " + packagesItem.getUpdateTime()));
        item.add(new JLabel("Likes: " + packagesItem.getLike()));
        item.add(new JLabel("Pub Point: " + packagesItem.getPubPoints()));
        item.add(new JLabel("POPULARITY: " + packagesItem.getPopulaprty()));
        JButton gotoWebSite = new JButton("website");
        gotoWebSite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime()
                            .exec("cmd /c start "+packagesItem.getTargetUrl());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        JButton copy_dependencies = new JButton("Copy dependencies");
        copy_dependencies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard systemClipboard = getToolkit().getSystemClipboard();
                Transferable content =  new StringSelection(packagesItem.getDependencies());
                systemClipboard.setContents(content,null);

                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        NotificationGroup  notificationGroup = new  NotificationGroup(
                                Constant.TITLE,
                                NotificationDisplayType.BALLOON,
                                true
                        );
                        Notification notification = notificationGroup.createNotification(Constant.DEPENDENCY_COPIED_TITLE, null, Constant.DEPENDENCY_COPIED_MSG, NotificationType.INFORMATION);
                        Notifications.Bus.notify(notification,project);
                    }
                });

            }
        });
        copy_dependencies.setSize(60, 30);
        item.add(gotoWebSite);
        item.add(copy_dependencies);
        item.setSize(contents.getWidth() - 16, 50);
        return item;
    }

    private void initView() {
        add(rootPanel);
        setTitle("Ok,Flutter");
        setSize(defaultSize);
        setMinimumSize(defaultSize);
    }

    private void setFullScreenCenter() {
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setLocation((int) (width - this.getWidth()) / 2, (int) (height - this.getHeight()) / 2);
    }


}

