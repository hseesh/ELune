package com.yatoufang.ui;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.table.JBTable;
import com.yatoufang.entity.HttpState;
import com.yatoufang.core.HttpService;
import com.yatoufang.templet.Application;
import com.yatoufang.templet.NotifyService;
import com.yatoufang.core.PersistentController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * http request module ui
 *
 * @author hse
 * @Date: 2021/1/25
 */
public class ExecuteDialog extends JComponent {


    private JComponent content;
    private ComboBox<String> requestType;
    private JTextField url;
    private HttpTableController paramModel;

    private final ArrayList<HttpState> stateList;


    public ExecuteDialog(ArrayList<HttpState> httpStates) {
        stateList = httpStates;
        initDrawPanel(httpStates.get(0));
    }


    private void initDrawPanel(HttpState httpState) {

        String[] listData = new String[]{"GET", "POST", "PUT", "DELETE"};
        // create ui component
        JSplitPane rootPane = new JSplitPane();

        JList<String> menu = new JBList<>();

        requestType = new ComboBox<>(listData);

        url = new JTextField();

        Box horizontalBox = Box.createHorizontalBox();

        JPanel contentPane = new JPanel(new BorderLayout());

        JPanel formData = new JPanel();
        JPanel clearDimension = new JPanel();
        JPanel buttonDimension = new JPanel();

        JTabbedPane bodyType = new JBTabbedPane();
        JBTabbedPane contentType = new JBTabbedPane();

        JButton execute = new JButton("execute");

        JButton selectFile = new JButton("Select File");

        JButton cLearCache = new JButton("CLear Cache");

        JBLabel cLearCacheTip = new JBLabel("Cookies、Headers、HostUrl");

        rootPane.setDividerLocation(150);
        rootPane.setDividerSize(2);

        clearDimension.setSize(40, 80);
        buttonDimension.setSize(40, 80);
        url.setSize(new Dimension(50, 100));

        // create 6 table(param,head,json,x-www-form-urlencoded,form_data,file)
        paramModel = new HttpTableController();
        HttpTableController jsonModel = new HttpTableController();
        HttpTableController urlencodedModel = new HttpTableController();
        HttpTableController headModel = new HttpTableController();
        HttpTableController formModel = new HttpTableController();
        HttpTableController fileModel = new HttpTableController();

        JBTable paramTable = new JBTable(paramModel);
        JBTable headsTable = new JBTable(headModel);
        JBTable formTable = new JBTable(formModel);
        JBTable fileTable = new JBTable(fileModel);
        JBTable jsonModelTable = new JBTable(jsonModel);
        JBTable urlencodedTable = new JBTable(urlencodedModel);

        //ui layout
        clearDimension.add(cLearCache);
        clearDimension.add(cLearCacheTip);
        buttonDimension.add(selectFile);

        formData.add(new JScrollPane(formTable));
        formData.add(new JScrollPane(fileTable));

        contentType.addTab("none ", new JLabel());
        contentType.addTab("application/json", new JScrollPane(jsonModelTable));
        contentType.addTab("x-www-form-urlencoded", new JScrollPane(urlencodedTable));
//        contentType.addTab("form—data", formData);
//        contentType.addTab("binary ", buttonDimension);

        bodyType.addTab("Params", new JScrollPane(paramTable));
        bodyType.addTab("Heads", new JScrollPane(headsTable));
        bodyType.addTab("Body", contentType);
        bodyType.addTab("Setting", clearDimension);

        contentPane.add(horizontalBox, BorderLayout.NORTH);
        contentPane.add(bodyType, BorderLayout.CENTER);
        contentPane.add(execute, BorderLayout.SOUTH);

        horizontalBox.add(requestType);
        horizontalBox.add(url);

        menu.setListData(getMenuData());
        rootPane.setLeftComponent(menu);
        rootPane.setRightComponent(contentPane);

        paramTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);


        // fill param
        if(httpState.getBodyType() == null){
            paramModel.setRowData(httpState.getParams());
        }else if("JSON".equals(httpState.getBodyType())){
            jsonModel.setRowData(httpState.getParams());
            bodyType.setSelectedIndex(2);
            contentType.setSelectedIndex(1);
        }else {
            paramModel.setRowData(httpState.getParams());
        }


        url.setText(httpState.getUrl());


        headModel.setRowData(httpState.getHeads());

        int requestIndex = getRequestIndex(httpState.getMethod());
        requestType.setSelectedIndex(requestIndex);

        // logical control
        execute.addActionListener(e -> new HttpService(httpState));

        cLearCache.addActionListener(e -> cLearAllCache());

        selectFile.addActionListener(e -> {
            String file = getSelectFile();
            if (!"".equals(file)) {
                httpState.setFile(new String[][]{{"file", file}});
            }
        });

        contentType.addChangeListener(e -> {
            switch (bodyType.getSelectedIndex()) {
                case 1:
                    httpState.setParams(jsonModel.getRowData());
                    httpState.setBodyType("JSON");
                    break;
                case 2:
                    httpState.setParams(urlencodedModel.getRowData());
                    httpState.setBodyType("urlencoded");
                    break;
                default:
                    httpState.setParams(paramModel.getRowData());
                    httpState.setBodyType("Null");
                    break;
            }
        });

        bodyType.addChangeListener(e -> {
            if (bodyType.getSelectedIndex() == 1) {
                httpState.setHeads(headModel.getRowData());
            }
        });

        paramModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (row == paramModel.getRowCount() - 1 && !"".equals(paramModel.getValueAt(row, 0))) {
                paramModel.addRow();
            }
            updateUrl();
            httpState.setUrl(url.getText());
        });

        headModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (row == headModel.getRowCount() - 1 && !"".equals(headModel.getValueAt(row, 0))) {
                headModel.addRow();
            }
        });

        jsonModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (row == jsonModel.getRowCount() - 1 && !"".equals(jsonModel.getValueAt(row, 0))) {
                jsonModel.addRow();
            }
        });

        formModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (row == formModel.getRowCount() - 1 && !"".equals(formModel.getValueAt(row, 0))) {
                formModel.addRow();
            }
        });

        fileModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 1) {
                String file = getSelectFile();
                fileModel.setValueAt(file, row, column);
            }
            if (row == fileModel.getRowCount() - 1 && !"".equals(fileModel.getValueAt(row, 1))) {
                fileModel.addRow();
            }
        });


        urlencodedModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            if (row == urlencodedModel.getRowCount() - 1 && !"".equals(urlencodedModel.getValueAt(row, 0))) {
                urlencodedModel.addRow();
            }
        });


        requestType.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectedIndex = requestType.getSelectedIndex();
                httpState.setMethod(getRequestType(selectedIndex));
            }
        });

        url.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = url.getText();
                httpState.setUrl(text);
                updateTable(text);
            }
        });

        this.content = rootPane;

    }

    private void cLearAllCache() {
        PersistentController.clearAll();
        NotifyService.notify("All cache cleared");
    }

    private void updateUrl() {
        String url = this.url.getText();
        int index = url.indexOf("?");
        if (index > 0) {
            url = url.substring(0, index);
        }
        String urlString = paramModel.getUrlString(url);
        this.url.setText(urlString);
    }

    public void updateTable(String url) {
        Object[][] parser = parser(url);
        if (parser.length == paramModel.getRowCount()) {
            parser = Arrays.copyOf(parser, parser.length + 1);
            parser[parser.length - 1] = new String[]{"", ""};
        }
        paramModel.setRowData(parser);
    }


    private int getRequestIndex(String type) {
        switch (type) {
            case "POST":
                return 1;
            case "PUT":
                return 2;
            case "DELETE":
                return 3;
            default:
                return 0;
        }
    }

    private String getRequestType(int index) {
        switch (index) {
            case 0:
                return "DELETE";
            case 1:
                return "POST";
            case 2:
                return "PUT";
            default:
                return "GET";
        }
    }

    /**
     * get the left menu data
     *
     * @return String[] menu data
     */
    private String[] getMenuData() {
        ArrayList<String> data = new ArrayList<>();
        for (HttpState method : this.stateList) {
            data.add(method.getDescription());
        }
        return data.toArray(new String[0]);
    }

    public JComponent getContent() {
        return content;
    }


    /**
     * url to params , for dynamic association url and params
     *
     * @param url url
     * @return String[][]
     */
    public Object[][] parser(String url) {
        String strUrlParams;
        if (url.contains("?")) {
            String[] strUrlPatten = url.split("\\?");
            strUrlParams = strUrlPatten[1];
        } else {
            strUrlParams = url;
        }

        String[] params;
        if (strUrlParams.contains("&")) {
            params = strUrlParams.split("&");
        } else {
            params = new String[]{strUrlParams};
        }
        Object[][] map = new String[params.length][2];
        for (int i = 0; i < params.length; i++) {
            if (params[i].contains("=")) {
                String[] param = params[i].split("=");
                if (param.length == 1) {
                    map[i][0] = param[0];
                    map[i][1] = "";
                } else {
                    map[i][0] = param[0];
                    map[i][1] = param[1];
                }
            }
        }
        return map;
    }


    private String getSelectFile() {
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true, false, true, true, false, false);
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(fileChooserDescriptor, this.content, Application.project, null);
        for (VirtualFile virtualFile : virtualFiles) {
            return virtualFile.getPath();
        }
        return "";
    }
}

