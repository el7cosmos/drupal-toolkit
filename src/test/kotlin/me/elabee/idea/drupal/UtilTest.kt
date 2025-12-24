package me.elabee.idea.drupal

import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.psi.PsiManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class UtilTest : BasePlatformTestCase() {
    fun testVirtualFileIcon() {
        myFixture.configureByText(PlainTextFileType.INSTANCE, "foo")
        assertEquals(PlainTextFileType.INSTANCE.icon, myFixture.file.virtualFile.getIcon(PsiManager.getInstance(project)))
    }
}
