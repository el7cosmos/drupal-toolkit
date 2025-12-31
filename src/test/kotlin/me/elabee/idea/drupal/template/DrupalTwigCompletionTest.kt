package me.elabee.idea.drupal.template

import com.intellij.testFramework.EditorTestUtil
import com.jetbrains.twig.TwigFileType
import me.elabee.idea.drupal.DrupalComponentTestCase

class DrupalTwigCompletionTest : DrupalComponentTestCase() {
  fun `test component completion`() {
    myFixture.configureByText(TwigFileType.INSTANCE, "{{ include('${EditorTestUtil.CARET_TAG}') }}")
    myFixture.completeBasic()
    assertContainsElements(myFixture.lookupElementStrings!!, COMPONENTS)
  }
}
