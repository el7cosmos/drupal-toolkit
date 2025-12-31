package me.elabee.idea.drupal.lang.psi

import com.intellij.psi.PsiManager
import com.jetbrains.php.lang.psi.PhpFile
import me.elabee.idea.drupal.DrupalTestCase

class DrupalPredefinedVariableProviderTest : DrupalTestCase() {
  fun `test arbitrary predefined variable`() {
    val provider = DrupalPredefinedVariableProvider()
    val virtualFile = myFixture.copyFileToProject("drupal/index.php")
    val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
    assertNotNull(psiFile)
    val phpFile = assertInstanceOf(psiFile, PhpFile::class.java)
    assertEmpty(provider.getPredefinedVariables(phpFile))
  }

  fun `test settings predefined variables`() {
    val provider = DrupalPredefinedVariableProvider()
    val virtualFile = myFixture.copyFileToProject("drupal/sites/default/default.settings.php", "drupal/sites/default/settings.php")
    val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
    assertNotNull(psiFile)
    val phpFile = assertInstanceOf(psiFile, PhpFile::class.java)
    assertEquals(setOf("app_root", "site_path"), provider.getPredefinedVariables(phpFile))
  }
}
