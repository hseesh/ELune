package com.yatoufang.complete;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.yatoufang.complete.model.CompletionCache;
import com.yatoufang.templet.Application;
import icons.Icon;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author hse
 * @since 2023/1/23 0023
 */
public class EluneCompletionContributor extends CompletionContributor {

    private static final CompletionCache completionCache = new CompletionCache();

    public EluneCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new EluneCompletionProvider());
    }

    public static void suggestCompletion(Project project, Editor editor, Document doc, String text, int offset, String triggerPoint) {
        String verifyToken = "hse";
        completionCache.setPredictions(new String[]{text});
        completionCache.setOffset(offset);
        completionCache.setVerifyToken(verifyToken);
        //completionCache.setTimeoutSupplier(() -> checkCodeChanges(project, verifyToken, null, offset, doc));
        completionCache.setEmpty(false);

        ApplicationManager.getApplication().invokeLater(() -> {
            CodeCompletionHandlerBase handler = CodeCompletionHandlerBase.createHandler(CompletionType.BASIC, false, false, false);
            handler.invokeCompletion(project, editor, 0, false);
        }, ModalityState.current());
    }


    private static class EluneCompletionProvider extends CompletionProvider<CompletionParameters> {

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
            if (completionCache.isEmpty()) {
                return;
            }

            // Need to add all elements in one go to avoid split
            ArrayList<LookupElement> elements = new ArrayList<>();
            for (String prediction : completionCache.getPredictions()) {
                elements.add(prioritize(LookupElementBuilder.create(prediction).withIcon(Icon.NODE_RETURN).withInsertHandler((cxt, item) -> {
                    String insertion = item.getLookupString();
                    String[] lines = cxt.getDocument().getText().substring(completionCache.getOffset()).split("\n");
                    String line = lines.length == 0 ? "" : lines[0];

                    if (!insertion.equals(line)) {
                        if (line.endsWith(")") || line.endsWith("}") || line.endsWith("]") || line.endsWith(">")) {
                            cxt.getDocument().deleteString(completionCache.getOffset() + insertion.length(), completionCache.getOffset() + insertion.length() + 1);
                        }
                    }
                }).withTypeText(Application.PROJECT_NAME)));
            }
            result.addAllElements(elements);

            completionCache.setEmpty(true);
        }
    }

    private static LookupElement prioritize(LookupElement element) {
        return PrioritizedLookupElement.withGrouping(PrioritizedLookupElement.withExplicitProximity(PrioritizedLookupElement.withPriority(element, Double.MAX_VALUE - 1), Integer.MAX_VALUE - 1), Integer.MAX_VALUE - 1);
    }


}
