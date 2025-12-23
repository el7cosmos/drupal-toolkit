package me.elabee.idea.drupal.theme

import com.intellij.psi.PsiPolyVariantReference
import com.intellij.testFramework.EditorTestUtil
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.twig.TwigFile
import me.elabee.idea.drupal.DrupalComponentTestCase
import org.jetbrains.yaml.psi.YAMLFile

class DrupalComponentReferenceProviderTest : DrupalComponentTestCase() {
    fun `test completion`() {
        myFixture.configureByText(PhpFileType.INSTANCE, "<?php ['#component' => '${EditorTestUtil.CARET_TAG}'];")
        myFixture.completeBasic()
        assertNotEmpty(myFixture.lookupElementStrings)
        assertContainsElements(myFixture.lookupElementStrings!!, COMPONENTS)
    }

    fun `test reference`() {
        myFixture.configureByText(PhpFileType.INSTANCE, "<?php ['#component' => 'sdc_test:array-to-object${EditorTestUtil.CARET_TAG}'];")
        val referenceAtCaret = assertInstanceOf(myFixture.getReferenceAtCaretPositionWithAssertion(), PsiPolyVariantReference::class.java)
        val references = referenceAtCaret.multiResolve(true)
        assertSize(2, references)
        val yamlFile = assertInstanceOf(references.first().element, YAMLFile::class.java)
        assertEquals("array-to-object.component.yml", yamlFile.name)
        val twigFile = assertInstanceOf(references.last().element, TwigFile::class.java)
        assertEquals("array-to-object.twig", twigFile.name)
    }
}
