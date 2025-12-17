package me.elabee.idea.drupal.theme

import com.intellij.util.indexing.FileBasedIndex
import me.elabee.idea.drupal.DrupalComponentTestCase
import me.elabee.idea.drupal.indexing.DrupalIndexIds

class DrupalComponentIndexTest : DrupalComponentTestCase() {
    fun `test component index`() {
        val fileBasedIndex = FileBasedIndex.getInstance()
        val keys = fileBasedIndex.getAllKeys(DrupalIndexIds.component, project)
        assertNotEmpty(keys)
        assertContainsElements(keys, COMPONENTS)
    }
}
