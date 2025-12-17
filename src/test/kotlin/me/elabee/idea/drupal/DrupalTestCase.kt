package me.elabee.idea.drupal

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.drupal.DrupalVersion
import com.jetbrains.php.drupal.settings.DrupalDataService

abstract class DrupalTestCase : BasePlatformTestCase() {
    override fun setUp() {
        super.setUp()

        DrupalDataService.getInstance(project).state = DrupalDataService.State(
            true,
            "${testDataPath}drupal",
            DrupalVersion.NINE.number,
            false,
            false,
        )
    }

    override fun getTestDataPath(): String {
        return "src/test/testData/"
    }
}
