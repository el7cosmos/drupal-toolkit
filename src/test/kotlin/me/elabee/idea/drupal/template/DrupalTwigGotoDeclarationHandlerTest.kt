package me.elabee.idea.drupal.template

import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.testFramework.EditorTestUtil
import com.jetbrains.twig.TwigFile
import com.jetbrains.twig.TwigFileType
import me.elabee.idea.drupal.DrupalComponentTestCase

class DrupalTwigGotoDeclarationHandlerTest : DrupalComponentTestCase() {
    fun `test goto declaration without source element`() {
        val targets = DrupalTwigGotoDeclarationHandler().getGotoDeclarationTargets(null, 0, myFixture.editor)
        assertSize(0, targets)
    }

    fun `test goto declaration rejected element`() {
        val file = myFixture.configureByText(PlainTextFileType.INSTANCE, "{{ include('${EditorTestUtil.CARET_TAG}sdc_test:no-props') }}")
        val editor = myFixture.editor
        val offset = editor.caretModel.offset
        val targets = DrupalTwigGotoDeclarationHandler().getGotoDeclarationTargets(file.findElementAt(offset), offset, editor)
        assertSize(0, targets)
    }

    fun `test goto declaration non-component element`() {
        val file = myFixture.configureByText(TwigFileType.INSTANCE, "{{ include('${EditorTestUtil.CARET_TAG}@sdc_test/no-props') }}")
        val editor = myFixture.editor
        val offset = editor.caretModel.offset
        val targets = DrupalTwigGotoDeclarationHandler().getGotoDeclarationTargets(file.findElementAt(offset), offset, editor)
        assertSize(0, targets)
    }

    fun `test goto declaration`() {
        val file = myFixture.configureByText(TwigFileType.INSTANCE, "{{ include('${EditorTestUtil.CARET_TAG}sdc_test:no-props') }}")
        val editor = myFixture.editor
        val offset = editor.caretModel.offset
        val targets = DrupalTwigGotoDeclarationHandler().getGotoDeclarationTargets(file.findElementAt(offset), offset, editor)
        assertSize(1, targets)
        val twigFile = assertInstanceOf(targets.first(), TwigFile::class.java)
        assertEquals("no-props.twig", twigFile.name)
    }
}
