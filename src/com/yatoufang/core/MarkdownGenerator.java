package com.yatoufang.core;

import com.yatoufang.entity.Param;
import com.yatoufang.entity.TcpMethod;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Auther: hse
 * @Date: 2021/1/23
 * <p>
 * Markdown template
 */
public class MarkdownGenerator {

    private final StringBuilder content;

    public MarkdownGenerator() {
        content = new StringBuilder();
    }

    public void addTitle(String contents, int level) {
        content.append(getFontSize(level));
        content.append(contents);
        content.append("\n");
        newLine();
    }

    public void addContent(String contents) {
        content.append("**");
        content.append(contents);
        content.append("**\n");
    }

    public void addURL(String url) {
        content.append("**URL**: ");
        content.append(url);
        content.append("\n");
    }


    public void addRequestExample(String url) {
        content.append("**Request-Example:** \n");
        content.append(url);
        content.append("\n");
    }

    public void addTcpRequestExample(String contents) {
        content.append("***Request-Example:*** \n");
        content.append("```json\n");
        content.append(contents);
        content.append("\n```");
    }

    public void addResponseExample(String contents) {
        content.append("**Response-Example:** \n");
        content.append("```json\n");
        content.append(contents);
        content.append("\n```");
    }

    public void newLine() {
        content.append("    \n");
    }

    public void addTableHead() {
        content.append("| Parameter | Type | Required | Description |\n");
        content.append("| :----: | :----: | :----: | :----: |\n");
    }

    public void addTableRow(String row) {
        content.append(row);
        content.append("\n");
    }

    public void addResponseTable() {
        content.append("| Field | Type | Description |\n");
        content.append("| :----: | :----: | :----: |\n");
    }

    private String getFontSize(int level) {
        String str = "";
        switch (level) {
            case 1:
                str = "# ";
                break;
            case 2:
                str = "## ";
                break;
            case 3:
                str = "### ";
                break;
            case 4:
                str = "#### ";
                break;
            case 5:
                str = "##### ";
                break;
            case 6:
                str = "###### ";
                break;
            default:
                break;
        }
        return str;
    }

    public String getContent() {
        return content.toString();
    }

    public static void main(String[] args) {
        MarkdownGenerator markdownGenerator = new MarkdownGenerator();
        markdownGenerator.addTitle("it is a title", 2);
        markdownGenerator.newLine();
        markdownGenerator.addResponseExample("empty");
        System.out.println(markdownGenerator.getContent());

    }

    public void build(ArrayList<TcpMethod> methods, String fileName) {
        int index = 1;
        addTitle(fileName, 1);
        for (TcpMethod method : methods) {
            addTitle(index++ + "." + method.getMethodName(),3);
            Collection<Param> params = method.getParams();
            if(params.size() > 0){
                addTableHead();
                for (Param param : params) {
                    addTableRow(param.toString());
                }
                newLine();
            }
            addTcpRequestExample(method.toString());
            newLine();
        }
    }
}
