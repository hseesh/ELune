package com.yatoufang.ui;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;

public class HttpTableController extends AbstractTableModel {

    private final String[] columnNames = {"Key", "Value"};

    private Object[][] rowData = {{"", ""}};


    public HttpTableController() {

    }

    public void setRowData(Object[][] data) {
        if (data != null) {
            this.rowData = data;
            this.fireTableDataChanged();
        }
    }

    /**
     * param to url
     *
     * @return http url
     */
    public String getUrlString(String url) {
        StringBuilder builder = new StringBuilder(url);
         for (int i = 0; i < rowData.length; i++) {
            if (rowData[i][0] != null && !"".equals(rowData[i][0])) {
                if(i == 0){
                    builder.append("?");
                }
                builder.append(rowData[i][0]);
                builder.append("=");
                if (rowData[i][1] != null && !"".equals(rowData[i][1])) {
                    builder.append(rowData[i][1]);
                }
                if ((i != 0 || i != rowData.length - 1) && i + 1 < rowData.length && rowData[i + 1][0] != null && !"".equals(rowData[i + 1][0])) {
                    builder.append("&");
                }
            }
        }
        return builder.toString();
    }

    /**
     *  add empty row
     */
    public void addRow() {
        this.rowData = Arrays.copyOf(rowData, rowData.length + 1);
        rowData[rowData.length - 1] = new String[]{"", ""};
        this.fireTableDataChanged();
    }

    public void addRow(Object key, Object value) {
        this.rowData = Arrays.copyOf(rowData, rowData.length + 1);
        rowData[rowData.length - 1] = new Object[]{key, value};
        this.fireTableDataChanged();
    }

    public Object[][] getRowData() {
        return rowData;
    }

    @Override
    public int getRowCount() {
        return rowData.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowData[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        rowData[rowIndex][columnIndex] = aValue;
        this.fireTableCellUpdated(rowIndex, columnIndex);
    }

}