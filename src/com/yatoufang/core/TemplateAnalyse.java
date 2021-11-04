package com.yatoufang.core;


import com.intellij.psi.PsiType;
import com.yatoufang.entity.TemplateMethod;

public class TemplateAnalyse {


    private final TemplateMethod method;
    private final String result;

    public TemplateAnalyse(String template, TemplateMethod method) {
        this.method = method;
        template = "@@{requestMethod}(\"/@{methodName}\")\n" +
                "@ApiOperation(\" \")\n" +
                "public @{returnType} @{methodName}(@{loop(#@@{contentType} @{properties.type} @{properties},#)}) {\n" +

                "        Page<@{entity}> page = new Page<>(pageNumber, pageSize);\n" +
                "        QueryWrapper<@{entity}> queryWrapper = new QueryWrapper<>();\n" +
                "        queryWrapper\n" +
                "@{      loop(#        .eq(@{properties} != null, \"@{properties.alias}\", @{properties})#)};" +
                "        IPage<@{entity}> pageList = incomeService.page(page, queryWrapper);\n" +
                "        return R.ok().put(\"data\", pageList.getRecords()).put(\"total\", pageList.getTotal());" +
                "\n}";

        this.result = action(template.toCharArray(), 0);
    }

    private String action(char[] chars, int index) {

        StringBuilder builder = new StringBuilder();

        int[] jumpLength = new int[1];
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '@' && i + 1 < chars.length && chars[i + 1] == '{') {
                jumpLength[0] = 0;
                builder.append(decode(fetchWords(chars, jumpLength, i + 2), index));
                i += jumpLength[0] + 2;
            } else {
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }

    public String fetchWords(char[] chars, int[] jumpLength, int index) {
        StringBuilder builder = new StringBuilder();
        int functionCounter = 0;
        for (int i = index; i < chars.length; i++) {
            if (chars[i] == '(' && i + 1 < chars.length && chars[i + 1] == '#') {
                functionCounter++;
            }
            if (chars[i] == '#' && i + 1 < chars.length && chars[i + 1] == ')') {
                functionCounter--;
                jumpLength[0]++;
                builder.append(chars[i]);
            } else if (chars[i] == '}') {
                if (functionCounter > 0) {
                    jumpLength[0]++;
                    builder.append(chars[i]);
                } else {
                    return builder.toString();
                }
            } else {
                jumpLength[0]++;
                builder.append(chars[i]);
            }
        }
        System.out.println("builder = " + builder.toString());
        return builder.toString();
    }


    public String decode(String instruction, int index) {

        System.out.println("instruction = " + instruction);
        char[] chars = instruction.toCharArray();
        int count = 0;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(' && i + 1 < chars.length && chars[i + 1] == '#') {
                count++;
            }
        }

        if (count == 0) {
            return getValue(instruction, index);
        }

        int orderCount = 0;
        int expressionCount = 0;
        boolean isOrder = true;
        String[] orders = new String[count];
        String[] expression = new String[count];
        StringBuilder result = new StringBuilder();
        StringBuilder ordersBuilder = new StringBuilder();
        StringBuilder expressionBuilder = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(' && i + 1 < chars.length && chars[i + 1] == '#') {
                isOrder = false;
                i++;
                orders[orderCount++] = ordersBuilder.toString();
                ordersBuilder.delete(0, ordersBuilder.length());
                continue;
            }

            if (chars[i] == '#' && i + 1 < chars.length && chars[i + 1] == ')') {
                isOrder = true;
                i++;
                expression[expressionCount++] = expressionBuilder.toString();
                expressionBuilder.delete(0, expressionBuilder.length());
                continue;
            }

            if (isOrder) {
                ordersBuilder.append(chars[i]);
            } else {
                expressionBuilder.append(chars[i]);
            }
        }

        for (String order : orders) {
            System.out.println("order = " + order);
        }


        for (int i = orders.length - 1; i > -1; i--) {
            StringBuilder retraction = new StringBuilder();
            for (char c : expression[i].toCharArray()) {
                if (c == ' ') {
                    retraction.append(" ");
                }
            }
            orders[i] = orders[i].trim();
            switch (orders[i]) {
                case "if":
                    break;
                case "loop":
                    for (int j = 0; j < method.getParams().size(); j++) {
                        result.append(retraction.toString());
                        result.append(action(expression[i].toCharArray(), j));
                        if (j == method.getParams().size() - 1) {
                            if (result.lastIndexOf(",") == result.length() - 1) {
                                result.deleteCharAt(result.length() - 1);
                            }
                        } else {
                            result.append("\n");
                        }

                    }
                    break;
                case "count":
                    break;
                case "contain":
                    break;
                case "startWith":
                    break;
                case "endWith":
                    break;

            }
        }
        return result.toString();
    }

    public String getValue(String args, int index) {
        switch (args) {
            case "methodName":
                return method.getName();
            case "returnType":
                return method.getReturnType();
            case "contentType":
                return method.getContentType();
            case "requestMethod":
                return method.getRequestMethod();
            case "entity":
                return method.getEntity().get(0);
            case "service":
                return method.getService().get(0);
            case "properties":
                return method.getParams().get(index).getName();
            case "properties.alias":
                return method.getParams().get(index).getAlias();
            case "properties.type":
                String returnType = "";
                PsiType type = method.getParams().get(index).getType();
                if (type != null) {
                    returnType = type.getPresentableText();
                }
                returnType = returnType.length() == 0 ? method.getParams().get(index).getValue() : returnType;
                return returnType;
        }
        return args;

    }

    public String getResult() {
        return result;
    }
}
