package me.elabee.idea.drupal.theme

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.ResolveResult
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext
import com.intellij.util.indexing.FileBasedIndex
import com.jetbrains.php.lang.parser.PhpElementTypes
import com.jetbrains.php.lang.patterns.PhpPatterns
import com.jetbrains.php.lang.psi.elements.ArrayHashElement
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import me.elabee.idea.drupal.getIcon
import me.elabee.idea.drupal.indexing.DrupalIndexIds

class DrupalComponentReferenceProvider : PsiReferenceProvider() {
  companion object {
    val PATTERN = PhpPatterns.phpLiteralExpression().withParent(
      PhpPatterns.phpElement().withElementType(PhpElementTypes.ARRAY_VALUE).withParent(
        PhpPatterns.phpElement().withElementType(PhpElementTypes.HASH_ARRAY_ELEMENT).with(HashKey("#component")),
      ),
    )
  }

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference?> {
    val stringLiteral = element as? StringLiteralExpression ?: return PsiReference.EMPTY_ARRAY

    if (stringLiteral.contents.isEmpty()) {
      return PsiReference.EMPTY_ARRAY
    }

    // Create reference with text range (excluding quotes)
    val range = TextRange(1, stringLiteral.textLength - 1)
    return arrayOf(DrupalComponentReference(stringLiteral, range))
  }

  private class DrupalComponentReference(element: StringLiteralExpression, textRange: TextRange) :
    PsiPolyVariantReferenceBase<StringLiteralExpression>(element, textRange),
    PsiPolyVariantReference {
    private val componentKey: String get() = element.contents

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
      val project = element.project
      val psiManager = PsiManager.getInstance(project)
      val index = FileBasedIndex.getInstance()

      // Find files that contain this component key
      val files = mutableListOf<ResolveResult>()

      index.processFilesContainingAllKeys(
        DrupalIndexIds.component,
        listOf(componentKey),
        GlobalSearchScope.allScope(project),
        null,
      ) { yamlFile ->
        psiManager.findFile(yamlFile)?.let { psiFile ->
          files.add(PsiElementResolveResult(psiFile))
        }

        val componentDir = yamlFile.parent
        val componentName = yamlFile.nameWithoutExtension.removeSuffix(".component")
        val twigFile = componentDir?.findChild("$componentName.twig") ?: return@processFilesContainingAllKeys true

        psiManager.findFile(twigFile)?.let { psiFile ->
          files.add(PsiElementResolveResult(psiFile))
        }

        true
      }
      return files.toTypedArray()
    }

    override fun getVariants(): Array<Any> {
      val project = element.project
      val projectDir = project.guessProjectDir() ?: return emptyArray()
      val variants = mutableListOf<LookupElementBuilder>()
      val index = FileBasedIndex.getInstance()
      val psiManager = PsiManager.getInstance(project)

      index.processAllKeys(
        DrupalIndexIds.component,
        { key ->
          index.getFilesWithKey(
            DrupalIndexIds.component,
            setOf(key),
            {
              val path = VfsUtil.getRelativePath(it.parent, projectDir)
              psiManager.findDirectory(it.parent) ?: return@getFilesWithKey true
              variants.add(
                LookupElementBuilder
                  .create(key)
                  .withTailText(path)
                  .withIcon(it.parent.getIcon(psiManager))
                  .bold(),
              )
            },
            GlobalSearchScope.allScope(project),
          )
        },
        project,
      )

      return variants.toTypedArray()
    }
  }

  private class HashKey(private val keyName: String) : PatternCondition<ArrayHashElement>("hashKey") {
    override fun accepts(element: ArrayHashElement, context: ProcessingContext?): Boolean =
      (element.key as? StringLiteralExpression)?.contents == keyName
  }
}
