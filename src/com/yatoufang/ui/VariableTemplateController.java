package com.yatoufang.ui;

import com.intellij.util.ui.EditableModel;
import com.yatoufang.entity.Param;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


public class VariableTemplateController extends AbstractTableModel implements EditableModel {

    private final String[] columnNames = {"paramName", "type", "required", "defaultValue", "description"};

    private final List<Param> params;


    public VariableTemplateController(List<Param> params) {
        this.params = params;
    }


    @Override
    public int getRowCount() {
        return params.size();
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
        Param param = params.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return param.getName() == null ? "" : param.getName();
            case 1:
                return param.getTypeAlias() == null ? "" : param.getTypeAlias();
            case 2:
                return param.getRequired() == null ? "" : false;
            case 3:
                return param.getDefaultValue() == null ? "" : param.getDefaultValue();
            case 4:
                return param.getDescription() == null ? "" : param.getDescription();
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        aValue = aValue == null ? "" : aValue;
        switch (columnIndex) {
            case 0:
                params.get(rowIndex).setName(String.valueOf(aValue));
            case 1:
                params.get(rowIndex).setTypeAlias(String.valueOf(aValue));
            case 2:
                aValue = String.valueOf(aValue).length() == 0 ? "false" : aValue;
                params.get(rowIndex).setRequired(Boolean.valueOf(String.valueOf(aValue)));
            case 3:
                params.get(rowIndex).setValue(String.valueOf(aValue));
            case 4:
                params.get(rowIndex).setValue(String.valueOf(aValue));
        }
        this.fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public void addRow() {
        Param param = new Param("");
        param.setRequired(false);
        param.setTypeAlias("");
        param.setDefaultValue("");
        param.setDescription("");
        params.add(param);
        this.fireTableDataChanged();
    }

    @Override
    public void exchangeRows(int i, int i1) {

    }

    @Override
    public boolean canExchangeRows(int i, int i1) {
        return false;
    }

    @Override
    public void removeRow(int i) {
        params.remove(i);
    }

    public List<Param> getParams() {
        return params;
    }
}