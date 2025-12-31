package me.elabee.idea.drupal.template

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.jetbrains.twig.elements.TwigElementTypes
import fr.adrienbrault.idea.symfony2plugin.templating.TwigPattern

class DrupalTwigCompletionContributor : CompletionContributor() {
  init {
    extend(
      CompletionType.BASIC,
      PlatformPatterns.or(
        TwigPattern.getTemplateFileReferenceTagPattern(),
        TwigPattern.getTagTernaryPattern(TwigElementTypes.EXTENDS_TAG),
        TwigPattern.getPrintBlockOrTagFunctionPattern("include", "source"),
        TwigPattern.getIncludeTagArrayPattern(),
        TwigPattern.getTagTernaryPattern(TwigElementTypes.INCLUDE_TAG),
      ),
      DrupalTwigCompletionProvider(),
    )
  }
}
