package me.elabee.idea.drupal

import com.intellij.openapi.util.Iconable
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import javax.swing.Icon

fun VirtualFile.getIcon(psiManager: PsiManager): Icon? {
  if (isDirectory) {
    psiManager.findDirectory(this)?.let { return it.getIcon(Iconable.ICON_FLAG_VISIBILITY) }
  }

  return fileType.icon
}
