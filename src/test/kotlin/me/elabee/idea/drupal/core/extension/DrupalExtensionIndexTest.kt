package me.elabee.idea.drupal.core.extension

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import me.elabee.idea.drupal.DrupalTestCase
import me.elabee.idea.drupal.indexing.DrupalIndexIds

class DrupalExtensionIndexTest : DrupalTestCase() {
  fun `test extension index`() {
    myFixture.copyFileToProject("drupal/core/modules/system/system.info.yml")

    val fileBasedIndex = FileBasedIndex.getInstance()
    val values = fileBasedIndex.getValues(DrupalIndexIds.extension, "module", GlobalSearchScope.allScope(project))
    assertNotEmpty(values)
    assertContainsElements(values, "system")
  }
}
