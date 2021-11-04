package com.yatoufang.ui;

import com.intellij.util.ui.EditableModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;


public class TemplateTableController extends AbstractTableModel implements EditableModel {

    private final String[] columnNames = {"Template Name"};

    private final ArrayList<String> rowData;


    public TemplateTableController(ArrayList<String> rowData) {
        this.rowData = rowData;
    }


    @Override
    public int getRowCount() {
        return rowData.size();
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
        return rowData.get(rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        rowData.set(rowIndex,(String)aValue);
        this.fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public void addRow() {
        rowData.add("");
        this.fireTableDataChanged();
    }

    @Override
    public void exchangeRows(int i, int i1) {
        String temp = rowData.get(i);
        rowData.set(i,rowData.get(i1));
        rowData.set(i1,temp);
    }

    @Override
    public boolean canExchangeRows(int i, int i1) {
        return true;
    }

    @Override
    public void removeRow(int i) {
        rowData.remove(i);
    }
}