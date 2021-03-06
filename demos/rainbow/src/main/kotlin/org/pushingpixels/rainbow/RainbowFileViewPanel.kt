/*
 * Copyright (c) 2005-2018 Rainbow Kirill Grouchnikov 
 * and Alexander Potochkin. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *    
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *    
 *  o Neither the name of Rainbow, Kirill Grouchnikov 
 *    and Alexander Potochkin nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.rainbow

import org.pushingpixels.flamingo.api.bcb.JBreadcrumbBar
import org.pushingpixels.flamingo.api.common.*
import org.pushingpixels.flamingo.api.layout.TransitionLayoutManager
import org.pushingpixels.photon.icon.SvgBatikResizableIcon
import org.pushingpixels.neon.icon.ResizableIcon
import java.awt.Dimension
import java.io.InputStream
import javax.swing.SwingUtilities

/**
 * Panel that hosts SVG-based gallery buttons.
 *
 * @author Kirill Grouchnikov
 */
class RainbowFileViewPanel<T>(private val bar: JBreadcrumbBar<T>, startingDimension: Int) :
        AbstractFileViewPanel<T>(startingDimension) {
    init {
        TransitionLayoutManager.getInstance().track(this, true)
    }

    override fun configureCommandButton(leaf: AbstractFileViewPanel.Leaf,
            button: JCommandButton, icon: ResizableIcon) {
        button.setActionRichTooltip(RichTooltip.RichTooltipBuilder().setTitle("Transcode")
                .addDescriptionSection("Click to generate Java2D class").build())
        button.addActionListener {
            SwingUtilities.invokeLater {
                // can't pass the stream contents since the
                // input can be .svgz
                val svgIcon = icon as SvgBatikResizableIcon
                RainbowUtils.processSvgButtonClick(svgIcon.svgBytes, leaf.leafName)
            }
        }
    }

    override fun toShowFile(pair: StringValuePair<T>): Boolean {
        val name = pair.key
        return name.endsWith(".svg") || name.endsWith(".svgz")
    }

    override fun getLeafContent(leaf: T): java.io.InputStream? {
        return bar.callback.getLeafContent(leaf)
    }

    override fun getResizableIcon(leaf: AbstractFileViewPanel.Leaf, stream: InputStream,
            state: CommandButtonDisplayState, dimension: Dimension): ResizableIcon? {
        val name = leaf.leafName
        return if (name.endsWith(".svg"))
            SvgBatikResizableIcon.getSvgIcon(leaf.leafStream, dimension)
        else
            SvgBatikResizableIcon.getSvgzIcon(leaf.leafStream, dimension)
    }
}
