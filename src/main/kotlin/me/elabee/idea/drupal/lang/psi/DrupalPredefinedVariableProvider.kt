package me.elabee.idea.drupal.lang.psi

import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.PhpPredefinedVariableProvider

class DrupalPredefinedVariableProvider : PhpPredefinedVariableProvider {
  override fun getPredefinedVariables(file: PhpFile): MutableSet<CharSequence> {
    if (file.virtualFile.nameWithoutExtension.contains("settings")) {
      return mutableSetOf("app_root", "site_path")
    }

    return mutableSetOf()
  }
}
