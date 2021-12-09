package com.yatoufang.core;

import com.google.gson.*;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.yatoufang.entity.HttpState;
import com.yatoufang.entity.MyCookie;
import com.yatoufang.service.PersistentService;
import com.yatoufang.templet.Application;
import com.yatoufang.service.NotifyService;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * print http request result
 *
 * @author hse
 * @date 2021/3/29 0029
 */
public class HttpRequestResult {

    private final ConsoleView consoleView;

    public HttpRequestResult(HttpState httpState) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(Application.project).getToolWindow("com.yatoufang.http");
        consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(Application.project).getConsole();
        if (toolWindow == null){
            NotifyService.notifyError("My POST Console");
            return;
        }
        Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Http Test", true);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);
        toolWindow.activate(() -> printResult(httpState));
    }

    private void printResult(HttpState httpState) {
        try {
            HttpRequest.initRequest(httpState);
        } catch (Exception e) {
            String exceptionInfo = getExceptionInfo(e);
            printSelfInfo(httpState);
            consoleView.print(exceptionInfo, ConsoleViewContentType.ERROR_OUTPUT);
            return;
        }
        if (httpState.getResponseCode() > 0) {
            printHead(httpState);
            printMessage(httpState.getResponseBody());
            printAllHeads(httpState.getRequestHeads(), httpState.getResponseHeads());
            printOtherInfo(httpState);
        } else {
            consoleView.print(httpState.getResponseBody(), ConsoleViewContentType.ERROR_OUTPUT);
        }

        if (httpState.getResponseCode() == 200) {
            storeHostInfo(httpState);
        }
    }

    private void printSelfInfo(HttpState httpState) {
        consoleView.print(httpState.getMethod() + " " + httpState.getUrl() + "\n", ConsoleViewContentType.NORMAL_OUTPUT);
        printAllHeads(httpState.getRequestHeads(), httpState.getResponseHeads());
    }

    private void printOtherInfo(HttpState state) {
        if(state.getRequestCookies() != null && state.getRequestCookies().size() > 0){
            consoleView.print("\nRequest Cookies: \n   ", ConsoleViewContentType.NORMAL_OUTPUT);
            for (MyCookie cookie : state.getRequestCookies()) {
                consoleView.print(cookie.name + " " + cookie.value + "\n   ", ConsoleViewContentType.NORMAL_OUTPUT);
            }
        }

        if (state.getResponseCookies() != null && state.getResponseCookies().size()> 0) {
            consoleView.print("\nResponse Cookies: \n   ", ConsoleViewContentType.NORMAL_OUTPUT);
            for (Cookie cookie : state.getResponseCookies()) {
                consoleView.print(cookie.getName() + " " + cookie.getValue() + "\n   ", ConsoleViewContentType.NORMAL_OUTPUT);
            }
        }
        consoleView.print("\nURL:" + state.getUrl(), ConsoleViewContentType.NORMAL_OUTPUT);

        consoleView.scrollTo(0);
        System.out.println("state = " + state);
    }


    private void printHead(HttpState state) {
        if (state.getResponseCode() == 200) {
            consoleView.print("State: " + state.getResponseCode() + " " + state.getMethod() + " " +
                    state.getShortUrl() + " " + state.getResponseTime() + "ms\n\n", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        } else {
            consoleView.print("State: " + state.getResponseCode() + " " + state.getMethod() + " " +
                    state.getShortUrl() + " " + state.getResponseTime() + "ms" + "\n", ConsoleViewContentType.ERROR_OUTPUT);
        }
    }


    private void printMessage(String responseBody) {
        if (isJson(responseBody)) {
            responseBody = getFormatJson(responseBody);
        }
        consoleView.print(responseBody + "\n\n", ConsoleViewContentType.NORMAL_OUTPUT);
    }

    private void printAllHeads(Header[] requestHeads, Header[] responseHeads) {
        if (requestHeads != null && requestHeads.length > 0) {
            consoleView.print("Request Heads:\n   ", ConsoleViewContentType.NORMAL_OUTPUT);
            for (Header requestHead : requestHeads) {
                consoleView.print(requestHead.toString() + "\n   ", ConsoleViewContentType.NORMAL_OUTPUT);
            }
        }
        consoleView.print("\n", ConsoleViewContentType.NORMAL_OUTPUT);

        if (responseHeads != null && responseHeads.length > 0) {
            consoleView.print("Response Heads: \n   ", ConsoleViewContentType.NORMAL_OUTPUT);
            for (Header responseHead : responseHeads) {
                consoleView.print(responseHead.toString() + "\n   ", ConsoleViewContentType.NORMAL_OUTPUT);
            }
        }

    }

    /**
     * get all exception  info<p>
     *
     * </p>>
     * catch exception[Exception.getMessage()] may cause null point exception
     */
    public String getExceptionInfo(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String msg = out.toString();
        pout.close();
        try {
            out.close();
        } catch (Exception d) {
            d.printStackTrace();
        }
        return msg;
    }

    /**
     * Storage url and http request headers
     *
     * @param httpState httpState
     */
    private void storeHostInfo(HttpState httpState) {
        String hostInfo = PersistentService.getHostInfo();
        if (hostInfo == null) {
            String url = httpState.getUrl();
            int index = url.indexOf(httpState.getShortUrl());
            if (index > 0) {
                url = url.substring(0, index);
            }
            PersistentService.storeHostInfo(url);
        }
        storeHeads(httpState);
        storeCookies(httpState);
    }

    private void storeCookies(HttpState httpState) {
        if (httpState.getResponseCookies() != null && httpState.getResponseCookies().size() > 0) {
            List<MyCookie> list = new ArrayList<>();
            for (Cookie c : httpState.getResponseCookies()) {
                list.add(new MyCookie(c.getName(), c.getValue(), c.getComment(), c.getDomain(), c.getExpiryDate(), c.getPath(), c.isSecure(), c.getVersion()));
            }
            Gson gson = new Gson();
            String value = gson.toJson(list);
            PersistentService.storeCookies(value);
        }
    }

    private void storeHeads(HttpState state) {
        if (state.getHeads() != null && state.getHeads().length > 0) {
            Gson gson = new Gson();
            String value = gson.toJson(state.getHeads());
            PersistentService.storeHeaders(value);
        }
    }

    /**
     * get standard format Json String
     */
    public String getFormatJson(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    /**
     * Judge whether the String is a Json String
     *
     * @param jsonStr target String
     */
    public boolean isJson(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        return jsonElement.isJsonObject();
    }


    public static void main(String[] args) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();

    }

}
