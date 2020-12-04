package com.xiaxiayige.okflutter;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.ui.components.panels.VerticalLayout;
import com.xiaxiayige.okflutter.bean.PackagesItem;
import com.xiaxiayige.okflutter.respostory.SearchFlutterRespostory;
import io.reactivex.functions.Consumer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchForm extends JFrame {
    private JPanel rootPanel;
    private JTextField editText;
    private JButton btnSearch;
    private JPanel contents;
    private String emptyText = "Nothing to show";

    private List<PackagesItem> dataList = new ArrayList<>();

    public SearchForm() {
        initView();
        setFullScreenCenter();
        initListener();
        showData();

    }

    private void initListener() {
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emptyText="Searching...,Please wait";
                showData();
                String keyworld = editText.getText().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SearchFlutterRespostory.INSTANCE.search(keyworld, "").subscribe(new Consumer<List<PackagesItem>>() {
                            @Override
                            public void accept(List<PackagesItem> packagesItems) throws Exception {
                                dataList.addAll(packagesItems);
                                showData();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                emptyText = throwable.getMessage();
                                showData();
                            }
                        });
//
                    }
                }).start();

            }
        });
    }


    private void showData() {
        System.out.println("showData----------------------");
        contents.removeAll();
        contents.setLayout(new VerticalLayout(0));
        JPanel jbList = new JPanel();
        jbList.setLayout(new VerticalLayout(10));
        if(dataList==null || dataList.size()<=0){

        }else{
            emptyText="";

            jbList.setBackground(Color.RED);
            for (PackagesItem packagesItem : dataList) {
                JPanel item = _buildItemPanel(packagesItem);
                jbList.add(item);
                jbList.add(item);
            }

        }



        JBScrollPane scrollPane = new JBScrollPane(jbList);


        Dimension preferredSize = new Dimension(contents.getWidth() - 16, contents.getHeight() - 16*2);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.setPreferredSize(preferredSize);
        contents.add(scrollPane);
        contents.revalidate();
    }

    @NotNull
    private JPanel _buildItemPanel(PackagesItem packagesItem) {
        JPanel item = new JPanel();
        item.setLayout(new HorizontalLayout(16));
        item.add(new JLabel("" + packagesItem.getResultName()));
        item.add(new JLabel("latest version: " + packagesItem.getVersion()));
        item.add(new JLabel("updateTime: " + packagesItem.getUpdateTime()));
        item.add(new JLabel("like: " + packagesItem.getLike()));
        item.add(new JLabel("pubPoint: " + packagesItem.getPubPoints()));
        item.add(new JLabel("populaprty: " + packagesItem.getPopulaprty()));
        JButton copy_dependencies = new JButton("Copy dependencies");
        copy_dependencies.setSize(60, 30);
        item.add(copy_dependencies);
        item.setSize(contents.getWidth() - 16, 50);
        return item;
    }

    private void initView() {
        add(rootPanel);
        setTitle("Ok,Flutter");
        setSize(750, 400);
        setMinimumSize(new Dimension(750, 400));
    }

    private void setFullScreenCenter() {
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setLocation((int) (width - this.getWidth()) / 2, (int) (height - this.getHeight()) / 2);
    }


}

