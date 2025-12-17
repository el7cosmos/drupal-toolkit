package me.elabee.idea.drupal.theme

import com.intellij.testFramework.EditorTestUtil
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.twig.TwigFile
import me.elabee.idea.drupal.DrupalComponentTestCase

class DrupalComponentReferenceProviderTest : DrupalComponentTestCase() {
    fun `test completion`() {
        myFixture.configureByText(PhpFileType.INSTANCE, "<?php ['#component' => '${EditorTestUtil.CARET_TAG}'];")
        myFixture.completeBasic()
        assertNotEmpty(myFixture.lookupElementStrings)
        assertContainsElements(myFixture.lookupElementStrings!!, COMPONENTS)
    }

    fun `test reference`() {
        myFixture.configureByText(PhpFileType.INSTANCE, "<?php ['#component' => 'sdc_test:array-to-object${EditorTestUtil.CARET_TAG}'];")
        val referenceAtCaret = myFixture.getReferenceAtCaretPositionWithAssertion()
        val file = assertInstanceOf(referenceAtCaret.resolve(), TwigFile::class.java)
        assertEquals("array-to-object.twig", file.name)
    }
}
