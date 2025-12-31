package me.elabee.idea.drupal.template

import fr.adrienbrault.idea.symfony2plugin.Settings
import me.elabee.idea.drupal.DrupalTestCase

class DrupalTwigVariableCollectorTest : DrupalTestCase() {
  override fun setUp() {
    super.setUp()

    Settings.getInstance(project).pluginEnabled = true

    myFixture.copyFileToProject("drupal/core/lib/Drupal/Core/Template/Attribute.php")
    myFixture.copyFileToProject("drupal/core/lib/Drupal/Core/Theme/Component/ComponentMetadata.php")
    myFixture.copyDirectoryToProject(
      "template/foo",
      "drupal/modules/custom/foo",
    )
  }

  fun `test variable collector`() {
    myFixture.configureByFile("drupal/modules/custom/foo/components/bar/bar.twig")
    myFixture.completeBasic()
    assertNotNull(myFixture.lookupElementStrings)
    assertContainsElements(myFixture.lookupElementStrings!!, "attributes", "componentMetadata", "baz")
  }
}
