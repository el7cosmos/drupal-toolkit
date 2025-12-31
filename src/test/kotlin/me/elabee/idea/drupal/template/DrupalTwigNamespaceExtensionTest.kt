package me.elabee.idea.drupal.template

import fr.adrienbrault.idea.symfony2plugin.extension.TwigNamespaceExtensionParameter
import fr.adrienbrault.idea.symfony2plugin.templating.path.TwigPath
import fr.adrienbrault.idea.symfony2plugin.templating.util.TwigUtil
import me.elabee.idea.drupal.DrupalTestCase

class DrupalTwigNamespaceExtensionTest : DrupalTestCase() {
  fun `test namespace extension`() {
    myFixture.copyDirectoryToProject(
      "drupal/core/modules/system/tests/themes/test_theme",
      "drupal/core/modules/system/tests/themes/test_theme",
    )
    val namespaces = DrupalTwigNamespaceExtension().getNamespaces(TwigNamespaceExtensionParameter(project))
    assertSize(1, namespaces)
    val twigPath = assertInstanceOf(namespaces.first(), TwigPath::class.java)
    assertEquals("test_theme", twigPath.namespace)
    assertEquals("/src/drupal/core/modules/system/tests/themes/test_theme/templates", twigPath.path)
    assertEquals(TwigUtil.NamespaceType.ADD_PATH, twigPath.namespaceType)
  }
}
