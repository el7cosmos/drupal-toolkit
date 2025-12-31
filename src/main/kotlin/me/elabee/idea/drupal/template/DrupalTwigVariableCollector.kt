package me.elabee.idea.drupal.template

import com.intellij.util.indexing.FileBasedIndex
import com.jetbrains.php.PhpIndex
import fr.adrienbrault.idea.symfony2plugin.templating.variable.TwigFileVariableCollector
import fr.adrienbrault.idea.symfony2plugin.templating.variable.TwigFileVariableCollectorParameter
import fr.adrienbrault.idea.symfony2plugin.templating.variable.dict.PsiVariable
import me.elabee.idea.drupal.indexing.DrupalIndexIds

class DrupalTwigVariableCollector : TwigFileVariableCollector {
  override fun collect(parameter: TwigFileVariableCollectorParameter, variables: MutableMap<String, Set<String>>) {
    val twigFile = parameter.element.containingFile
    val component = (twigFile.parent ?: return).findFile("${twigFile.virtualFile.nameWithoutExtension}.component.yml") ?: return
    FileBasedIndex.getInstance().getFileData(DrupalIndexIds.component, component.virtualFile, parameter.project).forEach {
      it.value.slots.forEach { (name, type) -> variables[name] = setOf(type) }
      it.value.props.forEach { (name, type) -> variables[name] = setOf(type) }
    }
  }

  override fun collectPsiVariables(parameter: TwigFileVariableCollectorParameter, variables: MutableMap<String, PsiVariable>) {
    val phpIndex = PhpIndex.getInstance(parameter.project)

    val attributeVariable = PsiVariable()
    phpIndex.getClassesByFQN("\\Drupal\\Core\\Template\\Attribute").forEach {
      attributeVariable.addType(it.fqn)
      attributeVariable.addElements(it)
    }
    variables["attributes"] = attributeVariable

    val twigFile = parameter.element.containingFile
    (twigFile.parent ?: return).findFile("${twigFile.virtualFile.nameWithoutExtension}.component.yml") ?: return
    val metadataVariable = PsiVariable()
    phpIndex.getClassesByFQN("\\Drupal\\Core\\Theme\\Component\\ComponentMetadata").forEach {
      metadataVariable.addType(it.fqn)
      metadataVariable.addElements(it)
    }
    variables["componentMetadata"] = metadataVariable
  }
}
