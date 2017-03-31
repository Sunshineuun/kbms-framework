/*
 * ================================================================
 * 基干系统：上海金仕达卫宁软件有限公司 Web::Java DB基干系统 (Patrasche 3.0)
 * 
 * 业务系统：框架基盘
 * 
 * 
 * $Id: CompressFileUtils.java,v 1.6 2016/05/16 09:00:00 yuanfeng Exp $
 * ================================================================
 */
package com.winning.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 
 * @author gang.liu
 * @date 2013-7-31
 */
public class CompressFileUtils {
	private static final int BUFFEREDSIZE = 10 * 1024;

	public static void zip(String inputFilename, String zipFilename) throws IOException {
		zip(new File(inputFilename), zipFilename);
	}

	public static void zip(File inputFile, String zipFilename) throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
		try {
			zip(new File[] { inputFile }, out, null);
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void zip(File[] inputFiles, String zipFilename) throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
		try {
			zip(inputFiles, out, null);
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	private static void zip(File[] inputFiles, ZipOutputStream out, String base) throws IOException {
		for (int i = 0; i < inputFiles.length; i++) {
			if (i > 0)
				base = inputFiles[i].getName();
			zip(inputFiles[i], out, base);
		}
	}

	public static void zip(File inputFile, ZipOutputStream out, String base) throws IOException {
		if (inputFile.isDirectory()) {
			File[] listFile = inputFile.listFiles();
			out.putNextEntry(new ZipEntry(StringUtils.defaultIfEmpty(base, "") + "/"));
			base = StringUtils.isEmpty(base) ? inputFile.getName() + "/" : base + "/";
			for (int i = 0; i < listFile.length; i++) {
				zip(new File[] { listFile[i] }, out, base + listFile[i].getName());
			}
		} else {
			if (StringUtils.isNotEmpty(base)) {
				out.putNextEntry(new ZipEntry(base));
			} else {
				out.putNextEntry(new ZipEntry(inputFile.getName()));
			}

			FileInputStream in = new FileInputStream(inputFile);
			try {
				int len;
				byte[] by = new byte[BUFFEREDSIZE];
				while ((len = in.read(by)) != -1) {
					out.write(by, 0, len);
				}
			} catch (IOException e) {
				throw e;
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
	}

	public static void unzip(String zipFilename, String outputDirectory) throws IOException {
		File file;
		ZipFile zipFile = null;
		InputStream input = null;
		FileOutputStream output = null;
		try {
			zipFile = new ZipFile(zipFilename);
			for (Enumeration<?> entries = zipFile.getEntries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				file = new File(outputDirectory, entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}

					try {
						input = zipFile.getInputStream(entry);
						output = new FileOutputStream(file);
						byte[] bt = new byte[BUFFEREDSIZE];
						int c;
						while ((c = input.read(bt)) != -1) {
							output.write(bt, 0, c);
						}
					} catch (IOException e) {
						throw e;
					} finally {
						IOUtils.closeQuietly(output);
						IOUtils.closeQuietly(input);
					}
				}
			}
		} finally {
			if (zipFile != null)
				zipFile.close();
		}
	}

	public static void unzip(InputStream is, String outputDirectory) throws IOException {
		FileOutputStream fos = null;
		File tmpFile = new File(getOutputDirectoryPath(outputDirectory) + RandomUIDUtils.getUUID() + ".zip");
		try {
			fos = new FileOutputStream(tmpFile);
			int c;
			byte[] by = new byte[BUFFEREDSIZE];
			while ((c = is.read(by)) != -1) {
				fos.write(by, 0, c);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(fos);
		}

		try {
			unzip(tmpFile.getPath(), outputDirectory);
		} finally {
			if (tmpFile.exists())
				tmpFile.delete();
		}
	}

	private static String getOutputDirectoryPath(String outputDirectory) {
		if (outputDirectory.endsWith("/") || outputDirectory.endsWith("\\"))
			return outputDirectory;

		return outputDirectory + File.separator;
	}

}

/* Copyright (C) 2016, 上海金仕达卫宁软件科技有限公司 Project, All Rights Reserved. */