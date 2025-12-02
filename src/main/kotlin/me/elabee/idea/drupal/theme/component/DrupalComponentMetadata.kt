package me.elabee.idea.drupal.theme.component

data class DrupalComponentMetadata(
    val slots: Map<String, String>,
    val props: Map<String, String>,
)
