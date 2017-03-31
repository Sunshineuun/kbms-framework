/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: ListUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class ImageUtils {
	/**
	 * 图片水印
	 * 
	 * @param sourcePath
	 *            源图片
	 * @param pressImg
	 *            水印图片
	 * @param targetPath
	 *            目标图片
	 * @param x
	 *            修正值 默认在中间
	 * @param y
	 *            修正值 默认在中间
	 * @param alpha
	 *            透明度
	 * @throws IOException
	 */
	public final static void pressImage(String sourcePath, String pressImg,
			String targetPath, int x, int y, float alpha) throws IOException {
		pressImage(new File(sourcePath), new File(pressImg), new File(
				targetPath), x, y, alpha);
	}

	/**
	 * 图片水印
	 * 
	 * @param srcFile
	 *            源图片
	 * @param pressImg
	 *            水印图片
	 * @param targetImg
	 *            目标图片
	 * @param x
	 *            修正值 默认在中间
	 * @param y
	 *            修正值 默认在中间
	 * @param alpha
	 *            透明度
	 * @throws IOException
	 */
	public final static void pressImage(File srcFile, File pressImg,
			File targetImg, int x, int y, float alpha) throws IOException {
		Image src = ImageIO.read(srcFile);
		int srcWidth = src.getWidth(null);
		int srcHeight = src.getHeight(null);
		BufferedImage image = new BufferedImage(srcWidth, srcHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(src, 0, 0, srcWidth, srcHeight, null);
		// 水印文件
		Image press = ImageIO.read(pressImg);
		int pressWith = press.getWidth(null);
		int pressHeight = press.getHeight(null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		g
				.drawImage(press, (srcWidth - pressWith) / 2 + x,
						(srcHeight - pressHeight) / 2 + y, pressWith,
						pressHeight, null);
		// 水印文件结束
		g.dispose();

		if (!targetImg.exists())
			targetImg.createNewFile();
		String format = targetImg.getName().substring(
				targetImg.getName().indexOf(".") + 1);
		ImageIO.write((BufferedImage) image, format, targetImg);
	}

	/**
	 * 文字水印
	 * 
	 * @param sourcePath
	 *            源图片
	 * @param pressText
	 *            水印文字
	 * @param targetPath
	 *            目标图片
	 * @param fontName
	 *            字体名称
	 * @param fontStyle
	 *            字体样式
	 * @param color
	 *            字体颜色
	 * @param fontSize
	 *            字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度
	 * @throws IOException
	 */
	public static void pressText(String sourcePath, String pressText,
			String targetPath, String fontName, int fontStyle, Color color,
			int fontSize, int x, int y, float alpha) throws IOException {
		pressText(new File(sourcePath), pressText, new File(targetPath),
				fontName, fontStyle, color, fontSize, x, y, alpha);
	}

	/**
	 * 文字水印
	 * 
	 * @param srcFile
	 *            源图片
	 * @param pressText
	 *            水印文字
	 * @param targetImg
	 *            目标图片
	 * @param fontName
	 *            字体名称
	 * @param fontStyle
	 *            字体样式
	 * @param color
	 *            字体颜色
	 * @param fontSize
	 *            字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度
	 * @throws IOException
	 */
	public static void pressText(File srcFile, String pressText,
			File targetImg, String fontName, int fontStyle, Color color,
			int fontSize, int x, int y, float alpha) throws IOException {
		Image src = ImageIO.read(srcFile);
		int srcWidth = src.getWidth(null);
		int srcHeight = src.getHeight(null);
		BufferedImage image = new BufferedImage(srcWidth, srcHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(src, 0, 0, srcWidth, srcHeight, null);
		g.setColor(color);
		g.setFont(new Font(fontName, fontStyle, fontSize));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				alpha));
		g.drawString(pressText, (srcWidth - (getLength(pressText) * fontSize))
				/ 2 + x, (srcHeight - fontSize) / 2 + y);
		g.dispose();

		if (!targetImg.exists())
			targetImg.createNewFile();
		String format = targetImg.getName().substring(
				targetImg.getName().indexOf(".") + 1);
		ImageIO.write((BufferedImage) image, format, targetImg);
	}

	private static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}

	/**
	 * 缩放
	 * 
	 * @param filePath
	 *            源图片路径
	 * @param targetPath
	 *            目标图片路径
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param bb
	 *            比例不对时是否需要补白
	 * @throws IOException
	 */
	public static void resize(String filePath, String targetPath, int height,
			int width, boolean bb) throws IOException {
		resize(new File(filePath), new File(targetPath), height, width, bb);
	}

	/**
	 * 缩放
	 * 
	 * @param srcFile
	 *            源图片路径
	 * @param targetImg
	 *            目标图片路径
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param bb
	 *            比例不对时是否需要补白
	 * @throws IOException
	 */
	public static void resize(File srcFile, File targetImg, int height,
			int width, boolean bb) throws IOException {

		String format = targetImg.getName().substring(targetImg.getName().lastIndexOf(".") + 1).toLowerCase();
		if(format.equals("jpeg")){
			format = "jpg";
		}
		
		ImageHelper.reduceImg(srcFile,targetImg,width, height);
		
//		ImageHelper
//				.save(
//						ImageHelper
//								.resample(
//										ImageHelper
//												.loadImage(srcFile),
//												width, height), targetImg, format);

		/*
		 * double ratio = 0; // 缩放比例 BufferedImage bi = ImageIO.read(srcFile);
		 * Image itemp = bi.getScaledInstance(width, height,
		 * bi.SCALE_AREA_AVERAGING); // 计算比例 if ((bi.getHeight() > height) ||
		 * (bi.getWidth() > width)) { if (bi.getHeight() > bi.getWidth()) {
		 * ratio = (new Integer(height)).doubleValue() / bi.getHeight(); } else
		 * { ratio = (new Integer(width)).doubleValue() / bi.getWidth(); }
		 * AffineTransformOp op = new AffineTransformOp(AffineTransform
		 * .getScaleInstance(ratio, ratio), null); itemp = op.filter(bi, null);
		 * } if (bb) { BufferedImage image = new BufferedImage(width, height,
		 * BufferedImage.TYPE_INT_RGB); Graphics2D g = image.createGraphics();
		 * g.setColor(Color.white); g.fillRect(0, 0, width, height); if (width
		 * == itemp.getWidth(null)) g.drawImage(itemp, 0, (height -
		 * itemp.getHeight(null)) / 2, itemp.getWidth(null),
		 * itemp.getHeight(null), Color.white, null); else g.drawImage(itemp,
		 * (width - itemp.getWidth(null)) / 2, 0, itemp .getWidth(null),
		 * itemp.getHeight(null), Color.white, null); g.dispose(); itemp =
		 * image; }
		 * 
		 * if (!targetImg.exists()) targetImg.createNewFile(); String format =
		 * targetImg.getName().substring( targetImg.getName().indexOf(".") + 1);
		 * ImageIO.write((BufferedImage) itemp, format, targetImg);
		 */
	}
}


/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */