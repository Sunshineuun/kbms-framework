/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ImageHelper.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.mortennobel.imagescaling.ResampleOp;

public final class ImageHelper {
	// ~ Static fields/initializers
	// =============================================

	/**
	 * RGB-Colorspace wird verwendet, alle anderen scheint der Jpeg-Encoder
	 * nicht zu moegen
	 */
	private static final ColorSpace RGB_COLOR_SPACE = ColorModel
			.getRGBdefault().getColorSpace();

	/** verwendete RenderingHints */
	private static final RenderingHints RENDERING_HINTS = new RenderingHints(
			RenderingHints.KEY_COLOR_RENDERING,
			RenderingHints.VALUE_COLOR_RENDER_SPEED);

	// Konstanten fuer Integer-Fixkommaarithmetik

	/** Binaere Nachkommastellen */
	private static final int INT_SHIFT = 6;

	/** Darstellung der Eins in Fixkommaarithmetik */
	private static final int INT_PRECISION = 1 << INT_SHIFT;

	/** Bitmaske zum Trennen von Integer-Anteil und Nachkommastellen */
	private static final int INT_MASK = INT_PRECISION - 1;

	public static final float THUMBNAIL_JPEG_QUALITY = 0.95f;

	// ~ Constructors
	// ===========================================================

	/** nicht instanzierbare Klasse */
	private ImageHelper() {
	}

	// ~ Methods
	// ================================================================

	/**
	 * Bild in RGB umwandeln
	 * 
	 * @param img
	 *            umzuwandelndes Bild
	 * @return RGB-Bild
	 */
	public static BufferedImage convertToRGB(BufferedImage img) {
		ColorConvertOp op = new ColorConvertOp(img.getColorModel()
				.getColorSpace(), RGB_COLOR_SPACE, RENDERING_HINTS);

		BufferedImage result = new BufferedImage(img.getWidth(), img
				.getHeight(), BufferedImage.TYPE_INT_RGB);

		op.filter(img, result);

		return result;
	}

	/**
	 * Bild verkleinern
	 * 
	 * @param img
	 *            Bild, von dem ein Thumbnail erzeugt werden soll
	 * @param thumbWidth
	 *            Breite des Thumbs, darf 0 sein fuer automatisch passenden Wert
	 * @param thumbHeight
	 *            Hoehe des Thumbs, darf 0 sein fuer automatisch passenden Wert
	 * @return Thumbnail
	 */
	public static BufferedImage createThumbnail(BufferedImage img,
			int thumbWidth, int thumbHeight) {
		int imageWidth = img.getWidth();
		int imageHeight = img.getHeight();

		// auf keinen fall vergroessern ;-)
		if (thumbWidth > imageWidth)
			thumbWidth = imageWidth;

		if (thumbHeight > imageHeight)
			thumbHeight = imageHeight;

		// Seitenverhaeltnisse bestimmen und ggf. anpassen
		float inRatio = (float) imageWidth / (float) imageHeight;

		if (thumbWidth == 0) {
			// keine breite angegeben
			if (thumbHeight != 0)
				thumbWidth = (int) (inRatio * thumbHeight);
			else {
				// gar nix angegeben -> default-werte nehmen
				thumbWidth = 30;
				thumbHeight = 30;
			}
		} else {
			if (thumbHeight == 0)

				// keine hoehe angegeben
				thumbHeight = (int) (thumbWidth / inRatio);
			else {
				// beides angegeben
				float outRatio = (float) thumbWidth / (float) thumbHeight;

				// nur bei einem merkbaren verhaeltnisunterschied beschneiden
				float diff = outRatio - inRatio;

				if (Math.abs(diff) > 0.1) {
					if (diff > 0) {
						// Originalbild zu hoch
						int newHeight = (int) (img.getWidth() / outRatio);
						int cut = (imageHeight - newHeight) / 2;
						img = img.getSubimage(0, cut, imageWidth, newHeight);
					} else {
						// Originalbild zu breit
						int newWidth = (int) (img.getHeight() * outRatio);
						int cut = (imageWidth - newWidth) / 2;
						img = img.getSubimage(cut, 0, newWidth, imageHeight);
					}
				}
			}
		}

		return resample(img, thumbWidth, thumbHeight);
	}

	/**
	 * Bild von Platte lesen
	 * 
	 * @param fileName
	 *            Dateiname des Bilds
	 * @return Bild
	 * @throws IOException
	 */
	public static BufferedImage loadImage(String fileName) throws IOException {
		File inputImage = new File(fileName);
		return loadImage(inputImage);
	}
	

	public static void reduceImg(File imgsrc, File imgdist, int widthdist,
			int heightdist) throws IOException {
		FileOutputStream out = null;
		try {
			if (!imgsrc.exists()) {
				throw new IOException(imgsrc.getAbsolutePath() + " is not exists .");
			}
			BufferedImage src = javax.imageio.ImageIO.read(imgsrc);

//			ResampleOp resampleOp = new ResampleOp((int)(src.getWidth()*0.5),(int)( src.getHeight()*0.5));// 转换  
			ResampleOp resampleOp = new ResampleOp(widthdist,heightdist);
			BufferedImage tag = resampleOp.filter(src,  
			                     null);  
		
//			tag.getGraphics().drawImage(
//					src.getScaledInstance(src.getWidth(), src.getHeight(),
//							Image.SCALE_AREA_AVERAGING), 0, 0, null);

			out = new FileOutputStream(imgdist);
			
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			//encoder.get
//			JPEGImageWriteParam param = new JPEGImageWriteParam();
//			JPEGEncodeParam param=encoder.getDefaultJPEGEncodeParam(tag);
//			JPEGImageWriteParam
//			//param.setDensityUnit(96);
//		    param.setQuality((float) 1.0, true);
//		    encoder.setJPEGEncodeParam(param);
//			
//			encoder.encode(tag);
//			ImageIO.w
//			out.close();
			
			JPEGImageWriteParam param = new JPEGImageWriteParam(Locale.getDefault());
			param.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(1.0f);
			ImageIO.write(tag, "JPEG", out);
			out.close();

		} catch (IOException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if(out != null){
				try{
					out.close();
				}catch(Exception e){
					
				}
			}
		}
		
		
	}

	public static BufferedImage loadImage(File inputImage) throws IOException {
		String ext = inputImage.getName().substring(
				inputImage.getName().lastIndexOf('.') + 1);
		Iterator<?> it = ImageIO.getImageReadersBySuffix(ext);

		while (it.hasNext()) {
			ImageReader reader = (ImageReader) it.next();
			reader.setInput(new FileImageInputStream(inputImage));

			try {
				return reader.read(0);
			} catch (Exception ignored) {
				ignored.printStackTrace();
				System.err.println(ignored.getMessage());

				return reader.readThumbnail(0, 0);
			}
		}

		return null;
	}

	public static boolean isCMYK(String filename) {
		boolean result = false;
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println(e.getMessage() + ": " + filename);
		}
		if (img != null) {
			int colorSpaceType = img.getColorModel().getColorSpace().getType();
			result = colorSpaceType == ColorSpace.TYPE_CMYK;
		}

		return result;
	}

	/**
	 * Change the extension of 'filename' to 'newExtension'.
	 * 
	 * @param filename
	 * @param newExtension
	 * @return filename with new extension
	 */
	private static String changeExtension(String filename, String newExtension) {
		String result = filename;
		if (filename != null && newExtension != null
				&& newExtension.length() != 0)
			;
		{
			int dot = filename.lastIndexOf('.');
			if (dot != -1) {
				result = filename.substring(0, dot) + '.' + newExtension;
			}
		}
		return result;
	}

	/**
	 * If 'filename' is a CMYK file, then convert the image into RGB, store it
	 * into a JPEG file, and return the new filename.
	 * 
	 * @param filename
	 */
	public static String cmyk2rgb(String filename) throws IOException {
		// Change this format into any ImageIO supported format.
		String format = "gif";
		File imageFile = new File(filename);
		String rgbFilename = filename;
		BufferedImage image = ImageIO.read(imageFile);
		if (image != null) {
			int colorSpaceType = image.getColorModel().getColorSpace()
					.getType();
			if (colorSpaceType == ColorSpace.TYPE_CMYK) {
				BufferedImage rgbImage = new BufferedImage(image.getWidth(),
						image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
				ColorConvertOp op = new ColorConvertOp(null);
				op.filter(image, rgbImage);

				rgbFilename = changeExtension(imageFile.getName(), format);
				rgbFilename = new File(imageFile.getParent(), format + "_"
						+ rgbFilename).getPath();
				ImageIO.write(rgbImage, format, new File(rgbFilename));
			}
		}
		return rgbFilename;
	}

	/**
	 * Speichert ein Bild
	 * 
	 * @param image
	 *            zu speicherndes Bild
	 * @param out
	 *            OutputStream in den das Bild gespeichert werden soll
	 * @param format
	 *            Dateiformat, z.b. "jpg" oder "png" etc.
	 * @param compression
	 *            gibt an, ob das Bild komprimiert werden soll
	 */
	public static void save(BufferedImage image, OutputStream out,
			String format, boolean compression) {
		try {
			Iterator<?> it = ImageIO.getImageWriters(
					new ImageTypeSpecifier(image), format);

			if (it.hasNext()) {
				ImageOutputStream imgout = ImageIO.createImageOutputStream(out);
				ImageWriter writer = (ImageWriter) it.next();
				writer.setOutput(imgout);

				ImageWriteParam param = writer.getDefaultWriteParam();

				if (compression) {
					param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
					param.setCompressionQuality(THUMBNAIL_JPEG_QUALITY);
				}

				IIOMetadata meta = writer.getDefaultStreamMetadata(param);

				try {
					writer.write(meta, new IIOImage(image, null, null), param);
				} catch (SocketException bloederIE) {
				}
				writer.dispose();
				imgout.flush();
				imgout.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param image
	 *            zu speicherndes Bild
	 * @param file
	 *            Dateiname, unter dem das Bild gespeichert werden soll
	 * @param format
	 *            Dateiformat, z.b. "jpg" oder "png" etc.
	 * @throws IOException
	 */
	static void save(BufferedImage image, String file, String format)
			throws IOException {
		FileOutputStream out = new FileOutputStream(file + "." + format);
		save(image, out, format, false);
		out.close();
	}

	static void save(BufferedImage image, File outFile, String format)
			throws IOException {
		FileOutputStream out = new FileOutputStream(outFile);
		save(image, out, format, false);
		out.close();
	}

	/** verkleinert ein Bild */
	public static BufferedImage resample(BufferedImage img,
			final int thumbWidth, final int thumbHeight) {

		final int imageWidth = img.getWidth();
		final int imageHeight = img.getHeight();

		BufferedImage thumb = img.getSubimage(0, 0, thumbWidth, thumbHeight);

		int sourceStartPreciseY;
		int sourceEndPreciseY;
		int sourceStartY;
		int sourceEndY;
		int sourceStartFractionY;
		int sourceEndFractionY;
		int sourceStartPreciseX;
		int sourceEndPreciseX;
		int sourceStartX;
		int sourceEndX;
		int sourceStartFractionX;
		int sourceEndFractionX;

		final int ih1 = imageHeight - 1;
		final int iw1 = imageWidth - 1;

		for (int y = 0; y < thumbHeight; y++) {
			sourceStartPreciseY = ((y * ih1) << INT_SHIFT) / thumbHeight;
			sourceEndPreciseY = (((y + 1) * ih1) << INT_SHIFT) / thumbHeight;
			sourceStartY = sourceStartPreciseY >> INT_SHIFT;
			sourceEndY = ImageHelper.min(ih1,
					(sourceEndPreciseY >> INT_SHIFT) + 1);
			sourceStartFractionY = INT_MASK - (sourceStartPreciseY & INT_MASK);
			sourceEndFractionY = sourceEndPreciseY & INT_MASK;

			for (int x = 0; x < thumbWidth; x++) {
				sourceStartPreciseX = ((x * iw1) << INT_SHIFT) / thumbWidth;
				sourceEndPreciseX = (((x + 1) * iw1) << INT_SHIFT) / thumbWidth;
				sourceStartX = sourceStartPreciseX >> INT_SHIFT;
				sourceEndX = ImageHelper.min(iw1,
						(sourceEndPreciseX >> INT_SHIFT) + 1);
				sourceStartFractionX = INT_MASK
						- (sourceStartPreciseX & INT_MASK);
				sourceEndFractionX = sourceEndPreciseX & INT_MASK;

				int r = 0;
				int g = 0;
				int b = 0;

				int totalWeight = 0;

				int weightX;
				int weightY;
				int weight;

				for (int v = sourceStartY; v <= sourceEndY; v++) {
					weightY = INT_PRECISION;

					if (v == sourceStartY)
						weightY = sourceStartFractionY;
					else {
						if (v == sourceEndY)
							weightY = sourceEndFractionY;
					}

					for (int u = sourceStartX; u <= sourceEndX; u++) {
						weightX = INT_PRECISION;

						if (u == sourceStartX)
							weightX = sourceStartFractionX;
						else {
							if (u == sourceEndX)
								weightX = sourceEndFractionX;
						}

						weight = (weightX * weightY);
						totalWeight += weight;

						final int c = img.getRGB(u, v);

						r += ((c & 0x000000FF) * weight);
						g += (((c & 0x0000FF00) >> 8) * weight);
						b += (((c & 0x00FF0000) >> 16) * weight);
					}
				}

				r = (r / totalWeight);
				g = (g / totalWeight);
				b = (b / totalWeight);
				thumb.setRGB(x, y, r | (g << 8) | (b << 16));
			}
		}

		return thumb;
	}
	
	private static int min(int i, int j) {
		return (i < j) ? i : j;
	}
}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */