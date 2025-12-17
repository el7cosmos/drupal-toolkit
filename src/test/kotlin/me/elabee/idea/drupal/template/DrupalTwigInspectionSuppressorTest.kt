package me.elabee.idea.drupal.template

import com.intellij.testFramework.EditorTestUtil
import com.jetbrains.twig.TwigFileType
import me.elabee.idea.drupal.DrupalComponentTestCase

class DrupalTwigInspectionSuppressorTest : DrupalComponentTestCase() {
    override fun setUp() {
        super.setUp()
    }

    fun `test is suppressed for arbitrary id`() {
        myFixture.configureByText(TwigFileType.INSTANCE, "{{ ${EditorTestUtil.CARET_TAG} }}")
        assertFalse(
            DrupalTwigInspectionSuppressor().isSuppressedFor(
                myFixture.file.findElementAt(myFixture.editor.caretModel.offset)!!,
                "foo",
            ),
        )
    }

    fun `test is suppressed for rejected element`() {
        myFixture.configureByText(TwigFileType.INSTANCE, "{{ ${EditorTestUtil.CARET_TAG} }}")
        assertFalse(
            DrupalTwigInspectionSuppressor().isSuppressedFor(
                myFixture.file.findElementAt(myFixture.editor.caretModel.offset)!!,
                "TwigTemplateMissingInspection",
            ),
        )
    }

    fun `test is suppressed for`() {
        myFixture.configureByText(TwigFileType.INSTANCE, "{% extends '${EditorTestUtil.CARET_TAG}sdc_test:no-props' %}")
        assertTrue(
            DrupalTwigInspectionSuppressor().isSuppressedFor(
                myFixture.file.findElementAt(myFixture.editor.caretModel.offset)!!,
                "TwigTemplateMissingInspection",
            ),
        )
    }

    fun testGetSuppressActions() {
        assertEmpty(DrupalTwigInspectionSuppressor().getSuppressActions(null, "TwigTemplateMissingInspection"))
    }
}
