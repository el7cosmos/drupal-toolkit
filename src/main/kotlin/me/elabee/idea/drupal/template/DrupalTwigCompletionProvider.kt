package me.elabee.idea.drupal.template

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.ModificationTracker
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.util.ProcessingContext
import com.intellij.util.Processor
import com.intellij.util.indexing.FileBasedIndex
import fr.adrienbrault.idea.symfony2plugin.templating.TemplateLookupElement
import me.elabee.idea.drupal.getIcon
import me.elabee.idea.drupal.indexing.DrupalIndexIds

class DrupalTwigCompletionProvider : CompletionProvider<CompletionParameters>() {
    companion object {
        val key: Key<CachedValue<Set<TemplateLookupElement>>> = Key("DRUPAL_TWIG_COMPONENT_CACHE")
    }

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet,
    ) {
        result.addAllElements(getLookupElementsFromIndex(parameters.position.project))
    }

    private fun getLookupElementsFromIndex(project: Project): Set<TemplateLookupElement> {
        val projectDir = project.guessProjectDir() ?: return emptySet()
        val psiManager = PsiManager.getInstance(project)

        return CachedValuesManager.getManager(project).getCachedValue(
            project,
            key,
            {
                val index = FileBasedIndex.getInstance()
                val elements = mutableSetOf<TemplateLookupElement>()

                index.processAllKeys(
                    DrupalIndexIds.component,
                    { name ->
                        index.processFilesContainingAllKeys(
                            DrupalIndexIds.component,
                            setOf(name),
                            GlobalSearchScope.allScope(project),
                            null,
                            Processor {
                                elements.add(SdcLookupElement(name, it.parent, projectDir, psiManager))
                                true
                            },
                        )
                        true
                    },
                    project,
                )

                CachedValueProvider.Result.create(
                    elements,
                    ModificationTracker { index.getIndexModificationStamp(DrupalIndexIds.component, project) },
                )
            },
            false,
        )
    }

    private class SdcLookupElement : TemplateLookupElement {
        private var psiManager: PsiManager
        private var virtualFile: VirtualFile

        constructor(templateName: String, virtualFile: VirtualFile, projectBaseDir: VirtualFile, psiManager: PsiManager) : super(
            templateName,
            virtualFile,
            projectBaseDir,
            true,
        ) {
            this.psiManager = psiManager
            this.virtualFile = virtualFile
        }

        override fun renderElement(presentation: LookupElementPresentation) {
            super.renderElement(presentation)
            presentation.icon = virtualFile.getIcon(psiManager)
        }
    }
}
