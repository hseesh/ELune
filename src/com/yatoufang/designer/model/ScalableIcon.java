package com.yatoufang.designer.model;

import com.intellij.util.ui.UIUtil;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * @author GongHuang（hse）
 * @since 2022/3/24 0024
 */
public class ScalableIcon {

    public static final ScalableIcon FILE = new ScalableIcon(Images.PUSH);
    public static final int BASE_WIDTH = 16;
    public static final int BASE_HEIGHT = 16;
    private final Image baseImage;
    private final float baseScaleX;
    private final float baseScaleY;
    private double currentScaleFactor = -1.0d;
    private Image scaledCachedImage;

    public ScalableIcon(String name) {
        this.baseImage = loadStandardImage(name);;
        this.baseScaleX = (float) BASE_WIDTH / (float) this.baseImage.getWidth(null);
        this.baseScaleY = (float) BASE_HEIGHT / (float) this.baseImage.getHeight(null);
    }



    public static Image loadStandardImage(final String name) {
        InputStream resourceAsStream = ScalableIcon.class.getResourceAsStream(name);
        try {
            assert resourceAsStream != null;
            return ImageIO.read(resourceAsStream);
        } catch (Exception ex) {
            throw new Error("Can't load resource image " + name, ex); //NOI18N
        } finally {
            IOUtils.closeQuietly(resourceAsStream);
        }
    }

    public synchronized double getScaleFactor() {
        return this.currentScaleFactor;
    }

    public synchronized Image getImage(final double scale) {
        if (Double.compare(this.currentScaleFactor, scale) != 0) {
            this.scaledCachedImage = null;
        }

        if (this.scaledCachedImage == null) {
            this.scaledCachedImage = scaleImage(this.baseImage, this.baseScaleX, this.baseScaleY, scale);
            this.currentScaleFactor = scale;
        }
        return this.scaledCachedImage;
    }

    public Image getImage() {
        return baseImage;
    }

    public static Image scaleImage(final Image src, final double baseScaleX, final double baseScaleY, final double scale) {
        final int width = src.getWidth(null);
        final int height = src.getHeight(null);

        final int scaledW = (int) Math.round(width * baseScaleX * scale);
        final int scaledH = (int) Math.round(height * baseScaleY * scale);

        BufferedImage result = null;
        if (scaledH > 0 && scaledW > 0) {
            result = UIUtil.createImage(scaledW, scaledH, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g = (Graphics2D) result.getGraphics();
            g.drawImage(src, 0, 0, scaledW, scaledH, null);
            g.dispose();

        }
        return result;

    }
}
